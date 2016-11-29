import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;

/**
 * Created by Dmitry on 26.11.2016.
 */
public class Table {
    private String name = "";
    private Set<Column> columnSet = new HashSet<>();
    private ResultSet resultSet = null;
    private boolean hasForeignKey = false;

    public Table(ResultSet resultSet) {
        try {
            this.resultSet = resultSet;
            ResultSetMetaData resultSetMetaData = this.resultSet.getMetaData();
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                String dataType = resultSetMetaData.getColumnClassName(i+1);
                if (dataType.equals("java.lang.Integer"))
                    this.columnSet.add(new Column(i, resultSetMetaData.getColumnName(i+1), true));
                else
                    this.columnSet.add(new Column(i, resultSetMetaData.getColumnName(i+1), false));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Table(ResultSet resultSet, String name) {
        try {
            this.name = name;
            this.resultSet = resultSet;
            ResultSetMetaData resultSetMetaData = this.resultSet.getMetaData();
            for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                String dataType = resultSetMetaData.getColumnClassName(i+1);
                if (dataType.equals("java.lang.Integer"))
                    this.columnSet.add(new Column(i, resultSetMetaData.getColumnName(i+1), true));
                else
                    this.columnSet.add(new Column(i, resultSetMetaData.getColumnName(i+1), false));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void getForeignKeys(String tableName, Connection connection) {
        try {
            String sql = "SELECT tc.constraint_name, tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name," +
                    " ccu.column_name AS foreign_column_name FROM information_schema.table_constraints AS tc" +
                    " JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name" +
                    " JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name" +
                    " WHERE constraint_type = 'FOREIGN KEY' AND tc.table_name=(?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tableName);
            ResultSet fkResultSet = preparedStatement.executeQuery();
            while (fkResultSet.next()) {
                if (tableName.equals(fkResultSet.getString("table_name"))) {
                    this.hasForeignKey = true;
                    Iterator<Column> columnSetIterator = columnSet.iterator();
                    while (columnSetIterator.hasNext()) {
                        Column tempColumn = columnSetIterator.next();
                        if (tempColumn.getName().equals(fkResultSet.getString("column_name"))) {
                            tempColumn.setHasForeignKey(true);
                            tempColumn.setForeignTableName(fkResultSet.getString("foreign_table_name"));
                            tempColumn.setForeignColumnName(fkResultSet.getString("foreign_column_name"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getColumnNameList() {
        Iterator<Column> columnSetIterator = columnSet.iterator();
        String[] columnNameArray = new String[columnSet.size()];
        while (columnSetIterator.hasNext()) {
            Column tempColumn = columnSetIterator.next();
            columnNameArray[tempColumn.getOrdinal()] = tempColumn.getName();
        }
        List<String> columnNameList = Arrays.asList(columnNameArray);
        return columnNameList;
    }

    public List<Boolean> getIsIntegerList() {
        Iterator<Column> columnSetIterator = columnSet.iterator();
        Boolean[] isIntArray = new Boolean[columnSet.size()];
        while (columnSetIterator.hasNext()) {
            Column tempColumn = columnSetIterator.next();
            isIntArray[tempColumn.getOrdinal()] = tempColumn.isInt();
        }
        List<Boolean> isIntList = Arrays.asList(isIntArray);
        return isIntList;
    }

    public ResultSet getElementResultSet() {
        return this.resultSet;
    }
}
