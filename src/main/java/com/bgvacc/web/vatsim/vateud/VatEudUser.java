package com.bgvacc.web.vatsim.vateud;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
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
public class VatEudUser implements Serializable {

  @JsonProperty("success")
  private boolean success;

  @JsonProperty("data")
  private VatEudUserData data;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public class VatEudUserData {

    @JsonProperty("cid")
    private String cid;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_preferences")
    private String emailPreferences;

    @JsonProperty("preferences_read")
    private Integer preferencesRead;

    @JsonProperty("facility")
    private Integer facility;

    @JsonProperty("non_members_facility")
    private Integer nonMembersFacility;

    @JsonProperty("facility_join")
    private String facilityJoin;

    @JsonProperty("rating")
    private Integer rating;

    @JsonProperty("flag_exam_bypass")
    private Integer flagExamBypass;

    @JsonProperty("gdpr_deleted")
    private Integer gdprDeleted;

    @JsonProperty("last_api_check")
    private String lastApiCheck;

    @JsonProperty("emea_transfer_eligible")
    private Integer emeaTransferEligible;

    public String getFullName() {
      return (lastName != null) ? firstName + ' ' + lastName : firstName;
    }
  }
}
