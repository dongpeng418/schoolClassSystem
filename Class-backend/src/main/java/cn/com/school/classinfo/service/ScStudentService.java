/**
 *
 */
package cn.com.school.classinfo.service;

import java.util.List;

import cn.com.school.classinfo.model.ScStudentInfo;
import cn.com.school.classinfo.model.ScUser;

/**
 * @author 禧泰_董鹏鹏
 *
 */
public interface ScStudentService {

    /**
     * 保存批量学生信息
     * @param batch 批量估价信息
     * @return 影响行数
     */
    List<ScUser> saveBatch(List<ScStudentInfo> itemList);

}
