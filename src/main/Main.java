package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import assistance.Assistance;
import assistance.ProvideShelter;
import assistance.ProvideTransportation;
import deliveries.ClothingDelivery;
import deliveries.Delivery;
import deliveries.FoodDelivery;
import deliveries.MedicalDelivery;
import users.Beneficiary;
import users.Volunteer;
import users.Wealthgiver;
import users.volunteers.DrivingVolunteer;
import users.volunteers.ExtraSpaceVolunteer;
import users.volunteers.MedicalVolunteer;
import users.wealthgivers.ClothingShop;
import users.wealthgivers.Pharmacy;
import users.wealthgivers.PropertyDonor;
import users.wealthgivers.Supermarket;
import users.wealthgivers.VehicleDonor;

public class Main {

	public static boolean running = true;
	public static BufferedReader input;

	public static void main(String[] args) {
		// Fill our example program with sample data; at least one of each
		// respective type.
		populateUsers();
		populateDonationLists();

		input = new BufferedReader(new InputStreamReader(System.in));

		// Main program loop
		// This section logs in the appropriate user and takes them to the
		// appropriate part of the program
		while (running) {
			System.out.println("Welcome to iVolunteer.\nEnter \"-1\" at any time to quit the program.");
			System.out.println("Welcome screen\n" + " Log in as:\n" + " 1: Beneficiary\n" + " 2: Volunteer\n"
					+ " 3: Wealth giver");
			int choice = getUserInput();
			switch (choice) {
			case 1: // Beneficiary
				System.out.println("Beneficiary chosen.");
				System.out.println(" Please select a beneficiary to log in as.");
				// Displays all the beneficiaries
				for (int i = 0; i < Beneficiary.users.size(); i++)
					System.out.println(" " + i + ": " + Beneficiary.users.get(i).getUserName() + " ("
							+ Beneficiary.users.get(i).getClass().getSimpleName() + ")");
				int b_user = getUserInput();
				if (b_user == -1) {
					running = false;
					break;
				} else if (b_user >= Beneficiary.users.size()) {
					System.err.println("Invalid input: " + b_user);
					break;
				}
				Beneficiary beneficiary = Beneficiary.users.get(b_user);
				System.out.printf("Logged in as %s.\n", beneficiary.getUserName());
				doBeneficiaryMenu(beneficiary); // Goes to the beneficiary menu
				break;
			case 2: // Volunteer
				System.out.println("Volunteer chosen.");
				System.out.println(" Please select a volunteer to log in as.");
				// Displays all the volunteers and their type for this user
				for (int i = 0; i < Volunteer.users.size(); i++)
					System.out.println(" " + i + ": " + Volunteer.users.get(i).getUserName() + " ("
							+ Volunteer.users.get(i).getClass().getSimpleName() + ")");
				int v_user = getUserInput();
				if (v_user == -1) {
					running = false;
					break;
				} else if (v_user >= Volunteer.users.size()) {
					System.err.println("Invalid input: " + v_user);
					break;
				}
				Volunteer volunteer = Volunteer.users.get(v_user);
				System.out.printf("Logged in as %s.\n", volunteer.getUserName());
				doVolunteerMenu(volunteer); // Goes to the volunteer menu for
											// this user
				break;
			case 3: // Wealth giver
				System.out.println("Wealth giver chosen.");
				System.out.println(" Please select a wealth giver to log in as.");
				// Displays all the wealth givers and their type
				for (int i = 0; i < Wealthgiver.users.size(); i++)
					System.out.println(" " + i + ": " + Wealthgiver.users.get(i).getUserName() + " ("
							+ Wealthgiver.users.get(i).getClass().getSimpleName() + ")");
				int w_user = getUserInput();
				if (w_user == -1) {
					running = false;
					break;
				} else if (w_user >= Wealthgiver.users.size()) {
					System.err.println("Invalid input: " + w_user);
					break;
				}
				Wealthgiver wealthgiver = Wealthgiver.users.get(w_user);
				System.out.printf("Logged in as %s.\n", wealthgiver.getUserName());
				doWealthgiverMenu(wealthgiver); // Goes to the wealth giver menu
												// for this user
				break;
			case -1:
				running = false;
				break;
			default:
				System.err.println("Invalid input: " + choice);
			}

			if (!running) {
				System.out.println("Now exiting iVolunteer.");
			}
		}
	}

