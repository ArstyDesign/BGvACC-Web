package com.bgvacc.web.api;

import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import com.bgvacc.web.vatsim.members.VatsimMemberDetails;
import com.bgvacc.web.vatsim.members.VatsimMemberSoloValidation;
import com.bgvacc.web.vatsim.members.VatsimMemberSoloValidations;
import com.bgvacc.web.vatsim.members.VatsimMemberStatistics;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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

  public VatsimMemberDetails getMemberDetails(String cid) {

    final String url = "https://api.vatsim.net/v2/members/" + cid;

    return doRequest(Methods.GET, url, null, VatsimMemberDetails.class);
  }

  public VatsimMemberStatistics getMemberStatistics(String cid) {

    final String url = "https://api.vatsim.net/v2/members/" + cid + "/stats";

    return doRequest(Methods.GET, url, null, VatsimMemberStatistics.class);
  }

  public VatsimMemberSoloValidations getMemberSoloValidations() {

    VatsimMemberSoloValidations result = new VatsimMemberSoloValidations();
    result.setSuccess(true);
    
    List<VatsimMemberSoloValidation> data = new ArrayList<>();

    VatsimMemberSoloValidation vmsv = new VatsimMemberSoloValidation();
    vmsv.setId(1);
    vmsv.setUserCid(1720051);
    vmsv.setInstructorCid(1008143);
    vmsv.setPosition("LBSF_APP");
    
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2024);
    c.set(Calendar.MONTH, 8); // September (9-1)
    c.set(Calendar.DAY_OF_MONTH, 21);
    vmsv.setExpiry(new Timestamp(c.getTimeInMillis()));
    vmsv.setMaxDays(30);
    vmsv.setFacility(1);
    
    data.add(vmsv);
    
    vmsv = new VatsimMemberSoloValidation();
    vmsv.setId(2);
    vmsv.setUserCid(1724433);
    vmsv.setInstructorCid(1604267);
    vmsv.setPosition("LBSF_TWR");
    
    c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2024);
    c.set(Calendar.MONTH, 9);
    c.set(Calendar.DAY_OF_MONTH, 1);
    vmsv.setExpiry(new Timestamp(c.getTimeInMillis()));
    vmsv.setMaxDays(30);
    vmsv.setFacility(1);
    
    data.add(vmsv);
    
    vmsv = new VatsimMemberSoloValidation();
    vmsv.setId(3);
    vmsv.setUserCid(1779345);
    vmsv.setInstructorCid(1664545);
    vmsv.setPosition("LBSF_TWR");
    
    c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2024);
    c.set(Calendar.MONTH, 9);
    c.set(Calendar.DAY_OF_MONTH, 1);
    vmsv.setExpiry(new Timestamp(c.getTimeInMillis()));
    vmsv.setMaxDays(30);
    vmsv.setFacility(1);
    
    data.add(vmsv);
    
    vmsv = new VatsimMemberSoloValidation();
    vmsv.setId(4);
    vmsv.setUserCid(1704000);
    vmsv.setInstructorCid(1008143);
    vmsv.setPosition("LBSF_APP");
    
    c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2024);
    c.set(Calendar.MONTH, 8);
    c.set(Calendar.DAY_OF_MONTH, 16);
    vmsv.setExpiry(new Timestamp(c.getTimeInMillis()));
    vmsv.setMaxDays(30);
    vmsv.setFacility(1);
    
    data.add(vmsv);
    
    vmsv = new VatsimMemberSoloValidation();
    vmsv.setId(5);
    vmsv.setUserCid(1604267);
    vmsv.setInstructorCid(1008143);
    vmsv.setPosition("LBSR_CTR");
    
    c = Calendar.getInstance();
    c.set(Calendar.YEAR, 2024);
    c.set(Calendar.MONTH, 8);
    c.set(Calendar.DAY_OF_MONTH, 22);
    vmsv.setExpiry(new Timestamp(c.getTimeInMillis()));
    vmsv.setMaxDays(30);
    vmsv.setFacility(1);
    
    data.add(vmsv);
    
    result.setData(data);
    
    return result;

//    final String url = "https://core.vateud.net/api/solo";
//
//    return doRequest(Methods.GET, url, null, VatsimMemberSoloValidations.class);
  }
}
