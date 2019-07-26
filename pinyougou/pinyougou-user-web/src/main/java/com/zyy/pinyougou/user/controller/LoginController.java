package com.zyy.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.pojo.TbUser;
import com.zyy.pinyougou.user.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: Zyy
 * @date: 2019-07-08 10:24
 * @description:
 * @version:
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private UserService userService;

    @RequestMapping("/getUsername")
    public String getUsername() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        TbUser tbUser = new TbUser();

        tbUser.setUsername(name);

        TbUser tbUser1 = userService.selectOne(tbUser);
        tbUser1.setLastLoginTime(new Date());

      userService.updateByPrimaryKey(tbUser1);

        return name;
    }


}
