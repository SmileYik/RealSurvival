package com.outlook.schooluniformsama.data;

import com.outlook.schooluniformsama.data.recipe.WorkbenchType;

public class WorkbenchShape {
	private WorkbenchType type;
	private String name;
	private String title;
	private String main,left,right,up,down,front,behind;
	
	public WorkbenchShape(WorkbenchType type, String name, String title, String main, String left, String right,
			String up, String down, String front, String behind) {
		this.type = type;
		this.name = name;
		this.title = title;
		this.main = main;
		if(left!=null&&left!="null")
			this.left = left;
		if(right!=null&&right!="null")
			this.right = right;
		if(up!=null&&up!="null")
			this.up = up;
		if(down!=null&&down!="null")
			this.down = down;
		if(front!=null&&front!="null")
			this.front = front;
		if(behind!=null&&behind!="null")
			this.behind = behind;
	}

	public WorkbenchType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public String getMain() {
		return main;
	}

	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

	public String getUp() {
		return up;
	}

	public String getDown() {
		return down;
	}

	public String getFront() {
		return front;
	}

	public String getBehind() {
		return behind;
	}
	
}
