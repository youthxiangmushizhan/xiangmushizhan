package com.zyy.pinyougou.mapper;

import com.zyy.pinyougou.newPOJO.SellerData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SellerDataMapper {

    List<SellerData> getSellerDataByDate(@Param("startDate") Date startDate,@Param("endDate") Date endDate, @Param("sellerId") String sellerId);
}
