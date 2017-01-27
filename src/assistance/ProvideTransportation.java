package assistance;

import java.util.ArrayList;
import java.util.List;

import users.Volunteer;

public class ProvideTransportation extends Assistance {
	
	//List of all available volunteers that can transport beneficiaries
	static List<Volunteer> transportAvailable = new ArrayList<Volunteer>();

	public static boolean containsUser(Volunteer curUser) {
		return transportAvailable.contains(curUser);
	}
	
	public static void addUser(Volunteer curUser){
		transportAvailable.add(curUser);
	}
	
	public static void removeUser(Volunteer curUser){
		transportAvailable.remove(curUser);
	}
	
	public static int numAvailableVolunteers(){
		return transportAvailable.size();
	}
	
	public static boolean isValidIndex(int index) {
		return index < transportAvailable.size();
	}
	
	public static void displayAllAvailbleVolunteers() {
		for (int i = 0; i < transportAvailable.size(); i++)
			System.out.println(" " + (i+1) + ": "
					+ transportAvailable.get(i).getUserName());
	}

	public static Volunteer getVolunteer(int index) {
		return transportAvailable.get(index);
	}
}
