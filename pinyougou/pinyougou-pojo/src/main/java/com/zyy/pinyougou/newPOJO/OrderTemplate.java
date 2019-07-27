package com.zyy.pinyougou.newPOJO;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    private BigDecimal payment;

    /**
     * 支付类型，1、在线支付，2、货到付款
     */
    private String paymentType;

    /**
     * 订单类型，0--普通订单，1--秒杀付款
     */
    private String OrderType;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商家ID
     */
    private String sellerId;

    /**
     * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
     */
    private String status;

    /**
     * 品牌名称
     */
    private String brandName;

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
