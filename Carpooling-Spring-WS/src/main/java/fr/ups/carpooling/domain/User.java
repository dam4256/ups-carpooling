package fr.ups.carpooling.domain;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public class User {

    private String _id;
    private String lastName;
    private String firstName;
    private String mail;
    private String address;
    private Integer zip;
    private String town;
    private String latitude = "0";
    private String longitude = "0";

    public User(String lastName, String firstName, String mail,
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

    @Override
    public String toString() {
        return new StringBuilder().append("User{").append("_id='")
                .append(_id).append('\'').append(", name='").append(lastName)
                .append('\'').append(", fname='").append(firstName)
                .append('\'').append(", mail='").append(mail).append('\'')
                .append(", address='").append(address).append('\'')
                .append(", zip=").append(zip).append(", Town='").append(town)
                .append('\'').append(", latitude='").append(latitude)
                .append('\'').append(", longitude='").append(longitude)
                .append('\'').append('}').toString();
    }


    //calcul de la distance 3D conçue par partir-en-vtt.com
    private double distance(double lat2, double lon2)
    {
        double lat1,lon1,alt1,alt2;
        //rayon de la terre
        int r = 6366;
        lat1 = deg2rad(Double.valueOf(this.latitude));
        lat2 = deg2rad(lat2);
        lon1 = deg2rad(Double.valueOf(this.longitude));
        lon2 = deg2rad(lon2);

        //recuperation altitude en km
        alt1 =0;
        alt2 =0;

        //calcul précis
        double dp= 2 * Math.asin(Math.sqrt(Math.pow (Math.sin((lat1-lat2)/2) , 2) + Math.cos(lat1)*Math.cos(lat2)* Math.pow( Math.sin((lon1-lon2)/2) , 2)));

        //sortie en km
        double d = dp * r;

        //Pythagore a dit que :
        double dist = Math.sqrt(Math.pow(d,2)+Math.pow(alt2-alt1,2));

        return dist;
    }

    /*private double distance(double lat2, double lon2) {
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
    }    */

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    /* :: This function converts decimal degrees to radians : */
    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);

    }



    public Boolean inrange(User voisin, Integer range) {
        Double dist = this.distance(Double.valueOf(voisin.getLatitude()),
                Double.valueOf(voisin.getLongitude()));
        System.out.println(dist);
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

    public void setId(String id) {
        this._id = id;
    }
}
