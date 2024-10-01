package com.bgvacc.web.models.authentication;

import java.io.Serializable;
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
public class LoginModel implements Serializable {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;
}
