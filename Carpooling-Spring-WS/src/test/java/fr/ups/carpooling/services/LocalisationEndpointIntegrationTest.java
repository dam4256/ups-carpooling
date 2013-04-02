package fr.ups.carpooling.services;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

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

import fr.ups.carpooling.domain.User;

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

    private RegistrationServiceImpl rs = new RegistrationServiceImpl();

    @Before
    public void createClient() throws Exception {
        mockClient = MockWebServiceClient.createClient(applicationContext);

        // Initialize the database.
        CouchDbClient dbClient = new CouchDbClient();
        View users = dbClient.view("application/viewusers");
        users.includeDocs(true);
        List<User> potentialneighbours = users.query(User.class);
        for (User voisin : potentialneighbours) {
            dbClient.remove(voisin);
        }

        // User 1.
        User tempuser = new User("Durban", "Romain",
                "romain.durban@univ-tlse3.fr", "Allee des sciences appliquees",
                31100, "Toulouse");
        tempuser.setId("1");
        rs.register(tempuser);

        // User 2.
        tempuser = new User("Bitard", "Romain", "romain.bitard@univ-tlse3.fr",
                "rue des capitouls", 31650, "saint-orens de gameville");
        tempuser.setId("2");
        rs.register(tempuser);

        // User 3.
        tempuser = new User("Silvestre", "Franck",
                "franck.silvestre@univ-tlse3.fr", "118 route de Narbonne",
                31520, "Toulouse");
        tempuser.setId("3");
        rs.register(tempuser);

        // User 4.
        tempuser = new User("Massie", "Henry", "henry.massie@univ-tlse3.fr",
                "rue sylvain dauriac", 31506, "Toulouse");
        tempuser.setId("4");
        rs.register(tempuser);

        // User 5.
        tempuser = new User("Cherbonnaud", "Bernard",
                "bernard.cherbonneau@univ-tlse3.fr", "rue d'alger", 31500,
                "Toulouse");
        tempuser.setId("5");
        rs.register(tempuser);
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
