package com.zyy.pinyougou.cart.service;

import com.zyy.pinyougou.entity.Cart;
import com.zyy.pinyougou.pojo.TbOrderItem;

import java.util.List;

/**
 * @author: Zyy
 * @date: 2019-07-08 19:46
 * @description:
 * @version:
 */
public interface CartService {

    public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);

    public List<Cart> findCartListFromRedis(String username);

    public void saveCartListToRedis(String username,List<Cart> cartList);

    public List<Cart> mergeCartList(List<Cart> cookieList,List<Cart> redisList);

    void addmyfollow(Long itemId, String username);

    List<TbOrderItem> findmyfollow(String username);
}
