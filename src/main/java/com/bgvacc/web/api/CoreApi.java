package com.bgvacc.web.api;

import static com.bgvacc.web.api.APIConstants.VATEUD_API_KEY_HEADER_KEY;
import com.bgvacc.web.configurations.properties.AuthenticationProperties;
import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import com.bgvacc.web.vatsim.members.VatsimMemberDetails;
import com.bgvacc.web.vatsim.members.VatsimMemberSoloValidations;
import com.bgvacc.web.vatsim.members.VatsimMemberStatistics;
import com.bgvacc.web.vatsim.roster.VatEudRoster;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class CoreApi extends Api {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  public AuthenticationProperties authProps;

  public List<VatsimATC> getAllOnlineControllers() {

    final String url = "https://api.vatsim.net/v2/atc/online";

    return doRequestList(Methods.GET, url, null, VatsimATC.class);
  }

  public VatEudRoster getRoster() {

    final String url = "https://core.vateud.net/api/facility/roster";

    return doRequest(Methods.GET, url, null, VatEudRoster.class, null, VATEUD_API_KEY_HEADER_KEY, authProps.getVatEudApiKey());
  }

  public VatsimMemberDetails getMemberDetails(Long cid) {

    final String url = "https://api.vatsim.net/v2/members/" + cid;

    return doRequest(Methods.GET, url, null, VatsimMemberDetails.class);
  }

  public VatsimMemberStatistics getMemberStatistics(Long cid) {

    final String url = "https://api.vatsim.net/v2/members/" + cid + "/stats";

    return doRequest(Methods.GET, url, null, VatsimMemberStatistics.class);
  }

  public VatsimMemberSoloValidations getMemberSoloValidations() {

    final String url = "https://core.vateud.net/api/facility/endorsements/solo";

    return doRequest(Methods.GET, url, null, VatsimMemberSoloValidations.class, null, VATEUD_API_KEY_HEADER_KEY, authProps.getVatEudApiKey());
  }
}
