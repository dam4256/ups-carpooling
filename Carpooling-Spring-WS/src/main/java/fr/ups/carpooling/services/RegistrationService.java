package fr.ups.carpooling.services;

import org.jdom.Element;

import fr.ups.carpooling.domain.Teacher;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public interface RegistrationService {

    /**
     * Register a teacher looking for carpool.
     * 
     * @param teacher
     *            the teacher to register
     * @return the registration response
     */
    Element register(Teacher teacher);

}
