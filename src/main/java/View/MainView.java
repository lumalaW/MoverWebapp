package View;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import Application.ReadData;
import Application.Accident;
import Application.CarAccident;
import Application.RunningAccident;
import View.CarAccidents.CarAccidentView;
import View.Dashboard.DashboardView;
import View.DataVisualization.DataVisualizationView;
import View.Login.LoginView;
import View.RunningAccidents.RunningAccidentsView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;


/**
 * Created by william
 * The main view.
 * Contains two tabs; one for the map and the other for the view that a user has selected.
 */

public class MainView extends CustomComponent implements View {


    private MenuLayout root = new MenuLayout();
    private ComponentContainer viewDisplay = root.getContentContainer();
    private CssLayout menu = new CssLayout();
    private CssLayout menuItemsLayout = new CssLayout();

    private Navigator navigator;
    private LinkedHashMap<String, String> menuItems = new LinkedHashMap<>();

    private ReadData readData;
    private ArrayList<Accident> allAccidentsList = new ArrayList<>();

    /**
     * An api-key distributed by Google for the Google Maps
     */
    String apiKey = "AIzaSyAEnypV4PHMFOXEvKVNIKQqSsyjegGBDog";
    GoogleMap googleMap = new GoogleMap(apiKey, null, null);

    String name;


