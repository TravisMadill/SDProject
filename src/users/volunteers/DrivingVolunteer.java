package users.volunteers;

import users.Beneficiary;
import users.Volunteer;

public class DrivingVolunteer extends Volunteer {

	// Used for getting Beneficiary's information for a transportation request
	public Beneficiary waitingBeneficiary;

	public DrivingVolunteer(String name, String address, String userID) {
		super(name, address, userID);
	}

}
