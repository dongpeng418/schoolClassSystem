package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.BaseQuery;
import cn.com.school.classinfo.model.SysAssetType;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 资产类型Servcie
 *
 * @author dongpp
 * @date 2018/11/21
 */
public interface AssetTypeService {

    /**
     * 增加资产类型信息
     *
     * @param assetType 资产类型信息
     */
    void add(SysAssetType assetType);

    /**
     * 保存资产类型信息
     *
     * @param assetType 资产类型信息
     */
    void save(SysAssetType assetType);

    /**
     * 删除资产类型信息
     *
     * @param assetType 资产类型信息
     */
    void delete(SysAssetType assetType);

    /**
     * 根据资产类型名称获取资产类型信息
     *
     * @param name 资产类型名称
     */
    SysAssetType getByName(String name);

    /**
     * 获取资产类型信息列表
     *
     * @return 列表
     */
    List<SysAssetType> list();

    /**
     * 分页获取资产类型信息列表
     *
     * @param query 查询条件
     * @return 列表
     */
    PageInfo<SysAssetType> pageable(BaseQuery query);
}
