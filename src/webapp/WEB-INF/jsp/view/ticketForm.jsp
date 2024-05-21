<html>
<head>
  <title>Tickets</title>
</head>
<body>
<h2>Tickets</h2>
<a href="<c:url value='/Ticket'>
        <c:param name='action' value='createTicket' />
    </c:url>">Create Post</a><br><br>
<c:choose>
  <c:when test="${TicketDatabase.size() == 0}">
    <p>There are no ticket posts yet...</p>
  </c:when>
  <c:otherwise>
    <c:forEach var="Ticket" items="${TicketDatabase}">
      Ticket#:&nbsp;<c:out value="${Ticket.key}"/>
      <a href="<c:url value='/Ticket' >
                    <c:param name='action' value='view' />
                    <c:param name='TicketId' value='${Ticket.key}' />
                </c:url>">&nbsp;<c:out value="${Ticket.value.title}"/></a><br>
    </c:forEach>
  </c:otherwise>
</c:choose>

</body>
</html>
