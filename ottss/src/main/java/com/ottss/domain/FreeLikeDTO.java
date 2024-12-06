package com.ottss.domain;

public class FreeLikeDTO {
	private long fb_num; // 좋아요 누른 글 번호
	private String id; // 좋아요 누른 아이디
	public long getFb_num() {
		return fb_num;
	}
	public void setFb_num(long fb_num) {
		this.fb_num = fb_num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
