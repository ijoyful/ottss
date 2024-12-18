package com.ottss.domain;

public class PlayRecordDTO {
	private long play_num; // 플레이 기록 번호. 시퀀스 play_seq
	private String play_date; // 플레이한 날짜
	private int used_point; // 사용 포인트
	private int win_point; // 얻은 포인트
	private String result; // 게임 결과 ex. 진출 라운드, 점수 등
	private String id; // 게임을 플레이한 유저 아이디
	private int game_num; // 플레이한 게임 번호
	
	//++가위바위보추가(라운드)
	private String round;
	
	//++랭킹 닉네임추가
	private String nickname;
	
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getRound() {
		return round;
	}
	public void setRound(String round) {
		this.round = round;
	}
	public long getPlay_num() {
		return play_num;
	}
	public void setPlay_num(long play_num) {
		this.play_num = play_num;
	}
	public String getPlay_date() {
		return play_date;
	}
	public void setPlay_date(String play_date) {
		this.play_date = play_date;
	}
	public int getUsed_point() {
		return used_point;
	}
	public void setUsed_point(int used_point) {
		this.used_point = used_point;
	}
	public int getWin_point() {
		return win_point;
	}
	public void setWin_point(int win_point) {
		this.win_point = win_point;
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
	public int getGame_num() {
		return game_num;
	}
	public void setGame_num(int game_num) {
		this.game_num = game_num;
	}
}
