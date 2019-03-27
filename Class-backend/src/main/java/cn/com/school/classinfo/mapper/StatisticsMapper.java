package cn.com.school.classinfo.mapper;

import cn.com.school.classinfo.common.query.EvalItemsQuery;
import cn.com.school.classinfo.common.query.EvalSummaryQuery;
import cn.com.school.classinfo.vo.EvalItemVO;
import cn.com.school.classinfo.vo.EvalItemsVO;
import cn.com.school.classinfo.vo.EvalSummaryItemVO;
import cn.com.school.classinfo.vo.EvalSummaryVO;
import cn.com.school.classinfo.vo.UserStatisticsVO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 统计Mapper
 *
 * @author dongpp
 * @date 2018/11/23
 */
public interface StatisticsMapper {


    /**
     * 根据机构查询估价汇总信息（总览）
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    @Select("<script>" +
            " SELECT " +
            "     SUM(summary.haTotal) haTotal," +
            "     SUM(summary.rapidTotal) rapidTotal," +
            "     SUM(summary.inquiryTotal) inquiryTotal," +
            "     SUM(summary.advisoryTotal) advisoryTotal," +
            "     SUM(summary.batchTotal) batchTotal" +
            " FROM" +
            "     (SELECT " +
            "             SUM(1) AS haTotal," +
            "             SUM(record.history_count) AS rapidTotal," +
            "             0 AS inquiryTotal," +
            "             0 AS advisoryTotal," +
            "             0 AS batchTotal" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     LEFT JOIN" +
            "         evaluation_record record" +
            "     ON" +
            "         org.id = record.organization_id" +
            "     <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "       <if test='beginDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "       </if>" +
            "       <if test='endDate != null'>" +
            "       <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "       </if>" +
            "       <if test='account != null'>" +
            "           AND record.update_by LIKE CONCAT('%',#{account},'%')" +
            "       </if>" +
            "     </trim>" +
            "     GROUP BY org.id " +
            "     UNION ALL " +
            "     SELECT " +
            "             0," +
            "             0," +
            "             SUM(er.inquiry)," +
            "             SUM(er.advisory)," +
            "             0" +
            "     FROM" +
            "         (SELECT " +
            "             org.id as organizationId," +
            "             CASE report_type" +
            "                 WHEN 1 THEN 1" +
            "                 ELSE 0" +
            "             END AS inquiry," +
            "             CASE report_type" +
            "                 WHEN 2 THEN 1" +
            "                 ELSE 0" +
            "             END AS advisory" +
            "          FROM" +
            "             (SELECT " +
            "                   id, name" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "          LEFT JOIN" +
            "              evaluation_report report" +
            "          ON" +
            "              org.id = report.organization_id" +
            "              <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "                <if test='beginDate != null'>" +
            "                    <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='endDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='account != null'>" +
            "                    AND report.update_by LIKE CONCAT('%',#{account},'%')" +
            "                </if>" +
            "              </trim>" +
            "         ) er" +
            "          GROUP BY er.organizationId " +
            "     UNION ALL" +
            "     SELECT " +
            "             0, 0, 0, 0, SUM(eval_success)" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     LEFT JOIN" +
            "         evaluation_batch batch" +
            "     ON" +
            "         org.id = batch.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(batch.finish_time, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(batch.finish_time, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='account != null'>" +
            "               AND batch.update_by LIKE CONCAT('%',#{account},'%')" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY org.id) summary" +
            "</script>")
    @Results({
            @Result(column = "organizationId", property = "organizationId"),
            @Result(column = "organizationName", property = "organizationName"),
            @Result(column = "haTotal", property = "haTotal"),
            @Result(column = "rapidTotal", property = "rapidTotal"),
            @Result(column = "inquiryTotal", property = "inquiryTotal"),
            @Result(column = "advisoryTotal", property = "advisoryTotal"),
            @Result(column = "batchTotal", property = "batchTotal"),
    })
    EvalSummaryVO selectOrgSummary(EvalSummaryQuery query);

    /**
     * 根据机构查询估价汇总机构列表信息（机构信息）
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    @Select("<script>" +
            " SELECT " +
            "     organizationId," +
            "     max(organizationName) as organizationName," +
            "     SUM(summary.haTotal) ha," +
            "     SUM(summary.rapidTotal) rapid," +
            "     SUM(summary.inquiryTotal) inquiry," +
            "     SUM(summary.advisoryTotal) advisory," +
            "     SUM(summary.batchTotal) batch" +
            " FROM" +
            "     (SELECT " +
            "             org.id as organizationId," +
            "             max(org.name) as organizationName," +
            "             SUM(1) AS haTotal," +
            "             SUM(record.history_count) AS rapidTotal," +
            "             0 AS inquiryTotal," +
            "             0 AS advisoryTotal," +
            "             0 AS batchTotal" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     LEFT JOIN" +
            "         evaluation_record record" +
            "     ON" +
            "         org.id = record.organization_id" +
            "     <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "       <if test='beginDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "       </if>" +
            "       <if test='endDate != null'>" +
            "       <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "       </if>" +
            "     </trim>" +
            "     GROUP BY organizationId " +
            "     UNION ALL " +
            "     SELECT " +
            "             er.organizationId," +
            "             max(er.organizationName)," +
            "             0," +
            "             0," +
            "             SUM(er.inquiry)," +
            "             SUM(er.advisory)," +
            "             0" +
            "     FROM" +
            "         (SELECT " +
            "             org.id as organizationId," +
            "             org.name as organizationName," +
            "             CASE report_type" +
            "                 WHEN 1 THEN 1" +
            "                 ELSE 0" +
            "             END AS inquiry," +
            "             CASE report_type" +
            "                 WHEN 2 THEN 1" +
            "                 ELSE 0" +
            "             END AS advisory" +
            "          FROM" +
            "             (SELECT " +
            "                   id, name" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "          LEFT JOIN" +
            "              evaluation_report report" +
            "          ON" +
            "              org.id = report.organization_id" +
            "              <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "                <if test='beginDate != null'>" +
            "                    <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='endDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "              </trim>" +
            "         ) er" +
            "          GROUP BY er.organizationId " +
            "     UNION ALL" +
            "     SELECT " +
            "             org.id as organizationId," +
            "             max(org.name) as organizationName," +
            "             0, 0, 0, 0, SUM(eval_success)" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     LEFT JOIN" +
            "         evaluation_batch batch" +
            "     ON" +
            "         org.id = batch.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(batch.finish_time, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(batch.finish_time, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY organizationId) summary" +
            " GROUP BY summary.organizationId" +
            "</script>")
    @Results({
            @Result(column = "organizationId", property = "organizationId"),
            @Result(column = "organizationName", property = "organizationName"),
            @Result(column = "ha", property = "ha"),
            @Result(column = "rapid", property = "rapid"),
            @Result(column = "inquiry", property = "inquiry"),
            @Result(column = "advisory", property = "advisory"),
            @Result(column = "batch", property = "batch"),
    })
    List<EvalSummaryItemVO> selectOrgSummaryList(EvalSummaryQuery query);

    /**
     * 根据机构查询估价汇总用户列表信息（用户信息）
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    @Select("<script>" +
            " SELECT " +
            "     max(organizationId) organizationId," +
            "     max(organizationName) as organizationName," +
            "     loginUser," +
            "     max(userName) as userName," +
            "     SUM(summary.haTotal) ha," +
            "     SUM(summary.rapidTotal) rapid," +
            "     SUM(summary.inquiryTotal) inquiry," +
            "     SUM(summary.advisoryTotal) advisory," +
            "     SUM(summary.batchTotal) batch" +
            " FROM" +
            "     (SELECT " +
            "             max(org.id) as organizationId," +
            "             max(org.name) as organizationName," +
            "             record.update_by as loginUser," +
            "             max(record.update_name) as userName," +
            "             SUM(1) AS haTotal," +
            "             SUM(record.history_count) AS rapidTotal," +
            "             0 AS inquiryTotal," +
            "             0 AS advisoryTotal," +
            "             0 AS batchTotal" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     JOIN" +
            "         evaluation_record record" +
            "     ON" +
            "         org.id = record.organization_id" +
            "     <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "       <if test='beginDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "       </if>" +
            "       <if test='endDate != null'>" +
            "       <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "       </if>" +
            "       <if test='account != null'>" +
            "           AND record.update_by LIKE CONCAT('%',#{account},'%')" +
            "       </if>" +
            "     </trim>" +
            "     GROUP BY record.update_by " +
            "     UNION ALL " +
            "     SELECT " +
            "             max(er.organizationId)," +
            "             max(er.organizationName)," +
            "             er.loginUser as loginUser," +
            "             max(er.userName) as userName," +
            "             0," +
            "             0," +
            "             SUM(er.inquiry)," +
            "             SUM(er.advisory)," +
            "             0" +
            "     FROM" +
            "         (SELECT " +
            "             org.id as organizationId," +
            "             org.name as organizationName," +
            "             report.update_by as loginUser," +
            "             report.update_name as userName," +
            "             CASE report_type" +
            "                 WHEN 1 THEN 1" +
            "                 ELSE 0" +
            "             END AS inquiry," +
            "             CASE report_type" +
            "                 WHEN 2 THEN 1" +
            "                 ELSE 0" +
            "             END AS advisory" +
            "          FROM" +
            "             (SELECT " +
            "                   id, name" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "          JOIN" +
            "              evaluation_report report" +
            "          ON" +
            "              org.id = report.organization_id" +
            "              <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "                <if test='beginDate != null'>" +
            "                    <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='endDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='account != null'>" +
            "                    AND report.update_by LIKE CONCAT('%',#{account},'%')" +
            "                </if>" +
            "              </trim>" +
            "         ) er" +
            "          GROUP BY er.loginUser " +
            "     UNION ALL" +
            "     SELECT " +
            "             max(org.id) as organizationId," +
            "             max(org.name) as organizationName," +
            "             batch.update_by as loginUser," +
            "             max(batch.update_name) as userName," +
            "             0, 0, 0, 0, SUM(eval_success)" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     JOIN" +
            "         evaluation_batch batch" +
            "     ON" +
            "         org.id = batch.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(batch.finish_time, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(batch.finish_time, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='account != null'>" +
            "               AND batch.update_by LIKE CONCAT('%',#{account},'%')" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY batch.update_by) summary" +
            " WHERE summary.loginUser is not null " +
            " GROUP BY summary.loginUser" +
            "</script>")
    @Results({
            @Result(column = "organizationId", property = "organizationId"),
            @Result(column = "organizationName", property = "organizationName"),
            @Result(column = "loginUser", property = "loginUser"),
            @Result(column = "userName", property = "userName"),
            @Result(column = "ha", property = "ha"),
            @Result(column = "rapid", property = "rapid"),
            @Result(column = "inquiry", property = "inquiry"),
            @Result(column = "advisory", property = "advisory"),
            @Result(column = "batch", property = "batch"),
    })
    List<EvalSummaryItemVO> selectUserSummaryList(EvalSummaryQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    @Select("<script>" +
            " SELECT " +
            "     sum(loginUserCount)" +
            " FROM" +
            "  (SELECT " +
            "     count(distinct loginUser)loginUserCount " +
            "  FROM" +
            "     (SELECT " +
            "             record.update_by as loginUser" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     JOIN" +
            "         evaluation_record record" +
            "     ON" +
            "         org.id = record.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='account != null'>" +
            "               AND record.update_by LIKE CONCAT('%',#{account},'%')" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY record.update_by " +
            "     UNION ALL " +
            "     SELECT " +
            "             report.update_by as loginUser" +
            "     FROM" +
            "        (SELECT " +
            "              id, name" +
            "         FROM sys_organization" +
            "         WHERE id in" +
            "         <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "            #{id}" +
            "         </foreach>" +
            "        ) org" +
            "     JOIN" +
            "         evaluation_report report" +
            "     ON" +
            "         org.id = report.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='account != null'>" +
            "               AND report.update_by LIKE CONCAT('%',#{account},'%')" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY report.update_by " +
            "     UNION ALL" +
            "     SELECT " +
            "             batch.update_by as loginUser" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     JOIN" +
            "         evaluation_batch batch" +
            "     ON" +
            "         org.id = batch.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(batch.finish_time, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(batch.finish_time, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='account != null'>" +
            "               AND batch.update_by LIKE CONCAT('%',#{account},'%')" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY batch.update_by) summary" +
            " GROUP BY summary.loginUser) summaryCount" +
            "</script>")
    Integer selectUserSummaryTotal(EvalSummaryQuery query);

    /**
     * 查询估价明细-估价方式
     *
     * @param query 查询条件
     * @return 估价明细
     */
    @Select("<script>" +
            " SELECT " +
            "        name organizationName," +
            "        level organizationLevel," +
            "        update_by loginUser," +
            "        update_name userName," +
            "        eval_date evalDate," +
            "        eval_method evalMethod," +
            "        eval_type evalType," +
            "        city_name cityName," +
            "        ha_name haName," +
            "        location," +
            "        bldg_area bldgArea," +
            "        unit_price unitPrice," +
            "        total_price totalPrice" +
            " FROM (" +
            "     SELECT org.name," +
            "            org.level," +
            "            record.update_by," +
            "            record.update_name," +
            "            record.eval_date," +
            "            1 as eval_method," +
            "            record.eval_type," +
            "            record.city_name," +
            "            record.ha_name," +
            "            record.location," +
            "            record.bldg_area," +
            "            record.unit_price," +
            "            record.total_price" +
            "     FROM" +
            "             (SELECT " +
            "                   id, name, level" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "            JOIN evaluation_record record" +
            "                   ON org.id = record.organization_id" +
            "              <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "                <if test='beginDate != null'>" +
            "                    <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='endDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='account != null'>" +
            "                    AND record.update_by LIKE CONCAT('%',#{account},'%')" +
            "                </if>" +
            "              </trim>" +
            "     UNION ALL" +
            "     SELECT org.name," +
            "            org.level," +
            "            history.update_by," +
            "            history.update_name," +
            "            history.eval_date," +
            "            1 as eval_method," +
            "            history.eval_type," +
            "            history.city_name," +
            "            history.ha_name," +
            "            history.location," +
            "            history.bldg_area," +
            "            history.unit_price," +
            "            history.total_price" +
            "     FROM" +
            "             (SELECT " +
            "                   id, name, level" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "            JOIN evaluation_record_history history" +
            "                   ON org.id = history.organization_id" +
            "              <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "                <if test='beginDate != null'>" +
            "                    <![CDATA[ AND DATE_FORMAT(history.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='endDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(history.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='account != null'>" +
            "                    AND history.update_by LIKE CONCAT('%',#{account},'%')" +
            "                </if>" +
            "              </trim>" +
            "     UNION ALL" +
            "     SELECT org.name," +
            "            org.level," +
            "            item.update_by," +
            "            item.update_name," +
            "            item.eval_date," +
            "            2 as eval_method," +
            "            1 as eval_type," +
            "            item.city_name," +
            "            item.ha_name," +
            "            item.location," +
            "            item.bldg_area," +
            "            item.unit_price," +
            "            item.total_price" +
            "     FROM" +
            "             (SELECT " +
            "                   id, name, level" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "             JOIN evaluation_batch_item item" +
            "                   ON org.id = item.organization_id" +
            "              <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "                and item.eval_state = 1" +
            "                <if test='beginDate != null'>" +
            "                    <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='endDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='account != null'>" +
            "                    AND item.update_by LIKE CONCAT('%',#{account},'%')" +
            "                </if>" +
            "              </trim>" +
            "  ) statReport order by level, eval_date desc" +
            "</script>")
    @Results({
            @Result(column = "organizationName", property = "organizationName"),
            @Result(column = "userName", property = "userName"),
            @Result(column = "evalDate", property = "evalDate"),
            @Result(column = "evalMethod", property = "evalMethod"),
            @Result(column = "evalType", property = "evalType"),
            @Result(column = "haName", property = "haName"),
            @Result(column = "location", property = "location"),
            @Result(column = "bldgArea", property = "bldgArea"),
            @Result(column = "unitPrice", property = "unitPrice"),
            @Result(column = "totalPrice", property = "totalPrice"),
    })
    List<EvalItemVO> selectItemsByEval(EvalItemsQuery query);

