package fr.ups.carpooling.services;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("application-context.xml")
public class RegistrationEndpointIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    @Before
    public void createClient() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
        //initialisation de la base de données
    }

    @Test
    public void registrationEndpoint() throws Exception {
        Source requestPayload;
        Source responsePayload;
        
        // Test 1: OK.
        requestPayload = new StreamSource(new ClassPathResource(
                "RegistrationRequestOK.xml").getInputStream());
        responsePayload = new StreamSource(new ClassPathResource(
                "RegistrationResponseOK.xml").getInputStream());
        mockClient.sendRequest(withPayload(requestPayload)).andExpect(
                payload(responsePayload));
        
        // Test 2: KO 100.
        requestPayload = new StreamSource(new ClassPathResource(
                "RegistrationRequestKO100.xml").getInputStream());
        responsePayload = new StreamSource(new ClassPathResource(
                "RegistrationResponseKO100.xml").getInputStream());
        mockClient.sendRequest(withPayload(requestPayload)).andExpect(
                payload(responsePayload));
        
        // Test 3: KO 110.
        requestPayload = new StreamSource(new ClassPathResource(
                "RegistrationRequestKO110.xml").getInputStream());
        responsePayload = new StreamSource(new ClassPathResource(
                "RegistrationResponseKO110.xml").getInputStream());
        mockClient.sendRequest(withPayload(requestPayload)).andExpect(
                payload(responsePayload));
        
        // Test 4: KO 200.
        requestPayload = new StreamSource(new ClassPathResource(
                "RegistrationRequestKO200.xml").getInputStream());
        responsePayload = new StreamSource(new ClassPathResource(
                "RegistrationResponseKO200.xml").getInputStream());
        mockClient.sendRequest(withPayload(requestPayload)).andExpect(
                payload(responsePayload));
    }
}
