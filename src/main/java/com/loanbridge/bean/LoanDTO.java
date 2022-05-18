package com.loanbridge.bean;

import java.io.Serializable;

public class LoanDTO implements Serializable{
	static final long serialVersionUID = 387249877;
	String bankName;
	String borrowerName;
	double principalAmt;
	int years;
	float rateInt;
	double totalAmt;
	int emis;
	double monthlyEmi;
	double amtPaid;
	double lumpsum;
	public double getLumpsum() {
		return lumpsum;
	}
	public void setLumpsum(double lumpsum) {
		this.lumpsum = lumpsum;
	}
	public double getAmtPaid() {
		return amtPaid;
	}
	public void setAmtPaid(double amtPaid) {
		this.amtPaid = amtPaid;
	}
	public double getMonthlyEmi() {
		return monthlyEmi;
	}
	public void setMonthlyEmi(double monthlyEmi) {
		this.monthlyEmi = monthlyEmi;
	}
	public int getEmis() {
		return emis;
	}
	public void setEmis(int emis) {
		this.emis = emis;
	}
	public double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public LoanDTO(String bkName, String name, double pamt, int term, float intrest){
		this.bankName = bkName;
		this.borrowerName = name;
		this.principalAmt = pamt;
		this.years = term;
		this.rateInt = intrest;
		calculateTotAmt(pamt, term, intrest);
		calculateEmis(term);
		calculateMonthlyEmi();
		//printPretty();
	}
	public void printPretty() {
		System.out.println("Bank Name: "+this.bankName+"\nBorrower Name: "+this.borrowerName+"\nPrinciple Amount: "+this.principalAmt+"\nLoan Term: "+this.years+"\nIntrest Rate: "+this.rateInt+"\nTotal Loan Amount: "+this.totalAmt+"\nNumber of EMIs: "+this.emis+"\nMonthly EMI Amount: "+this.monthlyEmi+"\nLumpsum Amount: "+this.lumpsum);
	}
	public void calculateTotAmt( double pamt, int term, float intrest) {
		this.totalAmt = pamt + (pamt*term*intrest)/100;
	}
	public void calculateEmis(int term) {
		this.emis = term*12;
	}
	public void calculateMonthlyEmi() {
		this.monthlyEmi = Math.round(this.totalAmt/this.emis);
	}
	public String getBorrowerName() {
		return borrowerName;
	}
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	public double getPrincipalAmt() {
		return principalAmt;
	}
	public void setPrincipalAmt(double principalAmt) {
		this.principalAmt = principalAmt;
	}
	public int getYears() {
		return years;
	}
	public void setYears(int years) {
		this.years = years;
	}
	public float getRateInt() {
		return rateInt;
	}
	public void setRateInt(float rateInt) {
		this.rateInt = rateInt;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
