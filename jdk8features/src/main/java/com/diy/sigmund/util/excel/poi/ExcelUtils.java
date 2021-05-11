package com.diy.sigmund.util.excel.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ylm-sigmund
 * @since 2021/3/22 9:55
 */
public class ExcelUtils {
    public static void main(String[] args) {
        Workbook workbook = readExcel("C:\\Users\\ylm\\Desktop\\新建 Microsoft Excel 工作表.xlsx");
        //Workbook workbook = readExcel("F:\\work\\other\\测试xlsx文档解析.xlsx");

        //获得sheet的数量(sheet的index是从0开始的)
        int sheetCount = workbook.getNumberOfSheets();
        System.out.println("文档一共有"+sheetCount+"个Sheet");
        //遍历Sheet
        for(int i = 0;i < sheetCount;i++){
            System.out.println("开始遍历第"+i+"个sheet_________________________________________________________________________");
            //得到Sheet
            Sheet sheet = workbook.getSheetAt(i);
            //得到每个Sheet的行数,此工作表中包含的最后一行(Row的index是从0开始的)
            int rowCount = sheet.getLastRowNum();
            System.out.println("第"+i+"个sheet中一共有"+rowCount+"行");
            //遍历Row
            for(int j = 0 ;j <= rowCount;j++){
                System.out.println("开始遍历第"+j+"行~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                //得到Row
                Row row = sheet.getRow(j);
                if(row == null){
                    System.out.println("第"+j+"行为空，跳出本行");
                    continue;
                }
                //得到每个Row的单元格数
                int cellCount = row.getLastCellNum();
                System.out.println("第"+i+"个sheet中的第"+j+"行有"+cellCount+"个单元格");

                for(int k = 0 ;k < cellCount;k++){
                    System.out.println("开始遍历第"+k+"个单元格+++++++");
                    Cell cell = row.getCell(k);
                    //输出单元格里的值
                    System.out.println(getCellFormatValue(cell));
                }

                System.out.println("第"+j+"行遍历结束");
            }
            System.out.println("第"+i+"个sheet遍历结束");

        }


    }


    /**
     * 根据文件地址，解析后缀返回不同的Workbook对象
     * @param filePath 文件地址
     * @return Excel文档对象Workbook
     */
    public static Workbook readExcel(String filePath){

        if(filePath == null || filePath.equals("")){
            return null;
        }
        //得到文件后缀
        String suffix = filePath.substring(filePath.lastIndexOf("."));
        System.out.println(suffix);
        try {
            InputStream is = new FileInputStream(filePath);
            if(".xls".equals(suffix)){
                System.out.println("文件类型是.xls");
                return new HSSFWorkbook(is);
            }
            if(".xlsx".equals(suffix)){
                System.out.println("文件类型是.xlsx");
                return new XSSFWorkbook(is);
            }
            return null;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件没有找到");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("发生io异常");
        }
        return null;
    }

    public static Object getCellFormatValue(Cell cell){
        Object cellValue;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                //空值单元格
                case Cell.CELL_TYPE_BLANK:{
                    System.out.println("空单元格");
                    cellValue = "";
                    break;
                }
                //数值型单元格 getNumericCellValue()以数字形式获取单元格的值。
                case Cell.CELL_TYPE_NUMERIC:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        System.out.println("日期格式");
                        //转换为日期格式YYYY-mm-dd
                        //cellValue = cell.getDateCellValue();
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cellValue = dateFormat.format(date);
                    }else{
                        //数字
                        System.out.println("数字格式");
                        cellValue = cell.getNumericCellValue();
                    }
                    break;
                }
                //公式型单元格getCellFormula()返回单元格的公式
                case Cell.CELL_TYPE_FORMULA:{
                    System.out.println("公式型");
                    System.out.println(cell.getCellFormula());
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                //字符串单元格
                case Cell.CELL_TYPE_STRING:{
                    System.out.println("字符串格式");
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                //布尔值型单元格
                case Cell.CELL_TYPE_BOOLEAN:{
                    System.out.println("布尔值");
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
}
