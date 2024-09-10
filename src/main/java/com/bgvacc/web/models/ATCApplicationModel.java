package com.bgvacc.web.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
public class ATCApplicationModel {

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  private String cid;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String currentRating;

  @NotBlank
  private String reason;

}
