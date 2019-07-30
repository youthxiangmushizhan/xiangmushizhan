package com.zyy.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.zyy.pinyougou.newPOJO.SellerData;
import com.zyy.pinyougou.pojo.TbSeller;
import com.zyy.pinyougou.sellergoods.service.SalesChartService;
import com.zyy.pinyougou.sellergoods.service.SellerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/salesChart")
public class SalesChartController {

    @Reference
    private SellerService sellerService;

    @Reference
    private SalesChartService salesChartService;

    @RequestMapping("/getSalesChart")
    public Map getSalesChart(String startDate, String endDate) {
        Map<String,Object> map = new HashMap();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            map.put("flag", true);
            List<SellerData> saleDatas = salesChartService.getSaleCountsByDateAndSellerId(start, end, null);
            List<String> sellerList = new ArrayList();
            List<JSONObject> saleMoney = new ArrayList();
            List<JSONObject> saleNum = new ArrayList();
            JSONObject object1 = new JSONObject();
            int count = 5;
            for (SellerData saleData : saleDatas) {
                boolean flag = false;
                sellerList.add(saleData.getSellerName());
                if (count > 0 ) {
                    flag = true;
                }
                count--;
                object1.put(saleData.getSellerName(),flag);
                JSONObject money = new JSONObject();
                JSONObject num = new JSONObject();
                money.put("name",saleData.getSellerName());
                money.put("value",saleData.getSaleMoney().doubleValue());
                num.put("name",saleData.getSellerName());
                num.put("value",saleData.getSaleNum());
                saleMoney.add(money);
                saleNum.add(num);
            }

            map.put("allSeller",sellerList);
            map.put("saleMoney",saleMoney);
            map.put("saleNum",saleNum);
            map.put("selected",object1);
            return map;
        } catch (ParseException e) {
            e.printStackTrace();
            map.put("flag", false);
            map.put("message", "服务器异常");
            return map;
        }

    }
}
