package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

//  !!!WARNING: MAKE SURE THE JDBC CONNECTOR JAR IS IN THE BUILD PATH!!!
public class DBMS {
    //  Username and password for MySQL workbench connection
    private static final String usernameSQL = "CassCo";
    private static final String passwordSQL = "Password.";

    // Objects to be used for database access
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet result = null;

    //  Connect to the "UserDatabase" database scheme,
    //  Default port is 3306 (jdbc:mysql://<ip address>:<port>/<schema>)
    private final String schema = "sys";
    private final String table = "users";
    private final String url = "jdbc:mysql://127.0.0.1:3306/" + schema;

    //  Constructor for DBMS
    public DBMS (String username, String password) {
        try {
            // Make connection to the database
            connection = DriverManager.getConnection(url, username, password);
            // These will send queries to the database
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT VERSION()");
            if (result.next()) {
                System.out.println("MySQL version: " + result.getString(1) + "\n=====================\n");
            }
        } catch (SQLException ex) {
            // Handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    //  Accesses Perform Operations in MySQL Database
    public void accessDatabase() {
        try {
            // Query will return a ResultSet then get and print all records in the table
            System.out.println("Original Contents");
            String SQL = "SELECT * FROM " + table + ";";
            result = statement.executeQuery(SQL);
            printResultSet(result);

            // Insert new user into the table
            System.out.println("Inserted Contents");
            String username = "'testUsername'"; // SQL expects strings to be single quoted
            String password = "'testUsername'";
            int connected = 0;
            int loggedIn = 0;
            int strikes = 0;
            statement.executeUpdate("INSERT INTO " + table + " VALUE(" + username + ", " + password + ", " + connected + ", " + loggedIn + "," +  strikes + ");");

            // Get and print all records in the table
            result = statement.executeQuery("SELECT * FROM " + table + ";");
            printResultSet(result);

            // Modify a record in the table and get the result set of records
            result = statement.executeQuery("SELECT * FROM " + table + " WHERE Username=" + username + ";");
            result.next();
            strikes = Integer.parseInt(result.getString(5));
            System.out.println("Updated Contents");
            statement.executeUpdate("UPDATE " + table + " SET strikes=" + (strikes + 1) + " WHERE Username=" + username + ";");

            // Get and print all records in the table
            result = statement.executeQuery("SELECT * FROM " + table + " WHERE Username=" + username + ";");
            printResultSet(result);

            //  Delete Record
            result = statement.executeQuery("DELETE * FROM " + table + " WHERE Username=" + username + ";");
            result = statement.executeQuery("SELECT * FROM " + table + " WHERE Username=" + username + ";");
            System.out.println("should be blank");
            printResultSet(result);
        } catch (SQLException ex) {
            // Handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    //  Print Result Set from SQL table
    public void printResultSet(ResultSet resultSet) {
        try {
            // Metadata contains how many columns in the data
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int numberOfColumns = resultSetMetaData.getColumnCount();
            System.out.println("columns: " + numberOfColumns);

            // Loop through the ResultSet one row at a time
            while (resultSet.next()) {
                // Loop through the columns of the ResultSet
                for (int i = 1; i <= numberOfColumns; ++i) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    // Getter methods to access private static variables
    public static String getUsernameSQL() {
        return usernameSQL;
    }

    public static String getPasswordSQL() {
        return passwordSQL;
    }

    // Main test
    public static void LoadUserDatabase() {
        // Make the JDBC connection (Java Database Connectivity)
        DBMS dbc = new DBMS(getUsernameSQL(), getPasswordSQL());
        // Perform some SQL operations
        dbc.accessDatabase();
    }
}
//getterss
