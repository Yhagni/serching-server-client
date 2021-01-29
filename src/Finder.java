import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;


public class Finder {

    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document doc;
    private XPathFactory xPathfactory;
    private XPath xpath;

    public Finder() throws ParserConfigurationException, IOException, SAXException {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        doc = builder.parse("Library.xml");
        xPathfactory = XPathFactory.newInstance();
        xpath = xPathfactory.newXPath();
    }

    public String findPhrase(String phrase) throws XPathExpressionException {
        StringBuilder list = new StringBuilder();
        XPathExpression expr = xpath.compile("//book/*//text()[contains(.,'" + phrase + "')]/../..");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        int loop = nodes.getLength();
        if (loop > 10)
        {
            loop = 10;
        }
        for (int i = 0; i < loop; i++) {
            Node node = nodes.item(i);
            Element element = (Element) node;
            list.append("Author: " + element.getElementsByTagName("author").item(0).getTextContent() + "\n");
            list.append("Title: " + element.getElementsByTagName("title").item(0).getTextContent() + "\n");
            list.append("Description: " + element.getElementsByTagName("desc").item(0).getTextContent() + "\n");

        }


        return list.toString();
    }

    public int howManyFoundPhrases(String phrase) throws XPathExpressionException {
        XPathExpression expr = xpath.compile("//book/*//text()[contains(.,'" + phrase + "')]/../..");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nodes.getLength();

    }
}
