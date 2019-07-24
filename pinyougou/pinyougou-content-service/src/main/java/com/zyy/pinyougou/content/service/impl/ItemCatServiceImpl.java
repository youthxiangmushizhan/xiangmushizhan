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

import java.util.List;


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

}


