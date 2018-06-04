package com.outlook.schooluniformsama.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
	String cmd();
	String[] childCmds() default "";
	String[] args() default "" ;
	String permissions() default "";
	String des();
	boolean needPlayer() default true;
	boolean hasChildCmds();
	int argsLenght();
	String type();
	//"Type"'s feature is mainly classified command type to facilitate the display of information
	//"HELP" is the help info
}
