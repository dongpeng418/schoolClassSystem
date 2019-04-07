/**
 *
 */
package cn.com.school.classinfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.school.classinfo.mapper.ScStudentSelectMapper;
import cn.com.school.classinfo.model.ScStudentSelect;
import cn.com.school.classinfo.service.ScStudentSelectService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 禧泰_董鹏鹏
 *
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ScStudentSelectServiceImpl implements ScStudentSelectService{

    @Autowired
    private ScStudentSelectMapper scStudentSelectMapper;

    @Override
    public int saveSelectedScheme(ScStudentSelect scStudentSelect) {
        return scStudentSelectMapper.insert(scStudentSelect);
    }
}
