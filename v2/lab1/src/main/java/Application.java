import lombok.var;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Map;

public class Application extends JFrame {
    private static JFrame frame;

    private static WorldMap map;
    private static Country currentCountry = null;
    private static City currentCity = null;

    private static boolean editMode = false;
    private static boolean countryMode = true;

    private static JButton btnAddCountry = new JButton("Add Country");
    private static JButton btnAddCity = new JButton("Add City");
    private static JButton btnEdit = new JButton("Edit Data");
    private static JButton btnBack = new JButton("Back");
    private static JButton btnSave = new JButton("Save");
    private static JButton btnDelete = new JButton("Delete");

    private static Box menuPanel = Box.createVerticalBox();
    private static Box actionPanel = Box.createVerticalBox();
    private static Box comboPanel = Box.createVerticalBox();
    private static Box cityPanel = Box.createVerticalBox();
    private static Box countryPanel = Box.createVerticalBox();

    private static JComboBox comboCountry = new JComboBox();
    private static JComboBox comboCity = new JComboBox();

    private static JTextField textCountryName = new JTextField(30);
    private static JTextField textCityName = new JTextField(30);
    private static JTextField textCityCountryName = new JTextField(30);
    private static JTextField textCityPopulation = new JTextField(30);

    private Application() {
        super("World Map");
        frame = this;
        frame.setPreferredSize(new Dimension(400, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                frame.dispose();
                try {
                    DOMParser.write(map, "src/main/java/map.xml");
                } catch (ParserConfigurationException | TransformerException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        Box box = Box.createVerticalBox();
        sizeAllElements();
        frame.setLayout(new FlowLayout());

        // Menu
        menuPanel.add(btnAddCountry);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddCountry.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = false;
                countryMode = true;
                menuPanel.setVisible(false);
                comboPanel.setVisible(false);
                countryPanel.setVisible(true);
                cityPanel.setVisible(false);
                actionPanel.setVisible(true);
                pack();
            }
        });
        menuPanel.add(btnAddCity);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddCity.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = false;
                countryMode = false;
                menuPanel.setVisible(false);
                comboPanel.setVisible(false);
                countryPanel.setVisible(false);
                cityPanel.setVisible(true);
                actionPanel.setVisible(true);
                pack();
            }
        });
        menuPanel.add(btnEdit);
        menuPanel.add(Box.createVerticalStrut(20));
        btnEdit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = true;
                menuPanel.setVisible(false);
                comboPanel.setVisible(true);
                countryPanel.setVisible(false);
                cityPanel.setVisible(false);
                actionPanel.setVisible(true);
                pack();
            }
        });

        // ComboBoxes
        comboPanel.add(new JLabel("Country:"));
        comboPanel.add(comboCountry);
        comboPanel.add(Box.createVerticalStrut(20));
        comboCountry.addActionListener(e -> {
            String name = (String) comboCountry.getSelectedItem();
            currentCountry = map.getCountry(name);
            countryMode = true;
            countryPanel.setVisible(true);
            cityPanel.setVisible(false);
            fillCountryFields();
            pack();
        });
        comboPanel.add(new JLabel("City:"));
        comboPanel.add(comboCity);
        comboPanel.add(Box.createVerticalStrut(20));
        comboCity.addActionListener(e -> {
            String name = (String) comboCity.getSelectedItem();
            currentCity = map.getCity(name);
            countryMode = false;
            countryPanel.setVisible(false);
            cityPanel.setVisible(true);
            fillCityFields();
            pack();
        });
        fillComboBoxes();
        comboPanel.setVisible(false);

        // City Fields
        cityPanel.add(new JLabel("Name:"));
        cityPanel.add(textCityName);
        cityPanel.add(Box.createVerticalStrut(20));
        cityPanel.add(new JLabel("Country Name:"));
        cityPanel.add(textCityCountryName);
        cityPanel.add(Box.createVerticalStrut(20));
        cityPanel.add(new JLabel("Population:"));
        cityPanel.add(textCityPopulation);
        cityPanel.add(Box.createVerticalStrut(20));
        cityPanel.setVisible(false);

        // Country Fields
        countryPanel.add(new JLabel("Name:"));
        countryPanel.add(textCountryName);
        countryPanel.add(Box.createVerticalStrut(20));
        countryPanel.setVisible(false);

        // Action Bar		
        actionPanel.add(btnSave);
        actionPanel.add(Box.createVerticalStrut(20));
        btnSave.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                save();
            }
        });
        actionPanel.add(btnDelete);
        actionPanel.add(Box.createVerticalStrut(20));
        btnDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                delete();
            }
        });
        actionPanel.add(btnBack);
        actionPanel.add(Box.createVerticalStrut(20));
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                clearFields();
                menuPanel.setVisible(true);
                comboPanel.setVisible(false);
                countryPanel.setVisible(false);
                cityPanel.setVisible(false);
                actionPanel.setVisible(false);
                pack();
            }
        });
        actionPanel.setVisible(false);

        clearFields();
        box.setPreferredSize(new Dimension(300, 500));
        box.add(menuPanel);
        box.add(comboPanel);
        box.add(countryPanel);
        box.add(cityPanel);
        box.add(actionPanel);
        setContentPane(box);
        //pack();
    }

    private static void sizeAllElements() {
        Dimension dimension = new Dimension(300, 50);
        btnAddCountry.setMaximumSize(dimension);
        btnAddCity.setMaximumSize(dimension);
        btnEdit.setMaximumSize(dimension);
        btnBack.setMaximumSize(dimension);
        btnSave.setMaximumSize(dimension);
        btnDelete.setMaximumSize(dimension);

        btnAddCountry.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddCity.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension panelDimension = new Dimension(300, 300);
        menuPanel.setMaximumSize(panelDimension);
        comboPanel.setPreferredSize(panelDimension);
        actionPanel.setPreferredSize(panelDimension);
        cityPanel.setPreferredSize(panelDimension);
        countryPanel.setPreferredSize(panelDimension);

        comboCountry.setPreferredSize(dimension);
        comboCity.setPreferredSize(dimension);

        textCityCountryName.setPreferredSize(dimension);
        textCityName.setPreferredSize(dimension);
        textCityPopulation.setPreferredSize(dimension);
        textCountryName.setPreferredSize(dimension);
    }

    private static void save() {
        if (editMode) {
            if (countryMode) {
                var oldName = currentCountry.getName();
                var newName = textCountryName.getText();
                if (changeCountry(currentCountry) && !currentCountry.getName().equals(oldName)) {
                    comboCountry.removeItem(oldName);
                    comboCountry.addItem(newName);
                    comboCountry.setSelectedIndex(comboCountry.getItemCount() - 1);
                }
            } else {
                var oldName = currentCity.getName();
                var newName = textCityCountryName.getText();
                if (changeCity(currentCity) && !currentCity.getName().equals(oldName)) {
                    comboCity.removeItem(oldName);
                    comboCity.addItem(newName);
                    comboCity.setSelectedIndex(comboCity.getItemCount() - 1);
                }
            }
        } else {
            if (countryMode) {
                var country = new Country();
                map.generateId(country);
                if (changeCountry(country)) {
                    map.addCountry(country);
                    comboCountry.addItem(country.getName());
                }
            } else {
                var city = new City();
                map.generateId(city);
                if (changeCity(city)) {
                    map.addCity(city);
                    comboCity.addItem(city.getName());
                }
            }
        }
    }

    private static boolean changeCountry(Country country) {
        var newName = textCountryName.getText();
        if (map.getCountry(newName) == null) {
            map.rename(country, newName);
            return true;
        }
        fillCountryFields();
        JOptionPane.showMessageDialog(null, "Error: this country already exists!");
        return false;
    }

    private static boolean changeCity(City city) {
        var currCountry = map.getCountry(textCityCountryName.getText());
        if (currCountry == null) {
            fillCityFields();
            JOptionPane.showMessageDialog(null, "Error: no such country!");
            return false;
        }
        var newName = textCityName.getText();
        if (map.getCity(newName) == null)
            map.rename(city, newName);
        map.transferCity(city, currCountry);
        city.setPopulation(Integer.parseInt(textCityPopulation.getText()));
        return true;
    }

    private static void delete() {
        if (editMode) {
            if (countryMode) {
                map.removeCountry(currentCountry);
                for (City c : currentCountry.getCities())
                    comboCity.removeItem(c.getName());
                comboCountry.removeItem(currentCountry.getName());
            } else {
                map.removeCity(currentCity);
                comboCity.removeItem(currentCity.getName());
            }
        }
    }

    private void fillComboBoxes() {
        comboCountry.removeAllItems();
        comboCity.removeAllItems();
        var countries = map.getCountries();
        for (Map.Entry<String, Country> entry : countries.entrySet()) {
            comboCountry.addItem(entry.getValue().getName());
            for (City city : entry.getValue().getCities()) {
                comboCity.addItem(city.getName());
            }
        }
    }

    private static void clearFields() {
        textCountryName.setText("");
        textCityName.setText("");
        textCityCountryName.setText("");
        textCityPopulation.setText("");
        currentCountry = null;
        currentCity = null;
    }

    private static void fillCountryFields() {
        if (currentCountry == null)
            return;
        textCountryName.setText(currentCountry.getName());
    }

    private static void fillCityFields() {
        if (currentCity == null)
            return;
        var countries = map.getCountries();
        textCityName.setText(currentCity.getName());
        textCityCountryName.setText(countries.get(currentCity.getCountryId()).getName());
        textCityPopulation.setText(String.valueOf(currentCity.getPopulation()));
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        map = DOMParser.parse("src/main/java/map.xml");
        JFrame myWindow = new Application();
        myWindow.setVisible(true);
    }
}