	private static void doBeneficiaryMenu(Beneficiary curUser) {
		boolean menuRunning = true;
		System.out.printf("Welcome, %s.\n", curUser.getUserName());

		while (menuRunning) {
			// Menu options
			System.out.println("\nBeneficiary menu (Enter 0 to return to main menu):");
			System.out.println(" 1: Request food delivery\n" + " 2: Request medicine delivery\n"
					+ " 3: Request clothing delivery\n" + " 4: Request shelter\n" + " 5: Request transportation\n"
					+ " 6: Confirm item delivery");
			// Shows request notifications, if needed
			if (curUser.isWaiting())
				System.out.println("(You have requested an item.\n Please confirm if you have received your item.)");
			else if (!curUser.isWaiting() && curUser.lastRequestRejected())
				System.err.println("(Your previous request was rejected.\n Your previous request has been erased.)");

			int choice = getUserInput();
			switch (choice) {
			case 1: // Request food delivery
				if (FoodDelivery.checkItemStocks() == 0) {
					System.out.println("There is currently no food avaiable. Please try again later.");
					break;
				}
				if (curUser.isWaiting()) {
					System.err
							.println("You already have a pending request.\nPlease confirm that item's delivery first.");
					break;
				}
				System.out.println("List of currently available food:");
				FoodDelivery.displayAvailableItems();
				System.out.println("Please enter the corresponding number of the item you would like,"
						+ "\n or enter \"0\" to cancel.");
				int food;
				while ((food = getUserInput()) != 0) {
					food--; // Readjust index to align with zero-based index
					if (FoodDelivery.isValidIndex(food)) {
						curUser.setRequest(Delivery.type_Food, food);
						break;
					} else if (food == -1) { // "0" was entered
						System.out.println("Returning to main menu.");
						break;
					} else if (food == -2) { // "-1" was entered
						menuRunning = false;
						running = false;
						break;
					} else
						System.err.println("Invalid choice: " + (food + 1));
				}
				break;
			case 2: // Request medicine delivery
				if (MedicalDelivery.checkItemStocks() == 0) {
					System.out.println("There are currently no medical items avaiable. Please try again later.");
					break;
				}
				if (curUser.isWaiting()) {
					System.err
							.println("You already have a pending request.\nPlease confirm that item's delivery first.");
					break;
				}
				System.out.println("List of currently available medical items:");
				MedicalDelivery.displayAvailableItems();
				System.out.println("Please enter the corresponding number of the item you would like,"
						+ "\n or enter \"0\" to cancel.");
				int medic;
				while ((medic = getUserInput()) != 0) {
					medic--; // Readjust index to align with zero-based index
					if (MedicalDelivery.isValidIndex(medic)) {
						curUser.setRequest(Delivery.type_Medical, medic);
						break;
					} else if (medic == -1) { // "0" was entered
						System.out.println("Returning to main menu.");
						break;
					} else if (medic == -2) { // "-1" was entered
						menuRunning = false;
						running = false;
						break;
					} else
						System.err.println("Invalid choice: " + (medic + 1));
				}
				break;
			case 3: // Request clothing delivery
				if (ClothingDelivery.checkItemStocks() == 0) {
					System.out.println("There is currently no clothing avaiable. Please try again later.");
					break;
				}
				if (curUser.isWaiting()) {
					System.err
							.println("You already have a pending request.\nPlease confirm that item's delivery first.");
					break;
				}
				System.out.println("List of currently available clothing:");
				ClothingDelivery.displayAvailableItems();
				System.out.println("Please enter the corresponding number of the item you would like,"
						+ "\n or enter \"0\" to cancel.");
				int clothing;
				while ((clothing = getUserInput()) != 0) {
					clothing--; // Readjust index to align with zero-based index
					if (ClothingDelivery.isValidIndex(clothing)) {
						curUser.setRequest(Delivery.type_Clothing, clothing);
						break;
					} else if (clothing == -1) { // "0" was entered
						System.out.println("Returning to main menu.");
						break;
					} else if (clothing == -2) { // "-1" was entered
						menuRunning = false;
						running = false;
						break;
					} else
						System.err.println("Invalid choice: " + (clothing + 1));
				}
				break;
			case 4: // Request shelter
				if (ProvideShelter.numAvailableAddresses() == 0) {
					System.out.println("There are no shelters available at the moment. Please try again later.");
					break;
				}
				if (curUser.isWaiting()) {
					System.err
							.println("You already have a pending request.\nPlease confirm that item's delivery first.");
					break;
				}
				System.out.println("Addresses currently available:");
				ProvideShelter.displayAllAvailbleAddresses();
				System.out.println("Please enter the corresponding number address you would like to stay at,\n"
						+ " or enter \"0\" to cancel.");
				int addr;
				while ((addr = getUserInput()) != 0) {
					addr--; // Readjust index to align with zero-based index
					if (ProvideShelter.isValidIndex(addr)) {
						System.out.println("A request for shelter will be sent.");
						curUser.setShelterRequest(ProvideShelter.getAddress(addr));
						break;
					} else if (addr == -1) { // "0" was entered
						System.out.println("Returning to main menu.");
						break;
					} else if (addr == -2) { // "-1" was entered
						menuRunning = false;
						running = false;
						break;
					} else
						System.err.println("Invalid choice: " + (addr + 1));
				}
				break;
			case 5: // Request transportation
				if (ProvideTransportation.numAvailableVolunteers() == 0) {
					System.out.println(
							"There are no volunteers available for transportation at the moment. Please try again later.");
					break;
				}
				if (curUser.isWaiting()) {
					System.err
							.println("You already have a pending request.\nPlease confirm that item's delivery first.");
					break;
				}
				System.out.println("Volunteers currently available:");
				ProvideTransportation.displayAllAvailbleVolunteers();
				System.out.println(
						"Please enter the corresponding numbered volunteer that you would like to transport you,\n"
								+ " or enter \"0\" to cancel.");
				int vol;
				while ((vol = getUserInput()) != 0) {
					vol--; // Readjust index to align with zero-based index
					if (ProvideTransportation.isValidIndex(vol)) {
						System.out.println("A request for transportation will be sent. Please wait for approval.");
						curUser.setTransportRequest(ProvideTransportation.getVolunteer(vol));
						break;
					} else if (vol == -1) { // "0" was entered
						System.out.println("Returning to main menu.");
						break;
					} else if (vol == -2) { // "-1" was entered
						menuRunning = false;
						running = false;
						break;
					} else
						System.err.println("Invalid choice: " + (vol + 1));
				}
				break;
			case 6: // Confirm item delivery
				switch (curUser.getRequestType()) {
				case Delivery.type_Food:
				case Delivery.type_Medical:
				case Delivery.type_Clothing:
					System.out.println("Your delivery has been confirmed.");
					curUser.confirmation();
					break;
				default:
					if (curUser.isWaiting())
						System.err.println("Your current request must be confirmed by a volunteer.");
					else
						System.err.println("You have no current request to confirm.");
				}
				break;
			case 0:
				menuRunning = false;
				break;
			case -1:
				running = false;
				menuRunning = false;
				break;
			default:
				System.err.println("Invalid input: " + choice);
			}

			if (!menuRunning) {
				System.out.println("Logging out.");
			}
		}
	}

