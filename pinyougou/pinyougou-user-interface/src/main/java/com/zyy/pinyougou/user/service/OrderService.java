package com.zyy.pinyougou.user.service;

import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.core.service.CoreService;
import com.zyy.pinyougou.pojo.TbOrder;

import java.util.List;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService extends CoreService<TbOrder> {
	
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageInfo<TbOrder> findPage(Integer pageNo, Integer pageSize, String userId);
	
	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbOrder> findPage(Integer pageNo, Integer pageSize, TbOrder Order);

	List<TbOrder> getMyOrders(String userId, String status);
}
