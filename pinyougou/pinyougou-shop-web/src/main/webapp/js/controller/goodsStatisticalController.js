let app = new Vue({
    el: "#app",
    data: {
        startDate:"2019-09-09",
        endDate:"2019-09-09",
        brandList:[],
        list:[],
        pageNo:1,
        pages:10
    },
    methods: {
        changeStartDate:function () {
            let startDate = new Date();
            let year = startDate.getFullYear().toString();
            let month = startDate.getMonth();
            let day = startDate.getDate();
            let nowMonth = startDate.getMonth() + 1;
            if (month < 10) {
                month = "0" + month.toString();
            }else {
                month = month.toString()
            }
            if (nowMonth < 10) {
                nowMonth = "0" + nowMonth.toString();
            }else {
                nowMonth = nowMonth.toString();
            }
            if (day < 10) {
                day = "0" + day.toString();
            }else {
                day = day.toString();
            }
            let nowDate = year + "-" + nowMonth + "-" + day;
            let preDate = year + "-" + month + "-" + day;

            app.startDate = preDate;
            app.endDate = nowDate;
        },
        getBrandList:function(){
            axios.get("/goods/getBrandList.shtml").then(function (response) {
                app.brandList = response.data;
                for (let i = 0; i < app.brandList.length; i++) {
                    app.brandList[i] = app.brandList[i].name;
                }
            })
        },
        queryGoodsStatistical:function (pageNo) {
            axios.get("/goods/queryGoodsStatistical.shtml?pageNo=" + pageNo + "&startDate=" + app.startDate + "&endDate=" + app.endDate).then(function (response) {
                app.list = response.data.list;
                app.pageNo=pageNo;
                app.pages=response.data.pages;
            })
        }
    },
    created: function () {
        window.setTimeout(function () {
            app.getBrandList();
            app.changeStartDate();
            app.queryGoodsStatistical(1);
        },1);
    }

});