    public MainView(String name){

        //User name
        this.name = name;
        System.out.println("The name is:" + name);

        CssLayout rootLayout = new CssLayout();
        rootLayout.setSizeFull();
        setCompositionRoot(rootLayout);

        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        rootLayout.addComponent(tabs);

        tabs.addTab(root, "Dashboard");

        root.setWidth("100%");
        root.addMenu(buildMenu());


        //  Navigator
        navigator = new Navigator(UI.getCurrent(), viewDisplay);

        navigator.addView("dashboard", DashboardView.class);
        navigator.addView("car_accidents", CarAccidentView.class);
        navigator.addView("running_accidents", RunningAccidentsView.class);
        navigator.addView("stats", DataVisualizationView.class);

        String f = Page.getCurrent().getUriFragment();
        if(f == null || f.equals("")){
            navigator.navigateTo("dashboard");
        }
        navigator.setErrorView(DashboardView.class);

        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent viewChangeEvent) {

                for(Iterator<Component> it = menuItemsLayout.iterator(); it.hasNext();){
                    it.next().removeStyleName("selected");
                }

                for(java.util.Map.Entry<String,String> item : menuItems.entrySet()){
                    if(viewChangeEvent.getViewName().equals(item.getKey()) ){
                        for(Iterator<Component> it = menuItemsLayout.iterator(); it.hasNext();){
                            Component c = it.next();

                            if(c.getCaption() != null && c.getCaption().startsWith(item.getValue())){
                                c.addStyleName("selected");
                                break;
                            }
                        }
                        break;
                    }
                }
                menu.removeStyleName("valo-menu-visible");
            }
        });

        readData = new ReadData();
        allAccidentsList = readData.getAllAccidents();

        //Add the Google maps tab to the tab-sheet
        tabs.addTab(buildMap(), "Google Maps");

    }

    // A method to build the menu for the main page
    CssLayout buildMenu(){
        menuItems.put("dashboard", "Dashboard");
        menuItems.put("car_accidents", "Car Accidents");
        menuItems.put("running_accidents", "Running Accidents");
        menuItems.put("stats", "Data Visualization");

        final MenuBar menuBar = new MenuBar();
        menuBar.addStyleName("user-menu");
        ThemeResource resource = new ThemeResource("profile-pic-300px.jpg");
        MenuBar.MenuItem userDetails = menuBar.addItem("", resource, null);
        userDetails.setText(name);
        userDetails.addItem("Edit Profile", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {

            }
        });

        userDetails.addSeparator();
        userDetails.addItem("Sign out", new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getSession().close();
                getUI().getPage().setLocation(String.valueOf(new LoginView()));
            }
        });

        menu.addComponent(menuBar);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(menuItemsLayout);

        for(final java.util.Map.Entry item : menuItems.entrySet()) {
            Button b = new Button((String) item.getValue(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    navigator.navigateTo((String) item.getKey());
                }
            });
            b.setHtmlContentAllowed(true);
            b.setPrimaryStyleName(ValoTheme.MENU_ITEM);
            menuItemsLayout.addComponent(b);
        }
        return menu;
    }

    private Component buildMap(){

        final VerticalLayout mapContent = new VerticalLayout();
        mapContent.setSpacing(true);
        mapContent.setSizeFull();

        googleMap.setCenter(new LatLon(-27.637251, 24.232337));
        googleMap.setZoom(7);

        double ratio = 79.0/100.0;
        int height = (int) (ratio * (UI.getCurrent().getPage().getBrowserWindowHeight()));

        System.out.println("The height of the map " + height);

        googleMap.setHeight(height, Unit.PIXELS);
        googleMap.setWidth("100%");

        getAccidentData(); // fill the arrayList with all the data
        displayData(googleMap, allAccidentsList);

        mapContent.addComponent(googleMap);
        mapContent.setSpacing(true);
        mapContent.addComponent(createToolbar());

        mapContent.setExpandRatio(googleMap, 1.0f);

        return mapContent;
    }


    private void getAccidentData(){
        readData = new ReadData();
        allAccidentsList = readData.getAllAccidents();
    }

    // A method to add the data points to the map the first time before a user selects which data to View
    private void displayData(GoogleMap map, ArrayList<Accident> list){
        for(Accident element : list) {
            if (element instanceof CarAccident) {
                map.addMarker("Time: " + element.getAccidentTime().output() + System.lineSeparator() + "Acceleration: " + element.getAcceleration(),
                        new LatLon(element.getLatitude(), element.getLongitude()), false, null);
            } else if (element instanceof RunningAccident) {
                map.addMarker("Time: " + element.getAccidentTime().output(),
                        new LatLon(element.getLatitude(), element.getLongitude()), false, "VAADIN/themes/mytheme/blue.png");
            }
        }
    }


    // Toolbar at the bottom to change the data set
    private Component createToolbar(){

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);


        // Customized accident data
        VerticalLayout typeAcceleration = new VerticalLayout();
        typeAcceleration.setSpacing(true);
        layout.addComponent(typeAcceleration);

        ComboBox accidentType = new ComboBox(); accidentType.setInputPrompt("Accident Type");
        accidentType.addItem("All"); accidentType.addItem("Car accidents"); accidentType.addItem("Running accidents");
        typeAcceleration.addComponent(accidentType);

        ComboBox accelerations = new ComboBox(); accelerations.setInputPrompt("Acceleration (for car accidents)");
        accelerations.addItem("All"); accelerations.addItem("Below 30km/hr"); accelerations.addItem("30km/hr & above");
        typeAcceleration.addComponent(accelerations);


        VerticalLayout date = new VerticalLayout();
        date.setSpacing(true);
        layout.addComponent(date);

        PopupDateField startDate = new PopupDateField();
        startDate.setInputPrompt("Start Date");
        date.addComponent(startDate);

        PopupDateField endDate = new PopupDateField();
        endDate.setInputPrompt("End date");
        date.addComponent(endDate);


        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.setSpacing(true);
        layout.addComponent(buttonLayout);

        ComboBox timeFrame = new ComboBox(); timeFrame.setInputPrompt("Time of accident");
        timeFrame.addItem("All"); timeFrame.addItem("00 - 06"); timeFrame.addItem("06 - 12"); timeFrame.addItem("12 - 18"); timeFrame.addItem("18 - 00");
        buttonLayout.addComponent(timeFrame);

        Button custom = new Button("View Data");
        custom.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                if(accidentType.getValue() == null || accelerations.getValue() == null || startDate.getValue() == null || endDate.getValue() == null || timeFrame.getValue() == null){
                    Notification errorNotification = new Notification("Select accident type,time, acceleration, start and end date");
                    errorNotification.setHtmlContentAllowed(true);
                    errorNotification.setStyleName("tray dark small closable login-help");
                    errorNotification.show(UI.getCurrent().getPage());
                }else{
                    googleMap.clearMarkers();
                    displayData(googleMap, readData.seasonData(accidentType.getValue().toString() ,
                            accelerations.getValue().toString(),startDate.getValue().toString(), endDate.getValue().toString(),
                            timeFrame.getValue().toString()));
                }
            }
        });

        buttonLayout.addComponent(custom);

        Button overView = new Button("View Overview");
        overView.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                googleMap.clearMarkers();
                displayData(googleMap, allAccidentsList);
            }
        });

        layout.addComponent(overView);

        return layout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

}
