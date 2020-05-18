package utilities;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class related to DataBase actions.
 * Includes utilities to create DB connection, destroy DB connection,
 * record outcomes of given queries, etc.
 */
public class DBUtils {
	public static final String SCHEMA;
	static {
		SCHEMA = ConfigReader.getProperty("db_schema");
	}

    /** private fields necessary for JDBC connection */
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    
    /**Called by other utilities within and without the class
     * Executes the query given as String
     * Executes 'SELECT' queries only.
     * @param query given as String
     */
    public static void executeQuery(String query) {
        try {
            statement = connection.createStatement(
            		ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**Called by other utilities within and without the class
     * Executes the query given as String
     * Executes 'INSERT', 'UPDATE', 'DELETE' queries only.
     * @param query given as String
     */
    public static void executeUpdate(String query) {
        try {
            statement = connection.createStatement(
            		ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**Creates DB connectivity.
     * Information related to DB host, username and password is retrieved
     * from Run configurations - environment variables
     */
    public static void createDBConnection() {
        String url = "jdbc:mysql://" + System.getenv("db_server")
                + ":" + System.getenv("db_port")
                + "?useUnicode=true"
                + "&useJDBCCompliantTimezoneShift=true"
                + "&useLegacyDatetimeCode=false"
                + "&serverTimezone=Europe/Moscow"
                + "&autoReconnect=true"
                + "&sessionVariables="
                	+ "sql_mode='STRICT_TRANS_TABLES,"
                		+ "NO_AUTO_CREATE_USER,"
                		+ "NO_ENGINE_SUBSTITUTION,"
                		+ "PAD_CHAR_TO_FULL_LENGTH',"
                		+ "interactive_timeout=2147483,"
                		+ "wait_timeout=2147483";
        String user = System.getenv("db_username");
        String password = System.getenv("db_password");

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**Closes ResultSet, Statement and Connection instances
     * if they are not null already.
     */
    public static void destroyDBConnection() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**Given a query, returns the List of column headers
     * @param query given as String
     * @return List<String> of column headers per ResultSetMetaData
     */
    public static List<String> getColumnNames(String query) {
        executeQuery(query);
        List<String> columns = new ArrayList<>();
        ResultSetMetaData rsmd;

        try {
            rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++)
                columns.add(rsmd.getColumnName(i));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columns;
    }


    /**Given catalog name as a String, and a table name within the catalog as a String,
     * returns the List of column headers.
     * Internally calls the getColumnData(String query, String column) method since
     * the query accesses the table's metadata information where column data
     * under 'field' column is collected and returned.
     * @param catalogName as a String (e.g. aum_stage, aum_datawarehouse)
     * @param tableName as a String
     * @return List<String> of column headers
     */
    public static List<String> getColumnNames(String catalogName, String tableName) {
        return getColumnData("show fields from " + catalogName + "." + tableName + ";", "field")
                .stream().map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());
    }


    /**Given a query has already been run against the DB,
     * returns the count of rows in the ResultSet
     * @return an int of row count or -1 if caught an exception
     */
    public static int getRowCount() {
        int rowCount = -1;
        try {
            resultSet.last();
            rowCount = resultSet.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowCount;
    }


    /**Given a query to execute as a String
     * calls the no-args method and returns the number of rows
     * @param query as a String
     * @return number of rows of the result set
     */
    public static int getRowCount(String query) {
        executeQuery(query);
        return getRowCount();
    }


    /**Given a query and a column name (header), returns List of data
     * that belongs to that column only.
     * @param query given as String
     * @param column given as String, exact match
     * @return List<Object> of data within rows of the column
     */
    public static List<Object> getColumnData(String query, String column) {
        executeQuery(query);
        List<Object> rowList = new ArrayList<>();
        ResultSetMetaData rsmd;

        try {
            rsmd = resultSet.getMetaData();

            while (resultSet.next())
                rowList.add(resultSet.getObject(column));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowList;
    }
    

    /**Given query as a String and a column name as a String, calls getColumnData method
     * and internally converts the return to List<String> utilizing Java stream.
     * @param query as a String
     * @param column as a String
     * @return List<String> of requested column data
     */
    public static List<String> getColumnDataStringList(String query, String column) {
        return getColumnData(query, column).stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());
    }
    

    /**Given a catalog name as a String, returns list of table names within the catalog
     * @param catalog as a String (e.g. aum_stage, aum_datawarehouse)
     * @return List<String> of table names
     */
    public static List<String> getTableNames(String catalog) {
        List<String> tableNames = new ArrayList<>();

        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            resultSet = dbmd.getTables(catalog, null, "%", null);

            while (resultSet.next())
                tableNames.add(resultSet.getString("TABLE_NAME"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableNames;
    }
    

    /**Given a query as a String, returns the data within the first row.
     * @param query as a String
     * @return List<Object> of the contents of the first row
     */
    public static List<Object> getFirstRowData(String query) {
        return getQueryResultList(query).get(0);
    }
    

    /**Given a query as a String, returns the data of the whole query result
     * as a List of List of Objects in which case the inner List of Objects
     * represents the data in each column of the result.
     * @param query as a String
     * @return List<List<Object>> where List<Object> is the List of columns
     *         of a given row
     */
    public static List<List<Object>> getQueryResultList(String query) {
        executeQuery(query);
        List<List<Object>> rowList = new ArrayList<>();
        ResultSetMetaData rsmd;

        try {
            rsmd = resultSet.getMetaData();

            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();

                for (int i = 1; i <= rsmd.getColumnCount(); i++)
                    row.add(resultSet.getObject(i));

                rowList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowList;
    }
    

    /**Given a query as a String, returns a List of Maps where each Map represents
     * key (column/header) - value (data in the row related to the given column/header)
     * relationship.
     * @param query as a String
     * @return List<Map<String, Object>> where each Map is a single row with headers
     */
    public static List<Map<String, Object>> getQueryResultMap(String query) {
        executeQuery(query);
        List<Map<String, Object>> rowList = new ArrayList<>();
        ResultSetMetaData rsmd;

        try {
            rsmd = resultSet.getMetaData();

            while (resultSet.next()) {
                Map<String, Object> colNameValueMap = new HashMap<>();

                for (int i = 1; i <= rsmd.getColumnCount(); i++)
                    colNameValueMap.put(rsmd.getColumnName(i), resultSet.getObject(i));

                rowList.add(colNameValueMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowList;
    }


    /**Given a query as a String, returns the data within the first column of the first row
     * @param query as a String
     * @return the data within the first cell
     */
    public static Object getFirstCellData(String query) {
        return getQueryResultList(query).get(0).get(0);
    }
    

    /**Given a query as a String, returns the data within the first row as a Map
     * @param query as a String
     * @return Map<String, Object> of the first row
     */
    public static Map<String, Object> getFirstRowMap(String query) {
        return getQueryResultMap(query).get(0);
    }
}