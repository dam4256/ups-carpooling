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
public class LocalisationEndpointIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    @Before
    public void createClient() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }

    @Test
    public void localisationEndpoint() throws Exception {
        Source requestPayload = new StreamSource(new ClassPathResource(
                "LocalisationRequest.xml").getInputStream());
        Source responsePayload = new StreamSource(new ClassPathResource(
                "LocalisationResponse.xml").getInputStream());

        mockClient.sendRequest(withPayload(requestPayload)).andExpect(
                payload(responsePayload));
    }
}
