package servlets;

import data.dao.CityDAO;
import data.dao.CountryDAO;
import data.dto.CityDTO;
import data.dto.CountryDTO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//@WebServlet("/CountryCRUDServlet")
public class CountryCRUDServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CountryCRUDServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getServletPath();
        String action = url.substring(8);
        try {
            switch (action) {
                case "New":
                    showNewForm(request, response);
                    break;
                case "Insert":
                    insertCountry(request, response);
                    break;
                case "Delete":
                    deleteCountry(request, response);
                    break;
                case "Edit":
                    showEditForm(request, response);
                    break;
                case "Update":
                    updateCountry(request, response);
                    break;
                case "List":
                    listCountries(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listCountries(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<CountryDTO> listCountry = CountryDAO.findAll();
        request.setAttribute("listCountry", listCountry);
        RequestDispatcher dispatcher = request.getRequestDispatcher("CountryList.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("CountryForm.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        CountryDTO existingCountry = CountryDAO.findById(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("CountryForm.jsp");
        request.setAttribute("country", existingCountry);
        dispatcher.forward(request, response);

    }

    private void insertCountry(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");

        CountryDTO newCountry = new CountryDTO();
        newCountry.setName(name);
        CountryDAO.insert(newCountry);
        response.sendRedirect("CountryList");
    }

    private void updateCountry(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("name");

        CountryDTO country = new CountryDTO(id, name);
        CountryDAO.update(country);
        response.sendRedirect("CountryList");
    }

    private void deleteCountry(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        List<CityDTO> list = CityDAO.findByCountryId(id);
        assert list != null;
        for (CityDTO city : list) {
            CityDAO.delete(city);
        }
        CountryDTO country = new CountryDTO();
        country.setId(id);
        CountryDAO.delete(country);
        response.sendRedirect("CountryList");
    }
}
