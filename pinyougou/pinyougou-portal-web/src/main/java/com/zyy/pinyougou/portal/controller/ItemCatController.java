package com.zyy.pinyougou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zyy.pinyougou.content.service.ItemCatService;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.pojo.TbItemCat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

	@Reference
	private ItemCatService itemCatService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findByParentId/{id}")
	public List<TbItemCat> findAll(@PathVariable("id")Long id){
		return itemCatService.findByParentId(id);
	}

	@RequestMapping("/findAllItemCatList")
	public List findAllItemCatList() {
		List list = new ArrayList();

		//1级
		List<TbItemCat> itemCatList1 = itemCatService.findByParentId(0L);
		for (TbItemCat cat1 : itemCatList1) {
			Map<String,Object> map1 = new HashMap();
			//map1的childList
			List<Map> list1 = new ArrayList();
			map1.put("name",cat1.getName());
			//2级
			List<TbItemCat> itemCatList2 = itemCatService.findByParentId(cat1.getId());
			for (TbItemCat cat2 : itemCatList2) {
				Map<String,Object> map2 = new HashMap();
				List<String> list2 = new ArrayList();
				map2.put("name",cat2.getName());
				List<TbItemCat> itemCatList3 = itemCatService.findByParentId(cat2.getId());
				for (TbItemCat cat3 : itemCatList3) {
					list2.add(cat3.getName());
				}
				map2.put("childList",list2);

				list1.add(map2);
			}
			map1.put("childList",list1);

			list.add(map1);

		}

		return list;
	}
	
	@RequestMapping("/findPage")
    public PageInfo<TbItemCat> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
										@RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        return itemCatService.findPage(pageNo, pageSize);
    }
	
	/**
	 * 增加
	 * @param itemCat
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbItemCat itemCat){
		try {
			itemCatService.add(itemCat);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param itemCat
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbItemCat itemCat){
		try {
			itemCatService.update(itemCat);
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
	public TbItemCat findOne(@PathVariable(value = "id") Long id){
		return itemCatService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(@RequestBody Long[] ids){
		try {
			itemCatService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
	

	@RequestMapping("/search")
    public PageInfo<TbItemCat> findPage(@RequestParam(value = "pageNo", defaultValue = "1", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize,
                                      @RequestBody TbItemCat itemCat) {
        return itemCatService.findPage(pageNo, pageSize, itemCat);
    }

    @RequestMapping("/findAll")
    public List<TbItemCat> findAllCategoty() {
		return itemCatService.findAll();
	}


}
