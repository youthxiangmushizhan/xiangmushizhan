package com.zyy.pinyougou.sellergoods.service;

import com.zyy.pinyougou.entity.PageBean;
import com.zyy.pinyougou.newPOJO.OrderTemplate;

public interface OrderTemplateService {

    PageBean<OrderTemplate> findPage(Integer pageNo, Integer pageSize, String type, String timeType, String startTime, String endTime, OrderTemplate orderTemplate);

}
