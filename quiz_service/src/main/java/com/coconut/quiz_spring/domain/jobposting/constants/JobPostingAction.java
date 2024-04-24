package com.coconut.quiz_spring.domain.jobposting.constants;

import java.util.Arrays;

public enum JobPostingAction {
  CREATE("create"),
  EDIT("edit"),

  DELETE("delete");

  private final String action;

  JobPostingAction(String action) {
    this.action = action;
  }

  public String getValue() {
    return this.action;
  }

  public static JobPostingAction findBy(String action) {
    return Arrays.stream(JobPostingAction.values())
            .filter((JobPostingAction) -> JobPostingAction.getValue().equalsIgnoreCase(action))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("create, edit, delete 중 선택해주세요. 입력: " + action));
  }
}
