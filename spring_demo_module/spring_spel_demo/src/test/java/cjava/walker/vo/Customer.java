package cjava.walker.vo;

import java.util.Calendar;
import java.util.Date;

public class Customer {
	
	private String name;
	private Calendar dateOfBirth;
	private Address address;

	public Customer(String name,Date birth){
		this.name = name;
		this.dateOfBirth = Calendar.getInstance();
		dateOfBirth.setTime(birth);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}
	
	public Date getBirth(){
		return dateOfBirth == null ? null : dateOfBirth.getTime();
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Address getAddress() {
		return address;
	}
	
	/*public static class Builder{
		private String name;
		private Calendar dateOfBirth;
		private Address address;
		
		public Builder(String name){
			
		}
	}*/
}