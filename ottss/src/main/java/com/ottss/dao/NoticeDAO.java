package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ottss.domain.NoticeDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;
import com.ottss.util.MyMultipartFile;

public class NoticeDAO {
	
	private Connection conn = DBConn.getConnection();
	
	public void insertNotice(NoticeDTO dto) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "INSERT INTO notice_board(n_num, notice_status, title, content, hitCount, reg_date, mod_date, id, nickname)"
					+ " VALUES (n_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, SYSDATE, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setInt(2, dto.getNotice_status());
			pstmt.setString(3, dto.getContent());
			pstmt.setLong(4, dto.getHitCount());
			pstmt.setString(5, dto.getId());
			pstmt.setString(6, dto.getNickname());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getListFile().size() != 0) {
				sql = "INSERT INTO notice_file(fileNum, n_num, s_fileName, c_fileName) VALUES(n_file_seq.NEXTVAL, n_seq.CURRVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for(MyMultipartFile mf : dto.getListFile()) {
					pstmt.setLong(1, dto.getFileNum());
					pstmt.setString(2, mf.getSaveFilename());
					pstmt.setString(3, mf.getOriginalFilename());
					
					pstmt.executeUpdate(sql);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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
			
			sql = "SELECT NVL(COUNT(*), 0) FROM noice_board";
			
			pstmt = conn.prepareStatement(sql);
			
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

}
