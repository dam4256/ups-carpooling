package fr.ups.carpooling.services.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;
import org.w3c.dom.Element;

import fr.ups.carpooling.domain.constants.Constants;
import fr.ups.carpooling.services.LocalisationService;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
@Endpoint
public class LocalisationEndpoint {

    /**
     * Service associated with the endpoint.
     */
    private LocalisationService localisationService;

    /**
     * Create the endpoint.
     * 
     * @param localisationService
     *            the service associated with
     */
    public LocalisationEndpoint(LocalisationService localisationService) {
        this.localisationService = localisationService;
    }

    /**
     * Process request.
     * 
     * @param userID
     *            the user ID in the database
     * @param radiusKM
     *            the radius in kilometers
     * @return the service response
     * @throws Exception
     */
    @PayloadRoot(namespace = Constants.NAMESPACE_URI, localPart = "LocalisationRequest")
    @Namespace(prefix = "cp", uri = Constants.NAMESPACE_URI)
    @ResponsePayload
    public Element handleLocalisationRequest(
            @XPathParam("/cp:LocalisationRequest/cp:UserID") Integer userID,
            @XPathParam("/cp:LocalisationRequest/cp:RadiusKM") Integer radiusKM)
            throws Exception {
        // Search possible neighbours inside the radius and return the response.
        Element response = localisationService.searchForNeighbours(userID,
                radiusKM);
        return response;
    }

}
