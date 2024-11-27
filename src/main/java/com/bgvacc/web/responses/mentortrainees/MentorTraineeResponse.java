package com.bgvacc.web.responses.mentortrainees;

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
public class MentorTraineeResponse implements Serializable {

  private String mentorTraineeId;
  private String mentorCid;
  private Names mentorNames;
  private String traineeCid;
  private Names traineeNames;
  private String position;
  private Timestamp assignedAt;

}
