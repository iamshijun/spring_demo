package cjava.walker.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FixMenu {
	
	public final static FixMenu ZSLL = new FixMenu(-100, "只是浏览", "no_url");
	//ZSLL children
	public final static FixMenu QWSS = new FixMenu(-110, "全文搜索", "entry_word_search_result.html");
	public final static FixMenu GJSS = new FixMenu(-111, "高级搜索", "entry_column_search_result.html");
	public final static FixMenu ZSPH = new FixMenu(-120, "知识排行", "max_entry.html");
	public final static FixMenu ZXZS = new FixMenu(-130, "最新知识", "last_entry.html");
	
	static{
		ZSLL.addChildren(QWSS,GJSS,ZSPH,ZXZS);
	}

	private Integer id;
	private String text;
	private Attribute attributes;
	private List<FixMenu> children;

	public FixMenu(Integer id, String text, String url) {
		super();
		this.id = id;
		this.text = text;
		this.attributes = new Attribute(url);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Attribute getAttributes() {
		return attributes;
	}

	public void setAttributes(Attribute attributes) {
		this.attributes = attributes;
	}

	public List<FixMenu> getChildren() {
		return children;
	}

	public void setChildren(List<FixMenu> children) {
		this.children = children;
	}
	
	public void addChildren(FixMenu... children){
		if(this.children== null){
			this.children = new ArrayList<FixMenu>();
		}
		this.children.addAll(Arrays.asList(children));
	}
	

	class Attribute {
		String url;

		public Attribute(String url) {
			super();
			this.url = url;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}
}