    /**
     * 查询估价明细-估价方式（总览）
     *
     * @param query 查询条件
     * @return 估价明细
     */
    @Select("<script>" +
            " SELECT " +
            "       sum(ha) haTotal," +
            "       sum(rapid) rapidTotal," +
            "       sum(batch) batchTotal" +
            " FROM (" +
            "     SELECT " +
            "           1 as ha," +
            "           record.history_count as rapid," +
            "           0 as batch" +
            "     FROM" +
            "             (SELECT " +
            "                   id, name, level" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "             JOIN evaluation_record record" +
            "                   ON org.id = record.organization_id" +
            "              <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "                <if test='beginDate != null'>" +
            "                    <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='endDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='account != null'>" +
            "                    AND record.update_by LIKE CONCAT('%',#{account},'%')" +
            "                </if>" +
            "              </trim>" +
            "     UNION ALL" +
            "     SELECT " +
            "           0 as ha," +
            "           0 as rapid," +
            "           1 as batch" +
            "     FROM" +
            "             (SELECT " +
            "                   id, name, level" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "            JOIN evaluation_batch_item item" +
            "                   ON org.id = item.organization_id" +
            "              <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "                and item.eval_state = 1" +
            "                <if test='beginDate != null'>" +
            "                    <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='endDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='account != null'>" +
            "                    AND item.update_by LIKE CONCAT('%',#{account},'%')" +
            "                </if>" +
            "              </trim>" +
            "  ) statReport" +
            "</script>")
    @Results({
            @Result(column = "haTotal", property = "haTotal"),
            @Result(column = "rapidTotal", property = "rapidTotal"),
            @Result(column = "batchTotal", property = "batchTotal"),
    })
    EvalItemsVO selectItemsByEvalTotal(EvalItemsQuery query);

