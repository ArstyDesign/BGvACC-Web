package com.bgvacc.web.services;

import com.bgvacc.web.responses.mentortrainees.MentorTraineeResponse;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface MentorTraineeService {

  List<MentorTraineeResponse> getMentorTrainees(String mentorCid);

}
