import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.List;

/**
 * Created by Dmitry on 26.11.2016.
 */
public class Servlet extends HttpServlet {

    ConnectionFactory connectionFactory = null;

    private RequestDispatcher updateRequest (HttpServletRequest request) {
        try {
            Connection connection = connectionFactory.getConnection();
            String tableName = request.getParameter("tableName");
            int id = Integer.parseInt(request.getParameter("id"));
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Table selectedTable = new Table(resultSet, tableName);
            request.setAttribute("tableName", selectedTable.getName());
            request.setAttribute("columnNames", selectedTable.getColumnNameList());
            request.setAttribute("isIntList", selectedTable.getIsIntegerList());
            request.setAttribute("resultSet", selectedTable.getElementResultSet());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("list-update-table.jsp");
            connection.close();
            return requestDispatcher;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RequestDispatcher updateExecute (HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            List<String> columnNames = (List<String>) session.getAttribute("columnNames");
            List<Boolean> isIntList = (List<Boolean>) session.getAttribute("isIntList");
            int id = (int) session.getAttribute("id");
            String tableName = (String) session.getAttribute("tableName");
            String sql = "UPDATE " + tableName + " SET ";
            for (int i = 0; i < columnNames.size(); i++) {
                sql += columnNames.get(i) + "=?";
                if (i != columnNames.size() - 1)
                    sql += ", ";
                else
                    sql += " ";
            }
            sql += "WHERE id=?";
            Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            for (int i = 0; i < columnNames.size(); i++) {
                if (isIntList.get(i) == true)
                    preparedStatement1.setInt(i + 1, Integer.parseInt(request.getParameter(columnNames.get(i))));
                else
                    preparedStatement1.setString(i + 1, request.getParameter(columnNames.get(i)));
            }
            preparedStatement1.setInt(columnNames.size() + 1, id);
            preparedStatement1.executeUpdate();
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet resultSet = preparedStatement2.executeQuery();
            Table selectedTable = new Table(resultSet, tableName);
            request.setAttribute("tableName", selectedTable.getName());
            request.setAttribute("columnNames", selectedTable.getColumnNameList());
            request.setAttribute("resultSet", selectedTable.getElementResultSet());
            request.setAttribute("isIntList", selectedTable.getIsIntegerList());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("list-single-table.jsp");
            connection.close();
            return requestDispatcher;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RequestDispatcher connect (HttpServletRequest request) {
        try {
            String url = "jdbc:postgresql://" + request.getParameter("ip") + ":" + request.getParameter("port") +
                    "/" + request.getParameter("name");
            String user = request.getParameter("user");
            String password = request.getParameter("password");
            connectionFactory = new ConnectionFactory(url, user, password);
            Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT ON (table_name) table_name FROM information_schema.tables WHERE table_schema='public'");
            ResultSet tablesResultSet = preparedStatement.executeQuery();
            Table schemaTable = new Table(tablesResultSet);
            request.setAttribute("columnNames", schemaTable.getColumnNameList());
            request.setAttribute("resultSet", schemaTable.getElementResultSet());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("list-table-in-db.jsp");
            connection.close();
            return requestDispatcher;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RequestDispatcher delete (HttpServletRequest request) {
        try {
            Connection connection = connectionFactory.getConnection();
            String tableName = request.getParameter("tableName");
            int id = Integer.parseInt(request.getParameter("id"));
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM " + tableName + " WHERE id=?");
            preparedStatement1.setInt(1, id);
            preparedStatement1.executeUpdate();
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet resultSet = preparedStatement2.executeQuery();
            Table selectedTable = new Table(resultSet, tableName);
            request.setAttribute("tableName", selectedTable.getName());
            request.setAttribute("columnNames", selectedTable.getColumnNameList());
            request.setAttribute("resultSet", selectedTable.getElementResultSet());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("list-single-table.jsp");
            connection.close();
            return requestDispatcher;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }

    private RequestDispatcher add (HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            List<String> columnNames = (List<String>) session.getAttribute("columnNames");
            List<Boolean> isIntList = (List<Boolean>) session.getAttribute("isIntList");
            String tableName = (String) session.getAttribute("tableName");
            String sql = "INSERT INTO " + tableName + " (";
            for (int i = 0; i < columnNames.size(); i++) {
                sql += columnNames.get(i);
                if (i != columnNames.size() - 1)
                    sql += ", ";
                else
                    sql += ") ";
            }
            sql += "VALUES (";
            for (int i = 0; i < columnNames.size(); i++) {
                    sql += "?";
                if (i != columnNames.size() - 1)
                    sql += ", ";
                else
                    sql += ")";
            }
            System.out.println(sql);
            Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            for (int i = 0; i < columnNames.size(); i++) {
                if (isIntList.get(i) == true)
                    preparedStatement1.setInt(i + 1, Integer.parseInt(request.getParameter(columnNames.get(i))));
                else
                    preparedStatement1.setString(i + 1, request.getParameter(columnNames.get(i)));
            }
            preparedStatement1.executeUpdate();
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet resultSet = preparedStatement2.executeQuery();
            Table selectedTable = new Table(resultSet, tableName);
            request.setAttribute("tableName", selectedTable.getName());
            request.setAttribute("columnNames", selectedTable.getColumnNameList());
            request.setAttribute("resultSet", selectedTable.getElementResultSet());
            request.setAttribute("isIntList", selectedTable.getIsIntegerList());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("list-single-table.jsp");
            connection.close();
            return requestDispatcher;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RequestDispatcher listTable (HttpServletRequest request) {
        try {
            Connection connection = connectionFactory.getConnection();
            String tableName = request.getParameter("name");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet resultSet = preparedStatement.executeQuery();
            Table selectedTable = new Table(resultSet, tableName);
            request.setAttribute("tableName", selectedTable.getName());
            request.setAttribute("columnNames", selectedTable.getColumnNameList());
            request.setAttribute("resultSet", selectedTable.getElementResultSet());
            request.setAttribute("isIntList", selectedTable.getIsIntegerList());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("list-single-table.jsp");
            connection.close();
            return requestDispatcher;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        RequestDispatcher requestDispatcher = null;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("listChosenTable")) {
            requestDispatcher = listTable(request);
        } else if (action.equalsIgnoreCase("delete")) {
            requestDispatcher = delete(request);
        } else if (action.equalsIgnoreCase("update")) {
            requestDispatcher = updateRequest(request);
        }
        requestDispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        RequestDispatcher requestDispatcher = null;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("connection")) {
            requestDispatcher = connect(request);
        }
        else if (action.equalsIgnoreCase("update")) {
            requestDispatcher = updateExecute(request);
        }
        else if (action.equalsIgnoreCase("add")) {
            requestDispatcher = add(request);
        }
        requestDispatcher.forward(request, response);
    }
}
