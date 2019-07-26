package com.zyy.pinyougou.pojo;

import java.io.Serializable;

public class GoodsStatistical implements Serializable {

    private TbGoods tbGoods;
    private Integer statistical;

    public TbGoods getTbGoods() {
        return tbGoods;
    }

    public void setTbGoods(TbGoods tbGoods) {
        this.tbGoods = tbGoods;
    }

    public Integer getStatistical() {
        return statistical;
    }

    public void setStatistical(Integer statistical) {
        this.statistical = statistical;
    }
}
