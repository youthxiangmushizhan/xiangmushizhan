var app = new Vue({
    el: "#app",
    data: {
        pages: 15,
        pageNo: 1,
        entity: {},
        list: [],
        ids: [],
        searchEntity: {},
        dialogFormVisible: false,
        formLabelWidth: '110px',
        multipleSelection: [],
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
        loading:true,
        isHidden:false,
        timeScope:[],
        timeType:"",
        fileName:"",
        startTime:"",
        endTime:""
    },

    methods: {
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
        searchOrderItemList:function (curPage) {
            console.log(this.searchEntity)

            if (this.timeScope != '') {
                app.startTime = new Date(this.timeScope[0]).Format("yyyy-MM-dd hh:mm:ss")
                app.endTime = new Date(this.timeScope[1]).Format("yyyy-MM-dd hh:mm:ss")
            }

            axios.post('/orderTemplate/search.shtml?pageNo='+curPage+'&pageSize='+this.pageSize+'&type='+this.orderType+'&startTime='+ this.startTime+'&endTime='+this.endTime,this.searchEntity).then(function (response) {
                //获取数据
                app.list=response.data.list;

                console.log(app.list)

                //当前页
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
                //总条数
                app.total = response.data.total;

                app.loading = false
            });
        },
        exportUserOrderItem:function (typeId) {

            if (this.fileName == '') {
                this.fileName = new Date().getTime()
            }

            if (typeId == "1") {
                const form = {orderType:typeId} // 要发送到后台的数据
                axios({ // 用axios发送post请求
                    method: 'post',
                    url: "/file/exportOrderItem.shtml?orderType="+typeId, // 请求地址
                    data: form, // 参数
                    responseType: 'blob' // 表明返回服务器返回的数据类型
                }).then(res => { // 处理返回的文件流
                    const url = window.URL.createObjectURL(new Blob([res.data]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', app.fileName + ".xlsx");
                    document.body.appendChild(link);
                    link.click();
                    app.fileName=""
                    app.$message({
                        message: "导出成功",
                        type: 'success'
                    });
                }).catch(error =>{
                    app.fileName=""
                    app.$message({
                        message: "导出失败",
                        type: 'error'
                    });
                })
            } else if (typeId = "2") {
                axios({ // 用axios发送post请求
                    method: 'post',
                    url: '/file/exportOrder.shtml?startTime='+this.startTime+'&endTime='+this.endTime, // 请求地址
                    data: app.searchEntity, // 参数
                    responseType: 'blob' // 表明返回服务器返回的数据类型
                }).then(res => { // 处理返回的文件流
                    const url = window.URL.createObjectURL(new Blob([res.data]));
                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', app.fileName + ".xlsx");
                    document.body.appendChild(link);
                    link.click();
                    app.fileName=""
                    app.$message({
                        message: "导出成功",
                        type: 'success'
                    });
                }).catch(error =>{
                    app.fileName=""
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

        this.searchOrderItemList(1);
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
