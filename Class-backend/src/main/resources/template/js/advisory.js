var chart = new G2.Chart({
    container: 'main',
    forceFit: true,
    height: window.innerHeight
});
chart.source(data);
chart.line().position('month*data');
chart.point().position('month*data').size(4).shape('circle').style({
    stroke: '#fff',
    lineWidth: 1
});
chart.render();