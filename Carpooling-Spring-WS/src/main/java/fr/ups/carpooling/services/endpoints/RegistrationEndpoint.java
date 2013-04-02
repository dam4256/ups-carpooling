package fr.ups.carpooling.services.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;
import org.w3c.dom.Element;

import fr.ups.carpooling.domain.User;
import fr.ups.carpooling.domain.constants.Constants;
import fr.ups.carpooling.services.RegistrationService;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
@Endpoint
public class RegistrationEndpoint {

    /**
     * Service associated with the endpoint.
     */
    private RegistrationService registrationService;

    /**
     * Create the endpoint.
     * 
     * @param registrationService
     *            the service associated with
     */
    public RegistrationEndpoint(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Process request.
     * 
     * @param lastName
     *            the last name of the user
     * @param firstName
     *            the first name of the user
     * @param upsMail
     *            the mail of the user
     * @param address
     *            the mailing address of the user
     * @param zipCode
     *            the zip code of the user
     * @param town
     *            the town of the user
     * @return the service response
     * @throws Exception
     */
    @PayloadRoot(namespace = Constants.NAMESPACE_URI, localPart = "RegistrationRequest")
    @Namespace(prefix = "cp", uri = Constants.NAMESPACE_URI)
    @ResponsePayload
    public Element handleRegistrationRequest(
            @XPathParam("/cp:RegistrationRequest/cp:LastName") String lastName,
            @XPathParam("/cp:RegistrationRequest/cp:FirstName") String firstName,
            @XPathParam("/cp:RegistrationRequest/cp:UPSMail") String upsMail,
            @XPathParam("/cp:RegistrationRequest/cp:Address") String address,
            @XPathParam("/cp:RegistrationRequest/cp:ZipCode") Integer zipCode,
            @XPathParam("/cp:RegistrationRequest/cp:Town") String town)
            throws Exception {
        // Create the teacher in search of a registration.
        User user = new User(lastName, firstName, upsMail, address, zipCode,
                town);

        // Call the service to register the teacher and return the response.
        Element response = registrationService.register(user);
        return response;
    }

}
