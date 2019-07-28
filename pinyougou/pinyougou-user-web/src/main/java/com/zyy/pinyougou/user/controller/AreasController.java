package com.zyy.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.pojo.TbAreas;
import com.zyy.pinyougou.user.service.AreasService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/areas")
public class AreasController {

	@Reference
	private AreasService areasService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbAreas> findAll(){
		return areasService.findAll();
	}
	
	
	
	@RequestMapping("/findPage")
    public PageInfo<TbAreas> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return areasService.findPage(pageNo, pageSize);
    }
	
	/**
	 * 增加
	 * @param areas
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbAreas areas){
		try {
			areasService.add(areas);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param areas
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbAreas areas){
		try {
			areasService.update(areas);
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
	public TbAreas findOne(@PathVariable(value = "id") Long id){
		return areasService.findOne(id);		
	}

	/**
	 * 获取实体
	 * @param cityid
	 * @return
	 */
	@RequestMapping("/findAreasByCityid/{provinceId}")
	public List<TbAreas> findAreasByCityid(@PathVariable(value = "cityid") String cityid){
		return areasService.findAreasByCityid(cityid);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(@RequestBody Long[] ids){
		try {
			areasService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	

	@RequestMapping("/search")
    public PageInfo<TbAreas> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
                                      @RequestBody TbAreas areas) {
        return areasService.findPage(pageNo, pageSize, areas);
    }
	
}
