package com.loanbridge.bean;

import java.io.Serializable;

public class PaymentDTO  implements Serializable{
	static final long serialVersionUID = 387249877;
	String bankName;
	String borrowerName;
	double lumpsumAmt;
	int emiNum;
	public PaymentDTO(String bankName, String name, double lumpAmt, int emiNum) {
		this.bankName=bankName;
		this.borrowerName = name;
		this.lumpsumAmt = lumpAmt;
		this.emiNum = emiNum;
	}
	public String getBorrowerName() {
		return borrowerName;
	}
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	public double getLumpsumAmt() {
		return lumpsumAmt;
	}
	public void setLumpsumAmt(double lumpsumAmt) {
		this.lumpsumAmt = lumpsumAmt;
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
