package com.jinhe.tss.um.helper.dto;

import java.io.Serializable;

/**
 * 同步使用的DTO
 */
public class GroupDTO implements Serializable {

    private static final long serialVersionUID = 2427966217868833368L;
    
    private String  id;       // 用户组主键
	private String  parentId; // 父节点ID 
	private Integer seqNo;    // 序号 
	private String  name;     // 用户组名称
	private Integer groupType;
	private String  description;  // 用户组信息描述
	private Integer disabled;     // 用户组状态
	
	public String getId() {
		return id;
	} 
	
	public String getParentId() {
		return parentId;
	}
	
	public Integer getSeqNo() {
		return seqNo;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public Integer getDisabled() {
		return disabled;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	
	public void setName(String name) {
		this.name = name;
	}
 
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
	
	public String toString(){
		return "Name:[" + name + "] id:[" + id + "] parentId:[" + parentId
				+ "] seqNo:[" + seqNo + "]";
	}
}

	