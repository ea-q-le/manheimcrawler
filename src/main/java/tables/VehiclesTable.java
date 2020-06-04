package tables;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import beans.Vehicle;
import utilities.DBUtils;

public class VehiclesTable {
	public static final String TABLE_NAME = "vehicles";
	public static final List<String> allVins;
	
	static {
		String query = "SELECT vin " 
				+ "FROM " + DBUtils.SCHEMA + "." + VehiclesTable.TABLE_NAME + ";";
		allVins = DBUtils.getColumnDataStringList(query, "vin");
	}
	
	/** VEHICLES 	-> table ddl
	 * 
	 * id			-> bigint (primary, auto_increment, not null)
	 * year			-> int
	 * make_model	-> varchar(128)
	 * vin			-> varchar(32)
	 * odometer		-> int
	 * auction		-> varchar(32)
	 * lane			-> varchar(16)
	 * run_date		-> datetime
	 * announcements-> varchar(256)
	 * available	-> bool (not null)
	 * found_date	-> datetime (not null)
	 */
	private BigInteger id;
	private int year;
	private String make_model;
	private String vin;
	private int odometer;
	private String auction;
	private String lane;
	private Timestamp run_date;
	private String announcements;
	private boolean available;
	private Timestamp found_date;
	
	public VehiclesTable(int year, String make_model, String vin, 
			int odometer, String auction, String lane,
			Timestamp run_date, String announcements, boolean available,
			Timestamp found_date) {

		this.year = year;
		this.make_model = make_model;
		this.vin = vin;
		this.odometer = odometer;
		this.auction = auction;
		this.lane = lane;
		this.run_date = run_date;
		this.announcements = announcements;
		this.available = available;
		this.found_date = found_date;
		
	}

	public BigInteger getId() {
		return id;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getMake_model() {
		return make_model;
	}
	public void setMake_model(String make_model) {
		this.make_model = make_model;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public int getOdometer() {
		return odometer;
	}
	public void setOdometer(int odometer) {
		this.odometer = odometer;
	}
	public String getAuction() {
		return auction;
	}
	public void setAuction(String auction) {
		this.auction = auction;
	}
	public String getLane() {
		return lane;
	}
	public void setLane(String lane) {
		this.lane = lane;
	}
	public Timestamp getRun_date() {
		return run_date;
	}
	public void setRun_date(Timestamp run_date) {
		this.run_date = run_date;
	}
	public String getAnnouncements() {
		return announcements;
	}
	public void setAnnouncements(String announcements) {
		this.announcements = announcements;
	}
	public boolean getIsAvailable() {
		return available;
	}
	public void setIsAvailable(boolean available) {
		this.available = available;
	}
	public Timestamp getFound_date() {
		return found_date;
	}
	
	@Override
	public String toString() {
		return "VEHICLES row ["
				+ " | id:" + getId()
				+ " | year:" + getYear()
				+ " | make_model:" + getMake_model()
				+ " | vin:" + getVin()
				+ " | odometer:" + getOdometer()
				+ " | auction:" + getAuction()
				+ " | lane:" + getLane()
				+ " | run_date:" + getRun_date()
				+ " | announcements:" + getAnnouncements()
				+ " | available:" + getIsAvailable()
				+ " | found_date:" + getFound_date().toString()
				+ " | ]";
	}
	
	/**
	 * Given a Vehicle object, the method will implicitly create
	 * a new VehiclesTable row object and invoke
	 * the private 'insertIntoVehicles(VehiclesTable row) method
	 * to insert the Vehicle values into the DB 'vehicles' table.
	 * @param vehicle object of Vehicle type
	 */
	public static void insertIntoVehicles(Vehicle vehicle) {
		insertIntoVehicles(
				new VehiclesTable(
					vehicle.getYear(), 
					vehicle.getTitle(), 
					vehicle.getVIN(), 
					vehicle.getOdometer(), 
					vehicle.getAuction(), 
					vehicle.getLane(), 
					vehicle.getRunTimestamp(), 
					vehicle.getAnnouncement(), 
					vehicle.getIsAvailable(),
					vehicle.getFoundTimestamp()));
	}
	
	/**
	 * Given a VehiclesTable object, the method will implicitly call
	 * the DBUtils.executeUpdate(String query) method
	 * to convert the VehiclesTable values to String query.
	 * @param row object of VehiclesTable type
	 */
	private static void insertIntoVehicles(VehiclesTable row) {
		DBUtils.executeUpdate(
				"INSERT INTO " 
						+ DBUtils.SCHEMA + "." 
						+ VehiclesTable.TABLE_NAME 
				+ " (\n" 
					+ "year, make_model, vin,\n"
					+ "odometer, auction, lane,\n"
					+ "run_date, announcements, available,\n"
					+ "found_date)\n"
				+ "VALUES (\n" 
					+ row.getYear() + ", "
					+ "\"" + row.getMake_model() + "\"" + ", "
					+ "\"" + row.getVin() + "\"" + ", "
					+ row.getOdometer() + ", "
					+ "\"" + row.getAuction() + "\"" + ", "
					+ "\"" + row.getLane() + "\"" + ", "
					+ "\"" + row.getRun_date() + "\"" + ", "
					+ "\"" + row.getAnnouncements() + "\"" + ", "
					+ row.getIsAvailable() + ", "
					+ "\"" + row.getFound_date().toString() + "\""
				+ ");");
	}

	/**
	 * Given a VIN of a Vehicle object as a String,
	 * the method returns whether that VIN is present in the
	 * 'vehicles' table within the database.
	 * ***IMPORTANT***: The VIN should be passed in the way it is 
	 * formatted the first time. Hence it is necessary to utilize
	 * the getVIN() instead of passing the String fetched from the site.
	 * ***NOTE***: The method is dependent on successful generation
	 * of the List<String> allVins as the CONSTANT of this class.
	 * @param vin fetched using a vehicle object's getVIN()  
	 * @return boolean value whether the given vin exists in 'vehicles' table
	 */
	public static boolean vehicleExistsByVIN(String vin) {
		return allVins.contains(vin);
	}
}
