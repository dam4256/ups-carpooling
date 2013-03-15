package fr.ups.carpooling.ws;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

@Endpoint
public class RegistrationEndpoint {
    
    private static final String NAMESPACE_URI = "http://ups.fr/carpooling/schemas";
    
    private XPath nameExpression;
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
        
        nameExpression = XPath.newInstance("concat(//reg:FirstName,' ',//reg:LastName)");
        nameExpression.addNamespace(namespace);
        
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
    public void handleRegistrationRequest(@RequestPayload Element registrationRequest)
            throws Exception {
        String name = nameExpression.valueOf(registrationRequest);
        String mail = mailExpression.valueOf(registrationRequest);
        String address = addressExpression.valueOf(registrationRequest);
        int zip = Integer.parseInt(zipExpression.valueOf(registrationRequest));
        String town = townExpression.valueOf(registrationRequest);
        
        registrationService.register(name, mail, address, zip, town);
    }

}
