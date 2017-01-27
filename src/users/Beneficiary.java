package users;

import java.util.ArrayList;
import java.util.List;

import assistance.Assistance;
import deliveries.ClothingDelivery;
import deliveries.Delivery;
import deliveries.FoodDelivery;
import deliveries.MedicalDelivery;
import main.AI;
import users.volunteers.DrivingVolunteer;

public class Beneficiary extends User {

	// Master list of all beneficiaries
	public static List<Beneficiary> users = new ArrayList<Beneficiary>();

	// Current requested item/service
	private String request = "";
	private int request_type = 0;
	private boolean waitingItem = false;
	private boolean wasRejected = false;

	// Used for linking volunteers and beneficiaries deliveries/services
	// together
	private Volunteer deliverer;

	public Beneficiary(String name, String address, String userID) {
		super(name, address, userID);
	}

	public void getRequest() {
		System.out.println("Beneficiary requested items: " + request);
	}

	public boolean isWaiting() {
		return waitingItem;
	}

	// Check any of the delivery lists contain the requested item
	public int checkDeliveryLists(String request) {
		if (FoodDelivery.checkItemAvailability(request))
			return Delivery.type_Food;
		else if (MedicalDelivery.checkItemAvailability(request))
			return Delivery.type_Medical;
		else if (ClothingDelivery.checkItemAvailability(request))
			return Delivery.type_Clothing;
		else
			return Delivery.type_Null;
	}

	public void setRequest(int type, int index) {
		switch (type) {
		case Delivery.type_Food:
			setRequest(FoodDelivery.getItemAtIndex(index));
			break;
		case Delivery.type_Medical:
			setRequest(MedicalDelivery.getItemAtIndex(index));
			break;
		case Delivery.type_Clothing:
			setRequest(ClothingDelivery.getItemAtIndex(index));
			break;
		}
	}

	public void setRequest(String newRequest) {
		wasRejected = false;
		int requestType = checkDeliveryLists(newRequest);
		if (requestType != 0) {
			request = newRequest;
			waitingItem = true;

			switch (requestType) {
			case Delivery.type_Food:
				FoodDelivery.requestItem(newRequest);
				break;
			case Delivery.type_Medical:
				MedicalDelivery.requestItem(newRequest);
				break;
			case Delivery.type_Clothing:
				ClothingDelivery.requestItem(newRequest);
				break;
			}
			request_type = requestType;
			deliverer = AI.organizeDelivery(requestType, newRequest, this);
			System.out.println("Your item \"" + newRequest + "\" has been requested. Waiting for delivery.");
		}
	}

	public void setShelterRequest(String address) {
		wasRejected = false;
		request_type = Assistance.type_Shelter;
		request = address;
		waitingItem = true;
		AI.organizeShelter(address, this);
	}

	public void setTransportRequest(Volunteer volunteer) {
		wasRejected = false;
		request_type = Assistance.type_Transportation;
		request = volunteer.getUserInfo();
		waitingItem = true;
		deliverer = AI.organizeTransportation(this, (DrivingVolunteer) volunteer);
	}

	public void confirmation() {
		if (waitingItem) {
			waitingItem = false;
			request_type = Delivery.type_Null;
			request = null;
			wasRejected = false;
			deliverer.clearNotification();
		}
	}

	public void rejected() {
		if (waitingItem) {
			// Re-add the requested item back to the lists.
			switch (request_type) {
			case Delivery.type_Food:
				FoodDelivery.addItem(request);
				break;
			case Delivery.type_Medical:
				MedicalDelivery.addItem(request);
				break;
			case Delivery.type_Clothing:
				ClothingDelivery.addItem(request);
				break;
			}
			waitingItem = false;
			request_type = Delivery.type_Null;
			request = "Your request was rejected. Please try again.";
			wasRejected = true;
			deliverer.clearNotification();
		}
	}

	public boolean lastRequestRejected() {
		return wasRejected;
	}

	public int getRequestType() {
		return request_type;
	}

}
