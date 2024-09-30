package com.bgvacc.web.services;

import com.bgvacc.web.domains.Controllers;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface VatsimEudRosterService {

  Controllers getRosterControllers();

  void syncRosterControllers();
}
