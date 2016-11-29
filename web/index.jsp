<%--
  Created by IntelliJ IDEA.
  User: Dmitry
  Date: 25.11.2016
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Connection Page</title>
  </head>
  <body>
  <form action="Servlet" method="post">
    <input type="hidden" name="action" value="connection"/>
    Database ip: <input type="text" name="ip" value="95.213.204.15">
    <br>
    Database port: <input type="text" name="port" value="5432">
    <br>
    Database name: <input type="text" name="name" value="kek">
    <br>
    Database user: <input type="text" name="user" value="testuser">
    <br>
    Database password: <input type="password" name="password" value="testuser">
    <br>
    <input type="submit" value="Submit">
  </form>
  </body>
</html>
