package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.SysLogConstant;
import cn.com.school.classinfo.config.AvsConfig;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.model.SysReportConfig;
import cn.com.school.classinfo.service.SysLogService;
import cn.com.school.classinfo.service.SysReportConfigService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.FileUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.utils.ObjectUtil;
import cn.com.school.classinfo.validator.FileValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 系统报告配置
 *
 * @author dongpp
 * @date 2018/12/13
 */
@Slf4j
@Validated
@Api(tags = "渠道-系统报告配置接口")
@RestController
@RequestMapping("/api/tenant/report-config")
public class SysReportConfigController {

    private final AvsConfig avsConfig;

    private final SysReportConfigService sysReportConfigService;

    private final SysLogService sysLogService;

    public SysReportConfigController(AvsConfig avsConfig,
                                     SysReportConfigService sysReportConfigService,
                                     SysLogService sysLogService) {
        this.avsConfig = avsConfig;
        this.sysReportConfigService = sysReportConfigService;
        this.sysLogService = sysLogService;
    }

    /**
     * 增加系统报告配置
     *
     * @param sysReportConfig 系统报告配置
     * @return 响应消息
     */
    @ApiOperation(value = "增加系统报告配置")
    @PostMapping("/add")
    public ResultMessage add(@Valid SysReportConfig sysReportConfig,
                             @ApiParam(value = "营业执照文件")
                             @FileValidator(max = 10, message = "营业执照文件大小超过限制")
                             @RequestParam(required = false)
                                     MultipartFile licenseFile,
                             @ApiParam(value = "资质证书文件")
                                 @FileValidator(max = 10, message = "资质证书文件大小超过限制")
                             @RequestParam(required = false)
                                     MultipartFile certificateFile,
                             @ApiParam(value = "估价师签章1文件")
                                 @FileValidator(max = 2, message = "估价师签章1文件大小超过限制")
                             @RequestParam(required = false)
                                     MultipartFile appraiserSign1File,
                             @ApiParam(value = "估价师签章2文件")
                                 @FileValidator(max = 2, message = "估价师签章2文件大小超过限制")
                             @RequestParam(required = false)
                                     MultipartFile appraiserSign2File,
                             @ApiParam(value = "报告签章")
                                 @FileValidator(max = 2, message = "报告签章文件大小超过限制")
                             @RequestParam(required = false)
                                     MultipartFile reportSignImgFile) {
        String licensePath = uploadFile(licenseFile, sysReportConfig.getReportType());
        String certificatePath = uploadFile(certificateFile, sysReportConfig.getReportType());
        String appraiser1Path = uploadFile(appraiserSign1File, sysReportConfig.getReportType());
        String appraiser2Path = uploadFile(appraiserSign2File, sysReportConfig.getReportType());
        String reportSignPath = uploadFile(reportSignImgFile, sysReportConfig.getReportType());
        if (StringUtils.isNotBlank(licensePath)) {
            sysReportConfig.setBusinessLicense(licensePath);
        }
        if (StringUtils.isNotBlank(certificatePath)) {
            sysReportConfig.setCertificate(certificatePath);
        }
        if (StringUtils.isNotBlank(appraiser1Path)) {
            sysReportConfig.setAppraiserSign1(appraiser1Path);
        }
        if (StringUtils.isNotBlank(appraiser2Path)) {
            sysReportConfig.setAppraiserSign2(appraiser2Path);
        }
        if (StringUtils.isNotBlank(reportSignPath)) {
            sysReportConfig.setReportSignImg(reportSignPath);
        }
        sysReportConfigService.add(sysReportConfig);
        return ResultMessage.success();
    }

