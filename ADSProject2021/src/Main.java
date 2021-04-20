
public class Main {
	// Paths for VSCode only
	// /* Dominik */public static String path = "D:\\Dominik\\Documents\\College Material\\Year 2\\Year 2 - Algorithms and Data Structures (CSU22011 & CSU22012)\\ADS Project\\ADS-Project-2021\\ADSProject2021\\res\\";
	/*Daithi*/ public static String path =
	"C:\\Users\\daith\\OneDrive\\Documents\\Algorithms\\Group project\\ADS-Project-2021\\ADSProject2021\\res\\";
	/// *Adri*/ public static String path =
	// "/Users/adriana/Desktop/ADS/ADS-Project-2021/ADSProject2021/res/";
	// *Nick*
	// public static String path =
	// "D:\\dev\\College\\AlgosProj\\ADSProject2021\\res\\";

	// Relative path for Eclipse
	// /* Eclipse */public static String path = "./res/";

	public static void main(String[] args) {
		BusGraph graph = new BusGraph(path+"stops.txt", path+"stop_times.txt", path+"transfers.txt");
		graph.searchBusStop("water");
	}


	
	
}
