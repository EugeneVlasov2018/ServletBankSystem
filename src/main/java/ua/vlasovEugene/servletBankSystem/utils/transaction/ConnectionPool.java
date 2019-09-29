package ua.vlasovEugene.servletBankSystem.utils.transaction;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A static class that retrieves connections from the Tomcat connection pool
 */
public class ConnectionPool {
    /**
     * DataSource of Tomcat connection pool
     */
    private static DataSource dataSource;
    private static final String JNDI_LOOKUP_SERVICE = "java:/comp/env/jdbc/epamproject";

    /**
     * A static field that creates a Data Source on the settings from the files
     * "webapp / META-INF / context.xml" and "webapp / web.xml"
     */
    static{
        try {
            Context context = new InitialContext();
            Object lookup = context.lookup(JNDI_LOOKUP_SERVICE);
            if (lookup!=null){
                dataSource = (DataSource) lookup;
            } else {
                throw new RuntimeException("Datasource not found!!!");
            }
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method issuing connection from connection pool of Tomkat
     * @return Connection from DataSource
     * @throws SQLException if something goes wrong this exception will be thrown
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
