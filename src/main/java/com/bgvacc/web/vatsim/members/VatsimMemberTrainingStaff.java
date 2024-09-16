package com.bgvacc.web.vatsim.members;

import com.bgvacc.web.vatsim.utils.VatsimRatingUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class VatsimMemberTrainingStaff implements Serializable {

  @JsonProperty("success")
  private boolean success;

  @JsonProperty("data")
  private List<VatsimMemberTrainingStaffData> data;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class VatsimMemberTrainingStaffData {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("cid")
    private Long cid;

    @JsonProperty("access_type")
    private Integer accessType;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("updated_at")
    private Timestamp updatedAt;

    @JsonIgnore
    private String firstName;

    @JsonIgnore
    private String lastName;

    @JsonIgnore
    private Integer rating;

    public String getFullName() {
      return (lastName != null) ? firstName + ' ' + lastName : firstName;
    }

    public String getAtcRatingSymbol() {
      return VatsimRatingUtils.getATCRatingSymbol(this.rating);
    }

    public String getAtcRatingName() {
      return VatsimRatingUtils.getATCRatingName(this.rating);
    }
  }
}
