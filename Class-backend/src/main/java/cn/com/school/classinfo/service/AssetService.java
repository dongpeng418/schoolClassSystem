package cn.com.school.classinfo.service;

import cn.com.school.classinfo.common.query.AssetQuery;
import cn.com.school.classinfo.model.Asset;
import cn.com.school.classinfo.vo.AssetVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 资产Service
 * @author dongpp
 * @date 2018/10/29
 */
public interface AssetService {

    /**
     * 保存资产管理信息
     * @param asset 资产管理信息
     */
    void save(Asset asset);

    /**
     * 更新资产管理信息
     * @param asset 资产管理信息
     */
    void update(Asset asset);

    /**
     * 根据ID获取资产信息
     * @param query 查询条件
     * @return 资产信息
     */
    Asset getById(AssetQuery query);

    /**
     * 查询资产管理列表
     * @param query 查询条件
     * @return 资产管理列表
     */
    PageInfo<AssetVO> getListByQuery(AssetQuery query);

    /**
     * 查询资产管理列表
     * @param query 查询条件
     * @return 资产管理列表
     */
    List<AssetVO> getAllListByQuery(AssetQuery query);
}
