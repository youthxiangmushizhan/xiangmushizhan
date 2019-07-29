package com.zyy.pinyougou.sellergoods.service;

import com.zyy.pinyougou.entity.PageBean;
import com.zyy.pinyougou.newPOJO.OrderTemplate;

import java.util.List;

public interface OrderTemplateService {

    PageBean<OrderTemplate> findPage(Integer pageNo, Integer pageSize, String timeType, String startTime, String endTime, OrderTemplate orderTemplate);

    List<OrderTemplate> searchOrderTemplate(String timeType, String startTime, String endTime, OrderTemplate orderTemplate);

}
