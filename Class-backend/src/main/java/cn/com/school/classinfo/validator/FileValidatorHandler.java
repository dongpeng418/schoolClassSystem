package cn.com.school.classinfo.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 上传文件大小验证
 *
 * @author dongpp
 * @date 2018/12/10
 */
public class FileValidatorHandler implements ConstraintValidator<FileValidator, MultipartFile> {

    private int max;

    private int mb = 1000 * 1000;

    @Override
    public void initialize(FileValidator constraintAnnotation) {
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(Objects.isNull(file)){
            return true;
        }
        int size = (int) (file.getSize()/mb);
        return max >= size;
    }
}
