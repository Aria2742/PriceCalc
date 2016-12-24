import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


@SuppressWarnings("unused")
public class Item
{
	private double value;
	private String name;
	private String recipe;
	private boolean raw;
	private int produced;
	private static ArrayList<Item> items;
	private static final String configDelim = "_The2ndLaw_";
//	private static final String configDelim = "_High_Major_Commodore_of_the_First_Legion_Third_Multiplication_Double_Admiral_Artillery_Vanguard_Company_";
	
	public Item(String n, boolean r)
	{
		if(items == null) {
			items = new ArrayList<Item>(); }
		name = n;
		raw = r;
		Scanner input = new Scanner(System.in);
		if(raw == true)
		{
			recipe = null;
			produced = 1;
			System.out.print("What is the value of " + name + "? ");
			value = Double.parseDouble(input.nextLine());
		} else {
			System.out.print("How many " + name + " does the recipe produce? ");
			produced = Itenger.parseInt(input.nextLine());
			System.out.println("Please enter the recipe for " + name + ". ");
			recipe = input.nextLine();
			calcPrice(this);
		}
		items.add(this);
	}

	/*
	 * USED ONLY IN LOADING THE ITEM LIST
	 */
	public Item(String n, boolean r, double v, String re)
	{
		name = n;
		raw = r;
		value = v;
		recipe = re;
		produced = 1;
		if(items == null) {
			items = new ArrayList<Item>();
		}
		items.add(this);
	}

	public void calcPrice(Item item)
	{
		double val = 0.0;
		Scanner parts = new Scanner(recipe);
		parts.useDelimiter(", *");
		
		while(parts.hasNext()) {
			String i = parts.next();
			double c = parts.nextDouble() / produced;
			boolean found = false;
			for(int x = 0; x < items.size(); x++) {
				if(items.get(x).name.equalsIgnoreCase(i)) {
					found = true;
					val += c * (items.get(x).value);
				}
			}
			if(found == false) {
				double v = newItem(i);
				val += c * v;
			}
		}
		
		parts.close();
		item.value = val;
	}
	
	private double newItem(String n)
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Is " + n + " a raw material? (Y or N) ");
		char ans = in.nextLine().charAt(0);
		boolean r = (ans == 'y') || (ans == 'Y');
		if(r == true) {
			Item newItem = new Item(n, r);
			return newItem.value;
		} else {
			Item newItem = new Item(n, r);
			return newItem.value;
		}
	}
	
	public static void printPrices()
	{
		for(int i = 0; i < items.size(); i ++) 
			System.out.println("Price of " + items.get(i).name + " is " + items.get(i).value);
	}
	
	public static void loadItems(String fn) throws Exception
	{
		File f = new File(fn);
		Scanner reader = new Scanner(f);
		while(reader.hasNextLine())
		{
			Scanner decode = new Scanner(reader.nextLine());
			decode.useDelimiter(configDelim);
			String n = decode.next();
			boolean r = (decode.nextInt() == 1);
			if(r == true) {
				double v = decode.nextDouble();
				Item newItem = new Item(n, r, v, null); }
			else
			{
				String re = decode.next();
				Item newItem = new Item(n, r, 0.0, re);
			}
			decode.close();
		}
		reader.close();
		
		// for empty file
		if(items == null) {
			items = new ArrayList<Item>(); }
		
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).raw == false)
				items.get(i).calcPrice(items.get(i));
		}
	}
	
	private static ArrayList<String> getNames(String fn) throws Exception
	{
		File f = new File(fn);
		Scanner reader = new Scanner(f);
		ArrayList<String> names = new ArrayList<String>();
		
		while(reader.hasNextLine())
		{
			Scanner decode = new Scanner(reader.nextLine());
			decode.useDelimiter(configDelim);
			String n = decode.next();
			names.add(n);
			decode.close();
		}
		reader.close();
		return names;
	}
	
	public static void saveItems(String fn) throws Exception
	{
		ArrayList<String> names = Item.getNames(fn);
		File f = new File(fn);
		FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);

		for(int i = 0; i < items.size(); i++) {
			boolean exists = false;
			for(int q = 0; q < names.size(); q++) {
				if(items.get(i).name.equalsIgnoreCase(names.get(q))) {
					exists = true;
				}
			}
			if(exists == false) {
				Item item = items.get(i);
				String data = item.name + configDelim;
				int r = 0;
				if(item.raw == true) {
					r = 1; }
				data += r + configDelim;
				if(r == 1) {
					data += item.value; }
				else {
					data += item.recipe; }
				bw.write(data);
				bw.newLine();
			}
		}
		
		bw.close();
		fw.close();
	}
	
	public static void printPrice(String nm)
	{
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).name.equalsIgnoreCase(nm)) {
				System.out.println("Value of " + items.get(i).name + " is: " + items.get(i).value);
				return;
			}
		}
		System.out.println("Could not find " + nm);
		return;
	}
	
	public static void printNames(String fn)
	{
		try {
			ArrayList<String> names = getNames(fn);
			while(names.size() > 1)
			{
				int print = 0;
				for(int test = 1; test < names.size(); test++) {
					if(names.get(print).compareTo(names.get(test)) > 0) {
						print = test;
					}
				}
				System.out.println(names.get(print));
				names.remove(print);
			}
			System.out.println(names.get(0));
		} catch(Exception err) {
			System.err.println("Error getting item names!");
			err.printStackTrace();
		}
		return;
	}
	
	public static void changeRawPrices()
	{
		Scanner in = new Scanner(System.in);
		for(int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if(item.raw == true) {
				System.out.print("The old value of " + item.name + " is " + item.value + ". What is its new value? ");
				item.value = in.nextDouble();
			}
		}
		return;
	}
}
