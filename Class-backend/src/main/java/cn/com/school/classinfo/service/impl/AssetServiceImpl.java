package cn.com.school.classinfo.service.impl;

import cn.com.school.classinfo.common.query.AssetQuery;
import cn.com.school.classinfo.mapper.AssetMapper;
import cn.com.school.classinfo.model.Asset;
import cn.com.school.classinfo.service.AssetService;
import cn.com.school.classinfo.service.RiskCoefficientService;
import cn.com.school.classinfo.vo.AssetVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资产管理服务实现类
 * @author dongpp
 * @date 2018/10/29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AssetServiceImpl implements AssetService {

    private final AssetMapper assetMapper;

    private final RiskCoefficientService riskCoefficientService;

    @Autowired
    public AssetServiceImpl(AssetMapper assetMapper,
                            RiskCoefficientService riskCoefficientService) {
        this.assetMapper = assetMapper;
        this.riskCoefficientService = riskCoefficientService;
    }

    /**
     * 保存资产管理信息
     * @param asset 资产管理信息
     */
    @Override
    public void save(Asset asset){
        assetMapper.insert(asset);
    }

    /**
     * 更新资产管理信息
     * @param asset 资产管理信息
     */
    @Override
    public void update(Asset asset){
        assetMapper.update(asset);
    }

    @Override
    public Asset getById(AssetQuery query) {
        return assetMapper.selectById(query);
    }

    /**
     * 查询资产管理列表 - 分页
     * @param query 查询条件
     * @return 资产管理列表
     */
    @Override
    public PageInfo<AssetVO> getListByQuery(AssetQuery query){
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        List<AssetVO> assetList = assetMapper.selectByQuery(query);
        return new PageInfo<>(assetList);
    }

    /**
     * 查询资产管理列表
     * @param query 查询条件
     * @return 资产管理列表
     */
    @Override
    public List<AssetVO> getAllListByQuery(AssetQuery query){
        return assetMapper.selectByQuery(query);
    }


}
