package com.zyy.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.common.pojo.MessageInfo;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.pojo.TbGoods;
import com.zyy.pinyougou.pojo.TbSeckillGoods;
import com.zyy.pinyougou.sellergoods.service.SeckillGoodsService;
import com.zyy.pinyougou.xinzhen.Shangpin;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

	@Reference
	private SeckillGoodsService seckillGoodsService;

	@Autowired
	private DefaultMQProducer mqProducer;

	@RequestMapping("/change")
	public Result change(@RequestBody Shangpin shangpin) {
		System.out.println(shangpin.getTbGoods().getGoodsName());
		System.out.println(shangpin.getTbSeckillGoods().getCostPrice());
		try {
			seckillGoodsService.addSeckillGoods(shangpin);
			return new Result(true,"申请成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"申请失败");
		}

	}

	@RequestMapping("/updateStatus")
	public Result updateStatus(@RequestBody Long[] ids,String status) {
		try {
			for (Long id : ids) {
				TbSeckillGoods goods = new TbSeckillGoods();
				goods.setId(id);
				goods.setStatus(status);
				seckillGoodsService.updateByPrimaryKeySelective(goods);
			}

			MessageInfo messageInfo = new MessageInfo(ids,"TOPIC_SECKILL", "Tags_genHTML", "seckillGoods_updateStatus",  MessageInfo.METHOD_ADD);
			mqProducer.send(new Message(messageInfo.getTopic(),messageInfo.getTags(),messageInfo.getKeys(), JSON.toJSONString(messageInfo).getBytes(RemotingHelper.DEFAULT_CHARSET)));

			return new Result(true,"成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"失败");
		}
	}
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeckillGoods> findAll(){
		return seckillGoodsService.findAll();
	}
	
	
	
	@RequestMapping("/findPage")
    public PageInfo<TbSeckillGoods> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
											 @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return seckillGoodsService.findPage(pageNo, pageSize);
    }
	
	/**
	 * 增加
	 * @param seckillGoods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbSeckillGoods seckillGoods){
		try {
			//seckillGoods.setStatus("0");
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			seckillGoods.setSellerId(name);
			seckillGoodsService.add(seckillGoods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seckillGoods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbSeckillGoods seckillGoods){
		try {
			seckillGoodsService.update(seckillGoods);
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
	public TbSeckillGoods findOne(@PathVariable(value = "id") Long id){
		return seckillGoodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(@RequestBody Long[] ids){
		try {
			seckillGoodsService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	

	@RequestMapping("/search")
    public PageInfo<TbSeckillGoods> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
                                      @RequestBody TbSeckillGoods seckillGoods) {
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//seckillGoods.setSellerId(sellerId);
		return seckillGoodsService.findPage(pageNo, pageSize, seckillGoods,sellerId);
    }

    @RequestMapping("/findSellerGoods")
	public List<TbSeckillGoods> findSellerGoods() {
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		List<TbSeckillGoods> list = seckillGoodsService.findSellerGoods(sellerId);
		return list;
	}
	
}
