var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageNo:1,
        list:[],
        entity:{alias:''},
        ids:[],
        searchEntity:{},
    },
    methods: {
        searchList:function (curPage) {
            axios.post('/address/search.shtml?pageNo='+curPage,this.searchEntity).then(function (response) {
                //获取数据
                app.list=response.data.list;

                //当前页
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
            });
        },
        //查询所有品牌列表
        findAll:function () {
            console.log(app);
            axios.get('/address/findAddressByUserId.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data;

            }).catch(function (error) {

            })
        },
        getAddress:function(address){
            var dizhi = "";
            if (address.provinceId != null) {
                dizhi += address.provinceId;
            }
            if (address.cityId != null) {
                dizhi += address.cityId;
            }
            if (address.townId != null) {
                dizhi += address.townId;
            }
            if (address.address != null) {
                dizhi += address.address;
            }
            return dizhi;
        },
         findPage:function () {
            var that = this;
            axios.get('/address/findPage.shtml',{params:{
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
            axios.post('/address/add.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.findAll();
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        change:function(address) {
            //var parse = JSON.parse(JSON.stringify(address));
            var demo = address;
            this.entity = JSON.parse(JSON.stringify(demo));

            //app.entity=parse;

            //this.$set(app,entity,parse)
        },
        update:function () {
            axios.post('/address/update.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.findAll();
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
            axios.get('/address/findOne/'+id+'.shtml').then(function (response) {
                app.entity=response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele:function (id) {
            axios.get('/address/delete/'+id+'.shtml').then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.findAll();
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        changeDefault:function (id) {
            axios.get('/address/changeDefault/'+id+'.shtml').then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.findAll();
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        getAlias:function (alias) {
            //alert(alias)
            this.entity.alias = alias
        }



    },
    //钩子函数 初始化了事件和
    created: function () {
      
        this.findAll();

    }

})
