package com.ottss.domain;

public class STBoardDTO { // 자랑/분석게시판 게시글 DTO
	private long st_num; // 게시글 번호. 시퀀스 st_seq
	private String title; // 게시글 제목
	private String content; // 게시글 내용
	private String reg_date; // 게시글 작성일
	private String mod_date; // 게시글 수정일
	private int blind; // 게시글 블라인드 여부 0: 기본값 1: 블라인드 처리
	private long hitCount; // 게시글 조회수
	private String ipAddr; // 게시글 작성자 ip 주소
	private String board_type; // 자랑 게시글인지, 분석 게시글인지 속성값 저장
	private String id; // 작성자 id
	public long getSt_num() {
		return st_num;
	}
	public void setSt_num(long st_num) {
		this.st_num = st_num;
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
	public int getBlind() {
		return blind;
	}
	public void setBlind(int blind) {
		this.blind = blind;
	}
	public long getHitCount() {
		return hitCount;
	}
	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getBoard_type() {
		return board_type;
	}
	public void setBoard_type(String board_type) {
		this.board_type = board_type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
