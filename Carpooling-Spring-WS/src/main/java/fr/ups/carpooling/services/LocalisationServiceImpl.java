package fr.ups.carpooling.services;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.ups.carpooling.domain.User;
import fr.ups.carpooling.domain.constants.Constants;

public class LocalisationServiceImpl implements LocalisationService {

    private Document document;

    private Integer userID;
    private Integer radiusKM;

    public Element searchForNeighbours(Integer userID, Integer radiusKM)
            throws ParserConfigurationException {
        List<User> neighbours = new ArrayList<User>();
        this.userID = userID;
        this.radiusKM = radiusKM;

        // Complete neighbours.
        // ... Met ton code là!

        // Return the response.
        return createResponse(neighbours);
    }

    private Element createResponse(List<User> neighbours)
            throws ParserConfigurationException {
        // Create a new DOM.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        // Set DOM properties.
        document.setXmlStandalone(true);

        // Create the DOM tree diagram.
        Element root = document.createElement("LocalisationResponse");
        document.appendChild(root);
        root.setAttribute("xmlns", Constants.NAMESPACE_URI);
        root.setAttribute("xmlns:xs", Constants.NAMESPACE_XMLSCHEMA);
        root.setAttribute("xs:schemaLocation", Constants.NAMESPACE_URI + " "
                + "Localisation.xsd");
        root.setAttribute("UserID", String.valueOf(userID));
        root.setAttribute("RadiusKM", String.valueOf(radiusKM));

        for (User neighbour : neighbours) {
            Element user = createUser(neighbour);
            root.appendChild(user);
        }

        return document.getDocumentElement();
    }

    private Element createUser(User user) {
        Element root = document.createElement("User");

        Element lastName = document.createElement("LastName");
        lastName.setTextContent(user.getLastName());
        root.appendChild(lastName);

        Element firstName = document.createElement("FirstName");
        firstName.setTextContent(user.getFirstName());
        root.appendChild(firstName);

        Element upsMail = document.createElement("UPSMail");
        upsMail.setTextContent(user.getMail());
        root.appendChild(upsMail);

        Element address = document.createElement("Address");
        address.setTextContent(user.getAddress());
        root.appendChild(address);

        Element zipCode = document.createElement("ZipCode");
        zipCode.setTextContent(String.valueOf(user.getZip()));
        root.appendChild(zipCode);

        Element town = document.createElement("Town");
        town.setTextContent(user.getTown());
        root.appendChild(town);

        return root;
    }

}
