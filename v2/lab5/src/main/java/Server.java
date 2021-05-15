import DTO.CityDTO;
import DTO.CountryDTO;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.List;

public class Server {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;

    public void start() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueTo = session.createQueue("toClient");
            Destination queueFrom = session.createQueue("fromClient");

            producer = session.createProducer(queueTo);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            consumer = session.createConsumer(queueFrom);

            while (processQuery()) {
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private boolean processQuery() {
        String response = "";
        String query = "";
        try {
            Message request = consumer.receive(500);
            if (request == null) {
                return true;
            }
            if (request instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) request;
                query = textMessage.getText();
            } else {
                return true;
            }

            String[] fields = query.split("#");
            if (fields.length == 0) {
                return true;
            } else {
                var action = fields[0];
                CountryDTO country;
                CityDTO city;

                switch (action) {
                    case "CountryFindById":
                        var id = Long.parseLong(fields[1]);
                        country = CountryDAO.findById(id);
                        assert country != null;
                        response = country.getName();
                        TextMessage message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityFindByCountryId":
                        id = Long.parseLong(fields[1]);
                        var list = CityDAO.findByCountryId(id);
                        var str = new StringBuilder();
                        assert list != null;
                        citiesToString(list, str);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityFindByName":
                        var name = fields[1];
                        city = CityDAO.findByName(name);
                        assert city != null;
                        response = city.getId().toString() + "#" + city.getCountryId().toString() + "#" + city.getName() + "#" + city.getPopulation().toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CountryFindByName":
                        name = fields[1];
                        country = CountryDAO.findByName(name);
                        assert country != null;
                        response = country.getId().toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityUpdate":
                        id = Long.parseLong(fields[1]);
                        var countryId = Long.parseLong(fields[2]);
                        name = fields[3];
                        var population = Long.parseLong(fields[4]);
                        city = new CityDTO(id, countryId, name, population);
                        if (CityDAO.update(city)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CountryUpdate":
                        id = Long.parseLong(fields[1]);
                        name = fields[2];
                        country = new CountryDTO(id, name);
                        if (CountryDAO.update(country)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityInsert":
                        countryId = Long.parseLong(fields[1]);
                        name = fields[2];
                        population = Long.parseLong(fields[3]);
                        city = new CityDTO((long) 0, countryId, name, population);
                        if (CityDAO.insert(city)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CountryInsert":
                        name = fields[1];
                        country = new CountryDTO();
                        country.setName(name);

                        if (CountryDAO.insert(country)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityDelete":
                        id = Long.parseLong(fields[1]);
                        city = new CityDTO();
                        city.setId(id);
                        if (CityDAO.delete(city)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CountryDelete":
                        id = Long.parseLong(fields[1]);
                        country = new CountryDTO();
                        country.setId(id);
                        if (CountryDAO.delete(country)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CityAll":
                        var citiesList = CityDAO.findAll();
                        str = new StringBuilder();
                        assert citiesList != null;
                        citiesToString(citiesList, str);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "CountryAll":
                        var countriesList = CountryDAO.findAll();
                        str = new StringBuilder();
                        assert countriesList != null;
                        for (CountryDTO currCountry : countriesList) {
                            str.append(currCountry.getId());
                            str.append("#");
                            str.append(currCountry.getName());
                            str.append("#");
                        }
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void citiesToString(List<CityDTO> list, StringBuilder str) {
        for (CityDTO c : list) {
            str.append(c.getId());
            str.append("#");
            str.append(c.getCountryId());
            str.append("#");
            str.append(c.getName());
            str.append("#");
            str.append(c.getPopulation());
            str.append("#");
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

    public static void main(String[] args) {
        var server = new Server();
        server.start();
    }
}
