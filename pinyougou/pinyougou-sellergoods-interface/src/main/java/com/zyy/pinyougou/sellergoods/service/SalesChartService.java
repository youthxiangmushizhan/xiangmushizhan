package com.zyy.pinyougou.sellergoods.service;

import com.zyy.pinyougou.newPOJO.SellerData;

import java.util.Date;
import java.util.List;

public interface SalesChartService {

    List<SellerData> getSaleCountsByDate(Date date, String sellerId);

    List<SellerData> getSaleCountsByDateAndSellerId(Date start, Date end, String sellerId);
}
