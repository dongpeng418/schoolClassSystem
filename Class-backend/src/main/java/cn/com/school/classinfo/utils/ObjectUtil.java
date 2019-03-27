package cn.com.school.classinfo.utils;

import cn.com.school.classinfo.annotation.Modify;
import cn.com.school.classinfo.common.constant.SysLogConstant;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 对象工具类
 *
 * @author dongpp
 * @date 2018-12-19
 */
@Slf4j
public class ObjectUtil {

    /**
     * 比较两个对象的修改字段
     * 需要在比较的对象的属性上添加 @Modify 注解
     *
     * @param current 当前对象
     * @param old 比较对象
     * @return 比较结果
     */
    public static String getModifyInfo(Object current, Object old){
        if(Objects.isNull(current) || Objects.isNull(old)){
            return null;
        }
        StringBuilder info = new StringBuilder();
        Field[] fields = current.getClass().getDeclaredFields();
        for(Field field : fields){
            Modify modify = field.getAnnotation(Modify.class);
            if(Objects.isNull(modify)){
                continue;
            }
            PropertyDescriptor pd;
            PropertyDescriptor pd2;
            try {
                pd = new PropertyDescriptor(field.getName(), current.getClass());
                pd2 = new PropertyDescriptor(field.getName(), old.getClass());
                Method getMethod = pd.getReadMethod();
                Method getMethod2 = pd2.getReadMethod();
                Object newValue = getMethod.invoke(current);
                Object oldValue = getMethod2.invoke(old);
                if(Objects.isNull(newValue) && Objects.isNull(oldValue)){
                    continue;
                }
                if(Modify.Type.CHAR.equals(modify.type())){
                    if(Objects.isNull(newValue) || Objects.isNull(oldValue)){
                        if(Objects.isNull(newValue)){
                            info.append(String.format(SysLogConstant.COMMON_MODIFY, modify.value(), oldValue, "空"));
                        }
                        if(Objects.isNull(oldValue)){
                            info.append(String.format(SysLogConstant.COMMON_MODIFY, modify.value(), "空", newValue));
                        }
                    }else{
                        if(!newValue.toString().equals(oldValue.toString())){
                            info.append(String.format(SysLogConstant.COMMON_MODIFY, modify.value(), oldValue, newValue));
                        }
                    }
                }else if(Modify.Type.IMAGE.equals(modify.type())){
                    if(Objects.isNull(newValue) || Objects.isNull(oldValue)){
                        if(Objects.isNull(oldValue)){
                            info.append(String.format(SysLogConstant.COMMON_IMAGE_MODIFY, modify.value(), "上传图片"));
                        }
                    }else{
                        info.append(String.format(SysLogConstant.COMMON_IMAGE_MODIFY, modify.value(), "修改图片"));
                    }
                }else {
                    Integer ni = (Integer) newValue;
                    Integer oi = (Integer) oldValue;
                    if(ni.equals(oi)){
                        continue;
                    }
                    if(ni == 0){
                        info.append(String.format(SysLogConstant.COMMON_CHECKBOX_MODIFY, modify.value(), "去除"));
                    }else{
                        info.append(String.format(SysLogConstant.COMMON_CHECKBOX_MODIFY, modify.value(), "勾选"));
                    }
                }

            } catch (Exception e) {
                log.error("[object util] get PropertyDescriptor error", e);
            }
        }
        return info.toString();
    }
}
