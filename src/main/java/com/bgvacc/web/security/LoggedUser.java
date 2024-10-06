package com.bgvacc.web.security;

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
  private String firstName;
  private String lastName;
  private Boolean isActive;
  private Timestamp lastLogin;
  private Timestamp createdOn;
  private Timestamp editedOn;
  private Timestamp loggedOn;
  private String ipAddress;
  private UserAgent userAgent;

  public LoggedUser(String cid, String password, boolean enabled, boolean accountNonExpired,
                    boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                    String email, String emailVatsim, String firstName, String lastName, Boolean isActive, Timestamp lastLogin, Timestamp createdOn, Timestamp editedOn) {
    super(cid, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

    this.email = email;
    this.emailVatsim = emailVatsim;
    this.firstName = firstName;
    this.lastName = lastName;
    this.isActive = isActive;
    this.lastLogin = lastLogin;
    this.createdOn = createdOn;
    this.editedOn = editedOn;
  }

  public String getFullName() {
    return (lastName != null) ? firstName + ' ' + lastName : firstName;
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
