package com.ottss.domain;

public class STLikeDTO { // 자랑/분석게시글 좋아요 DTO
	private long st_num; // 게시글 번호
	private String id; // 좋아요 누른 유저 아이디
	public long getSt_num() {
		return st_num;
	}
	public void setSt_num(long st_num) {
		this.st_num = st_num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
