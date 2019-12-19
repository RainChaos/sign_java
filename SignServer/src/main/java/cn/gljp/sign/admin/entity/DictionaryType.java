package cn.gljp.sign.admin.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * 实体类
 * @author Administrator
 *
 */
public class DictionaryType implements Serializable{

	private Integer id;

	private Integer pid;

	private String name;

	private String value;

	private Integer sort;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	List<DictionaryType> children;

	public List<DictionaryType> getChildren() {
		return children;
	}

	public void setChildren(List<DictionaryType> children) {
		this.children = children;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
