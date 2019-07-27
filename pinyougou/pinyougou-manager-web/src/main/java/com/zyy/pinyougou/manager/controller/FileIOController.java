package com.zyy.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.common.POIUtils;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.newPOJO.OrderTemplate;
import com.zyy.pinyougou.pojo.TbOrder;
import com.zyy.pinyougou.sellergoods.service.FileIOService;
import com.zyy.pinyougou.sellergoods.service.OrderService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * @author: Zyy
 * @date: 2019-07-26 12:58
 * @description:
 * @version:
 */
@RestController
@RequestMapping("/file")
public class FileIOController {

    @Reference
    private FileIOService fileIOService;

    @Reference
    private OrderService orderService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile,@RequestParam(value = "className") String className) {
        try {
            List<List<String[]>> list = POIUtils.readExcel(excelFile);
            fileIOService.importFile(list,className);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "导入失败");
        }
        return new Result(true,"导入成功");
    }

    @RequestMapping("/exportOrder/{typeId}")
    public void exportOrderToFile(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable(value = "typeId") String typeId,
                                  @RequestBody OrderTemplate orderTemplate) {

        ServletOutputStream outputStream = null;
        XSSFWorkbook workbook = null;
        try {

            if ("0".equals(typeId)) {
                //所有订单

            } else if ("1".equals(typeId)) {
                //普通订单
                List<TbOrder> tbOrders = orderService.findAll();
                workbook = POIUtils.exportExcel(tbOrders);
            } else if ("2".equals(typeId)) {
                //秒杀订单

            } else if ("3".equals(typeId)) {
                //按需查找订单

            }

            response.setContentType("multipart/form-data");
            //为文件重新设置名字，采用数据库内存储的文件名称
            response.addHeader("Content-Disposition", "attachment; filename=" + new Date().getTime() + ".xlsx");
            outputStream = response.getOutputStream();

            workbook.write(outputStream);

            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                outputStream.close();
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
