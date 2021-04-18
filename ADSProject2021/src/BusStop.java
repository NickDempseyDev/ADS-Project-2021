public class BusStop implements Comparable <BusStop>{
    int ID;
    int code;
    String name;
    String desc;
    double lat;
    double lon;
    String zoneID;
    int locType;
    int parent;

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
    }

    @Override
    public String toString() {
        return ID + "," + code + "," + name + "," + desc + "," + lat + "," + lon + "," + zoneID + "," + locType + ","
                + parent;
    }

    @Override
    public int compareTo(BusStop stop) {
        if (this.ID == ((BusStop) stop).ID) {
            return 0;
        }
        else if (this.ID < ((BusStop) stop).ID) {
            return -1;
        }
        return 1;
    }

}
