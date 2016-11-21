package DataAccess;

import Application.AccidentTime;
import Application.CarAccident;
import Application.RunningAccident;


import java.sql.*;
import java.util.ArrayList;

/**
 * Created by william
 * A java class ti read data from the database and store it arrayLists
 */
public class FetchData {

    public static ArrayList<String> getAdminUsers(){
        ArrayList<String> users = new ArrayList<>();

        //MySQL statement.
        String queryAdminUsers = "SELECT username FROM moverdb.web_app_users";

        // Create connection to database first
        Connection connection;
        Statement statement;
        ResultSet result;

        try {
            connection = ConnectionConfiguration.getConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(queryAdminUsers);
            while(result.next()){
                //Retrieve by column name
                String uName = result.getString("userName");
                users.add(uName);
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static ArrayList<CarAccident> getCAccidents(){
        ArrayList<CarAccident> carAccidents = new ArrayList<>();

        //MySQL statement.
        String queryCarAccidentData = "SELECT accidentTime, latitude, longitude, acceleration FROM moverdb.car_accidents";

        // create connection to database first
        Connection connection;
        Statement statement;
        ResultSet result;

        try {
            connection = ConnectionConfiguration.getConnection();

            statement = connection.createStatement();
            result = statement.executeQuery(queryCarAccidentData);
            while(result.next()){
                //Retrieve by column name
                String timeOfAccident = result.getString("accidentTime");
                double lat  = result.getDouble("latitude");
                double lon  = result.getDouble("longitude");
                double acceleration = result.getDouble("acceleration");

                // Accident timeOfAccident
                String [] accidentTime = timeOfAccident.split("\\s+");

                String date = accidentTime[0]; System.out.println("The date"+ date);
                String [] dateDetails = date.split("-");

                int year = Integer.parseInt(dateDetails[0]);
                int month = Integer.parseInt(dateDetails[1]);
                int day = Integer.parseInt(dateDetails[2]);


                String time = accidentTime[1]; System.out.println("The time " + time);
                String h = time.substring(0,2); int hour = Integer.parseInt(h);
                String m = time.substring(3,5); int minutes = Integer.parseInt(m);
                String s = time.substring(6);  float seconds = Float.parseFloat(s);

                AccidentTime accidentTime1 = new AccidentTime(year,month,day,hour,minutes,seconds);
                CarAccident accident = new CarAccident(accidentTime1, lat, lon, acceleration);

                //Display values
                System.out.print("Time: " + time);
                System.out.print(", Lat: " + lat);
                System.out.print(", Lon: " + lon);
                System.out.println(", Acceleration: " + acceleration);

                carAccidents.add(accident);

            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carAccidents;
    }

    public static ArrayList<RunningAccident> getRAccidents(){
        ArrayList<RunningAccident> runningAccidents = new ArrayList<>();

        //MySQL statement.
        String queryRunningAccidentData = "SELECT accidentTime, ST_X(location), ST_Y(location) FROM moverdb.simpleRunningAccidents";


        // create connection to database first
        Connection connection;
        Statement statement;
        ResultSet result;

        try {
            connection = ConnectionConfiguration.getConnection();

            statement = connection.createStatement();
            result = statement.executeQuery(queryRunningAccidentData);
            while(result.next()){
                //Retrieve by column name
                String timeOfAccident = result.getString("accidentTime");

                Object latitude = result.getObject(2);
                double lat = new Double(latitude.toString());
                Object longitude = result.getObject(3);
                double lon  = new Double(longitude.toString());


                // Accident timeOfAccident
                String [] accidentTime = timeOfAccident.split("\\s+");

                String date = accidentTime[0]; System.out.println("The date"+ date);
                String [] dateDetails = date.split("-");

                int year = Integer.parseInt(dateDetails[0]);
                int month = Integer.parseInt(dateDetails[1]);
                int day = Integer.parseInt(dateDetails[2]);


                String time = accidentTime[1]; System.out.println("The time " + time);
                String h = time.substring(0,2); int hour = Integer.parseInt(h);
                String m = time.substring(3,5); int minutes = Integer.parseInt(m);
                String s = time.substring(6);  float seconds = Float.parseFloat(s);

                AccidentTime accidentTime1 = new AccidentTime(year,month,day,hour,minutes,seconds);
                RunningAccident accident = new RunningAccident(accidentTime1, lat, lon, 0);

                //Display values
                System.out.print("Time: " + time);
                System.out.print(", Lat: " + lat);
                System.out.print(", Lon: " + lon);


                runningAccidents.add(accident);

            }

            result.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return runningAccidents;
    }
}
