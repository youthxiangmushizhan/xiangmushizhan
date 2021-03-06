package com.zyy.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.entity.orderItem;
import com.zyy.pinyougou.pojo.TbOrderItem;
import com.zyy.pinyougou.sellergoods.service.OrderItemService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/orderItem")
public class OrderItemController {

    @Reference
    private OrderItemService orderItemService;

    /**
     * 返回全部列表
     *
     * @return
     */


    @RequestMapping("/findOrderItem")
    public List<orderItem> findOrderItem() {
        return orderItemService.findOrderItem();
    }


    @RequestMapping("/findAll")
    public List<TbOrderItem> findAll() {
        return orderItemService.findAll();
    }


    @RequestMapping("/findPage")
    public PageInfo<TbOrderItem> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return orderItemService.findPage(pageNo, pageSize);
    }


    @RequestMapping("/search")
    public PageInfo<TbOrderItem> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize, @RequestBody TbOrderItem orderItem) {


        return orderItemService.findPage(pageNo, pageSize, orderItem);
    }


    /**
     * 增加
     *
     * @param orderItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbOrderItem orderItem) {
        try {
            orderItemService.add(orderItem);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param orderItem
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbOrderItem orderItem) {
        try {
            orderItemService.update(orderItem);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public orderItem findOne(@PathVariable(value = "id") Long id) {
        return orderItemService.findOrderItemById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids) {
        try {
            orderItemService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }


    @RequestMapping("/findOrderByTiem")
    public List<orderItem> findOrderByTiem(String startTime, String endTime) {
        System.out.println(startTime + "  <><><><>   " + endTime);

        return orderItemService.findOrderByTiem(startTime, endTime);
    }

    @RequestMapping("/findOrderItem")
    public List<TbOrderItem> findOrderItem(@RequestParam(value = "start", defaultValue = "", required = true) String start,
                                           @RequestParam(value = "end", defaultValue = "", required = true) String end,
                                           @RequestParam String userId) {
        return orderItemService.findOrderItemByUserIdAndDate(start,end,userId);
    }

}
