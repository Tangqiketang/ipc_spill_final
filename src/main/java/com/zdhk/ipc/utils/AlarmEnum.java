package com.zdhk.ipc.utils;

public enum AlarmEnum {
	_ResidualAmmeter_Notice(1,		"emLeakage_OverNotice", 	"漏电预警",	"ResidualAmmeter_Notice"),
	_Power_Off_Alarm(       18, 	"emPowerFailAlarm",         "掉电报警",	"Power_Off_Alarm");
	
	
	private Integer index;
	private String f800Type;
	private String desc;
	private String xhType;
	
	private AlarmEnum(Integer index,String f800Type, String desc,String xhType) {
		this.index = index;
		this.f800Type = f800Type;
		this.desc = desc;
		this.xhType=xhType;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getF800Type() {
		return f800Type;
	}

	public void setF800Type(String f800Type) {
		this.f800Type = f800Type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getXhType() {
		return xhType;
	}

	public void setXhType(String xhType) {
		this.xhType = xhType;
	}

	
	
	
}
