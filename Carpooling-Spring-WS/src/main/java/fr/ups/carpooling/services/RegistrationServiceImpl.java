package fr.ups.carpooling.services;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;
import org.lightcouch.View;

import fr.ups.carpooling.domain.OSMNode;
import fr.ups.carpooling.domain.OSMWrapperAPI;
import fr.ups.carpooling.domain.Teacher;
import fr.ups.carpooling.domain.constants.Constants;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public class RegistrationServiceImpl implements RegistrationService {

    private CouchDbClient dbClient;

    private String result;
    private Integer code;
    private String error;

    public Element register(Teacher teacher) {
        // Set up the CouchDB database.
        dbClient = new CouchDbClient();

        // Verify the constraints.
        if (isValidConstraints(teacher)) {
            // Carry out the registration in accordance with the previous
            // verifications.
            Response response = dbClient.save(teacher);

            // Verify that the registration has been successfully completed.
            if (response.getId() != null) {
                teacher = dbClient.find(Teacher.class, response.getId());
                result = "OK";
            } else {
                result = "KO";
                code = 300;
                error = response.getError();
            }
        }

        // Return the response.
        return createResponse(teacher);
    }

    /**
     * Verify the following constraints:
     * <ul>
     * <li>Unused email address
     * <li>Invalid email address
     * <li>Unknown mailing address
     * </ul>
     * 
     * @param teacher
     *            the teacher to verify
     * @return <code>true</code> if the teacher is valid;
     *         <code>false</code> otherwise
     */
    private boolean isValidConstraints(Teacher teacher) {
        // Verify that the email address is free.
        View view = dbClient.view("application/viewmail");
        view.includeDocs(true);
        view.key(teacher.getMail());
        List<Teacher> teachers = view.query(Teacher.class);
        System.out.println(teachers.size());
        if (teachers.size() > 0) {
            result = "KO";
            code = 100;
            error = "Adresse email deja utilisée";
            return false;
        }

        // Verify that the email address is valid.
        //System.out.println(teacher.getMail().endsWith("@univ-tlse3.fr"));
        if (!teacher.getMail().endsWith("@univ-tlse3.fr")) {
            result = "KO";
            code = 110;
            error = "Adresse email invalide";
            return false;
        }

        // Verify that the mailing address is real.
        String url = Constants.OPENSTREETMAP_URL + teacher.getAddress() + " "
                + teacher.getZip() + " " + teacher.getTown()
                + Constants.OPENSTREETMAP_ENDING;
        //System.out.println(url);
        List<OSMNode> osmNodesInVicinity = null;
        try {
            osmNodesInVicinity = OSMWrapperAPI.getNodes(OSMWrapperAPI
                    .getXMLFile(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(osmNodesInVicinity.size());
        if ( osmNodesInVicinity == null ||  osmNodesInVicinity.size() == 0 ) {
            result = "KO";
            code = 200;
            error = "Adresse postale non connue de Open Street Map";
            return false;
        }
        if (osmNodesInVicinity.size() >1) {
            result = "KO";
            code = 200;
            error = "Adresse postale pas assez précise, plusieurs résultats possibles";
            return false;
        }
        if(osmNodesInVicinity.size()==1)
            for (OSMNode osmNode : osmNodesInVicinity) {
                System.out.println(osmNode.getId() + ":" + osmNode.getLat() + ":" + osmNode.getLon());
                teacher.setLatitude(osmNode.getLat());
                teacher.setLongitude(osmNode.getLon());
            }
        
        // Return true if every constraint is compliant.
        return true;
    }

    /**
     * Create the response of the registration service.
     * 
     * @param teacher
     *            the teacher associated with the request
     * @return the XML element completed
     */
    private Element createResponse(Teacher teacher) {
        // Get the global namespace.
        Namespace xmlns = Namespace.getNamespace(Constants.NAMESPACE_URI);
        
        // Create the root element.
        Element response = new Element("RegistrationResponse", xmlns);
        Namespace xs = Namespace.getNamespace("xs",
                Constants.NAMESPACE_XMLSCHEMA);
        response.addNamespaceDeclaration(xs);
        response.setAttribute("schemaLocation", Constants.NAMESPACE_URI + " "
                + "Registration.xsd", xs);

        // Remember the request.
        response.setAttribute("LastName", teacher.getLastName());
        response.setAttribute("FirstName", teacher.getFirstName());
        response.setAttribute("UPSMail", teacher.getMail());
        response.setAttribute("Address", teacher.getAddress());
        response.setAttribute("ZipCode", String.valueOf(teacher.getZip()));
        response.setAttribute("Town", teacher.getTown());
        
        // Create the main tag.
        Element resultat = new Element("Result", xmlns);
        resultat.setText(this.result);
        response.addContent(resultat);
        // Verify if an error has occurred during processing.
        if (this.result.equalsIgnoreCase("KO")) {
            // Set the error and its code.
            Element code = new Element("Code", xmlns);
            code.setText(String.valueOf(this.code));
            response.addContent(code);
            Element error = new Element("Error", xmlns);
            error.setText(this.error);
            response.addContent(error);
        }
        
        Document doc = new Document(response);
        return doc.getRootElement();
    }

}
