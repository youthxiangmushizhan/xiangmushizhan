package com.zyy.pinyougou.sellergoods.service.impl;
import java.util.*;

import com.zyy.pinyougou.mapper.*;
import com.zyy.pinyougou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo; 									  
import org.apache.commons.lang3.StringUtils;
import com.zyy.pinyougou.core.service.CoreServiceImpl;

import tk.mybatis.mapper.entity.Example;

import com.zyy.pinyougou.sellergoods.service.GoodsService;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl extends CoreServiceImpl<TbGoods> implements GoodsService {

	
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Autowired
	public GoodsServiceImpl(TbGoodsMapper goodsMapper) {
		super(goodsMapper, TbGoods.class);
		this.goodsMapper=goodsMapper;
	}

	@Autowired
	private TbGoodsDescMapper tbGoodsDescMapper;

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbSellerMapper tbSellerMapper;

	@Autowired
	private TbBrandMapper tbBrandMapper;

	@Autowired
	private TbItemCatMapper tbItemCatMapper;

	@Override
    public PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<TbGoods> all = goodsMapper.selectAll();
        PageInfo<TbGoods> info = new PageInfo<TbGoods>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbGoods> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }

	
	

	 @Override
    public PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize, TbGoods goods) {
        PageHelper.startPage(pageNo,pageSize);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();

        if(goods!=null){			
			if(StringUtils.isNotBlank(goods.getSellerId())){
				criteria.andEqualTo("sellerId", goods.getSellerId());
				/*criteria.andLike("sellerId","%"+goods.getSellerId()+"%");*/
				//criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
			}
			if(StringUtils.isNotBlank(goods.getGoodsName())){
				criteria.andLike("goodsName","%"+goods.getGoodsName()+"%");
				//criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(StringUtils.isNotBlank(goods.getAuditStatus())){
				criteria.andLike("auditStatus","%"+goods.getAuditStatus()+"%");
				//criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(StringUtils.isNotBlank(goods.getIsMarketable())){
				criteria.andLike("isMarketable","%"+goods.getIsMarketable()+"%");
				//criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(StringUtils.isNotBlank(goods.getCaption())){
				criteria.andLike("caption","%"+goods.getCaption()+"%");
				//criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(StringUtils.isNotBlank(goods.getSmallPic())){
				criteria.andLike("smallPic","%"+goods.getSmallPic()+"%");
				//criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(StringUtils.isNotBlank(goods.getIsEnableSpec())){
				criteria.andLike("isEnableSpec","%"+goods.getIsEnableSpec()+"%");
				//criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
	
		}
        List<TbGoods> all = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> info = new PageInfo<TbGoods>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbGoods> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }

	@Override
	public void add(Goods goods) {
		TbGoods tbGoods = goods.getTbGoods();
		tbGoods.setAuditStatus("0");
		tbGoods.setIsDelete(false);
		goodsMapper.insert(tbGoods);

		TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
		tbGoodsDesc.setGoodsId(tbGoods.getId());
		tbGoodsDescMapper.insert(tbGoodsDesc);

		saveItems(tbGoods, goods, tbGoodsDesc);

	}

	@Override
	public Goods findOne(Long id) {
		Goods goods = new Goods();

		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(id);
		TbItem tbItem = new TbItem();
		tbItem.setGoodsId(id);
		List<TbItem> tbItemList = tbItemMapper.select(tbItem);

		goods.setTbGoods(tbGoods);
		goods.setTbGoodsDesc(tbGoodsDesc);
		goods.setTbItemList(tbItemList);
		return goods;
	}

	@Override
	public void update(Goods goods) {
		TbGoods tbGoods = goods.getTbGoods();
		tbGoods.setAuditStatus("0");
		goodsMapper.updateByPrimaryKey(tbGoods);
		//更新描述
		tbGoodsDescMapper.updateByPrimaryKey(goods.getTbGoodsDesc());
		//更新SKU  先删除原来的SPUid对应的SKU的列表
		TbItem record = new TbItem();
		record.setGoodsId(tbGoods.getId());
		tbItemMapper.delete(record);
		//新增就可以了 这里也要判断是否为启用的状态
		saveItems(tbGoods,goods,goods.getTbGoodsDesc());
	}

	@Override
	public void updateStatus(Long[] ids, String statusId) {
		TbGoods tbGoods = new TbGoods();
		tbGoods.setAuditStatus(statusId);

		Example example = new Example(TbGoods.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", Arrays.asList(ids));

		goodsMapper.updateByExampleSelective(tbGoods, example);
	}

	@Override
	public void delete(Long[] ids) {
		TbGoods tbGoods = new TbGoods();
		tbGoods.setIsDelete(true);

		Example example = new Example(TbGoods.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("isDelete", false);
		criteria.andIn("id", Arrays.asList(ids));

		goodsMapper.updateByExampleSelective(tbGoods, example);
	}

	@Override
	public List<TbItem> findTbItemListByIds(Long[] ids) {

		Example example = new Example(TbItem.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("goodsId", Arrays.asList(ids)).andEqualTo("status", 1);

		List<TbItem> tbItemList = tbItemMapper.selectByExample(example);

		return tbItemList;
	}

    @Override
    public PageInfo<GoodsStatistical> queryGoodsStatistical(Integer pageNo,Integer pageSize,String userName, Date startDate, Date endDate) {
//		获取商户所有的商品
		Example example = new Example(TbGoods.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("sellerId",userName);
		PageHelper.startPage(pageNo,pageSize);
		List<TbGoods> tbGoods = goodsMapper.selectByExample(example);
//		获取商户所有的订单项目
		Example orderExample = new Example(TbOrder.class);
		Example.Criteria orderCriteria = orderExample.createCriteria();
		orderCriteria.andEqualTo("sellerId",userName);
		orderCriteria.andLessThan("paymentTime",endDate);
		orderCriteria.andGreaterThan("paymentTime",startDate);
		orderCriteria.andEqualTo("status","2");
		List<TbOrder> tbOrders = orderMapper.selectByExample(orderExample);
		List<GoodsStatistical> result = new ArrayList<>();
		for (TbGoods tbGood : tbGoods) {
			Long id = tbGood.getId();
			Integer goodsNum = 0;
			for (TbOrder tbOrder : tbOrders) {
//				获取订单编号
				Long orderId = tbOrder.getOrderId();
//				获取该订单下所有的订单项
				Example orderItemExample = new Example(TbOrderItem.class);
				Example.Criteria orderItemCriteria = orderItemExample.createCriteria();
				orderItemCriteria.andEqualTo("orderId",orderId);
				List<TbOrderItem> tbOrderItems = orderItemMapper.selectByExample(orderItemExample);
				for (TbOrderItem tbOrderItem : tbOrderItems) {
					System.out.println(id + "-----------------------" + tbOrderItem.getGoodsId());
					if (tbOrderItem.getGoodsId().equals(id)){
						goodsNum++;
					}
				}
			}
			GoodsStatistical goodsStatistical = new GoodsStatistical();
			goodsStatistical.setTbGoods(tbGood);
			goodsStatistical.setStatistical(goodsNum);
			result.add(goodsStatistical);
		}
		PageInfo pageInfo = new PageInfo<>(tbGoods);
		pageInfo.setList(result);
		return pageInfo;
    }

	@Override
	public List<TbBrand> getBrandList() {
		return tbBrandMapper.selectAll();
	}

	private void saveItems(TbGoods tbGoods,Goods goods,TbGoodsDesc tbGoodsDesc) {
		if ("1".equals(tbGoods.getIsEnableSpec())) {
			List<TbItem> tbItemList = goods.getTbItemList();
			if (tbItemList != null && tbItemList.size() > 0) {
				for (TbItem tbItem : tbItemList) {
					//设置ID
					tbItem.setGoodsId(tbGoods.getId());
					//设置title  SPU名 + 空格+ 规格名称 +
					String spec = tbItem.getSpec();//{"网络":"移动4G","机身内存":"16G"}
					String title = tbGoods.getGoodsName();
					Map map = JSON.parseObject(spec, Map.class);
					for (Object key : map.keySet()) {
						String o1 = (String) map.get(key);
						title += " " + o1;
					}

					//设置分类
					TbItemCat itemCat = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
					tbItem.setCategory(itemCat.getName());
					tbItem.setCategoryid(itemCat.getId());

					//时间
					tbItem.setCreateTime(new Date());
					tbItem.setUpdateTime(new Date());

					//设置商家
					TbSeller seller = tbSellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
					tbItem.setSellerId(seller.getSellerId());
					tbItem.setSeller(seller.getNickName());

					//设置品牌
					TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(tbGoods.getBrandId());
					tbItem.setBrand(tbBrand.getName());

					//设置图片从goodsDesc中获取
					String itemImages = tbGoodsDesc.getItemImages();
					List<Map> maps = JSON.parseArray(itemImages, Map.class);
					for (Map imageMap : maps) {
						String color = (String) imageMap.get("color");
						title += " " + color;
						tbItem.setTitle(title);
						String url = (String) imageMap.get("url");
						tbItem.setImage(url);
						tbItemMapper.insert(tbItem);
					}

				}
			}
		} else {

			//插入到SKU表 一条记录
			TbItem tbItem = new TbItem();
			tbItem.setTitle(tbGoods.getGoodsName());
			tbItem.setPrice(tbGoods.getPrice());
			tbItem.setNum(999);//默认一个
			tbItem.setStatus("1");//正常启用
			tbItem.setIsDefault("1");//默认的

			tbItem.setSpec("{}");

			//设置分类
			TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
			tbItem.setCategoryid(tbItemCat.getId());
			tbItem.setCategory(tbItemCat.getName());

			//时间
			tbItem.setCreateTime(new Date());
			tbItem.setUpdateTime(new Date());

			//设置SPU的ID
			tbItem.setGoodsId(tbGoods.getId());

			//设置商家
			TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
			tbItem.setSellerId(tbSeller.getSellerId());
			tbItem.setSeller(tbSeller.getNickName());//店铺名

			//设置品牌明后
			TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(tbGoods.getBrandId());
			tbItem.setBrand(tbBrand.getName());
			tbItemMapper.insert(tbItem);

			//设置图片从goodsDesc中获取
			String itemImages = tbGoodsDesc.getItemImages();
			List<Map> maps = JSON.parseArray(itemImages, Map.class);
			for (Map imageMap : maps) {
				String color = (String) imageMap.get("color");
				String url = (String) imageMap.get("url");
				tbItem.setImage(url);
				tbItemMapper.insert(tbItem);
			}
		}

	}

}
