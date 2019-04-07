/**
 *
 */
package cn.com.school.classinfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.school.classinfo.mapper.ScUserMapper;
import cn.com.school.classinfo.model.ScUser;
import cn.com.school.classinfo.service.ScUserService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 禧泰_董鹏鹏
 *
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ScUserServiceImpl implements ScUserService{

    @Autowired
    private ScUserMapper scUserMapper;

    @Override
    public ScUser getUserById(int userId) {
        return scUserMapper.selectByPrimaryKey(userId);
    }


}
