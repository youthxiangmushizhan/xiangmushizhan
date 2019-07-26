package com.zyy.pinyougou.user.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.pojo.TbUser;
import com.zyy.pinyougou.user.service.UserService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author: Zyy
 * @date: 2019-07-08 10:11
 * @description:
 * @version:
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TbUser oneByUserName = userService.findOneByUserName(username);

        String status = oneByUserName.getStatus();

        if ("1".equals(status)) {
            oneByUserName.setUsername(oneByUserName.getUsername()+"1");
            userService.updateByPrimaryKey(oneByUserName);
        }


        return new User(username, "", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
