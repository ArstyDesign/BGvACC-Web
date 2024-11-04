package com.bgvacc.web.models.portal.trainings;

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
public class CreateTrainingNoteModel implements Serializable {

  private Long userCid;
  private Long instructorCid;
  private String position;
  private String note;
  private Integer sessionType;
  private String file;
}
