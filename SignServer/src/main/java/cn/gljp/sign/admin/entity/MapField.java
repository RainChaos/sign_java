package cn.gljp.sign.admin.entity;

import java.io.Serializable;

public class MapField implements Serializable {
    private Integer id;

    private String excelField;

    private String dbField;

    private Boolean status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExcelField() {
        return excelField;
    }

    public void setExcelField(String excelField) {
        this.excelField = excelField;
    }

    public String getDbField() {
        return dbField;
    }

    public void setDbField(String dbField) {
        this.dbField = dbField;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}