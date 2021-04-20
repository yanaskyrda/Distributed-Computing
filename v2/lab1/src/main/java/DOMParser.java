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

    private static String getChildValue(Element element, String name) {
        Element child = (Element) element.getElementsByTagName(name).item(0);
        if (child == null) {
            return "";
        }
        Node node = child.getFirstChild();
        if (node != null) {
            return node.getNodeValue();
        } else {
            return null;
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
        NodeList nodes = doc.getElementsByTagName("country");

        for(int i = 0; i < nodes.getLength(); ++i) {
            mapHandler.setField("country", null);
            Element e = (Element) nodes.item(i);
            var attributes = e.getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                mapHandler.setElementValue( attributes.item(j).getNodeValue());
                mapHandler.setField(attributes.item(j).getNodeName());
            }
        }

        nodes = doc.getElementsByTagName("city");
        for(int i =0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            mapHandler.setField("city", null);
            var attributes = e.getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                mapHandler.setElementValue( attributes.item(j).getNodeValue());
                mapHandler.setField(attributes.item(j).getNodeName());
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
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "map.dtd");
        transformer.transform(domSource, fileResult);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        var map = parse("src/main/java/mod2lab1/map.xml");
        var city = new City("id10", "c1", "Hamburg", 43456);
        write(map, "src/main/java/map.xml");
    }
}
