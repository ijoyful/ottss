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
	public int insertQuestion(FAQDTO dto) throws SQLException {
		int num = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			conn.setAutoCommit(false);
			sql = "SELECT faq_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				num = rs.getInt(1);
			}
			dto.setFaq_num(num);

			pstmt = null;
			sql = "INSERT INTO faq (faq_num, q_title, q_content, question_date, hitCount, top_fix, user_id)"
					+ " VALUES (?, ?, ?, SYSDATE, 0, 0, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, dto.getFaq_num());
			pstmt.setString(2, dto.getQ_title());
			pstmt.setString(3, dto.getQ_content());
			pstmt.setString(4, dto.getUser_id());

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			if (dto.getListFile().size() != 0) {
				sql = "INSERT INTO faq_file (fileNum, s_fileName, c_fileName, faq_num)"
						+ " VALUES (faq_file_seq.NEXTVAL, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				for (MyMultipartFile file : dto.getListFile()) {
					pstmt.setString(1, file.getSaveFilename());
					pstmt.setString(2, file.getOriginalFilename());
					pstmt.setLong(3, dto.getFaq_num());

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
		return num;
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
			sql = "SELECT faq_num, p.id id, p.nickname nickname, q_title, hitCount, TO_CHAR(question_date, 'YYYY-MM-DD') question_date"
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
				dto.setQ_nickname(rs.getString("nickname"));
				dto.setQ_title(rs.getString("q_title"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setQuestion_date(rs.getString("question_date"));

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
			sb.append("SELECT faq_num, p.id id, p.nickname nickname, q_title, hitCount, TO_CHAR(question_date, 'YYYY-MM-DD') question_date");
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
				dto.setQ_nickname(rs.getString("nickname"));
				dto.setQ_title(rs.getString("q_title"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setQuestion_date(rs.getString("question_date"));
				
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
			sql = "SELECT faq_num, p.id id, p.nickname nickname, q_title, hitCount, TO_CHAR(question_date, 'YYYY-MM-DD') question_date"
					+ " FROM faq f JOIN player p ON f.user_id = p.id"
					+ " WHERE top_fix = 1"
					+ " ORDER BY faq_num DESC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				FAQDTO dto = new FAQDTO();
				dto.setFaq_num(rs.getLong("faq_num"));
				dto.setUser_id(rs.getString("id"));
				dto.setQ_nickname(rs.getString("nickname"));
				dto.setQ_title(rs.getString("q_title"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setQuestion_date(rs.getString("question_date"));
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

	public FAQDTO findByNum(long num) {
		FAQDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT faq_num, q_title, q_content, a_content, question_date, answer_date, hitCount,"
					+ " u.nickname usernickname, admin.nickname adminnickname"
					+ " FROM FAQ f JOIN player u ON f.user_id = u.id"
					+ " JOIN player admin ON f.admin_id = admin.id"
					+ " WHERE faq_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new FAQDTO();

				dto.setFaq_num(rs.getLong("faq_num"));
				dto.setQ_title(rs.getString("q_title"));
				dto.setQ_content(rs.getString("q_content"));
				dto.setA_content(rs.getString("a_content"));
				dto.setQuestion_date(rs.getString("question_date"));
				dto.setAnswer_date(rs.getString("answer_date"));
				dto.setHitCount(rs.getLong("hitCount"));
				dto.setQ_nickname(rs.getString("usernickname"));
				dto.setA_nickname(rs.getString("adminnickname"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return dto;
	}

	public void updateHitCount(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE FAQ SET hitCount = hitCount + 1 WHERE faq_num = ?";
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
	
	public FAQDTO findByPrev(long num, String schType, String kwd) {
		FAQDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT faq_num, q_title");
				sb.append(" FROM faq");
				sb.append(" WHERE (faq_num > ?)");
				if (schType.equals("all")) {
					sb.append("   AND (INSTR(q_title, ?) >= 1 OR INSTR(q_content, ?) >= 1) OR INSTR(a_content, ?) >= 1)");
				} else {
					sb.append("   AND (INSTR(" + schType + ", ?) >= 1)");
				}
				sb.append(" ORDER BY faq_num ASC");
				sb.append(" FETCH FIRST 1 ROWS ONLY");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT faq_num, q_title FROM faq");
				sb.append(" WHERE faq_num > ? ");
				sb.append(" ORDER BY faq_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new FAQDTO();
				
				dto.setFaq_num(rs.getLong("faq_num"));
				dto.setQ_title(rs.getString("q_title"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 다음글
	public FAQDTO findByNext(long num, String schType, String kwd) {
		FAQDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT faq_num, q_title");
				sb.append(" FROM faq");
				sb.append(" WHERE ( faq_num < ? ) ");
				if (schType.equals("all")) {
					sb.append("   AND (INSTR(q_title, ?) >= 1 OR INSTR(q_content, ?) >= 1 OR INSTR(a_content, ?) >= 1)) ");
				} else {
					sb.append("   AND (INSTR(" + schType + ", ?) >= 1) ");
				}
				sb.append(" ORDER BY faq_num DESC");
				sb.append(" FETCH FIRST 1 ROWS ONLY");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT faq_num, q_title FROM faq ");
				sb.append(" WHERE faq_num < ? ");
				sb.append(" ORDER BY faq_num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new FAQDTO();
				
				dto.setFaq_num(rs.getLong("faq_num"));
				dto.setQ_title(rs.getString("q_title"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	public List<FAQDTO> listFAQFile(long num) {
		List<FAQDTO> list = new ArrayList<FAQDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT fileNum, s_fileName, c_fileName, faq_num FROM faq_file WHERE faq_num = ?";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				FAQDTO dto = new FAQDTO();
				dto.setFileNum(rs.getLong("fileNum"));
				dto.setS_fileName(rs.getString("s_fileName"));
				dto.setC_fileName(rs.getString("c_fileName"));
				dto.setFaq_num(rs.getLong("faq_num"));

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
