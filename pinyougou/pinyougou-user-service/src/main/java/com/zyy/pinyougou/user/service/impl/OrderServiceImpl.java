package com.zyy.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.core.service.CoreServiceImpl;
import com.zyy.pinyougou.mapper.TbItemMapper;
import com.zyy.pinyougou.mapper.TbOrderItemMapper;
import com.zyy.pinyougou.mapper.TbOrderMapper;
import com.zyy.pinyougou.pojo.TbItem;
import com.zyy.pinyougou.pojo.TbOrder;
import com.zyy.pinyougou.pojo.TbOrderItem;
import com.zyy.pinyougou.user.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderServiceImpl extends CoreServiceImpl<TbOrder> implements OrderService {

	
	private TbOrderMapper orderMapper;

	@Autowired
	public OrderServiceImpl(TbOrderMapper orderMapper) {
		super(orderMapper, TbOrder.class);
		this.orderMapper=orderMapper;
	}

	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Autowired
	private TbItemMapper itemMapper;

	
	@Override
    public PageInfo<TbOrder> findPage(Integer pageNo, Integer pageSize, String userId) {
        PageHelper.startPage(pageNo,pageSize);
        //List<TbOrder> all = orderMapper.selectAll();
		//查询该用户的所有订单，并进行时间排序
		List<TbOrder> all = getMyOrders(userId,null);
		PageInfo<TbOrder> info = new PageInfo<TbOrder>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        //PageInfo<MyOrder> pageInfo = JSON.parseObject(s, PageInfo.class);
        PageInfo<TbOrder> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }

	public List<TbOrder> getMyOrders(String userId,String status) {
		Example example = new Example(TbOrder.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		//查询所有订单如果等于0
		if ("1".equals(status)) {
			//查询未付款状态的订单,如果创建时间超过时间自动取消订单，关闭支付接口
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MINUTE, -30);
			Date time = calendar.getTime();
			criteria.andEqualTo("status", status);
			criteria.andGreaterThan("createTime",time);

		}
		// todo  其他状态条件添加
		List<TbOrder> orderList = orderMapper.selectByExample(example);

		List<TbOrder> findOrderList = new ArrayList<>();

		for (int i = 0; i < orderList.size(); i++) {
			Long orderId = orderList.get(i).getOrderId();
			Example example1 = new Example(TbOrderItem.class);
			example1.createCriteria().andEqualTo("orderId", orderId);
			List<TbOrderItem> orderItemList = orderItemMapper.selectByExample(example1);

			//获取下订单中的每个商品信息
			if (orderItemList == null || orderItemList.size() == 0) {
				//orderItemList.remove(i);
				System.out.println("no add");
			} else {
				for (TbOrderItem orderItem : orderItemList) {
					Long itemId = orderItem.getItemId();
					Example example2 = new Example(TbItem.class);
					example2.createCriteria().andEqualTo("id", itemId);
					List<TbItem> itemList = itemMapper.selectByExample(example2);
					orderList.get(i).setOrderItemList(orderItemList);
					if (itemList != null && itemList.size() > 0) {
						orderItem.setItem(itemList.get(0));
						orderList.get(i).setOrderItemList(orderItemList);
						findOrderList.add(orderList.get(i));
					} else {
						//orderItemList.remove(i);
						//break;
						System.out.println("no add");
					}
				}
			}
			//通过每个订单获得每个订单的详情

			//orderList.get(i).setOrderItemList(orderItemList);
			//添加到我的订单中
		}

		//对我的订单进行排序
		findOrderList.sort(new Comparator<TbOrder>() {
			@Override
			public int compare(TbOrder o1, TbOrder o2) {
				return o2.getCreateTime().compareTo(o1.getCreateTime());
			}
		});
		return findOrderList;
	}


	@Override
    public PageInfo<TbOrder> findPage(Integer pageNo, Integer pageSize, TbOrder order) {
        PageHelper.startPage(pageNo,pageSize);

        Example example = new Example(TbOrder.class);
        Example.Criteria criteria = example.createCriteria();

        if(order!=null){			
						if(StringUtils.isNotBlank(order.getPaymentType())){
				criteria.andLike("paymentType","%"+order.getPaymentType()+"%");
				//criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(StringUtils.isNotBlank(order.getPostFee())){
				criteria.andLike("postFee","%"+order.getPostFee()+"%");
				//criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(StringUtils.isNotBlank(order.getStatus())){
				criteria.andLike("status","%"+order.getStatus()+"%");
				//criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(StringUtils.isNotBlank(order.getShippingName())){
				criteria.andLike("shippingName","%"+order.getShippingName()+"%");
				//criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(StringUtils.isNotBlank(order.getShippingCode())){
				criteria.andLike("shippingCode","%"+order.getShippingCode()+"%");
				//criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(StringUtils.isNotBlank(order.getUserId())){
				criteria.andLike("userId","%"+order.getUserId()+"%");
				//criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(StringUtils.isNotBlank(order.getBuyerMessage())){
				criteria.andLike("buyerMessage","%"+order.getBuyerMessage()+"%");
				//criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(StringUtils.isNotBlank(order.getBuyerNick())){
				criteria.andLike("buyerNick","%"+order.getBuyerNick()+"%");
				//criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(StringUtils.isNotBlank(order.getBuyerRate())){
				criteria.andLike("buyerRate","%"+order.getBuyerRate()+"%");
				//criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(StringUtils.isNotBlank(order.getReceiverAreaName())){
				criteria.andLike("receiverAreaName","%"+order.getReceiverAreaName()+"%");
				//criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(StringUtils.isNotBlank(order.getReceiverMobile())){
				criteria.andLike("receiverMobile","%"+order.getReceiverMobile()+"%");
				//criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(StringUtils.isNotBlank(order.getReceiverZipCode())){
				criteria.andLike("receiverZipCode","%"+order.getReceiverZipCode()+"%");
				//criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(StringUtils.isNotBlank(order.getReceiver())){
				criteria.andLike("receiver","%"+order.getReceiver()+"%");
				//criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(StringUtils.isNotBlank(order.getInvoiceType())){
				criteria.andLike("invoiceType","%"+order.getInvoiceType()+"%");
				//criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(StringUtils.isNotBlank(order.getSourceType())){
				criteria.andLike("sourceType","%"+order.getSourceType()+"%");
				//criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(StringUtils.isNotBlank(order.getSellerId())){
				criteria.andLike("sellerId","%"+order.getSellerId()+"%");
				//criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
        List<TbOrder> all = orderMapper.selectByExample(example);
        PageInfo<TbOrder> info = new PageInfo<TbOrder>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbOrder> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }
	
}
