package com.ottss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ottss.domain.MoleGameDTO;
import com.ottss.util.DBConn;
import com.ottss.util.DBUtil;

public class MoleGameDAO {
    private Connection conn = DBConn.getConnection();

    // 입장 포인트 확인
    public boolean checkEntryPoint(String id, int entryFee) {
        boolean canEnter = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        try {
            // 가장 최근의 left_pt 확인
            sql = "SELECT left_pt FROM point_record WHERE id = ? ORDER BY pt_date DESC FETCH FIRST 1 ROWS ONLY";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int leftPt = rs.getInt("left_pt");
                canEnter = leftPt >= entryFee;  // 입장료가 충분한지 확인
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return canEnter;
    }

    // 게임 기록 추가
    public void insertGameRecord(MoleGameDTO moleGameDTO) throws SQLException {
        PreparedStatement pstmt = null;
        String sql;

        try {
            sql = "INSERT INTO play_record (play_num, play_date, used_point, win_point, result, id, game_num) "
                + "VALUES (play_seq.NEXTVAL, SYSDATE, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, moleGameDTO.getUsedPoint());
            pstmt.setInt(2, moleGameDTO.getWinPoint());
            pstmt.setString(3, moleGameDTO.getResult());
            pstmt.setString(4, moleGameDTO.getId());
            pstmt.setInt(5, moleGameDTO.getGameNum());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(pstmt);
        }
    }

    // 포인트 기록 및 잔여 포인트 업데이트
    public void updatePointRecord(String id, String category, int point) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        // 트랜잭션 시작
        try {
            conn.setAutoCommit(false);  // 트랜잭션 시작

            // 1. 현재 포인트 확인
            sql = "SELECT left_pt FROM point_record WHERE id = ? ORDER BY pt_date DESC FETCH FIRST 1 ROWS ONLY";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            int currentLeftPt = 0;
            if (rs.next()) {
                currentLeftPt = rs.getInt("left_pt");
            }

            // 2. 새로운 잔여 포인트 계산
            int newLeftPt = currentLeftPt + point;

            // 3. 포인트 기록 추가
            sql = "INSERT INTO point_record (pt_num, id, categories, point, left_pt, pt_date) "
                + "VALUES (pt_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);
            pstmt.setString(2, category);
            pstmt.setInt(3, point);
            pstmt.setInt(4, newLeftPt);

            pstmt.executeUpdate();

            // 트랜잭션 커밋
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();  // 예외 발생 시 롤백
            throw e;
        } finally {
            conn.setAutoCommit(true);  // 트랜잭션 종료
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }
    }

    // 사용자 포인트 조회
    public int getUserPoint(String userId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        int userPoint = 0;

        try {
            // 가장 최근의 left_pt 조회
            sql = "SELECT left_pt FROM point_record WHERE id = ? ORDER BY pt_date DESC FETCH FIRST 1 ROWS ONLY";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                userPoint = rs.getInt("left_pt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return userPoint;
    }

    // 사용자 포인트 업데이트 (게임 후 포인트 차감 및 추가)
    public int updateUserPoint(String userId, int usedPoint, int winPoint) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        int updatedPoint = 0;

        // 트랜잭션 시작
        try {
            conn.setAutoCommit(false);  // 트랜잭션 시작

            // 1. 현재 포인트 확인
            sql = "SELECT left_pt FROM point_record WHERE id = ? ORDER BY pt_date DESC FETCH FIRST 1 ROWS ONLY";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            int currentLeftPt = 0;
            if (rs.next()) {
                currentLeftPt = rs.getInt("left_pt");
            }

            // 2. 새로운 잔여 포인트 계산
            int newLeftPt = currentLeftPt - usedPoint + winPoint;

            // 3. 포인트 기록 추가
            sql = "INSERT INTO point_record (pt_num, id, categories, point, left_pt, pt_date) "
                + "VALUES (pt_seq.NEXTVAL, ?, '게임', ?, ?, SYSDATE)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setInt(2, (winPoint - usedPoint));  // 게임에서 사용된 포인트와 얻은 포인트 차이
            pstmt.setInt(3, newLeftPt);

            pstmt.executeUpdate();

            // 트랜잭션 커밋
            conn.commit();

            updatedPoint = newLeftPt;  // 최종 잔여 포인트 반환
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();  // 예외 발생 시 롤백
            throw e;
        } finally {
            conn.setAutoCommit(true);  // 트랜잭션 종료
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }

        return updatedPoint;
    }

    // 게임 기록 삽입
    public void insertPlayRecord(String userId, int usedPoint, int winPoint, int gameNum, String result) throws SQLException {
        PreparedStatement pstmt = null;
        String sql;

        try {
            sql = "INSERT INTO play_record (play_num, play_date, used_point, win_point, result, id, game_num) "
                + "VALUES (play_seq.NEXTVAL, SYSDATE, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, usedPoint);
            pstmt.setInt(2, winPoint);
            pstmt.setString(3, result);
            pstmt.setString(4, userId);
            pstmt.setInt(5, gameNum);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DBUtil.close(pstmt);
        }
    }
}
