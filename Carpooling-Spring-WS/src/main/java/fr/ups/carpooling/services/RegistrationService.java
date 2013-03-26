package fr.ups.carpooling.services;

import org.jdom.Element;

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
     */
    Element register(User user);

}
