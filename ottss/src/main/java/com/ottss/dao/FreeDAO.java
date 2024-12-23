package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.FreeComDTO;
import com.ottss.domain.FreeDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;
import com.ottss.util.MyMultipartFile;

public class FreeDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertBoard(FreeDTO dto) throws SQLException {
		int fb_num = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			conn.setAutoCommit(false);
			sql = "SELECT fb_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				fb_num = rs.getInt(1);
			}
			dto.setFb_num(fb_num);

			pstmt = null;
			sql = "INSERT INTO free_board (fb_num, title, categories ,content, reg_date, hitCount, id)"
					+ " VALUES (fb_seq.NEXTVAL, ?, '잡담', ?, SYSDATE, 0, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getTitle());
			
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getId());

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			if (dto.getListFile().size() != 0) {
				sql = "INSERT INTO free_file (file_Num, s_fileName, c_fileName, fb_num)"
						+ " VALUES (fb_file_seq.NEXTVAL, ?, ?, fb_seq.CURRVAL)";
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
		return fb_num;
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM free_board WHERE blind = 0";
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
	
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) cnt "
					+ " FROM free_board fb"
					+ " JOIN player p ON fb.id = p.id "
					+ " WHERE blind = 0;";
			if(schType.equals("all") ) {
				sql += "AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1)";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sql += " AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += " AND INSTR(" + schType + ", ? ) >= 1";
			}
				
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,kwd);
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
	
	public List<FreeDTO> freeList(int offset, int size) {
		List<FreeDTO> list = new ArrayList<FreeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder(); 
		
		try {
			sb.append(" SELECT fb.fb_num, fb.title, fb.content, fb.hitCount, fb.categories, fb.id, p.nickname, ");
			sb.append("     TO_CHAR(fb.reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM free_board fb ");
			sb.append(" JOIN player p ON fb.id = p.id ");
			sb.append(" WHERE blind = 0 ");
			sb.append(" ORDER BY reg_date DESC, fb.fb_num DESC ");
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
	
	public List<FreeDTO> freeList(int offset, int size, String schType, String kwd) {
		List<FreeDTO> list = new ArrayList<FreeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT fb_num, fb.id, p.nickname, fb.title, fb.categories, fb.hitCount ");
			sb.append(" TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM free_board fb");
			sb.append(" JOIN player p ON fb.id = p.id ");
			sb.append(" WHERE blind = 0 ");
			if(schType.equals("all")) {
				sb.append(" AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
			} else if(schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append(" AND INSTR(" + schType + ", ?) >= 1");
			}
			sb.append(" ORDER BY fb_num DESC ");
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
				FreeDTO dto = new FreeDTO();
				dto.setFb_num(rs.getLong("fb_num"));
				dto.setTitle(rs.getString("title"));
				dto.setCategories(rs.getString("categories"));
				dto.setId(rs.getString("id"));
				dto.setNickname(rs.getString("nickname"));
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
	
	// 조회수 업데이트
	public void updateHitCount(long fb_num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE free_board SET hitCount=hitCount+1 WHERE fb_num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, fb_num);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	// 해당 게시물 보기
	public FreeDTO findById(long num) {
		FreeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT fb_num, title, content, hitCount, categories, fb.id, p.nickname, "
					+ " TO_CHAR(fb.reg_date, 'YYYY-MM-DD') reg_date, fb.blind  "
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
	
	public FreeDTO findByNext(long fb_num, String schType, String kwd) {
		FreeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(kwd != null && kwd.length() != 0) {
				sb.append(" SELECT fb_num, title ");
				sb.append(" FROM free_board ");
				sb.append(" WHERE fb_num > ? ");
				if(schType.equals("all")) {
					sb.append("	AND(INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
				} else {
					sb.append("	AND(INSTR(" + schType + ", ?) >= 1)");
				}
				sb.append(" ORDER BY fb_num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, fb_num);
				pstmt.setString(2, kwd);
				if(schType.equals("all")) {
					pstmt.setString(3,kwd);
				}
					
				} else {
					sb.append(" SELECT fb_num, title FROM free_board ");
					sb.append(" WHERE fb_num > ?");
					sb.append(" ORDER BY fb_num ASC");
					sb.append(" FETCH FIRST 1 ROWS ONLY");
					
					pstmt = conn.prepareStatement(sb.toString());
					
					pstmt.setLong(1, fb_num);
				}
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					dto = new FreeDTO();
					dto.setFb_num(rs.getLong("fb_num"));
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
	
	public FreeDTO findByPrev(long num, String schType, String kwd) {
		FreeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT fb_num, title");
				sb.append(" FROM free_board");
				sb.append(" WHERE ( fb_num < ? ) ");
				if (schType.equals("all")) {
					sb.append("   AND (INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1) ");
				} else {
					sb.append("   AND (INSTR(" + schType + ", ?) >= 1) ");
				}
				sb.append(" ORDER BY fb_num DESC");
				sb.append(" FETCH FIRST 1 ROWS ONLY");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT fb_num, title FROM free_board ");
				sb.append(" WHERE fb_num < ? ");
				sb.append(" ORDER BY fb_num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new FreeDTO();
				
				dto.setFb_num(rs.getLong("fb_num"));
				dto.setTitle(rs.getString("title"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	// 파일 리스트 
	public List<FreeDTO> listFreeFile(long fb_num){
		List<FreeDTO> list = new ArrayList<FreeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT file_Num, s_fileName, c_fileName, fb_num "
					+ " FROM free_file "
					+ " WHERE fb_num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, fb_num);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				FreeDTO dto = new FreeDTO();
				dto.setFileNum(rs.getLong("file_Num"));
				dto.setS_fileName(rs.getString("s_fileName"));
				dto.setC_fileName(rs.getString("c_fileName"));
				dto.setFb_num(rs.getLong("fb_num"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 게시글 수정 
	public void updateFree(FreeDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE Free_board SET title = ?, content = ? WHERE fb_num = ? AND id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setLong(3, dto.getFb_num());
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
	public void deleteFree(long num, String id, int powercode) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(powercode >= 99) {
				sql = "DELETE FROM free_board WHERE fb_num = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
				pstmt.execute();
			} else {
				sql = "DELETE FROM free_board WHERE fb_num=? AND id = ? ";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				pstmt.setString(2, id);
				
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 댓글 저장
	public void insertComment(FreeComDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
			
		try {
			sql = "INSERT INTO free_comment (fbc_num, content, reg_date, id, fb_num) "
					+ " VALUES(fbc_seq.NEXTVAL, ?, SYSDATE, ?, ?) ";
			pstmt = conn.prepareStatement(sql);
				
			pstmt.setString(1, dto.getContent());
			pstmt.setString(2, dto.getId());
			pstmt.setLong(3, dto.getFb_num());
				
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 댓글 개수
	public int dataCountComment(long fb_num) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) FROM free_comment "
					+ " WHERE fb_num = ? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, fb_num);
			
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
	public List<FreeComDTO> listComment(long fb_num, int offset, int size){
		List<FreeComDTO> list = new ArrayList<FreeComDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT fbc_num, fc.id, p.nickname, fb_num, ");
			sb.append("		content, fc.reg_date ");
			sb.append(" FROM free_comment fc ");
			sb.append(" JOIN player p ON p.id = fc.id ");
			sb.append(" WHERE fb_num = ? ");
			sb.append(" ORDER BY fbc_num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, fb_num);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				FreeComDTO dto = new FreeComDTO();
				dto.setFbc_num(rs.getLong("fbc_num"));
				dto.setId(rs.getString("id"));
				dto.setNickname(rs.getString("nickname"));
				dto.setFb_num(rs.getLong("fb_num"));
				dto.setContent(rs.getString("content"));
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
	
	public FreeComDTO findByComId(long fbc_num) {
		FreeComDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT fbc_num, fb_num, fb.id, p.nickname, fbc.content, fbc.reg_date "
					+ " FROM free_coment fc "
					+ " JOIN player p ON p.id = fb.id "
					+ " JOIN free_board fb ON fb.id = fbc.id "
					+ " WHERE fbc_num = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, fbc_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new FreeComDTO();
				
				dto.setFbc_num(rs.getLong("fbc_num"));
				dto.setFb_num(rs.getLong("fb_num"));
				dto.setId(rs.getString("id"));
				dto.setNickname(rs.getString("nickname"));
				dto.setContent(rs.getString("content"));
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
	
	// 댓글 삭제
	public void deleteComment(long fbc_num, String id, int powerCode ) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		if(powerCode != 99) {
			FreeComDTO dto = findByComId(fbc_num);
			if(dto == null || (! id.equals(dto.getId()))) {
				return;
			}
		}
		
		try {
			sql = "DELETE FROM free_comment WHERE fbc_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, fbc_num);
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 로그인 유저의 게시글 좋아요 유무
	public boolean isUserFreeLike(long st_num, String id) {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
			
			
		try {
			sql = "SELECT fb_num,id FROM free_like WHERE fb_num=? AND id=?";
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
		public void insertFreeLike(long fb_num, String id) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			try {
				sql = "INSERT INTO free_like (fb_num, id) VALUES (?,?) ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, fb_num);
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
		public void deleteFreeLike(long st_num, String id) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			try {
				sql = "DELETE FROM free_like WHERE fb_num =? AND id = ?";
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
	public int countFreeLike(long st_num) { 
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "SELECT count(*) FROM free_like WHERE fb_num = ?";
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
		
	public void deleteFree(long[] nums) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "DELETE FROM free_board WHERE fb_num = ?";
			
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
	public List<FreeDTO> listFAQFile(long num) {
		List<FreeDTO> list = new ArrayList<FreeDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT file_Num, s_fileName, c_fileName, fb_num FROM fb_file WHERE fb_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				FreeDTO dto = new FreeDTO();
				dto.setFileNum(rs.getLong("file_Num"));
				dto.setS_fileName(rs.getString("s_fileName"));
				dto.setC_fileName(rs.getString("c_fileName"));
				dto.setFb_num(rs.getLong("faq_num"));

				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public FreeDTO findByFileId(long fileNum) {
		FreeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT file_Num, s_filename, c_filename FROM Free_File WHERE file_Num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, fileNum);
			
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new FreeDTO();

				dto.setFileNum(rs.getLong("file_Num"));
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
	
}
