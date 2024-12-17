package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.ReportDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class AdminReportDAO {

	public Connection conn = DBConn.getConnection();
	
	public int dataCount() {
		
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			
			sql = "select count(*) cnt from report";
			
			pstmt = conn.prepareStatement(sql);
			
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
	
	public int dataCount(String schType, String kwd) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) cnt FROM report";
			if (schType.equals("all")) {
				sql += " WHERE (INSTR(id, ?) >= 1 OR INSTR(name, ?) >= 1)";
			} else {
				sql += " AND INSTR(" + schType + ", ?) >= 1";
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
	
	public List<ReportDTO> listReport(int offset, int size) {
		List<ReportDTO> list = new ArrayList<ReportDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("SELECT REPORT_NUM, TARGET_NUM, TARGET_TABLE, REPORT_REASON, REPORT_DATE, p.id");
			sb.append(" FROM REPORT r");
			sb.append(" JOIN player p ON r.id = p.id");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReportDTO dto = new ReportDTO();
				
				dto.setId(rs.getString("id"));
				dto.setReport_num(rs.getInt("report_num"));
				dto.setTarget_num(rs.getInt("target_num"));
				dto.setTarget_table(rs.getString("target_table"));
				dto.setReport_reason(rs.getString("report_reason"));
				dto.setReport_date(rs.getString("report_date"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	public List<ReportDTO> listReport(int offset, int size, String schType, String kwd) {
		List<ReportDTO> list = new ArrayList<ReportDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("SELECT REPORT_NUM, TARGET_NUM, TARGET_TABLE, REPORT_REASON, REPORT_DATE, p.id");
			sb.append(" FROM REPORT r");
			sb.append(" JOIN player p ON r.id = p.id");
			if (schType.equals("all")) {
				sb.append(" WHERE (INSTR(id, ?) >= 1 OR INSTR(name, ?) >= 1)");
			} else {
				sb.append(" AND INSTR(" + schType + ", ?) >= 1");
			}
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
				ReportDTO dto = new ReportDTO();
				
				dto.setId(rs.getString("id"));
				dto.setReport_num(rs.getInt("report_num"));
				dto.setTarget_num(rs.getInt("target_num"));
				dto.setTarget_table(rs.getString("target_table"));
				dto.setReport_reason(rs.getString("report_reason"));
				dto.setReport_date(rs.getString("report_date"));
				
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
