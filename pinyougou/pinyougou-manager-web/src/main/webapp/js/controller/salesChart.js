var app = new Vue({
    el:"#app",
    data:{
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
        timeScope:''
    },
    methods:{
        searchChart:function () {

            if (this.timeScope == '') {
                this.$message({
                    message: "请选择查询时间",
                    type: 'warning'
                });
            }

            // 基于准备好的dom，初始化echarts实例
            var myChart1 = echarts.init(document.getElementById('main1'));
            var myChart2 = echarts.init(document.getElementById('main2'));

            axios.get("/salesChart/getSalesChart.shtml",{params:{
                startDate:new Date(this.timeScope[0]),
                endDate:new Date(this.timeScope[1])
            }}).then(function (response) {
                if (response.data.flag) {
                    myChart1.setOption({
                        title : {
                            text: '销售额饼状图',
                            subtext: '单位：元',
                            x:'center'
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} 元 ({d}%)"
                        },
                        legend: {
                            type: 'scroll',
                            orient: 'vertical',
                            right: 10,
                            top: 20,
                            bottom: 300,
                            data: response.data.allSeller,

                            selected: response.data.selected
                        },
                        series : [
                            {
                                name: '商家名称',
                                type: 'pie',
                                radius: '56%',
                                center: ['50%', '60%'],
                                data: response.data.saleMoney,
                                itemStyle: {
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    });
                    myChart2.setOption({
                        title : {
                            text: '销量饼状图',
                            subtext: '单位：件',
                            x:'center'
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} 件 ({d}%)"
                        },
                        legend: {
                            type: 'scroll',
                            orient: 'vertical',
                            right: 10,
                            top: 20,
                            bottom: 400,
                            data: response.data.allSeller,

                            selected: response.data.selected
                        },
                        series : [
                            {
                                name: '商家名称',
                                type: 'pie',
                                radius: '56%',
                                center: ['50%', '60%'],
                                data: response.data.saleNum,
                                itemStyle: {
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    });
                } else {
                    console.log(response.data)
                    alert(response.data.message)
                }
            })
        },

    },
    created:function () {

    }
})