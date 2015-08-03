package com.zwei.utils;

public class Word {
	private  Double IWP;
	private  String cout;
	
	public Word() {
		// TODO Auto-generated constructor stub
	}
	
	public Word(String cout,double IWP){
		this.IWP=IWP;
		this.cout = cout;
	}

	public Double getIWP() {
		return IWP;
	}

	public void setIWP(Double iWP) {
		IWP = iWP;
	}

	public String getCout() {
		return cout;
	}

	public void setCout(String cout) {
		this.cout = cout;
	}
	
	
	
	
}