	private static void doVolunteerMenu(Volunteer curUser) {
		boolean menuRunning = true;
		System.out.printf("Welcome, %s.\n", curUser.getUserName());

		while (menuRunning) {
			// Menu options
			System.out.println("\nVolunteer menu (Enter 0 to return to main menu):");
			System.out.println(" 1: Check notifications\n" + " 2: Accept or decline requests");
			if (curUser instanceof ExtraSpaceVolunteer) {
				if (ProvideShelter.containsAddress(curUser.getUserAddress()))
					System.out.println(" 3: Remove your address from shelter list");
				else
					System.out.println(" 3: Add your address to shelter list");
			} else if (curUser instanceof DrivingVolunteer) {
				if (ProvideTransportation.containsUser(curUser))
					System.out.println(" 3: Remove yourself from the avaiable drivers list");
				else
					System.out.println(" 3: Add yourself to the available drivers list");
			}

			// Show if this volunteer has a notification
			if (curUser.currentDeliveryJobType != Delivery.type_Null)
				System.out.println(" You have a notification.\n Check it on the first option of the menu.");

			int choice = getUserInput();
			switch (choice) {
			case 1: // Display notifications
				if (curUser.currentDeliveryJobType != Delivery.type_Null) {
					System.out.println("\nYou have a new notification.");
					System.out.print("Request type: ");
					switch (curUser.currentDeliveryJobType) {
					case Delivery.type_Food:
						System.out.println("Food delivery.");
						break;
					case Delivery.type_Medical:
						System.out.println("Medicine delivery.");
						break;
					case Delivery.type_Clothing:
						System.out.println("Clothing delivery.");
						break;
					case Assistance.type_Shelter:
						System.out.println("Request for shelter.");
						break;
					case Assistance.type_Transportation:
						System.out.println("Request for transportation.");
						break;
					}
					System.out.println(curUser.notificationMessage);
					System.out.println();
				} else
					System.err.println("\nThere are no notifications for you at the moment. Check back later.");
				break;
			case 2: // Accept or decline requests
				if (curUser.currentDeliveryJobType != Assistance.type_Null) {
					switch (curUser.currentDeliveryJobType) {
					case Assistance.type_Shelter: {
						if (((ExtraSpaceVolunteer) curUser).waitingBeneficiary != null) {
							System.out.println(
									"\nA beneficiary is requesting to stay in the address you listed in the available shelter list:");
							System.out.println(" " + curUser.getUserAddress());
							// Get the requesting beneficiary's information
							Beneficiary b = ((ExtraSpaceVolunteer) curUser).waitingBeneficiary;
							boolean failedIn = false;
							int choice_in = 3;
							do {
								if (failedIn)
									System.err.println("Invalid choice: " + choice_in);
								System.out.println("Will you accept this beneficiary to your shelter address?");
								System.out.println(" 0: No\n 1: Yes");
								choice_in = getUserInput();
								if (choice_in == 1) {
									b.confirmation();
									curUser.clearNotification();
									System.out.println(
											"Beneficiary " + b.getUserName() + "'s request has been accepted.");
									break;
								} else if (choice_in == 0) {
									b.rejected();
									curUser.clearNotification();
									System.out.println(
											"Beneficiary " + b.getUserName() + "'s request has been rejected.");
									break;
								}
								failedIn = true;
							} while (true);
						} else
							System.err.println("An internal error occurred.\n User's waiting beneficiary is null.");
						break;
					}
					case Assistance.type_Transportation:
						if (((DrivingVolunteer) curUser).waitingBeneficiary != null) {
							// Get the requesting beneficiary's information
							Beneficiary b = ((DrivingVolunteer) curUser).waitingBeneficiary;
							System.out.println("\nA beneficiary waiting at " + b.getUserAddress()
									+ " is requesting transportation.");
							boolean failedIn = false;
							int choice_in = 3;
							do {
								if (failedIn)
									System.err.println("Invalid choice: " + choice_in);
								System.out.println("Will you transport this beneficiary?");
								System.out.println(" 0: No\n 1: Yes");
								choice_in = getUserInput();
								if (choice_in == 1) {
									b.confirmation();
									curUser.clearNotification();
									System.out.println(
											"Beneficiary " + b.getUserName() + "'s request has been accepted.");
									break;
								} else if (choice_in == 0) {
									b.rejected();
									curUser.clearNotification();
									System.out.println(
											"Beneficiary " + b.getUserName() + "'s request has been rejected.");
									break;
								}
								failedIn = true;
							} while (true);
						} else
							System.err.println("An internal error occurred.\n User's waiting beneficiary is null.");
						break;
					default:
						System.err.println("\nYour current request cannot be accepted or declined.\n"
								+ "A beneficiary must confirm if you fulfilled the request or not.");
						break;
					}
				} else
					System.err.println("\nYou do not have any requests at the moment. Check back later.");
				break;
			case 3: // Add volunteer/volunteer's address to the driving/shelter
					// list
				if (curUser instanceof ExtraSpaceVolunteer) {
					if (ProvideShelter.containsAddress(curUser.getUserAddress())) {
						ProvideShelter.removeLocation(curUser.getUserAddress());
						System.out.printf("Your address \"%s\" was removed from the available shelter list.\n",
								curUser.getUserAddress());
					} else {
						ProvideShelter.addLocation(curUser.getUserAddress());
						System.out.printf("Your address \"%s\" was added to the available shelter list.\n",
								curUser.getUserAddress());
					}
				} else if (curUser instanceof DrivingVolunteer) {
					if (ProvideTransportation.containsUser(curUser)) {
						ProvideTransportation.removeUser(curUser);
						System.out.println("You were removed from the available drivers list.");
					} else {
						ProvideTransportation.addUser(curUser);
						System.out.println("You were added to the available drivers list.");
					}
				} else
					System.err.println("Action not available for this user.");
				break;
			case 0:
				menuRunning = false;
				break;
			case -1:
				running = false;
				menuRunning = false;
				break;
			default:
				System.err.println("Invalid input: " + choice);
			}

			if (!menuRunning) {
				System.out.println("Logging out.");
			}
		}
	}

