package org.jigang.plat.generator.sql;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jigang.plat.core.lang.util.CollectionUtil;
import org.jigang.plat.core.lang.util.StringUtil;
import org.jigang.plat.core.template.ClasspathResTplUtil;
import org.jigang.plat.file.FileUtil;
import org.jigang.plat.file.excel.ExcelDealCallback;
import org.jigang.plat.file.excel.ExcelUtil;
import org.jigang.plat.generator.sql.pojo.SqlColumn;
import org.jigang.plat.generator.sql.pojo.SqlTable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wujigang on 2016/11/7.
 */
public class SqlGenerator {
    private static final String CREATE_SQL_TPL_PATH = "/tpl/sql/create.vm";

    private String schemaName;
    private String resourcesPath;

    public SqlGenerator(String schemaName, String resourcesPath) {
        this.schemaName = schemaName;
        this.resourcesPath = resourcesPath;
    }

    public void generate(String file) throws IOException {
        List<SqlTable> tables = parseTables(file);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tables", tables);
        params.put("schemaName", schemaName);
        FileUtil.writeFile(resourcesPath + File.separator + "create.sql", ClasspathResTplUtil.getTemplateContent(params, CREATE_SQL_TPL_PATH));
    }

    public List<SqlTable> parseTables(String file) throws IOException {
        List<Sheet> sheets = ExcelUtil.readExcelSheets(file);
        if (CollectionUtil.isEmpty(sheets)) {
            return null;
        }

        List<SqlTable> tables = new ArrayList<SqlTable>();
        for (Sheet sheet : sheets) {
            String sheetName = sheet.getSheetName();
            if (sheetName.indexOf("(") < 0 || sheetName.indexOf(")") < 0) {
                System.out.println(sheetName + "不包含表名,跳过");
                continue;
            }

            String tableName = sheetName.substring(sheetName.indexOf("(") + 1, sheetName.indexOf(")"));
            String tableComment = sheetName.substring(0, sheetName.indexOf("("));

            SqlTable table = new SqlTable();
            table.setName(tableName);
            table.setComment(tableComment);

            final List<SqlColumn> columns = new ArrayList<SqlColumn>();
            ExcelUtil.readExcelAndDeal(sheet, new ExcelDealCallback() {
                @Override
                public boolean deal(Row row) throws IOException {
                    if (row.getRowNum() == 0) {
                        return true;
                    }
                    SqlColumn column = new SqlColumn();
                    column.setName(getCellString(row, 0));
                    column.setType(getCellString(row, 1));
                    String isNullableStr = getCellString(row, 2);
                    column.setNotNullable(StringUtil.hasLength(isNullableStr) && "Y".equals(isNullableStr));
                    String defaultValue = getCellString(row, 3);
                    column.setDefaultValue(StringUtil.hasLength(defaultValue) ? defaultValue : null);
                    column.setComment(getCellString(row, 4));

                    columns.add(column);
                    return true;
                }
            });
            table.setSqlColumns(columns);
            tables.add(table);
        }
        return tables;
    }

    private String getCellString(Row row, int index) {
        if (null != row
                && row.getLastCellNum() >= index
                && row.getCell(index) != null) {
            Cell cell = row.getCell(index);
            int type = cell.getCellType();
            switch(type) {
                case Cell.CELL_TYPE_BLANK:
                    return null;
                case Cell.CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case Cell.CELL_TYPE_NUMERIC:
                    return String.valueOf((int)cell.getNumericCellValue());
                default:
                    return cell.getStringCellValue();
            }
        }
        return null;
    }
}
