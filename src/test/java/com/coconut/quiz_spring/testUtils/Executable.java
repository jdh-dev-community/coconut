package com.coconut.quiz_spring.testUtils;

@FunctionalInterface
public interface Executable<T> {
  T execute();
}
