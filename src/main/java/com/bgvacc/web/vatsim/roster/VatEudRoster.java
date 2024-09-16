package com.bgvacc.web.vatsim.roster;

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
public class VatEudRoster implements Serializable {

  @JsonProperty("success")
  private boolean success;

  @JsonProperty("data")
  private VatEudRosterData data;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public static class VatEudRosterData {

    @JsonProperty("facility")
    private String facility;

    @JsonProperty("staff")
    private List<List<VatEudRosterStaff>> staff;

    @JsonProperty("controllers")
    private List<Long> controllersIds;

    @JsonIgnore
    private List<VatEudRosterController> controllers;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class VatEudRosterStaff {

      @JsonProperty("id")
      private Long id;

      @JsonProperty("cid")
      private Long cid;

      @JsonProperty("email")
      private String email;

      @JsonProperty("name")
      private String name;

      @JsonProperty("name_short")
      private String nameShort;

      @JsonProperty("role_description")
      private String roleDescription;

      @JsonProperty("facilityId")
      private Long facility_id;

      @JsonProperty("created_at")
      private Timestamp createdAt;

      @JsonProperty("updated_at")
      private Timestamp updatedAt;

      @JsonIgnore
      private String firstName;

      @JsonIgnore
      private String lastName;

      public String getFullName() {
        return (lastName != null) ? firstName + ' ' + lastName : firstName;
      }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class VatEudRosterController {

      @JsonProperty("cid")
      private Long cid;

      @JsonIgnore
      private String firstName;

      @JsonIgnore
      private String lastName;

      public String getFullName() {
        return (lastName != null) ? firstName + ' ' + lastName : firstName;
      }
    }
  }
}
