package beans;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
	
	private String title;
	private String announcement;
	
	private static List<Vehicle> matches = new ArrayList<Vehicle>();
	
	public Vehicle() { }
	public Vehicle(String title, String announcement) {
		this.title = title;
		this.announcement = announcement;
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
		return "Vehicle [title=" + title 
				+ ",\n"
				+ "announcements=" + announcement
				+ "]";
	}

}
