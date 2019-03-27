package cn.com.school.classinfo.validator;

import cn.com.school.classinfo.dto.HouseInfo;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 估价时房屋信息验证
 *
 * @author dongpp
 * @date 2018/12/10
 */
public class HouseValidatorHandler implements ConstraintValidator<HouseValidator, HouseInfo> {

    @Override
    public void initialize(HouseValidator constraintAnnotation) {

    }

    @Override
    public boolean isValid(HouseInfo houseInfo, ConstraintValidatorContext context) {
        boolean valid = true;
        //房屋信息不能为空
        if(Objects.isNull(houseInfo)){
            valid = false;
        }

        //城市名和城市码不能都为空
        if(StringUtils.isEmpty(houseInfo.getCityName()) && StringUtils.isEmpty(houseInfo.getCityCode())){
            valid = false;
        }

        //小区信息和GPS信息不能都为空
        if(Objects.isNull(houseInfo.getHaInfo()) && Objects.isNull(houseInfo.getGpsInfo())){
            valid = false;
        }
        return valid;
    }
}
