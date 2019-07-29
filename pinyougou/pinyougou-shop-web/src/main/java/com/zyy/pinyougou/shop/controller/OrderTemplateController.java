package com.zyy.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.entity.PageBean;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.newPOJO.OrderTemplate;
import com.zyy.pinyougou.pojo.TbOrder;
import com.zyy.pinyougou.pojo.TbSeckillOrder;
import com.zyy.pinyougou.sellergoods.service.OrderService;
import com.zyy.pinyougou.sellergoods.service.OrderTemplateService;
import com.zyy.pinyougou.sellergoods.service.SeckillOrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.Temporal;
import java.util.Date;


@RestController
@RequestMapping("/orderTemplate")
public class OrderTemplateController {

    @Reference
    private OrderTemplateService orderTemplateService;

    @Reference
    private OrderService orderService;

    @Reference
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/search")
    public PageBean<OrderTemplate> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                            @RequestParam(value = "timeType",defaultValue = "",required = false) String timeType,
                                            String startTime,
                                            String endTime,
                                            @RequestBody OrderTemplate orderTemplate) {
        System.out.println(startTime + "-----------" + endTime);
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        orderTemplate.setSellerId(sellerId);
        return orderTemplateService.findPage(pageNo, pageSize,timeType,startTime,endTime,orderTemplate);
    }

    @RequestMapping("/consignOrder")
    public Result consignOrder(@RequestBody OrderTemplate orderTemplate) {
        try {
            Date date = new Date();
            String orderType = orderTemplate.getOrderType();
            if ("普通订单".equals(orderType)) {
                //普通订单
                TbOrder tbOrder = new TbOrder();
                tbOrder.setConsignTime(date);
                tbOrder.setOrderId(Long.valueOf(orderTemplate.getOrderId()));
                tbOrder.setStatus("4");
                tbOrder.setShippingName(orderTemplate.getShippingName());
                tbOrder.setShippingCode(orderTemplate.getShippingCode());
                orderService.updateByPrimaryKeySelective(tbOrder);

            } else if ("秒杀订单".equals(orderType)) {
                //秒杀订单
                TbSeckillOrder seckillOrder = new TbSeckillOrder();
                seckillOrder.setConsignTime(date);
                seckillOrder.setId(Long.valueOf(orderTemplate.getOrderId()));
                seckillOrder.setStatus("3");
                seckillOrder.setShippingName(orderTemplate.getShippingName());
                seckillOrder.setShippingCode(orderTemplate.getShippingCode());
                seckillOrderService.updateByPrimaryKeySelective(seckillOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发货失败");
        }

        return new Result(true, "发货成功");
    }

}
