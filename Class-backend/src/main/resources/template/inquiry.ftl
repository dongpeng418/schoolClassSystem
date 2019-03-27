<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>询价报告</title>
  <link rel="stylesheet" href="${resourcePath}template/css/inquiry.css">
  <link rel="stylesheet" href="${resourcePath}template/fonts/iconfont.css">
  <style type="text/css">
    .BMap_cpyCtrl {
      display: none;
    }

    .anchorBL {
      display: none;
    }

    .reportContainer .rBody .bColumn .cMain .mTable .tList .tItem .iCol:nth-child(2){
      border-left: 1px solid #dadee3;
    }

    .reportContainer .rBody .bColumn #radaWrap .chartBox .raTable ul li div {
      width: 50%;
      text-align: center;
      line-height: 36px;
      font-size: 12px;
    }

    .reportContainer .rBody .bColumn .cMain .mTable .tList .tItem .iCol {
      width: 50%;
      padding-left: 26px;
    }
  </style>
</head>
<body>
<div>
  <div id="print_area">
    <div class="reportContainer">
      <div class="rHeader">
        <div class="rTitle">
                <span>
                  <#if reportConfig.reportName?? && reportConfig.reportName?length gt 0>
                    ${reportConfig.reportName}
                  <#else>
                    ${record.haName}询价报告
                  </#if>
                </span>
          <span class="address">
                  <div class="nearColor iconfont icon-dingwei"></div>
                  ${record.cityName}市
                </span>
        </div>
        <div class="rTime">评估时间：&nbsp;${report.evalDate?string("yyyy/MM/dd HH:mm:ss")}</div>
      </div>
      <div class="rBody">
        <div class="bColumn">
          <div class="cTitle subTitle">房地产基础信息</div>
          <div class="cMain">
            <div class="mTable">
              <ul class="tList">
                <li class="tItem">
                  <div class="iCol">
                    <span class="iName">用途类型</span>
                    <span class="iValue">${record.propTypeName!"--"}</span>
                  </div>
                  <div class="iCol">
                    <span class="iName">面积</span>
                    <span class="iValue">${record.bldgArea!"--"}㎡</span>
                  </div>
                </li>
                <li class="tItem">
                  <div class="iCol">
                    <span class="iName">户型</span>
                    <#assign huxin><#if record.br?has_content>${record.br}室</#if><#if record.lr?has_content>${record.lr}厅</#if><#if record.cr?has_content>${record.cr}卫</#if></#assign>
                    <span class="iValue"><#if huxin?has_content>${huxin}<#else>--</#if></span>
                  </div>
                  <div class="iCol">
                    <span class="iName">房屋朝向</span>
                    <span class="iValue">${record.faceName!"--"}</span>
                  </div>
                </li>
                <li class="tItem">
                  <div class="iCol">
                    <span class="iName">所在楼层</span>
                    <span class="iValue">${record.floor!"--"}层（共${record.bheight!"--"}层）</span>
                  </div>
                  <div class="iCol">
                    <span class="iName">建筑年代</span>
                    <span class="iValue"><#if record.buildYear?? && record.buildYear?length gt 0>${record.buildYear}年<#else>--</#if></span>
                  </div>
                </li>
                <li class="tItem">
                  <div class="iCol">
                    <span class="iName">建筑类型</span>
                    <span class="iValue"><#if record.bldgTypeName?? && record.bldgTypeName?length gt 0>${record.bldgTypeName}<#else>--</#if></span>
                  </div>
                  <div class="iCol">
                    <span class="iName">室内结构</span>
                    <span class="iValue"><#if record.indoStruName?? && record.indoStruName?length gt 0>${record.indoStruName}<#else>--</#if></span>
                  </div>
                </li>
                <li class="tItem">
                  <div class="iCol">
                    <span class="iName">装修程度</span>
                    <span class="iValue"><#if record.intdecoName?? && record.intdecoName?length gt 0>${record.intdecoName}<#else>--</#if></span>
                  </div>
                  <div class="iCol">
                    <span class="iName">详细地址</span>
                    <#assign detailAddr><#if record.buildNo?has_content>${record.buildNo}号楼</#if><#if record.unit?has_content>${record.unit}单元</#if><#if record.roomNo?has_content>${record.roomNo}室</#if></#assign>
                    <span class="iValue"><#if detailAddr?has_content>${detailAddr}<#else>--</#if></span>
                  </div>
                </li>
              </ul>
            </div>
            <div class="mMap" id="map"></div>
          </div>
        </div>
        <div class="bColumn">
          <div class="cTitle subTitle">估价结果</div>
          <ul class="resultContainer">
            <#if record.evalType == 1>
              <li class="rItem">
                <p class="iTit">总价</p>
                <div class="iResult">
                  <span>${record.totalPrice?round?string["0"]}</span>万元
                </div>
              </li>
              <li class="rItem">
                <p class="iTit">单价</p>
                <div class="iResult">
                  <span>${record.unitPrice?round?string["0"]}</span>元/㎡
                </div>
              </li>
              <li class="rItem">
                <p class="iTit">出售均价</p>
                <div class="iResult">
                  <span>
                    <#if haDetail.price??>${haDetail.price?round?string["0"]}<#else>--</#if>
                    <#if haDetail.priceRise gt 0>
                      <i class="up">
                        <img alt="" src="${resourcePath}template/image/up.png" />
                        环比上月升${haDetail.priceRise}%
                      </i>
                    <#else>
                      <i class="down">
                        <img alt="" src="${resourcePath}template/image/down.png" />
                        环比上月降<#if haDetail.priceRise??>${haDetail.priceRise?replace("-","")}%<#else>--</#if>
                      </i>
                    </#if>
                  </span>元/㎡
                </div>
              </li>
            <#else>
              <li class="rItem">
                <p class="iTit">总价</p>
                <div class="iResult">
                  <span>${record.totalPrice?round?string["0"]}</span>元/月
                </div>
              </li>
              <li class="rItem">
                <p class="iTit">单价</p>
                <div class="iResult">
                  <span>${record.unitPrice?round?string["0"]}</span>元/月/㎡
                </div>
              </li>
              <li class="rItem">
                <p class="iTit">出租均价</p>
                <div class="iResult">
                  <span>
                  <#if haDetail.leasePrice??>${haDetail.leasePrice?string["0.##"]}<#else>--</#if>
                  <#if haDetail.leasePriceRise gt 0>
                    <i class="up">
                      <img alt="" src="${resourcePath}template/image/up.png" />
                      环比上月升${haDetail.leasePriceRise}%
                    </i>
                  <#else>
                    <i class="down">
                      <img alt="" src="${resourcePath}template/image/down.png" />
                      环比上月降<#if haDetail.leasePriceRise??>${haDetail.leasePriceRise?replace("-","")}%<#else>--</#if>
                    </i>
                  </#if>
                  </span>元/月/㎡
                </div>
              </li>
            </#if>
          </ul>
        </div>
        <div class="bColumn">
          <div class="cTitle subTitle">估价案例分析</div>
          <div class="cSubTitle">本案例采用了${record.sampleCount!"--"}个最新案例进行筛选评估，情况如下：</div>
          <div class="caseData">
            <#if record.evalType == 1>
              <div class="dItem">
                <span class="name">最高单价</span>
                <span class="value"><#if record.samplePriceMax??>${record.samplePriceMax?round?string["0"]}<#else>--</#if>元/㎡</span>
              </div>
              <div class="dItem">
                <span class="name">最低单价</span>
                <span class="value"><#if record.samplePriceMin??>${record.samplePriceMin?round?string["0"]}<#else>--</#if>元/㎡</span>
              </div>
              <div class="dItem">
                <span class="name">平均单价</span>
                <span class="value"><#if record.samplePriceAvg??>${record.samplePriceAvg?round?string["0"]}<#else>--</#if>元/㎡</span>
              </div>
            <#else>
              <div class="dItem">
                <span class="name">最高单价</span>
                <span class="value"><#if record.samplePriceMax??>${record.samplePriceMax?round?string["0"]}<#else>--</#if>元/月/㎡</span>
              </div>
              <div class="dItem">
                <span class="name">最低单价</span>
                <span class="value"><#if record.samplePriceMin??>${record.samplePriceMin?round?string["0"]}<#else>--</#if>元/月/㎡</span>
              </div>
              <div class="dItem">
                <span class="name">平均单价</span>
                <span class="value"><#if record.samplePriceAvg??>${record.samplePriceAvg?round?string["0"]}<#else>--</#if>元/月/㎡</span>
              </div>
            </#if>
          </div>
          <div class="cSubTitle">评估案例抽样：</div>
          <div class="rTable">
            <div class="tableBox">
              <table class="sampleTable" width="100%" style="table-layout:fixed;">
                <thead>
                <tr>
                  <th class="xiang" width="80">案例</th>
                  <th>案例小区</th>
                  <th class="xiang">面积</th>
                  <th class="xiang">楼层/楼高</th>
                  <th class="xiang">户型</th>
                  <th class="xiang">价格更新日期</th>
                  <#if record.evalType == 1>
                    <th class="xiang">交易总价(万元)</th>
                    <th class="xiang">交易单价(元/㎡)</th>
                  <#else>
                    <th class="xiang">交易总价(元/月)</th>
                    <th class="xiang">交易单价(元/月/㎡)</th>
                  </#if>
                </tr>
                </thead>
                <tbody>
                <#list samples as sample>
                  <tr>
                    <td class="xiang">案例${sample_index + 1}</td>
                    <td>${sample.haName!"--"}</td>
                    <td class="xiang">${sample.areaSize!"--"}㎡</td>
                    <td class="xiang">${sample.floor!"--"}/${sample.bheight!"--"}</td>
                    <td class="xiang"><#if sample.br??>${sample.br}室</#if><#if sample.lr??>${sample.lr}厅</#if></td>
                    <td class="xiang">${sample.offerTime?string("yyyy-MM-dd")}</td>
                    <td class="xiang">${sample.totalPrice?round?string["0"]}</td>
                    <td class="xiang">${sample.unitPrice?round?string["0"]}</td>
                  </tr>
                </#list>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <div class="bColumn">
          <div class="cTitle subTitle">楼盘小区信息调查</div>
          <dl class="plotInfo">
            <dt class="pImg">
              <img alt="" src="${haDetail.imageUrl}" />
            </dt>
            <dd class="pContent">
              <h4 class="pTitle">${record.haName}</h4>
              <#if record.evalType == 1>
                <p class="pDesc">
                  ${haDetail.price?round?string["0"]}元/㎡<span style="color:#999;padding-right: 10px;">[${currentDate?string("yyyy年MM月")}]</span>
                  <#if haDetail.priceRise gt 0>
                    <i class="up">
                      <img alt="" src="${resourcePath}template/image/up.png" />
                      环比上月升${haDetail.priceRise}%
                    </i>
                  <#else>
                    <i class="down">
                      <img alt="" src="${resourcePath}template/image/down.png" />
                      环比上月降<#if haDetail.priceRise??>${haDetail.priceRise?replace("-","")}%<#else>--</#if>
                    </i>
                  </#if>
                </p>
              <#else>
                <p class="pDesc">
                  ${haDetail.leasePrice?string["0.##"]}元/月/㎡<span style="color:#999;padding-right: 10px;">[${currentDate?string("yyyy年MM月")}]</span>
                  <#if haDetail.leasePriceRise gt 0>
                    <i class="up">
                      <img alt="" src="${resourcePath}template/image/up.png" />
                      环比上月升${haDetail.leasePriceRise}%
                    </i>
                  <#else>
                    <i class="down">
                      <img alt="" src="${resourcePath}template/image/down.png" />
                      环比上月降<#if haDetail.leasePriceRise??>${haDetail.leasePriceRise?replace("-","")}%<#else>--</#if>
                    </i>
                  </#if>
                </p>
              </#if>
              <ul class="pList">
                <li class="item">
                  <span class="name">建成年代</span>
                  <span class="value">
                          <#if haDetail.buildyear??>
                            ${haDetail.buildyear?replace("|","、")}
                          <#else>
                            --
                          </#if>
                        </span>
                </li>
                <li class="item">
                  <span class="name">分类</span>
                  <span class="value">${haDetail.haClName!"--"}</span>
                </li>
                <li class="item">
                  <span class="name">包含用途</span>
                  <span class="value">${haDetail.protoType!"--"}</span>
                </li>
                <li class="item">
                  <span class="name">建筑类型</span>
                  <span class="value"><#if haDetail.bldgType?? && haDetail.bldgType?length gt 0>${haDetail.bldgType}<#else>--</#if></span>
                </li>
                <li class="item">
                  <span class="name">车位数</span>
                  <span class="value">
                          <#assign carCount = "0" />
                    <#if (haDetail.carItem)??>
                      <#list haDetail.carItem as item>
                        <#if item.type == "总车位">
                          <#assign carCount = "${item.qnt}" />
                          <#break>
                        <#else>
                          <#assign carCount = "${carCount?number + item.qnt?number}" />
                        </#if>
                      </#list>
                    </#if>
                    <#if carCount == "0">--<#else>${carCount}个</#if>
                        </span>
                </li>
                <li class="item">
                  <span class="name">建筑面积</span>
                  <span class="value"><#if haDetail.constArea?? && haDetail.constArea?length gt 0>${haDetail.constArea}<#else>--</#if></span>
                </li>
                <li class="item">
                  <span class="name">占地面积</span>
                  <span class="value"><#if haDetail.groundAreaNum??>${haDetail.groundAreaNum}㎡<#else>--</#if></span>
                </li>
                <li class="item">
                  <span class="name">绿化率</span>
                  <span class="value"><#if haDetail.greeningRate?? && haDetail.greeningRate?length gt 0>${haDetail.greeningRate}%<#else>--</#if></span>
                </li>
                <li class="item">
                  <span class="name">容积率</span>
                  <span class="value"><#if haDetail.volumeRate?? && haDetail.volumeRate?length gt 0>${haDetail.volumeRate}%<#else>--</#if></span>
                </li>
                <li class="item full">
                  <span class="name">开发商</span>
                  <span class="value"><#if haDetail.devCoName?? && haDetail.devCoName?length gt 0>${haDetail.devCoName}<#else>--</#if></span>
                </li>
                <li class="item full">
                  <span class="name">物业地址</span>
                  <span class="value">
                          <#if (haDetail.cos)??>
                            <#list haDetail.cos as item>
                              <#if item.coName == (haDetail.wyCoName)!"--">
                                <#if item.coAddr?? && item.coAddr?length gt 0>${item.coAddr}<#else>--</#if>
                              </#if>
                            </#list>
                          </#if>
                        </span>
                </li>
                <li class="item full">
                  <span class="name">物业公司</span>
                  <span class="value"><#if haDetail.wyCoName?? && haDetail.wyCoName?length gt 0>${haDetail.wyCoName}<#else>--</#if></span>
                </li>
              </ul>
            </dd>
          </dl>
        </div>
        <div class="bColumn">
          <div class="cTitle subTitle">房产价值调查</div>
          <div class="cWrap">
            <div class="wTitle">价格分布比较</div>
            <!-- echarts -->
            <div id="line1"></div>
          </div>
          <div id="radaWrap">
            <div class="wTitle">房产特征对价格影响分析</div>
            <div class="chartBox">
              <div class="radia">
                <div class="radiaBox">
                  <!-- echarts -->
                  <div id="canvas4"></div>
                </div>
              </div>
              <div class="raTable">
                <ul>
                  <li class="title">
                    <div>特征</div>
                    <div>权值</div>
                  </li>
                  <#if betas??>
                    <#list betas as beta>
                      <li>
                        <div>${beta.title!"--"}</div>
                        <div>${beta.value}</div>
                      </li>
                    </#list>
                  </#if>
                </ul>
              </div>
            </div>
          </div>
          <div class="cWrap">
            <div class="wTitle">房价行情走势</div>
            <!-- echarts -->
            <div id="canvas1"></div>
          </div>
          <div class="cWrap">
            <div class="wTitle">房源供给量变化</div>
            <!-- echarts -->
            <div id="canvas3"></div>
          </div>
        </div>
        <#function parseJSON json>
          <#local null = ''> <#-- null is not a keyword in FTL -->
          <#return json?eval>
        </#function>
        <#assign arounds=parseJSON(aroundList!'{}') />
        <div class="bColumn">
          <div class="cTitle subTitle">周边环境调查</div>
          <div class="cSubTitle">交通：</div>
          <div class="rTable">
            <table>
              <thead>
              <tr>
                <th class="thin">公交站点</th>
                <th class="thin">方位距离</th>
                <th>公交路线</th>
              </tr>
              </thead>
              <tbody>
              <#if arounds??>
                <#list arounds as around>
                  <#if around.traffic?? && around.traffic.items??>
                    <#list around.traffic.items as traffic>
                      <tr>
                        <td>${traffic.name}</td>
                        <td>${traffic.direction}${traffic.distance}</td>
                        <td class="bus_num">
                          <#if traffic.routes??>
                            <#list traffic.routes as route>
                              ${route.routeName}<#sep>、</#sep>
                            </#list>
                          </#if>
                        </td>
                      </tr>
                    </#list>
                  </#if>
                </#list>
              </#if>
              </tbody>
            </table>
          </div>
          <div class="cSubTitle">教育：</div>
          <div class="rTable">
            <table>
              <thead>
              <tr>
                <th>学校名称</th>
                <th>方位距离</th>
              </tr>
              </thead>
              <tbody>
              <#if arounds??>
                <#list arounds as around>
                  <#if around.education?? && around.education.items??>
                    <#list around.education.items as education>
                      <tr>
                        <td>${education.name}</td>
                        <td>${education.direction}${education.distance}</td>
                      </tr>
                    </#list>
                  </#if>
                </#list>
              </#if>
              </tbody>
            </table>
          </div>
          <div class="cSubTitle">医疗：</div>
          <div class="rTable">
            <table>
              <thead>
              <tr>
                <th>医院名称</th>
                <th>方位距离</th>
              </tr>
              </thead>
              <tbody>
              <#if arounds??>
                <#list arounds as around>
                  <#if around.medical?? && around.medical.items??>
                    <#list around.medical.items as medical>
                      <tr>
                        <td>${medical.name}</td>
                        <td>${medical.direction}${medical.distance}</td>
                      </tr>
                    </#list>
                  </#if>
                </#list>
              </#if>
              </tbody>
            </table>
          </div>
        </div>
        <div class="bColumn">
          <div class="cTitle subTitle">评估声明</div>
          <#if reportConfig.reportStatement?? && reportConfig.reportStatement?length gt 0>
            <#list reportConfig.reportStatement?split("\n") as x>
              <p class="cp">${x}</p>
            </#list>
          <#else>
            <p class="cp">1.前提条件是估价对象权属明确、无法律纠纷、无意外事件的，所有运作方式、程序均符合国家、地方的有关法律、规定。</p>
            <p class="cp">2.本报告结果完全依据市场公开数据，所得分析、意见和结论仅代表我方的专业分析、意见和结论，但受到未知的不确定因素的限制，并不对因数据本身可能存在的错误、缺失和偏差所导致的结果负责。</p>
            <p class="cp">3.任何投资均有风险，房地产市场亦然。本报告资料仅供参考，任何因使用本报告所做出的投资、交易等决定本网站概不承担责任。</p>
            <p class="cp">4.本报告最终解释权归全国房地产自动估价平台。</p>
          </#if>
        </div>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript" src="${resourcePath}template/lib/echarts.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=oprtGDXoTujaw5iQ1DMOfG5WxcAFwE8s"></script>
