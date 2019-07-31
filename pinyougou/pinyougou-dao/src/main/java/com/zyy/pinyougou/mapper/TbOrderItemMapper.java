package com.zyy.pinyougou.mapper;

import com.zyy.pinyougou.pojo.TbOrderItem;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface TbOrderItemMapper extends Mapper<TbOrderItem> {

    List<TbOrderItem> findByUserIdAndDate(@Param("start") Date start,@Param("end") Date end,@Param("userId") String userId);
}