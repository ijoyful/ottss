package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ottss.domain.PlayRecordDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class RcpDAO {
	private Connection conn = DBConn.getConnection();

	// 사용자 보유 포인트 확인
	public boolean checkPoint(String id) {
		boolean start = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int entryPoint = 10;

		try {
			sql = "SELECT point FROM player WHERE id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int point = rs.getInt("point");
				start = point >= entryPoint;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return start;
	}

	// 사용자포인트
	public int userPoint(String id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int point = 0;

		try {
			sql = "SELECT point FROM player WHERE id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				point = rs.getInt("point");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return point;
	}

	
	
	// 게임시작시 사용자 보유포인트 update(10포인트 차감), 포인트내역 insert

	// 겜시작시 10포인트 차감
	public void startGamePoint(String id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		if (checkPoint(id)) { // 포인트가 있는 경우
			try {
				sql = "UPDATE player SET point = point-10 WHERE id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}

	}

	// 포인트내역 insert (주의 포인트 차감하고 userPoint 불러야할듯)
	public void insertStPoint(PlayRecordDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		int left_pt = userPoint(dto.getId());

		try {
			sql = "INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id)"
					+ " VALUES (pt_seq.NEXTVAL, 10, ?, ?, SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getUsed_point());
			pstmt.setInt(2, left_pt);
			pstmt.setString(3, dto.getId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}

	
	
	
	// 게임종료시 포인트내역 insert, 게임플레이기록 insert, 사용자 보유포인트 update / 인자 (최종라운드, 얻은포인트)

	// 포인트내역 insert
	public void insertEndPoint(PlayRecordDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		int left_pt = userPoint(dto.getId()) + dto.getWin_point();

		try {
			sql = "INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id)"
					+ " VALUES (pt_seq.NEXTVAL, 90, ?, ?, SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getWin_point());
			pstmt.setInt(2, left_pt);
			pstmt.setString(3, dto.getId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 게임플레이기록 insert
	public void insertPlayRecord(PlayRecordDTO dto) {
		PreparedStatement pstmt = null;
		String sql;

		try {
			// 사용포인트 10 고정 가위바오보 게임넘버 2
			sql = "INSERT INTO play_record (play_num, play_date, used_point, win_point, result, id, game_num)"
					+ " VALUES (play_seq.NEXTVAL, SYSDATE, 10, ?, ?, ?, 2)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getWin_point());
			pstmt.setString(2, dto.getRound());
			pstmt.setString(3, dto.getId());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 겜끝 포인트 업데이트
	public void endGamePoint(PlayRecordDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		int point = userPoint(dto.getId()) + dto.getWin_point();
		
		try {
			sql = "UPDATE player SET point = ? WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, point);
			pstmt.setString(2, dto.getId());
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}

}
