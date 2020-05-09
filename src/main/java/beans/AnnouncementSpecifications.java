package beans;

public class AnnouncementSpecifications {
	private String content;
	
	public AnnouncementSpecifications() { }
	public AnnouncementSpecifications(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "Announcement Specification [content=" + content + "]";
	}

}
