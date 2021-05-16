package data.dao;

import data.dto.CityDTO;
import data.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityDAO {
    public static CityDTO findById(long id) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "SELECT * "
                            + "FROM city "
                            + "WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getCityDTO(preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CityDTO getCityDTO(PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
        CityDTO city = null;
        if (resultSet.next()) {
            city = new CityDTO();
            city.setId(resultSet.getLong(1));
            city.setName(resultSet.getString(2));
            city.setCountryId(resultSet.getLong(3));
            city.setPopulation(resultSet.getLong(4));
        }
        preparedStatement.close();
        return city;
    }

    public static CityDTO findByName(String name) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "SELECT * "
                            + "FROM city "
                            + "WHERE name = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getCityDTO(preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean update(CityDTO city) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "UPDATE city "
                            + "SET name = ?, countryId = ?, population = ? "
                            + "WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, city.getName());
            preparedStatement.setLong(2, city.getCountryId());
            preparedStatement.setLong(3, city.getPopulation());
            preparedStatement.setLong(4, city.getId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insert(CityDTO city) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "INSERT INTO city (name,countryId,population) "
                            + "VALUES (?,?,?) "
                            + "RETURNING id";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, city.getName());
            preparedStatement.setLong(2, city.getCountryId());
            preparedStatement.setLong(3, city.getPopulation());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                city.setId(resultSet.getLong(1));
            } else
                return false;
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(CityDTO city) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "DELETE FROM city "
                            + "WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, city.getId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<CityDTO> findAll() {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "SELECT * "
                            + "FROM city";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<CityDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                CityDTO city = new CityDTO();
                city.setId(resultSet.getLong(1));
                city.setName(resultSet.getString(2));
                city.setCountryId(resultSet.getLong(3));
                city.setPopulation(resultSet.getLong(4));
                list.add(city);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<CityDTO> findByCountryId(Long id) {
        try (Connection connection = DBConnection.getConnection();) {
            String sql =
                    "SELECT * "
                            + "FROM city "
                            + "WHERE countryId = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<CityDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                CityDTO city = new CityDTO();
                city.setId(resultSet.getLong(1));
                city.setName(resultSet.getString(2));
                city.setCountryId(resultSet.getLong(3));
                city.setPopulation(resultSet.getLong(4));
                list.add(city);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
