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
			sql = "SELECT id, name, nickname, powercode, block, reg_date"
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
				dto.setPowercode(rs.getInt("powercode"));
				dto.setBlock(rs.getInt("block"));
				dto.setReg_date(rs.getString("reg_date"));
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
			sql = "INSERT INTO player(id, pwd, name, nickname, birth, tel1, tel2, tel3, email1, email2, reg_date, point, powercode, block) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,SYSDATE,1000,?,0)";
			pstmt = conn.prepareStatement(sql);

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
			pstmt.setInt(11, dto.getPowercode());

			pstmt.executeUpdate();
			pstmt = null;
			
			//포인트기록(95 회원가입)
			sql = "INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id)"
					+ " VALUES (pt_seq.NEXTVAL, 95, 1000, 1000, SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			
			pstmt.executeUpdate();
			
			
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
			DBUtil.close(pstmt);
		}
		
		
	}
	
	//추천인 db처리
	public void updateRecommend (MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			sql ="UPDATE player SET point = point+500 WHERE id = ?";
			
			pstmt = conn.prepareStatement(sql);		
			pstmt.setString(1, dto.getId());			
			pstmt.executeUpdate();
			pstmt = null;
			
			sql ="UPDATE player SET point = point+300 WHERE id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getRecommendId());
			pstmt.executeUpdate();
			pstmt = null;
			
			//포인트기록(96 추천한사람)
			sql = "INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id)"
					+ " VALUES (pt_seq.NEXTVAL, 96, 500, 1500, SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getId());
			
			pstmt.executeUpdate();
			
			
			MemberDTO recomperson = new MemberDTO();
			
			recomperson = findById(dto.getRecommendId());
			
			
			//포인트기록(96 추천받은사람)
			sql = "INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id)"
					+ " VALUES (pt_seq.NEXTVAL, 96, 300, ?, SYSDATE, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, recomperson.getPoint());
			pstmt.setString(2, dto.getRecommendId());
			
			pstmt.executeUpdate();
			
			
			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
			DBUtil.close(pstmt);
		}
		
	}
	
	
	public MemberDTO findById(String id) {// 패스워드 확인할 시 회원정보가져올때, 아이디 중복 검사할 때
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append(" SELECT id, pwd, name, nickname, TO_CHAR(birth, 'YYYY-MM-DD') birth, tel1, tel2, tel3, email1, email2, point, powercode, block, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date");
			sb.append(" FROM player");
			sb.append(" WHERE id = ?");
			
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
				dto.setPowercode(rs.getInt("powercode"));
				dto.setReg_date(rs.getString("reg_date"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return dto;
	}
	public MemberDTO findByNick(String nickName) {// 패스워드 확인할 시 회원정보가져올때, 아이디 중복 검사할 때
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append(" SELECT id, pwd, name, nickname, TO_CHAR(birth, 'YYYY-MM-DD') birth, tel1, tel2, tel3, email1, email2, point, powercode, block, TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date");
			sb.append(" FROM player");
			sb.append(" WHERE nickname = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, nickName);
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
				dto.setPowercode(rs.getInt("powercode"));
				dto.setReg_date(rs.getString("reg_date"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return dto;
	}
	
	public void updateMember(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE player SET pwd=?, birth=TO_DATE(?,'YYYY-MM-DD'), tel1=?, tel2=?, tel3=?, email1=?, email2=?  WHERE id = ?"; 	//++수정날짜 보류
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getPwd());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getTel1());
			pstmt.setString(4, dto.getTel2());
			pstmt.setString(5, dto.getTel3());
			pstmt.setString(6, dto.getEmail1());
			pstmt.setString(7, dto.getEmail2());
			pstmt.setString(8, dto.getId());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		
		
	}
	
	public void updateMemberPowerCode(String id, int powercode) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE player SET powercode=? WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, powercode);
			pstmt.setString(2, id);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	public void deleteMember(String id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM player WHERE id=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
				
	}
	
		
}
