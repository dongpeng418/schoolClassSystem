package cn.com.school.classinfo.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 导出excl相关工具类
 *
 * @author dongpp
 * @date 2018/11/29
 */
@Slf4j
public class ExportUtil {

    /**
     * 根据指定的数据生成excel文件，并将其装入ResponseEntity对象并返回
     *
     * @param tmpFilePath    临时文件夹路径
     * @param exportFileName 导出的文件名
     * @param exportData     导出的数据
     * @return ResponseEntity
     */
    public static ResponseEntity getExportExcelResponse(String tmpFilePath, String exportFileName, Class clazz, List<?> exportData) {
        //创建并写入文件
        String tmpPath = tmpFilePath + FileUtil.genRelativePathByExt("xlsx");
        File tmpFile = FileUtil.createDir(tmpPath);
        if (Objects.isNull(tmpFile)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("create file error!");
        }
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, clazz, exportData);
        try {
            workbook.write(new FileOutputStream(tmpFile));
            workbook.close();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("write file error!");
        }
        //下载
        return getExportFileResponse(tmpFile, exportFileName, true);
    }

    /**
     * 读取指定文件，并将其数据装入ResponseEntity对象并返回
     *
     * @param file           文件
     * @param exportFileName 导出文件名称
     * @param isDelete       导出后是否删除文件
     * @return ResponseEntity
     */
    public static ResponseEntity getExportFileResponse(File file, String exportFileName, boolean isDelete) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, FileUtil.getFileDownloadHeader(exportFileName));
        headers.setContentLength(file.length());
        byte[] bytes;
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            log.error("read file error, file path: {}", file.getAbsolutePath());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("read file error!");
        }
        if (isDelete) {
            FileUtil.deleteFile(file);
        }
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);
    }
}
