package com.ottss.domain;

import java.util.Date;

public class MoleGameDTO {
    private int playNum;         // 플레이 번호
    private Date playDate;       // 플레이한 날짜
    private int usedPoint;       // 사용 포인트
    private int winPoint;        // 얻은 포인트
    private String result;       // 기록 (게임 결과)
    private String id;           // 사용자 아이디
    private int gameNum;         // 게임 번호
    private int userPoint;       // 보유 포인트

    // Getter and Setter Methods
    public int getPlayNum() {
        return playNum;
    }
    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }
    public Date getPlayDate() {
        return playDate;
    }
    public void setPlayDate(Date playDate) {
        this.playDate = playDate;
    }
    public int getUsedPoint() {
        return usedPoint;
    }
    public void setUsedPoint(int usedPoint) {
        this.usedPoint = usedPoint;
    }
    public int getWinPoint() {
        return winPoint;
    }
    public void setWinPoint(int winPoint) {
        this.winPoint = winPoint;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getGameNum() {
        return gameNum;
    }
    public void setGameNum(int gameNum) {
        this.gameNum = gameNum;
    }
    public int getUserPoint() {
        return userPoint;
    }
    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }
}
