package busscheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BusSchedule {
    private List<CityStop> busStops;

    public BusSchedule() {
        busStops = new ArrayList<>();
    }

    void addBusStop(String city) {
        busStops.add(new CityStop(city));
        System.out.println("Added bus stop: " + city);
    }

    void deleteBusStop(String city) {
        CityStop cityStopToDelete = null;
        for (CityStop cityStop : busStops) {
            if (cityStop.getCity().equals(city)) {
                cityStopToDelete = cityStop;
                break;
            }
        }
        if (cityStopToDelete == null) {
            return;
        } else {
            Set<CityStop> stopsConnectedToDeleted = new HashSet<>(
                    cityStopToDelete.getNeighbors().keySet());
            for (CityStop cityStop : stopsConnectedToDeleted) {
                cityStopToDelete.removeConnection(cityStop);
            }
            busStops.remove(cityStopToDelete);
        }
        System.out.println("Deleted bus stop: " + city);
    }

    void changeFlightPrice(String firstCity, String secondCity, int price) {
        CityStop firstCityStopToChange = getCityStopByName(firstCity);
        CityStop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange != null && secondCity != null
                && firstCityStopToChange.isConnected(secondCityStopToChange)) {
            firstCityStopToChange.connect(secondCityStopToChange, price);
            System.out.println("Changed price for flight from " + firstCity + " to " + secondCity
                    + " for " + price);
        }
    }

    void addFlight(String firstCity, String secondCity, int price) {
        CityStop firstCityStopToChange = getCityStopByName(firstCity);
        CityStop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange != null && secondCity != null) {
            firstCityStopToChange.connect(secondCityStopToChange, price);
            System.out.println("Added flight from " + firstCity + " to " + secondCity
                    + " with price " + price);
        }
    }

    void deleteFlight(String firstCity, String secondCity) {
        CityStop firstCityStopToChange = getCityStopByName(firstCity);
        CityStop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange != null && secondCity != null
                && firstCityStopToChange.isConnected(secondCityStopToChange)) {
            firstCityStopToChange.removeConnection(secondCityStopToChange);
            System.out.println("Deleted flight from " + firstCity + " to " + secondCity);
        }
    }

    Integer getFlightPrice(String firstCity, String secondCity) {
        CityStop firstCityStopToChange = getCityStopByName(firstCity);
        CityStop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange == null || secondCityStopToChange == null) {
            throw new IllegalArgumentException("No such bus stop");
        }

        return firstCityStopToChange.getPrice(secondCityStopToChange);
    }

    private CityStop getCityStopByName(String cityName) {
        CityStop cityStop = new CityStop(cityName);
        if (busStops.contains(cityStop)) {
            return busStops.get(busStops.indexOf(cityStop));
        }
        return null;
    }
}
