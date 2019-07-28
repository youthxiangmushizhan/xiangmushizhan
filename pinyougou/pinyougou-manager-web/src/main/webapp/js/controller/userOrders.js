var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageSize:10,
        pageNo:1,
        total:0,
        list:[],
        entity:{},
        ids:[],
        searchEntity:{

        },
        dialogFormVisible: false,
        formLabelWidth: '75px',
        multipleSelection: [],
        statusList:[
            {label:"普通订单",value:'0'},
            {label:"秒杀订单",value:'1'},
            {label:"所有订单",value:''}
        ],
        timeTypeList:[
            {label:"订单创建时间",value:'0'},
            {label:"订单付款时间",value:'1'},
            {label:"请选择",value:''}
        ],
        payStatusList:[
            {name:"未付款",value:1},
            {name:"已付款",value:2},
            {name:"未发货",value:3},
            {name:"已发货",value:4},
            {name:"交易成功",value:5},
            {name:"交易关闭",value:6},
            {name:"待评价",value:7}
        ],
        payTypeList:[
            {name:"在线支付",value:1},
            {name:"货到付款",value:2}
        ],
        orderType:"",
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
        timeScope:[],
        timeType:"",
        fileName:""
    },
    methods: {
        show:function () {
            console.log(this.timeScope)
        },
        handleSizeChange(val) {
            this.pageSize = val;
            this.searchList(this.pageNo)
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            this.ids = []
            if (this.multipleSelection != null) {
                for (var i = 0;i<this.multipleSelection.length;i++) {
                    this.ids.push(this.multipleSelection[i].id)
                }
            }
            console.log(this.ids)
        },
        searchList:function (curPage) {
            console.log(this.searchEntity)

            let startTime = '',endTime = ''
            if (this.timeScope != '') {
                startTime = new Date(this.timeScope[0]).Format("yyyy-MM-dd hh:mm:ss")
                endTime = new Date(this.timeScope[1]).Format("yyyy-MM-dd hh:mm:ss")
            }

            axios.post('/orderTemplate/search.shtml?pageNo='+curPage+'&pageSize='+this.pageSize+'&type='+this.orderType+'&startTime='+startTime+'&endTime='+endTime+'&timeType='+this.timeType,this.searchEntity).then(function (response) {
                //获取数据
                app.list=response.data.list;

                console.log(app.list)

                //当前页
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
                //总条数
                app.total = response.data.total
            });
        },

        checkParams:function () {
            if (this.timeScope != '' && '' == this.timeType) {
                this.$message({
                    message: "请选择查询时间类型",
                    type: 'warning'
                });

                return;
            } else {
                this.searchList(1)
            }
        },
        exportUserOrder:function (typeId) {

            if (this.fileName == '') {
                this.fileName = new Date().getTime()
            }

            if (typeId in [0,1,2]) {
                const form = {} // 要发送到后台的数据
                axios({ // 用axios发送post请求
                    method: 'post',
                    url: "/file/exportOrder/"+typeId+".shtml", // 请求地址
                    data: form, // 参数
                    responseType: 'blob' // 表明返回服务器返回的数据类型
                }).then(res => { // 处理返回的文件流
                    const url = window.URL.createObjectURL(new Blob([res.data]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', app.fileName + ".xlsx");
                    document.body.appendChild(link);
                    link.click();
                    /*let blob = new Blob([res.data], {type: res.type})
                    let downloadElement = document.createElement('a')
                    let href = window.URL.createObjectURL(blob); //创建下载的链接
                    downloadElement.href = href;
                    downloadElement.download = new Date().getTime() + ".xlsx"; //下载后文件名
                    document.body.appendChild(downloadElement);
                    downloadElement.click(); //点击下载
                    document.body.removeChild(downloadElement); //下载完成移除元素
                    window.URL.revokeObjectURL(href); //释放blob对象*/
                    app.$message({
                        message: "导出成功",
                        type: 'success'
                    });
                }).catch(error =>{
                    app.$message({
                        message: "导出失败",
                        type: 'error'
                    });
                })
            } else if (typeId = 3) {
                axios({ // 用axios发送post请求
                    method: 'post',
                    url: "/file/exportOrder/"+typeId+".shtml", // 请求地址
                    data: app.searchEntity, // 参数
                    responseType: 'blob' // 表明返回服务器返回的数据类型
                }).then(res => { // 处理返回的文件流
                    const url = window.URL.createObjectURL(new Blob([res.data]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', app.fileName + ".xlsx");
                    document.body.appendChild(link);
                    link.click();
                    app.$message({
                        message: "导出成功",
                        type: 'success'
                    });
                }).catch(error =>{
                    app.$message({
                        message: "导出失败",
                        type: 'error'
                    });
                })
            }
        }

    },
    //钩子函数 初始化了事件和
    created: function () {

        this.searchList(1);

    }

})

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };

    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o){
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }

    return fmt;
}
