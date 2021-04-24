public class BusEdge implements Comparable<BusEdge>
{
    private boolean isTransfer;
    private double cost;
    private int tripID;
    private BusStop from;
    private BusStop to;
    private String arrivalTime;
    private String departureTime;
    private int stopSequence;
    private double distance;

    private int timeInt;

    // Constructor for transfer type edges
    public BusEdge(BusStop from, BusStop to, int cost) 
    {
        isTransfer = true;
        this.from = from;
        this.to = to;
        this.cost = cost / 100.0;

        tripID = -1;
        departureTime = "";
        arrivalTime = "";
        stopSequence = -1;
        distance = -1;

        timeInt = -1;
    }

    // Constructor for stop times edges
    public BusEdge(int tripID, BusStop from, BusStop to, String departureTime, String arrivalTime, int stopSequence,
            double distance) 
    {
        isTransfer = false;
        cost = 1;
        this.tripID = tripID;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.stopSequence = stopSequence;
        this.distance = distance;

        timeInt = timeToInt(arrivalTime);
    }

    /**
     * takes in string form of time, h:m:s and return int number of seconds
     * 
     * @param time: string time to be converted
     * @return : num of seconds
     */
    private int timeToInt(String time) 
    {
        time = time.replaceAll(" ", "");
        String[] times = time.split(":");
        int hours = Integer.parseInt(times[0]);
        int minutes = Integer.parseInt(times[1]);
        int seconds = Integer.parseInt(times[2]);

        int secondTime = (hours * 60 * 60) + (minutes * 60) + seconds;

        return secondTime;
    }

    /**
     * Returns data of edge as a string
     * 
     * @return string of edge data
     */
    @Override
    public String toString() 
    {
    	//string will change depend on if the edge is a transfer or not
        if (isTransfer) 
        {
            String s = "TRANSFER From: " + from.getName() + ", To: " + to.getName() + ", COST: " + cost;
            return s;
        } 
        else
        {
            String s = "TripID: " + tripID + ", From: " + from.getName() + ", To: " + to.getName() + ", Departure Time:"
                    + departureTime + ", Arrival Time:" + arrivalTime + ", Seq. No: " + stopSequence + ", Distance: "
                    + String.format("%.5f", distance) + ", COST: " + cost;
            return s;
        }
    }

    /**
     * Compares edge to input edge based on tripid
     * 
     * @param edge: edge to be compared to
     * @return 1 if >, -1 if <, 0 if equal
     */
    @Override
    public int compareTo(BusEdge edge)
    {
        if (tripID < edge.tripID)
            return -1;
        else if (tripID > edge.tripID)
            return 1;
        return 0;
    }

    /**
     * @return edge tripid
     */
    public int getTripID()
    {
        return tripID;
    }

    /**
     * @return edge cost
     */
    public double getCost() 
    {
        return cost;
    }

    /**
     * @return edge start busstop
     */
    public BusStop getFrom() 
    {
        return from;
    }

    /**
     * @return edge end busstop
     */
    public BusStop getTo()
    {
        return to;
    }

    /**
     * @return edge arrival time
     */
    public String getArrivalTime()
    {
        return arrivalTime;
    }

    /**
     * @return edge departuretime
     */
    public String getDepartureTime() 
    {
        return departureTime;
    }

    /**
     * @return edge distance
     */
    public double getDistance() 
    {
        return distance;
    }

    /**
     * @return edge seq number
     */
    public int getSequenceNumber()
    {
        return stopSequence;
    }

    /**
     * @return true if it is a transfer, false otherwise
     */
    public boolean getIsTransfer()
    {
        return isTransfer;
    }

    /**
     * @return the time in seconds
     */
    public int getArrivalTimeAsSeconds()
    {
        return timeInt;
    }
}