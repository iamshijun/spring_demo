package cjava.walker.spel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import cjava.walker.vo.Customer;

@Component("colSel")
public class CollectionSelection {

	private List<Customer> customers;
	
	public List<Customer> getCustomers() {
		return customers;
	}
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
	{//mock
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		customers = new ArrayList<Customer>();
		for (int i = 0; i < 100; ++i) {
			try {
				int year = 1914 + (int)(Math.random() * 100);
				Date birth = dateFormat.parse(year + "-11-11 00:00:00");
				Customer customer = new Customer("cus"+i, birth);
				customers.add(customer);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
