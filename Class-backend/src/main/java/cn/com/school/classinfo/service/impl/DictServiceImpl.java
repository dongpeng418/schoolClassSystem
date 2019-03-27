package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.mapper.DictMapper;
import cn.com.school.classinfo.model.Dict;
import cn.com.school.classinfo.service.DictService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 字典服务实现类
 * @author dongpp
 * @date 2018/10/25
 */
@Service
@CacheConfig(cacheNames = "DICT")
public class DictServiceImpl implements DictService {

    private final DictMapper dictMapper;

    @Autowired
    public DictServiceImpl(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    /**
     * 获取字典Map
     * @param dictTypeCode 字典类型，可以为空，为空则查询全部字典
     * @return key: 字典类型编码，value: 字典项
     */
    @Override
    @Cacheable(key = "#root.caches[0].name  + ':' + #root.methodName + ':' + #dictTypeCode")
    public Map<String, List<Dict>> getDictMap(String dictTypeCode){
        List<Dict> dictList = dictMapper.select(dictTypeCode);
        Map<String, List<Dict>> dictMap = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(dictList)){
            dictList.forEach(dict -> {
                List<Dict> dicts = dictMap.get(dict.getDictTypeCode());
                if(CollectionUtils.isEmpty(dicts)){
                    dicts = Lists.newArrayList();
                    dictMap.put(dict.getDictTypeCode(), dicts);
                }
                dicts.add(dict);
            });
        }
        return dictMap;
    }
}
