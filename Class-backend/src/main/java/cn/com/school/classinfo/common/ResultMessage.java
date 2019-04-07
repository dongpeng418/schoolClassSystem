package cn.com.school.classinfo.common;

import com.fasterxml.jackson.databind.JsonNode;

import cn.com.school.classinfo.utils.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 接口统一返回结果对象
 *
 * @author dongpp
 * @date 2018/10/22
 */
@Data
@ApiModel("响应结果对象")
public class ResultMessage {

    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 请求错误
     */
    public static final int PARAMETER_ERROR = 400;

    /**
     * 权限错误
     */
    public static final int AUTH_ERROR = 401;

    /**
     * API接口错误
     */
    public static final int API_ERROR = 402;

    /**
     * 重复校验错误
     */
    public static final int DUPLICATE_ERROR = 403;

    /**
     * 其它服务错误
     */
    public static final int SERVER_ERROR = 500;

    /**
     * 状态码
     */
    @ApiModelProperty("响应码")
    private int code = SUCCESS;

    /**
     * 返回消息
     */
    @ApiModelProperty("响应消息")
    private String message = "SUCCESS";

    /**
     * 返回数据
     */
    @ApiModelProperty("响应数据")
    private Object data;

    private ResultMessage(){}

    /**
     * 成功
     *
     * @return ResultMessage
     */
    public static ResultMessage success() {
        return new ResultMessage();
    }

    /**
     * 成功
     *
     * @return ResultMessage
     */
    public static ResultMessage success(String message) {
        return success().message(message);
    }

    /**
     * 成功
     *
     * @return ResultMessage
     */
    public static ResultMessage success(Object data) {
        return success().data(data);
    }

    /**
     * 中转接口错误时响应消息
     *
     * @param errJson 错误消息
     * @return ResultMessage
     */
    public static ResultMessage apiError(String errJson) {
        JsonNode rootNode = JsonUtil.readTree(errJson).get("error");
        String errMsg = rootNode.get("detail").textValue();
        return new ResultMessage()
                        .code(API_ERROR)
                        .message(errMsg);
    }

    /**
     * 请求错误
     * @return ResultMessage
     */
    public static ResultMessage requestError(String message) {
        return new ResultMessage()
                        .code(PARAMETER_ERROR)
                        .message(message);
    }

    /**
     * 参数错误
     * @return ResultMessage
     */
    public static ResultMessage paramError() {
        return paramError(null);
    }

    /**
     * 参数错误
     * @return ResultMessage
     */
    public static ResultMessage paramError(String param){
        return new ResultMessage()
                        .code(PARAMETER_ERROR)
                        .message("参数错误" + (param == null ? "" : "：" + param));
    }

    /**
     * 权限错误
     * @return ResultMessage
     */
    public static ResultMessage authError(String message){
        return new ResultMessage()
                        .code(AUTH_ERROR)
                        .message(message);
    }

    /**
     * 权限错误
     * @return ResultMessage
     */
    public static ResultMessage duplicateError(String message){
        return new ResultMessage()
                        .code(DUPLICATE_ERROR)
                        .message(message);
    }

    /**
     * 服务器错误
     * @return ResultMessage
     */
    public static ResultMessage serverError(String message){
        return new ResultMessage()
                        .code(SERVER_ERROR)
                        .message(message);
    }

    /**
     * 根据中转接口返回结果转换响应消息
     * @param result 中转接口返回结果信息
     * @return 响应消息
     */
    public static ResultMessage transfer(Pair<Boolean, String> result){
        if(result.getLeft()){
            return new ResultMessage().data(JsonUtil.readTree(result.getRight()));
        }else{
            return ResultMessage.apiError(result.getRight());
        }
    }

    public ResultMessage code(int code){
        this.code = code;
        return this;
    }

    public ResultMessage message(String message){
        this.message = message;
        return this;
    }

    public ResultMessage data(Object data){
        this.data = data;
        return this;
    }
}
