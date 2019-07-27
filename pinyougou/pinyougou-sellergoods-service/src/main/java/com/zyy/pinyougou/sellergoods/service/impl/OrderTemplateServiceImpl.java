package com.zyy.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.entity.PageBean;
import com.zyy.pinyougou.mapper.TbOrderMapper;
import com.zyy.pinyougou.mapper.TbSeckillOrderMapper;
import com.zyy.pinyougou.newPOJO.OrderTemplate;
import com.zyy.pinyougou.pojo.TbOrder;
import com.zyy.pinyougou.pojo.TbSeckillOrder;
import com.zyy.pinyougou.sellergoods.service.OrderTemplateService;
import javafx.scene.text.TextBoundsType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderTemplateServiceImpl implements OrderTemplateService {

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    @Override
    public PageBean<OrderTemplate> findPage(Integer pageNo, Integer pageSize, String type, String timeType, String startTime, String endTime, OrderTemplate orderTemplate) {
        List<OrderTemplate> all = new ArrayList<>();
        if ("0".equals(type)) {
            //普通订单
            List<OrderTemplate> normalOrderList = getNormalOrderList(timeType,startTime,endTime,orderTemplate);
            all.addAll(normalOrderList);

        } else if ("1".equals(type)) {
            //秒杀订单
            List<OrderTemplate> seckillOrderList = getSeckillOrderList(timeType,startTime,endTime,orderTemplate);
            all.addAll(seckillOrderList);
        } else {
            //所有订单
            all.addAll(getNormalOrderList(timeType,startTime,endTime,orderTemplate));
            all.addAll(getSeckillOrderList(timeType,startTime,endTime,orderTemplate));
        }

        PageBean<OrderTemplate> pageBean = new PageBean<OrderTemplate>(pageNo,pageSize,all);

        return pageBean;
    }

    private List<OrderTemplate> getSeckillOrderList(String timeType, String startTime, String endTime, OrderTemplate orderTemplate) {

        Example example = new Example(TbSeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if (orderTemplate.getOrderId() != null && !"null".equals(String.valueOf(orderTemplate.getOrderId()))) {
            criteria.andLike("id","%"+orderTemplate.getOrderId()+"%");
        }
        if (StringUtils.isNotBlank(orderTemplate.getSellerId())) {
            criteria.andEqualTo("sellerId",orderTemplate.getSellerId());
        }
        if ("0".equals(timeType)) {
            //createTime
            if (StringUtils.isNotBlank(startTime)) {
                criteria.andGreaterThanOrEqualTo("createTime",startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                criteria.andLessThan("createTime",endTime);
            }
        } else if("1".equals(timeType)) {
            //payTime
            if (StringUtils.isNotBlank(startTime)) {
                criteria.andGreaterThanOrEqualTo("payTime",startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                criteria.andLessThan("payTime",endTime);
            }
        }
        if (StringUtils.isNotBlank(orderTemplate.getBrandName())) {

        }

        List<OrderTemplate> list = new ArrayList<>();
        List<TbSeckillOrder> seckillOrders = seckillOrderMapper.selectByExample(example);
        for (TbSeckillOrder seckillOrder : seckillOrders) {
            OrderTemplate template = new OrderTemplate();
            template.setOrderType("1");
            template.setOrderId(seckillOrder.getId());
            template.setPayment(seckillOrder.getMoney());
            template.setPaymentType("1");
            template.setSellerId(seckillOrder.getSellerId());
            template.setUserId(seckillOrder.getUserId());
            template.setCreateTime(seckillOrder.getCreateTime());
            template.setPaymentTime(seckillOrder.getPayTime());
            template.setStatus(seckillOrder.getStatus());
            list.add(template);
        }

        return list;
    }

    private List<OrderTemplate> getNormalOrderList(String timeType, String startTime, String endTime, OrderTemplate orderTemplate) {
        Example example = new Example(TbOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if (orderTemplate.getOrderId() != null && !"null".equals(String.valueOf(orderTemplate.getOrderId()))) {
            criteria.andLike("orderId","%"+orderTemplate.getOrderId()+"%");
        }
        if (StringUtils.isNotBlank(orderTemplate.getSellerId())) {
            criteria.andEqualTo("sellerId",orderTemplate.getSellerId());
        }
        if ("0".equals(timeType)) {
            //createTime
            if (StringUtils.isNotBlank(startTime)) {
                criteria.andGreaterThanOrEqualTo("createTime",startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                criteria.andLessThan("createTime",endTime);
            }
        } else if("1".equals(timeType)) {
            //payTime
            if (StringUtils.isNotBlank(startTime)) {
                criteria.andGreaterThanOrEqualTo("paymentTime",startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                criteria.andLessThan("paymentTime",endTime);
            }
        }

        if (StringUtils.isNotBlank(orderTemplate.getBrandName())) {

        }

        List<OrderTemplate> list = new ArrayList<>();
        List<TbOrder> tbOrders = orderMapper.selectByExample(example);
        for (TbOrder tbOrder : tbOrders) {
            OrderTemplate template = new OrderTemplate();
            template.setOrderType("0");
            template.setOrderId(tbOrder.getOrderId());
            template.setPayment(tbOrder.getPayment());
            template.setPaymentType(tbOrder.getPaymentType());
            template.setSellerId(tbOrder.getSellerId());
            template.setUserId(tbOrder.getUserId());
            template.setCreateTime(tbOrder.getCreateTime());
            template.setPaymentTime(tbOrder.getPaymentTime());
            template.setStatus(tbOrder.getStatus());
            list.add(template);
        }

        return list;
    }
}
