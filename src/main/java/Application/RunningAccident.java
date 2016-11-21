package Application;

/**
 * Created by william.
 * A java POJO to represent a running accident...extends the accident class
 */

public class RunningAccident extends Accident {
    public RunningAccident(AccidentTime accidentTime, double latitude, double longitude, double acceleration) {
        super(accidentTime, latitude, longitude, 0); // no acceleration
    }
}
