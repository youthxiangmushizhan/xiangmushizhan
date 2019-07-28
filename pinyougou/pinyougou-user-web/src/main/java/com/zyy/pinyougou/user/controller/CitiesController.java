package com.zyy.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.pojo.TbCities;
import com.zyy.pinyougou.user.service.CitiesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/cities")
public class CitiesController {

	@Reference
	private CitiesService citiesService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbCities> findAll(){
		return citiesService.findAll();
	}
	
	
	
	@RequestMapping("/findPage")
    public PageInfo<TbCities> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return citiesService.findPage(pageNo, pageSize);
    }
	
	/**
	 * 增加
	 * @param cities
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbCities cities){
		try {
			citiesService.add(cities);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param cities
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbCities cities){
		try {
			citiesService.update(cities);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne/{id}")
	public TbCities findOne(@PathVariable(value = "id") Long id){
		return citiesService.findOne(id);		
	}

	/**
	 * 获取实体
	 * @param provinceid
	 * @return
	 */
	@RequestMapping("/findCitiesByProvinceid/{provinceid}")
	public List<TbCities> findOne(@PathVariable(value = "provinceid") String provinceid){
		return citiesService.findCitiesByProvinceid(provinceid);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(@RequestBody Long[] ids){
		try {
			citiesService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	

	@RequestMapping("/search")
    public PageInfo<TbCities> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
                                      @RequestBody TbCities cities) {
        return citiesService.findPage(pageNo, pageSize, cities);
    }
	
}
