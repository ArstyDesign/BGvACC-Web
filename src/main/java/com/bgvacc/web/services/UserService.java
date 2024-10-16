package com.bgvacc.web.services;

import com.bgvacc.web.responses.users.UserResponse;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface UserService {
  
  List<UserResponse> getUsers();

  UserResponse getUser(String cidEmail);
  
  void updateLastLogin(String cid);

}
