package com.ottss.domain;

public class PointShopDTO {
	private long item_num;
	private String item_name;
	private String categories;
	private int amount;
	private String item_explain;
	
	
	
	public String getItem_explain() {
		return item_explain;
	}
	public void setItem_explain(String item_explain) {
		this.item_explain = item_explain;
	}
	public long getItem_num() {
		return item_num;
	}
	public void setItem_num(long itemNum) {
		this.item_num = itemNum;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String itemName) {
		this.item_name = itemName;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
