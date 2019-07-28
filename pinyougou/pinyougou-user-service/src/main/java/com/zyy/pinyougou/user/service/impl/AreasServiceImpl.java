package com.zyy.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.core.service.CoreServiceImpl;
import com.zyy.pinyougou.mapper.TbAreasMapper;
import com.zyy.pinyougou.pojo.TbAddress;
import com.zyy.pinyougou.pojo.TbAreas;
import com.zyy.pinyougou.user.service.AreasService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class AreasServiceImpl extends CoreServiceImpl<TbAreas> implements AreasService {

	
	private TbAreasMapper areasMapper;

	@Autowired
	public AreasServiceImpl(TbAreasMapper areasMapper) {
		super(areasMapper, TbAreas.class);
		this.areasMapper=areasMapper;
	}

	
	

	
	@Override
    public PageInfo<TbAreas> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<TbAreas> all = areasMapper.selectAll();
        PageInfo<TbAreas> info = new PageInfo<TbAreas>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbAreas> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }

	
	

	 @Override
    public PageInfo<TbAreas> findPage(Integer pageNo, Integer pageSize, TbAreas areas) {
        PageHelper.startPage(pageNo,pageSize);

        Example example = new Example(TbAreas.class);
        Example.Criteria criteria = example.createCriteria();

        if(areas!=null){			
						if(StringUtils.isNotBlank(areas.getAreaid())){
				criteria.andLike("areaid","%"+areas.getAreaid()+"%");
				//criteria.andAreaidLike("%"+areas.getAreaid()+"%");
			}
			if(StringUtils.isNotBlank(areas.getArea())){
				criteria.andLike("area","%"+areas.getArea()+"%");
				//criteria.andAreaLike("%"+areas.getArea()+"%");
			}
			if(StringUtils.isNotBlank(areas.getCityid())){
				criteria.andLike("cityid","%"+areas.getCityid()+"%");
				//criteria.andCityidLike("%"+areas.getCityid()+"%");
			}
	
		}
        List<TbAreas> all = areasMapper.selectByExample(example);
        PageInfo<TbAreas> info = new PageInfo<TbAreas>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbAreas> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }

    @Override
    public List<TbAreas> findAreasByCityid(String cityid) {
        Example example = new Example(TbAreas.class);
        example.createCriteria().andEqualTo("cityid",cityid);
        return areasMapper.selectByExample(example);
    }

}
