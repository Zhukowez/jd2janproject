<%--
  Created by IntelliJ IDEA.
  User: Noirix
  Date: 09.02.2023
  Time: 17:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Hello</title>
</head>
<body>
${userName}

<div>
    <h1>System Users</h1>
</div>
<div>
    <table>
        <tr>
            <td>User Id</td>
            <td>User Name</td>
            <td>User Surname</td>
            <td>Birth date</td>
            <td>Weight</td>
            <td>Edit</td>
            <td>Delete</td>
        </tr>
        <c:forEach var="athlete" items="${athletes}">
            <tr>
                <td>${athlete.id}</td>
                <td>${athlete.name}</td>
                <td>${athlete.surname}</td>
                <td>${athlete.birthDate}</td>
                <td>${athlete.weight}</td>
                <td><button>Edit</button></td>
                <td><button>Delete</button></td>
            </tr>
        </c:forEach>
    </table>
</div>


</body>
</html>
