import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class BusStop implements Comparable<BusStop> {
    private int ID;
    private int code;
    private String name;
    private String desc;
    private double lat;
    private double lon;
    private String zoneID;
    private int locType;
    private int parent;

    private ArrayList<BusEdge> edges;
    private int index;

    public BusStop(int ID, int code, String name, String desc, double lat, double lon, String zoneID, int locType,
            int parent) {
        this.ID = ID;
        this.code = code;
        this.name = name;
        this.desc = desc;
        this.lat = lat;
        this.lon = lon;
        this.zoneID = zoneID;
        this.locType = locType;
        this.parent = parent;

        edges = new ArrayList<BusEdge>();
        index = -1;
    }

    /**
     * Returns data of bus stop as a string
     * 
     * @return string of bus stop data
     */
    @Override
    public String toString() {
        return ID + "," + code + "," + name + "," + desc + "," + lat + "," + lon + "," + zoneID + "," + locType + ","
                + parent;
    }

    /**
     * Compares bus stop to another busstop based on ids
     * 
     * @param stop: bus stop to be compared to
     * @return 1 if >, -1 if <, 0 if equal
     */
    @Override
    public int compareTo(BusStop stop) {
        if (this.ID == ((BusStop) stop).ID) {
            return 0;
        } else if (this.ID < ((BusStop) stop).ID) {
            return -1;
        }
        return 1;
    }

    public BusEdge findBusEdge(int id) {
		int low = 0;
		int high = edges.size() - 1;

		while (low <= high) {
			int middle = low + ((high - low) / 2);
			int midID = edges.get(middle).getTripID();

			if (midID == id)
				return edges.get(middle);
			else if (midID > id)
				high = middle - 1;
			else
				low = middle + 1;
		}
		return null;
	}

    /**
     * @return bus stop id
     */
    public int getID() {
        return ID;
    }

    /**
     * @return bus stop code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return bus stop name
     */
    public String getName() {
        return name;
    }

    /**
     * @return bus stop desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @return bus stop lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     * @return bus stop lon
     */
    public Double getLon() {
        return lon;
    }

    /**
     * @return bus stop zoneID
     */
    public String getZoneID() {
        return zoneID;
    }

    /**
     * @return bus stop loctype
     */
    public int getLocType() {
        return locType;
    }

    /**
     * @return bus stop parent
     */
    public int getParent() {
        return parent;
    }

    /**
     * @return bus stop edges
     */
    public ArrayList<BusEdge> getEdges() {
        return edges;
    }

    /**
     * Add edge to the list of edges
     * 
     * @param edge: edge to be added to bus stop
     * @return true if it worked, false otherwise
     */
    public boolean addEdge(BusEdge edge) {
        return edges.add(edge);
    }

    /**
     * Sort the edge arraylist
     */
    public void sortEdges() {
        Collections.sort(edges);
    }

    
    /**
     * @return bus stop index in bus stop array
     */
    public int getIndex()
    {
        return index;
    }

     /**
     * @param index: new bus stop index
     */
    public void setIndex(int index)
    {
        this.index = index;
    }
 
}
