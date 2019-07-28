var app = new Vue({
    el: "#app",
    data: {
        total: '',
        pages: 10,
        pageNo: 1,
        list: [],
        entity: {},
        ids: [],
        searchEntity: {},
        status: ["未冻结", "已冻结"],
        sex: ["男", "女"],
        typeSource: ["PC", "H5", "Android", "IOS", "WeChat"],
        isCheck: ['否', '是'],
        experience: ['活跃用户', '非活跃用户']
    },
    methods: {

        changeTpye: function () {
            this.searchList(1)
        },

        searchList: function (curPage) {
            axios.post('/user/search.shtml?pageNo=' + curPage, this.searchEntity).then(function (response) {
                //获取数据

                app.total = response.data.total

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
            axios.get('/user/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list = response.data;

            }).catch(function (error) {

            })
        },
        findPage: function () {
            var that = this;
            axios.get('/user/findPage.shtml', {
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
            axios.post('/user/add.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update: function () {
            axios.post('/user/update.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        save: function () {
            if (this.entity.id != null) {
                this.update();
            } else {
                this.add();
            }
        },
        findOne: function (id) {
            axios.get('/user/findOne/' + id + '.shtml').then(function (response) {
                app.entity = response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele: function () {
            axios.post('/user/delete.shtml', this.ids).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },

        updateStatus: function () {

            if (this.entity.status == 1) {
                if (confirm("你确定要冻结该用户吗?")) {
                    axios.post('/user/update.shtml', this.entity).then(function (response) {
                    })
                }

            } else {
                if (confirm("你确定要解除该用户限制吗?")) {
                    axios.post('/user/update.shtml', this.entity).then(function (response) {
                    })
                }
            }
        },

        lockUser: function () {
            axios.post("/user/lockUser.shtml").then(a => {
                if (a.data.success) {
                    console.log(a.data.message);
                } else {
                    console.log(a.data.message);
                }
            })
        }


    },
    //钩子函数 初始化了事件和
    created: function () {
        this.lockUser();
        this.searchList(1);
    }

})
