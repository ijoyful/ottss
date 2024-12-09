package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.FreeDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class FreeDAO {
	private Connection conn = DBConn.getConnection();
	
	// 글 작성
	public void insertBoard(FreeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO free_board(fb_num, title, content, reg_date, blind, hitCount, categories, id "
					+ " VALUES(fb_seq.NEXTVAL, ?, ?, SYSDATE, 0, 0, ?, ?"; 
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getCategories());
			pstmt.setString(4, dto.getId());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM free_board WHERE block = 0";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return result;
	}
	
	public List<FreeDTO> freeList(int offset, int size) {
		List<FreeDTO> list = new ArrayList<FreeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder(); 
		
		try {
			sb.append(" SELECT fb_num, title, content, hitCount, categories, fb.id, p.nickname");
			sb.append("     TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date  ");
			sb.append(" FROM free_board fb ");
			sb.append(" JOIN player p ON fb.id = p.id ");
			sb.append(" WHERE block = 0 ");
			sb.append(" ORDER BY fb_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				FreeDTO dto = new FreeDTO();
				
				dto.setFb_num(rs.getLong("fb_num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setCategories(rs.getString("categories"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setId(rs.getString("id"));
				dto.setNickname(rs.getString("nickname"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}
	
	public FreeDTO findById(long num) {
		FreeDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT fb_num, title,  content, hitCount, categories, fb.id, p.nickname,"
					+ " TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date "
					+ " FROM free_board fb"
					+ " JOIN player p ON p.id = fb.id "
					+ " WHERE fb_num = ? AND blind = 0" ;
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1,num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new FreeDTO();
				
				dto.setFb_num(rs.getLong("fb_num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setCategories(rs.getString("categories"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setId(rs.getString("id"));
				dto.setNickname(rs.getString("nickname"));
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
