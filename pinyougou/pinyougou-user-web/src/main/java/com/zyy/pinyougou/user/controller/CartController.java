package com.zyy.pinyougou.user.controller;
/*
 *  @项目名：  pinyougou
 *  @包名：    com.zyy.pinyougou.user.controller
 *  @文件名:   CartContro
 *  @创建者:   魏铭佳牛逼
 *  @创建时间:  2019/7/26 21:00
 *  @描述：    TODO
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.cart.service.CartService;
import com.zyy.pinyougou.entity.Cart;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.pojo.TbOrderItem;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @RequestMapping("/findmyfollow")
    public List<TbOrderItem> findmyfollow(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<TbOrderItem> myfollowList=cartService.findmyfollow(username);

        return myfollowList;

    }

    @RequestMapping("/addcartList/{itemId}")
    public Result addcartList(@PathVariable("itemId") Long itemId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            List<Cart> cartListFromRedis = cartService.findCartListFromRedis(username);
            List<Cart> cartListNew = cartService.addGoodsToCartList(cartListFromRedis, itemId, 1);
            cartService.saveCartListToRedis(username,cartListNew);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }

    }

}
