package fr.ups.carpooling.services;

public class RegistrationServiceImpl implements RegistrationService {

    private String result;
    private Integer code;
    private String error;

    public void register(String lastName, String firstName, String mail,
            String address, int zip, String town) {
        // result = ...
        // code = ...
        // error = ...
    }

    public String getResult() {
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

}
