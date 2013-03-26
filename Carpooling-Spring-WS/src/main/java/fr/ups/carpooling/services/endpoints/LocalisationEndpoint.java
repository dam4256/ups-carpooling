package fr.ups.carpooling.services.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
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

    private LocalisationService localisationService;

    public LocalisationEndpoint(LocalisationService localisationService) {
        this.localisationService = localisationService;
    }

    @PayloadRoot(namespace = Constants.NAMESPACE_URI, localPart = "LocalisationRequest")
    @ResponsePayload
    public Element handleLocalisationRequest(
            @XPathParam("//UserID") Integer userID,
            @XPathParam("//RadiusKM") Integer radiusKM) throws Exception {
        // Search possible neighbours inside the radius and return the response.
        Element response = localisationService.searchForNeighbours(userID,
                radiusKM);
        return response;
    }

}
