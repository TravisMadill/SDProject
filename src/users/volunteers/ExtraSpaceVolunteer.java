package users.volunteers;

import users.Beneficiary;
import users.Volunteer;

public class ExtraSpaceVolunteer extends Volunteer {

	// Used for getting beneficiary's request information for a shelter request
	public Beneficiary waitingBeneficiary;

	public ExtraSpaceVolunteer(String name, String address, String userID) {
		super(name, address, userID);
	}

}
