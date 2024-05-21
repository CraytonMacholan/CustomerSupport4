<html>
<head>
    <title>Ticket #<c:out value="${TicketId}"/></title>
</head>
<body>
    <h2>Ticket Post</h2>
    <h3>Ticket #<c:out value="${TicketId}"/>: <c:out value="${Ticket.title}"/></h3>
    <p>Date: <c:out value="${Ticket.date}"/></p>
    <p><c:out value="${Ticket.body}"/></p>
    <c:if test="${Ticket.hasImage()}">
        <a href="<c:url value='/Ticket' >
            <c:param name='action' value='download' />
            <c:param name='ticketId' value='${TicketId}' />
            <c:param name='image' value='${Ticekt.image.name}'/>
        </c:url>"><c:out value="${Ticket.image.name}"/></a>
    </c:if>
    <br><a href="Ticket">Return to ticket list</a>

</body>
</html>
