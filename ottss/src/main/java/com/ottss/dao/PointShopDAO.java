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
	
	public PointShopDTO getItem(long itemNum) {
        PointShopDTO item = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sql;
        
        try {
        	 sql = "SELECT item_num, item_name, categories, amount, item_explain "
        	 		+ " FROM point_shop_items "
        	 		+ " WHERE item_num = ?"; 
        	 
        	 pstmt = conn.prepareStatement(sql);
        	 
        	 pstmt.setLong(1, itemNum);
        	 
        	 rs = pstmt.executeQuery();
 
        	 if(rs.next()) {
        		item = new PointShopDTO();
                item.setItem_num(rs.getLong("item_num"));
                item.setItem_name(rs.getString("item_name"));
                item.setCategories(rs.getString("categories"));
                item.setAmount(rs.getInt("amount"));
                item.setItem_explain(rs.getString("item_explain"));	 
        	 }
		} catch (Exception e) {
			 e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
        return item;
    }

    // 3. 아이템 구매 처리
    public boolean purchaseItem(int userId, long itemNum) {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;

        String getUserPointsSql = "SELECT points FROM users WHERE user_id = ?";
        String getItemPriceSql = "SELECT amount FROM point_shop_items WHERE item_num = ?";
        String updateUserPointsSql = "UPDATE users SET points = points - ? WHERE user_id = ?";
        String insertInventorySql = "INSERT INTO user_inventory (user_id, item_num) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            // 1. 사용자 포인트 조회
            int userPoints = 0;
            pstmt1 = conn.prepareStatement(getUserPointsSql);
            pstmt1.setInt(1, userId);
            rs = pstmt1.executeQuery();
            if (rs.next()) {
                userPoints = rs.getInt("points");
            }

            // 2. 아이템 가격 조회
            int itemPrice = 0;
            pstmt2 = conn.prepareStatement(getItemPriceSql);
            pstmt2.setLong(1, itemNum);
            rs = pstmt2.executeQuery();
            if (rs.next()) {
                itemPrice = rs.getInt("amount");
            }

            // 3. 포인트 부족 확인
            if (userPoints < itemPrice) {
                return false;
            }

            // 4. 사용자 포인트 차감
            pstmt1 = conn.prepareStatement(updateUserPointsSql);
            pstmt1.setInt(1, itemPrice);
            pstmt1.setInt(2, userId);
            pstmt1.executeUpdate();

            // 5. 인벤토리에 아이템 추가
            pstmt2 = conn.prepareStatement(insertInventorySql);
            pstmt2.setInt(1, userId);
            pstmt2.setLong(2, itemNum);
            pstmt2.executeUpdate();

            // 트랜잭션 커밋
            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt1);
            DBUtil.close(pstmt2);
        }
    }

    // 4. 사용자 인벤토리 조회
    public List<PointShopDTO> getUserInventory(int userId) {
        List<PointShopDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        
        try {
        	sql = "SELECT i.item_num, i.item_name, i.categories, i.amount, i.item_explain "
        			+ " FROM point_shop_items i "
        			+ " JOIN user_inventory u ON i.item_num = u.item_num "
        			+ " WHERE u.user_id = ? "; 
                   
        	pstmt = conn.prepareStatement(sql);
        	
        	pstmt.setInt(1, userId);
        	
        	rs = pstmt.executeQuery();
        	while(rs.next()) {
        		PointShopDTO item = new PointShopDTO();
                item.setItem_num(rs.getLong("item_num"));
                item.setItem_name(rs.getString("item_name"));
                item.setCategories(rs.getString("categories"));
                item.setAmount(rs.getInt("amount"));
                item.setItem_explain(rs.getString("item_explain"));

                list.add(item);	
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
