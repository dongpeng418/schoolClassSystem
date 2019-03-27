package cn.com.school.classinfo.service;

import cn.com.school.classinfo.model.Dict;

import java.util.List;
import java.util.Map;

/**
 * 字典Service
 * @author dongpp
 * @date 2018/10/25
 */
public interface DictService {

    /**
     * 获取字典Map
     * @param dictTypeCode 字典类型，可以为空，为空则查询全部字典
     * @return key: 字典类型编码，value: 字典项
     */
    Map<String, List<Dict>> getDictMap(String dictTypeCode);

}
