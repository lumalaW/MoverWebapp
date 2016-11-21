package View.ViewAccidentDetails;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Created by william
 * A class to display the location of an accident in the table on Google Maps
 */
public class MapWindow extends Window {

    // Uses the GPS co-ordinates in the table
    double lat;
    double lng;
    String icon;

    public MapWindow(double lat, double lng, String icon){
        super("Location of the accident");
        center();

        setHeight("70%");
        setWidth("60%");

        this.lat = lat;
        this.lng = lng;
        this.icon = icon;

        final VerticalLayout content = new VerticalLayout();


        GoogleMap googleMap;
        String apiKey = "AIzaSyAEnypV4PHMFOXEvKVNIKQqSsyjegGBDog";
        googleMap = new GoogleMap(apiKey, null, null);
        googleMap.setCenter(new LatLon(lat,lng));
        googleMap.addMarker("Accident", new LatLon(lat,lng), false, icon);
        googleMap.setZoom(7);
        int height = 380;
        googleMap.setHeight(height, Unit.PIXELS);
        googleMap.setWidth("100%");

        content.addComponent(googleMap);
        content.setMargin(true);
        setContent(content);

    }
}
