package beans;

import java.util.ArrayList;
import java.util.List;

import utilities.ConfigReader;

public class Vehicle {
	
	public static final short YEAR_OLDEST;
	public static final short YEAR_YOUNGEST;
	public static final int ODOMETER_MAX;
	static {
		YEAR_OLDEST = Short.parseShort(
				ConfigReader.getProperty("youngerThan"));
		YEAR_YOUNGEST = Short.parseShort(
				ConfigReader.getProperty("olderThan"));
		ODOMETER_MAX = Integer.parseInt(
				ConfigReader.getProperty("lowerThan"));
	}
	
	private String auction;
	private String lane;
	private short year;
	private String title;
	private int odometer;
	private String vin;
	private String announcement;
	private boolean isAvailable;
	
	private static List<Vehicle> matches = new ArrayList<Vehicle>();
	
	public Vehicle() { }
	public Vehicle(String auction, String lane, 
			short year, String title, int odometer,
			String vin,
			String announcement,
			boolean isAvailable) {
		this.auction = auction;
		this.lane = lane;
		this.year = year;
		this.title = title;
		this.odometer = odometer;
		this.vin = vin;
		this.announcement = announcement;
		this.isAvailable = isAvailable;
	}
	
	public String getAuction() {
		return auction;
	}
	public void setAuction(String auction) {
		if (auction.contains(":"))
			auction = auction.substring(0, auction.indexOf(':') -1);
		this.auction = auction;
	}
	public String getLane() {
		return lane;
	}
	public void setLane(String lane) {
		this.lane = lane;
	}
	public short getYear() {
		return year;
	}
	public void setYear(short year) {
		this.year = year;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getOdometer() {
		return odometer;
	}
	public void setOdometer(int odometer) {
		this.odometer = odometer;
	}
	public String getVIN() {
		return vin;
	}
	public void setVIN(String vin) {
		this.vin = (vin.length() == 17) 
				? vin.substring(0, 9) + " " 
					+ vin.substring(9, 11) + " [ "
					+ vin.substring(11) + " ]"
				: vin;
	}
	public String getAnnouncement() {
		return announcement;
	}
	public void setAnnouncement(String announcement) {
		if (announcement.contains("Announcements\n"))
			announcement = announcement.replace("Announcements\n", "");
		this.announcement = announcement;
	}	
	public boolean getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
		
	public static List<Vehicle> getMatches() {
		return matches;
	}
	public static void addAMatch(Vehicle vehicle) {
		matches.add(vehicle);
	}
	
	@Override
	public String toString() {
		return auction + "\tLane: " + lane
				+ "\n" + year + " " + title + " w/ " + odometer + " miles"
				+ "\n[VIN]: " + vin 
				+ "\n*Announcements*: " + announcement
				+ ( isAvailable ? "" : "\n***SOLD***" );
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
