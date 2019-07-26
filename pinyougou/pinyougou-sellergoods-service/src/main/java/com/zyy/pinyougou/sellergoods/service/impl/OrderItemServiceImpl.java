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
import com.zyy.pinyougou.pojo.TbOrderItem;
import com.zyy.pinyougou.pojo.TbPayLog;
import com.zyy.pinyougou.sellergoods.service.OrderItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
            orderItem orderItem = new orderItem();
            TbOrderItem tbOrderItem = new TbOrderItem();
            tbOrderItem.setGoodsId(aLong);
            List<TbOrderItem> select = orderItemMapper.select(tbOrderItem);

            for (TbOrderItem item : select) {
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
        String s = JSON.toJSONString(orderItemList);
        List<orderItem> pageInfo = JSON.parseArray(s, orderItem.class);
        return pageInfo;
    }
}
