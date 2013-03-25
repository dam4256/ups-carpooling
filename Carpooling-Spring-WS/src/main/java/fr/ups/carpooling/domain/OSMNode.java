package fr.ups.carpooling.domain;

import java.util.Map;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public class OSMNode {

    private String id;
    private String lat;
    private String lon;
    private final Map<String, String> tags;
    private String version;

    public OSMNode(String id, String latitude, String longitude,
            String version, Map<String, String> tags) {
        this.version = version;
        this.tags = tags;
        this.lon = longitude;
        this.lat = latitude;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}