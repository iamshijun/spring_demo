package feng.shi.controller.support.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import feng.shi.model.PhoneNumberModel;

public class StringToPhoneNumberConverter implements Converter<String, PhoneNumberModel>{
	
	private Pattern pattern = Pattern.compile("^(\\d{3,4})-(\\d{7,8})$");
	
	@Override
	public PhoneNumberModel convert(String source) {
		if(!StringUtils.hasLength(source)) 
			return null;
		
		Matcher matcher = pattern.matcher(source);
		if(matcher.find()){
			PhoneNumberModel model = new PhoneNumberModel();
			model.setAreaCode(matcher.group(1));
			model.setPhoneNumber(matcher.group(2));
			return model;
		}else{
			throw new IllegalArgumentException(String.format("类型转换失败，需要格式 如[010-12345678]，但输入是[%s]", source));
		}
	}

}
