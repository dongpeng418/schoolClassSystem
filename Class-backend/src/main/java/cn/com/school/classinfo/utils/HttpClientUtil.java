package cn.com.school.classinfo.utils;

import cn.com.school.classinfo.common.Pair;
import cn.com.school.classinfo.exception.TransferApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * http client util
 * @author hpt, dongpp
 */
@Slf4j
public class HttpClientUtil {

    private static SSLConnectionSocketFactory SSLSF;
    private static final CloseableHttpClient HTTP_CLIENT;
    private static final RequestConfig DEFAULT_REQUEST_CONFIG;

    static {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true);
            SSLSF = new SSLConnectionSocketFactory(builder.build(),
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.error("httpClient ssl config error.", e);
        }
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", SSLSF)
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(1000);
        cm.setDefaultMaxPerRoute(100);
        DEFAULT_REQUEST_CONFIG = RequestConfig
                .custom()
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(4 * 1000)
                .setSocketTimeout(10 * 1000)
                .build();
        HTTP_CLIENT = HttpClients
                .custom()
                .setMaxConnPerRoute(100)
                .setMaxConnTotal(200)
                .setSSLSocketFactory(SSLSF)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG)
                .build();
    }

    public static Pair<Boolean, String> requestByHttpGet(String url, Map<String, String> params) {
        return requestByHttpGet(url, params, 0);
    }

    public static Pair<Boolean, String> requestByHttpGet(String url, Map<String, String> params, int soTimeout) {
        long begin = System.currentTimeMillis();
        HttpGet get = null;
        HttpEntity entity = null;
        try {
            get = new HttpGet(appendParams(url, params));
            if (soTimeout > 0) {
                get.setConfig(RequestConfig.copy(DEFAULT_REQUEST_CONFIG).setSocketTimeout(soTimeout).build());
            }
            HttpResponse response = HTTP_CLIENT.execute(get);
            entity = response.getEntity();
            String content = EntityUtils.toString(entity, "UTF-8");
            int statusCode = response.getStatusLine().getStatusCode();
            boolean isOk = false;
            if (statusCode >= 200 && statusCode < 300) {
                isOk = true;
            }
            return Pair.of(isOk, content);
        } catch (Exception e) {
            if (get != null) {
                get.abort();
            }
            log.error("request error, url:" + url, e);
            throw getException(e);
        } finally {
            closeEntity(entity);
            if(Objects.nonNull(get)){
                get.releaseConnection();
            }
            long ts = System.currentTimeMillis() - begin;
            log.info("request url:{}, ts:{}, params:{}", url, ts, params);
        }
    }

    public static InputStream requestStreamByHttpGet(String url, Map<String, String> params, int soTimeout) {
        long begin = System.currentTimeMillis();
        HttpGet get = null;
        HttpEntity entity = null;
        try {
            get = new HttpGet(appendParams(url, params));
            if (soTimeout > 0) {
                get.setConfig(RequestConfig.copy(DEFAULT_REQUEST_CONFIG).setSocketTimeout(soTimeout).build());
            }
            HttpResponse response = HTTP_CLIENT.execute(get);
            entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                return new ByteArrayInputStream(EntityUtils.toByteArray(entity));
            } else {
                EntityUtils.consume(entity);
            }
            return null;
        } catch (Exception e) {
            if (get != null) {
                get.abort();
            }
            log.error("request error, url:" + url, e);
            throw getException(e);
        } finally {
            closeEntity(entity);
            if(Objects.nonNull(get)){
                get.releaseConnection();
            }
            long ts = System.currentTimeMillis() - begin;
            log.info("request url:{}, ts:{}, params:{}", url, ts, params);
        }
    }

    public static Pair<Boolean, String> requestByHttpPost(String url, Map<String, String> params) {
        return requestByHttpPost(url, params, 0);
    }

    public static Pair<Boolean, String> requestByHttpPost(String url, Map<String, String> params, int soTimeout) {
        long begin = System.currentTimeMillis();
        HttpPost post = new HttpPost(url);
        if (soTimeout > 0) {
            post.setConfig(RequestConfig.copy(DEFAULT_REQUEST_CONFIG).setSocketTimeout(soTimeout).build());
        }
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        for (Entry<String, String> entry : params.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        HttpEntity entity = null;
        try {
            HttpResponse response = HTTP_CLIENT.execute(post);
            entity = response.getEntity();
            String content = EntityUtils.toString(entity, "UTF-8");
            int statusCode = response.getStatusLine().getStatusCode();
            boolean isOk = false;
            if (statusCode >= 200 && statusCode < 300) {
                isOk = true;
            }
            return Pair.of(isOk, content);
        } catch (Exception e) {
            post.abort();
            log.error("request error, url:" + url, e);
            throw getException(e);
        } finally {
            closeEntity(entity);
            post.releaseConnection();
            long ts = System.currentTimeMillis() - begin;
            log.info("request url:{}, ts:{}, params:{}", url, ts, params);
        }
    }

    public static Pair<Boolean, String> requestWithMultipartByHttpPost(String url, Map<String, ContentBody> params,
                                                                       int soTimeout) {
        long begin = System.currentTimeMillis();
        HttpPost post = new HttpPost(url);
        if (soTimeout > 0) {
            post.setConfig(RequestConfig.copy(DEFAULT_REQUEST_CONFIG).setSocketTimeout(soTimeout).build());
        }

        HttpEntity entity = null;
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (Entry<String, ContentBody> entry : params.entrySet()) {
                multipartEntityBuilder.addPart(entry.getKey(), entry.getValue());
            }
            HttpEntity reqEntity = multipartEntityBuilder.build();
            post.setEntity(reqEntity);
            HttpResponse response = HTTP_CLIENT.execute(post);
            entity = response.getEntity();
            String content = EntityUtils.toString(entity, "UTF-8");
            int statusCode = response.getStatusLine().getStatusCode();
            boolean isOk = false;
            if (statusCode >= 200 && statusCode < 300) {
                isOk = true;
            }
            return Pair.of(isOk, content);
        } catch (Exception e) {
            post.abort();
            log.error("request error, url:" + url, e);
            throw getException(e);
        } finally {
            closeEntity(entity);
            post.releaseConnection();
            long ts = System.currentTimeMillis() - begin;
            log.info("request url:{}, ts:{}", url, ts);
        }
    }

    public static Pair<Boolean, String> requestWithBodyByHttpPost(String url, Map<String, String> params,
                                                                  String jsonBody, int soTimeout) {
        long begin = System.currentTimeMillis();
        HttpPost post = new HttpPost(appendParams(url, params));
        if (soTimeout > 0) {
            post.setConfig(RequestConfig.copy(DEFAULT_REQUEST_CONFIG).setSocketTimeout(soTimeout).build());
        }
        post.setHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity postEntity = new StringEntity(jsonBody, Charset.forName("UTF-8"));
        postEntity.setContentEncoding("UTF-8");
        postEntity.setContentType("application/json");
        post.setEntity(postEntity);

        HttpEntity entity = null;
        try {
            HttpResponse response = HTTP_CLIENT.execute(post);
            entity = response.getEntity();
            String content = EntityUtils.toString(entity, "UTF-8");
            int statusCode = response.getStatusLine().getStatusCode();
            boolean isOk = false;
            if (statusCode >= 200 && statusCode < 300) {
                isOk = true;
            }
            return Pair.of(isOk, content);
        } catch (Exception e) {
            post.abort();
            log.error("request error, url:" + url, e);
            throw getException(e);
        } finally {
            closeEntity(entity);
            post.releaseConnection();
            long ts = System.currentTimeMillis() - begin;
            log.info("request url:{}, ts:{}, params:{}", url, ts, params);
        }
    }

    public static Pair<Boolean, String> requestWithJsonBodyByHttpPut(String url, Map<String, String> params,
                                                                     String jsonBody) {
        return requestWithJsonBodyByHttpPut(url, params, jsonBody, 0);
    }

    public static Pair<Boolean, String> requestWithJsonBodyByHttpPut(String url, Map<String, String> params,
                                                                     String jsonBody, int soTimeout) {
        long begin = System.currentTimeMillis();
        HttpPut put = new HttpPut(appendParams(url, params));
        if (soTimeout > 0) {
            put.setConfig(RequestConfig.copy(DEFAULT_REQUEST_CONFIG).setSocketTimeout(soTimeout).build());
        }
        if (StringUtils.isNotBlank(jsonBody)) {
            put.setHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity postEntity = new StringEntity(jsonBody, Charset.forName("UTF-8"));
            postEntity.setContentEncoding("UTF-8");
            postEntity.setContentType("application/json");
            put.setEntity(postEntity);
        }
        HttpEntity entity = null;
        try {
            HttpResponse response = HTTP_CLIENT.execute(put);
            entity = response.getEntity();
            String content = EntityUtils.toString(entity, "UTF-8");
            int statusCode = response.getStatusLine().getStatusCode();
            boolean isOk = false;
            if (statusCode >= 200 && statusCode < 300) {
                isOk = true;
            }
            return Pair.of(isOk, content);
        } catch (Exception e) {
            put.abort();
            log.error("request error, url:" + url, e);
            throw getException(e);
        } finally {
            closeEntity(entity);
            put.releaseConnection();
            long ts = System.currentTimeMillis() - begin;
            log.info("request url:{}, ts:{}, params:{}", url, ts, params);
        }
    }

    private static void closeEntity(HttpEntity entity) {
        if (entity != null) {
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                log.warn("close entity fail", e);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        HTTP_CLIENT.close();
    }

    private static String appendParams(String url, Map<String, String> params) {

        if (MapUtils.isEmpty(params)) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (!url.contains("?")) {
            sb.append("?");
        } else {
            sb.append("&");
        }
        for (Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(encode(entry.getValue())).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static String encode(String str) {
        try {
            return java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static TransferApiException getException(Exception e) {
        String errorMessage = "connect transfer api error";
        if (e instanceof UnknownHostException) {
            errorMessage = "connect transfer api host error";
        } else if (e instanceof SocketTimeoutException) {
            errorMessage = "connect transfer api time out";
        }else if(e instanceof ConnectionPoolTimeoutException){
            errorMessage = "connection pool time out";
        }
        return new TransferApiException(errorMessage);
    }

}
