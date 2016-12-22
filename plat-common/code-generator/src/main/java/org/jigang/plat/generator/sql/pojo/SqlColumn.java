package org.jigang.plat.generator.sql.pojo;

/**
 * Created by wujigang on 2016/11/7.
 */
public class SqlColumn {
    private String name;
    private String type;
    private boolean isNotNullable;
    private String defaultValue;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean getIsNotNullable() {
        return isNotNullable;
    }

    public void setNotNullable(boolean notNullable) {
        isNotNullable = notNullable;
    }
}
