package com.zyy.pinyougou.newPOJO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商家统计数据类
 */
public class SellerData implements Serializable {

    //商家名称
    private String sellerName;

    //商家Id
    private String sellerId;

    //销售件数
    private Integer saleNum;

    //销售额
    private BigDecimal saleMoney;

    public SellerData() {

    }

    public SellerData(String sellerName, String sellerId, Integer saleNum, BigDecimal saleMoney) {
        this.sellerName = sellerName;
        this.saleNum = saleNum;
        this.saleMoney = saleMoney;
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public BigDecimal getSaleMoney() {
        return saleMoney;
    }

    public void setSaleMoney(BigDecimal saleMoney) {
        this.saleMoney = saleMoney;
    }
}
