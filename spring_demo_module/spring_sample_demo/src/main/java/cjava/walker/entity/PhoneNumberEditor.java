package cjava.walker.entity;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class PhoneNumberEditor extends PropertyEditorSupport {
	
	private Pattern pattern = Pattern.compile("^(\\d{3,4})-(\\d{7,8})$");
	
    @Override  
    public void setAsText(String text) throws IllegalArgumentException {  
        if(text == null || !StringUtils.hasLength(text)) {  
            setValue(null); //���ûֵ����ֵΪnull  
        }  
        Matcher matcher = pattern.matcher(text);  
        if(matcher.matches()) {  
            PhoneNumber phoneNumber = new PhoneNumber();  
            phoneNumber.setAreaCode(matcher.group(1));  
            phoneNumber.setPhoneNumber(matcher.group(2));  
            setValue(phoneNumber);  
        } else {  
            throw new IllegalArgumentException(String.format("����ת��ʧ�ܣ���Ҫ��ʽ[010-12345678]��������Ϊ[%s]", text));  
        }  
    }  
    @Override  
    public String getAsText() {  
        PhoneNumber phoneNumber = ((PhoneNumber)getValue());  
        return phoneNumber == null ? "" : phoneNumber.getAreaCode() + "-" + phoneNumber.getPhoneNumber();  
    }  
}  