package com.zyy.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zyy.pinyougou.common.POIUtils;
import com.zyy.pinyougou.entity.Result;
import com.zyy.pinyougou.sellergoods.service.FileIOService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
