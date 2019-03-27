<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>房地产价格咨询报告</title>
    <link rel="stylesheet" type="text/css" href="${resourcePath}template/css/advisory.css">
    <link rel="stylesheet" href="${resourcePath}template/fonts/iconfont.css">
</head>
<body>
<div>
    <div id="print_area">
        <div class="advisoryReport" id="mainC">
            <div class="main_body">
                <div class="header">
                    <div class="title">
                        <#if reportConfig.reportName?? && reportConfig.reportName?length gt 0>
                            ${reportConfig.reportName}
                        <#else>
                            房地产价格咨询报告
                        </#if>
                    </div>
                    <div class="report_num">评估报告编号：${report.reportCode!""}</div>
                </div>
                <div class="body">
                    <div class="info">
                        <#if record.evalType == 1>
                            <div class="item">
                                <div class="item_name">总价</div>
                                <div class="item_price">
                                    <span>${record.totalPrice?round?string["0"]}</span>万元
                                </div>
                            </div>
                            <div class="item">
                                <div class="item_name">单价</div>
                                <div class="item_price">
                                    <span>${record.unitPrice?round?string[",000"]}</span>元/m²
                                </div>
                            </div>
                        <#else>
                            <div class="item">
                                <div class="item_name">总价</div>
                                <div class="item_price">
                                    <span>${record.totalPrice?round?string["0"]}</span>元/月
                                </div>
                            </div>
                            <div class="item">
                                <div class="item_name">单价</div>
                                <div class="item_price">
                                    <span>${record.unitPrice?round?string["0"]}</span>元/月/m²
                                </div>
                            </div>
                        </#if>
                        <div class="item">
                            <div class="item_name">价值时点：${report.evalDate?string("yyyy-MM-dd")}</div>
                            <div class="item_price">价值类型：
                                <font>市场价值</font>
                            </div>
                        </div>
                    </div>
                    <div class="base_info">
                        <div class="div_title">房地产基础信息</div>
                        <div class="base_body">
                            <div class="table_box">
                                <table>
                                    <tr>
                                        <td colspan="2">
                                            <span>委托人：</span>
                                            <b><#if record.principal?? && record.principal?length gt 0>${record.principal}<#else>--</#if></b>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td colspan="2">
                                            <span>所有权人：</span>
                                            <b><#if record.owner?? && record.owner?length gt 0>${record.owner}<#else>--</#if></b>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td colspan="2">
                                            <span>证载地址：</span>
                                            <b><#if record.cardAddress?? && record.cardAddress?length gt 0>${record.cardAddress}<#else>--</#if></b>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td colspan="2">
                                            <span>用途类型：</span>
                                            <b>${record.propTypeName!"--"}</b>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>面积：</span>
                                            <b>${record.bldgArea!"--"}㎡</b>
                                        </td>
                                        <td>
                                            <span>户型：</span>
                                            <#assign huxin><#if record.br?has_content>${record.br}室</#if><#if record.lr?has_content>${record.lr}厅</#if><#if record.cr?has_content>${record.cr}卫</#if></#assign>
                                            <b><#if huxin?has_content>${huxin}<#else>--</#if></b>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>房屋朝向：</span>
                                            <b>${record.faceName!"--"}</b>
                                        </td>
                                        <td>
                                            <span>所在楼层/总层数：</span>
                                            <b>${record.floor!"--"}/${record.bheight!"--"}</b>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>建成年代：</span>
                                            <b><#if record.buildYear?? && record.buildYear?length gt 0>${record.buildYear}<#else>--</#if></b>
                                        </td>
                                        <td>
                                            <span>建筑结构：</span>
                                            <b><#if haDetail.bldgstru?? && haDetail.bldgstru?length gt 0>${haDetail.bldgstru}<#else>--</#if></b>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>建筑类型：</span>
                                            <b><#if record.bldgTypeName?? && record.bldgTypeName?length gt 0>${record.bldgTypeName}<#else>--</#if></b>
                                        </td>
                                        <td>
                                            <span>室内结构：</span>
                                            <b><#if record.indoStruName?? && record.indoStruName?length gt 0>${record.indoStruName}<#else>--</#if></b>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>装修程度：</span>
                                            <b><#if record.intdecoName?? && record.intdecoName?length gt 0>${record.intdecoName}<#else>--</#if></b>
                                        </td>
                                        <td>
                                            <span>权属情况：</span>
                                            <b><#if record.proprtName?? && record.proprtName?length gt 0>${record.proprtName}<#else>--</#if></b>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="valuation">
                                <img style="height:154px;width:310px;" src="${haDetail.imageUrl}">
                                <span>根据《中华人民共和国国家标准房地产估价规范》GB/T50291-2015、国家房地产评估术语《GB/T50899-2013》采用：</span>
                                <div class="method">
                                    <font>评估方法</font>
                                    <div class="button">
                                        <div class="btn">比较法</div>
                                        <div class="btn">收益法</div>
                                        <div class="btn">多元回归分析法</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="trend">
                        <div class="div_title">房价行情走势</div>
                        <div id="market-trend"></div>
                    </div>
                    <div class="cert">
                        <div class="div_title">房地产估价资质及其他</div>
                        <div class="img">
                            <div class="rcLF" >
                                <#if reportConfig.businessLicense??>
                                    <img src="${basePath + reportConfig.businessLicense}">
                                </#if>
                            </div>
                            <div class="rcZZ">
                                <div class="rcTop" >
                                    <#if reportConfig.certificate??>
                                        <img src="${basePath + reportConfig.certificate}">
                                    </#if>
                                </div>
                                <div class="rcBot">
                                    <div>
                                        <#if reportConfig.appraiserSign1??>
                                            <img src="${basePath + reportConfig.appraiserSign1}">
                                        </#if>
                                    </div>
                                    <div style="margin-left:26px;">
                                        <#if reportConfig.appraiserSign2??>
                                            <img src="${basePath + reportConfig.appraiserSign2}">
                                        </#if>
                                    </div>
                                </div>
                            </div>
                            <div class="rcYZ">
                                <#if reportConfig.reportSignImg??>
                                    <img src="${basePath + reportConfig.reportSignImg}">
                                    <div class="reportSign">${reportConfig.reportSign}<br>${report.evalDate?string("yyyy-MM-dd")}</div>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${resourcePath}template/lib/echarts.min.js"></script>
