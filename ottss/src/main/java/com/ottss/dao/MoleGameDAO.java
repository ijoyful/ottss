package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;
import com.ottss.domain.MoleGameDTO;

public class MoleGameDAO {
    private Connection conn = DBConn.getConnection();

    // 입장 포인트 확인
    public boolean checkPoint(String id) {
        boolean canEnter = false;  // 디폴트 : 입장 할 수 없음 
        PreparedStatement pstmt = null; // SQL 쿼리를 실행
        ResultSet rs = null; // 쿼리 결과를 저장하는 객체 
        String sql; // SQL 넣을 변수
        int entryFee = 10;

        try {
            // 가장 최근의 left_pt 확인
            sql = "SELECT point FROM Player WHERE id = ?"; 
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int point = rs.getInt("point");
                canEnter = point >= entryFee;  // 입장료가 충분한지 확인
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return canEnter;
    }

    // 게임 시작 시 10포인트 차감
    public boolean startGame(MoleGameDTO dto) throws SQLException {
        boolean success = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        int entryFee = 10;

        try {
            // 1. 현재 포인트 확인
            sql = "SELECT point FROM Player WHERE id = ?"; 
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            rs = pstmt.executeQuery();

            int currentPoint = 0; // 포인트 변수 
            if (rs.next()) {
                currentPoint = rs.getInt("point");
            }

            // 2. 포인트가 충분한지 확인
            if (currentPoint >= entryFee) {
                // 3. 충분한 포인트가 있으면, 10포인트 차감
                int newPoint = currentPoint - entryFee;

                // 4. Player 테이블에서 포인트 업데이트
                sql = "UPDATE Player SET point = ? WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, newPoint); // 새로운 포인트
                pstmt.setString(2, dto.getId());
                pstmt.executeUpdate();

                success = true; // 포인트 차감 성공
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return success; // 포인트 차감 여부
    }

 // updateUserPoint에서 포인트 차감 로직 제거
    public int updateUserPoint(MoleGameDTO dto) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        int updatedPoint = 0;

        try {
            // 1. 현재 포인트 확인
            sql = "SELECT point FROM Player WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            rs = pstmt.executeQuery();

            int currentPoint = 0; // 포인트 변수
            if (rs.next()) {
                currentPoint = rs.getInt("point");
            }

            // 2. 새로운 잔여 포인트 계산
            int newPoint = currentPoint + dto.getWinPoint();  // 얻은 포인트만 반영

            // 3. Player 테이블에서 포인트 업데이트
            sql = "UPDATE Player SET point = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newPoint); // 새로운 포인트
            pstmt.setString(2, dto.getId());
            pstmt.executeUpdate();

            updatedPoint = newPoint;  // 최종 잔여 포인트 반환
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return updatedPoint;
    }

    // 게임 기록 삽입
    public void insertPlayRecord(MoleGameDTO dto) throws SQLException {
    	PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1, sql2;

        try {
            // 첫 번째 INSERT: play_record에 게임 기록 저장
            sql1 = "INSERT INTO play_record (play_num, play_date, used_point, win_point, result, id, game_num) "
                    + "VALUES (play_seq.NEXTVAL, SYSDATE, ?, ?, ?, ?, ?)";
            pstmt1 = conn.prepareStatement(sql1);

            pstmt1.setInt(1, dto.getUsedPoint());    // 사용한 포인트
            pstmt1.setInt(2, dto.getWinPoint());     // 얻은 포인트
            pstmt1.setString(3, dto.getResult());    // 게임 결과
            pstmt1.setString(4, dto.getId());        // 사용자 ID
            pstmt1.setInt(5, dto.getGameNum());      // 게임 번호

            pstmt1.executeUpdate(); // play_record 테이블에 쿼리 실행

            // 두 번째 INSERT: point_record에 포인트 기록 저장
            sql2 = "INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id) "
                    + "VALUES (pt_seq.NEXTVAL, '1', ?, ?, SYSDATE, ?)";
            pstmt2 = conn.prepareStatement(sql2);

            int pointDifference = dto.getWinPoint() - dto.getUsedPoint(); // 얻은 포인트 - 사용한 포인트
            pstmt2.setInt(1, pointDifference);  // 기록할 포인트 차이
            pstmt2.setInt(2, dto.getUsedPoint()); // 남은 포인트 계산 값 (이 예시는 left_pt로 계산)
            pstmt2.setString(3, dto.getId()); // 사용자 ID

            pstmt2.executeUpdate(); // point_record 테이블에 쿼리 실행

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(pstmt1);  // pstmt1 리소스 해제
            DBUtil.close(pstmt2);  // pstmt2 리소스 해제
        }
    }
}
