﻿var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageNo:1,
        list:[],
        entity:{},
        ids:[],
        searchEntity:{},
        loginName:"",
        spec:{},
        orderStatus:['','未付款','已付款','未发货','已发货','交易成功','交易关闭','待评价'],
        afterStatus:['','请及时付款','提醒发货','提醒发货','等待收货','','','评价'],
    },
    methods: {
        getUsername:function () {
            axios.get("/login/getUsername.shtml").then(function (response) {
                app.loginName = response.data.message
                if (app.loginName.success) {
                    app.findUserByUsername(app.loginName);
                }
               /* else {
                    app.checkStatus()
                }*/
            }).catch(function (error) {
                console.log(123123123)
            })
        },
        /*checkStatus: function () {
            axios.get("/login/getUsername.shtml").then(a => {

                if (!a.data.success) {
                    window.location.href = "http://localhost:9400/cas/logout?service=http://localhost:9106/shoplogin.html"
                } else {

                }
            })
        },*/
        searchList:function (curPage) {
            axios.post('/order/search.shtml?pageNo='+curPage,this.searchEntity).then(function (response) {
                //获取数据
                app.list=response.data.list;

                //当前页
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
            });
        },
        //查询所有品牌列表
        findAll:function (status) {
            //console.log(app);
            axios.get('/order/findAll/'+status+'.shtml').then(function (response) {
                //console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data;

            }).catch(function (error) {

            })
        },
         findPage:function () {
            var that = this;
            axios.get('/order/findPage.shtml',{params:{
                pageNo:this.pageNo
            }}).then(function (response) {
                console.log(app);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data.list;
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
            }).catch(function (error) {

            })
        },
        //该方法只要不在生命周期的
        add:function () {
            axios.post('/order/add.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update:function () {
            axios.post('/order/update.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        save:function () {
            if(this.entity.id!=null){
                this.update();
            }else{
                this.add();
            }
        },
        findOne:function (id) {
            axios.get('/order/findOne/'+id+'.shtml').then(function (response) {
                app.entity=response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele:function () {
            axios.post('/order/delete.shtml',this.ids).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        }



    },
    //钩子函数 初始化了事件和
    created: function () {

        this.getUsername();
      
        //this.findPage();

        //var urlParam = this.getUrlParam();

        if (window.location.href.indexOf("home-index.html") != -1) {
            //查询所有订单
            this.findAll("0");
        } else {
            //查询未付款订单
            this.findAll("1");
        }

    }

})
