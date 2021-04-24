import java.util.Scanner;

public class UserInterface 
{
	private BusGraph graph;
	private Scanner input;

	UserInterface(String path) 
	{
		graph = new BusGraph(path + "stops.txt", path + "stop_times.txt", path + "transfers.txt");
		input = new Scanner(System.in);
		runInterface();
	}

	/**
	 * Run the program
	 */
	private void runInterface() 
	{
		String inputLine = "";
		BusStop[] stops = null;
		
		//main interface loop, runs until user exits
		while (!inputLine.equalsIgnoreCase("exit")) 
		{
			System.out.println("\nType 1 to search for the shortest route between 2 stops.");
			System.out.println("Type 2 to search for bus stops by name.");
			System.out.println("Type 3 to search for all trips given an arrival time.");
			System.out.println("Type 'exit' at any time to close the program.");
			System.out.print("> ");

			inputLine = input.next();
			if (isExit(inputLine))
				System.exit(0);

			while (inputLine.length() != 1)
			{
				System.out.print("Incorrect input, try again: ");
				inputLine = input.next();
				if (isExit(inputLine))
					System.exit(0);
			}

			switch (inputLine.toLowerCase()) 
			{
			case "1":
				BusStop start = getStopFromUser("Please choose your source bus stop: ", stops, inputLine);
				BusStop destination = getStopFromUser("Please choose your destination bus stop: ", stops, inputLine);
				graph.dijkstra(start, destination);
				break;
			case "2":
				stops = searchStopsByName("Search for bus stop by name: ", stops, inputLine);
				if (stops.length == 0) 
				{
					System.out.println("No results found.");
				} 
				else 
				{
					for (BusStop stop : stops) 
					{
						System.out.println(stop);
					}
				}
				break;
			case "3":
				int hours = readIntFromUser("Type in the arrival time hours: ");
				while (hours > 23 || hours < 0) 
				{
					hours = readIntFromUser("Hours must be 0-23: ");
				}
				int minutes = readIntFromUser("Type in the arrival time minutes: ");
				while (minutes > 59 || minutes < 0) 
				{
					minutes = readIntFromUser("Minutes must be 0-59: ");
				}
				int seconds = readIntFromUser("Type in the arrival time seconds: ");
				while (seconds > 59 || seconds < 0)
				{
					seconds = readIntFromUser("Seconds must be 0-59: ");
				}
				int time = (hours * 60 * 60) + (minutes * 60) + seconds;
				graph.findTripByTime(time);
				break;
			default:
				break;

			}
		}
	}

	/**
	 * checks if the subarray of busstops has an index in it
	 * @param index : index to search for
	 * @param stops :subarray of busstops
	 * @return true if yes, false otherwise
	 */
	private boolean hasIndex(int index, BusStop[] stops) 
	{
		for (BusStop stop : stops) 
		{
			if (index == stop.getIndex()) 
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Calls the search method if input isnt exit
	 * @param str : prompt for the user
	 * @param stops : array of stops
	 * @param inputLine : user will write to, search by this
	 * @return array of bus stops which contain that prompt
	 */
	private BusStop[] searchStopsByName(String str, BusStop[] stops, String inputLine) 
	{
		inputLine = readStringFromUser(str);
		if (isExit(inputLine))
			System.exit(0);
		return graph.searchBusStop(inputLine);
	}

	/**
	 * Prompts user to enter a bus stop name, then gets the user to pick a bus stop 
	 * @param str : prompt for the user
	 * @param stops : array of bus stops
	 * @param inputLine : user will write to, search by this
	 * @return bus stop the user wanted
	 */
	public BusStop getStopFromUser(String str, BusStop[] stops, String inputLine) 
	{
		while(true)
		{
			//uses tst to see which stops contain the string
			stops = searchStopsByName(str, stops, inputLine);
			for (BusStop stop : stops)
			{
				System.out.println(stop);
			}
			if (stops.length == 0) 
			{
				System.out.println("No results were found for your input, please try again.");
			}
			else
				break;
		}

		//prompts the user to pick an option from the array of viable bus stops
		int index = readIntFromUser("Please choose a bus stop by typing in the number X from [X]: ");
		while (!hasIndex(index, stops)) 
		{
			index = readIntFromUser("Please choose a valid number: ");
		}
		return graph.stops[index];
	}

	/**
	 * Checks if input is exit
	 * @param str ; input to check
	 * @return true if yes, false otherwise
	 */
	public boolean isExit(String str) 
	{
		return str.equalsIgnoreCase("exit");
	}

	/**
	 * Prompts the user to enter an int, checks if input is an integer
	 * @param question : prompt for the user
	 * @return correctly entered int
	 */
	public int readIntFromUser(String question) 
	{
		System.out.print(question);
		
		String inputStr = input.next();
		if (isExit(inputStr))
			System.exit(0);
		while (inputStr.replaceAll("[0-9]", "").length() != 0) 
		{
			System.out.print("Input must be a positive integer: ");
			inputStr = input.next();
			if (isExit(inputStr))
				System.exit(0);
		}

		return Integer.parseInt(inputStr);
	}

	/**
	 * prompts the user for a string, checks if input is non-empty and returns
	 * @param question :  prompt for the user
	 * @return entered string
	 */
	public String readStringFromUser(String question)
	{
		System.out.print(question);
		String userInput = "";
		while (userInput.length() == 0) 
		{
			userInput = input.nextLine();
		}
		return userInput;
	}
}