	private static void doWealthgiverMenu(Wealthgiver curUser) {
		boolean menuRunning = true;
		System.out.printf("Welcome, %s.\n", curUser.getUserName());

		while (menuRunning) {
			// Menu options
			System.out.println("\nWealthgiver menu (Enter 0 to return to main menu):");
			// Display appropriate options for wealth giver's type
			if (curUser instanceof Supermarket) {
				System.out.println(" 1: Donate food");
			} else if (curUser instanceof Pharmacy) {
				System.out.println(" 1: Donate medicine");
			} else if (curUser instanceof ClothingShop) {
				System.out.println(" 1: Donate clothes");
			} else if (curUser instanceof VehicleDonor) {
				System.out.println(" 1: Donate a vehicle");
			} else if (curUser instanceof PropertyDonor) {
				if (!ProvideShelter.containsAddress(curUser.getUserAddress()))
					System.out.println(" 1: Add your address to the shelter list");
				else
					System.out.println(" 1: Remove your address to the shelter list");
			} else {
				// A generic wealth giver can donate whatever they want
				System.out.println(" 1: Donate medicine\n" + " 2: Donate medicine\n" + " 3: Donate clothes\n"
						+ " 4: Donate a vehicle");
				if (!ProvideShelter.containsAddress(curUser.getUserAddress()))
					System.out.println(" 5: Add your address to the shelter list");
				else
					System.out.println(" 5: Remove your address to the shelter list");
			}

			// Adjust the choices, so as not to have duplicate entries in the
			// next switch.
			int choice = getUserInput();
			if (choice == 1) {
				if (!curUser.getClass().getSimpleName()
						.equals(new Wealthgiver("", "", "").getClass().getSimpleName())) {
					if (curUser instanceof Supermarket) {
						choice = 1;
					} else if (curUser instanceof Pharmacy) {
						choice = 2;
					} else if (curUser instanceof ClothingShop) {
						choice = 3;
					} else if (curUser instanceof VehicleDonor) {
						choice = 4;
					} else if (curUser instanceof PropertyDonor) {
						choice = 5;
					}
				}
			}

			switch (choice) {
			case 1: // Donate food
				System.out.println("Please enter the name of the food you would like to donate:");
				System.out.print("> ");
				try {
					FoodDelivery.addItem(input.readLine());
					System.out
							.println("Your item has been added to our list successfully. Thank you for your donation.");
				} catch (IOException e) {
					System.err.println("Inserting item failed. Please try again.");
				}
				break;
			case 2: // Donate medicine
				System.out.println("Please enter the name of the medicine you would like to donate:");
				System.out.print("> ");
				try {
					MedicalDelivery.addItem(input.readLine());
					System.out
							.println("Your item has been added to our list successfully. Thank you for your donation.");
				} catch (IOException e) {
					System.err.println("Inserting item failed. Please try again.");
				}
				break;
			case 3: // Donate clothes
				System.out.println("Please enter the name of the clothing you would like to donate:");
				System.out.print("> ");
				try {
					ClothingDelivery.addItem(input.readLine());
					System.out
							.println("Your item has been added to our list successfully. Thank you for your donation.");
				} catch (IOException e) {
					System.err.println("Inserting item failed. Please try again.");
				}
				break;
			case 4: // Donate vehicle
				boolean failedIn = false;
				int choice_in = 3;
				do {
					if (failedIn)
						System.err.println("Invalid choice: " + choice_in);
					System.out.println(
							"A volunteer will be created under your credentials to allow your vehicle to be used for our transporation services.");
					System.out.println(" 0: No\n 1: Yes");
					choice_in = getUserInput();
					if (choice_in == 1) {
						DrivingVolunteer dv = new DrivingVolunteer(curUser.getUserName(), curUser.getUserAddress(),
								curUser.getUserID());
						Volunteer.users.add(dv);
						ProvideTransportation.addUser(dv);
						System.out.println("A driving volunteer has been created using your credentials.");
						break;
					} else if (choice_in == 0) {
						System.out.println("A driving volunteer will not be created.");
						break;
					}
					failedIn = true;
				} while (true);
				break;
			case 5: // Provide shelter
				if (!ProvideShelter.containsAddress(curUser.getUserAddress())) {
					ProvideShelter.addLocation(curUser.getUserAddress());
					System.out.printf("Your address \"%s\" was added to the available shelter list.\n",
							curUser.getUserAddress());
				} else {
					ProvideShelter.removeLocation(curUser.getUserAddress());
					System.out.printf("Your address \"%s\" was removed from the available shelter list.\n",
							curUser.getUserAddress());
				}
				break;
			case 0:
				menuRunning = false;
				break;
			case -1:
				running = false;
				menuRunning = false;
				break;
			default:
				System.err.println("Invalid input: " + choice);
			}

			if (!menuRunning) {
				System.out.println("Logging out.");
			}
		}
	}

