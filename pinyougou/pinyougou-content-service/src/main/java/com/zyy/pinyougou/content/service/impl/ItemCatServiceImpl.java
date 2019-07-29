package com.zyy.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.content.service.ItemCatService;
import com.zyy.pinyougou.core.service.CoreServiceImpl;
import com.zyy.pinyougou.mapper.TbItemCatMapper;
import com.zyy.pinyougou.pojo.TbItemCat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ItemCatServiceImpl extends CoreServiceImpl<TbItemCat>  implements ItemCatService {

	private TbItemCatMapper itemCatMapper;

	@Autowired
	public ItemCatServiceImpl(TbItemCatMapper itemCatMapper) {
		super(itemCatMapper, TbItemCat.class);
		this.itemCatMapper=itemCatMapper;
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
    public PageInfo<TbItemCat> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<TbItemCat> all = itemCatMapper.selectAll();
        PageInfo<TbItemCat> info = new PageInfo<TbItemCat>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbItemCat> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }

	
	

	 @Override
    public PageInfo<TbItemCat> findPage(Integer pageNo, Integer pageSize, TbItemCat itemCat) {
        PageHelper.startPage(pageNo,pageSize);

        Example example = new Example(TbItemCat.class);
        Example.Criteria criteria = example.createCriteria();

        if(itemCat!=null){			
						if(StringUtils.isNotBlank(itemCat.getName())){
				criteria.andLike("name","%"+itemCat.getName()+"%");
				//criteria.andNameLike("%"+itemCat.getName()+"%");
			}
	
		}
        List<TbItemCat> all = itemCatMapper.selectByExample(example);
        PageInfo<TbItemCat> info = new PageInfo<TbItemCat>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbItemCat> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }

    @Override
    public List<TbItemCat> findByParentId(Long id) {


        List<TbItemCat> itemcatsFromRedis = (List<TbItemCat>) redisTemplate.boundHashOps("itemCatList").get(id);
        if(itemcatsFromRedis!=null && itemcatsFromRedis.size()>0){
            System.out.println("走了缓存");
            return itemcatsFromRedis;
        }


        TbItemCat tbItemCat = new TbItemCat();
        tbItemCat.setParentId(id);
        List<TbItemCat> tbItemCatList = itemCatMapper.select(tbItemCat);

        redisTemplate.boundHashOps("itemCatList").put(id,tbItemCatList);

        return tbItemCatList;
    }

    @Override
    public List findAllItemCatList(Long id) {
        List allItemCatList = (List) redisTemplate.boundHashOps("allItemCatList").get(id);
        if(allItemCatList!=null && allItemCatList.size()>0){
            System.out.println("走了缓存");
            return allItemCatList;
        }

        List allitemcatList=new ArrayList<>();


        //第一级
        TbItemCat tbItemCat = new TbItemCat();
        tbItemCat.setParentId(id);
        List<TbItemCat> itemCatList1 = itemCatMapper.select(tbItemCat);

        for (TbItemCat itemCat1 : itemCatList1) {
            Map<String,Object> map1=new HashMap();
            map1.put("itemCat1",itemCat1);

            TbItemCat tbItemCat2 = new TbItemCat();
            tbItemCat2.setParentId(itemCat1.getId());
            List<TbItemCat> itemCatList2 = itemCatMapper.select(tbItemCat2);

            List<Map> itemList2=new ArrayList<>();

            for (TbItemCat itemCat2 : itemCatList2) {
                Map<String,Object> map2=new HashMap();
                map2.put("itemCat2",itemCat2);

                TbItemCat tbItemCat3 = new TbItemCat();
                tbItemCat3.setParentId(itemCat2.getId());
                List<TbItemCat> itemCatList3 = itemCatMapper.select(tbItemCat3);

                map2.put("itemCatList3",itemCatList3);

                itemList2.add(map2);
            }

            map1.put("itemList2",itemList2);

            allitemcatList.add(map1);


        }


        redisTemplate.boundHashOps("allItemCatList").put(id,allitemcatList);

        return allitemcatList;




    }

}


