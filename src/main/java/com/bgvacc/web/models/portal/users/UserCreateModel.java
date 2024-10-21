package com.bgvacc.web.models.portal.users;

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
@ToString
public class UserCreateModel implements Serializable {

  private String cid;
  private String email;
  private String emailVatsim;
  private String firstName;
  private String lastName;
  private Integer currentRating;

}
