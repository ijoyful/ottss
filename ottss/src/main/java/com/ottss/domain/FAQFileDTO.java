package com.ottss.domain;

public class FAQFileDTO {
	private long fileNum; // 질문게시판에 첨부된 파일 번호
	private String s_fileName; // 서버에 저장된 파일명
	private String c_fileName; // 클라이언트가 저장한 파일명
	private long faq_num; // 파일을 첨부한 게시글 번호
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
	public long getFaq_num() {
		return faq_num;
	}
	public void setFaq_num(long faq_num) {
		this.faq_num = faq_num;
	}
}
