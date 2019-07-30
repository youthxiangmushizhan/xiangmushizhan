var app = new Vue({
    el:"#app",
    data:{
        chartData:{},
        pickerOptions: {
            shortcuts: [{
                text: '最近一周',
                onClick(picker) {
                    const end = new Date();
                    const start = new Date();
                    start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                    picker.$emit('pick', [start, end]);
                }
            }, {
                text: '最近一个月',
                onClick(picker) {
                    const end = new Date();
                    const start = new Date();
                    start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                    picker.$emit('pick', [start, end]);
                }
            }, {
                text: '最近三个月',
                onClick(picker) {
                    const end = new Date();
                    const start = new Date();
                    start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                    picker.$emit('pick', [start, end]);
                }
            }]
        },
        timeScope:'',
        chartType:"line"
    },
    methods:{
        generateChart() {
            var title
            if (this.chartType == 'line') {
                title = '销售折线图'
            } else if (this.chartType == 'bar') {
                title = '销售柱状图'
            }
            var myChart = echarts.init(document.getElementById('main'));
            myChart.setOption({
                title: {
                    text: title
                },
                tooltip: {},
                legend: {
                    data:['销售额','销量']
                },
                toolbox: {
                    right: '20px',
                    feature: {
                        dataView: {},
                        saveAsImage: {
                            pixelRatio: 2
                        }
                    }
                },
                xAxis:[
                    {
                        data: app.chartData.dates
                    }
                ],
                yAxis: [
                    {
                        name:"销售额",
                        type: 'value'
                    },
                    {
                        name:"销量",
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '销售额',
                        yAxisIndex : 0,
                        data: app.chartData.saleMoney,
                        type: app.chartType,
                        animationDelay: function (idx) {
                            return idx * 1;
                        }
                    },
                    {
                        name: '销量',
                        yAxisIndex : 1,
                        data: app.chartData.saleNum,
                        type: app.chartType,
                        animationDelay: function (idx) {
                            return idx * 1 + 10;
                        }
                    },

                ],
                animationEasing: 'elasticOut',
                animationDelayUpdate: function (idx) {
                    return idx * 0.5;
                }
            })
        },
        searchChart:function () {

            if (this.timeScope == '') {
                this.$message({
                    message: "请选择查询时间",
                    type: 'warning'
                });
            }

            axios.get("/salesChart/getSalesChart.shtml",{params:{
                    startDate:new Date(this.timeScope[0]),
                    endDate:new Date(this.timeScope[1])
            }}).then(function (response) {
                if (response.data.flag) {
                    app.chartData = response.data
                    app.generateChart()
                } else {
                    alert(response.data.message)
                }
            })
        }
    },
    watch:{
        'chartType' : function () {
            app.generateChart()
        }
    }
})