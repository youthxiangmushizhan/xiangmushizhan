package com.zyy.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.entity.PageBean;
import com.zyy.pinyougou.newPOJO.OrderTemplate;
import com.zyy.pinyougou.sellergoods.service.OrderTemplateService;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/orderTemplate")
public class OrderTemplateController {

    @Reference
    private OrderTemplateService orderTemplateService;

    @RequestMapping("/search")
    public PageBean<OrderTemplate> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                            @RequestParam(value = "timeType",defaultValue = "",required = false) String timeType,
                                            String startTime,
                                            String endTime,
                                            @RequestBody OrderTemplate orderTemplate) {
        System.out.println(startTime + "-----------" + endTime);
        return orderTemplateService.findPage(pageNo, pageSize,timeType,startTime,endTime,orderTemplate);
    }

}
