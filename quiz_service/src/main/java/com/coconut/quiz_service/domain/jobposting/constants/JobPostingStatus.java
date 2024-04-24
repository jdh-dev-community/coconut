package com.coconut.quiz_service.domain.jobposting.constants;

import java.util.Arrays;

public enum JobPostingStatus {
  ACTIVE("active"),
  INACTIVE("inactive"),
  DELETED("deleted");

  private final String status;

  JobPostingStatus(String status) {
    this.status = status;
  }

  public String getValue() {
    return this.status;
  }

  public static JobPostingStatus findBy(String status) {
    return Arrays.stream(JobPostingStatus.values())
            .filter((jobPostingStatus) -> jobPostingStatus.getValue().equalsIgnoreCase(status))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("active, inactive, deleted 중 선택해주세요. 입력: " + status));
  }

}
