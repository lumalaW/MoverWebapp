package DataAccess.UserLogin;

import DataAccess.ConnectionConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by william
 * A class to get the password from the database. Method is used in the LoginView class for authentication.
 */
public class UserAuthentication {

    // A method to fetch the password from the database, given a username
    public static String authentication(String username){
        //MySQL statement.
        String query = "SELECT password FROM moverdb.web_app_users where userName = ?";

        // Create connection to database first
        Connection connection;
        PreparedStatement pStatement;
        ResultSet result;
        String password = null;

        try {
            connection = ConnectionConfiguration.getConnection();
            pStatement = connection.prepareStatement(query);
            pStatement.setString(1, username);
            result = pStatement.executeQuery();
            result.next();
            password = result.getString("password");
            pStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return password;
    }

    // A method to return the user names (first and last names)
    public static String getNames(String username){
        //MySQL statement.
        String query_1 = "SELECT firstName FROM moverdb.web_app_users where userName = ?";
        String query_2 = "SELECT lastName FROM moverdb.web_app_users where userName = ?";

        // create connection to database first
        Connection connection;

        PreparedStatement pStatement1;
        PreparedStatement pStatement2;

        ResultSet result1;
        ResultSet result2;

        String firstName;
        String lastName;
        String name = null;

        try {
            connection = ConnectionConfiguration.getConnection();

            pStatement1 = connection.prepareStatement(query_1);
            pStatement2 = connection.prepareStatement(query_2);

            pStatement1.setString(1, username);
            pStatement2.setString(1, username);

            result1 = pStatement1.executeQuery();
            result1.next();
            firstName = result1.getString("firstName");

            result2 = pStatement2.executeQuery();
            result2.next();
            lastName = result2.getString("lastName");

            name = firstName + " " + lastName;

            pStatement1.close();
            pStatement2.close();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }
}
