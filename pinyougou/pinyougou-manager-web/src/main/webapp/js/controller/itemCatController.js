var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageNo:1,
        list:[],
        entity:{parentId:0},
        ids:[],
        searchEntity:{
            status:""
        },
        entity1:{},
        entity2:{},
        grade:1,
        dialogFormVisible: false,
        formLabelWidth: '120px',
        multipleSelection: [],
        statusList:[
            {label:"未审核",value:'0'},
            {label:"审核通过",value:'1'},
            {label:"审核未通过",value:'2'},
            {label:"全选",value:''}
        ]
    },
    methods: {
        updateStatus:function (status,id) {
            if (id != null) {
                this.ids.push(id)
            }
            axios.post("/itemCat/updateItemCatStatus.shtml?status="+status, this.ids).then(function (response) {
                if (response.data.success) {
                    app.searchList({id:app.entity.parentId});
                    app.ids = []
                }
            })
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
        //上传之前进行文件格式校验
        beforeUpload(file){
            const isXLS = file.type === 'application/vnd.ms-excel';
            if(isXLS){
                return true;
            }
            const isXLSX = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
            if (isXLSX) {
                return true;
            }
            this.$message.error('上传文件只能是xls或者xlsx格式!');
            return false;
        },
        //下载模板文件（路径）
        downloadTemplate(){
            window.location.href="../template/Template_TbItemCat.xlsx";
        },
        //上传成功提示
        handleSuccess(response, file) {
            if(response.success){
                this.$message({
                    message: response.message,
                    type: 'success'
                });
                window.location.reload();
            }else{
                this.$message.error(response.message);
            }
            console.log(response, file);
        },
        searchList:function (p_entity) {

            if (this.grade == 1) {
                this.entity1 = {}
                this.entity2 = {}
            } else if (this.grade == 2) {
                this.entity1 = p_entity
                this.entity2 = {}
            } else if (this.grade == 3) {
                this.entity2 = p_entity
            }

            this.findAll(p_entity.id)

            /*axios.post('/itemCat/search.shtml?pageNo='+curPage,this.searchEntity).then(function (response) {
                //获取数据
                app.list=response.data.list;

                //当前页
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
            });*/
        },
        //查询所有品牌列表
        findAll:function (id) {
            console.log(app);
            axios.get('/itemCat/findByParentId/'+id+'.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data;
                app.entity.parentId = id
            }).catch(function (error) {

            })
        },
         findPage:function (id) {
            var that = this;
            axios.get('/itemCat/findPage.shtml',{params:{
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
            console.log(this.entity)
            axios.post('/itemCat/add.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList(entity.parentId);

                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update:function () {
            axios.post('/itemCat/update.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList({id:app.entity.parentId});
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
            axios.get('/itemCat/findOne/'+id+'.shtml').then(function (response) {
                app.entity=response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele:function () {
            axios.post('/itemCat/delete.shtml',this.ids).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList({id:0});
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        }



    },
    //钩子函数 初始化了事件和
    created: function () {
      
        this.searchList({id:0});

    }

})
