package com.coconut.global.constant;

import org.springframework.core.Ordered;

public class ControllerAdviceOrder {
  public static final int GLOBAL = Ordered.LOWEST_PRECEDENCE - 10;
  public static final int SHARE = Ordered.LOWEST_PRECEDENCE - 100;
  public static final int SERVICE = Ordered.LOWEST_PRECEDENCE - 1000;

}
