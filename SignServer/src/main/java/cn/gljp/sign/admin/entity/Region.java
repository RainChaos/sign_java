package cn.gljp.sign.admin.entity;

import java.io.Serializable;

public class Region implements Serializable {
    private Integer code;

    private Integer fatherCode;

    private Byte type;

    private String name;

    private String fullName;

    private Integer sort;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getFatherCode() {
        return fatherCode;
    }

    public void setFatherCode(Integer fatherCode) {
        this.fatherCode = fatherCode;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}