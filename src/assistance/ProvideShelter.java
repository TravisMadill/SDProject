package assistance;

import java.util.ArrayList;
import java.util.List;

public class ProvideShelter extends Assistance {
	// A list of addresses available as shelter
	static List<String> shelterAvailable = new ArrayList<String>();

	public static void addLocation(String item) {
		shelterAvailable.add(item);
	}

	public static void displayAllAvailbleAddresses() {
		for (int i = 0; i < shelterAvailable.size(); i++)
			System.out.println(" " + (i + 1) + ": " + shelterAvailable.get(i));
	}

	// Normally, this function would implement AI functions to determine the
	// closest address to the one provided, but since we don't have that now, we
	// just return the first entry in the list.
	public static String getClosest(String address) {
		return shelterAvailable.get(0);
	}

	public static boolean containsAddress(String userAddress) {
		return shelterAvailable.contains(userAddress);
	}

	public static void removeLocation(String userAddress) {
		shelterAvailable.remove(userAddress);
	}

	public static int numAvailableAddresses() {
		return shelterAvailable.size();
	}

	public static boolean isValidIndex(int index) {
		return index < shelterAvailable.size();
	}

	public static String getAddress(int index) {
		return shelterAvailable.get(index);
	}
}
