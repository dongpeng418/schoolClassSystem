package cn.com.school.classinfo.common.constant;

/**
 * 批量估价常量
 * @author dongpp
 * @date 2018/10/26
 */
public class BatchConstant {

    /**
     * 批量上传文件的路径
     */
    public static final String UPLOAD_PATH = "upload/";

    /**
     * 模板文件名
     */
    public static final String TEMPLATE_NAME = "template.xlsx";

    /**
     * 模板下载文件名称
     */
    public static final String TEMPLATE_DOWNLOAD_NAME = "房产批量导入模版.xlsx";

    /**
     * 批量估价进行中
     */
    public static final Integer TASK_PROGRESS = 1;

    /**
     * 批量估价已完成
     */
    public static final Integer TASK_FINISH = 2;

    /**
     * 批量估价错误
     */
    public static final Integer TASK_ERROR = 3;

    /**
     * 批量估价条目-未评估
     */
    public static final Integer ITEM_UNEVAL = 0;

    /**
     * 批量估价条目-估价成功
     */
    public static final Integer ITEM_SUCCESS = 1;

    /**
     * 批量估价条目-估价失败
     */
    public static final Integer ITEM_FAILED = 2;

    /**
     * 批量估价条目-解析失败
     */
    public static final Integer ITEM_ERROR = 3;

}
