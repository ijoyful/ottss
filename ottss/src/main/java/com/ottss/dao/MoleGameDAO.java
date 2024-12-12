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
        boolean canEnter = false;  
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        int entryFee = 10;

        try {
            sql = "SELECT point FROM Player WHERE id = ?"; 
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int point = rs.getInt("point");
                canEnter = point >= entryFee;  
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

            int currentPoint = 0;
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
                pstmt.setInt(1, newPoint); 
                pstmt.setString(2, dto.getId());
                pstmt.executeUpdate();

                success = true; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return success;
    }

    // 포인트 업데이트 (게임 끝날 때)
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

            int currentPoint = 0;
            if (rs.next()) {
                currentPoint = rs.getInt("point");
            }

            // 2. 새로운 잔여 포인트 계산
            int newPoint = currentPoint + dto.getWinPoint();

            // 3. Player 테이블에서 포인트 업데이트
            sql = "UPDATE Player SET point = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newPoint); 
            pstmt.setString(2, dto.getId());
            pstmt.executeUpdate();

            updatedPoint = newPoint; 
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return updatedPoint;
    }

    // 게임 시작 기록 삽입
    public void insertPlayRecordStart(MoleGameDTO dto) throws SQLException {
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1, sql2;

        try {
            conn.setAutoCommit(false); // 트랜잭션 시작

            sql1 = "INSERT INTO play_record (play_num, play_date, type, point, result, id, game_num) "
                    + "VALUES (play_seq.NEXTVAL, SYSDATE, 1, 10, '-', ?, ?)";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setString(1, dto.getId());
            pstmt1.setInt(2, dto.getGameNum());
            pstmt1.executeUpdate();

            sql2 = "INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id) "
                    + "VALUES (pt_seq.NEXTVAL, '1', ?, ?, SYSDATE, ?)";
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, 10);
            pstmt2.setInt(2, 10); 
            pstmt2.setString(3, dto.getId());
            pstmt2.executeUpdate();

            conn.commit();  // 트랜잭션 커밋
        } catch (SQLException e) {
            conn.rollback();  // 예외 발생 시 롤백
            e.printStackTrace();
            throw e;
        } finally {
            conn.setAutoCommit(true);  // 자동 커밋 모드로 복귀
            DBUtil.close(pstmt1);
            DBUtil.close(pstmt2);
        }
    }

    // 게임 종료 기록 삽입
    public void insertPlayRecordEnd(MoleGameDTO dto) throws SQLException {
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1, sql2;

        try {
            conn.setAutoCommit(false);

            sql1 = "INSERT INTO play_record (play_num, play_date, type, point, result, id, game_num) "
                    + "VALUES (play_seq.NEXTVAL, SYSDATE, 0, ?, ?, ?, ?)";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setInt(1, dto.getWinPoint());
            pstmt1.setString(2, dto.getResult());
            pstmt1.setString(3, dto.getId());
            pstmt1.setInt(4, dto.getGameNum());
            pstmt1.executeUpdate();

            sql2 = "INSERT INTO point_record (pt_num, categories, point, left_pt, pt_date, id) "
                    + "VALUES (pt_seq.NEXTVAL, '1', ?, ?, SYSDATE, ?)";
            pstmt2 = conn.prepareStatement(sql2);
            int pointDifference = dto.getWinPoint() - dto.getUsedPoint();
            pstmt2.setInt(1, pointDifference);
            pstmt2.setInt(2, dto.getUserPoint());
            pstmt2.setString(3, dto.getId());
            pstmt2.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            DBUtil.close(pstmt1);
            DBUtil.close(pstmt2);
        }
    }
}