    /**
     * 查询估价明细-批量估价
     *
     * @param query 查询条件
     * @return 估价明细
     */
    @Select("<script> " +
            " SELECT" +
            "        org.name organizationName," +
            "        org.level organizationLevel," +
            "        item.update_by loginUser," +
            "        item.update_name userName," +
            "        item.eval_date evalDate," +
            "        2 evalMethod," +
            "        1 evalType," +
            "        item.city_name cityName," +
            "        item.ha_name haName," +
            "        item.location location," +
            "        item.bldg_area bldgArea," +
            "        item.unit_price unitPrice," +
            "        item.total_price totalPrice" +
            " FROM" +
            "         (SELECT " +
            "               id, name, level" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "         JOIN evaluation_batch_item item" +
            "               ON org.id = item.organization_id" +
            "  <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "    <if test='beginDate != null'>" +
            "        <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "    </if>" +
            "    <if test='endDate != null'>" +
            "    <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "    </if>" +
            "    <if test='account != null'>" +
            "        AND item.update_by LIKE CONCAT('%',#{account},'%')" +
            "    </if>" +
            "   and item.update_by is not null" +
            "   and item.eval_state = 1" +
            "  </trim>" +
            "  ORDER BY org.level, item.eval_date desc" +
            "</script>")
    @Results({
            @Result(column = "organizationName", property = "organizationName"),
            @Result(column = "userName", property = "userName"),
            @Result(column = "evalDate", property = "evalDate"),
            @Result(column = "evalMethod", property = "evalMethod"),
            @Result(column = "evalType", property = "evalType"),
            @Result(column = "haName", property = "haName"),
            @Result(column = "location", property = "location"),
            @Result(column = "bldgArea", property = "bldgArea"),
            @Result(column = "unitPrice", property = "unitPrice"),
            @Result(column = "totalPrice", property = "totalPrice"),
    })
    List<EvalItemVO> selectItemsByBatch(EvalItemsQuery query);

