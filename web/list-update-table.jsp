<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.ResultSetMetaData" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Dmitry
  Date: 26.11.2016
  Time: 3:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Table list</title>
</head>
<body>
    <%
        List<String> columnNames = (List<String>) request.getAttribute("columnNames");
        List<Boolean> isIntList = (List<Boolean>) request.getAttribute("isIntList");
        ResultSet resultSet = (ResultSet) request.getAttribute("resultSet");
        String tableName = (String) request.getAttribute("tableName");
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
        <form action="Servlet" method="post">
            <input type="hidden" name="action" value="update"/>
            <%
                while (resultSet.next()) {
                    out.println("<tr>");
                    int id = resultSet.getInt("id");
                    session.setAttribute("id", id);
                    for (int i = 0; i < columnNames.size(); i++) {
                        out.println("<td><input type=\"text\" name=\"" + columnNames.get(i) + "\" value=\"" + resultSet.getString(columnNames.get(i)) + "\"></td>");
                    }
                    out.println("</tr>");
                }
                out.println("<br><input type=\"submit\" value=\"Update\">");
                session.setAttribute("columnNames", columnNames);
                session.setAttribute("tableName", tableName);
                session.setAttribute("isIntList", isIntList);
            %>
        </form>
        </tbody>
    </table>
    <br>
    <a href="index.jsp">Change connection</a>
    <%
        out.println("<a data-method=\"delete\" href=\"Servlet?action=listChosenTable&name=" + tableName + "\">Back to table list</a>");
    %>
</body>
</html>
