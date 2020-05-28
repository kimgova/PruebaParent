package springbootapp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Message implements Serializable {

	private long id;
	private String content;

	public Message() {
	}
	
	public Message(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		return "Value{" +
		        "id=" + id +
		        ", content='" + content + '\'' +
		        '}';
	}
}
