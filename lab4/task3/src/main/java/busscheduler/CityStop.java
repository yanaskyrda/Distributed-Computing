package busscheduler;

import java.util.HashMap;
import java.util.Map;

public class CityStop {
    private String city;
    private Map<CityStop, Integer> neighbors;

    CityStop(String city) {
        this.city = city;
        this.neighbors = new HashMap<>();
    }

    void connect(CityStop node, int price) {
        if (this == node) {
            throw new IllegalArgumentException("Can't ride in within one city");
        }
        this.neighbors.put(node, price);
        node.neighbors.put(this, price);
    }

    void removeConnection(CityStop node) {
        neighbors.remove(node);
        node.neighbors.remove(this);
    }

    boolean isConnected(CityStop node) {
        return neighbors.containsKey(node);
    }

    Map<CityStop, Integer> getNeighbors() {
        return neighbors;
    }

    String getCity() {
        return city;
    }

    Integer getPrice(CityStop other) {
        if (this.neighbors.containsKey(other)) {
            return this.neighbors.get(other);
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (this.getClass() != other.getClass()) {
            return false;
        }

        return this.city.equals(((CityStop) other).city);
    }
}
