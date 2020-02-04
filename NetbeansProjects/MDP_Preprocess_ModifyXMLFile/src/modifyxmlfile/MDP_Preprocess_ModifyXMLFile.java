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
public class MDP_Preprocess_ModifyXMLFile {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            /* Ruta fija de los XML Inicial y Final*/
            String filepath = "c:\\tmp\\PruebaPP_in.xml";
            String newFilepath = "c:\\tmp\\PruebaPP_out.xml";

            /* Variables */
            String stNodeDocType = "TipoDocumento"; // Etiqueta para comprobar tipo documental
            String stValidDocType = "DISCL_APP_DIS"; // Tipo documental comprobado (valor asociado a la etiqueta)
            String stNodeMonoToMulti = "TEXT_SEARCHED"; // Valor del atributo "name" correspondiente al nodo a migrar de Mono a Multivaluado
            String stNameAttrib = "name";   // Nombre del atributo usado para comprobar su valor y saber si es el nodo a migrar
            String stMultivaluados = "Metadatos"; // Nombre de la etiqueta/nodo "Multivaluados"
            String stNodeValor = "valor";   // Nombre del nodo destino "valor" dentro de "Multivaluados"
            String stMetadatos = "Metadatos"; // Nombre de la etiqueta/nodo "Metadatos"

            // Nuevos objetos necesarios para guardar el documento y poder trabajar con el DOM del XML
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder(); // A partir de un fichero XML crea el objeto documento en memoria
            Document doc = docBuilder.parse(filepath);

            // Comprobamos el Tipo de documento
            Node nodoDocType = doc.getElementsByTagName(stNodeDocType).item(0);
            // Filtramos sólo para el tipo de documento "DISCL_APP_DIS"
            if (stValidDocType.equalsIgnoreCase(nodoDocType.getTextContent())) {

                // Log
                System.out.println("Tipo documental correcto: " + stValidDocType);

                // Localizamos Nodo "Metadatos"
                Node metadatos = doc.getElementsByTagName(stMetadatos).item(0);

                // Añadimos dentro del nodo "Metadatos", un nuevo nodo al final para "Multivaluados"
                Element multivaluados = doc.createElement(stMultivaluados);
                metadatos.appendChild(multivaluados);

                // Añadimos atributos a mano
                //multivaluados.setAttribute("name", "TEXT_SEARCHED");
                //multivaluados.setAttribute("type", "string");
                // Node initialNode = doc.getElementsByTagName(stNodeMonoToMulti).item(0);
                // Traspasamos atributos y valor del nodo original al destino

                /* Recorremos todos los nodos hijos del nodo "Metadatos" para localizar el
                *  que se quiere pasar de mono a multivaluado.
                 */
                NodeList nodeList = metadatos.getChildNodes();
                System.out.println(nodeList.getLength());
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    /* Ignoramos nodos incorrectos (texto existente (texto y espacios entre medias) que no corresponde realmente con un nodo)
                    *   y también el nodo nuevo "Multivaluados"
                    */
                    
                    if ("#text".equalsIgnoreCase(node.getNodeName()) || stMultivaluados.equalsIgnoreCase(node.getNodeName())) {
                        continue;
                    }

                    // Comprobamos si el node hijo es el buscado 
                    NamedNodeMap attribs = node.getAttributes();    // Colección de sus atributos
                    Node nodeNameAttr = attribs.getNamedItem(stNameAttrib); // Atributo buscado ("name")
                    System.out.println("Nodo " + i + ". Nombre: " + node.getNodeName() + ". Atributo: " + nodeNameAttr.toString());
                    if (stNodeMonoToMulti.equalsIgnoreCase(nodeNameAttr.getTextContent())) {
                        // Creamos una colección de todos los atributos del nodo para recorrerla
                        NamedNodeMap attrib = node.getAttributes();

                        // Recorremos todos los atributos del nodo origen para pasarlos al nodo destino "Multivaluados"
                        for (int j = 0; j < attrib.getLength(); j++) {
                            Node nodeAttr = attrib.item(j);
                            // Log
                            System.out.println(nodeAttr.toString());
                            // Añadimos cada atributo del nodo origen al nodo destino "Multivaluados"
                            multivaluados.setAttribute(nodeAttr.getNodeName(), nodeAttr.getNodeValue());

                            // Para borrar - Log
                            NamedNodeMap attribMulti = multivaluados.getAttributes();
                            for (int z = 0; z < attribMulti.getLength(); z++) {
                                Node nodeAttrMulti = attribMulti.item(z);
                                System.out.println(nodeAttrMulti.toString());
                            }
                        }
                        // Traspasamos el valor del nodo original al destino "Multivaluados" (en el nodo hijo "valor" único que se ha creado)
                        Element valor = doc.createElement(stNodeValor);
                        String miValor = node.getNodeValue();
                        valor.appendChild(doc.createTextNode(miValor));
                        multivaluados.appendChild(valor);
                    }
                }

                /* 
            Actual
            <Item name="TEXT_SEARCHED" type="string">123</Item>
            Nueva
            <multivalueItem name="TEXT_SEARCHED" type="string">
                <valor>123</valor>
            </multivalueItem>
                 */
                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(newFilepath));
                transformer.transform(source, result);

                System.out.println("Done");
            }

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
