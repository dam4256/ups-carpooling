package fr.ups.carpooling.services;

public interface RegistrationService {

    void register(String name, String fname , String mail, String address, int zip, String town);
    String getResult();
    Integer getCode();
    String getError();

}
