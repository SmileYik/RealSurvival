package com.outlook.schooluniformsama.command;

import com.outlook.schooluniformsama.lowversion.Commands_1_8;

public enum CommandsType {
	Commands_1_8(Commands_1_8.class),
	Commands_1_9_UP(Commands.class);
	private Class<? extends Object> clazz;
	
	 private CommandsType(Class<? extends Object> clazz) {
		this.clazz=clazz;
	}
	 
	 public Class<? extends Object> getClazz(){
		 return clazz;
	 }
}
