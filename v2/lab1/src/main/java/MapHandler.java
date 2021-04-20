import lombok.Setter;
import lombok.var;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Collections;

public class MapHandler extends DefaultHandler {
    private static final String COUNTRY = "country";
    private static final String ID = "id";
    private static final String COUNTRYNAME = "countryName";

    private static final String CITY = "city";
    private static final String COUNTRYID = "countryId";
    private static final String CITYID = "cityid";
    private static final String CITYNAME = "cityName";
    private static final String POPULATION = "population";

    private WorldMap map = new WorldMap();
    @Setter
    private String elementValue;
    private Object currObject;

    @Override
    public void characters(char[] ch, int start, int length){
        elementValue = new String(ch, start, length);
    }

    @Override
    public void startDocument() {
        map = new WorldMap();
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr){
        switch (qName) {
            case COUNTRY:
                Country country = new Country();
                map.addCountry(country);
                break;
            case CITY:
                City city = new City();
                map.addCity(city);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName){
        setField(qName);
    }

    private Country lastCountry() {
        return (Country) currObject;
    }

    private String lastCountryKey() {
        String key = null;
        for (var entry : map.getCountries().entrySet()) {
            if (entry.getValue().equals(currObject)) {
                key = entry.getKey();
            }
        }
        return key;
    }

    private City lastCity() {
        return (City) currObject;
    }

    private String lastCityKey() {
        String key = null;
        for (var entry : map.getCities().entrySet()) {
            if (entry.getValue().equals(currObject)) {
                key = entry.getKey();
            }
        }
        return key;
    }

    public WorldMap getMap() {
        return map;
    }

    public String getName() {
        return "map";
    }

    void setField(String qName) {
        switch (qName) {
            case ID:
                Country country = lastCountry();
                country.setId(elementValue);
                map.getCountries().remove(lastCountryKey());
                map.getCountries().put(elementValue, country);
                if (country.getName() != null && !map.getCountryNames().containsKey(country.getName())) {
                    map.getCountryNames().put(country.getName(), country.getId());
                }
                break;
            case COUNTRYNAME:
                country = lastCountry();
                country.setName(elementValue);
                if (country.getId() != null && !map.getCountryNames().containsKey(elementValue)) {
                    map.getCountryNames().put(elementValue, country.getId());
                }
                break;
            case COUNTRYID:
                City city = lastCity();
                city.setCountryId(elementValue);
                country = map.getCountries().get(elementValue);
                if (country.getCities() == null) {
                    country.setCities(Collections.singletonList(city));
                } else {
                    country.getCities().add(city);
                }
                break;
            case CITYID:
                city = lastCity();
                city.setId(elementValue);
                map.getCities().remove(lastCityKey());
                map.getCities().put(elementValue, city);
                if (city.getName() != null && !map.getCityNames().containsKey(city.getName())) {
                    map.getCityNames().put(city.getName(), elementValue);
                }
                break;
            case CITYNAME:
                city = lastCity();
                city.setName(elementValue);
                if (city.getId() != null && !map.getCityNames().containsKey(elementValue)) {
                    map.getCityNames().put(elementValue, city.getId());
                }
                break;
            case POPULATION:
                city = lastCity();
                city.setPopulation(Integer.parseInt(elementValue));
                break;
        }
    }

    void setField(String qName, String attribute){
        switch (qName) {
            case COUNTRY:
                Country country = new Country();
                country.setId(attribute);
                currObject = country;
                map.addCountry(country);
                break;
            case CITY:
                City city = new City();
                city.setId(attribute);
                currObject = city;
                map.addCity(city);
                break;
            default:
                setField(qName);
                break;
        }
    }
}
