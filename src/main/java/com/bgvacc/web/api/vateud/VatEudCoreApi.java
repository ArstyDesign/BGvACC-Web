package com.bgvacc.web.api.vateud;

import static com.bgvacc.web.api.APIConstants.VATEUD_API_KEY_HEADER_KEY;
import static com.bgvacc.web.api.APIConstants.VATEUD_API_KEY_HEADER_VALUE;
import com.bgvacc.web.api.Api;
import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.vatsim.members.VatsimMemberTrainingStaff;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class VatEudCoreApi extends Api {

  private final Logger log = LoggerFactory.getLogger(getClass());

  public VatEudUser getMemberDetails(Long cid) {

    final String url = "https://core.vateud.net/api/facility/user/" + cid;

    return doRequest(Methods.GET, url, null, VatEudUser.class, null, VATEUD_API_KEY_HEADER_KEY, VATEUD_API_KEY_HEADER_VALUE);
  }

  public VatsimMemberTrainingStaff getTrainingStaff() {

    final String url = "https://core.vateud.net/api/facility/training/staff";

    return doRequest(Methods.GET, url, null, VatsimMemberTrainingStaff.class, null, VATEUD_API_KEY_HEADER_KEY, VATEUD_API_KEY_HEADER_VALUE);
  }
}
