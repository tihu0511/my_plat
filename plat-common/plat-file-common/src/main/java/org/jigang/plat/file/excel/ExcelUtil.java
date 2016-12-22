package org.jigang.plat.file.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jigang.plat.core.lang.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujigang on 2016/11/3.
 */
public class ExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 读取excel
     * @param file
     * @return
     * @throws IOException
     */
    public static Workbook readExcel(String file) throws IOException {
        if (!StringUtil.hasLength(file) || !(file.endsWith(EXCEL_XLS) || file.endsWith(EXCEL_XLSX))) {
            logger.info("{}不是excel文件", file);
            return null;
        }
        InputStream is = new FileInputStream(file);
        return file.endsWith("xls") ? new HSSFWorkbook(is) : new XSSFWorkbook(is);
    }

    /**
     * 读取表格的所有sheet页
     * @param file
     * @return
     */
    public static List<Sheet> readExcelSheets(String file) throws IOException {
        Workbook workbook = readExcel(file);
        if (workbook.getNumberOfSheets() <= 0) {
            return null;
        }
        List<Sheet> sheets = new ArrayList<Sheet>();
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet sheet = workbook.getSheetAt(numSheet);
            if (null == sheet) {
                continue;
            }
            sheets.add(sheet);
        }
        return sheets;
    }

    /**
     * 读取表格的所有sheet页
     * @param workbook
     * @return
     */
    public static List<Sheet> readExcelSheets(Workbook workbook) {
        if (workbook.getNumberOfSheets() <= 0) {
            return null;
        }
        List<Sheet> sheets = new ArrayList<Sheet>();
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet sheet = workbook.getSheetAt(numSheet);
            if (null == sheet) {
                continue;
            }
            sheets.add(sheet);
        }
        return sheets;
    }

    /**
     * 读取表格某sheet页的所有行
     * @param sheet
     * @return
     */
    public static List<Row> readExcelRows(Sheet sheet) {
        if (sheet.getLastRowNum() <= 0) {
            return null;
        }
        List<Row> rows = new ArrayList<Row>();
        for (int rowNum = sheet.getFirstRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     *
     * @param sheet
     * @param callback
     * @return
     * @throws IOException
     */
    public static boolean readExcelAndDeal(Sheet sheet, ExcelDealCallback callback) throws IOException {
        if (null == sheet || sheet.getLastRowNum() < 0) {
            logger.info("表格页{}中没有数据", sheet.getSheetName());
            return true;
        }
        //行
        for (int rowNum = sheet.getFirstRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null || row.getFirstCellNum() < 0 || row.getLastCellNum() < 0) {
                continue;
            }
            //处理行
            if (!callback.deal(row)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 读取表格并处理
     * @param file
     * @param callback
     * @throws IOException
     */
    public static boolean readExcelAndDeal(String file, ExcelDealCallback callback) throws IOException {
        Workbook workbook = null;
        try {
            workbook = readExcel(file);
            //页
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                Sheet sheet = workbook.getSheetAt(numSheet);
                readExcelAndDeal(sheet, callback);
            }
            return true;
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }
}
