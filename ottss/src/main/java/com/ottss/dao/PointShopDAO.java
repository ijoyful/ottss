package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.PointShopDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class PointShopDAO {
	private Connection conn = DBConn.getConnection();
	
	public List<PointShopDTO> itemList(int offset, int size) {
	List<PointShopDTO> list = new ArrayList<PointShopDTO>();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql;
	
	try {
		sql = " SELECT item_num, item_name, item_explain, categories, amount "
				+ " FROM point_shop "
				+ " ORDER BY item_num "
				+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, offset);
		pstmt.setInt(2, size);
		
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			PointShopDTO dto = new PointShopDTO();
			
			dto.setItem_num(rs.getLong("item_num"));
			dto.setItem_name(rs.getString("item_name"));
			dto.setItem_explain(rs.getString("item_explain"));
			dto.setCategories(rs.getString("categories"));
			dto.setAmount(rs.getInt("amount"));
			
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
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) cnt "
					+ " FROM point_shop ";
			
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
			sql = "SELECT COUNT(*) cnt "
					+ " FROM point_shop ";	
			if(schType.equals("all")) {
				sql += " WHERE(INSTR(item_name, ?) >=1 ";
			} else {
				sql += " WHERE INSTR(" + schType + ", ?) >= 1";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			if(schType.equals("all")) {
				pstmt.setString(2,kwd);
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
	
	public List<PointShopDTO> itemList(int offset, int size, String schType, String kwd){
		List<PointShopDTO> list = new ArrayList<PointShopDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT item_num, item_name, item_explain, categories, amount "
					+ " FROM point_shop ";
			if(schType.equals("all")) {
				sql += " WHERE (INSTR(item_name, ?) >= 1";
			} else {
				sql += " WHERE INSTR(" + schType + ", ?) >= 1";
			}
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			if(schType.equals("all")) {
				pstmt.setString(2, kwd);
			}
			rs =pstmt.executeQuery();
			while(rs.next()) {
				PointShopDTO dto = new PointShopDTO();
				
				dto.setItem_num(rs.getLong("item_num"));
				dto.setItem_name(rs.getString("item_name"));
				dto.setItem_explain(rs.getString("item_explain"));
				dto.setCategories(rs.getString("categories"));
				dto.setAmount(rs.getInt("amount"));
				
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
