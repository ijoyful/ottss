package com.ottss.dao;

import java.sql.Connection;

import com.ottss.util.DBConn;


// 게임 하면 포인트 GAME_RECORD에 인서트 / 마이페이지에 업데이트 / 쿼리 2번써서 
//  게임기록에 인서트  // 게임 시작전에 입장 포인트 보유 여부 확인
// 사용포인트 / 얻은포인트 한번에 인서트 
public class MoleGameDAO {
	private Connection conn = DBConn.getConnection();
}
