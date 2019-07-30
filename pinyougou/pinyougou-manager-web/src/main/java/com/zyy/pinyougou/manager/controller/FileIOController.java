package com.zyy.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.common.POIUtils;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.newPOJO.OrderTemplate;
import com.zyy.pinyougou.pojo.TbOrder;
import com.zyy.pinyougou.pojo.TbOrderItem;
import com.zyy.pinyougou.sellergoods.service.FileIOService;
import com.zyy.pinyougou.sellergoods.service.OrderItemService;
import com.zyy.pinyougou.sellergoods.service.OrderService;
import com.zyy.pinyougou.sellergoods.service.OrderTemplateService;
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

    @Reference
    private OrderTemplateService orderTemplateService;

    @Reference
    private OrderItemService orderItemService;

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

    @RequestMapping("/exportOrder")
    public void exportOrderToFile(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "timeType",defaultValue = "",required = false) String timeType,
                                  @RequestParam(value = "startTime",defaultValue = "",required = false) String startTime,
                                  @RequestParam(value = "endTime",defaultValue = "",required = false) String endTime,
                                  @RequestBody OrderTemplate orderTemplate) {

        ServletOutputStream outputStream = null;
        XSSFWorkbook workbook = null;
        try {
            List<OrderTemplate> orderTemplates = orderTemplateService.searchOrderTemplate(timeType, startTime, endTime, orderTemplate);
            workbook = POIUtils.exportExcel(orderTemplates);

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

    @RequestMapping("/exportOrderItem")
    public void exportOrderToFile(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "userId",defaultValue = "",required = false) String userId,
                                  @RequestParam(value = "startTime",defaultValue = "",required = false) String startTime,
                                  @RequestParam(value = "endTime",defaultValue = "",required = false) String endTime) {

        ServletOutputStream outputStream = null;
        XSSFWorkbook workbook = null;
        try {
            List<TbOrderItem> orderItems = orderItemService.findOrderItemByUserIdAndDate(startTime, endTime, userId);
            workbook = POIUtils.exportExcel(orderItems);

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
