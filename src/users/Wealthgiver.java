package users;

import java.util.ArrayList;
import java.util.List;

public class Wealthgiver extends User {
	
	// Master list of all wealth givers
	public static List<Wealthgiver> users = new ArrayList<Wealthgiver>();

	public Wealthgiver(String name, String address, String userID) {
		super(name, address, userID);
	}

}
