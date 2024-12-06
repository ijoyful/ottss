package com.ottss.domain;

public class FreeDTO {
	private long fb_num; // 게시글 번호. 시퀀스
	private String title; // 게시글 제목
	private String content; // 게시글 내용
	private String reg_date; // 게시글 작성일
	private int blind; // 블라인드 여부. 0: 기본값 1: 블라인드처리
	private long hitCount; // 조회수
	private String categories; // 자유게시판 내 카테고리(말머리로 사용 예정)
	private String ipAddr; // 작성자 ip 주소
	private String mod_date; // 게시글 수정일
	private String id; // 작성자 id

	// 업로드된 파일
	private long fileNum; // 업로드된 파일 번호. 시퀀스 fb_file_seq
	private String s_fileName; // 서버에 저장된 파일 이름
	private String c_fileName; // 클라이언트가 저장해놓은 파일 이름

	// 게시글 좋아요
	private int likeCount; // SELECT COUNT(*) FROM free_board WHERE fb_num = ?
	public long getFb_num() {
		return fb_num;
	}
	public void setFb_num(long fb_num) {
		this.fb_num = fb_num;
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
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
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
	
	public long getFileNum() {
		return fileNum;
	}
	public void setFileNum(long fileNum) {
		this.fileNum = fileNum;
	}
	public String getS_fileName() {
		return s_fileName;
	}
	public void setS_fileName(String s_fileName) {
		this.s_fileName = s_fileName;
	}
	public String getC_fileName() {
		return c_fileName;
	}
	public void setC_fileName(String c_fileName) {
		this.c_fileName = c_fileName;
	}

	public int getLikes() {
		return likeCount;
	}
	public void setLikes(int likeCount) {
		this.likeCount = likeCount;
	}
}
