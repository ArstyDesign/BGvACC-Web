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
public class UserResponse implements Serializable {

  private String cid;
  private String email;
  private String emailVatsim;
  private String password;
  private String firstName;
  private String lastName;
  private Boolean isActive;
  private Timestamp lastLogin;
  private Timestamp createdOn;
  private Timestamp editedOn;
  private List<RoleResponse> roles;
}