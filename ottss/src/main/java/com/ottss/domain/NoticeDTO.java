package com.ottss.domain;

import java.util.List;

import com.ottss.util.MyMultipartFile;

public class NoticeDTO {
	private long n_num; // 공지 게시판 게시글 번호. 시퀀스 n_seq
	private String title; // 게시글 제목
	private String content; // 게시글 내용
	private long hitCount; // 게시글 조회수
	private String reg_date; // 게시글 작성일
	private String mod_date; // 게시글 수정일
	private String id; // 게시글 작성자 id
	private String nickname; // 작성자 닉네임

	// 공지 게시글에 첨부된 파일
	private long fileNum; // 공지게시판에 첨부된 파일 번호. 시퀀스 n_file_seq
	private String s_fileName; // 서버에 저장된 파일
	private String c_fileName; // 클라이언트가 저장한 파일명
	private List<MyMultipartFile> listFile;

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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public List<MyMultipartFile> getListFile() {
		return listFile;
	}
	public void setListFile(List<MyMultipartFile> listFile) {
		this.listFile = listFile;
	}
}
