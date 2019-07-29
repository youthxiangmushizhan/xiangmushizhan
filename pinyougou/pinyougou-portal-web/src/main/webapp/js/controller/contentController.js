﻿var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageNo:1,
        list:[],
        entity:{},
        ids:[],
        searchEntity:{},
        contentList:[],
        content6List:[],
        categoryList:[],
        keywords:'',
        allItemCatList:[],
    },
    methods: {
        searchList:function (curPage) {

            axios.post('/content/search.shtml?pageNo='+curPage,this.searchEntity).then(function (response) {
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
            axios.get('/content/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data;

            }).catch(function (error) {

            })
        },
         findPage:function () {
            var that = this;
            axios.get('/content/findPage.shtml',{params:{
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
            axios.post('/content/add.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update:function () {
            axios.post('/content/update.shtml',this.entity).then(function (response) {
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
            axios.get('/content/findOne/'+id+'.shtml').then(function (response) {
                app.entity=response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele:function () {
            axios.post('/content/delete.shtml',this.ids).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        findByCategoryId:function (categoryId) {
            axios.get('/content/findByCategoryId/'+categoryId+'.shtml').then(function (response) {
                if (categoryId==1) {
                    app.contentList=response.data;
                }
                if (categoryId==6) {
                    app.content6List=response.data;
                }

            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        doSearch:function () {
            window.location.href = "http://localhost:9104/search.html?keywords="+encodeURIComponent(this.keywords)
        },

        //获取所有分类的类别的方法
        findAllItemCatList:function (parentId) {
            axios.get('/itemCat/findAllItemCatList/'+parentId+'.shtml').then(
                function (response) {
                    app.allItemCatList=response.data;
                }
            )
        }




    },
    //钩子函数 初始化了事件和
    created: function () {

        this.findByCategoryId(1);
        this.findByCategoryId(6);

        this.searchList(1);

        this.findAllItemCatList(0);


    }

})
