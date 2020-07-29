package miskyle.realsurvival.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
  /**
   * 子命令.
   * 
   * @return 子命令数列
   */
  String[] subCmd() default {};

  /**
   * 记录指令的长度及参数要求.
   * @return
   */
  String[] args() default {};

  /**
   * 记录指令需要的权限, 默认为无权限.
   * @return
   */
  String permission() default "";

  /**
   * 指令的注释.
   * @return
   */
  String des();

  /**
   * 是否需要玩家执行此指令,默认为true.
   * @return
   */
  boolean needPlayer() default true;

  /**
   * 指令长度是否无限长,默认为false.
   * @return
   */
  boolean unlimitedLength() default false;
}
