package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ottss.domain.MemberDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();
	
	
	public MemberDTO loginMember(String id, String pwd) { //로그인처리?
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT id, name, nickname"  //select 뭐 더 받아야할지 생각좀..
					+ " FROM player "
					+ " WHERE id = ? AND pwd = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setNickName(rs.getString("nickname"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}	
	
	
	public void insertMember(MemberDTO dto) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO palyer(id, pwd, name, nickname, birth, tel1, tel2, tel3, email1, email2, point, powercode, block) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,1)";
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getNickName());
			pstmt.setString(5, dto.getBirth());
			pstmt.setString(6, dto.getTel1());
			pstmt.setString(7, dto.getTel2());
			pstmt.setString(8, dto.getTel3());
			pstmt.setString(9, dto.getEmail1());
			pstmt.setString(10, dto.getEmail2());
			pstmt.setInt(11, dto.getPoint());
			pstmt.setInt(12, dto.getPowercode());

			pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollback(conn); //롤백왜하는지 아는사람
			
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true); //이게 뭔지 아는사람
			} catch (Exception e2) {
			}
			
		}
		
		
		
		
	}
	
	public MemberDTO findById(String id) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("SELECT id, pwd, name, nickname, TO_CHAR(birth, 'YYYY-MM-DD') birth, tel1, tel2, tel3, email1, email2, point, powercode, block");
			sb.append("FROM player");
			sb.append("WHERE id = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setId(rs.getString("id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setNickName(rs.getString("nickname"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel1(rs.getString("tel1"));
				dto.setTel2(rs.getString("tel2"));
				dto.setTel3(rs.getString("tel3"));
				dto.setEmail1(rs.getString("email1"));
				dto.setEmail2(rs.getString("email2"));
				dto.setPoint(rs.getInt("point"));
				dto.setPowercode(rs.getInt(1));
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