    /**
     * 查询估价明细-快速估价
     *
     * @param query 查询条件
     * @return 估价明细
     */
    @Select("<script> " +
            " SELECT " +
            "        org.name organizationName," +
            "        org.level organizationLevel," +
            "        record.update_by loginUser," +
            "        record.update_name userName," +
            "        record.eval_date evalDate," +
            "        1 evalMethod," +
            "        record.eval_type evalType," +
            "        record.city_name cityName," +
            "        record.ha_name haName," +
            "        record.location location," +
            "        record.bldg_area bldgArea," +
            "        record.unit_price unitPrice," +
            "        record.total_price totalPrice" +
            " FROM" +
            "         (SELECT " +
            "               id, name, level" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "        JOIN evaluation_record record" +
            "               ON org.id = record.organization_id" +
            " <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "   <if test='beginDate != null'>" +
            "       <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "   </if>" +
            "   <if test='endDate != null'>" +
            "   <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "   </if>" +
            "   <if test='account != null'>" +
            "       AND record.update_by LIKE CONCAT('%',#{account},'%')" +
            "   </if>" +
            " </trim>" +
            "  ORDER BY org.level, record.eval_date desc" +
            "</script>")
    @Results({
            @Result(column = "organizationName", property = "organizationName"),
            @Result(column = "userName", property = "userName"),
            @Result(column = "evalDate", property = "evalDate"),
            @Result(column = "evalMethod", property = "evalMethod"),
            @Result(column = "evalType", property = "evalType"),
            @Result(column = "haName", property = "haName"),
            @Result(column = "location", property = "location"),
            @Result(column = "bldgArea", property = "bldgArea"),
            @Result(column = "unitPrice", property = "unitPrice"),
            @Result(column = "totalPrice", property = "totalPrice"),
    })
    List<EvalItemVO> selectItemsByRapid(EvalItemsQuery query);

