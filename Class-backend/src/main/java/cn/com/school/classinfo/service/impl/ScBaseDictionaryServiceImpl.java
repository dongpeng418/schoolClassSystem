/**
 *
 */
package cn.com.school.classinfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.school.classinfo.mapper.ScBaseDictionaryMapper;
import cn.com.school.classinfo.model.ScBaseDictionary;
import cn.com.school.classinfo.service.ScBaseDictionaryService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dongpp
 *
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ScBaseDictionaryServiceImpl implements ScBaseDictionaryService{

    @Autowired
    private ScBaseDictionaryMapper scBaseDictionaryMapper;

    @Override
    public int save(ScBaseDictionary scBaseDictionary) {
        return scBaseDictionaryMapper.insert(scBaseDictionary);
    }

}
