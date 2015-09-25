package cjava.walker.ibatis.entity;

public enum Sex {
	MALE(0, "man"), FEMALE(1, "woman"),UNKOWN(-1,"not specified");
	int code;
	String description;

	Sex(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public static Sex getByCode(int code){
		for(Sex sex : Sex.values()){
			if(sex.getCode() == code){
				return sex;
			}
		}
		throw new IllegalArgumentException("There is no instance corresponse to the code "+ code+" specified ");
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
