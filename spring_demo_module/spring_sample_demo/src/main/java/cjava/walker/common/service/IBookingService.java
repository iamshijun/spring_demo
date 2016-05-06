package cjava.walker.common.service;

import java.util.List;

public interface IBookingService {

	public abstract void book(String... persons);

	public abstract List<String> findAllBookings();
	
	public abstract void dummy();

}