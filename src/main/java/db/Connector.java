package db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

//import org.apache.tomcat.jdbc.pool.PooledConnection;
//import org.apache.tomcat.jdbc.pool.DataSource;


public class Connector {
    private Preferences root = Preferences.userRoot();
    private Preferences node = Preferences.userNodeForPackage(this.getClass());

    //private DriverManager driverManager;
    private static Connector connector = new Connector();
    private Connector connector2;
    private static Connection connection;
    //private static PooledConnection connection2;
    private static Statement statement;
    private static ResultSet resultSet;
    private static String errorMessage = "";

    private static Logger logger = Logger.getGlobal();

//    public static final String CONFIG_FILE = System.getProperty("catalina.base")
//            + System.getProperty("file.separator")
//            + "conf"
//            + System.getProperty("file.separator")
//            + "db.properties";


//    public static Connector getInstance() {
//        return connector;
//    }

    /*public Connector(Connector connector) {
    }*/

    public static Connector getConnector() {
        return connector;
    }

    public static final String CONFIG_FILE = System.getProperty("catalina.base")
            + System.getProperty("file.separator")
            + "webapps"
            + System.getProperty("file.separator")
            + "db.properties";

    /**Method setConnection setting up connection to databse
     * @param url database connection string
     * @param driver class of database driver
     * @param properties connection properties such as user name, password
     * @return return Connector instance
     */
    public static Connector setConnection(String url, String driver, Properties properties) {
        logger.entering("Connector", "setConnection", new Object[] {url, driver, properties});
        try {
            //Context ctx = (Context) new InitialContext().lookup("java:comp/env");
            //DataSource ds = (DataSource) ctx.lookup("jdbc/partsDB");
            Class.forName(driver);
            //DriverManager.registerDriver();
            connection = DriverManager.getConnection(url, properties);
            //logger.info(ds.toString());
            //connection = ds.getConnection();
        //} catch (ClassNotFoundException | SQLException e) {
        } catch (/*NamingException |*/SQLException | ClassNotFoundException e) {
            logger.info(e.getMessage());
            errorMessage = e.getMessage() + "\n" + e.fillInStackTrace();
            throw new RuntimeException(e.getMessage() + "\n" + e.fillInStackTrace());
        } finally {
            logger.exiting("Connector", "setConnection", new Object[] {connector.toString(), errorMessage});
        }
        return connector;
    }

    public static Connector setConnection() {
        logger.entering("Connector", "setConnection", new Object[] {"!START"});
        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");
            DataSource ds = (DataSource) ctx.lookup("jdbc/partsDB");
            //Class.forName(driver);
            //DriverManager.registerDriver();
            //connection = DriverManager.getConnection(url, properties);

            if (connection == null) {
                logger.info("before get connection " + connection);
                connection = ds.getConnection();
                logger.info("after get connection " + connection);
            }
            //connection = ds.getPooledConnection();
            //connection = ds.getConnection("bis", "employer");
            //logger.info(ds.toString());
            //} catch (ClassNotFoundException | SQLException e) {
        } catch (NamingException | SQLException e) {
            //logger.info(e.getMessage());
            //errorMessage = e.getMessage();
            logger.throwing("ServletParts", "doGet", e.fillInStackTrace());
            throw new RuntimeException(/*e.getMessage()*/e.fillInStackTrace());
        } finally {
            //logger.exiting("Connector", "setConnection", new Object[] {connector, errorMessage});
        }
        logger.exiting("Connector", "setConnection", new Object[] {connection, "END!"});
        return getConnector();
    }

    /** Method getConnection @return Connection instance */
    public static Connection getConnection() {
        logger.exiting("Connector", "getConnection", new Object[] {connection});
        return connection;
    }

    /**Method setStatement setting up statement
     * @return return Connector instance
     */
    public static Connector setStatement() {
        logger.entering("Connector", "setStatement", new Object[] {connection, "!START"});
        try {
            if (statement == null) statement = connection.createStatement();
        } catch (SQLException e) {
            logger.info(e.getErrorCode() + " " + e.getMessage());
            errorMessage = e.getMessage();
            throw new RuntimeException(e.getMessage());
        } finally {
            logger.exiting("Connector", "setStatement", new Object[] {statement});
        }
        return getConnector();
    }

    /** Method getStatement @return Statement instance */
    public static Statement getStatement() {
        logger.exiting("Connector", "getStatement", new Object[] {statement});
        return statement;
    }

    /**Method setResultSet setting up resultSet
     * @param sql Query data from database
     * @return return Connector instance
     */
    public static Connector setResultSet(String sql) {
        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            logger.info(e.getErrorCode() + " " + e.getMessage());
            errorMessage = e.getMessage();
            throw new RuntimeException(e.getMessage());
        } finally {
            logger.exiting("Connector", "getStatement", new Object[] {sql, resultSet});
        }
        return getConnector();
    }

    /** Method getResultSet @return ResultSet instance */
    public static ResultSet getResultSet() { return resultSet; }

    public static void close() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            logger.info(e.getErrorCode() + " " + e.getMessage());
            errorMessage = e.getMessage();
            logger.throwing("Connector", "getResultSet", new Throwable("1 " + e.getErrorCode() + " " + e.getMessage()));
            throw new RuntimeException(e.getMessage());
        } finally {
            logger.exiting("Connector", "close", new Object[] {statement, connection});
        }
    }

    public static String printQuery() {
        String out = "";
        try {
            if (getResultSet() != null) {
                for (int i = 1; i <= getResultSet().getMetaData().getColumnCount(); i++) {
                    out += getResultSet().getMetaData().getColumnName(i);

                    if (i != getResultSet().getMetaData().getColumnCount()) out += "; ";
                    else out += "\n";
                }

                while (getResultSet().next()) {
                    for (int i = 1; i <= getResultSet().getMetaData().getColumnCount(); i++) {
                        out += getResultSet().getString(i);

                        if (i != getResultSet().getMetaData().getColumnCount()) out += "; ";
                        else out += "\n";
                    }
                }
            } else {
                throw new RuntimeException("[printQuery] ERRORS!!! " + "ResultSet not found!");
            }
        } catch (SQLException qe) {
            throw new RuntimeException("[printQuery] ERRORS!!!!!! " + qe.getErrorCode() + " " + qe.getMessage());
        }
        return out;
    }

    /** Method getResultSet @return ResultSet instance */
    public static String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "Connector.Hash{" + getConnector().hashCode() + '}';
    }

    //    public String printPreferences() {
////        String keys = "";
////        String names = "";
//        String res = "";
//        try {
//            res += "ROOT names: {";
//            for (String s : root.childrenNames()) res += s + ",";
//            res += "}; keys: {";
//            for (String s : root.keys()) res += s + ",";
//            res += "};";
//
//            res += " NODE names: {";
//            for (String s : node.childrenNames()) res += s + ",";
//            res += "}; keys: {";
//            for (String s : node.keys()) res += s + ",";
//            res += "};";
//        } catch (BackingStoreException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }
}
