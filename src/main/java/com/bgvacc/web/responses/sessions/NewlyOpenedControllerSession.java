package com.bgvacc.web.responses.sessions;

import com.bgvacc.web.utils.Names;
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
public class NewlyOpenedControllerSession implements Serializable {

  private Long cid;
  private Names controllerNames;
  private String positionCallsign;
  private String positionName;
  private String frequency;
  private Timestamp loggedAt;

}
