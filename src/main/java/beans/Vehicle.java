package beans;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
	
	private String auction;
	private String title;
	private String announcement;
	
	private static List<Vehicle> matches = new ArrayList<Vehicle>();
	
	public Vehicle() { }
	public Vehicle(String auction, String title, String announcement) {
		this.auction = auction;
		this.title = title;
		this.announcement = announcement;
	}
	
	public String getAuction() {
		return auction;
	}
	public void setAuction(String auction) {
		if (auction.contains(":"))
			auction = auction.substring(0, auction.indexOf(':') -1);
		this.auction = auction;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAnnouncement() {
		return announcement;
	}
	public void setAnnouncement(String announcement) {
		if (announcement.contains("Announcements\n"))
			announcement = announcement.replace("Announcements\n", "");
		this.announcement = announcement;
	}
	
	public static List<Vehicle> getMatches() {
		return matches;
	}
	public static void addAMatch(Vehicle vehicle) {
		matches.add(vehicle);
	}
	
	@Override
	public String toString() {
		return auction
				+ "\n" + title
				+ "\nANNOUNCEMENT: " + announcement;
	}
	
	public static String printMatches() {
		StringBuilder retStrb = new StringBuilder();
		
		for (Vehicle each : matches)
			retStrb.append("\n")
				.append(each.toString())
				.append("\n");
		
		return retStrb.toString().trim();
	}

}