	private static int getUserInput() {
		System.out.print("> ");
		try {
			return Integer.parseInt(input.readLine());
		} catch (NumberFormatException | IOException e) {
			;
		}
		return 0;
	}

	private static void populateDonationLists() {
		ClothingDelivery.addItem("Sweater, Small, Black");
		ClothingDelivery.addItem("Sweater, Medium, Grey");
		ClothingDelivery.addItem("Sweater, Large, Green");
		ClothingDelivery.addItem("Jeans, Small, Dark Blue");
		ClothingDelivery.addItem("Jeans, Medium, Blue");
		ClothingDelivery.addItem("Jeans, Large, Light Blue");
		ClothingDelivery.addItem("Shirt, Small, Orange");
		ClothingDelivery.addItem("Shirt, Medium, White");
		ClothingDelivery.addItem("Shirt, Small, Red");

		FoodDelivery.addItem("Canned vegetables");
		FoodDelivery.addItem("Canned soup");
		FoodDelivery.addItem("Meat and poultry");
		FoodDelivery.addItem("Tuna and seafood");
		FoodDelivery.addItem("Beans");

		MedicalDelivery.addItem("Aspirin");
		MedicalDelivery.addItem("Cough medicine");
		MedicalDelivery.addItem("Asthma inhaler");
		MedicalDelivery.addItem("Stomach medicine");
	}

