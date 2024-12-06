package com.ottss.domain;

public class GameDTO {
	private int game_num; // 등록된 게임 번호
	private String game_title; // 등록된 게임 제목
	private String game_text; // 게임 방법
	private String game_image; // 게임 gif
	public int getGame_num() {
		return game_num;
	}
	public void setGame_num(int game_num) {
		this.game_num = game_num;
	}
	public String getGame_title() {
		return game_title;
	}
	public void setGame_title(String game_title) {
		this.game_title = game_title;
	}
	public String getGame_text() {
		return game_text;
	}
	public void setGame_text(String game_text) {
		this.game_text = game_text;
	}
	public String getGame_image() {
		return game_image;
	}
	public void setGame_image(String game_image) {
		this.game_image = game_image;
	}
}
