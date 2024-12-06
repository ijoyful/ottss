package com.ottss.domain;

public class NoticeFileDTO {
	private long fileNum; // 공지게시판에 첨부된 파일 번호. 시퀀스 n_file_seq
	private String s_fileName; // 서버에 저장된 파일
	private String c_fileName; // 클라이언트가 저장한 파일명
	private long n_num; // 파일을 첨부한 공지게시판 글 번호
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
	public long getN_num() {
		return n_num;
	}
	public void setN_num(long n_num) {
		this.n_num = n_num;
	}
}
