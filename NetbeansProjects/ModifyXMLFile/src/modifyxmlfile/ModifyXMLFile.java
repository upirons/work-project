/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modifyxmlfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author lrmedina
 */
public class ModifyXMLFile {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            /* Ruta fija XML Inicial */
            String filepath = "c:\\tmp\\file.xml";
            String newFilepath = "c:\\tmp\\newfile.xml";

            // Nuevos objetos necesarios para guardar el documento y poder trabajar con el DOM del XML
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            // Get the root element
            Node company = doc.getFirstChild();

            // Get the staff element , it may not working if tag has spaces, or
            // whatever weird characters in front...it's better to use
            // getElementsByTagName() to get it directly.
            // Node staff = company.getFirstChild();
            // Get the staff element by tag name directly
            Node staff = doc.getElementsByTagName("staff").item(0);

            // update staff attribute
            NamedNodeMap attr = staff.getAttributes();
            Node nodeAttr = attr.getNamedItem("id");
            nodeAttr.setTextContent("2");

            // append a new node to staff
            Element age = doc.createElement("age");
            age.appendChild(doc.createTextNode("28"));
            staff.appendChild(age);

            // loop the staff child node
            NodeList list = staff.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                // get the salary element, and update the value
                if ("salary".equals(node.getNodeName())) {
                    node.setTextContent("2000000");
                }

                //remove firstname
                if ("firstname".equals(node.getNodeName())) {
                    staff.removeChild(node);
                    removeNodeAndTrailingWhitespace(doc);
                }

            }

            

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(newFilepath));
            transformer.transform(source, result);

            System.out.println("Done");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }

    }

    private static void removeNodeAndTrailingWhitespace(Node node) {
        List<Node> exiles = new ArrayList<Node>();

        exiles.add(node);
        for (Node whitespace = node.getNextSibling();
                whitespace != null && whitespace.getNodeType() == Node.TEXT_NODE && whitespace.getTextContent().matches("\\s*");
                whitespace = whitespace.getNextSibling()) {
            exiles.add(whitespace);
        }

        for (Node exile : exiles) {
            exile.getParentNode().removeChild(exile);
        }
    }

}
