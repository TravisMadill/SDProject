package users;

import java.util.ArrayList;
import java.util.List;

import deliveries.Delivery;

public class Volunteer extends User {

	// Master list of all volunteers
	public static List<Volunteer> users = new ArrayList<Volunteer>();

	// Current requests bring asked of volunteers
	public int currentDeliveryJobType = Delivery.type_Null;
	public String notificationMessage = "";

	public Volunteer(String name, String address, String userID) {
		super(name, address, userID);
	}

	public void clearNotification() {
		currentDeliveryJobType = Delivery.type_Null;
		notificationMessage = "";
	}

}