<script type="text/javascript">
  //百度地图
  var gps = "<#if haDetail.bdlocation??>${haDetail.bdlocation}<#elseif haDetail.location??>${haDetail.location}</#if>";
  if (gps) {
    var gpsArray = gps.split(",");
    var map = new BMap.Map("map");
    var point = new BMap.Point(gpsArray[0], gpsArray[1]);
    map.centerAndZoom(point, 16);
    var deviceSize = new BMap.Size(38, 38);
    var myIcon = new BMap.Icon("${resourcePath}/template/image/house.png", deviceSize, {
      imageSize: deviceSize
    });
    // 创建标注对象并添加到地图
    var marker = new BMap.Marker(point, {icon: myIcon});
    map.addOverlay(marker);
  }

  //*********************************************
  //*********************************************价格分布
  //*********************************************
  var distribution = ${distribution!""};
  if (distribution && distribution.rows.length>0) {
    var current = "${record.unitPrice?round?string["0"]}";
    var haPrice = ${record.unitPrice?round?string["0"]};
    var total = "${record.totalPrice?round?string["0"]}";
    var _x = [];
    distribution.rows.forEach(function(v, i) {
      if (v.data) {
        _x.push([v.step, v.data]);
      }
    });
    var that = {
      <#if record.evalType == 1>
      Uunit : "元/m²",
      Tunit : "万元",
      z : 1000
      <#else>
      Uunit : "元/月/m²",
      Tunit : "元/月",
      z : 10
      </#if>
    };
    var myLine1 = echarts.init(document.getElementById("line1"));
    myLine1.setOption({
      animation:false,
      tooltip: {
        trigger: "axis",
        formatter: function(a){
          return "单价：" + a[0].data[0] + that.Uunit + "<br>占比："+a[0].data[1] +"%"
        }
      },
      grid: {
        left: "1%",
        right: "8%",
        containLabel: true
      },
      xAxis: {
        type: "value",
        name: that.Uunit,
        axisTick: {
          show: false
        },
        data: _x,
        min: function(value) {
          return value.min - that.z;
        },

        max: function(value) {
          return value.max +  that.z;
        },
        nameRotate: -30,
        nameTextStyle: {
          color: "#999",
          fontSize: 10,
          align: "center"
        },
        axisLine: {
          lineStyle: {
            color: "#DCE0E5"
          }
        },
        splitLine: {
          show: false
        },
        axisLabel: {
          color: "#666",
          rotate: "-30",
          fontSize: 10
        }
      },
      yAxis: {
        type: "value",
        axisTick: {
          show: false
        },
        axisLine: {
          show: false
        },
        axisLabel: {
          show: true,
          align:'left',
          inside: false,
          fontSize: 10,
          color: "#666",
          width: 30,
          margin: 20,
          padding:[0,0,0,-12],
          formatter: function(value, index) {
            return value + "%";
          },
          rich: {
          }
        }
      },
      series: [
        {
          data: _x,
          barGap: "1%",
          barWidth: "16",
          type: "bar",
          smooth: true,
          itemStyle: {
            normal: {
              color: "#206EF5"
            }
          },
          areaStyle: {
            normal: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: "#2cc795"
                },
                {
                  offset: 1,
                  color: "#5adbd8"
                }
              ])
            }
          },
          symbolSize: 0
        },
        {
          type: "line",
          markLine: {
            data: [
              [
                { coord: [haPrice, 0] },
                { coord: [haPrice, 12] } //如何获取grid上侧最大值，目前是写死的
              ]
            ],
            lineStyle: {
              type: "solid",
              color: "#F4B469 ",
              height:100
            },
            symbolSize: 1,
            label: {
              show: true,
              formatter: [
                "{t|您的房产}" +
                "\n{a|总价: " +total +"}{b|" + that.Tunit+"}" +
                "\n{a|单价: " +current +"}{b|" + that.Uunit+"}"
              ].join("\n"),
              padding: 12,
              backgroundColor: "rgba(255,255,255, 0.7)",
              borderColor: "#F4B469",
              borderWidth: 1,
              borderRadius: 5,
              rich: {
                a: {
                  color: "#999 ",
                  align: "left",
                  fontSize: 12,
                  lineHeight: 15
                },
                b: {
                  color: "#999 ",
                  fontSize: 12,
                  lineHeight: 15
                },
                t: {
                  fontSize: 14,
                  color: "#666666 ",
                  lineHeight: 28,
                  fontWeight:600
                }
              }
            }
          }
        }
      ]
    });
  }else{
    var box = document.getElementById("line1");
    box.innerHTML = "<div class='noValshow'>暂无数据</div>";
  }

  //雷达图
  var self = {
    indicator:[],
    value:[]
  };
  var betas = <#if betasJson??>${betasJson}<#else>[]</#if>;
  if (betas && betas.length > 0) {
    betas.forEach(function(v) {
      self.indicator.push({ name: v.title });
      self.value.push(v.value);
    });
    self.indicator = self.indicator.reverse();
    self.value = self.value.reverse();
    var option = {
      animation:false,
      title: {
        text: ""
      },
      tooltip: {},
      legend: {
        data: []
      },
      radar: {
        // shape: 'circle',
        name: {
          textStyle: {
            color: "#fff",
            backgroundColor: "#999",
            borderRadius: 3,
            padding: [2, 4]
          }
        },
        indicator: self.indicator,
        splitNumber: 4
      },
      grid: {
        left: 80,
        right: 10,
        bottom: 40,
        top: 40,
        containLabel: true
      },
      series: [
        {
          name: "房产特征对价格影响关系",
          type: "radar",
          data: [
            {
              value: self.value,
              name: "房产特征对价格影响关系"
            }
          ],
          itemStyle: {
            color: "#206EF5"
          }
        }
      ]
    };
    var radar = echarts.init(document.getElementById("canvas4"));
    radar.setOption(option);
  } else {
    var box = document.getElementById("canvas4");
    box.innerHTML = "<div class='noValshow'>暂无数据</div>";
  }

  //*********************************************
  //*********************************************价格比较
  //*********************************************
  var that1 = {};
  <#if record.evalType == 1>
  that1.unit = "元/m²";
  <#else>
  that1.unit = "元/月/m²";
  </#if>
  var analysis = ${analysis!""};
  if (analysis && analysis.rows.length >0) {
    var _x1 = [],
            _data1 = [];
    var arr1 = analysis.rows;
    arr1.forEach(function(v, i) {
      if (v.data) {
        var n = v.month.split("-")[0];
        var m = v.month.split("-")[1];
        if (m < 10) {
          m = "0" + m;
        }
        _x1.push(n + "/" + m);
        _data1.push(v.data || "");
      }
    });
    var canvas1 = echarts.init(document.getElementById("canvas1"));
    var name = "单价（"+that1.unit+"）";
    var final = [];
    final.push({
      data: _data1,
      type: "line",
      smooth: false,
      symbol: "circle",
      symbolSize: 4,
      itemStyle: {
        color: "#206ef5"
      }
    });

    canvas1.setOption({
      animation:false,
      textStyle: {
        fontSize: 10
      },
      tooltip: {
        trigger: "axis",
        formatter: "日期：{b}<br>单价：{c}"+that1.unit
      },
      grid: {
        left: "20",
        right: "8%",
        containLabel: true
      },
      xAxis: {
        type: "category",
        name: "年月",
        nameRotate: -30,
        nameTextStyle: {
          color: "#999",
          fontSize: 10,
          align: "left"
        },
        axisTick: {
          show: false
        },
        splitLine: {
          show: false,
          lineStyle: {
            color: "#dbdbdb"
          }
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: "#dbdbdb"
          }
        },
        axisLabel: {
          rotate: -30,
          margin: 12,
          fontSize: 10,
          color: "#666"
        },
        data: _x1
      },
      yAxis: {
        type: "value",
        name: name,
        nameTextStyle: {
          color: "#999",
          fontSize: 10,
          align: "left",
          padding:[0,0,20,-8]
        },
        splitNumber: 6,
        axisLine: {
          show: false,
          onZero: false,
          lineStyle: {
            color: "#dbdbdb"
          }
        },

        splitLine: {
          show: true,
          lineStyle: {
            color: "#dbdbdb"
          }
        },
        axisTick: {
          show: false
        },
        axisLabel: {
          align:'left',
          inside: false,
          fontSize: 10,
          color: "#666",
          margin: 10,
          padding:[0,0,0,-28],
          formatter: function(value, index) {
            return value;
          },
          rich: {}
        }
      },
      series: final
    });
  }else{
    var box = document.getElementById("canvas1");
    box.innerHTML = "<div class='noValshow'>暂无数据</div>";
  }

  //*********************************************
  //*********************************************供给量变化
  //*********************************************
  var countList = ${countList!""};
  if (countList && countList.rows.length > 0) {
    var arr1 = countList.rows;
    var _x1 = [], _data1 = [];
    arr1.forEach(function(v, i) {
      if (v.data) {
        var n = v.month.split("-")[0];
        var m = v.month.split("-")[1];
        if (m < 10) {
          m = "0" + m;
        }
        _x1.push(n + "/" + m);
        _data1.push(v.data || "");
      }
    });
    var myLine5 = echarts.init(document.getElementById("canvas3"));
    var color = "#36A853";
    var name = "套";
    var final = [];

    final.push({
      data: _data1,
      type: "line",
      smooth: false,
      symbol: "circle",
      symbolSize: 4,
      itemStyle: {
        color: color
      }
    });

    myLine5.setOption({
      animation:false,
      textStyle: {
        fontSize: 10
      },
      tooltip: {
        trigger: "axis",
        formatter: "日期：{b}<br>供给量：{c}套"
      },
      xAxis: {
        type: "category",
        name: "年月",
        nameRotate: -30,
        nameTextStyle: {
          color: "#333",
          fontSize: 10,
          align: "center"
        },
        axisTick: {
          show: false
        },
        splitLine: {
          show: false,
          lineStyle: {
            color: "#dbdbdb"
          }
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: "#dbdbdb"
          }
        },
        axisLabel: {
          rotate: -30,
          margin: 12,
          fontSize: 10,
          color: "#666"
        },
        data: _x1
      },
      yAxis: {
        type: "value",
        name: name,
        nameTextStyle: {
          color: "#999",
          fontSize: 10,
          align: "left",
          padding: [0, 0, 20, -26]
        },
        axisLine: {
          show: false,
          lineStyle: {
            color: "#dbdbdb"
          }
        },
        splitLine: {
          show: true,
          lineStyle: {
            color: "#dbdbdb"
          }
        },
        // minInterval: 10000,
        axisTick: {
          show: false
        },
        axisLabel: {
          inside: false,
          fontSize: 10,
          verticalAlign: "bottom",
          color: "#666",
          formatter: function(value, index) {
            return value;
          }
        }
      },
      grid: {
        left: "20",
        right: "38",
        bottom: "60",
        top: 60,
        containLabel: true
      },
      series: final
    });
  } else {
    var box = document.getElementById("canvas3");
    box.innerHTML = "<div class='noValshow'>暂无数据</div>";
  }

</script>
</body>
</html>