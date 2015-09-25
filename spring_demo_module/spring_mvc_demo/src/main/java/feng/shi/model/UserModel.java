package feng.shi.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserModel {

	private String username;
	private String password;
	private String realname; // ’Ê µ–’√˚
	private WorkInfoModel workInfo;
	private SchoolInfoModel schoolInfo;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public WorkInfoModel getWorkInfo() {
		return workInfo;
	}

	public void setWorkInfo(WorkInfoModel workInfo) {
		this.workInfo = workInfo;
	}

	public SchoolInfoModel getSchoolInfo() {
		return schoolInfo;
	}

	public void setSchoolInfo(SchoolInfoModel schoolInfo) {
		this.schoolInfo = schoolInfo;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
}