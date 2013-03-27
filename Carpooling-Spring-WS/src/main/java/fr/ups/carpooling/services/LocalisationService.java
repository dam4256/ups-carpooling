package fr.ups.carpooling.services;

import org.w3c.dom.Element;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public interface LocalisationService {

    /**
     * Search for possible neighbours inside a given radius.
     * 
     * @param userID
     *            the user associated with the search
     * @param radiusKM
     *            the radius around the user, in kilometers
     * @return the possible neighbours
     * @throws Exception
     */
    Element searchForNeighbours(Integer userID, Integer radiusKM) throws Exception;

}
