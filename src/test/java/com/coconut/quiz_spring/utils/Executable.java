package com.coconut.quiz_spring.utils;

@FunctionalInterface
public interface Executable<T> {
  T execute();
}
