package fr.ups.carpooling.services;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;
import org.lightcouch.View;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.ups.carpooling.domain.OSMNode;
import fr.ups.carpooling.domain.OSMWrapperAPI;
import fr.ups.carpooling.domain.User;
import fr.ups.carpooling.domain.constants.Constants;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public class RegistrationServiceImpl implements RegistrationService {

    /**
     * CouchDB Client.
     */
    private CouchDbClient dbClient;

    /**
     * XML document.
     */
    private Document document;

    /**
     * Result (OK / KO).
     */
    private String result;

    /**
     * Code (100 / 110 / 200 / 300). May be null.
     */
    private Integer code;

    /**
     * Error associated with the code. May be null.
     */
    private String error;

    public Element register(User user) throws Exception {
        // Set up the CouchDB database.
        dbClient = new CouchDbClient();

        // Verify the constraints.
        if (isValidConstraints(user)) {
            // Carry out the registration in accordance with the previous
            // verifications.
            Response response = dbClient.save(user);
            // Verify that the registration has been successfully completed.
            if (response.getId() != null) {
                user = dbClient.find(User.class, response.getId());
                result = "OK";
            } else {
                result = "KO";
                code = 300;
                error = response.getError();
            }
        }

        // Return the response.
        return createResponse(user);
    }

    /**
     * Verify the following constraints:
     * <ul>
     * <li>Unused email address
     * <li>Invalid email address
     * <li>Unknown mailing address
     * </ul>
     * 
     * @param user
     *            the user to verify
     * @return <code>true</code> if the user is valid; <code>false</code>
     *         otherwise
     */
    private boolean isValidConstraints(User user) throws Exception {
        // Verify that the email address is free.
        View view = dbClient.view("application/viewmail");
        view.includeDocs(true);
        view.key(user.getMail());
        List<User> users = view.query(User.class);
        if (users.size() > 0) {
            result = "KO";
            code = 100;
            error = "Adresse email deja utilisee";
            return false;
        }

        // Verify that the email address is valid.
        if (!user.getMail().endsWith("@univ-tlse3.fr")) {
            result = "KO";
            code = 110;
            error = "Adresse email invalide";
            return false;
        }

        // Verify that the mailing address is real.
        String url = Constants.OPENSTREETMAP_URL + user.getAddress() + " "
                + user.getZip() + " " + user.getTown()
                + Constants.OPENSTREETMAP_ENDING;
        ;
        List<OSMNode> osmNodesInVicinity = null;
        osmNodesInVicinity = OSMWrapperAPI.getNodes(OSMWrapperAPI
                .getXMLFile(url));
        if (osmNodesInVicinity == null || osmNodesInVicinity.size() == 0) {
            result = "KO";
            code = 200;
            error = "Adresse postale non connue de Open Street Map";
            return false;
        }

        // Get the first OSM node, even if there are several responses.
        if (osmNodesInVicinity.size() > 1 || osmNodesInVicinity.size() == 1) {
            user.setLatitude(osmNodesInVicinity.get(0).getLat());
            user.setLongitude(osmNodesInVicinity.get(0).getLon());
        }

        // Return true if every constraint is compliant.
        return true;
    }

    /**
     * Create the response of the registration service.
     * 
     * @param user
     *            the user associated with the request
     * @return the XML element completed
     * @throws ParserConfigurationException
     */
    private Element createResponse(User user)
            throws ParserConfigurationException {
        // Create a new DOM.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        // Set DOM properties.
        document.setXmlStandalone(true);

        // Create the DOM tree diagram.
        Element root = document.createElement("RegistrationResponse");
        document.appendChild(root);
        root.setAttribute("xmlns", Constants.NAMESPACE_URI);
        root.setAttribute("xmlns:xs", Constants.NAMESPACE_XMLSCHEMA);
        root.setAttribute("xs:schemaLocation", Constants.NAMESPACE_URI + " "
                + "Registration.xsd");
        root.setAttribute("LastName", user.getLastName());
        root.setAttribute("FirstName", user.getFirstName());
        root.setAttribute("UPSMail", user.getMail());
        root.setAttribute("Address", user.getAddress());
        root.setAttribute("ZipCode", String.valueOf(user.getZip()));
        root.setAttribute("Town", user.getTown());

        // Create the main tag.
        Element resultElement = document.createElement("Result");
        resultElement.setTextContent(result);
        root.appendChild(resultElement);

        // Verify if an error has occurred during processing.
        if (result.equals("KO")) {
            // Set the error and its code.
            Element codeElement = document.createElement("Code");
            codeElement.setTextContent(String.valueOf(code));
            root.appendChild(codeElement);
            Element errorElement = document.createElement("Error");
            errorElement.setTextContent(error);
            root.appendChild(errorElement);
        }

        return document.getDocumentElement();
    }

}
