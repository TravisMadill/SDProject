package deliveries;

import java.util.ArrayList;
import java.util.List;

public class ClothingDelivery extends Delivery {

	// List of clothing available
	static List<String> itemsAvailable = new ArrayList<String>();

	public static void addItem(String item) {
		itemsAvailable.add(item);
	}
	
	public static int checkItemStocks(){
		return itemsAvailable.size();
	}
	
	public static boolean checkItemAvailability(String item) {
		return itemsAvailable.contains(item);
	}

	public static String getItemAtIndex(int index){
		return itemsAvailable.get(index);
	}

	public static void requestItem(String item) {
		if (checkItemAvailability(item) == true)
			itemsAvailable.remove(item);
		else System.out.println("Clothing item \"" + item + "\" is not available");
	}

	public static void displayAvailableItems() {
		for (int i = 0; i < itemsAvailable.size(); i++)
			System.out.println((i+1) + " " + itemsAvailable.get(i));
	}

	public static boolean isValidIndex(int index) {
		return index < itemsAvailable.size();
	}
}
