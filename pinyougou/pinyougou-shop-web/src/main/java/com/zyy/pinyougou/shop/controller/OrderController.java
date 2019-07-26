package com.zyy.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.mapper.TbOrderMapper;
import com.zyy.pinyougou.pojo.TbItem;
import com.zyy.pinyougou.pojo.TbOrder;
import com.zyy.pinyougou.pojo.TbOrderItem;
import com.zyy.pinyougou.sellergoods.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/findOrderBySellerId")
    public PageInfo<TbOrder> findOrderBySellerId(@RequestParam(value = "pageNo", defaultValue = "1", required = true)Integer pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = "10", required = true)Integer pageSize,
                                                 @RequestBody TbOrder order){
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.findOrderBySellerId(sellerId,pageNo,pageSize,order);
    }

    @RequestMapping("/getItemByOrder")
    public List<TbOrderItem> getItemByOrder(Long orderId){
        return orderService.getItemByOrder(orderId);
    }

}
