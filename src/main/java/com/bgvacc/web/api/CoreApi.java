package com.bgvacc.web.api;

import static com.bgvacc.web.api.APIConstants.VATEUD_API_KEY_HEADER_KEY;
import static com.bgvacc.web.api.APIConstants.VATEUD_API_KEY_HEADER_VALUE;
import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import com.bgvacc.web.vatsim.members.VatsimMemberDetails;
import com.bgvacc.web.vatsim.members.VatsimMemberSoloValidations;
import com.bgvacc.web.vatsim.members.VatsimMemberStatistics;
import com.bgvacc.web.vatsim.roster.VatEudRoster;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class CoreApi extends Api {

  public List<VatsimATC> getAllOnlineControllers() {

//    List<VatsimATC> fakeResult = new ArrayList<>();
//
//    VatsimATC v1 = new VatsimATC();
//    v1.setId(1739874l);
//    v1.setCallsign("EWR_TWR");
//    v1.setServer("VIRTUALNAS");
//    v1.setRating(3);
//
//    fakeResult.add(v1);
//
//    VatsimATC v2 = new VatsimATC();
//    v2.setId(1248644l);
//    v2.setCallsign("CLT_TWR");
//    v2.setServer("VIRTUALNAS");
//    v2.setRating(3);
//
//    fakeResult.add(v2);
//
//    VatsimATC v3 = new VatsimATC();
//    v3.setId(1486768l);
//    v3.setCallsign("RFD_E_APP");
//    v3.setServer("VIRTUALNAS");
//    v3.setRating(5);
//
//    fakeResult.add(v3);
//
//    VatsimATC v4 = new VatsimATC();
//    v4.setId(1604267l);
//    v4.setCallsign("LBSR_CTR");
//    v4.setServer("GERMANY");
//    v4.setRating(5);
//
//    fakeResult.add(v4);
//
//    VatsimATC v5 = new VatsimATC();
//    v5.setId(1720051l);
//    v5.setCallsign("LBSF_APP");
//    v5.setServer("GERMANY");
//    v5.setRating(3);
//
//    fakeResult.add(v5);
//    
//    VatsimATC v6 = new VatsimATC();
//    v6.setId(1773453l);
//    v6.setCallsign("LBSF_TWR");
//    v6.setServer("GERMANY");
//    v6.setRating(2);
//
//    fakeResult.add(v6);
//
//    return fakeResult;
    final String url = "https://api.vatsim.net/v2/atc/online";

    return doRequestList(Methods.GET, url, null, VatsimATC.class);
  }

  public VatEudRoster getRoster() {

    final String url = "https://core.vateud.net/api/facility/roster";

    return doRequest(Methods.GET, url, null, VatEudRoster.class, null, VATEUD_API_KEY_HEADER_KEY, VATEUD_API_KEY_HEADER_VALUE);
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

    return doRequest(Methods.GET, url, null, VatsimMemberSoloValidations.class, null, VATEUD_API_KEY_HEADER_KEY, VATEUD_API_KEY_HEADER_VALUE);
  }
}
