package fr.ups.carpooling.ws;
import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.parsers.XMLParser;
import org.ektorp.*;
import org.ektorp.impl.*;
import org.ektorp.http.*;
import org.jdom.JDOMFactory;
import org.lightcouch.CouchDbClient;
import org.lightcouch.Response;
import org.lightcouch.View;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: menestrel
 * Date: 18/03/13
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class RegistrationService {

    public void register(String name, String mail, String address, int zip, String town) throws IOException, SAXException, ParserConfigurationException {
        // initialize a new client instance - reuse
        CouchDbClient dbClient = new CouchDbClient();

// CRUD API
        Foo foo = new Foo(name,mail,address,zip,town);
        Foo foo2 = new Foo(name,mail,"15 avenue du colonel roche ",31400,"Toulouse");
        Response response = dbClient.save(foo);
        foo = dbClient.find(Foo.class, response.getId());
        System.out.println(foo.toString());
        View v =dbClient.view("application/viewmail");
        v.includeDocs(true);
        v.key(mail);
        List<Foo> res =v.query(Foo.class);
        System.out.println(res.size()+" " +res.toString());
        String adresse="http://nominatim.openstreetmap.org/search/";
        adresse+=foo2.getAddress()+" "+foo2.getZip()+" "+foo2.getTown();
        adresse+="?format=xml&addressdetails=1";
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
        foo2 = dbClient.find(Foo.class, response2.getId());
        System.out.println(foo2.toString());
        View vv =dbClient.view("application/viewusers");
        vv.includeDocs(true);
        List<Foo> ress = vv.query(Foo.class);
        System.out.println(ress.size()+" " +ress.toString());
        List<Foo> voisins = new ArrayList<Foo>();
        for(Foo f : ress)
        {
            if(foo2.inrange(f,25.0) && (!f.equals(foo2)))
            {
                voisins.add(f);
            }
        }

        System.out.println(voisins.size()+" " +voisins.toString());
        //get(builder(getDBUri()).path(id).build(), classType);
    }
}
