import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	// Paths for VSCode only
	/* Dominik */public static String path = "D:\\Dominik\\Documents\\College Material\\Year 2\\Year 2 - Algorithms and Data Structures (CSU22011 & CSU22012)\\ADS Project\\ADS-Project-2021\\ADSProject2021\\res\\";
	/// *Daithi*/ public static String path =
	// "C:\\Users\\daith\\OneDrive\\Documents\\Algorithms\\Group
	// project\\ADS-Project-2021\\ADSProject2021\\res\\";
	/// *Adri*/ public static String path =
	// "/Users/adriana/Desktop/ADS/ADS-Project-2021/ADSProject2021/res/";
	// *Nick*
	// public static String path =
	// "D:\\dev\\College\\AlgosProj\\ADSProject2021\\res\\";

	// Relative path for Eclipse
	// /* Eclipse */public static String path = "./res/";

	public static void main(String[] args) {
		TST<BusStop> tst = new TST<BusStop>();
		BusStop[] stops = readInStopData();
		for (int i = 0; i < stops.length; i++)
			tst.put(stops[i].getName(), stops[i]);

		String matchString = "HAS";
		String[] queue = tst.keysWithPrefix(matchString);
		for (int i = 0; i < queue.length; i++) {
			BusStop temp = tst.get(queue[i]);
			System.out.println(temp);
		}

		// System.out.println(stops.length);
		// readInTrips(stops);
		// System.out.println(stops[10].getEdges().size());
		// readInTransfers(stops);
		// System.out.println(stops[10].getEdges().size());
		// long time = System.nanoTime();
		// CSVLoader c = new CSVLoader(path + "stop_times.txt");
		// time = System.nanoTime() - time;
		// System.out.println(time);
		// ArrayList<String> list = c.readFile();
		// for (String s : list) {
		// String[] arr = s.split(",");
		// if (!arr[1].equals(arr[2])) {
		// System.out.println(s);
		// }
		// }
		// System.out.println(stops.length);
		// for (int i = 0; i < stops.length; i++) {
		// System.out.println(stops[i]);
		// }
	}

	/**
	 * Reads in stop_times file and creates edges from data, adding them to the
	 * approppriate bus stop
	 * 
	 * @param stops: Array of all bus stops
	 **/
	public static void readInTrips(BusStop[] stops) {
		CSVLoader c = new CSVLoader(path + "stop_times.txt");
		ArrayList<String> list = c.readFile();
		for (int i = 2; i < list.size(); i++) {
			String[] from = list.get(i - 1).split(",");
			String[] to = list.get(i).split(",");
			if (from[0].equals(to[0])) {
				int fromIndex = findBusStop(Integer.parseInt(from[3]), stops);
				int toIndex = findBusStop(Integer.parseInt(to[3]), stops);
				int tripID = Integer.parseInt(from[0]);
				String departureTime = from[2];
				String arrivalTime = to[1];
				int seq = Integer.parseInt(from[4]);
				double fromDist = 0;
				double toDist = Double.parseDouble(to[to.length - 1]);
				if (from.length == 8) {
					fromDist = Double.parseDouble(from[from.length - 1]);
				}
				stops[fromIndex].addEdge(new BusEdge(tripID, stops[fromIndex], stops[toIndex], departureTime,
						arrivalTime, seq, toDist - fromDist));
			}
		}
	}

	/**
	 * Reads in transfers file and creates edges from data and adds it to
	 * appropriate bus stop
	 * 
	 * @param stops: array of all bus stops
	 */
	public static void readInTransfers(BusStop[] stops) {
		CSVLoader c = new CSVLoader(path + "transfers.txt");
		ArrayList<String> list = c.readFile();

		for (int i = 1; i < list.size(); i++) {
			String[] transfer = list.get(i).split(",");
			int fromIndex = findBusStop(Integer.parseInt(transfer[0]), stops);
			int toIndex = findBusStop(Integer.parseInt(transfer[1]), stops);
			int type = Integer.parseInt(transfer[2]);
			int cost = 0;
			if (type != 0) {
				cost = Integer.parseInt(transfer[3]);
			}
			stops[fromIndex].addEdge(new BusEdge(stops[fromIndex], stops[toIndex], cost));
		}
	}

	/**
	 * Takes in id of bus stop and returns the index in the array of that stop
	 * 
	 * @param id:    id of bus stop we want to find
	 * @param stops: array of all bus stops
	 * @return index of bus stop with the given array
	 **/
	public static int findBusStop(int id, BusStop[] stops) {
		int low = 0;
		int high = stops.length - 1;

		while (low <= high) {
			int middle = low + ((high - low) / 2);
			int midID = stops[middle].getID();

			if (midID == id)
				return middle;
			else if (midID > id)
				high = middle - 1;
			else
				low = middle + 1;
		}
		return -1;
	}

	/**
	 * Reads in stops file and creates an array of bus stop classes from the data
	 * 
	 * @return array of all bus stops
	 */
	public static BusStop[] readInStopData() {
		CSVLoader c = new CSVLoader(path + "stops.txt");
		ArrayList<String> list = c.readFile();
		BusStop[] stops = new BusStop[list.size() - 1];

		for (int i = 0; i < stops.length; i++) {
			String[] data = list.get(i + 1).split(",");
			int parent = -1;
			if (data.length == 10) {
				parent = Integer.parseInt(data[9]);
			}
			int code = -1;
			if (!data[1].equals(" ")) {
				code = Integer.parseInt(data[1]);
			}
			String token = data[2].split(" ")[0];
			String busStopName = data[2];
			if (token.length() == 2 && token.charAt(1) == 'B') {
				busStopName = data[2].substring(3) + " " + token;
			}

			stops[i] = new BusStop(Integer.parseInt(data[0]), code, busStopName, data[3], Double.parseDouble(data[4]),
					Double.parseDouble(data[5]), data[6], Integer.parseInt(data[8]), parent);
		}
		Arrays.sort(stops);
		return stops;
	}

}
