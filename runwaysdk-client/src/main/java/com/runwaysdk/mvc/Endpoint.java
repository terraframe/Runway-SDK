package com.runwaysdk.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.runwaysdk.controller.ServletMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Endpoint {
  public String url() default "[unassigned]";

  public ServletMethod method() default ServletMethod.GET;
}
