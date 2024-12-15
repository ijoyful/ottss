package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			
			sql = "INSERT INTO notice_board(n_num, notice_status, title, content, hitCount, reg_date, mod_date, id)"
					+ " VALUES (n_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, SYSDATE, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNotice_status());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setLong(4, dto.getHitCount());
			pstmt.setString(5, dto.getId());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(dto.getListFile().size() != 0) {
				sql = "INSERT INTO notice_file(file_num, n_num, s_fileName, c_fileName) VALUES(n_file_seq.NEXTVAL, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for(MyMultipartFile mf : dto.getListFile()) {
					pstmt.setLong(1, dto.getN_num());
					pstmt.setString(2, mf.getSaveFilename());
					pstmt.setString(3, mf.getOriginalFilename());
					
					pstmt.executeUpdate();
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
			
			sql = "SELECT NVL(COUNT(*), 0) FROM notice_board";
			
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

	public int dataCount(String schType, String kwd) {
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "SELECT NVL(COUNT(*), 0) FROM notice_board n"
					+ " JOIN player p ON p.id = n.id";
			if(schType.equals("all")) {
				sql += " WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += " WHERE INSTR(" + schType + ", ?) >= 1";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			
			if(schType.equals("all")) {
				pstmt.setString(2, kwd);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
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
	
	public List<NoticeDTO> listNotice(int offset, int size) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("SELECT N_NUM, TITLE, CONTENT, nickname, HITCOUNT, n.ID, n.REG_DATE, MOD_DATE");
			sb.append(" FROM notice_board n");
			sb.append(" JOIN player p ON p.id = n.id ");
			sb.append(" ORDER BY N_NUM DESC");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setN_num(rs.getLong("n_num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setNickname(rs.getString("nickname"));
				dto.setHitCount(rs.getInt("hitcount"));
				dto.setId(rs.getString("id"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setMod_date(rs.getString("mod_date"));
				
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
	
	public List<NoticeDTO> listNotice(int offset, int size, String schType, String kwd) {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("SELECT N_NUM, TITLE, CONTENT, HITCOUNT, n.ID, nickname, n.REG_DATE, MOD_DATE");
			sb.append(" FROM notice_board n");
			sb.append(" JOIN player p ON p.id = n.id ");
			
			if(schType.equals("all")) {
				sb.append(" WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("n.reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			
			sb.append(" ORDER BY N_NUM DESC");
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
				NoticeDTO dto = new NoticeDTO();
				
				dto.setN_num(rs.getLong("n_num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitcount"));
				dto.setId(rs.getString("id"));
				dto.setNickname(rs.getString("nickname"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setMod_date(rs.getString("mod_date"));
				
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
	
	public List<NoticeDTO> listNotice() {
		List<NoticeDTO> list = new ArrayList<NoticeDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("SELECT N_NUM, TITLE, CONTENT, HITCOUNT, n.ID, nickname, TO_CHAR(n.REG_DATE, 'YYYY-MM-DD') REG_DATE, TO_CHAR(MOD_DATE, 'YYYY-MM-DD') MOD_DATE");
			sb.append(" FROM notice_board n");
			sb.append(" JOIN player p ON p.id = n.id ");
			sb.append(" WHERE NOTICE_STATUS = 1");
			sb.append(" ORDER BY N_NUM DESC");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setN_num(rs.getLong("n_num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitcount"));
				dto.setId(rs.getString("id"));
				dto.setNickname(rs.getString("nickname"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setMod_date(rs.getString("mod_date"));
				
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
	
	public NoticeDTO findById(long num) {
		
		NoticeDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "SELECT N_NUM, TITLE, CONTENT, HITCOUNT, n.ID, nickname, n.REG_DATE, MOD_DATE"
					+ " FROM notice_board n"
					+ " JOIN player p ON p.id = n.id"
					+ " WHERE n_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				
				dto.setN_num(rs.getLong("n_num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setId(rs.getString("id"));
				dto.setNickname(rs.getString("nickname"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setMod_date(rs.getString("mod_date"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
		
	}
	
	public NoticeDTO findByPrev(long num, String schType, String kwd) {
		
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			if(kwd != null && kwd.length() != 0) {
				sb.append("SELECT n_num, title"
						+ " FROM noitce_board n"
						+ " JOIN player p ON n.id = p.id"
						+ " WHERE (n_num > ?)");
				if (schType.equals("all")) {
					sb.append(" AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1)");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append(" AND (TO_CHAR(reg_date, 'YYYYMMDD') = ?)");
				} else {
					sb.append(" AND (INSTR(" + schType + ", ?) >= 1)");
				}
				sb.append(" ORDER BY n_num ASC"
						+ " FETCH FIRST 1 ROWS ONLY");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if(schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
				
			} else {
				sb.append(" SELECT n_num, title FROM notice_board"
						+ "	WHERE n_num > ?"
						+ " ORDER BY n_num ASC"
						+ " FETCH FIRST 1 ROWS ONLY");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				
				dto.setN_num(rs.getLong("n_num"));
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
	
	public NoticeDTO findByNext(long num, String schType, String kwd) {
		
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			if(kwd != null && kwd.length() != 0) {
				sb.append("SELECT n_num, title"
						+ " FROM noitce_board n"
						+ " JOIN player p ON n.id = p.id"
						+ " WHERE (n_num < ?)");
				if (schType.equals("all")) {
					sb.append(" AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1)");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append(" AND (TO_CHAR(reg_date, 'YYYYMMDD') = ?)");
				} else {
					sb.append(" AND (INSTR(" + schType + ", ?) >= 1)");
				}
				sb.append(" ORDER BY n_num DESC"
						+ " FETCH FIRST 1 ROWS ONLY");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if(schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
				
			} else {
				sb.append(" SELECT n_num, title FROM notice_board"
						+ "	WHERE n_num < ?"
						+ " ORDER BY n_num DESC"
						+ " FETCH FIRST 1 ROWS ONLY");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				
				dto.setN_num(rs.getLong("n_num"));
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
	
	public void updateHitCount(long num) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "UPDATE notice_board SET hitCount = hitCount + 1 WHERE n_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	public void updateNotice(NoticeDTO dto) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "UPDATE notice_board SET notice_status = ?, title = ?, content = ?, mod_date = SYSDATE WHERE n_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, dto.getNotice_status());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setLong(4, dto.getN_num());
			
			pstmt.executeQuery();
			
			pstmt.close();
			pstmt = null;
			
			if (dto.getListFile() != null && dto.getListFile().size() != 0) {
				sql = "INSERT INTO notice_file(FILE_NUM, N_NUM, S_FILENAME, C_FILENAME) VALUES (n_file_seq.NEXTVAL, ?, ?, ?)";
				
				pstmt = conn.prepareStatement(sql);
				
				for(MyMultipartFile mf : dto.getListFile()) {
					pstmt.setLong(1, dto.getN_num());
					pstmt.setString(2, mf.getSaveFilename());
					pstmt.setString(3, mf.getOriginalFilename());
					
					pstmt.executeQuery();
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	public void deleteNotice(long num) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "DELETE FROM notice_board WHERE n_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	public void deleteNotice(long[] nums) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "DELETE FROM notice_board WHERE n_num = ?";
			
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
	
	public List<NoticeDTO> listNoticeFile(long num) {
		List<NoticeDTO> list = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "SELECT FILE_NUM, N_NUM, S_FILENAME, C_FILENAME FROM notice_file WHERE n_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				
				dto.setFileNum(rs.getLong("file_num"));
				dto.setN_num(rs.getLong("n_num"));
				dto.setS_fileName(rs.getString("s_filename"));
				dto.setC_fileName(rs.getString("c_filename"));
				
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
	
	public NoticeDTO findByFileId(long fileNum) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "SELECT FILE_NUM, N_NUM, S_FILENAME, C_FILENAME FROM notice_file WHERE FILE_NUM = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, fileNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new NoticeDTO();
				
				dto.setFileNum(rs.getLong("file_num"));
				dto.setN_num(rs.getLong("n_num"));
				dto.setS_fileName(rs.getString("s_filename"));
				dto.setC_fileName(rs.getString("c_filename"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	public void deleteNoticeFile(String mode, long num) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			if(mode.equals("all")) {
				sql = "DELETE FROM notice_file WHERE n_num = ?";
			} else {
				sql = "DELETE FROM notice_file WHERE FILE_NUM = ?";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
}
