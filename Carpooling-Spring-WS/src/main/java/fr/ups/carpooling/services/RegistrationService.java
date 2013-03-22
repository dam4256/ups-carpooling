package fr.ups.carpooling.services;

public interface RegistrationService {

    void register(String lastName, String firstName, String mail,
            String address, int zip, String town);

    String getResult();

    Integer getCode();

    String getError();

}
