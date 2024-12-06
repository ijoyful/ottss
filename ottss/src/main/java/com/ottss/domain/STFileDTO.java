package com.ottss.domain;

public class STFileDTO {
	private long fileNum; // 자랑/분석게시판에 업로드된 파일 번호. 시퀀스 st_file_seq
	private String s_fileName; // 서버에 저장된 파일명
	private String c_fileName; // 클라이언트가 저장한 파일명
	private long st_num; // 파일이 첨부된 글 번호
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
	public long getSt_num() {
		return st_num;
	}
	public void setSt_num(long st_num) {
		this.st_num = st_num;
	}
	
}
