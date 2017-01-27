package main;

import assistance.Assistance;
import deliveries.Delivery;
import users.Beneficiary;
import users.Volunteer;
import users.volunteers.DrivingVolunteer;
import users.volunteers.MedicalVolunteer;

public class AI {

	public static Volunteer organizeDelivery(int requestType, String newRequest, Beneficiary b) {
		// The AI would normally get a volunteer that is close to the
		// beneficiary, but for now we just get the first available volunteer
		Volunteer v = null;
		switch (requestType) {
		case Delivery.type_Medical:
			v = getAvailbleMedicalVolunteer();
			break;
		case Delivery.type_Food:
		case Delivery.type_Clothing:
			v = getAvailbleVolunteer();
			break;
		}
		v.currentDeliveryJobType = requestType;
		v.notificationMessage = "Deliver " + newRequest + " to " + b.getUserAddress();
		return v;
	}

	public static void organizeShelter(String address, Beneficiary beneficiary) {
		beneficiary.confirmation();
		System.out.println("You are now registered at " + address + ".");
	}

	public static Volunteer organizeTransportation(Beneficiary beneficiary, DrivingVolunteer volunteer) {
		volunteer.currentDeliveryJobType = Assistance.type_Transportation;
		volunteer.notificationMessage = "Transport " + beneficiary.getUserName() + ", located at "
				+ beneficiary.getUserAddress();
		volunteer.waitingBeneficiary = beneficiary;
		return volunteer;
	}

	// The AI would normally get a volunteer that is close to the
	// beneficiary's address (which would be given), but for now we just get the
	// first available volunteer
	private static Volunteer getAvailbleMedicalVolunteer() {
		// Get the first available medical volunteer
		for (Volunteer v : Volunteer.users)
			if (v instanceof MedicalVolunteer)
				if (v.currentDeliveryJobType == Delivery.type_Null)
					return v;

		// If no medical volunteers are available, get the first non-medical
		// one.
		for (Volunteer v : Volunteer.users)
			if (v.currentDeliveryJobType == Delivery.type_Null)
				return v;
		return null;
	}

	private static Volunteer getAvailbleVolunteer() {
		// We're prioritizing driving volunteers since they are the most useful
		// in general.

		// Get the first available driver volunteer
		for (Volunteer v : Volunteer.users)
			if (v instanceof DrivingVolunteer)
				if (v.currentDeliveryJobType == Delivery.type_Null)
					return v;

		// If no driving volunteers are available, get the first non-driving
		// one.
		for (Volunteer v : Volunteer.users)
			if (v.currentDeliveryJobType == Delivery.type_Null)
				return v;
		return null;
	}

}
