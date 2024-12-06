package com.ottss.domain;

import java.util.List;

import com.ottss.util.MyMultipartFile;

public class FAQDTO {
	private long faq_num; // faq 게시판 글 번호. 시퀀스 faq_seq
	private String q_title; // 질문제목
	private String q_content; // 질문 내용
	private String a_content; // 답변 내용
	private String q_date; // 질문 날짜
	private String a_date; // 답변 날짜
	private long hitCount; // 조회수
	private int top_fix; // 게시판에서 상단 고정 여부 0: 고정안함(기본값) 1: 고정
	private String user_id; // 질문자 아이디
	private String admin_id; // 답변자 아이디
	private String q_nickname; // 질문자 닉네임
	private String a_nickname; // 답변자 닉네임

	private long fileNum; // 질문게시판에 첨부된 파일 번호. 시퀀스 faq_file_seq
	private String s_fileName; // 서버에 저장된 파일명
	private String c_fileName; // 클라이언트가 저장한 파일명
	private List<MyMultipartFile> listFile;
	
	public long getFaq_num() {
		return faq_num;
	}
	public void setFaq_num(long faq_num) {
		this.faq_num = faq_num;
	}
	public String getQ_title() {
		return q_title;
	}
	public void setQ_title(String q_title) {
		this.q_title = q_title;
	}
	public String getQ_content() {
		return q_content;
	}
	public void setQ_content(String q_content) {
		this.q_content = q_content;
	}
	public String getA_content() {
		return a_content;
	}
	public void setA_content(String a_content) {
		this.a_content = a_content;
	}
	public String getQ_date() {
		return q_date;
	}
	public void setQ_date(String q_date) {
		this.q_date = q_date;
	}
	public String getA_date() {
		return a_date;
	}
	public void setA_date(String a_date) {
		this.a_date = a_date;
	}
	public long getHitCount() {
		return hitCount;
	}
	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}
	public int getTop_fix() {
		return top_fix;
	}
	public void setTop_fix(int top_fix) {
		this.top_fix = top_fix;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	public String getQ_nickname() {
		return q_nickname;
	}
	public void setQ_nickname(String q_nickname) {
		this.q_nickname = q_nickname;
	}
	public String getA_nickname() {
		return a_nickname;
	}
	public void setA_nickname(String a_nickname) {
		this.a_nickname = a_nickname;
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
