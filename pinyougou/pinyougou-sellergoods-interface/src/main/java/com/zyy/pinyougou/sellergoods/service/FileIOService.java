package com.zyy.pinyougou.sellergoods.service;

import com.zyy.pinyougou.newPOJO.OrderTemplate;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * @author: Zyy
 * @date: 2019-07-26 12:55
 * @description:
 * @version:
 */
public interface FileIOService {
    void importFile(List<List<String[]>> list, String className);

    /*XSSFWorkbook exportOrdersExcel(String typeId, OrderTemplate orderTemplate);*/
}
