/*
 * (c) Jens Kubler
 * This software is public domain
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package fr.ups.carpooling.domain;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.ups.carpooling.domain.constants.Constants;

/**
 * @author Kevin ANATOLE
 * @author Damien ARONDEL
 */
public class OSMWrapperAPI {

    /**
     * Get the XML file from an URI.
     * 
     * @param location
     *            the URI
     * @return the XML document
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document getXMLFile(String location)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        return docBuilder.parse(location);
    }

    /**
     * Get nodes of a OSM XML document.
     * 
     * @param xmlDocument
     *            the OSM XML document
     * @return a list of OSM nodes extracted from the XML document
     */
    @SuppressWarnings("nls")
    public static List<OSMNode> getNodes(Document xmlDocument) {
        List<OSMNode> osmNodes = new ArrayList<OSMNode>();

        Node osmRoot = xmlDocument.getFirstChild();
        NodeList osmXMLNodes = osmRoot.getChildNodes();
        for (int i = 1; i < osmXMLNodes.getLength(); i++) {
            Node item = osmXMLNodes.item(i);
            if (item.getNodeName().equals("place")) {
                NamedNodeMap attributes = item.getAttributes();
                NodeList tagXMLNodes = item.getChildNodes();
                Map<String, String> tags = new HashMap<String, String>();
                for (int j = 1; j < tagXMLNodes.getLength(); j++) {
                    Node tagItem = tagXMLNodes.item(j);
                    NamedNodeMap tagAttributes = tagItem.getAttributes();
                    if (tagAttributes.getLength() != 0) {
                        tags.put(
                                tagAttributes.getNamedItem("k").getNodeValue(),
                                tagAttributes.getNamedItem("v").getNodeValue());
                    }
                }
                Node namedItemID = attributes.getNamedItem("osm_id");
                Node namedItemLat = attributes.getNamedItem("lat");
                Node namedItemLon = attributes.getNamedItem("lon");
                Node namedItemVersion = attributes.getNamedItem("version");

                String id = namedItemID.getNodeValue();
                String latitude = namedItemLat.getNodeValue();
                String longitude = namedItemLon.getNodeValue();
                String version = "0";
                if (namedItemVersion != null) {
                    version = namedItemVersion.getNodeValue();
                }

                osmNodes.add(new OSMNode(id, latitude, longitude, version, tags));
            }

        }
        return osmNodes;
    }

    /**
     * Get OSM nodes in vicinity, according to a latitude and a longitude.
     * 
     * @param lat
     *            the latitude
     * @param lon
     *            the longitude
     * @param vicinityRange
     *            the radius
     * @return the OSM nodes in vicinity
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static List<OSMNode> getOSMNodesInVicinity(double lat, double lon,
            double vicinityRange) throws IOException, SAXException,
            ParserConfigurationException {
        return OSMWrapperAPI.getNodes(getXML(lon, lat, vicinityRange));
    }

    /**
     * Get the list of OSM nodes in XML file.
     * 
     * @param lon
     *            the longitude
     * @param lat
     *            the latitude
     * @param vicinityRange
     *            bounding box in this range
     * @return the XML document containing the queries nodes
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    @SuppressWarnings("nls")
    private static Document getXML(double lon, double lat, double vicinityRange)
            throws IOException, SAXException, ParserConfigurationException {

        DecimalFormat format = new DecimalFormat("##0.0000000",
                DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        String left = format.format(lat - vicinityRange);
        String bottom = format.format(lon - vicinityRange);
        String right = format.format(lat + vicinityRange);
        String top = format.format(lon + vicinityRange);

        String string = Constants.OPENSTREETMAP_API_06 + "map?bbox=" + left
                + "," + bottom + "," + right + "," + top;
        URL osm = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) osm.openConnection();

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        return docBuilder.parse(connection.getInputStream());
    }

}
