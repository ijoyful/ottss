package com.ottss.domain;

public class ReportDTO {
	private long report_num; // 신고번호. 시퀀스 report_seq
	private String report_reason; // 신고 사유
	private String report_date; // 신고 날짜
	private String target_table; // 신고 게시글/댓글 소속 테이블
	private String target_num; // 신고 게시글/댓글 번호
	private String id; // 신고한 아이디
	public long getReport_num() {
		return report_num;
	}
	public void setReport_num(long report_num) {
		this.report_num = report_num;
	}
	public String getReport_reason() {
		return report_reason;
	}
	public void setReport_reason(String report_reason) {
		this.report_reason = report_reason;
	}
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public String getTarget_table() {
		return target_table;
	}
	public void setTarget_table(String target_table) {
		this.target_table = target_table;
	}
	public String getTarget_num() {
		return target_num;
	}
	public void setTarget_num(String target_num) {
		this.target_num = target_num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
