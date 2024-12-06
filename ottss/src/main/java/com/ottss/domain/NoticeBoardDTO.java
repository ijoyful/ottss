package com.ottss.domain;

public class NoticeBoardDTO {
	private long n_num; // 공지 게시판 게시글 번호. 시퀀스 n_seq
	private String title; // 게시글 제목
	private String content; // 게시글 내용
	private long hitCount; // 게시글 조회수
	private String reg_date; // 게시글 작성일
	private String mod_date; // 게시글 수정일
	private String id; // 게시글 작성자 id
	public long getN_num() {
		return n_num;
	}
	public void setN_num(long n_num) {
		this.n_num = n_num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getHitCount() {
		return hitCount;
	}
	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getMod_date() {
		return mod_date;
	}
	public void setMod_date(String mod_date) {
		this.mod_date = mod_date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
