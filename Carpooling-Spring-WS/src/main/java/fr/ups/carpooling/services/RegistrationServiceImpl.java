package fr.ups.carpooling.services;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
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

    private CouchDbClient dbClient;

    private Document document;

    private String result;
    private Integer code;
    private String error;

    public Element register(User user) {
        // Set up the CouchDB database.
        /*
         * dbClient = new CouchDbClient();
         * 
         * // Verify the constraints. if (isValidConstraints(user)) { // Carry
         * out the registration in accordance with the previous //
         * verifications. Response response = dbClient.save(user);
         * 
         * // Verify that the registration has been successfully completed. if
         * (response.getId() != null) { user = dbClient.find(User.class,
         * response.getId()); result = "OK"; } else { result = "KO"; code = 300;
         * error = response.getError(); } }
         */
        result = "KO";
        code = 100;
        error = "Adresse email deja utilisee";

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
    private boolean isValidConstraints(User user) {
        // Verify that the email address is free.
        View view = dbClient.view("application/viewmail");
        view.includeDocs(true);
        view.key(user.getMail());
        List<User> users = view.query(User.class);
        System.out.println(users.size());
        if (users.size() > 0) {
            result = "KO";
            code = 100;
            error = "Adresse email deja utilisee";
            return false;
        }

        // Verify that the email address is valid.
        // System.out.println(user.getMail().endsWith("@univ-tlse3.fr"));
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
        // System.out.println(url);
        List<OSMNode> osmNodesInVicinity = null;
        try {
            osmNodesInVicinity = OSMWrapperAPI.getNodes(OSMWrapperAPI
                    .getXMLFile(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(osmNodesInVicinity.size());
        if (osmNodesInVicinity == null || osmNodesInVicinity.size() == 0) {
            result = "KO";
            code = 200;
            error = "Adresse postale non connue de Open Street Map";
            return false;
        }
        if (osmNodesInVicinity.size() > 1) {
            result = "KO";
            code = 200;
            error = "Adresse postale pas assez precise, plusieurs resultats possibles";
            return false;
        }
        if (osmNodesInVicinity.size() == 1)
            for (OSMNode osmNode : osmNodesInVicinity) {
                System.out.println(osmNode.getId() + ":" + osmNode.getLat()
                        + ":" + osmNode.getLon());
                user.setLatitude(osmNode.getLat());
                user.setLongitude(osmNode.getLon());
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
     */
    private Element createResponse(User user) {
        try {
            // Create a new DOM.
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();

            // Create the DOM tree diagram.
            Element root = document.createElement("RegistrationResponse");
            root.setAttribute("xmlns", Constants.NAMESPACE_URI);
            root.setAttribute("xmlns:xs", Constants.NAMESPACE_XMLSCHEMA);
            root.setAttribute("xs:schemaLocation", Constants.NAMESPACE_URI
                    + " " + "Registration.xsd");
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

}
