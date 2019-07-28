package com.zyy.pinyougou.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.tomcat.jni.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class POIUtils {
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    private final static String DATE_FORMAT = "yyyy/MM/dd";
    /**
     * 读入excel文件，解析后返回
     * @param file
     * @throws IOException
     */
    public static List<List<String[]>> readExcel(MultipartFile file) throws IOException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<List<String[]>> list = new ArrayList<>();
        if(workbook != null){
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                List<String[]> list1 = new ArrayList();
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum  = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了第一行的所有行
                for(int rowNum = firstRowNum+5;rowNum <= lastRowNum;rowNum++){
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if(row == null){
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    //循环当前行
                    for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list1.add(cells);
                }

                list.add(list1);
            }
            workbook.close();
        }
        return list;
    }

    public static XSSFWorkbook exportExcel(List list) throws Exception {

        //创建hssfWorkbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        if(list!=null || list.size()!=0){
            Field[] fields = list.get(0).getClass().getDeclaredFields();

            //创建工作簿
            XSSFSheet sheet = workbook.createSheet("用户订单信息表");

            //合并单元格
            //param1：起始行    param2：结束行      param3：起始列      param4：结束列
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,(fields.length==0)?5:fields.length-1));
            //修改行间距
            sheet.setDefaultColumnWidth(10);


            //创建标题
            //指定标题所在的行
            XSSFRow row = sheet.createRow(0);
            row.setHeight((short) 50);
            //指定标题所在的列
            XSSFCell cell = row.createCell(0);
            //设定单元格样式
            XSSFCellStyle cellStyle = workbook.createCellStyle();//获取样式对象
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置单元格居中对齐

            XSSFFont font = workbook.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short) 36);
            cellStyle.setFont(font);

            cellStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

            cell.setCellValue("用  户  订  单  信  息  表");
            cell.setCellStyle(cellStyle);

            //创建副标题行
            XSSFRow titleRow = sheet.createRow(1);
            for (int i=0;i<fields.length;i++) {
                XSSFCell titleCell = titleRow.createCell(i);
                //titleCell.setCellStyle();//可设置样式
                String fieldName = fields[i].getName();
                if (!"serialVersionUID".equals(fieldName) && !"orderIdStr".equals(fieldName)) {
                    titleCell.setCellValue(TranslationUtil.translateKeyWord(fieldName));//给当前列单元格设置小标题
                } else {
                    sheet.addMergedRegion(i > 0 ? new CellRangeAddress(1,1,i-1,i) : new CellRangeAddress(1,1,i,i + 1));
                }
            }

            //创建数据列
            for (int i=0;i<list.size();i++) {
                XSSFRow dataRow = sheet.createRow(i + 2);//开始创建数据的行位置

                //遍历字段对象
                for(int j=0;j<fields.length;j++){
                    XSSFCell dataCell = dataRow.createCell(j);
                    //dataCell.setCellStyle();//可给数据列设置样式

                    //通过反射获取对象的get方法
                    String getMethodName = "get"+fields[j].getName().substring(0,1).toUpperCase()+fields[j].getName().substring(1);
                    if (!"getSerialVersionUID".equals(getMethodName) && !"getOrderIdStr".equals(getMethodName)) {
                        Method method = list.get(0).getClass().getDeclaredMethod(getMethodName, new Class[]{});
                        Object invoke = method.invoke(list.get(i), new Object[]{});

                        if (invoke != null) {
                            if(invoke.getClass() == Date.class){
                                dataCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(invoke));
                            } else {
                                dataCell.setCellValue(invoke.toString());
                            }
                        } else {
                            dataCell.setCellValue("null");
                        }
                    }
                }
            }
        }

        return workbook;

    }

    //校验文件是否合法
    public static void checkFile(MultipartFile file) throws IOException{
        //判断文件是否存在
        if(null == file){
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){
            throw new IOException(fileName + "不是excel文件");
        }
    }
    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith(xls)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith(xlsx)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }
    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //如果当前单元格内容为日期类型，需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if(dataFormatString.equals("m/d/yy")){
            cellValue = new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }
}
