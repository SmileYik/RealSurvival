package miskyle.realsurvival.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
	String[] subCmd() default {};
	String[] args() default {};
	String permission() default "";
	String des();
	boolean needPlayer() default true;
	boolean unlimitedLength() default false;
}
