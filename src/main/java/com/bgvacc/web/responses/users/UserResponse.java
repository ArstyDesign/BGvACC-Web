package com.bgvacc.web.responses.users;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponse extends SavedSearchUser implements Serializable {

  private String password;
  private Boolean isActive;
  private Timestamp lastLogin;
  private Timestamp createdOn;
  private Timestamp editedOn;
  private Integer highestControllerRating;
  private String passwordResetToken;
  private List<RoleResponse> roles;

  public boolean isUserPasswordReset() {
    return passwordResetToken != null && !passwordResetToken.trim().isEmpty();
  }
}
