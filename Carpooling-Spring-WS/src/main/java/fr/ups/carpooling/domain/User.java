package fr.ups.carpooling.domain;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public class User {

    private String _id;
    private String _rev;
    private String lastName;
    private String firstName;
    private String mail;
    private String address;
    private Integer zip;
    private String town;
    private String latitude = "0";
    private String longitude = "0";

    public User(String lastName, String firstName, String mail, String address,
            int zip, String town) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.mail = mail;
        this.address = address;
        this.zip = zip;
        this.town = town;
    }

    public void setId(String id) {
        this._id = id;
    }
    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public int getZip() {
        return zip;
    }

    public String getTown() {
        return town;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getId() {
        return _id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMail() {
        return mail;
    }

    /**
     * Calculate the 3D distance between two sets of coordinates. Extracted from
     * partir-en-vtt.com.
     * 
     * @param lat2
     *            the latitude of the other user
     * @param lon2
     *            the longitude of the other user
     * @return the distance in kilometers
     */
    private double distance(double lat2, double lon2) {
        double lat1, lon1, alt1, alt2;

        // Get the Earth radius.
        int r = 6366;

        // Convert latitude and longitude in radians.
        lat1 = deg2rad(Double.valueOf(this.latitude));
        lat2 = deg2rad(lat2);
        lon1 = deg2rad(Double.valueOf(this.longitude));
        lon2 = deg2rad(lon2);

        // Get the altitude in kilometers.
        alt1 = 0;
        alt2 = 0;

        // Calculate accurately.
        double dp = 2 * Math.asin(Math.sqrt(Math.pow(
                Math.sin((lat1 - lat2) / 2), 2)
                + Math.cos(lat1)
                * Math.cos(lat2)
                * Math.pow(Math.sin((lon1 - lon2) / 2), 2)));

        // Calculate the output in kilometers.
        double d = dp * r;

        // Refer to Pythagorus.
        double dist = Math.sqrt(Math.pow(d, 2) + Math.pow(alt2 - alt1, 2));

        return dist;
    }

    /**
     * Convert decimal degrees to radians.
     * 
     * @param deg
     *            the decimal degrees
     * @return the radians
     */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);

    }

    /**
     * Check if a user is in vicinity of the current one.
     * 
     * @param neighbour
     *            the user to compare with
     * @param range
     *            the radius in kilometers
     * @return <code>true</code> if the users are in range; <code>false</code>
     *         otherwise
     */
    public Boolean inrange(User user, Integer range) {
        Double dist = this.distance(Double.valueOf(user.getLatitude()),
                Double.valueOf(user.getLongitude()));
        return (dist <= range);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return this._id.equals(((User) obj)._id);
        } else {
            return false;
        }
    }

}
