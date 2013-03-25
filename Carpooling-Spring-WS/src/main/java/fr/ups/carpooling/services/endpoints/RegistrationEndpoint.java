package fr.ups.carpooling.services.endpoints;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import fr.ups.carpooling.domain.Teacher;
import fr.ups.carpooling.services.RegistrationService;

@Endpoint
public class RegistrationEndpoint {

    private static final String NAMESPACE_URI = "http://ups/fr/carpooling/schemas";

    private XPath firstNameExpression;
    private XPath lastNameExpression;
    private XPath mailExpression;
    private XPath addressExpression;
    private XPath zipExpression;
    private XPath townExpression;

    private RegistrationService registrationService;

    @Autowired
    public RegistrationEndpoint(RegistrationService registrationService)
            throws JDOMException {
        this.registrationService = registrationService;

        Namespace namespace = Namespace.getNamespace("reg", NAMESPACE_URI);

        firstNameExpression = XPath.newInstance("//reg:FirstName");
        firstNameExpression.addNamespace(namespace);

        lastNameExpression = XPath.newInstance("//reg:LastName");
        lastNameExpression.addNamespace(namespace);

        mailExpression = XPath.newInstance("//reg:UPSMail");
        mailExpression.addNamespace(namespace);

        addressExpression = XPath.newInstance("//reg:Address");
        addressExpression.addNamespace(namespace);

        zipExpression = XPath.newInstance("//reg:ZipCode");
        zipExpression.addNamespace(namespace);

        townExpression = XPath.newInstance("//reg:Town");
        townExpression.addNamespace(namespace);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrationRequest")
    public Element handleRegistrationRequest(
            @RequestPayload Element registrationRequest) throws Exception {
        Namespace namespace = Namespace.getNamespace("reg", NAMESPACE_URI);

        // Process request.
        String firstName = firstNameExpression.valueOf(registrationRequest);
        String lastName = lastNameExpression.valueOf(registrationRequest);
        String mail = mailExpression.valueOf(registrationRequest);
        String address = addressExpression.valueOf(registrationRequest);
        int zip = Integer.parseInt(zipExpression.valueOf(registrationRequest));
        String town = townExpression.valueOf(registrationRequest);

        // Create the teacher in search of a registration.
        Teacher teacher = new Teacher(lastName, firstName, mail, address, zip,
                town);

        // Call the service to register the teacher and return the response.
        Element response = registrationService.register(teacher);
        return response;
    }

}
