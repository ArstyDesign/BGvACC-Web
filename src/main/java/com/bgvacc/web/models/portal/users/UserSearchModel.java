package com.bgvacc.web.models.portal.users;

import java.io.Serializable;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkove
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSearchModel implements Serializable {

  private String cid;
  private String name;
  private String role;

  public boolean areAllFieldsEmpty() {

    return (cid == null || cid.trim().isEmpty())
            && (name == null || name.trim().isEmpty());
  }
}
