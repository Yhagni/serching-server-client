import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XmlReader {

    private File fileXML;
    private DocumentBuilderFactory docBuilderFactory;
    private DocumentBuilder docBuilder;
    private Document doc;
    private Element root;


    public XmlReader() throws ParserConfigurationException, IOException, SAXException {
        this.fileXML = new File("Users.xml");
        this.docBuilderFactory = DocumentBuilderFactory.newInstance();
        this.docBuilder = docBuilderFactory.newDocumentBuilder();
        this.doc = docBuilder.parse(fileXML);
        this.root = doc.getDocumentElement();
    }

    public void addElementToXmlFile(String username, String password) throws TransformerException {
        Element user = doc.createElement("user");
        Element usernameElem = doc.createElement("username");
        Element passwordElem = doc.createElement("password");

        usernameElem.appendChild(doc.createTextNode(username));
        passwordElem.appendChild(doc.createTextNode(password));

        user.appendChild(usernameElem);
        user.appendChild(passwordElem);
        root.appendChild(user);

        saveNewElementToXmlFile();
    }

    private void saveNewElementToXmlFile() throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File("Users.xml"));
        transformer.transform(domSource, streamResult);
    }

    public boolean isTheSameUser(String login) {

        doc.getDocumentElement().normalize();
        doc.getDocumentElement().getNodeName();
        NodeList nodeList = doc.getElementsByTagName("user");
        for (int itr = 0; itr < nodeList.getLength(); itr++) {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                if (login.equals(eElement.getElementsByTagName("username").item(0).getTextContent())) {
                    return true;
                }
            }
        }


        return false;
    }

    public boolean logIn(String login, String password) {

        doc.getDocumentElement().normalize();
        doc.getDocumentElement().getNodeName();
        NodeList nodeList = doc.getElementsByTagName("user");
        for (int itr = 0; itr < nodeList.getLength(); itr++) {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                if (login.equals(eElement.getElementsByTagName("username").item(0).getTextContent()) && password.equals(eElement.getElementsByTagName("password").item(0).getTextContent())) {
                    return true;
                }
            }
        }

        return false;
    }

    public String passwordReminder(String login) {

        doc.getDocumentElement().normalize();
        doc.getDocumentElement().getNodeName();
        NodeList nodeList = doc.getElementsByTagName("user");
        for (int itr = 0; itr < nodeList.getLength(); itr++) {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                if (login.equals(eElement.getElementsByTagName("username").item(0).getTextContent())) {
                    return eElement.getElementsByTagName("password").item(0).getTextContent();
                }
            }
        }
        return "";
    }

}