// -- download MySQL from: http://dev.mysql.com/downloads/
//    Community Server version
// -- Installation instructions are here: http://dev.mysql.com/doc/refman/5.7/en/installing.html
// -- open MySQL Workbench to see the contents of the database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// -- MAKE SURE THE JDBC CONNECTOR JAR IS IN THE BUILD PATH
//    workspace -> properties -> Java Build Path -> Libraries -> Add External JARs...
//    "C:\Program Files\MySQL\mysql-connector-j-8.2.0\mysql-connector-java-8.0.27.jar"
public class DBMS {

    // -- objects to be used for database access
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rset = null;

    // -- connect to the world database schema
    // -- this is the connector to the database, default port is 3306

    //    jdbc:mysql://<ip address>:<port>/<schema>
    private String schema = "UserDatabase";
    private String url = "jdbc:mysql://127.0.0.1:3306/" + schema;

    // -- this is the username/password, created during installation and in MySQL Workbench
    //    When you add a user make sure you give them the appropriate Administrative Roles
    //    (DBA sets all which works fine)
    //    private static String username = "<<username>>";
    //    private static String password = "<<password>>";
    public DBMS (String username, String password) {
        try {
            // -- make the connection to the database
            conn = DriverManager.getConnection(url, username, password);

            // -- These will be used to send queries to the database
            stmt = conn.createStatement();
            rset = stmt.executeQuery("SELECT VERSION()");

            if (rset.next()) {
                System.out.println("MySQL version: " + rset.getString(1) + "\n=====================\n");
            }
        }
        catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public void accessDatabase() {
        try {

            // -- a query will return a ResultSet
            //    get and print all records in the table
            System.out.println("Original Contents");
            rset = stmt.executeQuery("SELECT * FROM " + schema + ";");
            printResultSet(rset);

            // -- insert a record into the table
            System.out.println("Inserted Contents");
            String username = "testUsername"; // -- SQL expects strings to be single quoted
            String password = "testUsername";
            int loggedIn = 0;
            int connected = 0;
            int strikes = 0;
            stmt.executeUpdate("INSERT INTO " + schema + " VALUE(" + username + ", " + password + ", " + loggedIn + ", " + connected + "," +  strikes + ");");

            // -- get and print all records in the table
            rset = stmt.executeQuery("SELECT * FROM " + schema + ";");
            printResultSet(rset);

            // -- modify a record in the table
            //    get the result set of records
            rset = stmt.executeQuery("SELECT * FROM " + schema + " WHERE Username=" + username + ";");
            //    move the iterator to the record, if there is no record this will throw an exception
            rset.next();
            //    get the population column and convert it to int
            strikes = Integer.parseInt(rset.getString(5));
            System.out.println("Updated Contents");
            stmt.executeUpdate("UPDATE " + schema + " SET strikes=" + (strikes + 1) + " WHERE Username=" + username + ";");

            // -- get and print all records in the table
            rset = stmt.executeQuery("SELECT * FROM " + schema + " WHERE Username=" + username + ";");
            printResultSet(rset);
            // -- get and print all records in the table
            rset = stmt.executeQuery("SELECT * FROM " + schema + " WHERE Username=" + username + ";");
            System.out.println("should be blank");
            printResultSet(rset);

        }
        catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public void printResultSet(ResultSet rset)
    {
        try {
            // -- the metadata tells us how many columns in the data
            ResultSetMetaData rsmd = rset.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            System.out.println("columns: " + numberOfColumns);

            // -- loop through the ResultSet one row at a time
            //    Note that the ResultSet starts at index 1
            while (rset.next()) {
                // -- loop through the columns of the ResultSet
                for (int i = 1; i < numberOfColumns; ++i) {
                    System.out.print(rset.getString(i) + "\t");
                }
                System.out.println(rset.getString(numberOfColumns));
            }
        }
        catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }


    public static void main(String[] args) {

        Scanner kb = new Scanner(System.in);
        System.out.print("MySQL username: ");
        String username = kb.next();
        System.out.print("MySQL password: ");
        String password = kb.next();

        // -- make the JDBC connection
        DBMS dbc = new DBMS(username, password);
        // -- perform some SQL operations
        dbc.accessDatabase();
    }

}
