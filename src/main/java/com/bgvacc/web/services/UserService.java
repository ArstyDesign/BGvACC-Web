package com.bgvacc.web.services;

import com.bgvacc.web.enums.UserRoles;
import com.bgvacc.web.models.portal.users.UserCreateModel;
import com.bgvacc.web.models.portal.users.UserSearchModel;
import com.bgvacc.web.responses.users.*;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface UserService {

  List<UserResponse> getUsers();

  List<UserResponse> searchUsers(UserSearchModel search);

  UserResponse getUser(String cidEmail);

  UserResponse getUserByPasswordResetToken(String passwordResetToken);

  boolean doUserExist(String cid);

  boolean doUserExistByEmail(String email);

  boolean doPasswordMatch(String cid, String password);

  boolean isUserActive(String cid);

  String createUser(UserCreateModel ucm);

  void updateLastLogin(String cid);

  List<RoleResponse> getAllUserRoles();

  List<RoleResponse> getUserUnassignedRoles(String cid);

  boolean addUserRole(String cid, String role);

  boolean removeUserRole(String cid, String role);

  Long getUsersCountByRole(UserRoles userRole);

  Long getUsersCountByRoles(UserRoles... userRole);

  UsersCount getUsersCount();

  boolean changePassword(String cid, String password);

  boolean activateUserAccount(String cid, String password);

  List<SavedSearchUser> getUserSavedUserSearches(String cid);

  boolean addSavedUserSearch(String cid, String cidToAdd);

  boolean removeUserFromSavedSearches(String cid, String cidToRemove);
}
