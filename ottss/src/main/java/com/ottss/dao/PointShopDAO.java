package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.PlayRecordDTO;
import com.ottss.domain.PointRecordDTO;
import com.ottss.domain.PointShopDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class PointShopDAO {
	private Connection conn = DBConn.getConnection();
	
	// 포인트 샵 상품 리스트
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

    // 3. 아이템 구매 처리
    public boolean purchaseItem(String id, long item_num) {
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        boolean result = false;

        String getUserPointsSql = "SELECT point FROM player WHERE id = ?";
        String getItemPriceSql = "SELECT amount FROM point_shop WHERE item_num = ?";
        String updateUserPointsSql = "UPDATE player SET point = point - ? WHERE id = ?";
        String insertInventorySql = "INSERT INTO buy_record (buy_num, buy_date, equip, id, item_num) "
        								+ "VALUES (buy_seq.NEXTVAL,SYSDATE, 0 , ?, ?)";
     
        try {
            conn.setAutoCommit(false);

            // 1. 사용자 포인트 조회
            int userPoints = 0;
            pstmt1 = conn.prepareStatement(getUserPointsSql);
            pstmt1.setString(1, id);
            rs = pstmt1.executeQuery();
            if (rs.next()) {
                userPoints = rs.getInt("point");
            }

            // 2. 아이템 가격 조회
            int itemPrice = 0;
            pstmt2 = conn.prepareStatement(getItemPriceSql);
            pstmt2.setLong(1, item_num);
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
            pstmt1.setString(2, id);
            pstmt1.executeUpdate();

            // 5. 인벤토리에 아이템 추가
            pstmt2 = conn.prepareStatement(insertInventorySql);
            pstmt2.setString(1, id);
            pstmt2.setLong(2, item_num);
            pstmt2.executeUpdate();

            result = true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SQL 오류: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            result = false;
        } finally {
        	try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
            DBUtil.close(rs);
            DBUtil.close(pstmt1);
            DBUtil.close(pstmt2);
        }

        return result;
    }

    // 4. 사용자 인벤토리 조회
    public List<PointShopDTO> getPlayerInventory(String id) {
        List<PointShopDTO> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        
        try {
        	sql = "SELECT ps.item_num, ps.item_name, ps.categories, ps.amount, ps.item_explain "
        			+ " FROM point_shop ps "
        			+ " JOIN buy_record br ON ps.item_num = br.item_num "
        			+ " WHERE br.id = ? "
        			+ " ORDER BY categories, item_num "; 
                   
        	pstmt = conn.prepareStatement(sql);
        	
        	pstmt.setString(1, id);
        	
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
    
    // 5. 포인트 레코드 테이블에 기록 추가.
    public void insertPointRecord(PointRecordDTO dto) {
    	PreparedStatement pstmt = null;
    	String sql;
    	
    	try {
    		sql = " INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id) "
					+ " VALUES (pt_seq.NEXTVAL, 11, ?, ?, SYSDATE, ?)";
    		
    		pstmt = conn.prepareStatement(sql);
    		
    		pstmt.setInt(1,dto.getPoint());
    		pstmt.setInt(2,dto.getLeft_point());
    		pstmt.setString(3, dto.getId());
    		
    		pstmt.executeUpdate();
    		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
    }
		
}
