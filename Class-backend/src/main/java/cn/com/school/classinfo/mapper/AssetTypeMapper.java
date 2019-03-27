package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.model.SysAssetType;

import java.util.List;

/**
 * 资产类型Mapper
 *
 * @author dongpp
 * @date 2018/11/21
 */
public interface AssetTypeMapper {

    /**
     * 插入资产类型
     *
     * @param assetType 资产类型
     * @return 数量
     */
    int insert(SysAssetType assetType);

    /**
     * 更新资产类型信息
     *
     * @param assetType 资产类型
     * @return 数量
     */
    int update(SysAssetType assetType);

    /**
     * 删除资产类型信息
     *
     * @param assetType 资产类型
     * @return 数量
     */
    int delete(SysAssetType assetType);

    /**
     * 查询列表
     *
     * @return 列表
     */
    List<SysAssetType> selectByCompanyId(Integer companyId);

    /**
     * 查询列表
     *
     * @param assetType 查询条件
     * @return 列表
     */
    SysAssetType selectByName(SysAssetType assetType);
}
