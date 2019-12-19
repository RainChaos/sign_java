package cn.gljp.sign.admin.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AuthRule implements Serializable {
    private String id;
    private String father_id;
    private String appid;
    private String type;
    private String name;
    private String url;
    private String status;
    private String sort;
    private String remark;
    private boolean checked;
    private List<AuthRule> children;

    public AuthRule() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFather_id() {
        return father_id;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<AuthRule> getChildren() {
        return children;
    }

    public void setChildren(List<AuthRule> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "AuthRule{" +
                "id='" + id + '\'' +
                ", father_id='" + father_id + '\'' +
                ", appid='" + appid + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", status='" + status + '\'' +
                ", sort='" + sort + '\'' +
                ", remark='" + remark + '\'' +
                ", checked=" + checked +
                ", children=" + children +
                '}';
    }
}
