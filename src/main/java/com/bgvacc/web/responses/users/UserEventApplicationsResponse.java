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
@ToString
public class UserEventApplicationsResponse implements Serializable {

  private Long totalUserEventApplications;
  private Long approvedEventApplications;
  private Double approvedPercentage;
}
