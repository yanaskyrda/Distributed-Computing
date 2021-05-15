import DTO.CountryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {
    public static CountryDTO findById(long id) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql =
                    "SELECT * "
                            + "FROM country "
                            + "WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            CountryDTO country = null;
            if (resultSet.next()) {
                country = new CountryDTO();
                country.setId(resultSet.getLong(1));
                country.setName(resultSet.getString(2));
            }
            preparedStatement.close();
            return country;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CountryDTO findByName(String name) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql =
                    "SELECT * "
                            + "FROM country "
                            + "WHERE name = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            CountryDTO country = null;
            if (resultSet.next()) {
                country = new CountryDTO();
                country.setId(resultSet.getLong(1));
                country.setName(resultSet.getString(2));
            }
            preparedStatement.close();
            return country;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean update(CountryDTO country) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql =
                    "UPDATE country "
                            + "SET name = ? "
                            + "WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, country.getName());
            preparedStatement.setLong(4, country.getId());
            var result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insert(CountryDTO country) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql =
                    "INSERT INTO country (name) "
                            + "VALUES (?) "
                            + "RETURNING id";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, country.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                country.setId(resultSet.getLong(1));
            } else
                return false;
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(CountryDTO country) {
        try (Connection connection = DBConnection.getConnection()) {
            String sql =
                    "DELETE FROM country "
                            + "WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, country.getId());
            var result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<CountryDTO> findAll() {
        try (Connection connection = DBConnection.getConnection()) {
            String sql =
                    "SELECT * "
                            + "FROM country";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<CountryDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                CountryDTO country = new CountryDTO();
                country.setId(resultSet.getLong(1));
                country.setName(resultSet.getString(2));
                list.add(country);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
