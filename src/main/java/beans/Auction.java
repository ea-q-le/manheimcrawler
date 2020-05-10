package beans;

public class Auction {
	
	private String name;
	
	public Auction() { }
	public Auction(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Auction [name=" + name;
	}

}
