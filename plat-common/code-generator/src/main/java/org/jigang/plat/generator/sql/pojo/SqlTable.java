package org.jigang.plat.generator.sql.pojo;

import java.util.List;

/**
 * Created by wujigang on 2016/11/7.
 */
public class SqlTable {
    private String name;
    private String comment;
    private List<SqlColumn> sqlColumns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SqlColumn> getSqlColumns() {
        return sqlColumns;
    }

    public void setSqlColumns(List<SqlColumn> sqlColumns) {
        this.sqlColumns = sqlColumns;
    }
}
