var app = new Vue({
    el:"#app",
    data:{
        entity:{},
        list:[],
        count:"0",
    },
    methods:{
        findFootmark:function () {
            axios.get('/user/findFootmark.shtml').then(
                function (response) {
                    //console.log(response.data)
                    app.list = response.data;
                    app.count = app.list.length;
                }
            )
        },
        addGoodsToCartList:function () {
            axios.get("http://localhost:9107/cart/addGoodsToCartList.shtml",{
                params:{
                    itemId:this.sku.id,
                    num:1
                },
                withCredentials:true
            }).then(function (response) {
                if (response.data.success) {
                    window.location.href = "http://localhost:9107/cart.html"
                } else {
                    alert(response.data.message)
                }
            })
        }
    },
    //钩子函数
    created:function () {
        this.findFootmark();
    }
})