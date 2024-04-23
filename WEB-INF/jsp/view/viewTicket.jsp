<html>
<head>
    <title>Ticket #<c:out value="${ticketId}"/></title>
</head>
<body>
<h2>Ticket Post</h2>
<h3>Ticket #<c:out value="${ticketId}"/>: <c:out value="${ticket.title}"/></h3>
<p>Date: <c:out value="${ticket.date}"/></p>
<p><c:out value="${ticket.body}"/></p>
<c:if test="${ticket.hasImage()}">
    <a href="<c:url value='/ticket' >
            <c:param name='action' value='download' />
            <c:param name='ticketId' value='${ticketId}' />
            <c:param name='image' value='${ticekt.image.name}'/>
        </c:url>"><c:out value="${ticket.image.name}"/></a>
</c:if>
<br><a href="ticket">Return to ticket list</a>

</body>
</html>
