package com.zyy.pinyougou.user.service;

import java.util.List;


import com.zyy.pinyougou.pojo.TbGoods;
import com.zyy.pinyougou.pojo.TbItem;
import com.zyy.pinyougou.pojo.TbUser;

import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.core.service.CoreService;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface UserService extends CoreService<TbUser> {


    /**
     * 返回分页列表
     *
     * @return
     */
    PageInfo<TbUser> findPage(Integer pageNo, Integer pageSize);


    /**
     * 分页
     *
     * @param pageNo   当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageInfo<TbUser> findPage(Integer pageNo, Integer pageSize, TbUser User);

	void createSmsCode(String phone);

	boolean checkSmsCode(String phone,String code);

	TbUser findUserByUsername(String username);

    void addFootmark(Long itemId, String username);

	List<TbItem> getFootmark(String username);
    boolean checkSmsCode(String phone, String code);

    TbUser findOneByUserName(String username);
}
