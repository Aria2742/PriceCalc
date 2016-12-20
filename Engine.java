import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Engine
{
	private static final String fileName = "ItemList.txt";
	
	public static void main(String[] args)
	{
		boolean exit = false;
		Scanner input = new Scanner(System.in);

		recipes();

		do{
			System.out.println("Enter \"exit\" to quit, \"price <name>\" for an item price, \"add\" to add an item, \"change\" to edit base material prices, or \"list\" to list the current items.");
			String in = input.nextLine();
			if(in.equalsIgnoreCase("exit")) {
				exit = true;
			} else if(in.substring(0,3).equalsIgnoreCase("add")) {
				System.out.print("What is the item name? ");
				String n = input.nextLine();
				System.out.print("Is the item a raw material? (Y or N) ");
				boolean r = input.nextLine().toUpperCase().charAt(0) == 'Y';
				Item newItem = new Item(n,r);
			} else if(in.substring(0,4).equalsIgnoreCase("list")) {
				Item.printNames(fileName);
			} else if(in.substring(0,5).equalsIgnoreCase("price")) {
				Item.printPrice(in.substring(6));
			} else if(in.substring(0,6).equalsIgnoreCase("change")) {
				Item.changeRawPrices();
			}
		}while(exit == false);
		try {
			Item.saveItems(fileName);
			System.out.println("Items saved!");
		} catch (Exception err) {
			System.out.println("Problem saving! ERROR!");
			err.printStackTrace();
		}
	}
	
	public static void recipes()
	{
		try {
			Item.loadItems(fileName);
		} catch (FileNotFoundException err) {
			File f = new File(fileName);
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println("Error creating config file!");
				e.printStackTrace();
			}
			System.out.println("Config file created. Please restart the program.");
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
}
