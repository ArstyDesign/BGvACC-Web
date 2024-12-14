package com.bgvacc.web.models.portal.profile.settings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ChangePasswordModel implements Serializable {

  @NotBlank
  private String currentPassword;

  @NotBlank
  @Size(min = 6, max = 30)
  private String newPassword;

  @NotBlank
  @Size(min = 6, max = 30)
  private String confirmPassword;

}
