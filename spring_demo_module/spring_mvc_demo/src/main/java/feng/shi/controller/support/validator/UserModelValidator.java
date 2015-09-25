package feng.shi.controller.support.validator;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import feng.shi.model.UserModel;

public class UserModelValidator implements Validator {
	
    private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z]\\w{4,19}");  
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9]{5,20}");
    
    private static final Set<String> FORBINDDDEN_WORD_SET = new HashSet<String>();
    
    static {  
       FORBINDDDEN_WORD_SET.add("fuck");  
       FORBINDDDEN_WORD_SET.add("admin");  
    }    
    @Override  
    public boolean supports(Class<?> clazz) {  
    	//ֻ��UserModel���͵�Ŀ�����ʵʩ��֤  
       return UserModel.class.isAssignableFrom(clazz);
    }  
    @Override  
    public void validate(Object target, Errors errors) {  
       //�����ʾ���Ŀ������ username ����Ϊ�գ����ʾ���󣨼������ֹ��ж��Ƿ�Ϊ�գ�  
       ValidationUtils.rejectIfEmpty(errors, "username", "username.not.empty");//username Ϊ UserModel��field name
       //errors��ʵ �󲿷�Ӧ�ö��� BindResult...
        
       UserModel user = (UserModel) target;  
        
       if(!USERNAME_PATTERN.matcher(user.getUsername()).matches()) {  
           errors.rejectValue("username", "username.not.illegal");//����û������Ϸ�  
       }  
        
       for(String forbiddenWord : FORBINDDDEN_WORD_SET) {  
           if(user.getUsername().contains(forbiddenWord)) {  
              errors.rejectValue("username", "username.forbidden", new Object[]{forbiddenWord}, "�����û��������Ƿ��ؼ���");//�û����������ιؼ���  
              break;  
           }  
       }  
       if(!PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {  
           errors.rejectValue("password","password.not.illegal", "���벻�Ϸ�");//���벻�Ϸ�  
       }    
    }  
} 