package com.zyy.pinyougou.shop.securiyt;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.pojo.TbSeller;
import com.zyy.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author: Zyy
 * @date: 2019-06-23 16:04
 * @description:
 * @version:
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TbSeller tbSeller = sellerService.findOne(username);

        if (tbSeller == null) {
            //账号不存在
         throw new RuntimeException("账号不存在");
        }

        if (!"1".equals(tbSeller.getStatus())) {
            //未审核或审核未通过
           throw new RuntimeException("审核未通过");
        }



        return new User(username, tbSeller.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_SELLER"));
    }
}
