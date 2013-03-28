package fr.ups.carpooling.domain;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;
import org.lightcouch.View;
import org.xml.sax.SAXException;

import fr.ups.carpooling.domain.constants.Constants;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public class RegistrationService {

    public void register(String name, String mail, String address, int zip, String town) throws IOException, SAXException, ParserConfigurationException {
        // initialize a new client instance - reuse
        CouchDbClient dbClient = new CouchDbClient();

// CRUD API
User foo = new User(name,"ddsfsd", mail,address,zip,town);
        User foo2 = new User(name,"vqvdsvd",mail,"15 avenue du colonel roche ",31400,"Toulouse");
        Response response = dbClient.save(foo);
        foo = dbClient.find(User.class, response.getId());
        System.out.println(foo.toString());
        View v =dbClient.view("application/viewmail");
        v.includeDocs(true);
        v.key(mail);
        List<User> res =v.query(User.class);
        System.out.println(res.size()+" " +res.toString());
        String adresse=Constants.OPENSTREETMAP_URL;
        adresse+=foo2.getAddress()+" "+foo2.getZip()+" "+foo2.getTown();
        adresse+=Constants.OPENSTREETMAP_ENDING;
        URL osm = null;
        System.out.println(adresse);
        List<OSMNode> osmNodesInVicinity =OSMWrapperAPI.getNodes(OSMWrapperAPI.getXMLFile(adresse));
        if(osmNodesInVicinity.size()==1)
        for (OSMNode osmNode : osmNodesInVicinity) {
            System.out.println(osmNode.getId() + ":" + osmNode.getLat() + ":" + osmNode.getLon());
            foo2.setLatitude(osmNode.getLat());
            foo2.setLongitude(osmNode.getLon());
        }
        Response response2 = dbClient.save(foo2);
        foo2 = dbClient.find(User.class, response2.getId());
        System.out.println(foo2.toString());
        View vv =dbClient.view("application/viewusers");
        vv.includeDocs(true);
        List<User> ress = vv.query(User.class);
        System.out.println(ress.size()+" " +ress.toString());
        List<User> voisins = new ArrayList<User>();
        for(User f : ress)
        {
            if(foo2.inrange(f,25) && (!f.equals(foo2)))
            {
                voisins.add(f);
            }
        }

        System.out.println(voisins.size()+" " +voisins.toString());
        //get(builder(getDBUri()).path(id).build(), classType);
    }
}
