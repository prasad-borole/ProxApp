package beacon.com.test.model;

/**
 * Created by abhishek on 11/5/16.
 */
public class BeaconItem {
    private String beaconId;
    private int rssi;
    private String title;
    private String description;
    private String url;

    public BeaconItem(String beaconId, int rssi, String title, String description, String url) {
        this.beaconId = beaconId;
        this.rssi = rssi;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BeaconItem{" +
                "beaconId='" + beaconId + '\'' +
                ", rssi=" + rssi +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }


}
