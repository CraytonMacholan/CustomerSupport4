<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Active Sessions</title>
</head>
<body>
<h2>Active Sessions</h2>
<table border="1">
    <tr>
        <th>Session ID</th>
        <th>Creation Time</th>
        <th>Last Accessed Time</th>
    </tr>
    <c:forEach var="session" items="${sessions}">
        <tr>
            <td>${session.id}</td>
            <td>${session.creationTime}</td>
            <td>${session.lastAccessedTime}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
