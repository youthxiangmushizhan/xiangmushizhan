let app = new Vue({
    el: "#app",
    data: {
        list:[],
        orderItemList:[],
        orderStatus:["","未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"],
        pageNo:1,
        pages:10,
        selectKey:{}
    },
    methods: {
        findOrderBySellerId:function (pageNo) {
            axios.post("/order/findOrderBySellerId.shtml?pageNo="+pageNo,this.selectKey).then(function (response) {
                app.list = response.data.list;
                app.pageNo=pageNo;
                app.pages=response.data.pages;
            })
        },
        getItemByOrder:function (orderId) {
            axios.get("/order/getItemByOrder.shtml?orderId=" + orderId).then(function (response) {
                app.orderItemList = response.data;
            })
        }
    },
    created: function () {
        this.findOrderBySellerId(1);
    }
});
