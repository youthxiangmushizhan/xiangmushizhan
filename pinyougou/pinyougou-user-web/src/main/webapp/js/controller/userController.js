var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageNo:1,
        list:[],
        entity:{career:"",address:"",birthday:""},
        ids:[],
        smsCode:"",
        loginName:"",
        searchEntity:{},
        birthday:{},
        address:{},
    },
    methods: {
        getUsername:function () {
            axios.get("/login/getUsername.shtml").then(function (response) {
                app.loginName = response.data
                if (app.loginName) {
                    app.findUserByUsername(app.loginName);
                } else {
                    app.checkStatus()
                }
            }).catch(function (error) {
                console.log(123123123)
            })
        },
        searchList:function (curPage) {
            axios.post('/user/search.shtml?pageNo='+curPage,this.searchEntity).then(function (response) {
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
            axios.get('/user/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data;

            }).catch(function (error) {

            })
        },
         findPage:function () {
            var that = this;
            axios.get('/user/findPage.shtml',{params:{
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
            axios.post("/user/add/"+this.smsCode+".shtml",this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    window.location.href = "home-index.html"
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update:function () {
            this.getData();
            axios.post('/user/update.shtml',this.entity).then(function (response) {
                console.log(response);
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
        findUserByUsername:function (username) {
            axios.get('/user/findUserByUsername/'+username+'.shtml').then(function (response) {
                app.entity=response.data;
                /*var split = app.entity.address.split(" ");
                app.address.province=split[0];
                app.address.city=split[1];
                app.address.district=split[2];*/
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele:function () {
            axios.post('/user/delete.shtml',this.ids).then(function (response) {
                console.log(response);
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        createSmsCode: function () {
            axios.get("/user/sendCode.shtml?phone=" + this.entity.phone).then(function (response) {
                if (response.data.success) {
                    alert(response.data.message)
                } else {
                    alert(response.data.message)
                }
            })
        },


        checkStatus: function () {
            axios.get("login/getUsername.shtml").then(a => {

                if (!a.data.success) {
                    window.location.href = "http://localhost:9400/cas/logout?service=http://localhost:9106/shoplogin.html"
                } else {

                }
            })
        },

        formSubmit: function () {
            var that = this;
            this.$validator.validate().then(function (result) {
                if (result) {
                    console.log(that);
                    axios.post("/user/add/"+that.smsCode+".shtml",that.entity).then(function (response) {
                        if (response.data.success) {
                            window.location.href = "home-index.html"
                        } else {
                            that.$validator.errors.add(response.data.errorList);
                        }
                    }).catch(function (error) {
                        console.log("123123123")
                    })
                }
            })
        },
        getData:function () {
            var area = this.address.province+" "+this.address.city+" "+this.address.district;
            //var date = this.birthday.year+":"+this.birthday.month+":"+this.birthday.day;
            var date = new Date(this.birthday.year,this.birthday.month,this.birthday.day);
            app.entity.address=area;
            app.entity.birthday=date;
        },
        upload:function () {
            var formData=new FormData();
            //参数formData.append('file' 中的file 为表单的参数名  必须和 后台的file一致
            //file.files[0]  中的file 指定的时候页面中的input="file"的id的值 files 指定的是选中的图片所在的文件对象数组，这里只有一个就选中[0]
            formData.append('file', up_img_WU_FILE_0.files[0]);
            axios({
                url: 'http://localhost:9110/upload/uploadFile.shtml',
                data: formData,
                method: 'post',
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                //开启跨域请求携带相关认证信息
                withCredentials:true
            }).then(function (response) {
                if(response.data.success){
                    //上传成功
                    console.log(this);
                    app.entity.headPic=response.data.message;
                    //console.log(app.entity.headPic);
                }else{
                    //上传失败
                    alert(response.data.message);
                }
            })
        },



    },
    //钩子函数 初始化了事件和
    created: function () {
        this.getUsername()
        this.setExperience()
    },

})
