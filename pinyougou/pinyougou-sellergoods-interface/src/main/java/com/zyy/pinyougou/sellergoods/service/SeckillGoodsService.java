package com.zyy.pinyougou.sellergoods.service;
import java.util.List;
import com.zyy.pinyougou.pojo.TbSeckillGoods;

import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.core.service.CoreService;
import com.zyy.pinyougou.newPOJO.Shangpin;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SeckillGoodsService extends CoreService<TbSeckillGoods> {


	PageInfo<TbSeckillGoods> findPage(Integer pageNo, Integer pageSize, TbSeckillGoods SeckillGoods);

	PageInfo<TbSeckillGoods> findPage(Integer pageNo, Integer pageSize);

	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbSeckillGoods> findPage(Integer pageNo, Integer pageSize, TbSeckillGoods SeckillGoods,String sellerId);

    List<TbSeckillGoods> findSellerGoods(String sellerId);

	void addSeckillGoods(Shangpin shangpin);
}
