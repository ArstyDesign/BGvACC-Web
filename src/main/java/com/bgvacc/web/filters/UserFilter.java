package com.bgvacc.web.filters;

import java.io.Serializable;
import lombok.*;

/**
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserFilter extends ObjFilter implements Serializable {

  private String cid;
  private String email;
  private String firstName;
  private String lastName;
  private String role = "all";

  @Override
  public boolean isFilterEmpty() {
    return !(cid != null || firstName != null || lastName != null);
  }

  @Override
  public String getPagingParams() {
    
    String result = "";

    if (cid != null) {
      result += "&cid=" + cid;
    }

    if (email != null) {
      result += "&email=" + email;
    }

    if (firstName != null) {
      result += "&firstName=" + firstName;
    }

    if (lastName != null) {
      result += "&lastName=" + lastName;
    }

    if (role != null && !role.equals("all")) {
      result += "&role=" + role;
    }

    return result;
  }
}
