package com.ottss.domain;

public class FreeFileDTO { // 자유게시판 게시글에 첨부한 파일 이름
	private long fileNum; // 업로드된 파일 번호. 시퀀스 fb_file_seq
	private String s_fileName; // 서버에 저장된 파일 이름
	private String c_fileName; // 클라이언트가 저장해놓은 파일 이름
	private long fb_num; // 파일을 업로드한 자유게시판 게시글 번호
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
	public long getFb_num() {
		return fb_num;
	}
	public void setFb_num(long fb_num) {
		this.fb_num = fb_num;
	}
	
}
