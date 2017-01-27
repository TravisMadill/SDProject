package users;

public abstract class User {
	// Generic user information used across all users
	private String name;
	private String address;
	private String userID;

	public User(String name, String address, String userID) {
		this.name = name;
		this.address = address;
		this.userID = userID;
	}

	public String getUserInfo() {
		return name + " " + address + " " + userID;
	}

	public String getUserName() {
		return name;
	}

	public String getUserAddress() {
		return address;
	}

	public void setUserAddress(String newUserAddress) {
		address = newUserAddress;
	}

	public String getUserID() {
		return userID;
	}

}
