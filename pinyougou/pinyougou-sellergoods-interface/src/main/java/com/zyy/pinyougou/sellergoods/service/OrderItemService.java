package com.zyy.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.core.service.CoreService;
import com.zyy.pinyougou.entity.orderItem;
import com.zyy.pinyougou.pojo.TbOrderItem;

import java.util.Date;
import java.util.List;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface OrderItemService extends CoreService<TbOrderItem> {


    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<TbOrderItem> findPage(Integer pageNo, Integer pageSize);


    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */

    public PageInfo<TbOrderItem> findPage(Integer pageNo, Integer pageSize, TbOrderItem orderItem);


    List<orderItem> findOrderItem();

    orderItem findOrderItemById(Long id);

    List<orderItem> findOrderByTiem(String startTime, String endTime);

    List<TbOrderItem> findOrderItemByUserIdAndDate(String start, String end, String userId);
}
