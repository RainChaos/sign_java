package cn.gljp.sign.admin.entity;

import java.io.Serializable;

public class FormField implements Serializable {


    private  String dataType;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    private Integer id;

    private String formName;

    private String fieldName;
    private Integer piciId;

    public Integer getPiciId() {
        return piciId;
    }

    public void setPiciId(Integer piciId) {
        this.piciId = piciId;
    }

    private String describe;

    private String rules;

    private String value;

    private Integer labelWidth;

    private Integer width;

    private Integer sort;

    private Boolean isMustInput;

    private Boolean isShow;

    private Boolean isDisabed;

    private Boolean isExport;

    private String remark;

    private Boolean isCenter;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(Integer labelWidth) {
        this.labelWidth = labelWidth;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getIsMustInput() {
        return isMustInput;
    }

    public void setIsMustInput(Boolean isMustInput) {
        this.isMustInput = isMustInput;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Boolean getIsDisabed() {
        return isDisabed;
    }

    public void setIsDisabed(Boolean isDisabed) {
        this.isDisabed = isDisabed;
    }

    public Boolean getIsExport() {
        return isExport;
    }

    public void setIsExport(Boolean isExport) {
        this.isExport = isExport;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getIsCenter() {
        return isCenter;
    }

    public void setIsCenter(Boolean isCenter) {
        this.isCenter = isCenter;
    }
}