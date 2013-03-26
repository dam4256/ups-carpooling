package fr.ups.carpooling.ws;

import fr.ups.carpooling.domain.Teacher;
import fr.ups.carpooling.services.RegistrationServiceImpl;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import org.jdom.Attribute;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: menestrel
 * Date: 19/03/13
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class Main {



    public static void main(String[] args){
        RegistrationServiceImpl rs = new RegistrationServiceImpl();
        String nom ="ARRONDEL";
        String prenom ="Damien";
        String mail="dduhybuyugb@univ-tlse3.fr";
        String address ="15 avenue des herbettes";
        int zip = 31100;
        String Town ="Toulouse";

        Element e =rs.register(new Teacher(nom,prenom,mail,address,zip,Town));
        System.out.println(e.toString());
        List<Attribute> l = e.getAttributes();
        for(Attribute a : l)
        {
            System.out.println(a.toString());
        }
        System.out.println(e.getContentSize());
        List<Content> lc =e.getContent();
        for(Content content : lc)
        {
            System.out.println(content.getValue());
        }

        try {


            Document doc = new Document(e);
            doc.setRootElement(e);

            /*Element staff = new Element("staff");
            staff.setAttribute(new Attribute("id", "1"));
            staff.addContent(new Element("firstname").setText("yong"));
            staff.addContent(new Element("lastname").setText("mook kim"));
            staff.addContent(new Element("nickname").setText("mkyong"));
            staff.addContent(new Element("salary").setText("199999"));

            doc.getRootElement().addContent(staff);

            Element staff2 = new Element("staff");
            staff2.setAttribute(new Attribute("id", "2"));
            staff2.addContent(new Element("firstname").setText("low"));
            staff2.addContent(new Element("lastname").setText("yin fong"));
            staff2.addContent(new Element("nickname").setText("fong fong"));
            staff2.addContent(new Element("salary").setText("188888"));

            doc.getRootElement().addContent(staff2);  */

            // new XMLOutputter().output(doc, System.out);
            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter("RegsitrationResponse.xml"));

            System.out.println("File Saved!");
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }

}


