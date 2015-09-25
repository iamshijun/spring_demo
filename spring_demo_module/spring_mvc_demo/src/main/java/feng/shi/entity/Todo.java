package feng.shi.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Todo {

	private Long id;
	private String title;
	private String description;

	public Todo(){
		
	}
	
	public Todo(TodoBuilder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.description = builder.description;
	}

	public static class TodoBuilder {
		private Long id;
		private String title;
		private String description;

		public TodoBuilder id(Long id) {
			this.id = id;
			return this;
		}
		public TodoBuilder title(String title){
			this.title = title;
			return this;
		}
		public TodoBuilder description(String description){
			this.description = description;
			return this;
		}

		public Todo build() {
			return new Todo(this);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
