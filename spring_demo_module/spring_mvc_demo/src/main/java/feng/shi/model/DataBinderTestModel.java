package feng.shi.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("rawtypes")
public class DataBinderTestModel {
	
	private String username;
	private boolean bool;// Boolean值测试
	private SchoolInfoModel schooInfo;
	private List<String> hobbyList;// 集合测试，此处可以改为数组/Set进行测试
	private Map map;// Map测试
	private PhoneNumberModel phoneNumber;// String->自定义对象的转换测试
	private Date date;// 日期类型测试
	private UserState state;// String——>Enum类型转换测试
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isBool() {
		return bool;
	}
	public void setBool(boolean bool) {
		this.bool = bool;
	}
	public SchoolInfoModel getSchooInfo() {
		return schooInfo;
	}
	public void setSchooInfo(SchoolInfoModel schooInfo) {
		this.schooInfo = schooInfo;
	}
	public List<String> getHobbyList() {
		return hobbyList;
	}
	public void setHobbyList(List<String> hobbyList) {
		this.hobbyList = hobbyList;
	}
	
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public PhoneNumberModel getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(PhoneNumberModel phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public UserState getState() {
		return state;
	}
	public void setState(UserState state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}
