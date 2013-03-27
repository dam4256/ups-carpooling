package fr.ups.carpooling.services;

import org.w3c.dom.Element;

import fr.ups.carpooling.domain.User;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public interface RegistrationService {

    /**
     * Register a user looking for carpool.
     * 
     * @param user
     *            the user to register
     * @return the registration response
     * @throws Exception
     */
    Element register(User user) throws Exception;

}
