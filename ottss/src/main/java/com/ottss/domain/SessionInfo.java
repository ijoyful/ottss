package com.ottss.domain;

public class SessionInfo {
	private String id;
	private String nickName;
	private int powerCode;
	
	public int getPowerCode() {
		return powerCode;
	}

	public void setPowerCode(int powerCode) {
		this.powerCode = powerCode;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
}
