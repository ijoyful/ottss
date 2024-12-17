package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.MemberDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class AdminMemberDAO {
	
	public Connection conn = DBConn.getConnection();
	
	public int userCount(boolean blind) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT COUNT(*) cnt FROM player");
			if(blind) sb.append(" WHERE block = 1");
			
			pstmt = conn.prepareStatement(sb.toString());
			
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
	
	public int userCount(String schType, String kwd, boolean blind) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) cnt FROM player";
			
			if (schType.equals("all")) {
				sql += " WHERE (INSTR(id, ?) >= 1 OR INSTR(name, ?) >= 1)";
			} else {
				sql += " AND INSTR(" + schType + ", ?) >= 1";
			}
			
			if(blind) {
				sql += " AND block=1";
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

	public List<MemberDTO> listMember(int offset, int size, boolean blind) {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("SELECT id, name, nickname, TO_CHAR(birth, 'YYYY-MM-DD') birth, tel1, tel2, tel3, email1, email2, TO_CHAR(reg_date,'YYYY-MM-DD') reg_date, point, powercode, block FROM player");
			if(blind) sb.append(" WHERE block = 1");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			rs = pstmt.executeQuery();
			
			while(rs.next() ) {
				MemberDTO dto = new MemberDTO();
				
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setNickName(rs.getString("nickName"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel1(rs.getString("tel1"));
				dto.setTel2(rs.getString("tel2"));
				dto.setTel3(rs.getString("tel3"));
				dto.setEmail1(rs.getString("email1"));
				dto.setEmail2(rs.getString("email2"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setPoint(rs.getInt("point"));
				dto.setPowercode(rs.getInt("powercode"));
				dto.setBlock(rs.getInt("block"));
				
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
	
	public List<MemberDTO> listMember(int offset, int size, String schType, String kwd, boolean blind) {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("SELECT id, name, nickname, TO_CHAR(birth, 'YYYY-MM-DD') birth, tel1, tel2, tel3, email1, email2, TO_CHAR(reg_date,'YYYY-MM-DD') reg_date, point, powercode, block FROM player");
			if (schType.equals("all")) {
				sb.append(" WHERE (INSTR(id, ?) >= 1 OR INSTR(name, ?) >= 1)");
			} else {
				sb.append(" AND INSTR(" + schType + ", ?) >= 1");
			}
			if(blind) sb.append(" AND block = 1");
			sb.append(" ORDER BY id DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if (schType.equals("all")) {
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
				MemberDTO dto = new MemberDTO();
				
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setNickName(rs.getString("nickName"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel1(rs.getString("tel1"));
				dto.setTel2(rs.getString("tel2"));
				dto.setTel3(rs.getString("tel3"));
				dto.setEmail1(rs.getString("email1"));
				dto.setEmail2(rs.getString("email2"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setPoint(rs.getInt("point"));
				dto.setPowercode(rs.getInt("powercode"));
				dto.setBlock(rs.getInt("block"));
				
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
	
	public int toggleUserBlind(String id, int block) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "UPDATE player SET block = ? WHERE Id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, block);
			pstmt.setString(2, id);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

}