    /**
     * 更新系统报告配置
     *
     * @param sysReportConfig 系统报告配置
     * @return 响应消息
     */
    @ApiOperation(value = "更新系统报告配置")
    @PostMapping("/update")
    public ResultMessage update(@Valid SysReportConfig sysReportConfig,
                                @ApiParam(value = "营业执照文件")
                                @FileValidator(max = 10, message = "营业执照文件大小超过限制")
                                @RequestParam(required = false)
                                        MultipartFile licenseFile,
                                @ApiParam(value = "资质证书文件")
                                    @FileValidator(max = 10, message = "资质证书文件大小超过限制")
                                    @RequestParam(required = false)
                                            MultipartFile certificateFile,
                                @ApiParam(value = "估价师签章1文件")
                                    @FileValidator(max = 2, message = "估价师签章1文件大小超过限制")
                                    @RequestParam(required = false)
                                            MultipartFile appraiserSign1File,
                                @ApiParam(value = "估价师签章2文件")
                                    @FileValidator(max = 2, message = "估价师签章2文件大小超过限制")
                                    @RequestParam(required = false)
                                            MultipartFile appraiserSign2File,
                                @ApiParam(value = "报告签章")
                                    @FileValidator(max = 2, message = "报告签章文件大小超过限制")
                                    @RequestParam(required = false)
                                            MultipartFile reportSignImgFile,
                                HttpServletRequest request) {
        SysReportConfig reportConfig = sysReportConfigService.getByReportType(sysReportConfig.getReportType());
        sysReportConfig.setId(null);
        sysReportConfig.setBusinessLicense(reportConfig.getBusinessLicense());
        sysReportConfig.setCertificate(reportConfig.getCertificate());
        sysReportConfig.setAppraiserSign1(reportConfig.getAppraiserSign1());
        sysReportConfig.setAppraiserSign2(reportConfig.getAppraiserSign2());
        sysReportConfig.setReportSignImg(reportConfig.getReportSignImg());
        add(sysReportConfig, licenseFile, certificateFile, appraiserSign1File, appraiserSign2File, reportSignImgFile);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        String reportName = sysReportConfig.getReportType() == 1 ? "询价报告" : "咨询报告";
        String modifyDetail = ObjectUtil.getModifyInfo(sysReportConfig, reportConfig);
        sysLog.setOperateLog(String.format(SysLogConstant.TENANT_REPORT_MODIFY, CommonUtil.getLoginUser().getLoginUser(),
                DateUtil.nowToString(), reportName, modifyDetail, HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 获取系统报告配置
     *
     * @param reportType 报告类型
     * @return 响应消息
     */
    @ApiOperation(value = "获取系统报告配置")
    @GetMapping("/get")
    public ResultMessage get(@ApiParam(value = "报告类型，1：询价，2：咨询。reportType 和 id 二选一")
                             @RequestParam(required = false)
                             @Range(min = 1, max = 2, message = "reportType 参数错误") Integer reportType,
                             @ApiParam(value = "报告配置ID。reportType 和 id 二选一")
                             @RequestParam(required = false) Integer id) {

        if(Objects.isNull(reportType) && Objects.isNull(id)){
            return ResultMessage.requestError("reportType 和 id 必须有一个不为空");
        }
        SysReportConfig reportConfig;
        if(Objects.nonNull(reportType)){
            reportConfig = sysReportConfigService.getByReportType(reportType);
        }else{
            reportConfig = sysReportConfigService.getById(id);
        }
        return ResultMessage.success(reportConfig);
    }

    @ApiOperation(value = "删除图片")
    @PostMapping("/images/delete")
    public ResultMessage delete(@ApiParam(value = "报告类型，1：询价，2：咨询", required = true)
                                  @Range(min = 1, max = 2, message = "reportType 参数错误")
                                  @RequestParam Integer reportType,
                              @ApiParam(value = "图片类型，1：营业执照，2：资质证书，3：估价师签章1，4：估价师签章2，5：报告签章", required = true)
                                  @Range(min = 1, max = 5, message = "reportType 参数错误")
                                  @RequestParam Integer imageType) {
        SysReportConfig reportConfig = sysReportConfigService.getByReportType(reportType);
        if(Objects.isNull(reportConfig)){
            return ResultMessage.paramError("reportType");
        }
        switch (imageType) {
            case 1:
                reportConfig.setBusinessLicense(null);
                break;
            case 2:
                reportConfig.setCertificate(null);
                break;
            case 3:
                reportConfig.setAppraiserSign1(null);
                break;
            case 4:
                reportConfig.setAppraiserSign2(null);
                break;
            default:
                reportConfig.setReportSignImg(null);
        }
        reportConfig.setId(null);
        sysReportConfigService.add(reportConfig);
        return ResultMessage.success();
    }

    @ApiOperation(value = "获取报告配置图片")
    @GetMapping("/images")
    public ResponseEntity images(@ApiParam(value = "报告类型，1：询价，2：咨询。reportType 和 id 二选一")
                                     @Range(min = 1, max = 2, message = "reportType 参数错误")
                                     @RequestParam(required = false) Integer reportType,
                                 @ApiParam(value = "报告配置ID。reportType 和 id 二选一")
                                     @RequestParam(required = false) Integer id,
                                 @ApiParam(value = "图片类型，1：营业执照，2：资质证书，3：估价师签章1，4：估价师签章2，5：报告签章", required = true)
                                     @Range(min = 1, max = 5, message = "reportType 参数错误")
                                     @RequestParam Integer imageType) {

        if(Objects.isNull(reportType) && Objects.isNull(id)){
            return ResponseEntity.ok(ResultMessage.requestError("reportType 和 id 必须有一个不为空"));
        }
        SysReportConfig reportConfig;
        if(Objects.nonNull(reportType)){
            reportConfig = sysReportConfigService.getByReportType(reportType);
        }else{
            reportConfig = sysReportConfigService.getById(id);
        }
        String filePath = avsConfig.getPath().getBase() + getImagesPath(imageType, reportConfig);
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(FileUtil.getMediaType(file.getName()));
            headers.setContentLength(file.length());
            byte[] bytes;
            try {
                bytes = FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                log.error("read file error, file path: {}", filePath);
                return ResponseEntity.ok(ResultMessage.requestError("read file error"));
            }
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        } else {
            return ResponseEntity.ok(ResultMessage.requestError("file not found"));
        }
    }

    /**
     * 根据图片类型获取相关联的路径
     *
     * @param imageType 图片类型
     * @param reportConfig 报告配置信息
     * @return 图片路径
     */
    private String getImagesPath(Integer imageType, SysReportConfig reportConfig) {
        String path;
        switch (imageType) {
            case 1:
                path = reportConfig.getBusinessLicense();
                break;
            case 2:
                path = reportConfig.getCertificate();
                break;
            case 3:
                path = reportConfig.getAppraiserSign1();
                break;
            case 4:
                path = reportConfig.getAppraiserSign2();
                break;
            default:
                path = reportConfig.getReportSignImg();
        }
        return path;
    }


    /**
     * 将上传的文件保存到本地
     *
     * @param multipartFile 上传文件
     * @param reportType    报告类型
     * @return 本地文件的相对路径，如果为空，说明保存失败
     */
    private String uploadFile(MultipartFile multipartFile, Integer reportType) {
        String path = null;
        if (Objects.nonNull(multipartFile) && !multipartFile.isEmpty()) {
            //拼接相对路径
            String relativePath = FileUtil.stitchingPath(CommonUtil.getTenantId().toString(),
                    avsConfig.getPath().getReportConfig(), reportType.toString());
            path = relativePath + FileUtil.genUuidFileName(multipartFile.getOriginalFilename());
            //全路径
            String filePath = avsConfig.getPath().getBase() + path;
            //保存到本地
            File file = FileUtil.createDir(filePath);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                log.error("[report-config] upload multipartFile error, filePath: {}, msg: {}", filePath, e);
                path = null;
            }
        }
        return path;
    }

}
