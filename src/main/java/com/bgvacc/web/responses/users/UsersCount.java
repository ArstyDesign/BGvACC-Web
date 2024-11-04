package com.bgvacc.web.responses.users;

import java.io.Serializable;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@ToString
public class UsersCount implements Serializable {

  private final Long totalUsers;
  private final Long activeUsers;
  private final Long inactiveUsers;

  public UsersCount(Long totalUsers, Long activeUsers) {

    if (totalUsers == null || totalUsers < 0) {
      this.totalUsers = 0L;
    } else {
      this.totalUsers = totalUsers;
    }

    if (activeUsers == null || activeUsers < 0) {
      this.activeUsers = 0L;
    } else {
      this.activeUsers = activeUsers;
    }

    this.inactiveUsers = this.totalUsers - this.activeUsers;
  }

  public Long getTotalUsers() {
    return totalUsers;
  }

  public Long getActiveUsers() {
    return activeUsers;
  }

  public Long getInactiveUsers() {
    return inactiveUsers;
  }
}
