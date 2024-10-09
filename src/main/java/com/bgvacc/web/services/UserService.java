package com.bgvacc.web.services;

import com.bgvacc.web.responses.users.UserResponse;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface UserService {

  UserResponse getUser(String cidEmail);

}
