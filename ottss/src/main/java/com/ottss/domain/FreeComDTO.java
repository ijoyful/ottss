package com.ottss.domain;

public class FreeComDTO { // 자유게시판 댓글 DTO
	private long fbc_num; // 자유게시판 댓글 번호. 시퀀스 fbc_seq
	private String content; // 댓글 내용
	private String reg_date; // 댓글 작성일
	private String id; // 작성자 아이디
	private long fb_num; // 댓글을 작성한 게시글 번호
	public long getFbc_num() {
		return fbc_num;
	}
	public void setFbc_num(long fbc_num) {
		this.fbc_num = fbc_num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getFb_num() {
		return fb_num;
	}
	public void setFb_num(long fb_num) {
		this.fb_num = fb_num;
	}
	
}
