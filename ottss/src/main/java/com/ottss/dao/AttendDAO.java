package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.MemberDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class AttendDAO {
	private Connection conn = DBConn.getConnection();

	// 유저가 출석했을 때
	public void insertAttend(String id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO attend (att_date, id) VALUES (SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, id);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 한 해 월별로 출석통계 뽑아오기
	public List<MemberDTO> attendRecord(String id, String year) {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) cnt, TO_CHAR(att_date, 'YYYY-MM') att_date"
					+ " FROM attend"
					+ " WHERE id = ? AND TO_CHAR(att_date, 'YYYY') = ?"
					+ " GROUP BY TO_CHAR(att_date, 'YYYY-MM')";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, id);
			pstmt.setString(2, year);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setAttendCount(rs.getInt("cnt"));
				dto.setAtt_date(rs.getString("att_date"));

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}

	// 한달 동안의 출석 날짜
	public List<Integer> attendRecord(String id, String year, String month) {
		List<Integer> list = new ArrayList<Integer>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			// "날짜"만 select
			sql = "SELECT TO_CHAR(att_date, 'DD') att_date"
					+ " FROM attend"
					+ " WHERE id = ? AND TO_CHAR(att_date, 'YYYY-MM') = ? || '-' || ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, year);
			pstmt.setString(3, month);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String date = rs.getString("att_date");
				list.add(Integer.parseInt(date));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	} 
}
