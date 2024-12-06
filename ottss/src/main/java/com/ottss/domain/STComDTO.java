package com.ottss.domain;

public class STComDTO { // 자랑/분석게시판 게시글 댓글
	private long stc_num; // 댓글 번호. 시퀀스 stc_seq
	private String content; // 댓글 내용
	private String reg_date; // 댓글 작성일
	private String id; // 댓글 작성자 아이디
	private long st_num; // 댓글을 작성한 게시글
	public long getStc_num() {
		return stc_num;
	}
	public void setStc_num(long stc_num) {
		this.stc_num = stc_num;
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
	public long getSt_num() {
		return st_num;
	}
	public void setSt_num(long st_num) {
		this.st_num = st_num;
	}
}
