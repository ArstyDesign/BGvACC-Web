package com.bgvacc.web.requests.atc;

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
public class ATCApplicationRequest implements Serializable {

  private String firstName;
  private String lastName;
  private String cid;
  private String email;
  private String currentRating;
  private String reason;

}
