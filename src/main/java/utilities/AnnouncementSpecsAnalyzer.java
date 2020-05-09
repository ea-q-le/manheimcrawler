package utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.*;

public class AnnouncementSpecsAnalyzer {
	
	private static List<AnnouncementSpecifications> specs;
	
	static {
		specs = new ArrayList<AnnouncementSpecifications>();
		listOfSpecs();
	}
	
	public static boolean matchesAnnouncementSpecs(Vehicle vehicle) {
		String announcement = vehicle.getAnnouncement().toLowerCase();
		
		for (AnnouncementSpecifications each : specs)
			if (announcement.contains(each.getContent()))
				return true;
		
		return false;
	}
	
	private static void listOfSpecs() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			specs = mapper.readValue(
					new File("announcement_specs.json"), 
					new TypeReference<List<AnnouncementSpecifications>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
