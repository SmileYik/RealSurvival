package com.outlook.schooluniformsama.data.recipes;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
	protected int needTime;
	protected String name;
	protected List<String> shape=new ArrayList<>();
	protected WorkbenchType type;
	
	protected Recipe(String name,WorkbenchType type,List<String> shape,int needTime){
		this.name=name;
		this.type=type;
		this.shape=shape;
		this.needTime=needTime;
	}
	
	protected Recipe(String name,WorkbenchType type,int needTime){
		this.name=name;
		this.type=type;
		this.needTime=needTime;
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
	
}
