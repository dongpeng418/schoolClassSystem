package cn.com.school.classinfo.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.github.pagehelper.PageInfo;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.SysLogConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.CustomerCompanyQuery;
import cn.com.school.classinfo.config.AvsConfig;
import cn.com.school.classinfo.model.CustomerCompany;
import cn.com.school.classinfo.model.SysLog;
import cn.com.school.classinfo.service.CustomerCompanyService;
import cn.com.school.classinfo.service.SysLogService;
import cn.com.school.classinfo.utils.DateUtil;
import cn.com.school.classinfo.utils.FileUtil;
import cn.com.school.classinfo.utils.HttpRequestUtil;
import cn.com.school.classinfo.utils.ObjectUtil;
import cn.com.school.classinfo.validator.FileValidator;
import cn.com.school.classinfo.vo.CustomerCompanyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户公司接口
 *
 * @author dongpp
 * @date 2018/12/13
 */
@Slf4j
@Validated
@Api(tags = "渠道-客户公司接口")
@RestController
@RequestMapping("/api/tenant/customer-company")
public class CustomerCompanyController {

    private final AvsConfig avsConfig;

    private final CustomerCompanyService customerCompanyService;

    private final SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    public CustomerCompanyController(AvsConfig avsConfig,
                    CustomerCompanyService customerCompanyService,
                    SysLogService sysLogService) {
        this.avsConfig = avsConfig;
        this.customerCompanyService = customerCompanyService;
        this.sysLogService = sysLogService;
    }

