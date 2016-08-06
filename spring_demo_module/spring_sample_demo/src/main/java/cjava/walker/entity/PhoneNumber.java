package cjava.walker.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PhoneNumber{
	
	private String areaCode;// ÇøºÅ
	private String phoneNumber;// µç»°ºÅÂë

	public String getAreaCode() {
		return areaCode;
	}
	
	public PhoneNumber(){}
	
	
	public PhoneNumber(String areaCode, String phoneNumber) {
		super();
		this.areaCode = areaCode;
		this.phoneNumber = phoneNumber;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}