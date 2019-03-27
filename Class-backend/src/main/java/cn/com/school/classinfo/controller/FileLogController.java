package cn.com.school.classinfo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

/**
 * 日志查看
 *
 * @author dongpp
 * @date 2019-01-08
 */
@ApiIgnore
@RestController
@RequestMapping("/test/log")
public class FileLogController {

    @GetMapping("/{type}")
    public String getLog(@PathVariable String type, @RequestParam(required = false, defaultValue = "1") Integer size) {
        long viewSize = size * 1024 * 1024;
        File workFileDir = null;
        String filename = "/logs/%s/%s.log";
        filename = String.format(filename, type, type);
        File file = null;
        for(int i = 0; i < 5; i++){
            if(Objects.isNull(workFileDir)){
                workFileDir = new File(System.getProperty("user.dir"));
            }else{
                workFileDir = workFileDir.getParentFile();
            }
            file = new File(workFileDir.getAbsolutePath() + filename);
            if(file.exists()){
                break;
            }
        }
        if(!file.exists()){
            return "file not found";
        }
        StringBuilder lines = new StringBuilder((int) viewSize);
        try (RandomAccessFile rf = new RandomAccessFile(file, "r")) {
            long fileLength = rf.length();
            long readIndex = fileLength - (viewSize);
            if(readIndex < 0){
                readIndex = 0;
            }
            String line;
            rf.seek(readIndex);
            while ((line = rf.readLine()) != null){
                lines.append(line).append("<br/>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.toString();
    }

}
