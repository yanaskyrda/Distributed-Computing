import DTO.CityDTO;
import DTO.CountryDTO;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private static final String split = "#";

    Client() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueOut = session.createQueue("fromClient");
            Destination queueIn = session.createQueue("toClient");

            producer = session.createProducer(queueOut);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            consumer = session.createConsumer(queueIn);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private String handleMessage(String query, int timeout) throws JMSException {
        TextMessage mes = session.createTextMessage(query);
        producer.send(mes);
        Message message = consumer.receive(timeout);
        if (message == null) {
            return null;
        }

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            return textMessage.getText();
        }
        return "";
    }

    public CountryDTO countryFindById(Long id) {
        var query = "CountryFindById" + split + id.toString();
        try {
            String response = handleMessage(query, 15000);
            return new CountryDTO(id, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CityDTO cityFindByName(String name) {
        var query = "CityFindByName" + split + name;
        try {
            String response = handleMessage(query, 15000);
            assert response != null;

            String[] fields = response.split(split);
            var id = Long.parseLong(fields[0]);
            var countryId = Long.parseLong(fields[1]);
            var population = Long.parseLong(fields[3]);
            return new CityDTO(id, countryId, name, population);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CountryDTO countryFindByName(String name) {
        var query = "CountryFindByName" + split + name;
        try {
            String response = handleMessage(query, 15000);

            assert response != null;
            var responseId = Long.parseLong(response);
            return new CountryDTO(responseId, name);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean cityUpdate(CityDTO city) {
        var query = "CityUpdate" + split + city.getId().toString() +
                "#" + city.getCountryId().toString() + "#" + city.getName()
                + "#" + city.getPopulation().toString();
        try {
            String response = handleMessage(query, 15000);

            return "true".equals(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean countryUpdate(CountryDTO country) {
        var query = "CountryUpdate" + split + country.getId().toString() +
                "#" + country.getName();
        String response = "";
        try {
            response = handleMessage(query, 150000);

            return "true".equals(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cityInsert(CityDTO city) {
        var query = "CityInsert" +
                "#" + city.getCountryId().toString() + "#" + city.getName()
                + "#" + city.getPopulation().toString();
        try {
            String response = handleMessage(query, 15000);

            return "true".equals(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean countryInsert(CountryDTO country) {
        var query = "CountryInsert" +
                "#" + country.getName();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean countryDelete(CountryDTO country) {
        var query = "CountryDelete" + split + country.getId().toString();
        try {
            String response = handleMessage(query, 15000);

            return "true".equals(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cityDelete(CityDTO city) {
        var query = "CityDelete" + split + city.getId().toString();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CountryDTO> countryAll() {
        var query = "CountryAll";
        var list = new ArrayList<CountryDTO>();
        try {
            String response = handleMessage(query, 15000);

            assert response != null;
            String[] fields = response.split(split);
            for (int i = 0; i < fields.length; i += 2) {
                if (fields[i].equals("")) {
                    continue;
                }
                var id = Long.parseLong(fields[i]);
                var name = fields[i + 1];
                list.add(new CountryDTO(id, name));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CityDTO> cityAll() {
        var query = "CityAll";
        var list = new ArrayList<CityDTO>();
        try {
            String response = handleMessage(query, 15000);

            assert response != null;
            return getCityDTOS(list, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CityDTO> cityFindByCountryId(Long idc) {
        var query = "CityFindByCountryId" + split + idc.toString();
        var list = new ArrayList<CityDTO>();
        try {
            String response = handleMessage(query, 15000);

            if ("".equals(response)) {
                return list;
            }

            assert response != null;
            return getCityDTOS(list, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<CityDTO> getCityDTOS(ArrayList<CityDTO> list, String response) {
        String[] fields = response.split(split);
        for (int i = 0; i < fields.length; i += 4) {
            if (fields[i].equals("") || fields[i + 1].equals("") || fields[i + 3].equals("")) {
                continue;
            }
            var id = Long.parseLong(fields[i]);
            var countryId = Long.parseLong(fields[i + 1]);
            var name = fields[i + 2];
            var population = Long.parseLong(fields[i + 3]);
            list.add(new CityDTO(id, countryId, name, population));
        }
        return list;
    }

    public void cleanMessages() {
        try {
            Message message = consumer.receiveNoWait();

            while (message != null) {
                message = consumer.receiveNoWait();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
