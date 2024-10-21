package com.bgvacc.web.services;

import com.bgvacc.web.models.portal.users.UserCreateModel;
import com.bgvacc.web.responses.users.RoleResponse;
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

  boolean createUser(UserCreateModel ucm);

  void updateLastLogin(String cid);

  List<RoleResponse> getAllUserRoles();

  List<RoleResponse> getUserUnassignedRoles(String cid);

  boolean addUserRole(String cid, String role);

  boolean removeUserRole(String cid, String role);
}
