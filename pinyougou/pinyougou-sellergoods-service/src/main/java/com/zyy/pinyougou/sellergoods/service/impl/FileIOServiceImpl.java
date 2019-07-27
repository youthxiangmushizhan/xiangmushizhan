package com.zyy.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.zyy.pinyougou.common.POIUtils;
import com.zyy.pinyougou.mapper.TbItemCatMapper;
import com.zyy.pinyougou.newPOJO.OrderTemplate;
import com.zyy.pinyougou.pojo.*;
import com.zyy.pinyougou.sellergoods.service.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Zyy
 * @date: 2019-07-26 12:55
 * @description:
 * @version:
 */
@Service
public class FileIOServiceImpl implements FileIOService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ItemCatService itemCatService;

    @Autowired
    private TypeTemplateService typeTemplateService;

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private SpecificationOptionService optionService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Override
    public void importFile(List<List<String[]>> list, String className) {
        try {
            if ("TbBrand".equals(className)) {
                if(list != null && list.size() > 0) {
                    if (list.get(0) != null && list.get(0).size() > 0) {
                        List<TbBrand> brandList = new ArrayList();
                        for (String[] strings : list.get(0)) {
                            TbBrand tbBrand = new TbBrand();
                            tbBrand.setName(strings[0]);
                            tbBrand.setFirstChar(strings[1]);
                            tbBrand.setStatus(strings[2]);
                            brandList.add(tbBrand);
                        }
                        brandService.add(brandList);
                    }
                }
            } else if ("TbItemCat".equals(className)){
                if(list != null && list.size() > 0) {
                    if (list.get(0) != null && list.get(0).size() > 0) {
                        List<TbItemCat> itemCatList = new ArrayList();
                        for (String[] strings : list.get(0)) {
                            TbItemCat itemCat = new TbItemCat();
                            itemCat.setParentId(Long.valueOf(strings[0]));
                            itemCat.setName(strings[1]);
                            itemCat.setTypeId(Long.valueOf(strings[2]));
                            itemCat.setStatus(strings[3]);
                            itemCatList.add(itemCat);
                        }
                        itemCatService.add(itemCatList);
                    }
                }
            } else if ("TbTypeTemplate".equals(className)){
                if(list != null && list.size() > 0) {
                    if (list.get(0) != null && list.get(0).size() > 0) {
                        List<TbTypeTemplate> typeTemplateList = new ArrayList();
                        for (String[] strings : list.get(0)) {
                            TbTypeTemplate typeTemplate = new TbTypeTemplate();
                            typeTemplate.setName(strings[0]);
                            typeTemplate.setSpecIds(strings[1]);
                            typeTemplate.setBrandIds(strings[2]);
                            typeTemplate.setCustomAttributeItems(strings[3]);
                            typeTemplate.setStatus(strings[4]);
                            typeTemplateList.add(typeTemplate);
                        }
                        typeTemplateService.add(typeTemplateList);
                    }
                }
            } else if ("TbSpecification".equals(className)){
                if (list != null && list.size() > 0) {
                    if (list.get(0) != null && list.get(0).size() > 0) {
                        List<TbSpecification> tbSpecificationList = new ArrayList();
                        for (String[] strings : list.get(0)) {
                            TbSpecification tbSpecification = new TbSpecification();
                            tbSpecification.setSpecName(strings[0]);
                            tbSpecification.setStatus(strings[1]);
                            tbSpecificationList.add(tbSpecification);
                        }
                        specificationService.add(tbSpecificationList);
                    }

                    if (list.get(1) != null && list.get(1).size() > 0) {
                        List<TbSpecificationOption> tbSpecificationOptionList = new ArrayList();
                        for (String[] strings : list.get(1)) {
                            TbSpecificationOption tbSpecificationOption = new TbSpecificationOption();
                            tbSpecificationOption.setOptionName(strings[0]);
                            tbSpecificationOption.setSpecId(Long.valueOf(strings[1]));
                            tbSpecificationOption.setOrders(Integer.valueOf(strings[2]));
                            tbSpecificationOptionList.add(tbSpecificationOption);
                        }
                        optionService.add(tbSpecificationOptionList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public List exportOrdersExcel(String typeId, OrderTemplate orderTemplate) {

        List<Object> list = new ArrayList<>();

        //根据typeID,判断导出类型
            if ("0".equals(typeId)) {
                //所有订单

            } else if ("1".equals(typeId)) {
                //普通订单
                List<TbOrder> tbOrders = orderService.findAll();

            } else if ("2".equals(typeId)) {
                //秒杀订单

            } else if ("3".equals(typeId)) {
                //按需查找订单

            }
        return list;
    }*/
}
