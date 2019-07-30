package com.zyy.test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.pojo.TbBrand;
import com.zyy.pinyougou.sellergoods.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WebTest {

    /*@Test
    public void test() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH,3);
        Date future = calendar.getTime();
        System.out.println(future.compareTo(now));
    }*/
}
