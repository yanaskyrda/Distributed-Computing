<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>lab6</title>
</head>
<body>
    <center>
        <h1>Cities</h1>
        <h2>
            <a href="${pageContext.request.contextPath}/CityNew">Add New City</a>
            &nbsp;&nbsp;&nbsp;
            <a href="${pageContext.request.contextPath}/CityList">List All Cities</a>
            &nbsp;&nbsp;&nbsp;
            <a href="index.html">Back</a>
        </h2>
    </center>
    <div align="center">
        <table border="1" cellpadding="5">
            <caption><h2>List of Cities</h2></caption>
            <tr>
                <th>ID</th>
                <th>Country ID</th>
                <th>Name</th>
                <th>Population</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="city" items="${listCity}">
                <tr>
                    <td><c:out value="${city.id}" /></td>
                    <td><c:out value="${city.countryId}" /></td>
                    <td><c:out value="${city.name}" /></td>
                    <td><c:out value="${city.population}" /></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/CityEdit?id=<c:out value='${city.id}' />">Edit</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="${pageContext.request.contextPath}/CityDelete?id=<c:out value='${city.id}' />">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>   
</body>
</html>