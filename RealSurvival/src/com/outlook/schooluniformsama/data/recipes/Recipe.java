package com.outlook.schooluniformsama.data.recipes;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
	protected int needTime;
	protected String name;
	protected List<String> shape=new ArrayList<>();
	protected WorkbenchType type;
	protected String tableType;
	
	protected Recipe(String name,WorkbenchType type,List<String> shape,int needTime,String tableType){
		this.name=name;
		this.type=type;
		this.shape=shape;
		this.needTime=needTime;
		this.tableType = tableType;
	}
	
	protected Recipe(String name,WorkbenchType type,int needTime,String tableType){
		this.name=name;
		this.type=type;
		this.needTime=needTime;
		this.tableType = tableType;
	}

	public int getNeedTime() {
		return needTime;
	}

	public void setNeedTime(int needTime) {
		this.needTime = needTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getShape() {
		return shape;
	}

	public void setShape(List<String> shape) {
		this.shape = shape;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

}
