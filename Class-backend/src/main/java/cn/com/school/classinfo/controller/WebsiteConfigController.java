package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.CommonConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.config.AvsConfig;
import cn.com.school.classinfo.model.SysWebsiteConfig;
import cn.com.school.classinfo.service.WebsiteConfigService;
import cn.com.school.classinfo.utils.FileUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.validator.FileValidator;
import cn.com.school.classinfo.vo.SysWebsiteConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * 网站配置接口
 *
 * @author dongpp
 * @date 2018/11/21
 */
@Api(tags = "网站配置接口")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/site-config")
public class WebsiteConfigController {

    private final WebsiteConfigService websiteConfigService;

    private final AvsConfig avsConfig;

    public WebsiteConfigController(WebsiteConfigService websiteConfigService,
                                   AvsConfig avsConfig) {
        this.websiteConfigService = websiteConfigService;
        this.avsConfig = avsConfig;
    }

    @ApiOperation(value = "保存网站配置")
    @PostMapping("/save")
    public ResultMessage save(@Valid @ModelAttribute SysWebsiteConfigVO config,
                              @ApiParam(value = "LOGO文件")
                              @FileValidator(max = 2, message = "Logo文件大小超过限制")
                              @RequestParam(required = false)
                                      MultipartFile logoFile,
                              @ApiParam(value = "背景文件")
                              @FileValidator(max = 20, message = "背景文件文件大小超过限制")
                              @RequestParam(required = false)
                                     MultipartFile backgroundFile,
                              HttpServletRequest request) {
        String host = HttpRequestUtil.getRealHost(request);
        String websitePath = avsConfig.getPath().getBase() + avsConfig.getPath().getWebsite();
        SysWebsiteConfig oldConfig = websiteConfigService.getByDomain(host, CommonConstant.CUSTOMER_TYPE);
        log.info("website file path: {}", websitePath);
        if (Objects.nonNull(logoFile) && !logoFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "." +
                    FilenameUtils.getExtension(logoFile.getOriginalFilename());
            String tmpPath = FileUtil.stitchingPath(host) + fileName;
            config.setLogoPath(tmpPath);
            String logoPath = websitePath + tmpPath;
            File localLogoFile = FileUtil.createDir(logoPath);
            try {
                logoFile.transferTo(localLogoFile);

                //删除之前上传的文件
                if(StringUtils.isNotBlank(oldConfig.getLogoPath())){
                    String oldPath = websitePath + oldConfig.getLogoPath();
                    FileUtil.deleteFile(oldPath);
                }
            } catch (IOException e) {
                log.error("[website-config] save logo file error. file path: {}, error: {}", logoPath, e);
                return ResultMessage.requestError("保存LOGO文件失败");
            }
        }
        if (Objects.nonNull(backgroundFile) && !backgroundFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "." +
                    FilenameUtils.getExtension(backgroundFile.getOriginalFilename());
            String tmpPath = FileUtil.stitchingPath(host) + fileName;
            config.setBackgroundPath(tmpPath);
            String backgroundPath = websitePath + tmpPath;
            File localBackgroundFile = FileUtil.createDir(backgroundPath);
            try {
                backgroundFile.transferTo(localBackgroundFile);

                //删除之前上传的文件
                if(StringUtils.isNotBlank(oldConfig.getBackgroundPath())){
                    String oldPath = websitePath + oldConfig.getBackgroundPath();
                    FileUtil.deleteFile(oldPath);
                }
            } catch (IOException e) {
                log.error("[website-config] save Background file error. file path: {}, error: {}", backgroundPath, e);
                return ResultMessage.requestError("保存Background文件失败");
            }
        }
        CommonUtil.doEvalCreateUpdateInfo(config, CreateUpdateEnum.UPDATE);
        websiteConfigService.save(config);
        return ResultMessage.success();
    }

    @ApiOperation(value = "删除图片")
    @PostMapping("/delete")
    public ResultMessage delete(@ApiParam(value = "图片类型：1、LOGO，2：Background")
                                  @Pattern(regexp = "[12]", message = "type 参数错误")
                                  @RequestParam String type,
                                HttpServletRequest request) {
        String host = HttpRequestUtil.getRealHost(request);
        String websitePath = avsConfig.getPath().getBase() + avsConfig.getPath().getWebsite();
        SysWebsiteConfig config = websiteConfigService.getByDomain(host, CommonConstant.CUSTOMER_TYPE);
        if("1".equals(type)){
            String path = websitePath + config.getLogoPath();
            FileUtil.deleteFile(path);
            config.setLogoPath(null);
        }else{
            String path = websitePath + config.getBackgroundPath();
            FileUtil.deleteFile(path);
            config.setBackgroundPath(null);
        }
        CommonUtil.doEvalCreateUpdateInfo(config, CreateUpdateEnum.UPDATE);
        websiteConfigService.updatePath(config);
        return ResultMessage.success();
    }

    @ApiOperation(value = "获取网站配置")
    @GetMapping("/get")
    public ResultMessage get(HttpServletRequest request) {
        String host = HttpRequestUtil.getRealHost(request);
        log.info("[website-config] get config, host: {}", host);
        SysWebsiteConfig config = websiteConfigService.getByDomain(host, CommonConstant.CUSTOMER_TYPE);
        return ResultMessage.success(config);
    }

    @ApiOperation(value = "获取网站配置图片")
    @GetMapping("/images")
    public ResponseEntity get(@ApiParam(value = "图片类型：1、LOGO，2：Background")
                                  @NotBlank(message = "type 不能为空")
                                  @Pattern(regexp = "[12]", message = "type 参数错误")
                                  @RequestParam String type,
                              HttpServletRequest request) throws IOException {
        String host = HttpRequestUtil.getRealHost(request);
        String websitePath = avsConfig.getPath().getBase() + avsConfig.getPath().getWebsite();
        SysWebsiteConfig config = websiteConfigService.getByDomain(host, CommonConstant.CUSTOMER_TYPE);
        String filePath = "1".equals(type) ? websitePath + config.getLogoPath() : websitePath + config.getBackgroundPath();
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
                throw new IOException(e);
            }
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
