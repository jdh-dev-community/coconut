package com.coconut.quiz_spring.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ConcurrencyTestUtils {

  public static <T> void executeConcurrently(Executable<T> executor, int concurrencyCount) {
    ExecutorService executorService = Executors.newFixedThreadPool(concurrencyCount);
    CountDownLatch latch = new CountDownLatch(concurrencyCount);

    try {
      for (int i = 0; i < concurrencyCount; i++) {
        executorService.submit(() -> {
          try {
            executor.execute();
          } finally {
            latch.countDown();
          }
        });
      }

      latch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      executorService.shutdown();
    }
  }


}