    /**
     * 查询估价明细-估价报告
     *
     * @param query 查询条件
     * @return 估价明细
     */
    @Select("<script>" +
            " SELECT name organizationName," +
            "        level organizationLevel," +
            "        update_by loginUser," +
            "        update_name userName," +
            "        eval_date evalDate," +
            "        report_type evalMethod," +
            "        eval_type evalType," +
            "        city_name cityName," +
            "        ha_name haName," +
            "        location," +
            "        bldg_area bldgArea," +
            "        unit_price unitPrice," +
            "        total_price totalPrice" +
            " FROM (" +
            "     SELECT org.name," +
            "            org.level," +
            "            report.update_by," +
            "            report.update_name," +
            "            report.eval_date," +
            "            report.report_type," +
            "            record.eval_type," +
            "            record.city_name," +
            "            record.ha_name," +
            "            record.location," +
            "            record.bldg_area," +
            "            report.unit_price," +
            "            report.total_price" +
            "     FROM" +
            "            (SELECT " +
            "                  id, name, level" +
            "             FROM sys_organization" +
            "             WHERE id in" +
            "             <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                #{id}" +
            "             </foreach>" +
            "            ) org" +
            "            JOIN evaluation_record record" +
            "              ON org.id = record.organization_id" +
            "            JOIN evaluation_report report" +
            "              ON record.eval_code = report.eval_code" +
            "             <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "               <if test='beginDate != null'>" +
            "                   <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "               </if>" +
            "               <if test='endDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "               </if>" +
            "               <if test='account != null'>" +
            "                   AND report.update_by LIKE CONCAT('%',#{account},'%')" +
            "               </if>" +
            "               <if test=\"statSubMethod == '21'\">" +
            "                   AND report.report_type = 1" +
            "               </if>" +
            "               <if test=\"statSubMethod == '22'\">" +
            "                   AND report.report_type = 2" +
            "               </if>" +
            "             </trim>" +
            "     UNION ALL" +
            "     SELECT org.name," +
            "            org.level," +
            "            report.update_by," +
            "            report.update_name," +
            "            report.eval_date," +
            "            report.report_type," +
            "            history.eval_type," +
            "            history.city_name," +
            "            history.ha_name," +
            "            history.location," +
            "            history.bldg_area," +
            "            report.unit_price," +
            "            report.total_price" +
            "     FROM" +
            "            (SELECT " +
            "                  id, name, level" +
            "             FROM sys_organization" +
            "             WHERE id in" +
            "             <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                #{id}" +
            "             </foreach>" +
            "            ) org" +
            "            JOIN evaluation_record_history history" +
            "              ON org.id = history.organization_id" +
            "            JOIN evaluation_report report" +
            "              ON history.eval_code = report.eval_code" +
            "             <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "               <if test='beginDate != null'>" +
            "                   <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "               </if>" +
            "               <if test='endDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "               </if>" +
            "               <if test='account != null'>" +
            "                   AND report.update_by LIKE CONCAT('%',#{account},'%')" +
            "               </if>" +
            "               <if test=\"statSubMethod == '21'\">" +
            "                   AND report.report_type = 1" +
            "               </if>" +
            "               <if test=\"statSubMethod == '22'\">" +
            "                   AND report.report_type = 2" +
            "               </if>" +
            "             </trim>" +
            "  ) statReport order by level, eval_date" +
            "</script>")
    @Results({
            @Result(column = "organizationName", property = "organizationName"),
            @Result(column = "userName", property = "userName"),
            @Result(column = "evalDate", property = "evalDate"),
            @Result(column = "evalMethod", property = "evalMethod"),
            @Result(column = "evalType", property = "evalType"),
            @Result(column = "haName", property = "haName"),
            @Result(column = "location", property = "location"),
            @Result(column = "bldgArea", property = "bldgArea"),
            @Result(column = "unitPrice", property = "unitPrice"),
            @Result(column = "totalPrice", property = "totalPrice"),
    })
    List<EvalItemVO> selectItemsByReport(EvalItemsQuery query);

