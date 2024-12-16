package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hs.util.DBConn;
import com.ottss.domain.QuizPlayDTO;
import com.ottss.util.DBUtil;

public class QuizDAO {
	private Connection conn = DBConn.getConnection();
	
	public int leftPoint(String id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql;
		
		try {
			sql = "SELECT point FROM player WEHRE id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return result;
	}
	
	public void startGame(QuizPlayDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		int left_pt = 0;
		
		try {
			left_pt = leftPoint(dto.getId()) - dto.getUsed_point();
			
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO point_record(pt_num, categories, point, left_pt, pt_date, id) "
					+ " VALUES(pt_seq.NEXTVAL, 10, ?, ?, SYSDATE, ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getUsed_point());
			pstmt.setInt(2, left_pt);
			pstmt.setString(3, dto.getId());
			
			pstmt.executeUpdate();
			
			pstmt = null;
			
			sql = " UPDATE player SET point = ? WHERE id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, left_pt);
			pstmt.setString(2, dto.getId());
			pstmt.executeUpdate();
			
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollback(conn);
			e.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
			DBUtil.close(pstmt);
		}
	}
	
	public void endGame(QuizPlayDTO dto) {
		PreparedStatement pstmt = null;
		String sql;
		int left_pt = 0;
	

		try {
			left_pt = leftPoint(dto.getId()) + dto.getWin_point(); // 게임을 통해 얻은 포인트를 추가한 후 남은 포인트
			conn.setAutoCommit(false);

			// point_record 에 얻은 포인트 저장: categories 컬럼 값 90
			sql = "INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id)"
					+ " VALUES (pt_seq.NEXTVAL, 90, ?, ?, SYSDATE, ?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getWin_point()); // 얻은 포인트
			pstmt.setInt(2, left_pt);
			pstmt.setString(3, dto.getId());
			pstmt.executeUpdate();

			pstmt = null;
			// play_record 에 이번에 플레이한 게임 정보 저장
			sql = "INSERT INTO play_record (play_num, play_date, used_point, win_point, result, id, game_num)"
					+ " VALUES (play_seq.NEXTVAL, SYSDATE, 10, ?, ?, ?, 4)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getUsed_point()); // 이번 게임에 사용한 포인트(룰렛 제외 10포인트 기본, 룰렛은 건 만큼)
			pstmt.setInt(2, dto.getWin_point()); // 이번 게임을 통해서 얻은 포인트
			pstmt.setString(3, dto.getResult()); // 게임 결과값. ex) 룰렛: 얻은 배수 결과, 가위바위보: 생존 라운드
			pstmt.setString(4, dto.getId());
			pstmt.setInt(5, dto.getGame_num()); // 플레이한 게임 번호. 두더지: 1 / 가위바위보: 2 / 룰렛: 3 / 퀴즈: 4
			pstmt.executeUpdate();

			pstmt = null;
			// player 테이블에 point 컬럼 업데이트
			sql = " UPDATE player SET point = ? WHERE id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, left_pt);
			pstmt.setString(2, dto.getId());
			pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			e.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
			DBUtil.close(pstmt);
		}
	}

}
	