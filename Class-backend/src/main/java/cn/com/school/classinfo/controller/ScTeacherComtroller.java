/**
 *
 */
package cn.com.school.classinfo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.BaseConstant;
import cn.com.school.classinfo.model.ScTeacherInfo;
import cn.com.school.classinfo.model.ScUser;
import cn.com.school.classinfo.service.ScTeacherInfoService;
import cn.com.school.classinfo.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongpp
 *
 */
@Api(tags = "学生信息相关接口")
@Validated
@RestController
@RequestMapping("/api/teacher")
@Slf4j
public class ScTeacherComtroller {

    @Value("${file.studentInfo.basPath}")
    private String basePath;

    /**
     * 模板文件名
     */
    public static final String TEMPLATE_NAME = "teacher_template.xlsx";

    /**
     * 模板下载文件名称
     */
    public static final String TEMPLATE_DOWNLOAD_NAME = "教师信息批量导入模板.xlsx";

    @Autowired
    private ScTeacherInfoService scTeacherInfoService;

    @ApiOperation(value = "excel导入教师信息")
    @PostMapping("/upload")
    public ResultMessage upload(@ApiParam(value = "教师信息文件,返回用户信息，enablePassword 6位随机密码，登录后必须修改才能操作", required = true)
    @RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return ResultMessage.paramError("file");
        }

        //上传的文件保存到本地
        String uploadPath = BaseConstant.TEACHER_UPLOAD_PATH + FileUtil.genRelativePath(file.getOriginalFilename());

        String filePath = basePath  + uploadPath;
        File localFile = FileUtil.createFile(filePath);
        if (Objects.isNull(localFile)) {
            log.error("create local file error, file path: {} ", filePath);
            return ResultMessage.requestError("保存上传文件失败");
        }

        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            log.error("save upload file error, file path: {}, error: {}", filePath, e);
            return ResultMessage.requestError("保存上传文件失败");
        }

        //验证上传的文件
        List<ScTeacherInfo> uploadInfos;
        try {
            uploadInfos = getBatchFileRecord(localFile);
        } catch (IOException e) {
            return ResultMessage.requestError(e.getMessage());
        }
        if (CollectionUtils.isEmpty(uploadInfos)) {
            return ResultMessage.paramError("上传文件没有记录");
        }

        //不能超过1W条
        if (uploadInfos.size() > 10000) {
            uploadInfos.clear();
            return ResultMessage.paramError("学生记录超过10000条");
        }

        List<ScUser> reslutUsers  = scTeacherInfoService.saveBatch(uploadInfos);

        return ResultMessage.success().data(reslutUsers);
    }

    /**
     * 获取上传文件的记录
     *
     * @param batchFile 批量估价文件
     * @return 记录列表
     */
    private List<ScTeacherInfo> getBatchFileRecord(File batchFile) throws IOException {
        String ext = FilenameUtils.getExtension(batchFile.getName());
        List<ScTeacherInfo> uploadInfos;
        if ("CSV".equalsIgnoreCase(ext)) {
            //如果以utf-8编码读取不到记录，则以GBK编码再读一次
            uploadInfos = getBatchFileRecordFromCsv(batchFile, null);
            if (CollectionUtils.isEmpty(uploadInfos)) {
                uploadInfos = getBatchFileRecordFromCsv(batchFile, "gbk");
            }
        } else {
            uploadInfos = getBatchFileRecordFromExcel(batchFile);
        }
        return uploadInfos;
    }

    /**
     * 从Excel中获取上传文件的记录
     *
     * @param batchFile 批量估价文件
     * @return 记录列表
     */
    private List<ScTeacherInfo> getBatchFileRecordFromExcel(File batchFile) {
        ImportParams params = new ImportParams();
        return ExcelImportUtil.importExcel(
                        batchFile,
                        ScTeacherInfo.class,
                        params);
    }

    /**
     * 从Csv中获取上传文件的记录
     *
     * @param batchFile 批量估价文件
     * @return 记录列表
     */
    private List<ScTeacherInfo> getBatchFileRecordFromCsv(File batchFile, String encode) throws IOException {
        List<ScTeacherInfo> uploadInfos = Lists.newArrayList();
        encode = StringUtils.isBlank(encode) ? "utf-8" : encode;
        try (
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(batchFile), encode))
                        ) {
            String line;
            ScTeacherInfo uploadInfo;
            int index = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (index == 0 && !line.contains("姓名")) {
                    return uploadInfos;
                }
                index++;
                if (line.contains("姓名")) {
                    continue;
                }
                String[] arrays = line.split(",");
                if (arrays.length != 6) {
                    throw new IOException("file format error.");
                }
                uploadInfo = new ScTeacherInfo();
                uploadInfo.settName(arrays[0]);
                uploadInfo.settSex(arrays[1]);
                uploadInfo.settBirthday(Date.valueOf(arrays[2]));
                uploadInfo.settTeachYear(Integer.getInteger(arrays[3]));
                uploadInfo.settCertification(arrays[4]);
                uploadInfos.add(uploadInfo);
            }
        } catch (IOException e) {
            log.error("[studentInfo] read upload file error, filePath:{}, error:{}", batchFile.getAbsolutePath(), e);
            throw new IOException(e);
        }
        return uploadInfos;
    }

}
