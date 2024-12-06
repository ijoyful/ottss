package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ottss.domain.FAQDTO;
import com.ottss.util.DBConn;

public class FAQDAO {
	private Connection conn = DBConn.getConnection();

	// 질문 우선 등록, 답변은 존재하지 않으며 파일 등록!!할지 결정
	public void insertQuestion(FAQDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);
			sql = "INSERT INTO faq (faq_num, q_title, q_content, q_date, hitCount, top_fix, user_id)"
					+ " VALUES (faq_seq.NEXTVAL, ?, ?, SYSDATE, 0, 0, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getQ_title());
			pstmt.setString(2, dto.getQ_content());
			pstmt.setString(3, dto.getUser_id());

			pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			conn.setAutoCommit(true);
		}
	}
}
