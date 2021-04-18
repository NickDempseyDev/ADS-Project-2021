import java.util.ArrayList;

public class Main {
	// Paths for VSCode only
	// / *Dominik*/public static String path = "D:\\Dominik\\Documents\\College
	// Material\\Year 2\\Year 2 - Algorithms and Data Structures (CSU22011 &
	// CSU22012)\\ADS Project\\ADS-Project-2021\\ADSProject2021\\res\\";
	/// *Daithi*/ public static String path =
	// "C:\\Users\\daith\\OneDrive\\Documents\\Algorithms\\Group
	// project\\ADS-Project-2021\\ADSProject2021\\res\\";
	/// *Adri*/ public static String path =
	// "/Users/adriana/Desktop/ADS/ADS-Project-2021/ADSProject2021/res/";
	// *Nick* 
	// public static String path = "D:\\dev\\College\\AlgosProj\\ADSProject2021\\res\\";

	// Relative path for Eclipse
	/* Eclipse */public static String path = "./res/";

	public static void main(String[] args) {
		// BusStop[] stops = readInStopData();
		// long time = System.nanoTime();
		// CSVLoader c = new CSVLoader(path + "stop_times.txt");
		// time = System.nanoTime() - time;
		// System.out.println(time);
		// ArrayList<String> list = c.readFile();
		// for (String s : list) {
		// 	String[] arr = s.split(",");
		// 	if (!arr[1].equals(arr[2])) {
		// 		System.out.println(s);
		// 	}
		// }
		// System.out.println(stops.length);
		// for (int i = 0; i < stops.length; i++) {
		// 	System.out.println(stops[i]);
		// }
	}

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
			if(token.length() == 2 && token.charAt(1) == 'B') {
				busStopName = data[2].substring(3) + " " + token;
			}

			stops[i] = new BusStop(Integer.parseInt(data[0]), code, busStopName, data[3], Double.parseDouble(data[4]),
					Double.parseDouble(data[5]), data[6], Integer.parseInt(data[8]), parent);
		}
		return stops;
	}

}
