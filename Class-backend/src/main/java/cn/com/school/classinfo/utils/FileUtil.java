package cn.com.school.classinfo.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件工具类
 *
 * @author dongpp
 * @date 2018/10/26
 */
@Slf4j
public class FileUtil {
    /**
     * linux 和 windows 的文件分隔符
     */
    private static final String LP = "/", WP = "\\";

    /**
     * 根据文件名称获取路径
     * 例
     * 输入 aaa.txt
     * 输出 2018/10/26/{uuid}.txt
     *
     * @param fileName 文件名称
     * @return 转换后相对路径
     */
    public static String genRelativePath(String fileName) {
        StringBuilder filePath = new StringBuilder();
        String extension = FilenameUtils.getExtension(fileName);
        String date = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd");
        filePath.append(date)
        .append(File.separator)
        .append(UUID.randomUUID().toString())
        .append(".")
        .append(extension);
        return filePath.toString();
    }

    /**
     * 根据扩展名称获取路径
     * 例
     * 输入 txt
     * 输出 2018/10/26/{uuid}.txt
     *
     * @param ext 扩展名
     * @return 转换后相对路径
     */
    public static String genRelativePathByExt(String ext) {
        StringBuilder filePath = new StringBuilder();
        String date = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd");
        filePath.append(date)
        .append(File.separator)
        .append(UUID.randomUUID().toString())
        .append(".")
        .append(ext);
        return filePath.toString();
    }

    /**
     * 根据原文件名来生成UUID文件名
     *
     * @param fileName 原文件名
     * @return UUID文件名
     */
    public static String genUuidFileName(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * 创建新文件
     *
     * @param file 需要创建的文件
     * @return 是否成功
     */
    public static boolean createFile(File file) {
        if (file.exists()) {
            return true;
        }
        boolean success = true;
        try {
            if (!file.getParentFile().mkdirs()) {
                log.warn("file mkdirs failed. file path: {}", file.getParentFile().getAbsolutePath());
            }
            if (!file.createNewFile()) {
                log.warn("file create failed. file path: {}", file.getAbsolutePath());
            }
        } catch (IOException e) {
            log.error("file create error! ", e);
            success = false;
        }
        return success;
    }

    /**
     * 根据文件路径创建新文件
     *
     * @param filePath 文件路径
     * @return 创建后的文件对象
     */
    public static File createFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        File file = new File(filePath);
        return createFile(file) ? file : null;
    }

    /**
     * 根据文件路径创建新文件
     *
     * @param filePath 文件路径
     */
    public static void deleteFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return;
        }
        File file = new File(filePath);
        deleteFile(file);
    }

    /**
     * 根据文件路径创建新文件
     *
     * @param file 需要删除的文件
     */
    public static void deleteFile(File file) {
        if (Objects.isNull(file)) {
            return;
        }
        if (file.isDirectory()) {
            log.warn("file is directory can't be delete. file path: {}", file.getAbsolutePath());
            return;
        }
        if (!file.delete()) {
            log.warn("file delete failed. file path: {}", file.getAbsolutePath());
        }
    }

    /**
     * 根据文件路径创建新目录
     *
     * @param filePath 文件路径
     * @return 创建后的文件对象
     */
    public static File createDir(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            if (!file.mkdirs()) {
                log.warn("file mkdirs failed. file path: {}", filePath);
            }
        } else {
            if (!file.getParentFile().mkdirs()) {
                log.warn("file mkdirs failed. file path: {}", file.getParentFile().getAbsolutePath());
            }
        }
        return file;
    }

    /**
     * 将多个文件路径拼接成一个
     * exam:
     * 传参： a/b/c, d, e, f
     * 返回：a/b/c/d/e/f/
     *
     * @param subPaths 需要拼接的子路径
     * @return 拼接后的文件路径
     */
    public static String stitchingPath(String... subPaths) {
        if (subPaths.length == 0) {
            return "";
        }
        StringBuilder paths = new StringBuilder();
        String subPath;
        for (int i = 0; i < subPaths.length; i++) {
            //清理子路径的文件分隔符，防止拼接后有重复分隔符的情况
            //第一个路径不清除首部文件分隔符，防止绝对路径变相对路径
            subPath = i == 0 ? cleanPathEndSeparator(subPaths[i]) : cleanPathSeparator(subPaths[i]);
            if (StringUtils.isBlank(subPath)) {
                continue;
            }
            paths.append(subPath).append(File.separator);
        }
        return paths.toString();
    }

    /**
     * 清除文件路径两端的分隔符
     * exam:
     * 参数：/a/
     * 返回：a
     *
     * @param path 文件路径
     * @return 处理后的路径
     */
    public static String cleanPathSeparator(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        return cleanPathEndSeparator(cleanPathStartSeparator(path));
    }

    /**
     * 清除文件路径尾端的分隔符
     * exam:
     * 参数：/a/
     * 返回：/a
     *
     * @param path 文件路径
     * @return 处理后的路径
     */
    private static String cleanPathEndSeparator(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        if (path.endsWith(LP) || path.endsWith(WP)) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * 清除文件路径首端的分隔符
     * exam:
     * 参数：/a/
     * 返回：a/
     *
     * @param path 文件路径
     * @return 处理后的路径
     */
    private static String cleanPathStartSeparator(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        if (path.startsWith(LP) || path.startsWith(WP)) {
            path = path.substring(1);
        }
        return path;
    }

    /**
     * <pre>
     * 浏览器下载文件时需要在服务端给出下载的文件名，当文件名是ASCII字符时没有问题
     * 当文件名有非ASCII字符时就有可能出现乱码
     *
     * 这里的实现方式参考这篇文章
     * http://blog.robotshell.org/2012/deal-with-http-header-encoding-for-file-download/
     *
     * 最终设置的response header是这样:
     *
     * Content-Disposition: attachment;
     *                      filename="encoded_text";
     *                      filename*=utf-8''encoded_text
     *
     * 其中encoded_text是经过RFC 3986的“百分号URL编码”规则处理过的文件名
     * </pre>
     *
     * @param filename 文件名称
     * @return headerValue
     */
    public static String getFileDownloadHeader(String filename) {
        String headerValue = "attachment;";
        headerValue += " filename=\"" + encodeURIComponent(filename) + "\";";
        headerValue += " filename*=utf-8''" + encodeURIComponent(filename);
        return headerValue;
    }

    /**
     * <pre>
     * 符合 RFC 3986 标准的“百分号URL编码”
     * 在这个方法里，空格会被编码成%20，而不是+
     * 和浏览器的encodeURIComponent行为一致
     * </pre>
     *
     * @param value 需要转码的字符串
     * @return 转码后的字符串
     */
    public static String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据文件名称获取对应的content-type
     *
     * @param fileName 文件名称
     * @return content-type
     */
    public static MediaType getMediaType(String fileName) {
        String ext = FilenameUtils.getExtension(fileName).toUpperCase();
        switch (ext) {
            case "PNG":
                return MediaType.IMAGE_PNG;
            case "JPG":
            case "JPEG":
                return MediaType.IMAGE_JPEG;
            case "GIF":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
