package Application;

/**
 * Created by william.
 * A java POJO to represent a car accident...extends the accident class.
 */
public class CarAccident extends Accident {

    public CarAccident(AccidentTime accidentTime, double latitude, double longitude, double acceleration) {
        super(accidentTime, latitude, longitude, acceleration);
    }

}
