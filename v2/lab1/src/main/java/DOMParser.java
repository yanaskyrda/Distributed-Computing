import lombok.var;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DOMParser {
    public static class SimpleErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            System.out.println("Line " + e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }

        public void error(SAXParseException e) throws SAXException {
            System.out.println("Line " + e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }

        public void fatalError(SAXParseException e) throws SAXException {
            System.out.println("Line " + e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
    }

    private static void setFieldAndAttributes(Node node, MapHandler mapHandler) {
        Element e = (Element) node;
        mapHandler.setField(e.getTagName(), null);
        //mapHandler.setField("country", null);
        var attributes = e.getAttributes();
        for (int j = 0; j < attributes.getLength(); j++) {
            mapHandler.setElementValue(attributes.item(j).getNodeValue());
            mapHandler.setField(attributes.item(j).getNodeName());
        }
    }

    public static WorldMap parse(String path) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new SimpleErrorHandler());
        Document doc = builder.parse(new File(path));
        doc.getDocumentElement().normalize();

        MapHandler mapHandler = new MapHandler();
        Node parentNode = doc.getChildNodes().item(1);
        NodeList nodeList = parentNode.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            setFieldAndAttributes(node, mapHandler);
            NodeList childNodes = node.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                setFieldAndAttributes(childNodes.item(j), mapHandler);
            }
        }
        return mapHandler.getMap();
    }

    public static void write(WorldMap map, String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        var doc = builder.newDocument();
        Element root = doc.createElement("map");
        doc.appendChild(root);

        var countries = map.getCountries();
        for (Map.Entry<String, Country> entry : countries.entrySet()) {
            Element countryElem = doc.createElement("country");
            countryElem.setAttribute("id", entry.getValue().getId());
            countryElem.setAttribute("countryName", entry.getValue().getName());
            root.appendChild(countryElem);

            for (City city : entry.getValue().getCities()) {
                Element cityElem = doc.createElement("city");
                cityElem.setAttribute("cityid", city.getId());
                cityElem.setAttribute("countryId", city.getCountryId());
                cityElem.setAttribute("cityName", city.getName());
                cityElem.setAttribute("population", String.valueOf(city.getPopulation()));
                countryElem.appendChild(cityElem);
            }
        }
        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(path));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "map.dtd");
        transformer.transform(domSource, fileResult);
    }
}
