package com.ottss.domain;

public class BuyRecordDTO {
	private long buy_num; // 구매 내역 번호. 시퀀스 buy_seq
	private String buy_date; // 구매날짜(시각)
	private int equip; // 장착 여부. 0: 미장착(기본값) 1: 장착
	private String id; // 구매한 유저 아이디
	private int item_num; // 상품번호
	public long getBuy_num() {
		return buy_num;
	}
	public void setBuy_num(long buy_num) {
		this.buy_num = buy_num;
	}
	public String getBuy_date() {
		return buy_date;
	}
	public void setBuy_date(String buy_date) {
		this.buy_date = buy_date;
	}
	public int getEquip() {
		return equip;
	}
	public void setEquip(int equip) {
		this.equip = equip;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getItem_num() {
		return item_num;
	}
	public void setItem_num(int item_num) {
		this.item_num = item_num;
	}
}
