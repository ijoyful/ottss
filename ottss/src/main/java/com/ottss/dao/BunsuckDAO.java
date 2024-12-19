package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.BunsuckComDTO;
import com.ottss.domain.BunsuckDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;
import com.ottss.util.MyMultipartFile;

public class BunsuckDAO {
	private Connection conn = DBConn.getConnection();

	public void insertST(BunsuckDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			conn.setAutoCommit(false);
			// 테이블 참조하는 컬럼이 시퀀스라 시퀀스만 미리 저장
			sql = "SELECT st_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto.setSt_num(rs.getInt(1));
			}

			pstmt.close();
			pstmt = null;

			sql = "INSERT INTO show_tip_board (st_num, title, content, reg_date, mod_date, blind, hitcount, board_type, id) "
					+ "VALUES (?, ?, ?, SYSDATE, SYSDATE, 0, 0, 'showing', ? )";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, dto.getSt_num());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getId());
			// pstmt.setString(5, "ip주소");
			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;

			sql = "INSERT INTO show_tip_file (file_Num, s_fileName, c_fileName, st_num)"
					+ " VALUES (st_file_seq.NEXTVAL, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			for (MyMultipartFile m : dto.getListFile()) {
				pstmt.setString(1, m.getSaveFilename());
				pstmt.setString(2, m.getOriginalFilename());
				pstmt.setLong(3, dto.getSt_num());
				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}

		}
	}

	// 페이징 처리할때 필요
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) cnt FROM show_tip_board WHERE blind=0 AND board_type = 'showing' ";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
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

	// 페이징 처리할때 필요(schType, kwd)
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT COUNT(*) cnt " + " FROM show_tip_board s  " + " JOIN player p ON s.id = p.id "
					+ " WHERE block = 0 AND board_type = 'showing' ";
			if (schType.equals("all")) { // title 또는 content
				sql += " AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 )";
			} else if (schType.equals("reg_date")) { // reg_date
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sql += " AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else { // nickname, title, content
				sql += " AND INSTR(" + schType + ", ?) >= 1";
			}

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, kwd);
			if (schType.equals("all")) {
				pstmt.setString(2, kwd);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
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

	// 페이징 처리할때 필요
	public List<BunsuckDTO> listBoard(int offset, int size) {
		List<BunsuckDTO> list = new ArrayList<BunsuckDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT st_num, nickName, title, hitcount, ");
			sb.append("     TO_CHAR(s.reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM show_tip_board s ");
			sb.append(" JOIN player p ON s.id = p.id ");
			sb.append(" WHERE block = 0 AND board_type = 'showing'");
			sb.append(" ORDER BY st_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				BunsuckDTO dto = new BunsuckDTO();

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

	// 페이징 처리할때 필요(schType, kwd)
	public List<BunsuckDTO> listBoard(int offset, int size, String schType, String kwd) {
		List<BunsuckDTO> list = new ArrayList<BunsuckDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT st_num, nickName, title, hitcount, ");
			sb.append("     TO_CHAR(s.reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM show_tip_board s ");
			sb.append(" JOIN player p ON s.id = p.id ");
			sb.append(" WHERE block = 0 AND board_type = 'showing'");
			if (schType.equals("all")) { // title 또는 content
				sb.append(" AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 )");
			} else if (schType.equals("reg_date")) { // reg_date
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
			} else { // nickname, title, content
				sb.append(" AND INSTR(" + schType + ", ?) >= 1");
			}

			sb.append(" ORDER BY st_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

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
			while (rs.next()) {
				BunsuckDTO dto = new BunsuckDTO();

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

	// 조회수
	public void updateHitCount(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE show_tip_board SET hitCount=hitCount+1 WHERE st_num=? AND board_type = 'showing'";
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

	// 해당 게시물 보기
	public BunsuckDTO findById(long num) {
		BunsuckDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		// ++게시글 좋아요 보류
		try {
			sql = " SELECT s.st_num, s.id, nickname, title, content, s.reg_date, hitCount, blind "
					+ " FROM show_tip_board s" + " JOIN player p ON p.id=s.id" + " WHERE s.st_num = ? AND blind = 0 AND board_type = 'showing'";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BunsuckDTO();
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

	// 이전글
	public BunsuckDTO findByPrev(long num, String schType, String kwd) {
		BunsuckDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT st_num, title");
				sb.append(" FROM show_tip_board s ");
				sb.append(" JOIN player p ON s.id = p.id ");
				sb.append(" WHERE (block = 0 AND st_num > ?) AND board_type = 'showing'");
				if (schType.equals("all")) { // title 또는 content
					sb.append(" AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 )");
				} else if (schType.equals("reg_date")) { // reg_date
					kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
					sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
				} else { // nickname, title, content
					sb.append(" AND INSTR(" + schType + ", ?) >= 1");
				}
				sb.append(" ORDER BY st_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}

			} else {
				sb.append(" SELECT st_num, title");
				sb.append(" FROM show_tip_board");
				sb.append(" WHERE blind = 0 AND st_num > ?");
				sb.append(" ORDER BY st_num ASC");
				sb.append(" FETCH FIRST 1 ROWS ONLY");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new BunsuckDTO();
				dto.setSt_num(rs.getLong("st_num"));
				dto.setTitle(rs.getString("title"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 다음글
	public BunsuckDTO findByNext(long num, String schType, String kwd) {
		BunsuckDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT st_num, title");
				sb.append(" FROM show_tip_board s ");
				sb.append(" JOIN player p ON s.id = p.id ");
				sb.append(" WHERE block = 0 AND st_num < ? AND board_type = 'showing'");
				if (schType.equals("all")) { // title 또는 content
					sb.append(" AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 )");
				} else if (schType.equals("reg_date")) { // reg_date
					kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
					sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
				} else { // nickname, title, content
					sb.append(" AND INSTR(" + schType + ", ?) >= 1");
				}
				sb.append(" ORDER BY st_num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}

			} else {
				sb.append(" SELECT st_num, title");
				sb.append(" FROM show_tip_board");
				sb.append(" WHERE blind = 0 AND st_num < ?");
				sb.append(" ORDER BY st_num DESC");
				sb.append(" FETCH FIRST 1 ROWS ONLY");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				dto = new BunsuckDTO();
				dto.setSt_num(rs.getLong("st_num"));
				dto.setTitle(rs.getString("title"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 게시글 수정
	public void updateST(BunsuckDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE show_tip_board SET title = ?, content =? WHERE st_num = ? AND id = ? AND board_type = 'showing'";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setLong(3, dto.getSt_num());
			pstmt.setString(4, dto.getId());

			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}

	// 게시물 삭제
	public void deleteST(long num, String id, int powercode) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (powercode >= 99) {
				sql = "DELETE FROM show_tip_board WHERE st_num=? AND board_type = 'showing'";
				pstmt = conn.prepareStatement(sql);

				pstmt.setLong(1, num);

				pstmt.executeUpdate();
			} else {
				sql = "DELETE FROM show_tip_board WHERE st_num=? AND id=? AND board_type = 'showing'";

				pstmt = conn.prepareStatement(sql);

				pstmt.setLong(1, num);
				pstmt.setString(2, id);

				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 파일
	public List<BunsuckDTO> listSTFile(long num) {
		List<BunsuckDTO> list = new ArrayList<BunsuckDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT file_num, s_filename, c_filename, st_num FROM show_tip_file WHERE st_num = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, num);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BunsuckDTO dto = new BunsuckDTO();

				dto.setFileNum(rs.getLong("file_num"));
				dto.setS_fileName(rs.getString("s_filename"));
				dto.setC_fileName(rs.getString("c_filename"));
				dto.setSt_num(rs.getLong("st_num"));

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

	// 게시글의 댓글 저장
	public void insertReply(BunsuckComDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO SHOW_TIP_COMMENT (STC_NUM, CONTENT, REG_DATE, ID, ST_NUM) "
					+ " VALUES (STC_SEQ.NEXTVAL, ?, SYSDATE, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getContent());
			pstmt.setString(2, dto.getId());
			pstmt.setLong(3, dto.getSt_num());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 게시글의 댓글 개수
	public int dataCountReply(long num) {
		int result = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM show_tip_comment WHERE st_num =?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
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
	
	
	// 게시글의 댓글
	public List<BunsuckComDTO> listReply(long num, int offset, int size){
		List<BunsuckComDTO> list = new ArrayList<BunsuckComDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT STC_NUM, s.ID, NICKNAME, CONTENT, s.REG_DATE, ST_NUM ");
			sb.append(" FROM show_tip_comment s");
			sb.append(" JOIN player p ON s.id = p.id");
			sb.append(" WHERE st_num = ?");
			sb.append(" ORDER BY stc_num DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, num);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BunsuckComDTO dto = new BunsuckComDTO();
				dto.setStc_num(rs.getLong("stc_num"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setNickname(rs.getString("nickname"));
				dto.setId(rs.getString("id"));
				dto.setSt_num(rs.getLong("st_num"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return list;
	}
	
	public BunsuckComDTO findByCommentId(long stc_num) {
		BunsuckComDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT stc_num, nickname, content, s.reg_date, s.id, st_num"
					+ " FROM show_tip_comment s"
					+ " JOIN player p ON s.id = p.id"
					+ " WHERE stc_num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, stc_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new BunsuckComDTO();
				dto.setSt_num(rs.getLong("stc_num"));
				dto.setNickname(rs.getString("nickname"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setId(rs.getString("id"));
				dto.setSt_num(rs.getLong("st_num"));
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	
	//게시글 댓글 삭제
	public void deleteReply (long stc_num, String id, int powercode) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		if(powercode != 99) {
			BunsuckComDTO dto = findByCommentId(stc_num);
			if(dto == null || (! id.equals(dto.getId()))) {
				return;
			}
			
		}
		
		
		try {
			sql = " DELETE FROM show_tip_comment"
					+ " WHERE stc_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, stc_num);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;	
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	
	// 로그인 유저의 게시글 좋아요 유무
	public boolean isUserShowLike(long st_num, String id) {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		
		try {
			sql = "SELECT st_num,id FROM show_tip_like WHERE st_num=? AND id=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, st_num);
			pstmt.setString(2, id);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
			
		return result;
	
	}
	
	// 게시글의 좋아요 추가
	public void insertShowLike(long st_num, String id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "INSERT INTO show_tip_like (st_num, id) VALUES (?,?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, st_num);
			pstmt.setString(2, id);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}	
	
	// 게시글의 좋아요 삭제
	public void deleteShowLike(long st_num, String id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "DELETE FROM show_tip_like WHERE st_num =? AND id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, st_num);
			pstmt.setString(2, id);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}	
	
	// 게시글의 좋아요 개수
	public int countShowLike(long st_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT count(*) FROM show_tip_like WHERE st_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, st_num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return result;
	}	
	
	public void deleteShow(long[] nums) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "DELETE FROM show_tip_board WHERE st_num=? AND board_type = 'showing'";
			
			pstmt = conn.prepareStatement(sql);
			
			for(long num : nums) {
				pstmt.setLong(1, num);
				pstmt.executeQuery();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	
}
