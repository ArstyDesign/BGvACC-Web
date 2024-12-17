package com.bgvacc.web.responses.users;

import com.bgvacc.web.utils.Names;
import java.io.Serializable;
import lombok.*;

/**
 *
 * @author aarshinkov
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SavedSearchUser implements Serializable {

  private String cid;
  private String email;
  private String emailVatsim;
  private Names names;

  private Boolean isSavedSearch = false;
}