<script>
    var option = {
        animation:false,
        title: {
            text: "行情走势",
            textStyle: {
                fontSize: "14"
            }
        },
        tooltip: {
            trigger: "axis",
            formatter: "单价：{c}元/㎡"
        },
        legend: {
            data: ["同小区行情走势"],
            right: "10%",
            top: "10%",
            itemWidth: 20,
            itemHeight: 6
        },
        calculable: true,
        xAxis: [
            {
                axisLabel: {
                    rotate: -45,
                    interval: 0,
                    color: "black",
                    fontSize: 12
                },
                axisLine: {
                    lineStyle: {
                        color: "#CECECE"
                    }
                },
                name: "年-月",
                nameLocation: "center",
                nameGap: 45,
                nameTextStyle: {
                    color: "black"
                },
                type: "category",
                boundaryGap: false,
                data: []
            }
        ],
        yAxis: [
            {
                type: "value",
                axisLine: {
                    lineStyle: {
                        color: "#CECECE"
                    }
                },
                axisLabel: {
                    color: "black",
                    fontSize: 11
                },
                splitNumber: 10,
                name: "",
                nameGap: 10,
                nameTextStyle: {
                    color: "black"
                }
            }
        ],
        series: [
            {
                name: "同小区行情走势",
                type: "line",
                symbol: "rect",
                symbolSize: 5,
                color: ["#FFA808"],
                data: []
            }
        ]
    };

    function dateFormat(date) {
        date = date.split("-");
        if (date[1] < 10) {
            date[1] = "0" + date[1];
        }
        return date[0] + "-" + date[1];
    }

    var analysis = {};
    <#if analysis??>
    analysis = ${analysis}.rows;
    var date = [];
    var data = [];
    analysis.forEach(function (v, i) {
        if (analysis[i].data) {
            data.push(analysis[i].data);
            date.push(dateFormat(analysis[i].month));
        }
    });
    if (date.length > 40) {
        var d = new Date();
        var month = d.getMonth() + 1;
        var even = !!(month % 2);
        date = date.map(function (v, k) {
            if (even) {
                return k % 2 ? "" : v;
            } else {
                return k % 2 ? v : "";
            }
        });
    }
    if (data.length) {
        option.series[0].data = data;
        option.xAxis[0].data = date;
        <#if record.evalType == 1>
        option.yAxis[0].name = "元/㎡";
        option.tooltip.formatter = "单价：{c}元/㎡";
        <#else>
        Tunit = "元/月";
        option.yAxis[0].name = "元/月/㎡";
        option.tooltip.formatter = "单价：{c}元/月/㎡";
        </#if>
        var myChart = echarts.init(
            document.getElementById("market-trend")
        );
        myChart.setOption(option);
    }
    </#if>
</script>
</body>
</html>
