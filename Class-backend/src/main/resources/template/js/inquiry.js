var distChart = new G2.Chart({
    container: 'bar_chart',
    forceFit: true,
    height : 400,
    animate: false
});
distChart.animate(false);
distChart.source(distribution);
distChart.interval().position('step*data');
var tooltipHtml = "<div class='chart-tooltip'><div class='header'>您的房产</div>" +
    "<div class='content'>总价："+haTotalPriceStr+"万元<br>单价："+ haPriceStr+"元/㎡</div></div>";
distChart.guide().line({
    start: [haPrice, '0'], // 使用数组格式
    end: [haPrice, 'max'],
    lineStyle: {
        stroke: '#e9662f', // 线的颜色
        lineDash: null, // 虚线的设置
        lineWidth: 2 // 线的宽度
    }, // 图形样式配置
});
// // 坐标点
var point = [ haPrice, maxRate ];
distChart.guide().html({
    position: point,
    html: tooltipHtml,
    alignX: 'right',
    alignY: 'bottom',
    offsetX: 86
});
distChart.render();

var radarChat = new G2.Chart({
    container: 'beta_chart',
    width: 380,
    height : 400,
    animate: false
});
radarChat.source(betas, {
    value: {
        min: 0,
        max: maxRadarValue
    }
});
radarChat.coord('polar', {
    radius: 1
});
radarChat.axis('title', {
    line: null,
    tickLine: null,
    grid: {
        lineStyle: {
            lineDash: null
        },
        hideFirstLine: false
    }
});
radarChat.axis('value', {
    line: null,
    tickLine: null,
    label: null,
    grid: {
        type: 'polygon',
        lineStyle: {
            lineDash: null
        }
    },
    alternateColor: 'rgba(0, 0, 0, 0.04)'
});
radarChat.line().position('title*value').color('#5ea3f8').size(2);
radarChat.point().position('title*value').color('#5ea3f8').shape('circle').size(4).style({
    stroke: '#fff',
    lineWidth: 1,
    fillOpacity: 1
});
radarChat.area().position('title*value').color('#5ea3f8');
radarChat.render();

var priceChart = new G2.Chart({
    container: 'price_count',
    forceFit: true,
    height: 400,
    animate: false
});
priceChart.source(analysis);
priceChart.scale('data', {
    min: 0
});
priceChart.scale('month', {
    range: [0, 1],
    tickCount: 30
});
priceChart.tooltip({
    crosshairs: {
        type: 'line'
    }
});
priceChart.line().position('month*data');
priceChart.point().position('month*data').size(4).shape('circle').style({
    stroke: '#fff',
    lineWidth: 1
});
priceChart.render();

var countChart = new G2.Chart({
    container: 'price_trend',
    forceFit: true,
    height: 400,
    animate: false
});
countChart.source(analysis);
countChart.scale('count', {
    min: 0
});
countChart.scale('month', {
    range: [0, 1],
    tickCount: 10
});
countChart.tooltip({
    crosshairs: {
        type: 'line'
    }
});
countChart.areaStack().position('month*count');
countChart.line().position('month*count');
countChart.point().position('month*count').size(4).shape('circle').style({
    stroke: '#fff',
    lineWidth: 1
});
countChart.render();