package com.bgvacc.web.responses.sessions;

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
public class NotCompletedControllerSession implements Serializable {

  private String controllerOnlineId;
  private String cid;
  private String position;

}
