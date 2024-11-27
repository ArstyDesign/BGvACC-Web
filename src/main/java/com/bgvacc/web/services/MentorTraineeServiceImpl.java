package com.bgvacc.web.services;

import com.bgvacc.web.responses.atc.ATCReservationResponse;
import com.bgvacc.web.responses.mentortrainees.MentorTraineeResponse;
import com.bgvacc.web.utils.Names;
import java.sql.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class MentorTraineeServiceImpl implements MentorTraineeService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<MentorTraineeResponse> getMentorTrainees(String mentorCid) {

    final String getMentorTraineesSql = "SELECT mt.mentor_trainee_id, mt.mentor_cid mentor_cid, mu.first_name mentor_first_name, mu.last_name mentor_last_name, mt.trainee_cid, tu.first_name trainee_first_name, tu.last_name trainee_last_name, mt.position_id, mt.assigned_at FROM mentor_trainees mt JOIN users mu ON mt.mentor_cid = mu.cid JOIN users tu ON mt.trainee_cid = tu.cid WHERE mt.mentor_cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getMentorTraineesPstmt = conn.prepareStatement(getMentorTraineesSql)) {

      try {

//        conn.setAutoCommit(false);
        getMentorTraineesPstmt.setString(1, mentorCid);

        List<MentorTraineeResponse> mentorTrainees = new ArrayList<>();

        ResultSet getMentorTraineesRset = getMentorTraineesPstmt.executeQuery();

        while (getMentorTraineesRset.next()) {

          MentorTraineeResponse mtr = getMentorTrainee(getMentorTraineesRset);

          mentorTrainees.add(mtr);
        }

        return mentorTrainees;

      } catch (SQLException ex) {
        log.error("Error getting mentor trainees for mentor with CID '" + mentorCid + "'.", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting mentor trainees for mentor with CID '" + mentorCid + "'.", e);
    }

    return null;
  }

  private MentorTraineeResponse getMentorTrainee(ResultSet rset) throws SQLException {

    MentorTraineeResponse mtr = new MentorTraineeResponse();
    mtr.setMentorTraineeId(rset.getString("mentor_trainee_id"));

    mtr.setMentorCid(rset.getString("mentor_cid"));

    String mentorFirstName = rset.getString("mentor_first_name");
    String mentorLastName = rset.getString("mentor_last_name");

    Names mentorNames = Names.builder().firstName(mentorFirstName).lastName(mentorLastName).build();
    mtr.setMentorNames(mentorNames);

    mtr.setTraineeCid(rset.getString("trainee_cid"));

    String traineeFirstName = rset.getString("trainee_first_name");
    String traineeLastName = rset.getString("trainee_last_name");

    Names traineeNames = Names.builder().firstName(traineeFirstName).lastName(traineeLastName).build();
    mtr.setTraineeNames(traineeNames);

    mtr.setPosition(rset.getString("position_id"));
    mtr.setAssignedAt(rset.getTimestamp("assigned_at"));

    return mtr;
  }
}
