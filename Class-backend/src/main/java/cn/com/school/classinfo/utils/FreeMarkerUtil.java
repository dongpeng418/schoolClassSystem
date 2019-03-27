package cn.com.school.classinfo.utils;

import cn.com.school.classinfo.common.constant.ReportConstant;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * freemarker工具类
 * @author dongpp
 * @date 2018/11/7
 */
@Slf4j
public class FreeMarkerUtil {

    private static final Configuration CFG;

    static {
        CFG = new Configuration(Configuration.VERSION_2_3_28);
        CFG.setClassForTemplateLoading(FreeMarkerUtil.class, "/template");
        CFG.setDefaultEncoding("UTF-8");
        CFG.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        CFG.setLogTemplateExceptions(false);
        CFG.setWrapUncheckedExceptions(true);
    }

    /**
     * 获取指定模板名称的模板对象
     * @param templateName 模板名称
     * @return 模板对象
     */
    public static Template getTemplate(String templateName){
        try {
            return CFG.getTemplate(templateName);
        } catch (IOException e) {
            log.error("get freemarker template error, templateName: {}, error: {}", templateName, e);
        }
        return null;
    }

    /**
     * 根据模板名称生成文件
     * @param templateName 模板名称
     * @param param 模板参数
     * @param toFilePath 生成文件路径
     * @return 生成是否成功
     */
    public static boolean generateFile(String templateName, Map<String, Object> param, String toFilePath){
        boolean success = true;
        Template template = getTemplate(templateName);
        if(Objects.nonNull(template)){
            try (FileWriter writer = new FileWriter(toFilePath)){
                template.process(param, writer);
            } catch (Exception e) {
                success = false;
                log.error("generate freemarker template error, templateName: {}, error: {}", templateName, e);
            }
        }
        return success;
    }

    /**
     * 生成咨询报告文件
     * @param param 模板参数
     * @param toFilePath 生成文件路径
     * @return 生成是否成功
     */
    public static boolean generateAdvisoryFile(Map<String, Object> param, String toFilePath){
        return generateFile(ReportConstant.ADVISORY_FTL, param, toFilePath);
    }

    /**
     * 生成询价报告文件
     * @param param 模板参数
     * @param toFilePath 生成文件路径
     * @return 生成是否成功
     */
    public static boolean generateInquiryFile(Map<String, Object> param, String toFilePath){
        return generateFile(ReportConstant.INQUIRY_FTL, param, toFilePath);
    }

}
