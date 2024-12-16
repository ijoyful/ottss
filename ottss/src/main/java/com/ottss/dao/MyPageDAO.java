package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.PointRecordDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class MyPageDAO {
	Connection conn = DBConn.getConnection();

	// 포인트 사용 기록(싹다)
	public List<PointRecordDTO> pointrecord(String id, int offset, int size) {
		// DTO: pt_num, categories(int), category(String), point, left_point, pt_date, id
		List<PointRecordDTO> list = new ArrayList<PointRecordDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT categories, point, left_pt, pt_date"
					+ " FROM point_record"
					+ " WHERE id = ?"
					+ " ORDER BY pt_date DESC"
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();
			while(rs.next()) {
				PointRecordDTO dto = new PointRecordDTO();
				// dto.setCategories(rs.getInt("categories"));
				dto.setCategories(rs.getInt("categories") / 10); // categories 가 1 이면 소비(-) 9이면 소득(+)
				switch(dto.getCategories()) {
				case 0: dto.setCategory("게임"); break;
				case 1: dto.setCategory("상점"); break;
				case 2: dto.setCategory("출석"); break;
				case 3: dto.setCategory("게시판"); break;
				}
				dto.setPoint(rs.getInt("point"));
				dto.setLeft_point(rs.getInt("left_pt"));
				dto.setPt_date(rs.getString("pt_date"));

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}

		return list;
	}

	public int dataCount(String id, String mode) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT COUNT(*) FROM point_record WHERE id = ?");
			if (!mode.equals("all")) {
				switch(mode) {
				case "game": sb.append(" AND MOD(categories, 10) = 0");
				case "shop": sb.append(" AND MOD(categories, 10) = 1");
				case "used": sb.append(" AND FLOOR(categories / 10) = 1");
				case "win": sb.append(" AND FLOOR(categories / 10) = 9");
				}
			}
			
			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}

		return result;
	}

}
