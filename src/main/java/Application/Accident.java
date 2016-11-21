package Application;

/**
 * Created by william.
 * A java POJO to represent an accident with get and set methods
 */


public class Accident {

    private AccidentTime accidentTime;
    private double latitude;
    private double longitude;
    private double acceleration;

    public Accident(AccidentTime accidentTime, double latitude, double longitude, double acceleration){
        this.accidentTime = accidentTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.acceleration = acceleration;
    }

    public AccidentTime getAccidentTime() {
        return accidentTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAcceleration() {
        return acceleration;
    }


}
