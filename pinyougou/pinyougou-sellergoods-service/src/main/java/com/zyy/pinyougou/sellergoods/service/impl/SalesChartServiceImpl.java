package com.zyy.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zyy.pinyougou.mapper.SellerDataMapper;
import com.zyy.pinyougou.mapper.TbOrderMapper;
import com.zyy.pinyougou.mapper.TbSeckillOrderMapper;
import com.zyy.pinyougou.newPOJO.SellerData;
import com.zyy.pinyougou.pojo.TbOrder;
import com.zyy.pinyougou.pojo.TbSeckillOrder;
import com.zyy.pinyougou.sellergoods.service.SalesChartService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SalesChartServiceImpl implements SalesChartService {

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SellerDataMapper dataMapper;

    @Override
    public List<SellerData> getSaleCountsByDate(Date date, String sellerId) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH,1);
        Date date1 = instance.getTime();
        List<SellerData> sellerDatas = dataMapper.getSellerDataByDate(date, date1, sellerId);
        return sellerDatas;

    }

    @Override
    public List<SellerData> getSaleCountsByDateAndSellerId(Date start, Date end, String sellerId) {
        List<SellerData> sellerDatas = dataMapper.getSellerDataByDate(start, end, sellerId);
        return sellerDatas;
    }
}
