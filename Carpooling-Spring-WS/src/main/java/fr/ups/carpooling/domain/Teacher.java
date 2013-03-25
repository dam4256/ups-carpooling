package fr.ups.carpooling.domain;

/**
 * Created with IntelliJ IDEA.
 * User: menestrel
 * Date: 19/03/13
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public class Teacher {
    public String _id;
    private String name;
    private String fname;
    private String mail;
    private String address;
    private int zip;
    private String Town;
    private String latitude = "0";
    private String longitude = "0";

    public Teacher(String name, String fname, String mail, String address, int zip, String town) {
        this.name = name;
        this.fname= fname;
        this.mail = mail;
        this.address = address;
        this.zip = zip;
        this.Town = town;
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
        return Town;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getFname() {
        return fname;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Utilisateur{").append("_id='").append(_id).append('\'').append(", name='").append(name).append('\'').append(", fname='").append(fname).append('\'').append(", mail='").append(mail).append('\'').append(", address='").append(address).append('\'').append(", zip=").append(zip).append(", Town='").append(Town).append('\'').append(", latitude='").append(latitude).append('\'').append(", longitude='").append(longitude).append('\'').append('}').toString();
    }

    private double distance(double lat2, double lon2) {
        double theta = Double.valueOf(this.longitude) - lon2;
        double dist = Math.sin(deg2rad(Double.valueOf(this.latitude))) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(Double.valueOf(this.latitude))) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


    private double deg2rad(double deg) {
     return (deg * Math.PI / 180.0);

    }

    	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    	/*::  This function converts radians to decimal degrees             :*/
    	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


    private double rad2deg(double rad) {
      return (rad * 180 / Math.PI);
         }



    public Boolean inrange(Teacher voisin,Double range)
    {
        Double dist =this.distance(Double.valueOf(voisin.getLatitude()), Double.valueOf(voisin.getLongitude()));
        System.out.println(dist);
       return (dist<=range);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Teacher)
        {
            return this._id.equals(((Teacher) obj)._id);
        }
        else
        {
            return false;
        }
    }
}
