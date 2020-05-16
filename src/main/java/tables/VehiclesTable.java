package tables;

import java.math.BigInteger;
import java.sql.Date;

import beans.Vehicle;
import utilities.DBUtils;

public class VehiclesTable {
	public static final String TABLE_NAME = "vehicles";
	
	/** VEHICLES 	-> table ddl
	 * 
	 * id			-> bigint
	 * year			-> int
	 * make_model	-> varchar(128)
	 * vin			-> varchar(32)
	 * odometer		-> int
	 * auction		-> varchar(32)
	 * lane			-> varchar(16)
	 * run_date		-> Datetime
	 * announcements-> varchar(256)
	 * available	-> bool
	 */
	private BigInteger id;
	private int year;
	private String make_model;
	private String vin;
	private int odometer;
	private String auction;
	private String lane;
	private Date run_date;
	private String announcements;
	private boolean available;
	
	public VehiclesTable(int year, String make_model, String vin, 
			int odometer, String auction, String lane,
			Date run_date, String announcements, boolean available) {

		this.year = year;
		this.make_model = make_model;
		this.vin = vin;
		this.odometer = odometer;
		this.auction = auction;
		this.lane = lane;
		this.run_date = run_date;
		this.announcements = announcements;
		this.available = available;
		
	}

	public BigInteger getId() {
		return id;
	}
	/** Do NOT set the ID manually,
	 * it is being auto-incremented within the DB.
	 * This method is to be used by Object mappers.
	 * @param id of BigInteger type.
	 */
	public void setId(BigInteger id) {
		this.id = id;
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
	public Date getRun_date() {
		return run_date;
	}
	public void setRun_date(Date run_date) {
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
	
	@Override
	public String toString() {
		return "VEHICLES row ["
				+ " | id:" + id
				+ " | year:" + year
				+ " | make_model:" + make_model
				+ " | vin:" + vin
				+ " | odometer:" + odometer
				+ " | auction:" + auction
				+ " | lane:" + lane
				+ " | run_date:" + run_date
				+ " | announcements:" + announcements
				+ " | available:" + available
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
					vehicle.getRunDate(), 
					vehicle.getAnnouncement(), 
					vehicle.getIsAvailable() ));
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
					+ "run_date, announcements, available)\n"
				+ "VALUES (\n" 
					+ row.getYear() + ", "
					+ "\"" + row.getMake_model() + "\"" + ", "
					+ "\"" + row.getVin() + "\"" + ", "
					+ row.getOdometer() + ", "
					+ "\"" + row.getAuction() + "\"" + ", "
					+ "\"" + row.getLane() + "\"" + ", "
					+ row.getRun_date() + ", "
					+ "\"" + row.getAnnouncements() + "\"" + ", "
					+ row.getIsAvailable()
				+ ");");
	}

}
