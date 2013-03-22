package fr.ups.carpooling.services;

import fr.ups.carpooling.ws.OSMNode;
import fr.ups.carpooling.ws.OSMWrapperAPI;
import fr.ups.carpooling.ws.Utilisateur;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;
import org.lightcouch.View;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class RegistrationServiceImpl implements RegistrationService {

    private String result;
    private Integer code;
    private String error;

    /**
     *
     * @param name
     * @param fname
     * @param mail
     * @param address
     * @param zip
     * @param town
     */
    public void register(String name, String fname , String mail, String address, int zip, String town) {
        CouchDbClient dbClient = new CouchDbClient();

        //Vérification des contraintes de validité
            //Adresse email déjà utilisée
            View v =dbClient.view("application/viewmail");
            v.includeDocs(true);
            v.key(mail);
            List<Utilisateur> utilisateurs_mail =v.query(Utilisateur.class);
            if(utilisateurs_mail.size()>0)
            {
                result ="K0";
                code = 100;
                error= "Adresse email déjà utilisée";
                return;
            }
            //Adresse email ivalide
            if(!mail.endsWith("@univ-tlse3.fr"))
            {
                result ="K0";
                code = 110;
                error= "Adresse email invalide";
                return;
            }
            //Adresse postale non reconnue par OSM
            String adresse_osm_api="http://nominatim.openstreetmap.org/search/";
            adresse_osm_api+=address+" "+zip+" "+town;
            adresse_osm_api+="?format=xml&addressdetails=1";
            URL osm = null;
            List<OSMNode> osmNodesInVicinity = null;
            try {
                osmNodesInVicinity = OSMWrapperAPI.getNodes(OSMWrapperAPI.getXMLFile(adresse_osm_api));
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (SAXException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            if(osmNodesInVicinity.size()==0)
            {
                result ="K0";
                code = 200;
                error= "Adresse postale non reconnue par OSM";
                return;
            }
        //Toutes les contraintes sont vérifiées maintenant on procède à l'enregistrement
        Utilisateur user = new Utilisateur(name,fname,mail,address,zip,town);
        Response response = dbClient.save(user);
        if(response.getId()!=null)
        {
            user = dbClient.find(Utilisateur.class, response.getId());
            result="OK";
            code = 220;
            error= "Inscription réussie";
        }
        else
        {
            result ="K0";
            code = 300;
            error= response.getError();
        }
        // result = ...
        // code = ...
        // error = ...
    }

    public String getResult() {
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

}
