package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by william
 * A class to connect to the database.
 * Used when fetching data or authenticating users
 */
public class ConnectionConfiguration {

    private static String url;
    private static String username;
    private static String password;

    public static Connection getConnection(){

        url = "jdbc:mysql://localhost:3306/moverdb";
        username = "root";
        password = "should magnet did parent";
        //password = "moverdb";

        Connection connection = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url,username,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
