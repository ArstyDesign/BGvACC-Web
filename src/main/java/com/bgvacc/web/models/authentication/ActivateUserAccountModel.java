package com.bgvacc.web.models.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkove
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivateUserAccountModel implements Serializable {

  @NotBlank
  @Size(min = 6, max = 30)
  private String newPassword;

  @NotBlank
  @Size(min = 6, max = 30)
  private String confirmPassword;

}
