﻿var app = new Vue({
    el: "#app",
    data: {
        pages: 15,
        pageNo: 1,
        list: [],
        entity: {},
        ids: [],
        searchEntity: {},
        status: ['未付款', '已付款', '未发货', '已发货', '交易关闭', '待评价'],
        payType: ['在线支付', '货到付款', '']
    },
    methods: {


        changeTpye: function () {

            this.searchList(1)
        },



        searchList: function (curPage) {

            axios.post('/order/search.shtml?pageNo=' + curPage, this.searchEntity).then(function (response) {

                //获取数据
                app.list = response.data.list;

                //当前页
                app.pageNo = curPage;
                //总页数
                app.pages = response.data.pages;
            });
        },
        //查询所有品牌列表
        findAll: function () {
            console.log(app);
            axios.get('/order/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list = response.data;

            }).catch(function (error) {

            })
        },
        findPage: function () {
            var that = this;
            axios.get('/order/findPage.shtml', {
                params: {
                    pageNo: this.pageNo
                }
            }).then(function (response) {
                console.log(app);
                //注意：this 在axios中就不再是 vue实例了。
                app.list = response.data.list;
                app.pageNo = curPage;
                //总页数
                app.pages = response.data.pages;
            }).catch(function (error) {

            })
        },
        //该方法只要不在生命周期的
        add: function () {
            alert("不允许后台添加订单")
        },
        update: function () {
            axios.post('/order/update.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        save: function () {

            if (this.entity.orderId != null) {
                this.update();
            } else {
                this.add();
            }
        },
        findOne: function (id) {

            axios.get('/order/findOne.shtml?id=' + id).then(function (response) {
                app.entity = response.data;
            })
        },
        dele: function () {
            if(app.ids==''){
                alert("请勾选删除订单")
            }else {
                if (confirm("你确定要删除" + app.ids + "订单吗?")) {

                    axios.post('/order/delete.shtml', this.ids).then(function (response) {
                        console.log(response);
                        if (response.data.success) {
                            app.ids = [];
                            app.searchList(1);
                        }
                    }).catch(function (error) {
                        console.log("1231312131321");
                    });
                } else {
                    app.ids = [];
                    app.searchList(1);
                }
            }

        }


    },
    //钩子函数 初始化了事件和
    created: function () {

        this.searchList(1)

    }

})
