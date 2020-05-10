package analyzers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Auction;

public class AuctionAnalyzer {
	
	private static List<Auction> auctions;
	
	static {
		auctions = new ArrayList<Auction>();
		listOfAuctions();
	}
	
	public static boolean matchesAuctionList(Auction auction) {
		String name = auction.getName().toLowerCase();
		
		for (Auction each : auctions)
			if (name.contains(each.getName()))
				return true;
		
		return false;
	}
	
	private static void listOfAuctions() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			auctions = mapper.readValue(
					new File("auction_list.json"), 
					new TypeReference<List<Auction>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
