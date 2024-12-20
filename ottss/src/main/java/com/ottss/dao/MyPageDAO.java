package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.PlayRecordDTO;
import com.ottss.domain.PointRecordDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class MyPageDAO {
	Connection conn = DBConn.getConnection();

	// 포인트 사용 기록(싹다)
	public List<PointRecordDTO> pointrecord(String id, int offset, int size, String mode) {
		// DTO: pt_num, categories(int), category(String), point, left_point, pt_date, id
		List<PointRecordDTO> list = new ArrayList<PointRecordDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT categories, point, left_pt, pt_date");
			sb.append(" FROM point_record");
			sb.append(" WHERE id = ? AND point != 0");
			if (!mode.equals("all")) {
				switch(mode) {
				case "game": sb.append(" AND MOD(categories, 10) = 0"); break;
				case "shop": sb.append(" AND MOD(categories, 10) = 1"); break;
				case "used": sb.append(" AND FLOOR(categories / 10) = 1"); break;
				case "win": sb.append(" AND FLOOR(categories / 10) = 9"); break;
				}
			}
			sb.append(" ORDER BY pt_date DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();
			while(rs.next()) {
				PointRecordDTO dto = new PointRecordDTO();
				dto.setCategories(rs.getInt("categories"));
				// dto.setCategories(rs.getInt("categories") % 10); // categories 가 1 이면 소비(-) 9이면 소득(+)
				switch(dto.getCategories() % 10) {
				case 0: dto.setCategory("게임"); break;
				case 1: dto.setCategory("상점"); break;
				case 2: dto.setCategory("출석"); break;
				case 3: dto.setCategory("게시판"); break;
				case 5: dto.setCategory("회원가입"); break;
				case 6: dto.setCategory("추천인"); break;
				default: dto.setCategory("기타"); break;
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

	public int pointdataCount(String id, String mode) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT COUNT(*) FROM point_record WHERE id = ? AND point != 0");
			if (!mode.equals("all")) {
				switch(mode) {
				case "game": sb.append(" AND MOD(categories, 10) = 0"); break;
				case "shop": sb.append(" AND MOD(categories, 10) = 1"); break;
				case "used": sb.append(" AND FLOOR(categories / 10) = 1"); break;
				case "win": sb.append(" AND FLOOR(categories / 10) = 9"); break;
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

	public List<PlayRecordDTO> bestrecord(String id) {
		List<PlayRecordDTO> list = new ArrayList<PlayRecordDTO>();
		PreparedStatement pstmt = null;
		PlayRecordDTO dto = null;
		ResultSet rs = null;
		String sql;

		try {
			// game_num = 1 : 두더지 게임
			sql = "SELECT play_num, TO_CHAR(play_date, 'YYYY-MM-DD') play_date, used_point, win_point, result, game_num"
					+ " FROM play_record"
					+ " WHERE game_num = 1 AND id = ?"
					+ " ORDER BY result desc, play_date ASC"
					+ " OFFSET 0 ROWS FETCH FIRST ROW ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new PlayRecordDTO();

				dto.setPlay_num(rs.getLong("play_num"));
				dto.setPlay_date(rs.getString("play_date"));
				dto.setUsed_point(rs.getInt("used_point"));
				dto.setWin_point(rs.getInt("win_point"));
				dto.setResult(rs.getString("result"));
				dto.setGame_num(1);
			}
			list.add(dto);

			pstmt = null; rs = null; dto = null; // 내용 리셋

			// game_num = 2 : 가위바위보 게임
			sql = "SELECT play_num, TO_CHAR(play_date, 'YYYY-MM-DD') play_date, used_point, win_point, result, game_num"
					+ " FROM play_record"
					+ " WHERE game_num = 2 AND id = ?"
					+ " ORDER BY result desc, win_point desc, play_date ASC"
					+ " OFFSET 0 ROWS FETCH FIRST ROW ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new PlayRecordDTO();

				dto.setPlay_num(rs.getLong("play_num"));
				dto.setPlay_date(rs.getString("play_date"));
				dto.setUsed_point(rs.getInt("used_point"));
				dto.setWin_point(rs.getInt("win_point"));
				dto.setResult(rs.getString("result"));
				if (Integer.parseInt(dto.getResult()) > 1 && dto.getWin_point() == 0) {
					// result 에 해당하는 라운드에 진출했지만, 진출 라운드에서 패배해 포인트를 얻지 못한 경우
					String result = Integer.parseInt(dto.getResult()) - 1 + "";
					dto.setResult(result);
				}
				dto.setGame_num(2);
			}
			list.add(dto);

			pstmt = null; rs = null; dto = null; // 내용 리셋

			// game_num = 3 : 룰렛 게임
			sql = "SELECT play_num, TO_CHAR(play_date, 'YYYY-MM-DD') play_date, used_point, win_point, result, game_num"
					+ " FROM play_record"
					+ " WHERE game_num = 3 AND id = ?"
					+ " ORDER BY result desc, play_date ASC"
					+ " OFFSET 0 ROWS FETCH FIRST ROW ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new PlayRecordDTO();

				dto.setPlay_num(rs.getLong("play_num"));
				dto.setPlay_date(rs.getString("play_date"));
				dto.setUsed_point(rs.getInt("used_point"));
				dto.setWin_point(rs.getInt("win_point"));
				dto.setResult(rs.getString("result"));
				dto.setRound(rs.getString("result"));
				dto.setGame_num(3);
			}
			list.add(dto);

			pstmt = null; rs = null; dto = null; // 내용 리셋

			// game_num = 4 : 퀴즈 게임
			sql = "SELECT play_num, TO_CHAR(play_date, 'YYYY-MM-DD') play_date, used_point, win_point, result, game_num"
					+ " FROM play_record"
					+ " WHERE game_num = 4 AND id = ?"
					+ " ORDER BY result desc, play_date ASC"
					+ " OFFSET 0 ROWS FETCH FIRST ROW ONLY";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new PlayRecordDTO();

				dto.setPlay_num(rs.getLong("play_num"));
				dto.setPlay_date(rs.getString("play_date"));
				dto.setUsed_point(rs.getInt("used_point"));
				dto.setWin_point(rs.getInt("win_point"));
				dto.setResult(rs.getString("result"));
				dto.setRound(rs.getString("result"));
				dto.setGame_num(4);
			}
			list.add(dto);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}

		return list;
	}

}