    /**
     * 增加客户公司
     *
     * @param customerCompany 客户公司
     * @return 响应消息
     */
    @ApiOperation(value = "增加客户公司")
    @PostMapping("/add")
    public ResultMessage add(@Valid CustomerCompany customerCompany,
                    @ApiParam(value = "营业执照文件")
    @FileValidator(max = 10)
    @RequestParam(required = false)
    MultipartFile licenseFile) {
        CustomerCompany company = customerCompanyService.getByName(customerCompany.getName());
        if (Objects.nonNull(company)) {
            return ResultMessage.duplicateError("公司名称已存在");
        }
        if (Objects.nonNull(licenseFile) && !licenseFile.isEmpty()) {
            boolean isSuccess = uploadFile(licenseFile, customerCompany);
            if (!isSuccess) {
                return ResultMessage.requestError("upload file error");
            }
        }
        customerCompanyService.add(customerCompany);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        sysLog.setOperateLog(String.format(SysLogConstant.COMPANY_ADD, CommonUtil.getLoginUser().getLoginUser(),
                        DateUtil.nowToString(), customerCompany.getName(), HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 更新客户公司
     *
     * @param customerCompany 客户公司
     * @return 响应消息
     */
    @ApiOperation(value = "更新客户公司")
    @PostMapping("/update")
    public ResultMessage update(@Valid CustomerCompany customerCompany,
                    @ApiParam(value = "营业执照文件")
    @FileValidator(max = 10)
    @RequestParam(required = false)
    MultipartFile licenseFile) {
        if(Objects.isNull(customerCompany.getId())){
            return ResultMessage.paramError("id");
        }
        CustomerCompany company = customerCompanyService.getByName(customerCompany.getName());
        if (Objects.nonNull(company) && !company.getId().equals(customerCompany.getId())) {
            return ResultMessage.duplicateError("公司名称已存在");
        }
        if (Objects.nonNull(licenseFile) && !licenseFile.isEmpty()) {
            boolean isSuccess = uploadFile(licenseFile, customerCompany);
            if (!isSuccess) {
                return ResultMessage.requestError("upload file error");
            } else {
                //删除之前上传的文件
                company = customerCompanyService.getById(customerCompany.getId());
                if(StringUtils.isNotBlank(company.getBusinessLicense())){
                    String filePath = avsConfig.getPath().getBase() + company.getBusinessLicense();
                    FileUtil.deleteFile(filePath);
                }
            }
        }
        customerCompanyService.update(customerCompany);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        String modifyDetail = ObjectUtil.getModifyInfo(customerCompany, company);
        sysLog.setOperateLog(String.format(SysLogConstant.COMPANY_MODIFY, CommonUtil.getLoginUser().getLoginUser(),
                        DateUtil.nowToString(), modifyDetail, HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 删除客户公司
     *
     * @param id 客户公司ID
     * @return 响应消息
     */
    @ApiOperation(value = "删除客户公司")
    @PostMapping("/delete")
    public ResultMessage delete(Integer id) {
        CustomerCompany company = customerCompanyService.getById(id);
        if(Objects.isNull(company)){
            return ResultMessage.paramError("id");
        }
        customerCompanyService.delete(id);

        //系统日志
        SysLog sysLog = new SysLog();
        CommonUtil.doOperateLogInfo(sysLog);
        sysLog.setOperateLog(String.format(SysLogConstant.COMPANY_DELETE, CommonUtil.getLoginUser().getLoginUser(),
                        DateUtil.nowToString(), company.getName(), HttpRequestUtil.getRemoteIp(request)));
        sysLogService.add(sysLog);
        return ResultMessage.success();
    }

    /**
     * 获取所有客户公司
     *
     * @return 响应消息
     */
    @ApiOperation(value = "获取所有客户公司列表")
    @GetMapping("/list")
    public ResultMessage list() {
        List<CustomerCompany> customerCompanyList = customerCompanyService.getListByQuery();
        return ResultMessage.success(customerCompanyList);
    }

    /**
     * 获取所有客户公司
     *
     * @return 响应消息
     */
    @ApiOperation(value = "获取未添加域名的客户公司列表")
    @GetMapping("/filter")
    public ResultMessage filter() {
        List<CustomerCompany> customerCompanyList = customerCompanyService.getFilterListByQuery();
        return ResultMessage.success(customerCompanyList);
    }

    /*
     * 获取所有客户公司
     *
     * @return 响应消息
     */
    @ApiOperation(value = "获取未创建客户账户得公司列表")
    @GetMapping("/list/nouser")
    public ResultMessage noUserList() {
        List<CustomerCompanyVO> customerCompanyList = customerCompanyService.getNoCreatedUserListByQuery();
        return ResultMessage.success(customerCompanyList);
    }


    /**
     * 分页获取客户公司列表
     *
     * @return 响应消息
     */
    @ApiOperation(value = "获取客户公司列表-分页")
    @GetMapping("/pageable")
    public ResultMessage pageable(@Valid @ModelAttribute CustomerCompanyQuery query) {
        PageInfo<CustomerCompanyVO> customerCompanyList = customerCompanyService.getPageableListByQuery(query);
        return ResultMessage.success(customerCompanyList);
    }

    @ApiOperation(value = "获取营业执照图片")
    @GetMapping("/images")
    public ResponseEntity image(@ApiParam(value = "客户公司ID") @RequestParam Integer companyId) {
        CustomerCompany customerCompany = customerCompanyService.getById(companyId);
        if(Objects.isNull(customerCompany)){
            return ResponseEntity.ok(ResultMessage.paramError("companyId"));
        }
        String filePath = avsConfig.getPath().getBase() + customerCompany.getBusinessLicense();
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

    @ApiOperation(value = "删除图片")
    @PostMapping("/images/delete")
    public ResultMessage save(@ApiParam(value = "客户公司ID") @RequestParam Integer companyId) {
        CustomerCompany customerCompany = customerCompanyService.getById(companyId);
        if(Objects.isNull(customerCompany)){
            return ResultMessage.paramError("companyId");
        }
        String path = avsConfig.getPath().getBase() + avsConfig.getPath().getWebsite() + customerCompany.getBusinessLicense();
        customerCompany.setBusinessLicense(null);
        CommonUtil.doEvalCreateUpdateInfo(customerCompany, CreateUpdateEnum.UPDATE);
        customerCompanyService.updatePath(customerCompany);
        //删除图片
        FileUtil.deleteFile(path);
        return ResultMessage.success();
    }

    /**
     * 将上传的文件保存到本地
     *
     * @param licenseFile     上传文件
     * @param customerCompany 客户公司信息
     * @return 是否保存成功
     */
    private boolean uploadFile(MultipartFile licenseFile, CustomerCompany customerCompany) {
        boolean flag = true;
        //文件名MD5
        String companyName = DigestUtils.md5Hex(customerCompany.getName());
        //拼接相对路径
        String relativePath = FileUtil.stitchingPath(CommonUtil.getTenantId().toString(),
                        avsConfig.getPath().getCompany(), companyName);
        //全路径
        String path = relativePath + FileUtil.genUuidFileName(licenseFile.getOriginalFilename());
        String filePath = avsConfig.getPath().getBase() + path;
        //保存到本地
        File file = FileUtil.createDir(filePath);
        try {
            licenseFile.transferTo(file);
        } catch (IOException e) {
            log.error("[customer-company] upload licenseFile error, filePath: {}, msg: {}", filePath, e);
            flag = false;
        }
        //将相对路径保存到数据库
        customerCompany.setBusinessLicense(path);
        return flag;
    }

}
