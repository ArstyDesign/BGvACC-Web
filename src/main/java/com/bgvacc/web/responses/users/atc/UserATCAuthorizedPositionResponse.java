package com.bgvacc.web.responses.users.atc;

import java.io.Serializable;
import java.sql.Timestamp;
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
public class UserATCAuthorizedPositionResponse implements Serializable {

  private String id;
  private String userCid;
  private String position;
  private Boolean isPositionManuallyAdded;
  private Timestamp expiresOn;
  private Timestamp createdOn;
}
