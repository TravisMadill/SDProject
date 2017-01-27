package users.volunteers;

import users.Volunteer;

public class MedicalVolunteer extends Volunteer {

	// A specialized class used to prioritize medical deliveries
	public MedicalVolunteer(String name, String address, String userID) {
		super(name, address, userID);
	}

}
