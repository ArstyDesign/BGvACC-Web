package com.bgvacc.web.filters;

import com.bgvacc.web.enums.Ordering;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public abstract class ObjFilter implements Serializable {

  private String order = Ordering.DESCENDING.getOrder();

  @JsonIgnore
  public abstract boolean isFilterEmpty();
  
  @JsonIgnore
  public boolean isSearched() {
    return !isFilterEmpty();
  }

  @JsonIgnore
  public abstract String getPagingParams();
}
