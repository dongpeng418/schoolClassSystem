/**
 *
 */
package cn.com.school.classinfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.school.classinfo.mapper.ScSlnInfoMapper;
import cn.com.school.classinfo.model.ScSlnInfo;
import cn.com.school.classinfo.service.ScSlnInfoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 禧泰_董鹏鹏
 *
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ScSlnInfoServiceImpl implements ScSlnInfoService{

    @Autowired
    private ScSlnInfoMapper scSlnInfoMapper;

    @Override
    public List<ScSlnInfo> getAllDatas() {
        return scSlnInfoMapper.selectAllDatas();
    }

}
