package com.bgvacc.web.utils;

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
@Builder
public class Names implements Serializable {

  private String firstName;
  private String lastName;

  public String getFullName() {
    return (lastName != null) ? firstName + ' ' + lastName : firstName;
  }

  @Override
  public String toString() {
    return getFullName();
  }
}
