package com.db.riskit.dto;

import com.tree.utils.common.StringUtils;

public class ComboDTO implements Comparable<ComboDTO> {
	private String name;
	private String code;
	private String newCode;
	private String parent;
	

	@Override
	public int compareTo(ComboDTO arg0) {
		return this.getName().compareTo(arg0.getName());
	}

	
	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		if (StringUtils.isStringNull(name)) {
			this.name = null;
		} else {
			this.name = name;
		}
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		if (StringUtils.isStringNull(code)) {
			this.code = null;
		} else {
			this.code = code;
		}
	}
	
	public String getParent() {
		return parent;
	}
	
	public void setParent(String parent) {
		if (StringUtils.isStringNull(parent)) {
			this.parent = null;
		} else {
			this.parent = parent;
		}
	}
	
	public String getNewCode() {
		return newCode;
	}
	
	public void setNewCode(String newCode) {
		if (StringUtils.isStringNull(newCode)) {
			this.newCode = null;
		} else {
			this.newCode = newCode;
		}
	}

}