	private static void populateUsers() {
		// Names courtesy of http://www.behindthename.com/random/
		// Addresses courtesy of https://www.randomlists.com/random-addresses
		Beneficiary.users.add(new Beneficiary("Hammond Smith", "9974 School Court", "1"));
		Beneficiary.users.add(new Beneficiary("Lucretius Archer", "9365 Cactus Drive", "2"));
		Beneficiary.users.add(new Beneficiary("Jasper Wolfe", "9581 Pheasant Street ", "3"));
		Beneficiary.users.add(new Beneficiary("Melisizwe Ahmad", "6 Iroquois Ave.", "4"));
		Beneficiary.users.add(new Beneficiary("Adalberht Cernik", "98 Market Road", "5"));

		Volunteer.users.add(new DrivingVolunteer("Syuzanna Waldvogel", "9667 Buckingham Dr.", "6"));
		Volunteer.users.add(new ExtraSpaceVolunteer("Klytië Alves", "31 South St.", "7"));
		Volunteer.users.add(new MedicalVolunteer("Corina Jonsson", "90 Heather Avenue", "8"));
		Volunteer.users.add(new Volunteer("Valeria Coste", "116 Wild Rose Street", "9"));

		Wealthgiver.users.add(new Supermarket("Fortinos", "8268 Carson Ave.", "10"));
		Wealthgiver.users.add(new Pharmacy("Shoppers Drug Mart", "8714 Carpenter Drive", "11"));
		Wealthgiver.users.add(new ClothingShop("Salvation Army", "804 Cambridge Street", "12"));
		Wealthgiver.users.add(new VehicleDonor("Donate a Car Canada", "771 Water Court", "13"));
		Wealthgiver.users
				.add(new PropertyDonor("Carrington House Property Donation Co. LLC", "34 Lawrence Lane ", "14"));
		Wealthgiver.users.add(new Wealthgiver("Good Shepherd", "1 West Wagon Dr.", "15"));
	}
}
