package com.zyy.pinyougou.sellergoods.service;
import com.zyy.pinyougou.pojo.TbBrand;

import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.core.service.CoreService;

import java.util.List;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface BrandService extends CoreService<TbBrand> {
	
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize);
	
	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize, TbBrand Brand);

	void add(List<TbBrand> brandList);

//	品牌审核
    void updateStatus(String status, Long[] ids);


}
