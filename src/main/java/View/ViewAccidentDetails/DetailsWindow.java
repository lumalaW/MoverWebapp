package View.ViewAccidentDetails;

import com.vaadin.ui.*;

/**
 * Created by william
 * A class to display the details of the accidents in the table
 */
public class DetailsWindow extends Window{

    // Initialized with the details as parameters
    public DetailsWindow(String datetime, String lat, String lon, String acceleration){
        super("Accident details");

        setHeight("55%");
        setWidth("50%");

        setPosition(400,150);

        VerticalLayout content = new VerticalLayout();
        Panel details = new Panel();
        content.addComponent(details);

        // Create a layout inside the panel
        final FormLayout form = new FormLayout();

        // Have some margin around it.
        form.setMargin(true);

        details.setContent(form);

        Label timer = new Label("Time : " + datetime);
        Label location = new Label("Latitude : " + lat);
        Label location1 = new Label("Longitude : " + lon);
        Label acc = new Label("Acceleration at impact : " + acceleration);

        form.addComponent(timer);
        form.addComponent(location);
        form.addComponent(location1);
        form.addComponent(acc);

        content.setMargin(true);
        setContent(content);

        Button ok = new Button("Close Window");
        ok.addClickListener((Button.ClickListener) event -> {
            close(); // Close the sub-window
        });

    }
}
