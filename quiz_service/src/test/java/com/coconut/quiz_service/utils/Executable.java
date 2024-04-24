package com.coconut.quiz_service.utils;

@FunctionalInterface
public interface Executable<T> {
  T execute();
}
