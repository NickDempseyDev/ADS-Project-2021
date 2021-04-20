import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class BusGraph {
	BusStop[] stops;
	TST<BusStop> tst;

	public BusGraph(String stopsFile, String tripsFile, String transferFile) {
		stops = readInStopData(stopsFile);
		readInTrips(stops, tripsFile);
		readInTransfers(stops, transferFile);
		tst = new TST<BusStop>();
		for (int i = 0; i < stops.length; i++) {
			tst.put(stops[i].getName(), stops[i]);
		}
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
		ArrayList<BusEdge> toVisit = new ArrayList<BusEdge>();

		do {
			// returns index of intersection in toVisit with least distance from start
			BusStop currentStop = start;
			int toGet = -1;

			if (toVisit.size() > 0) {
				toGet = findMinPriority(toVisit, distTo);
				currentStop = toVisit.remove(toGet).getTo();// toVisit.get(toGet).getTo();
			}

			for (int i = 0; i < currentStop.getEdges().size(); i++) {
				BusEdge currentEdge = currentStop.getEdges().get(i);
				// if node has not been visited or isnt on the list already, add it to the queue
				if (visit[currentEdge.getTo().getIndex()] != true) {
					toVisit.add(currentEdge);
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

			// remove current intersection
			// toVisit.remove(toGet);
		} while (toVisit.size() > 0);

		printPath(edgeTo, destination, start, distTo[destination.getIndex()]);

		return distTo[destination.getIndex()];
	}

	private void printPath(BusEdge[] edges, BusStop destination, BusStop start, double totalCost) {
		Stack<BusStop> busStops = new Stack<BusStop>();
		Stack<BusEdge> path = new Stack<BusEdge>();
		busStops.push(destination);

		BusEdge current = edges[destination.getIndex()];
		while (current.getTo() != start) {
			busStops.push(current.getFrom());
			path.push(current);
			current = edges[current.getFrom().getIndex()];
			// System.out.println(current);
		}

		System.out.println("\nBus Stops\n");
		while (!busStops.empty()) {
			System.out.println(busStops.pop());
		}
		System.out.println("\nTrip\n");

		while (!path.empty()) {
			System.out.println(path.pop());
		}
		System.out.println("\nTotal Cost of Trip: " + totalCost + "\n");

	}

	private int findMinPriority(ArrayList<BusEdge> intersections, double[] costs) {
		// instantiate mindistance and mindid as intersection at position 0 in
		// intersections
		double minCost = costs[intersections.get(0).getTo().getIndex()];
		int minID = 0;

		for (int i = 1; i < intersections.size(); i++) {
			if (costs[intersections.get(i).getTo().getIndex()] < minCost) {
				minCost = costs[intersections.get(i).getTo().getIndex()];
				minID = i;
			}
		}

		return minID;
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
				if (from.length == 8) {
					fromDist = Double.parseDouble(from[from.length - 1]);
				}
				stops[fromIndex].addEdge(new BusEdge(tripID, stops[fromIndex], stops[toIndex], departureTime,
						arrivalTime, seq, toDist - fromDist));
			}
		}
	}

	public void searchBusStop(String busStopName) {
		String[] matches = tst.keysWithPrefix(busStopName);
		// remove later and return string array
		for (int i = 0; i < matches.length; i++) {
			System.out.println((i + 1) + ". " + matches[i]);
		}
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

			stops[i] = new BusStop(Integer.parseInt(data[0]), code, busStopName, data[3], Double.parseDouble(data[4]),
					Double.parseDouble(data[5]), data[6], Integer.parseInt(data[8]), parent);
		}
		Arrays.sort(stops);
		for (int i = 0; i < stops.length; i++) {
			stops[i].setIndex(i);
		}
		return stops;
	}

}