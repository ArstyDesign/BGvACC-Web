package com.bgvacc.web.security;

import com.bgvacc.web.utils.Names;
import eu.bitwalker.useragentutils.UserAgent;
import java.sql.Timestamp;
import java.util.Collection;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class LoggedUser extends User {

  private String cid;
  private String email;
  private String emailVatsim;
  private Names names;
  private Boolean isActive;
  private Timestamp lastLogin;
  private Timestamp createdOn;
  private Timestamp editedOn;
  private Timestamp loggedOn;
  private String ipAddress;
  private UserAgent userAgent;

  public LoggedUser(String cid, String password, boolean enabled, boolean accountNonExpired,
                    boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                    String email, String emailVatsim, Names names, Boolean isActive, Timestamp lastLogin, Timestamp createdOn, Timestamp editedOn) {
    super(cid, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

    this.cid = cid;
    this.email = email;
    this.emailVatsim = emailVatsim;
    this.names = names;
    this.isActive = isActive;
    this.lastLogin = lastLogin;
    this.createdOn = createdOn;
    this.editedOn = editedOn;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.isActive;
  }
}
