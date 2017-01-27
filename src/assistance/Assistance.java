package assistance;

import deliveries.Delivery;

public abstract class Assistance {
	// Using 4 and 5 to prevent any potential conflict with Delivery types
	public static final int type_Transportation = 4;
	public static final int type_Shelter = 5;
	public static final int type_Null = Delivery.type_Null;
}
