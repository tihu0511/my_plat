package org.jigang.plat.file.excel;

import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;

/**
 * Created by wujigang on 2016/11/3.
 */
public interface ExcelDealCallback {
    boolean deal(Row row) throws IOException;
}
