import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.HashMap;
import java.util.Collections;

public class BusGraph {
	BusStop[] stops;
	TST<BusStop> tst;
	HashMap<Integer, Integer> tripIDs;

	public BusGraph(String stopsFile, String tripsFile, String transferFile) {
		tripIDs = new HashMap<Integer, Integer>();
		System.out.print("Reading in stops...");
		stops = readInStopData(stopsFile);
		System.out.println(" complete!");
		System.out.print("Reading in trips...");
		readInTrips(stops, tripsFile);
		System.out.println(" complete!");
		System.out.print("Reading in transfers...");
		readInTransfers(stops, transferFile);
		System.out.println(" complete!");
		System.out.print("Creating TST...");
		tst = new TST<BusStop>();
		for (int i = 0; i < stops.length; i++) {
			tst.put(stops[i].getName(), stops[i]);
		}
		System.out.println(" complete!");
		System.out.print("Sorting edges...");
		sortEdges();
		System.out.println(" complete!");

	}

	/**
	 * Finds all trips with arrival time = input time
	 * 
	 * @param time: time we want to find
	 */
	public void findTripByTime(int time) {
		ArrayList<Integer> matchingTrips = new ArrayList<Integer>();
		for (int i = 0; i < stops.length; i++) {
			ArrayList<BusEdge> edges = stops[i].getEdges();
			for (int j = 0; j < edges.size(); j++) {
				BusEdge currentEdge = edges.get(j);
				if (currentEdge.getArrivalTimeAsSeconds() == time) {
					matchingTrips.add(currentEdge.getTripID());
				}
			}
		}
		Collections.sort(matchingTrips);
		for (int i = 0; i < matchingTrips.size(); i++) {
			System.out.println("Details for trip: " + matchingTrips.get(i));

			BusEdge currentEdge = stops[tripIDs.get(matchingTrips.get(i))].findBusEdge(matchingTrips.get(i));

			while (currentEdge != null && currentEdge.getArrivalTimeAsSeconds() <= time) {
				System.out.println(currentEdge);
				currentEdge = currentEdge.getTo().findBusEdge(matchingTrips.get(i));
			}
			System.out.println("\n");
		}

	}

	public BusStop getExactBusStop(String key) {
		return tst.get(key);
	}

	/**
	 * Takes in 2 bus Stops and finds the shortest path between them returns the
	 * 'cost' of the trip
	 * 
	 * @param start:       bus stop we begin from
	 * @param destination: bus stop we want to end at
	 * @return the cost of the shortest trip
	 */

	public double dijkstra(BusStop start, BusStop destination) {
		int startPos = findBusStop(start.getID());
		// initialize distto array, everything but the start intersection should be
		// infinity
		double[] distTo = new double[stops.length];
		Arrays.fill(distTo, Double.POSITIVE_INFINITY);
		distTo[startPos] = 0;

		// initialize visitied array, everything but startinersection should be false
		boolean[] visit = new boolean[stops.length];
		Arrays.fill(visit, false);
		visit[startPos] = true;

		// initialize edgeTo array
		BusEdge[] edgeTo = new BusEdge[stops.length];
		BusEdge temp = new BusEdge(start, start, 0);
		edgeTo[start.getIndex()] = temp;

		// initialize queue for visiting
		ArrayList<BusStop> toVisit = new ArrayList<BusStop>();
		toVisit.add(start);
		while (!toVisit.isEmpty()) {
			// returns index of intersection in toVisit with least distance from start
			BusStop currentStop = findMinPriority(toVisit, distTo);

			for (BusEdge currentEdge : currentStop.getEdges()) {
				// if node has not been visited or isnt on the list already, add it to the queue
				if (!visit[currentEdge.getTo().getIndex()]) {
					toVisit.add(currentEdge.getTo());
					visit[currentEdge.getTo().getIndex()] = true;
				}

				// relax - if the new proposed distance is less than whats in the distTo array,
				// replace it
				if (distTo[currentEdge.getTo().getIndex()] > distTo[currentEdge.getFrom().getIndex()]
						+ currentEdge.getCost()) {
					distTo[currentEdge.getTo().getIndex()] = distTo[currentEdge.getFrom().getIndex()]
							+ currentEdge.getCost();
					edgeTo[currentEdge.getTo().getIndex()] = currentEdge;
				}
			}
		}

		printPath(edgeTo, destination, start, distTo[destination.getIndex()]);

		return distTo[destination.getIndex()];
	}

	public BusStop findMinPriority(ArrayList<BusStop> stops, double[] dist) {
		int minIndex = -1;
		double minDist = Double.POSITIVE_INFINITY;
		for (int index = 0; index < stops.size(); index++) {
			int i = stops.get(index).getIndex();
			if (minDist > dist[i]) {
				minIndex = index;
				minDist = dist[i];
			}
		}
		return stops.remove(minIndex);
	}

