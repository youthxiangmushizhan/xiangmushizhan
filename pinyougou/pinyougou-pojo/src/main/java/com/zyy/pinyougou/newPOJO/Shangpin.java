package com.zyy.pinyougou.newPOJO;

import com.zyy.pinyougou.pojo.TbGoods;
import com.zyy.pinyougou.pojo.TbSeckillGoods;

import java.io.Serializable;

/**
 * @作者:大帥
 * @时间:2019/07/25 22:43
 */
public class Shangpin implements Serializable {

    private TbSeckillGoods tbSeckillGoods;

    private TbGoods tbGoods;

    public TbSeckillGoods getTbSeckillGoods() {
        return tbSeckillGoods;
    }

    public void setTbSeckillGoods(TbSeckillGoods tbSeckillGoods) {
        this.tbSeckillGoods = tbSeckillGoods;
    }

    public TbGoods getTbGoods() {
        return tbGoods;
    }

    public void setTbGoods(TbGoods tbGoods) {
        this.tbGoods = tbGoods;
    }
}
