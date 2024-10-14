package com.bgvacc.web.responses.users;

import java.io.Serializable;
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
public class RoleResponse implements Serializable {

  private String roleName;

  @Override
  public String toString() {
    return roleName;
  }
}
