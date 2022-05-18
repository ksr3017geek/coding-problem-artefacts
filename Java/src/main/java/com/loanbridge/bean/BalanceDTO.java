package com.loanbridge.bean;

import java.io.Serializable;

public class BalanceDTO implements Serializable{
	static final long serialVersionUID = 387249877;
	String bankName;
	String borrowerName;
	int emiNum;
	public BalanceDTO(String bankName, String name, int emiCnt){
		this.bankName=bankName;
		this.borrowerName = name;
		this.emiNum = emiCnt;
	}
	public String getBorrowerName() {
		return borrowerName;
	}
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	public int getEmiNum() {
		return emiNum;
	}
	public void setEmiNum(int emiNum) {
		this.emiNum = emiNum;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
}
