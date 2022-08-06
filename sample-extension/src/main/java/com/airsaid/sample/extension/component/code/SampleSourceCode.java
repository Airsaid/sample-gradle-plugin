package com.airsaid.sample.extension.component.code;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JackChen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SampleSourceCode {
  /**
   * If you want to filter the source files by regex.
   */
  String regex() default "";
}
