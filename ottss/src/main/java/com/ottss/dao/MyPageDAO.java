package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.PointRecordDTO;
import com.ottss.util.DBConn;

public class MyPageDAO {
	Connection conn = DBConn.getConnection();

	// 포인트 사용 기록(싹다)
	public List<PointRecordDTO> pointrecord(String id) {
		// DTO: pt_num, categories(int), category(String), point, left_point, pt_date, id
		List<PointRecordDTO> list = new ArrayList<PointRecordDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT categories, point, left_pt, TO_CHAR(pt_date, 'YYYY-MM-DD') pt_date FROM point_record WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			while(rs.next()) {
				PointRecordDTO dto = new PointRecordDTO();
				// dto.setCategories(rs.getInt("categories"));
				dto.setCategories(rs.getInt("categories") / 10); // categories 가 1 이면 소비(-) 9이면 소득(+)
				switch(dto.getCategories()) {
				case 10: dto.setCategory("게임"); break;
				case 90: dto.setCategory("게임"); break;
				}
				dto.setPoint(rs.getInt("point"));
				dto.setLeft_point(rs.getInt("left_pt"));
				dto.setPt_date(rs.getString("pt_date"));

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
