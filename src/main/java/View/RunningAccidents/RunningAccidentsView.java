package View.RunningAccidents;

import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import Application.RunningAccident;
import Application.ReadData;
import View.ViewAccidentDetails.DetailsWindow;
import View.ViewAccidentDetails.MapWindow;

import java.util.ArrayList;

/**
 * Created by william
 * A View to display all the running accidents in a table
 */
public class RunningAccidentsView extends VerticalLayout implements View{

    // Variables for the table and arrayList to hold the accident data
    private final Table table;
    private final ReadData readData;
    private ArrayList<RunningAccident> runningAccidents = new ArrayList<>();

    // Initialize the arrayList
    public RunningAccidentsView() {

        setMargin(true);
        setSpacing(true);

        readData = new ReadData();
        runningAccidents = readData.getRunningAccidents();

        addComponent(buildHeader());
        table = buildTable();
        addComponent(table);
        setExpandRatio(table, 1);
    }

    // Method to build header to hold title
    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setSpacing(true);

        double ratio = 1100.0 / 1366.0;
        int widthToUse = (int) (ratio * UI.getCurrent().getPage().getBrowserWindowWidth());
        String width = "" + widthToUse; // get the width depending on screen size

        header.setWidth(width + "px");
        Responsive.makeResponsive(header);

        Label title = new Label("Latest Running Accidents");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        return header;
    }

    // Method to build table and add data into the table
    private Table buildTable() {
        final Table table = new Table();

        table.setSizeFull();
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);

        table.addContainerProperty("Time of accident", String.class, null);
        table.addContainerProperty("GPS (Lat)", Double.class, null);
        table.addContainerProperty("GPS (Long)", Double.class, null);


        for(int i = 0; i < runningAccidents.size(); i++){
            table.addItem(new Object[]{ runningAccidents.get(i).getAccidentTime().output(),
                    runningAccidents.get(i).getLatitude(), runningAccidents.get(i).getLongitude()} , new Integer(i));
        }

        table.setPageLength(table.size());
        table.addActionHandler(new RunningAccidentsView.AccidentHandler());
        return table;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    // Private inner class to implement the action method to get accident details
    private class AccidentHandler implements Action.Handler {

        // Two actions...view the accident details and view location on Google Maps
        private final Action location = new Action("View Location");
        private final Action details = new Action("Accident Details");

        @Override
        public Action[] getActions(Object o, Object o1) {
            return new Action[]{location, details};
        }

        // Method implementation of the two actions
        @Override
        public void handleAction(Action action, Object sender, Object target) {

            Item item = ((Table) sender).getItem(target);
            double lat = (double) item.getItemProperty("GPS (Lat)").getValue();
            double lng = (double) item.getItemProperty("GPS (Long)").getValue();

            if (action == location) {

                MapWindow window = new MapWindow(lat, lng, "VAADIN/themes/mytheme/blue.png");
                UI.getCurrent().addWindow(window);
            } else if (action == details) {

                String dateTime = item.getItemProperty("Time of accident").getValue().toString();
                String location = item.getItemProperty("GPS (Lat)").getValue().toString();
                String location1 = item.getItemProperty("GPS (Long)").getValue().toString();

                DetailsWindow detailsWindow = new DetailsWindow(dateTime, location, location1, "N/A");
                UI.getCurrent().addWindow(detailsWindow);
            }
        }
    }
}
