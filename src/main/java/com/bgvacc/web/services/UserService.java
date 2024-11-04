package com.bgvacc.web.services;

import com.bgvacc.web.enums.UserRoles;
import com.bgvacc.web.models.portal.users.UserCreateModel;
import com.bgvacc.web.responses.users.*;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface UserService {

  List<UserResponse> getUsers();

  UserResponse getUser(String cidEmail);

  boolean doUserExist(String cid);

  boolean createUser(UserCreateModel ucm);

  void updateLastLogin(String cid);

  List<RoleResponse> getAllUserRoles();

  List<RoleResponse> getUserUnassignedRoles(String cid);

  boolean addUserRole(String cid, String role);

  boolean removeUserRole(String cid, String role);

  Long getUsersCountByRole(UserRoles userRole);

  Long getUsersCountByRoles(UserRoles... userRole);
  
  UsersCount getUsersCount();
}
