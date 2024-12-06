package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.FAQDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;
import com.ottss.util.MyMultipartFile;

public class FAQDAO {
	private Connection conn = DBConn.getConnection();

	// 질문 우선 등록, 답변은 존재하지 않으며 파일 등록!!할지 결정
	public void insertQuestion(FAQDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);
			sql = "INSERT INTO faq (faq_num, q_title, q_content, q_date, hitCount, top_fix, user_id)"
					+ " VALUES (faq_seq.NEXTVAL, ?, ?, SYSDATE, 0, 0, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getQ_title());
			pstmt.setString(2, dto.getQ_content());
			pstmt.setString(3, dto.getUser_id());

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			if (dto.getListFile().size() != 0) {
				sql = "INSERT INTO faq_file (fileNum, s_fileName, c_fileName, faq_num)"
						+ " VALUES (faq_file_seq.NEXTVAL, ?, ?, faq_seq.CURRVAL)";
				pstmt = conn.prepareStatement(sql);
				for (MyMultipartFile file : dto.getListFile()) {
					pstmt.setString(1, file.getSaveFilename());
					pstmt.setString(2, file.getOriginalFilename());

					pstmt.executeUpdate();
				}
			}

			conn.commit();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			conn.setAutoCommit(true);
		}
	}

	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM FAQ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
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

	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT NVL(COUNT(*), 0) FROM FAQ f JOIN player p ON f.user_id = p.id");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(q_title, ?) >= 1 OR INSTR(q_content, ?) >= 1 OR INSTR(a_content, ?) >= 1");
			} else {
				sb.append(" WEHRE INSTR(" + schType + ", ?) >= 1");
			}
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, kwd);
			if (schType.equals("all")) {
				pstmt.setString(2, kwd);
				pstmt.setString(3, kwd);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
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

	// faq 게시글 리스트
	public List<FAQDTO> listFAQ(int offset, int size) {
		List<FAQDTO> list = new ArrayList<FAQDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT faq_num, p.id id, p.nickname nickname, q_title, hitCount, a_date"
					+ " FROM faq f JOIN player p ON f.user_id = p.id"
					+ " ORDER BY faq_num DESC"
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				FAQDTO dto = new FAQDTO();

				dto.setFaq_num(rs.getLong("faq_num"));
				dto.setUser_id(rs.getString("id"));
				dto.setA_nickname(rs.getString("nickname"));
				dto.setQ_title(rs.getString("q_title"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setA_date("a_date");

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

	public List<FAQDTO> listFAQ(int offset, int size, String schType, String kwd) {
		List<FAQDTO> list = new ArrayList<FAQDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT faq_num, p.id id, p.nickname nickname, q_title, hitCount, a_date");
			sb.append(" FROM faq f JOIN player p ON f.user_id = p.id");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(q_title, ?) >= 1 OR INSTR(q_content, ?) >= 1 OR INSTR(a_content, ?) >= 1");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1");
			}
			sb.append(" ORDER BY faq_num DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			pstmt = conn.prepareStatement(sb.toString());

			if (schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
				pstmt.setString(3, kwd);
				pstmt.setInt(4, offset);
				pstmt.setInt(5, size);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);				
			}
				
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				FAQDTO dto = new FAQDTO();
				
				dto.setFaq_num(rs.getLong("faq_num"));
				dto.setUser_id(rs.getString("id"));
				dto.setA_nickname(rs.getString("nickname"));
				dto.setQ_title(rs.getString("q_title"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setA_date("a_date");
				
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

	public List<FAQDTO> listFAQ() {
		List<FAQDTO> list = new ArrayList<FAQDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT faq_num, p.id id, p.nickname nickname, q_title, hitCount, a_date"
					+ " FROM faq f JOIN player p ON f.user_id = p.id"
					+ " WHERE top_fix = 1"
					+ " ORDER BY faq_num DESC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				FAQDTO dto = new FAQDTO();
				dto.setFaq_num(rs.getLong("faq_num"));
				dto.setUser_id(rs.getString("id"));
				dto.setA_nickname(rs.getString("nickname"));
				dto.setQ_title(rs.getString("q_title"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setA_date(rs.getString("a_date"));
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
