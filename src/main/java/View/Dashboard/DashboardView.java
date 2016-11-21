package View.Dashboard;


import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.data.HighChartsData;
import at.downdrown.vaadinaddons.highchartsapi.model.data.base.IntData;
import at.downdrown.vaadinaddons.highchartsapi.model.series.LineChartSeries;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import Application.ReadData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by william
 * The dashboard view instructions to the user and a line graph that shows the trend over the past 12 months.
 */

public class DashboardView extends VerticalLayout implements View {


    ReadData readData;

    // Constructor to initialize method to read data
    public DashboardView(){

        readData = new ReadData();

        setMargin(true);
        setSpacing(true);
        setSizeFull();

        addComponent(buildHeader());
        addComponent(buildLineGraph());
        addComponent(buildNotes());


    }

    /**
     * Method to create header to hold the title
     */

    private Component buildHeader(){
        final HorizontalLayout header = new HorizontalLayout();

        header.setSpacing(true);

        double ratio = 1100.0 / 1366.0;
        int widthToUse = (int) (ratio * UI.getCurrent().getPage().getBrowserWindowWidth());
        String width = "" + widthToUse; // get the width depending on screen size

        header.setWidth(width + "px");

        Label title = new Label("Dashboard");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        header.setComponentAlignment(title, Alignment.TOP_LEFT);

        return header;
    }

    /**
     * Method to build the line graph
     */
    private Component buildLineGraph(){

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);


        ChartConfiguration lineConfiguration = new ChartConfiguration();
        lineConfiguration.setTitle("Trend over the past 12 months");
        lineConfiguration.setChartType(ChartType.LINE);
        lineConfiguration.setBackgroundColor(Color.WHITE);

        Panel panel = new Panel();
        layout.addComponent(panel);


        Axis x_axis = new Axis(Axis.AxisType.xAxis);
        x_axis.setCategories(readData.getAllDates());

        lineConfiguration.setxAxis(x_axis);

        List <String> dates = readData.getAllDates();
        List <Integer> caraccidents = readData.getCarTrend();
        List <Integer> runaccidents = readData.getRunningTrend();

        System.out.println(dates);
        System.out.println(caraccidents);
        System.out.println(runaccidents);

        List<HighChartsData> carAccidents = new ArrayList<>();
        List<HighChartsData> runningAccidents = new ArrayList<>();

        for(int i = 0; i < caraccidents.size(); i++){
            carAccidents.add(new IntData(caraccidents.get(i)));
            runningAccidents.add(new IntData(runaccidents.get(i)));
        }

        LineChartSeries car = new LineChartSeries("Car Accidents", carAccidents);
        LineChartSeries running = new LineChartSeries("Running Accidents", runningAccidents);

        lineConfiguration.getSeriesList().add(car);
        lineConfiguration.getSeriesList().add(running);

        try {
            HighChart lineChart = new HighChartFactory().renderChart(lineConfiguration);
            lineChart.setHeight(40, Unit.PERCENTAGE);
            lineChart.setWidth(panel.getWidth(), Unit.PERCENTAGE); panel.getWidth();
            panel.setContent(lineChart);
            layout.setComponentAlignment(panel, Alignment.TOP_LEFT);
        }catch(HighChartsException e){
            e.printStackTrace();
        }

        return layout;
    }

    /**
     * Method to create textfield with user instructions
     */
    private Component buildNotes(){
        final HorizontalLayout layout = new HorizontalLayout();

        TextArea notes = new TextArea("Notes");
        notes.setValue("Remember to :\n~" +
                "Use the map exactly like Google Maps \n~" +
                "Right click on a row in the tables to View details \n~" +
                "Select different types of data to View on the map");
        notes.setSizeFull();
        notes.setHeightUndefined();
        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        Component panel = contentWrapper(notes);
        panel.setWidth("50%");
        panel.addStyleName("notes");
        layout.addComponent(panel);

        return panel;
    }

    private Component contentWrapper(final Component content){
        final CssLayout wrapper = new CssLayout();
        wrapper.setWidth("100%");
        wrapper.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        toolbar.addComponents(caption);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        wrapper.addComponent(card);

        return wrapper;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
