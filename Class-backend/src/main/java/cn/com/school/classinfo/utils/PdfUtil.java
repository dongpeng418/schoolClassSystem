package cn.com.school.classinfo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 生成PDF文件工具类
 * 根据wkhtmltopdf工具将html文件转成pdf文件
 *
 * @author dongpp
 * @date 2018/11/7
 */
@Slf4j
public class PdfUtil {

    /**
     * 生成pdf文件的命令
     */
    private static String PDF_CMD = "wkhtmltopdf";

    /**
     * 线程池
     */
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 生成pdf
     * @param fromPath html文件路径
     * @param toPath 生成的pdf文件路径
     * @return 是否成功
     */
    public static boolean generatePdf(String fromPath, String toPath) {
        return generatePdf(fromPath, toPath, null, null);
    }

    /**
     * 生成pdf
     * @param fromPath html文件路径
     * @param toPath 生成的pdf文件路径
     * @param pdfWidth pdf宽度（单位：mm）
     * @param pdfHeight pdf高度（单位：mm）
     * @return 是否成功
     */
    public static boolean generatePdf(String fromPath, String toPath, Integer pdfWidth, Integer pdfHeight) {
        StringBuilder sb = new StringBuilder(PDF_CMD);
        if(Objects.nonNull(pdfWidth)){
            sb.append(" --page-width ").append(pdfWidth)
                    .append(" --page-height ").append(pdfHeight)
                    .append(" --javascript-delay 1000 ");
        }
        sb.append(" ").append(fromPath).append(" ").append(toPath);
        try {
            Process process = Runtime.getRuntime().exec(sb.toString());
            executorService.execute(new ClearBufferRunner(process.getInputStream()));
            executorService.execute(new ClearBufferRunner(process.getErrorStream()));
            int flag = process.waitFor();
            return flag == 0;
        } catch (Exception e) {
            log.error("generate pdf error: ", e);
        }
        return false;
    }


    /**
     * 清除进程缓存，防止阻塞
     */
    static class ClearBufferRunner implements Runnable {

        private InputStream inputStream;

        ClearBufferRunner(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            } catch (IOException e) {
                log.error("clear process cached error: ", e);
            }
        }
    }
}
