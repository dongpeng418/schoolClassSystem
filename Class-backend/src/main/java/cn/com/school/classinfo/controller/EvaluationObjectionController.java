package cn.com.school.classinfo.controller;

import cn.com.school.classinfo.common.CommonUtil;
import cn.com.school.classinfo.common.ResultMessage;
import cn.com.school.classinfo.common.constant.ObjectionConstant;
import cn.com.school.classinfo.common.emum.CreateUpdateEnum;
import cn.com.school.classinfo.common.query.EvalQuery;
import cn.com.school.classinfo.common.query.ObjectionQuery;
import cn.com.school.classinfo.model.EvaluationObjection;
import cn.com.school.classinfo.model.EvaluationRecord;
import cn.com.school.classinfo.model.EvaluationRecordHistory;
import cn.com.school.classinfo.model.SysUser;
import cn.com.school.classinfo.service.EvaluationObjectionService;
import cn.com.school.classinfo.service.EvaluationService;
import cn.com.school.classinfo.utils.DateUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * 估价异议
 * @author dongpp
 * @date 2018/10/28
 */
@Api(tags = "估价异议接口", position = 7)
@RestController
@RequestMapping("/api/objection")
public class EvaluationObjectionController {

    private final EvaluationObjectionService evaluationObjectionService;

    private final EvaluationService evaluationService;

    @Autowired
    public EvaluationObjectionController(EvaluationObjectionService evaluationObjectionService,
                                         EvaluationService evaluationService) {
        this.evaluationObjectionService = evaluationObjectionService;
        this.evaluationService = evaluationService;
    }

    /**
     * 添加估价异议
     * @param evalCode 估价唯一码
     * @param price 价格
     * @param type 异议类型
     * @return 结果
     */
    @ApiOperation(value = "增加估价异议")
    @PostMapping("/add")
    public ResultMessage addObjection(@ApiParam(value = "估价记录唯一码", required = true)
                                          @NotBlank(message = "evalCode 不能为空")
                                          @RequestParam String evalCode,
                                      @ApiParam(value = "异议价格", required = true)
                                          @NotNull(message = "price 不能为空")
                                          @RequestParam Double price,
                                      @ApiParam(value = "异议类型", required = true)
                                          @NotBlank(message = "type 不能为空")
                                          @RequestParam String type){
        EvalQuery query = new EvalQuery();
        query.setEvalCode(evalCode);
        //根据当前用户查询信息
        SysUser user = CommonUtil.getLoginUser();
        query.setLoginUser(user.getLoginUser());

        EvaluationObjection objection = new EvaluationObjection();
        EvaluationRecord record = evaluationService.getRecordByEvalCode(query);
        if(Objects.isNull(record)){
            EvaluationRecordHistory history = evaluationService.getRecordHistoryByEvalCode(query);
            if(Objects.nonNull(history)){
                BeanUtils.copyProperties(history, objection);
                objection.setEvalId(history.getId());
            }else{
                return ResultMessage.paramError("evalCode");
            }
        }else{
            BeanUtils.copyProperties(record, objection);
            objection.setEvalId(record.getId());
        }
        objection.setId(null);
        objection.setEvalCode(evalCode);
        objection.setActualPrice(price);
        objection.setObjectionReason(type);
        objection.setObjectionTime(DateUtil.now());
        objection.setState(ObjectionConstant.PENDING_REPLY);
        CommonUtil.doEvalCreateUpdateInfo(objection, CreateUpdateEnum.CREATE);
        evaluationObjectionService.save(objection);
        return ResultMessage.success();
    }

    /**
     * 估价异议详情
     * @param id id
     * @return 异议详情
     */
    @ApiOperation(value = "估价异议详情")
    @GetMapping("/detail")
    public ResultMessage get(@ApiParam(value = "异议记录ID", required = true) @RequestParam Integer id){
        if(Objects.isNull(id)){
            return ResultMessage.paramError("id");
        }
        //根据当前用户查询信息
        SysUser user = CommonUtil.getLoginUser();
        ObjectionQuery objectionQuery = new ObjectionQuery();
        objectionQuery.setId(id);
        objectionQuery.setLoginUser(user.getLoginUser());
        EvaluationObjection objection = evaluationObjectionService.getById(objectionQuery);
        if(Objects.isNull(objection)){
            return ResultMessage.paramError("id");
        }
        EvalQuery query = new EvalQuery();
        query.setEvalCode(objection.getEvalCode());
        query.setLoginUser(user.getLoginUser());
        Map<String, Object> map = Maps.newHashMap();
        EvaluationRecord record = evaluationService.getRecordByEvalCode(query);
        if(Objects.isNull(record)){
            EvaluationRecordHistory history = evaluationService.getRecordHistoryByEvalCode(query);
            if(Objects.nonNull(history)){
                map.put("record", history);
            }
        }else{
            map.put("record", record);
        }
        map.put("objection", objection);
        return ResultMessage.success(map);
    }

    /**
     * 估价异议列表
     * @param query 查询条件
     * @return 列表
     */
    @ApiOperation(value = "估价异议列表")
    @GetMapping("/list")
    public ResultMessage list(@Valid @ModelAttribute ObjectionQuery query){
        //根据当前用户查询信息
        query.authInfo();
        PageInfo<EvaluationObjection> objectionList = evaluationObjectionService.getList(query);
        return ResultMessage.success(objectionList);
    }
}
