package com.zyy.pinyougou.sellergoods.service;
import java.util.Date;
import java.util.List;

import com.zyy.pinyougou.pojo.*;

import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.core.service.CoreService;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService extends CoreService<TbGoods> {
	
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize);
	
	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize, TbGoods Goods);

	void add(Goods goods);

	Goods findOne(Long id);

	void update(Goods goods);

	void updateStatus(Long[] ids, String statusId);

	void delete(Long[] ids);

	List<TbItem> findTbItemListByIds(Long[] ids);

    PageInfo<GoodsStatistical> queryGoodsStatistical(Integer pageNo,Integer pageSize,String userName, Date startDate, Date endDate);

	List<TbBrand> getBrandList();

}