	private void printPath(BusEdge[] edges, BusStop destination, BusStop start, double totalCost) {
		System.out.println();
		if (totalCost != Double.POSITIVE_INFINITY) {

			Stack<BusStop> busStops = new Stack<BusStop>();
			// Stack<BusEdge> path = new Stack<BusEdge>();
			busStops.push(destination);

			BusEdge current = edges[destination.getIndex()];
			while (current.getTo() != start) {
				busStops.push(current.getFrom());
				// path.push(current);
				current = edges[current.getFrom().getIndex()];

				if (current == null) {
					break;
				}
				// System.out.println(current);
			}
			BusStop previous = null;
			System.out.println("\nBus Stops\n");
			while (!busStops.empty()) {
				BusStop currentStop = busStops.pop();
				if (previous != null) {
					if (previous.existsTransfer(currentStop)) {
						System.out.println("TRANSFER");
						System.out.println("   |	 ");
						System.out.println("   V	 ");
					}
				}
				System.out.println(currentStop);
				previous = currentStop;
				if (!busStops.empty()) {
					System.out.println("   |	 ");
					System.out.println("   V	 ");
				}
			}

			// System.out.println("\nTrip\n");

			// while (!path.empty()) {
			// System.out.println(path.pop());
			// }
			System.out.println("\nTotal Cost of Trip: " + totalCost + "\n");
		} else {
			System.out.println("There are no paths to " + destination.getName() + ".");
		}
	}

	/**
	 * Reads in stop_times file and creates edges from data, adding them to the
	 * approppriate bus stop
	 * 
	 * @param file:  name of file that contains trip information
	 * @param stops: Array of all bus stops
	 **/
	private void readInTrips(BusStop[] stops, String file) {
		CSVLoader c = new CSVLoader(file);
		ArrayList<String> list = c.readFile();
		for (int i = 2; i < list.size(); i++) {
			String[] from = list.get(i - 1).split(",");
			String[] to = list.get(i).split(",");
			if (from[0].equals(to[0])) {
				int fromIndex = findBusStop(Integer.parseInt(from[3]));
				int toIndex = findBusStop(Integer.parseInt(to[3]));
				int tripID = Integer.parseInt(from[0]);
				String departureTime = from[2];
				String arrivalTime = to[1];
				int seq = Integer.parseInt(from[4]);
				double fromDist = 0;
				double toDist = Double.parseDouble(to[to.length - 1]);
				if (from.length == 9) {
					fromDist = Double.parseDouble(from[from.length - 1]);
				}

				int depHour = Integer.parseInt(departureTime.replaceAll(" ", "").split(":")[0]);
				int arrHour = Integer.parseInt(arrivalTime.replaceAll(" ", "").split(":")[0]);
				if (!(depHour > 23 || arrHour > 23)) {
					stops[fromIndex].addEdge(new BusEdge(tripID, stops[fromIndex], stops[toIndex], departureTime,
							arrivalTime, seq, toDist - fromDist));
				}
				if (!tripIDs.containsKey(tripID))
					tripIDs.put(tripID, fromIndex);
			}
		}
	}

	public BusStop[] searchBusStop(String busStopName) {
		String[] matches = tst.keysWithPrefix(busStopName.toUpperCase());
		// remove later and return string array
		// for (int i = 0; i < matches.length; i++) {
		// System.out.println((i + 1) + ". " + matches[i]);
		// }
		BusStop[] stops = new BusStop[matches.length];
		for (int i = 0; i < stops.length; i++) {
			stops[i] = tst.get(matches[i]);
		}
		Arrays.sort(stops);
		return stops;
	}

	/**
	 * Reads in transfers file and creates edges from data and adds it to
	 * appropriate bus stop
	 * 
	 * @param file:  name of file that contains transfers
	 * @param stops: array of all bus stops
	 */
	private void readInTransfers(BusStop[] stops, String file) {
		CSVLoader c = new CSVLoader(file);
		ArrayList<String> list = c.readFile();

		for (int i = 1; i < list.size(); i++) {
			String[] transfer = list.get(i).split(",");
			int fromIndex = findBusStop(Integer.parseInt(transfer[0]));
			int toIndex = findBusStop(Integer.parseInt(transfer[1]));
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
	public int findBusStop(int id) {
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
	 * @param fileName: name of file that contains the stops
	 * @return array of all bus stops
	 */
	private BusStop[] readInStopData(String file) {
		CSVLoader c = new CSVLoader(file);
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

			token = data[2].split(" ")[1];
			if (token.length() == 1
					&& (token.equals("N") || token.equals("E") || token.equals("S") || token.equals("W"))) {
				busStopName = busStopName.substring(2) + " " + token;
			}

			stops[i] = new BusStop(Integer.parseInt(data[0]), code, busStopName, data[3], Double.parseDouble(data[4]),
					Double.parseDouble(data[5]), data[6], Integer.parseInt(data[8]), parent);
		}
		Arrays.sort(stops);
		for (int i = 0; i < stops.length; i++) {
			stops[i].setIndex(i);
		}
		return stops;
	}

	private void sortEdges() {
		for (BusStop stop : stops) {
			stop.sortEdges();
		}
	}

}