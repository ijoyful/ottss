package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.PlayRecordDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class RankingDAO {
	private Connection conn = DBConn.getConnection();
	
	//게임마다 랭킹 뽑는 방법이 달라서 메서드 여러개 필요(랭킹 계산은 자바에서 하자)
	// ++ 유지보수면에서 겜 추가될때마다 랭킹 계산하는 법이 달라지니까 너무 불편한데.......>>생각좀 해보자6

	//가위바위보 게임
	public List<PlayRecordDTO> rcpRanking() {
		List<PlayRecordDTO> list = new ArrayList<PlayRecordDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT DISTINCT p.nickname, r.result, r.game_num, r.win_point, g.game_title ");
			sb.append(" FROM play_record r ");
			sb.append(" JOIN player p ON p.id = r.id ");
			sb.append(" JOIN game g ON g.game_num = r.game_num ");
			sb.append(" WHERE r.result = ( ");
			sb.append("    SELECT MAX(sub_r.result) ");
			sb.append("    FROM play_record sub_r ");
			sb.append("    WHERE sub_r.id = r.id ");
			sb.append("    AND sub_r.game_num = 2 ");
			sb.append(" ) ");
			sb.append(" AND r.game_num = 2");

			
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				PlayRecordDTO dto = new PlayRecordDTO();
				dto.setResult(rs.getString("result"));
				dto.setGame_num(rs.getInt("game_num"));
				dto.setNickname(rs.getString("nickname"));
				dto.setWin_point(rs.getInt("win_point"));
				dto.setGame_title(rs.getString("game_title"));
				
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
	
	//퀴즈 게임
		public List<PlayRecordDTO> quizRanking() {
			List<PlayRecordDTO> list = new ArrayList<PlayRecordDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT DISTINCT p.nickname, r.result, r.game_num, r.win_point, g.game_title ");
				sb.append(" FROM play_record r ");
				sb.append(" JOIN player p ON p.id = r.id ");
				sb.append(" JOIN game g ON g.game_num = r.game_num ");
				sb.append(" WHERE r.result = ( ");
				sb.append("    SELECT MAX(sub_r.result) ");
				sb.append("    FROM play_record sub_r ");
				sb.append("    WHERE sub_r.id = r.id ");
				sb.append("    AND sub_r.game_num = 4 ");
				sb.append(" ) ");
				sb.append(" AND r.game_num = 4");
 
				pstmt = conn.prepareStatement(sb.toString());
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					PlayRecordDTO dto = new PlayRecordDTO();
					dto.setResult(rs.getString("result"));
					dto.setGame_num(rs.getInt("game_num"));
					dto.setNickname(rs.getString("nickname"));
					dto.setWin_point(rs.getInt("win_point"));
					dto.setGame_title(rs.getString("game_title"));
					
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
		
		//두더지 게임
		public List<PlayRecordDTO> molRanking() {
			List<PlayRecordDTO> list = new ArrayList<PlayRecordDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT distinct r.result, r.game_num, p.nickname, r.win_point, g.game_title ");
				sb.append(" FROM play_record r ");
				sb.append(" JOIN player p ON p.id = r.id ");
				sb.append(" JOIN game g ON g.game_num = r.game_num ");
				sb.append(" WHERE r.game_num = 1 ");
				sb.append(" AND r.win_point = ( ");
				sb.append("    SELECT MAX(sub_r.win_point) ");
				sb.append("    FROM play_record sub_r ");
				sb.append("    WHERE sub_r.id = r.id ");
				sb.append("    AND sub_r.game_num = 1 ");
				sb.append(" )");

				
				pstmt = conn.prepareStatement(sb.toString());
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					PlayRecordDTO dto = new PlayRecordDTO();
					dto.setResult(rs.getString("result"));
					dto.setGame_num(rs.getInt("game_num"));
					dto.setNickname(rs.getString("nickname"));
					dto.setWin_point(rs.getInt("win_point"));
					dto.setGame_title(rs.getString("game_title"));
					
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
		
		//룰렛 게임
		public List<PlayRecordDTO> rolleteRanking() {
			List<PlayRecordDTO> list = new ArrayList<PlayRecordDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT p.nickname, SUM(r.win_point) AS total_win_point, MAX(g.game_title) AS game_title ");
				sb.append(" FROM play_record r ");
				sb.append(" JOIN player p ON p.id = r.id ");
				sb.append(" JOIN game g ON g.game_num = r.game_num ");
				sb.append(" WHERE r.game_num = 3 ");
				sb.append(" GROUP BY p.nickname");
				
				pstmt = conn.prepareStatement(sb.toString());
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					PlayRecordDTO dto = new PlayRecordDTO();
					
					dto.setNickname(rs.getString("nickname"));
					dto.setGame_title(rs.getString("game_title"));
					dto.setSum_win_point(rs.getInt("total_win_point"));
					
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
		
	
}
