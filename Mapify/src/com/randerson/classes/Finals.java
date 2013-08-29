package com.randerson.classes;

public class Finals {

	// application constants
	public static final int NULL = 0, 
					 AUTOS = 1, 
					 BOATS = 2, 
					 CLOTHES = 3, 
					 APPLIANCES = 4,
					 ELECTRONICS = 5, 
					 FURNITURE = 6, 
					 HOMES = 7, 
					 JEWELRY = 8,
					 SERVICES = 9;
	
	public static String returnCategory(int catValue)
	{
		String category = "";
		
		switch(catValue)
		{
		case 1:
			category = "Autos";
			break;
			
		case 2:
			category = "Boats";
			break;
			
		case 3:
			category = "Clothes";
			break;
			
		case 4:
			category = "Appliances";
			break;
			
		case 5:
			category = "Electronics";
			break;
			
		case 6:
			category = "Furniture";
			break;
			
		case 7:
			category = "Homes";
			break;
			
		case 8:
			category = "Jewelry";
			break;
			
		case 9:
			category = "Services";
			break;
			
			default:
				break;
		
		}
		
		return category;
	}
	
}
