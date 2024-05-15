<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Create Ticket</title>
</head>
<body>
<h2>Create a Ticket Post</h2>
<form method="POST" action="Ticket" enctype="multipart/form-data">
  <input type="hidden" name="action" value="create">
  Title:<br>
  <input type="text" name="title"><br><br>
  Body:<br>
  <textarea name="body" rows="25" cols="100"></textarea><br><br>
  <b>Image</b><br>
  <input type="file" name="file1"><br><br>
  <input type="submit" value="Submit">
</form>
</body>
</html>
