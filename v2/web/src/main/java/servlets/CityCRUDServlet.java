package servlets;

import data.dao.CityDAO;
import data.dto.CityDTO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//@WebServlet("/CityCRUDServlet")
public class CityCRUDServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CityCRUDServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getServletPath();
        String action = url.substring(5);
        try {
            switch (action) {
                case "New":
                    showNewForm(request, response);
                    break;
                case "Insert":
                    insertCity(request, response);
                    break;
                case "Delete":
                    deleteCity(request, response);
                    break;
                case "Edit":
                    showEditForm(request, response);
                    break;
                case "Update":
                    updateCity(request, response);
                    break;
                case "List":
                    listCity(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listCity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<CityDTO> listCity = CityDAO.findAll();
        request.setAttribute("listCity", listCity);
        RequestDispatcher dispatcher = request.getRequestDispatcher("CityList.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("CityForm.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        CityDTO existingCity = CityDAO.findById(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("CityForm.jsp");
        request.setAttribute("city", existingCity);
        dispatcher.forward(request, response);

    }

    private void insertCity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");
        Long countryId = Long.parseLong(request.getParameter("countryId"));
        Long population = Long.parseLong(request.getParameter("population"));

        CityDTO newCity = new CityDTO();
        newCity.setName(name);
        newCity.setCountryId(countryId);
        newCity.setPopulation(population);
        CityDAO.insert(newCity);
        response.sendRedirect("CityList");
    }

    private void updateCity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("name");
        Long countryId = Long.parseLong(request.getParameter("countryId"));
        Long population = Long.parseLong(request.getParameter("population"));

        CityDTO city = new CityDTO(id, countryId, name, population);
        CityDAO.update(city);
        response.sendRedirect("CityList");
    }

    private void deleteCity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        CityDTO city = new CityDTO();
        city.setId(id);
        CityDAO.delete(city);
        response.sendRedirect("CityList");
    }
}
