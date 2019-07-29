package com.zyy.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.common.pojo.MessageInfo;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.pojo.*;
import com.zyy.pinyougou.sellergoods.service.GoodsService;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	@Autowired
	private DefaultMQProducer mqProducer;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){
		return goodsService.findAll();
	}
	
	
	
	@RequestMapping("/findPage")
    public PageInfo<TbGoods> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
									  @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return goodsService.findPage(pageNo, pageSize);
    }
	
	/**
	 * 增加
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(username);
		goods.getTbGoods().setSellerId(username);
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne/{id}")
	public Goods findOne(@PathVariable(value = "id") Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(@RequestBody Long[] ids){
		try {
			goodsService.delete(ids);
			MessageInfo messageInfo = new MessageInfo(ids, "Goods_Topic", "goods_delete_tag", "delete", MessageInfo.METHOD_DELETE);
			SendResult sendResult = mqProducer.send(new Message(messageInfo.getTopic(), messageInfo.getTags(), messageInfo.getKeys(), JSON.toJSONString(messageInfo).getBytes()));

			System.out.println(">>>>" + sendResult.getSendStatus());
			//itemSearchService.deleteByIds(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	

	@RequestMapping("/search")
    public PageInfo<TbGoods> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
                                      @RequestBody TbGoods goods) {
		goods.setSellerId(SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication().getName());
        return goodsService.findPage(pageNo, pageSize, goods);
    }

    @RequestMapping("/queryGoodsStatistical")
	public PageInfo<GoodsStatistical> queryGoodsStatistical(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
															@RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
															@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
															@DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("1111111111");
		return goodsService.queryGoodsStatistical(pageNo,pageSize,userName,startDate,endDate);
	}

	@RequestMapping("/getBrandList")
	public List<TbBrand> getBrandList(){
		return goodsService.getBrandList();
	}

	@RequestMapping("/upperShelf")
	public Result upperShelf(Long id) {
		Result result = new Result();
		//修改数据库数据
		try {
			goodsService.upperShelf(id);
			//发送生成页面和增加索引库数据的消息
			Long[] ids = new Long[1];
			ids[0] = id;
			List<TbItem> tbItemList = goodsService.findTbItemListByIds(ids);
			MessageInfo messageInfo = new MessageInfo(tbItemList, "Goods_Topic", "goods_update_tag", "updateStatus", MessageInfo.METHOD_UPDATE);
			SendResult sendResult = mqProducer.send(new Message(messageInfo.getTopic(), messageInfo.getTags(), messageInfo.getKeys(), JSON.toJSONString(messageInfo).getBytes()));
			System.out.println(">>>>" + sendResult.getSendStatus());
			result.setSuccess(true);
			result.setMessage("上架成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("上架成功");
		}

		return result;
	}

	@RequestMapping("/offShelfGood")
	public Result offShelfGood(Long id){
		Result result = new Result();
		try {
			//修改数据库数据
			goodsService.offShelfGood(id);
			//发送下架消息
			Long[] ids = new Long[1];
			ids[0] = id;
			MessageInfo messageInfo = new MessageInfo(ids, "Goods_Topic", "goods_delete_tag", "delete", MessageInfo.METHOD_DELETE);
			SendResult sendResult = mqProducer.send(new Message(messageInfo.getTopic(), messageInfo.getTags(), messageInfo.getKeys(), JSON.toJSONString(messageInfo).getBytes()));
			System.out.println(">>>>" + sendResult.getSendStatus());
			result.setMessage("下架成功");
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("下架失败");
			result.setSuccess(false);
		}
		return result;
	}
	
}
