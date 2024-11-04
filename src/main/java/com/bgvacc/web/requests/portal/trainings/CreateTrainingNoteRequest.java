package com.bgvacc.web.requests.portal.trainings;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CreateTrainingNoteRequest implements Serializable {

  @JsonProperty("instructor_cid")
  private Long instructorCid;

  @JsonProperty("position")
  private String position;

  @JsonProperty("note")
  private String note;

  @JsonProperty("session_type")
  private Integer sessionType;

  @JsonProperty("file")
  private String file;
}
