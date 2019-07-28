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
            status:'2'
        },
        consign:{
            orderId:"",
            shoppingCode:"",
            shoppingName:""
        },
        dialogFormVisible1: false,
        dialogFormVisible2: false,
        formLabelWidth: '110px',
        multipleSelection: [],
        statusList:[
            {label:"普通订单",value:'0'},
            {label:"秒杀订单",value:'1'},
            {label:"所有订单",value:''}
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
        startTime:"",
        endTime:""
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

            /*app.startTime = new Date(this.timeScope[0]).Format("yyyy-MM-dd hh:mm:ss")
            app.endTime = new Date(this.timeScope[1]).Format("yyyy-MM-dd hh:mm:ss")*/

            axios.post('/orderTemplate/search.shtml?pageNo='+curPage+'&pageSize='+this.pageSize+'&type='+this.orderType+'&startTime='+ this.startTime+'&endTime='+this.endTime,this.searchEntity).then(function (response) {
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
        consignOrder:function () {

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
