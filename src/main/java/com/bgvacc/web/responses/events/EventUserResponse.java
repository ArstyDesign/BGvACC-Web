package com.bgvacc.web.responses.events;

import com.bgvacc.web.utils.Names;
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
public class EventUserResponse implements Serializable {
  
  private String cid;
  private Names names;
  private Integer highestControllerRating;

}
