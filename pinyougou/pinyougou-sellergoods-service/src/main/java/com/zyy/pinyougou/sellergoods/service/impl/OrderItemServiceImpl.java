package com.zyy.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.core.service.CoreServiceImpl;
import com.zyy.pinyougou.entity.orderItem;
import com.zyy.pinyougou.mapper.TbGoodsMapper;
import com.zyy.pinyougou.mapper.TbItemMapper;
import com.zyy.pinyougou.mapper.TbOrderItemMapper;
import com.zyy.pinyougou.mapper.TbOrderMapper;
import com.zyy.pinyougou.pojo.TbGoods;
import com.zyy.pinyougou.pojo.TbOrder;
import com.zyy.pinyougou.pojo.TbOrderItem;
import com.zyy.pinyougou.pojo.TbPayLog;
import com.zyy.pinyougou.sellergoods.service.OrderItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import javax.management.OperationsException;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class OrderItemServiceImpl extends CoreServiceImpl<com.zyy.pinyougou.pojo.TbOrderItem> implements OrderItemService {


    private TbOrderItemMapper orderItemMapper;

    @Autowired
    public OrderItemServiceImpl(TbOrderItemMapper orderItemMapper) {
        super(orderItemMapper, com.zyy.pinyougou.pojo.TbOrderItem.class);
        this.orderItemMapper = orderItemMapper;
    }


    @Override
    public PageInfo<com.zyy.pinyougou.pojo.TbOrderItem> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<TbOrderItem> all = orderItemMapper.selectAll();
        PageInfo<TbOrderItem> info = new PageInfo<com.zyy.pinyougou.pojo.TbOrderItem>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbOrderItem> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }


    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbGoodsMapper goodsMapper;


    @Autowired
    private TbOrderMapper orderMapper;

    @Override
    public PageInfo<TbOrderItem> findPage(Integer pageNo, Integer pageSize, TbOrderItem orderItem) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(TbOrderItem.class);
        Example.Criteria criteria = example.createCriteria();

        if (orderItem != null) {
            if (StringUtils.isNotBlank(orderItem.getTitle())) {
                criteria.andLike("title", "%" + orderItem.getTitle() + "%");
                //criteria.andTitleLike("%"+orderItem.getTitle()+"%");
            }
            if (StringUtils.isNotBlank(orderItem.getPicPath())) {
                criteria.andLike("picPath", "%" + orderItem.getPicPath() + "%");
                //criteria.andPicPathLike("%"+orderItem.getPicPath()+"%");
            }
            if (StringUtils.isNotBlank(orderItem.getSellerId())) {
                criteria.andLike("sellerId", "%" + orderItem.getSellerId() + "%");
                //criteria.andSellerIdLike("%"+orderItem.getSellerId()+"%");
            }

        }
        List<TbOrderItem> all = orderItemMapper.selectByExample(example);
        PageInfo<TbOrderItem> info = new PageInfo<TbOrderItem>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbOrderItem> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }

    @Override
    public List<orderItem> findOrderItem() {

        List<TbOrderItem> tbOrderItems = orderItemMapper.selectAll();

        List<orderItem> orderItemList = new ArrayList<>();

        Set<Long> goodsId = new HashSet<>();


        for (TbOrderItem tbOrderItem : tbOrderItems) {
            goodsId.add(tbOrderItem.getGoodsId());

        }


        int totalNum = 0;
        Double totalMoney = 0.0;
        Long ItemId = 0L;
        String title = "";
        String picPath = "";
        String price = "";


        for (Long aLong : goodsId) {
            List<TbOrder> orderList = new ArrayList<>();

            orderItem orderItem = new orderItem();
            TbOrderItem tbOrderItem = new TbOrderItem();
            tbOrderItem.setGoodsId(aLong);
            List<TbOrderItem> select = orderItemMapper.select(tbOrderItem);

            for (TbOrderItem item : select) {

                orderList.add(orderMapper.selectByPrimaryKey(item.getOrderId()));
                ItemId = item.getItemId();
                totalNum += item.getNum();
                totalMoney += Double.parseDouble(item.getTotalFee() + "");
                title = item.getTitle();
                picPath = item.getPicPath();
                price = item.getPrice() + "";

            }

            orderItem.setGoods(goodsMapper.selectByPrimaryKey(aLong));
            orderItem.setTbItem(itemMapper.selectByPrimaryKey(ItemId));
            orderItem.setTotalNum(totalNum);
            orderItem.setOrderList(orderList);


            BigDecimal bigDecimal = new BigDecimal(totalNum);

            BigDecimal bigDecimal1 = new BigDecimal(price);


            orderItem.setTotalMoney(bigDecimal.multiply(bigDecimal1));
            orderItem.setPrice(price);
            orderItem.setTitle(title);
            orderItem.setPicPath(picPath);

            orderItemList.add(orderItem);


            totalNum = 0;
            totalMoney = 0.0;
            ItemId = 0L;

        }


        return orderItemList;
    }

    @Override
    public orderItem findOrderItemById(Long id) {

        orderItem orderItems = new orderItem();
        List<TbOrder> orderList = new ArrayList<>();

        TbOrderItem tbOrderItem = new TbOrderItem();
        tbOrderItem.setGoodsId(id);
        List<TbOrderItem> select = orderItemMapper.select(tbOrderItem);


        int totalNum = 0;
        Double totalMoney = 0.0;
        String title = "";
        String picPath = "";
        String price = "";

        for (TbOrderItem orderItem : select) {
            orderList.add(orderMapper.selectByPrimaryKey(orderItem.getOrderId() + ""));
            orderItems.setGoods(goodsMapper.selectByPrimaryKey(orderItem.getGoodsId()));
            orderItems.setTbItem(itemMapper.selectByPrimaryKey(orderItem.getItemId()));

            totalNum += orderItem.getNum();
            totalMoney += Double.parseDouble(orderItem.getTotalFee() + "");
            title = orderItem.getTitle();
            picPath = orderItem.getPicPath();
            price = orderItem.getPrice() + "";
        }

        BigDecimal bigDecimal = new BigDecimal(totalNum);

        BigDecimal bigDecimal1 = new BigDecimal(price);

        orderItems.setTotalNum(totalNum);
        orderItems.setTotalMoney(bigDecimal.multiply(bigDecimal1));
        orderItems.setPrice(price);
        orderItems.setTitle(title);
        orderItems.setPicPath(picPath);
        orderItems.setOrderList(orderList);
        return orderItems;
    }

    @Override
    public List<orderItem> findOrderByTiem(String startTime, String endTime) {


        List<orderItem> orderItemList = new ArrayList<>();


        Example example = new Example(TbOrder.class);
        Example.Criteria criteria = example.createCriteria();


        if (StringUtils.isNotBlank(startTime)) {
            criteria.andGreaterThan("createTime", startTime);
        }

        if (StringUtils.isNotBlank(endTime)) {
            criteria.andLessThan("createTime", endTime);
        }


        List<TbOrder> orderList = orderMapper.selectByExample(example);


        List<String> orderId = new ArrayList<>();
        for (TbOrder tbOrder : orderList) {
            orderId.add(String.valueOf(tbOrder.getOrderId()));
        }

        TbOrderItem tbOrderItem = new TbOrderItem();


        Set<Long> setItemId = new HashSet<>();
        for (String s : orderId) {
            tbOrderItem.setOrderId(Long.parseLong(s));
            List<TbOrderItem> select = orderItemMapper.select(tbOrderItem);
            for (TbOrderItem item : select) {
                setItemId.add(item.getItemId());
            }
        }


        int totalNum = 0;
        Double totalMoney = 0.0;
        Long goodsId = 0L;
        String title = "";
        String picPath = "";
        String price = "";


        for (Long aLong : setItemId) {

            orderItem orderItems = new orderItem();

            TbOrderItem tbOrderItem1 = new TbOrderItem();

            tbOrderItem1.setItemId(aLong);
            List<TbOrderItem> orderItems1 = orderItemMapper.select(tbOrderItem1);

            for (TbOrderItem orderItem : orderItems1) {

                goodsId = orderItem.getGoodsId();
                totalNum += orderItem.getNum();
                totalMoney += Double.parseDouble(orderItem.getTotalFee() + "");
                title = orderItem.getTitle();
                picPath = orderItem.getPicPath();
                price = orderItem.getPrice() + "";
            }


            orderItems.setTbItem(itemMapper.selectByPrimaryKey(aLong));
            orderItems.setGoods(goodsMapper.selectByPrimaryKey(goodsId));


            orderItems.setTotalNum(totalNum);

            BigDecimal bigDecimal = new BigDecimal(totalNum);

            BigDecimal bigDecimal1 = new BigDecimal(price);


            orderItems.setTotalMoney(bigDecimal.multiply(bigDecimal1));

            orderItems.setPrice(price);

            orderItems.setTitle(title);

            orderItems.setPicPath(picPath);

            orderItemList.add(orderItems);

            totalNum = 0;
            totalMoney = 0.0;
            goodsId = 0L;
        }


        return orderItemList;
    }

    @Override
    public List<TbOrderItem> findOrderItemByUserIdAndDate(String start, String end, String userId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dateFormat.parse(start);
            Date date2 = dateFormat.parse(end);
            return orderItemMapper.findByUserIdAndDate(date1,date2,userId);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<TbOrderItem>();
        }
    }
}
