package com.zyy.pinyougou.sellergoods.service.impl;
import java.util.List;

import com.zyy.pinyougou.mapper.TbGoodsMapper;
import com.zyy.pinyougou.mapper.TbItemMapper;
import com.zyy.pinyougou.pojo.TbGoods;
import com.zyy.pinyougou.pojo.TbItem;
import com.zyy.pinyougou.xinzhen.Shangpin;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo; 									  
import org.apache.commons.lang3.StringUtils;
import com.zyy.pinyougou.core.service.CoreServiceImpl;

import tk.mybatis.mapper.entity.Example;

import com.zyy.pinyougou.mapper.TbSeckillGoodsMapper;
import com.zyy.pinyougou.pojo.TbSeckillGoods;

import com.zyy.pinyougou.sellergoods.service.SeckillGoodsService;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SeckillGoodsServiceImpl extends CoreServiceImpl<TbSeckillGoods>  implements SeckillGoodsService {

	private TbItemMapper itemMapper;

	private TbGoodsMapper goodsMapper;

	private TbSeckillGoodsMapper seckillGoodsMapper;

	@Autowired
	public SeckillGoodsServiceImpl(TbSeckillGoodsMapper seckillGoodsMapper) {
		super(seckillGoodsMapper, TbSeckillGoods.class);
		this.seckillGoodsMapper=seckillGoodsMapper;
	}

	
	

	
	@Override
	public PageInfo<TbSeckillGoods> findPage(Integer pageNo, Integer pageSize) {
		PageHelper.startPage(pageNo,pageSize);
		List<TbSeckillGoods> all = seckillGoodsMapper.selectAll();
		PageInfo<TbSeckillGoods> info = new PageInfo<TbSeckillGoods>(all);

		//序列化再反序列化
		String s = JSON.toJSONString(info);
		PageInfo<TbSeckillGoods> pageInfo = JSON.parseObject(s, PageInfo.class);
		return pageInfo;
	}


	@Override
	public PageInfo<TbSeckillGoods> findPage(Integer pageNo, Integer pageSize, TbSeckillGoods SeckillGoods) {
		return null;
	}



	@Override
    public PageInfo<TbSeckillGoods> findPage(Integer pageNo, Integer pageSize, TbSeckillGoods seckillGoods, String sellerId) {
        PageHelper.startPage(pageNo,pageSize);

        Example example = new Example(TbSeckillGoods.class);
        Example.Criteria criteria = example.createCriteria();

		 criteria.andEqualTo("sellerId",sellerId);


		 if(seckillGoods!=null){
						if(StringUtils.isNotBlank(seckillGoods.getTitle())){
				criteria.andLike("title","%"+seckillGoods.getTitle()+"%");
				//criteria.andTitleLike("%"+seckillGoods.getTitle()+"%");
			}
			if(StringUtils.isNotBlank(seckillGoods.getSmallPic())){
				criteria.andLike("smallPic","%"+seckillGoods.getSmallPic()+"%");
				//criteria.andSmallPicLike("%"+seckillGoods.getSmallPic()+"%");
			}
			/*if(StringUtils.isNotBlank(seckillGoods.getSellerId())){
				//criteria.andLike("sellerId","%"+seckillGoods.getSellerId()+"%");
				criteria.andEqualTo("sellerId",seckillGoods.getSellerId());
				//criteria.andSellerIdLike("%"+seckillGoods.getSellerId()+"%");
			}*/
			if(StringUtils.isNotBlank(seckillGoods.getStatus())){
				criteria.andLike("status","%"+seckillGoods.getStatus()+"%");
				//criteria.andStatusLike("%"+seckillGoods.getStatus()+"%");
			}
			if(StringUtils.isNotBlank(seckillGoods.getIntroduction())){
				criteria.andLike("introduction","%"+seckillGoods.getIntroduction()+"%");
				//criteria.andIntroductionLike("%"+seckillGoods.getIntroduction()+"%");
			}
			if(seckillGoods.getCostPrice()!=null){
				criteria.andEqualTo("costPrice",seckillGoods.getCostPrice().doubleValue()+"");
			}
	
		}
        List<TbSeckillGoods> all = seckillGoodsMapper.selectByExample(example);
        PageInfo<TbSeckillGoods> info = new PageInfo<TbSeckillGoods>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbSeckillGoods> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }

    @Override
    public List<TbSeckillGoods> findSellerGoods(String sellerId) {
		Example example = new Example(TbSeckillGoods.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("sellerId", sellerId);
		List<TbSeckillGoods> list = seckillGoodsMapper.selectByExample(example);
		return list;
	}

	@Override
	public void addSeckillGoods(Shangpin shangpin) {
		TbGoods tbGoods = shangpin.getTbGoods();
		TbSeckillGoods tbSeckillGoods = shangpin.getTbSeckillGoods();
		tbSeckillGoods.setSellerId(tbGoods.getSellerId());
		tbSeckillGoods.setGoodsId(tbGoods.getId());
		tbSeckillGoods.setTitle(tbGoods.getGoodsName());
		tbSeckillGoods.setSmallPic(tbGoods.getSmallPic());
		tbSeckillGoods.setPrice(tbGoods.getPrice());
		tbSeckillGoods.setStatus("0");
		seckillGoodsMapper.insert(tbSeckillGoods);
	}
}
