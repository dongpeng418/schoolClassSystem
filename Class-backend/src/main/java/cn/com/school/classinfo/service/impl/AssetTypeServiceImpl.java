package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.query.BaseQuery;
import cn.com.school.classinfo.mapper.AssetTypeMapper;
import cn.com.school.classinfo.model.SysAssetType;
import cn.com.school.classinfo.service.AssetTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资产类型服务实现类
 *
 * @author dongpp
 * @date 2018/11/21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AssetTypeServiceImpl implements AssetTypeService {

    private final AssetTypeMapper assetTypeMapper;

    @Autowired
    public AssetTypeServiceImpl(AssetTypeMapper assetTypeMapper) {
        this.assetTypeMapper = assetTypeMapper;
    }

    /**
     * 增加资产类型信息
     *
     * @param assetType 资产类型信息
     */
    @Override
    public void add(SysAssetType assetType) {
        assetTypeMapper.insert(assetType);
    }

    /**
     * 保存资产类型信息
     *
     * @param assetType 资产类型信息
     */
    @Override
    public void save(SysAssetType assetType) {
        assetTypeMapper.update(assetType);
    }

    /**
     * 删除资产类型信息
     *
     * @param assetType 资产类型信息
     */
    @Override
    public void delete(SysAssetType assetType) {
        assetTypeMapper.delete(assetType);
    }

    /**
     * 根据资产类型名称获取资产类型信息
     *
     * @param name 资产类型名称
     */
    @Override
    public SysAssetType getByName(String name) {
        SysAssetType type = new SysAssetType();
        type.setAssetName(name);
        type.setCompanyId(CommonUtil.getLoginUser().getCustomerCompanyId());
        return assetTypeMapper.selectByName(type);
    }

    /**
     * 获取资产类型信息列表
     *
     * @return 列表
     */
    @Override
    public List<SysAssetType> list() {
        return assetTypeMapper.selectByCompanyId(CommonUtil.getLoginUser().getCustomerCompanyId());
    }

    /**
     * 获取资产类型信息列表
     *
     * @param query 查询条件
     * @return 列表
     */
    @Override
    public PageInfo<SysAssetType> pageable(BaseQuery query) {
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        return new PageInfo<>(assetTypeMapper.selectByCompanyId(CommonUtil.getLoginUser().getCustomerCompanyId()));
    }
}
