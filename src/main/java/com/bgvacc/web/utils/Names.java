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
  
  /**
   * Gets full name with short first name.
   * 
   * e.g. John Doe - J. Doe
   * @return 
   */
  public String getFullNameWithShortFirst() {
    
    if (lastName != null) {
      return firstName.charAt(0) + ". " + lastName;
    }
    
    return firstName.charAt(0) + ".";
  }

  @Override
  public String toString() {
    return getFullName();
  }
}
