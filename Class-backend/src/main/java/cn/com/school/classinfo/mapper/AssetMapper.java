package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.AssetQuery;
import cn.com.school.classinfo.model.Asset;
import cn.com.school.classinfo.vo.AssetVO;

import java.util.List;

/**
 * 估价资产Mapper
 * @author dongpp
 * @date 2018/10/29
 */
public interface AssetMapper {

    /**
     * 插入资产信息
     * @param asset 资产信息
     * @return 数量
     */
    int insert(Asset asset);

    /**
     * 更新资产信息
     * @param asset 资产信息
     * @return 数量
     */
    int update(Asset asset);

    /**
     * 根据id查询记录
     * @param query 查询条件
     * @return 资产信息
     */
    Asset selectById(AssetQuery query);

    /**
     * 查询资产管理记录列表
     * @param query 查询条件
     * @return 资产管理列表
     */
    List<AssetVO> selectByQuery(AssetQuery query);

}