    /**
     * 查询估价明细-估价报告（总览）
     *
     * @param query 查询条件
     * @return 总览信息
     */
    @Select("<script>" +
            " SELECT " +
            "     SUM(summary.haTotal) haTotal," +
            "     SUM(summary.inquiryTotal) inquiryTotal," +
            "     SUM(summary.advisoryTotal) advisoryTotal" +
            " FROM" +
            "     (SELECT " +
            "             SUM(1) AS haTotal," +
            "             0 AS inquiryTotal," +
            "             0 AS advisoryTotal" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     LEFT JOIN" +
            "         evaluation_record record" +
            "     ON" +
            "         org.id = record.organization_id" +
            "          <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "            <if test='beginDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "            </if>" +
            "            <if test='endDate != null'>" +
            "            <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "            </if>" +
            "            <if test='account != null'>" +
            "                AND record.update_by LIKE CONCAT('%',#{account},'%')" +
            "            </if>" +
            "          </trim>" +
            "     GROUP BY org.id " +
            "     UNION ALL " +
            "     SELECT " +
            "             0," +
            "             SUM(er.inquiry)," +
            "             SUM(er.advisory)" +
            "     FROM" +
            "         (SELECT " +
            "             org.id as organizationId," +
            "             CASE report_type" +
            "                 WHEN 1 THEN 1" +
            "                 ELSE 0" +
            "             END AS inquiry," +
            "             CASE report_type" +
            "                 WHEN 2 THEN 1" +
            "                 ELSE 0" +
            "             END AS advisory" +
            "          FROM" +
            "             (SELECT " +
            "                   id, name" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "          LEFT JOIN" +
            "              evaluation_report report" +
            "          ON" +
            "              org.id = report.organization_id" +
            "          <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "            <if test='beginDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "            </if>" +
            "            <if test='endDate != null'>" +
            "            <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "            </if>" +
            "            <if test='account != null'>" +
            "                AND report.update_by LIKE CONCAT('%',#{account},'%')" +
            "            </if>" +
            "            <if test=\"statSubMethod == '21'\">" +
            "                AND report.report_type = 1" +
            "            </if>" +
            "            <if test=\"statSubMethod == '22'\">" +
            "                AND report.report_type = 2" +
            "            </if>" +
            "          </trim>" +
            "         ) er" +
            "          GROUP BY er.organizationId " +
            ") summary" +
            "</script>")
    @Results({
            @Result(column = "haTotal", property = "haTotal"),
            @Result(column = "inquiryTotal", property = "inquiryTotal"),
            @Result(column = "advisoryTotal", property = "advisoryTotal"),
    })
    EvalItemsVO selectItemsByReportTotal(EvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    @Select("<script>" +
            " SELECT " +
            "     count(distinct loginUser)" +
            " FROM" +
            "     (" +
            "     SELECT " +
            "             record.update_by as loginUser" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     LEFT JOIN" +
            "         evaluation_record record" +
            "     ON" +
            "         org.id = record.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='account != null'>" +
            "               AND record.update_by LIKE CONCAT('%',#{account},'%')" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY record.update_by " +
            "     UNION ALL" +
            "     SELECT " +
            "             item.update_by as loginUser" +
            "     FROM" +
            "             (SELECT " +
            "                   id, name, level" +
            "              FROM sys_organization" +
            "              WHERE id in" +
            "              <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "                 #{id}" +
            "              </foreach>" +
            "             ) org" +
            "             JOIN evaluation_batch_item item" +
            "                   ON org.id = item.organization_id" +
            "              <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "                and item.eval_state = 1" +
            "                <if test='beginDate != null'>" +
            "                    <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='endDate != null'>" +
            "                <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "                </if>" +
            "                <if test='account != null'>" +
            "                    AND item.update_by LIKE CONCAT('%',#{account},'%')" +
            "                </if>" +
            "              </trim>" +
            "     GROUP BY item.update_by" +
            ") summary" +
            "</script>")
    Integer selectItemsEvalUsers(EvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    @Select("<script>" +
            " SELECT " +
            "     count(loginUser)" +
            " FROM" +
            "     (SELECT " +
            "             record.update_by as loginUser" +
            "     FROM" +
            "         (SELECT " +
            "               id, name" +
            "          FROM sys_organization" +
            "          WHERE id in" +
            "          <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "             #{id}" +
            "          </foreach>" +
            "         ) org" +
            "     LEFT JOIN" +
            "         evaluation_record record" +
            "     ON" +
            "         org.id = record.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(record.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='account != null'>" +
            "               AND record.update_by LIKE CONCAT('%',#{account},'%')" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY record.update_by " +
            ") summary" +
            "</script>")
    Integer selectItemsRecordUsers(EvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    @Select("<script>" +
            " SELECT " +
            "     count(loginUser)" +
            " FROM" +
            "     (SELECT " +
            "             report.update_by as loginUser" +
            "     FROM" +
            "        (SELECT " +
            "              id, name" +
            "         FROM sys_organization" +
            "         WHERE id in" +
            "         <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "            #{id}" +
            "         </foreach>" +
            "        ) org" +
            "     LEFT JOIN" +
            "         evaluation_report report" +
            "     ON" +
            "         org.id = report.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(report.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='account != null'>" +
            "               AND report.update_by LIKE CONCAT('%',#{account},'%')" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY report.update_by " +
            ") summary" +
            "</script>")
    Integer selectItemsReportUsers(EvalItemsQuery query);

    /**
     * 根据机构查询估价汇总用户数量
     *
     * @param query 查询条件
     * @return 汇总列表
     */
    @Select("<script>" +
            " SELECT " +
            "     count(loginUser)" +
            " FROM" +
            "     (SELECT " +
            "             item.update_by as loginUser" +
            "     FROM" +
            "        (SELECT " +
            "              id, name, level" +
            "         FROM sys_organization" +
            "         WHERE id in" +
            "         <foreach collection='organizationIds' item='id' open='(' close=')' separator=','>" +
            "            #{id}" +
            "         </foreach>" +
            "        ) org" +
            "        JOIN evaluation_batch_item item" +
            "              ON org.id = item.organization_id" +
            "         <trim prefix='WHERE' prefixOverrides='AND |OR'>" +
            "           and item.eval_state = 1" +
            "           <if test='beginDate != null'>" +
            "               <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') >= DATE_FORMAT(#{beginDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='endDate != null'>" +
            "           <![CDATA[ AND DATE_FORMAT(item.eval_date, '%Y-%m-%d') <= DATE_FORMAT(#{endDate}, '%Y-%m-%d')]]>" +
            "           </if>" +
            "           <if test='account != null'>" +
            "               AND item.update_by LIKE CONCAT('%',#{account},'%')" +
            "           </if>" +
            "         </trim>" +
            "     GROUP BY item.update_by) summary" +
            "</script>")
    Integer selectItemsBatchUsers(EvalItemsQuery query);

    /**
     * 统计当前登录用户的估价信息
     *
     * @param loginUser 登录用户名
     * @return 统计信息
     */
    @Select(" SELECT Sum(rapid) rapidTotal," +
            "       Sum(batch) batchTotal," +
            "       Sum(inquiry) inquiryTotal," +
            "       Sum(advisory) advisoryTotal" +
            " FROM   (SELECT Sum(history_count) rapid," +
            "               0                  batch," +
            "               0                  inquiry," +
            "               0                  advisory" +
            "        FROM   `evaluation_record`" +
            "        WHERE  update_by = #{loginUser}" +
            "           AND company_id = #{companyId}" +
            "        UNION ALL" +
            "        SELECT 0," +
            "               Sum(eval_success)," +
            "               0," +
            "               0" +
            "        FROM   evaluation_batch" +
            "        WHERE  update_by = #{loginUser}" +
            "           AND company_id = #{companyId}" +
            "        UNION ALL" +
            "        SELECT 0," +
            "               0," +
            "               Sum(1)," +
            "               0" +
            "        FROM   evaluation_report" +
            "        WHERE  update_by = #{loginUser}" +
            "           AND report_type = 1" +
            "           AND company_id = #{companyId}" +
            "        UNION ALL" +
            "        SELECT 0," +
            "               0," +
            "               0," +
            "               Sum(1)" +
            "        FROM   evaluation_report" +
            "        WHERE  update_by = #{loginUser}" +
            "           AND report_type = 2" +
            "           AND company_id = #{companyId}" +
            "           ) s")
    @Results({
            @Result(column = "rapidTotal", property = "rapidTotal"),
            @Result(column = "batchTotal", property = "batchTotal"),
            @Result(column = "inquiryTotal", property = "inquiryTotal"),
            @Result(column = "advisoryTotal", property = "advisoryTotal"),
    })
    UserStatisticsVO selectUserStatistics(String loginUser, Integer companyId);
}
