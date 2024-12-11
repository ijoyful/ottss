package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.STDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class STDAO {
	public Connection conn = DBConn.getConnection();
	
	public void insertST(STDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO show_tip_board (st_num, title, content, reg_date, mod_date, blind, hitcount, board_type, id) "
					+ "VALUES (st_seq.NEXTVAL, ?, ?, SYSDATE, SYSDATE, 0, 0, 'showing', ? )";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getId());
			//pstmt.setString(4, "ip주소");
			
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}	

	
	
	//페이징 처리할때 필요
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) cnt FROM show_tip_board WHERE blind=0 ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return result;
	}

	//페이징 처리할때 필요(schType, kwd)
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) cnt "
					+ " FROM show_tip_board s  "
					+ " JOIN player p ON s.id = p.id "
					+ " WHERE block = 0 ";
			if(schType.equals("all")) { // title 또는 content
				sql += " AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 )";
			} else if(schType.equals("reg_date")) { //reg_date
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sql += " AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else { // nickname, title, content
				sql += " AND INSTR(" + schType + ", ?) >= 1";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			if(schType.equals("all")) {
				pstmt.setString(2, kwd);
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	
	//페이징 처리할때 필요
	public List<STDTO> listBoard(int offset, int size) {
		List<STDTO> list = new ArrayList<STDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT st_num, nickName, title, hitcount, ");
			sb.append("     TO_CHAR(s.reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM show_tip_board s ");
			sb.append(" JOIN player p ON s.id = p.id ");
			sb.append(" WHERE block = 0 ");
			sb.append(" ORDER BY st_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				STDTO dto = new STDTO();
				
				dto.setSt_num(rs.getLong("st_num"));
				dto.setNickname(rs.getString("nickname"));
				dto.setTitle(rs.getString("title"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
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


	//페이징 처리할때 필요(schType, kwd)
	public List<STDTO> listBoard(int offset, int size, String schType, String kwd) {
		List<STDTO> list = new ArrayList<STDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT st_num, nickName, title, hitcount, ");
			sb.append("     TO_CHAR(s.reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM show_tip_board s ");
			sb.append(" JOIN player p ON s.id = p.id ");
			sb.append(" WHERE block = 0 ");
			if(schType.equals("all")) { // title 또는 content
				sb.append(" AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 )");
			} else if(schType.equals("reg_date")) { //reg_date
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
			} else { // nickname, title, content
				sb.append(" AND INSTR(" + schType + ", ?) >= 1");
			}
			
			sb.append(" ORDER BY st_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			if(schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}

			rs = pstmt.executeQuery();
			while(rs.next()) {
				STDTO dto = new STDTO();
				
				dto.setSt_num(rs.getLong("st_num"));
				dto.setNickname(rs.getString("nickname"));
				dto.setTitle(rs.getString("title"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
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
	
	//조회수
	public void updateHitCount(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE show_tip_board SET hitCount=hitCount+1 WHERE st_num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	//해당 게시물 보기
	public STDTO findById(long num) {
		STDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		//++게시글 좋아요 보류
		try {
			sql = " SELECT s.st_num, s.id, nickname, title, content, reg_date, hitCount, blind "
					+ " FROM show_tip_board s"
					+ " JOIN player p ON p.id=s.id"
					+ " WHERE s.st_num = ? AND blind = 0";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new STDTO();
				dto.setSt_num(rs.getLong("st_num"));
				dto.setId(rs.getString("id"));
				dto.setNickname(rs.getString("nickname"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setBlind(rs.getInt("blind"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	



}
