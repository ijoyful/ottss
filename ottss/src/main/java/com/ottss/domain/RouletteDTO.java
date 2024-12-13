package com.ottss.domain;

public class RouletteDTO {
	private long play_Num; // play_seq
	private String play_Date;
	private int used_point;
	private int win_point;

	private String result;
	private String id;
	private int game_num;
	
	private int category;
	private int left_point;
	public int getWin_point() {
		return win_point;
	}
	public void setWin_point(int win_point) {
		this.win_point = win_point;
	}
	public int getUsed_point() {
		return used_point;
	}
	public void setUsed_point(int used_point) {
		this.used_point = used_point;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getLeft_point() {
		return left_point;
	}
	public void setLeft_point(int left_point) {
		this.left_point = left_point;
	}
	public long getPlay_Num() {
		return play_Num;
	}
	public void setPlay_Num(long play_Num) {
		this.play_Num = play_Num;
	}
	public String getPlay_Date() {
		return play_Date;
	}
	public void setPlay_Date(String play_Date) {
		this.play_Date = play_Date;
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
