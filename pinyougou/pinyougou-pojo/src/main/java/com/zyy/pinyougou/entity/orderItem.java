package com.zyy.pinyougou.entity;

import com.zyy.pinyougou.pojo.TbGoods;
import com.zyy.pinyougou.pojo.TbItem;
import com.zyy.pinyougou.pojo.TbOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName orderItem
 * @Description
 * @Author miaomiaole
 * @Date 2019/7/26 8:30
 * Version 1.0
 **/
public class orderItem implements Serializable {


    private TbItem tbItem;

    private TbGoods goods;


    private List<TbOrder> orderList;

    public List<TbOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<TbOrder> orderList) {
        this.orderList = orderList;
    }

    private String title;


    private String picPath;

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public TbItem getTbItem() {
        return tbItem;
    }

    public String price;


    public void setTbItem(TbItem tbItem) {
        this.tbItem = tbItem;
    }

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    private BigDecimal totalMoney;

    private Integer totalNum;

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }
}
