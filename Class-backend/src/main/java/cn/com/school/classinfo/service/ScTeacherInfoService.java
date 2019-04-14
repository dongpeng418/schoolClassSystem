/**
 *
 */
package cn.com.school.classinfo.service;

import java.util.List;

import cn.com.school.classinfo.model.ScTeacherInfo;
import cn.com.school.classinfo.model.ScUser;

/**
 * @author dongpp
 *
 */
public interface ScTeacherInfoService {

    /**
     * 保存批量教师信息
     * @param
     * @return 影响行数
     */
    List<ScUser> saveBatch(List<ScTeacherInfo> itemList);

}
