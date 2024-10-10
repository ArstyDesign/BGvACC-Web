package com.bgvacc.web.vatsim.vateud;

import com.bgvacc.web.utils.Names;
import static com.bgvacc.web.utils.Utils.convertToHtml;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
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
public class ControllerNotes implements Serializable {

  @JsonProperty("success")
  private boolean success;

  @JsonProperty("notes")
  private List<ControllerNote> notes;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class ControllerNote {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_cid")
    private Long userCid;

    @JsonProperty("instructor_cid")
    private Long instructorCid;

    @JsonProperty("facility_id")
    private Long facilityId;

    @JsonProperty("position_trained")
    private String positionTrained;

    @JsonProperty("training_note")
    private String trainingNote;

    @JsonProperty("session_type")
    private Long sessionType;

    @JsonProperty("marking_sheet")
    private String markingSheet;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("updated_at")
    private Timestamp updatedAt;

    @JsonProperty("ots_pass")
    private Long otsPass;

    @JsonIgnore
    private Names instructorName;

    public String getTrainingNoteHtml() {
      return convertToHtml(this.trainingNote);
    }
  }
}
