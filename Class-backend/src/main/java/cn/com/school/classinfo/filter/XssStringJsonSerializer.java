package cn.com.school.classinfo.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

/**
 * 过滤JSON数据
 *
 * @author dongpp
 * @date 2018-12-27
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isNotBlank(s)) {
            String encodedValue = StringEscapeUtils.escapeHtml4(s);
            jsonGenerator.writeString(encodedValue.replace("&middot;", "·"));
        }else{
            jsonGenerator.writeString(s);
        }
    }
}
