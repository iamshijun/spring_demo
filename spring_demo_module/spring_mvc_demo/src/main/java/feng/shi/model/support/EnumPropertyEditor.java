package feng.shi.model.support;

import java.beans.PropertyEditorSupport;

public class EnumPropertyEditor extends PropertyEditorSupport{
	
	private Class<? extends Enum<?>> clazz;
	private boolean caseSensitive ;
	
	public EnumPropertyEditor(Class<? extends Enum<?>> clazz) {
		this(clazz, true);
	}
	
	public EnumPropertyEditor(Class<? extends Enum<?>> clazz,boolean caseSensitive) {
		this.clazz = clazz;
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			//��ϧEnum.valueOf������һ��������Ҫȷ�е�ö������Enum.valueOf(clazz,text)�������� 
		    //Enum.valueOf(UserState.class,text);
			
			for (Object e : clazz.getEnumConstants()) {
				String type = e.toString();
				if (!caseSensitive) {
					text = text.toLowerCase();
					type = type.toLowerCase();
				}
				if (type.equals(text)) {
					setValue(e);
					return;
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
	}
	
	@Override
	public String getAsText() {
		return (String)getValue();
	}
}
