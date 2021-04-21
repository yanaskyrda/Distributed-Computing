import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class WorldMap {
    private Map<String, Country> countries = new HashMap<>();
    private Map<String, String> countryNames = new HashMap<>();
    private Map<String, City> cities = new HashMap<>();
    private Map<String, String> cityNames = new HashMap<>();

    public void generateId(Country country) {
        var id = countries.size();
        String idStr = "country" + id;
        while (countries.get(idStr) != null) {
            id++;
            idStr = "country" + id;
        }
        country.setId(idStr);
    }

    public void generateId(City city) {
        var id = cities.size();
        String idStr = "id" + id;
        while (cities.get(idStr) != null) {
            id++;
            idStr = "id" + id;
        }
        city.setId(idStr);
    }

    public void addCountry(Country country) {
        var changed = false;
        if (countries.get(country.getId()) != null) {
            changed = true;
            generateId(country);
        }
        if (changed) {
            for (City ct : country.getCities())
                ct.setCountryId(country.getId());
        }
        countries.put(country.getId(), country);
        countryNames.put(country.getName(), country.getId());
    }

    public void removeCountry(Country country) {
        countries.remove(country.getId());
        countryNames.remove(country.getName());
        for (City ct : country.getCities())
            cities.remove(ct.getId());
    }

    public void addCity(City city) {
        if (cities.get(city.getId()) != null) {
            generateId(city);
        }
        cities.put(city.getId(), city);
        if (city.getCountryId() != null) {
            countries.get(city.getCountryId()).getCities().add(city);
        }
        cityNames.put(city.getName(), city.getId());
    }

    public void removeCity(City city) {
        cities.remove(city.getId());
        cityNames.remove(city.getName());
        countries.get(city.getCountryId()).getCities().remove(city);
    }

    public void transferCity(City city, Country country) {
        var old = countries.get(city.getCountryId());
        if (old != null)
            old.getCities().remove(city);
        city.setCountryId(country.getId());
        country.getCities().add(city);
    }

    public void rename(Country country, String name) {
        countryNames.remove(country.getName());
        country.setName(name);
        countryNames.put(country.getName(), country.getId());
    }

    public void rename(City city, String name) {
        cityNames.remove(city.getName());
        city.setName(name);
        cityNames.put(city.getName(), city.getId());
    }


    public Country getCountry(String name) {
        var id = countryNames.get(name);
        if (id != null)
            return countries.get(id);
        return null;
    }

    public City getCity(String name) {
        var id = cityNames.get(name);
        if (id != null)
            return cities.get(id);
        return null;
    }
}
