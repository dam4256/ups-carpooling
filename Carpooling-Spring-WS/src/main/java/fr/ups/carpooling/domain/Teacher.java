package fr.ups.carpooling.domain;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public class Teacher {

    private String id;
    private String lastName;
    private String firstName;
    private String mail;
    private String address;
    private Integer zip;
    private String town;
    private String latitude = "0";
    private String longitude = "0";

    public Teacher(String lastName, String firstName, String mail,
            String address, int zip, String town) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.mail = mail;
        this.address = address;
        this.zip = zip;
        this.town = town;
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
        return id;
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

    @Override
    public String toString() {
        return new StringBuilder().append("Teacher{").append("_id='")
                .append(id).append('\'').append(", name='").append(lastName)
                .append('\'').append(", fname='").append(firstName)
                .append('\'').append(", mail='").append(mail).append('\'')
                .append(", address='").append(address).append('\'')
                .append(", zip=").append(zip).append(", Town='").append(town)
                .append('\'').append(", latitude='").append(latitude)
                .append('\'').append(", longitude='").append(longitude)
                .append('\'').append('}').toString();
    }

    private double distance(double lat2, double lon2) {
        double theta = Double.valueOf(this.longitude) - lon2;
        double dist = Math.sin(deg2rad(Double.valueOf(this.latitude)))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(Double.valueOf(this.latitude)))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    /* :: This function converts decimal degrees to radians : */
    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);

    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    /* :: This function converts radians to decimal degrees : */
    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public Boolean inrange(Teacher voisin, Double range) {
        Double dist = this.distance(Double.valueOf(voisin.getLatitude()),
                Double.valueOf(voisin.getLongitude()));
        System.out.println(dist);
        return (dist <= range);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Teacher) {
            return this.id.equals(((Teacher) obj).id);
        } else {
            return false;
        }
    }
}
