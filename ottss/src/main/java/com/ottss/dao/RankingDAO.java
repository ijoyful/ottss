package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ottss.domain.PlayRecordDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class RankingDAO {
	private Connection conn = DBConn.getConnection();
	
	public List<PlayRecordDTO> playRanking() {
		List<PlayRecordDTO> list = new ArrayList<PlayRecordDTO>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT result, game_num, nickname");
			sb.append(" FROM play_record r");
			sb.append("	JOIN player p ON p.id = r.id");
			sb.append("");
			sb.append("");
			sb.append("");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstm);
		}
		
		
		
		return list;
	}
	
	
	
}
