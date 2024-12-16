package com.ottss.domain;

public class PointRecordDTO { // 포인트 내역
	private long pt_num; // 포인트 내역 번호. 시퀀스 pt_seq
	private int categories; // 포인트 사용 카테고리. 소비: 0x 획득: 1x
	private String category;
	private int point; // 포인트
	private int left_point; // 잔여 포인트
	private String pt_date; // 포인트 내역 발생 날짜
	private String id; // 포인트 관련 유저 아이디
	public long getPt_num() {
		return pt_num;
	}
	public void setPt_num(long pt_num) {
		this.pt_num = pt_num;
	}
	public int getCategories() {
		return categories;
	}
	public void setCategories(int categories) {
		this.categories = categories;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getLeft_point() {
		return left_point;
	}
	public void setLeft_point(int left_point) {
		this.left_point = left_point;
	}
	public String getPt_date() {
		return pt_date;
	}
	public void setPt_date(String pt_date) {
		this.pt_date = pt_date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
