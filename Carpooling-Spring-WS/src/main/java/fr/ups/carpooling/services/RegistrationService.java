package fr.ups.carpooling.services;

import org.jdom.Element;

import fr.ups.carpooling.domain.Teacher;

public interface RegistrationService {

    Element register(Teacher teacher);

}
