package fr.ups.carpooling.services;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import fr.ups.carpooling.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightcouch.CouchDbClient;
import org.lightcouch.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;

import java.util.List;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("application-context.xml")
public class LocalisationEndpointIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    private RegistrationServiceImpl rs=new RegistrationServiceImpl();

    @Before
    public void createClient() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
        //Initialisation de la base de données
        CouchDbClient dbClient = new CouchDbClient();
        View users =dbClient.view("application/viewusers");
        users.includeDocs(true);
        List<User> potentialneighbours = users.query(User.class);
        for(User voisin : potentialneighbours)
        {
            dbClient.remove(voisin);
        }
        User tempuser = new User("DURBAN","Romain","durban.romain@univ-tlse3.fr","Allée des sciences appliquées",31100,"Toulouse");
        tempuser.setId("1");
        try {
            rs.register(tempuser);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        tempuser = new User("Bitard","Romain","bitard.romain@univ-tlse3.fr","rue des capitouls",31650,"saint-orens de gameville");
        tempuser.setId("2");
        try {
            rs.register(tempuser);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        tempuser = new User("SYLVESTRE","Franck","sylvestre.franck@univ-tlse3.fr","118 route de Narbonne",31520,"Toulouse");
        tempuser.setId("3");
        try {
            rs.register(tempuser);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        tempuser = new User("MASSIE","Henry","massie.henry@univ-tlse3.fr","rue rue sylvain dauriac",31506,"Toulouse");
        tempuser.setId("4");
        try {
            rs.register(tempuser);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        tempuser = new User("CHERBONNAUD","Bernard","cherbonneau.bernard@univ-tlse3.fr","rue d'alger",31500,"Toulouse");
        tempuser.setId("5");
        try {
            rs.register(tempuser);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void localisationEndpoint() throws Exception {
        Source requestPayload = new StreamSource(new ClassPathResource(
                "localisation/LocalisationRequest.xml").getInputStream());
        Source responsePayload = new StreamSource(new ClassPathResource(
                "localisation/LocalisationResponse.xml").getInputStream());

        mockClient.sendRequest(withPayload(requestPayload)).andExpect(
                payload(responsePayload));
    }
}
