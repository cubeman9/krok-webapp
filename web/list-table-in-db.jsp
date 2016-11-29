<%@ page import="java.sql.ResultSetMetaData" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Set" %><%--
  Created by IntelliJ IDEA.
  User: Dmitry
  Date: 26.11.2016
  Time: 2:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>list</title>
</head>
<body>
    <%
        List<String> columnNames = (List<String>) request.getAttribute("columnNames");
        ResultSet resultSet = (ResultSet) request.getAttribute("resultSet");
    %>
    <table>
        <thead>
            <tr>
                <%
                    for (int i = 0; i < columnNames.size(); i++) {
                        out.println("<th>" + columnNames.get(i) + "</th>");
                    }
                %>
            </tr>
        </thead>
        <tbody>
            <%
                while (resultSet.next()) {
                    out.println("<tr>");
                    for (int i = 0; i < columnNames.size(); i++) {
                        out.println("<td><a data-method=\"delete\" href=\"Servlet?action=listChosenTable&name=" + resultSet.getString(columnNames.get(i)) + "\">" + resultSet.getString(columnNames.get(i)) + "</a></td>");
                    }
                    out.println("</tr>");
                }
            %>
        </tbody>
    </table>
    <br>
    <a href="index.jsp">Change connection</a>
</body>
</html>
