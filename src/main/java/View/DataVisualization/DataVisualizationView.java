package View.DataVisualization;


import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.Margin;
import at.downdrown.vaadinaddons.highchartsapi.model.data.HighChartsData;
import at.downdrown.vaadinaddons.highchartsapi.model.data.base.IntData;
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.ColumnChartPlotOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.series.ColumnChartSeries;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import Application.ReadData;
import Application.Accident;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by william on 20-09-2016.
 */

public class DataVisualizationView extends VerticalLayout implements View {


    private ArrayList<Accident> allAccidents = new ArrayList<>();
    private ArrayList<String> years = new ArrayList<>();
    private ArrayList<String> months = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();

    // ArrayLists to hold the data for the charts
    private List<HighChartsData> cAccidentList = new ArrayList<>();
    private List<HighChartsData> rAccidentList = new ArrayList<>();

    private ReadData readData = new ReadData();

    // 2 lists to represent the number of accidents for each type...default is all the data
    private int [] cAccidents = readData.getCAccidentData(0);
    private int [] rAccidents = readData.getRAccidentData(0);

    // Array to hold x axis items
    private List<String> xAxisValues = new ArrayList<>();

    // UI Elements to hold the data
    Panel panel;
    VerticalLayout chartLayout;

    public DataVisualizationView(){
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        createData();

        addComponent(buildHeader());
        addComponent(buildMainGraph());
        addComponent(buildGraph());

    }

    // A method to get all the data and setup the chart legends
    private void createData(){

        months.add("All");
        months.add("Jan"); months.add("Feb"); months.add("Mar"); months.add("Apr");
        months.add("May"); months.add("Jun"); months.add("Jul"); months.add("Aug");
        months.add("Sep"); months.add("Oct"); months.add("Nov"); months.add("Dec");

        time.add("All"); time.add("00 - 06"); time.add("06 - 12"); time.add("12 - 18"); time.add("18 - 00");

        ArrayList<Integer> year = new ArrayList<>();

        // Calculate how many years are in the database
        allAccidents = readData.getAllAccidents();
        for(Accident accident : allAccidents){
            if(year.contains(accident.getAccidentTime().getYear())){
                continue;
            }
            else{
                year.add(accident.getAccidentTime().getYear());
            }
        }

        Collections.sort(year);

        years.add("All");
        for(int element : year){
            String yearValue = "" + element;
            years.add(yearValue);
        }

        xAxisValues.add("J"); xAxisValues.add("F");
        xAxisValues.add("M"); xAxisValues.add("A");
        xAxisValues.add("M"); xAxisValues.add("J");
        xAxisValues.add("J"); xAxisValues.add("A");
        xAxisValues.add("S"); xAxisValues.add("O");
        xAxisValues.add("N"); xAxisValues.add("D");

    }


    /**
     * Method to create header to hold the title
     */
    private Component buildHeader(){
        final HorizontalLayout header = new HorizontalLayout();

        header.setSpacing(true);

        double ratio = 1100.0 / 1366.0;
        int widthToUse = (int) (ratio * UI.getCurrent().getPage().getBrowserWindowWidth());
        System.out.println(widthToUse);
        String width = "" + widthToUse; // get the width depending on screen size

        header.setWidth(width + "px");

        Label title = new Label("Data Visualization");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        header.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

        return header;
    }

    /**
     * Method to create the layouts with the graphs
     */
    private Component buildGraph(){
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        // Add both layouts
        layout.addComponent(buildCarAccidentGraph());
        layout.addComponent(buildRunningAccidentGraph());

        return layout;
    }

    /**
     * Method to create the main graph with the comparison of both accidents
     */
    private Component buildMainGraph(){
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        chartLayout = new VerticalLayout();
        chartLayout.setSpacing(true);
        layout.addComponent(chartLayout);

        displayComparisonData(chartLayout);

        // Buttons and other controls
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        layout.addComponent(verticalLayout);

        ComboBox yearOption = new ComboBox("Select year", years);
        verticalLayout.addComponent(yearOption);

        ComboBox timeOption = new ComboBox("Select Time Period", time);
        verticalLayout.addComponent(timeOption);

        ComboBox accelerationOption = new ComboBox("Select Acceleration (car accidents)");
        accelerationOption.addItem("All");
        accelerationOption.addItem("Below 30km/hr");
        accelerationOption.addItem("30km/hr & above");

        verticalLayout.addComponent(accelerationOption);

        Button button = new Button("Choose data set");
        verticalLayout.addComponent(button);

        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent){
                if(yearOption.getValue() == null || timeOption.getValue() == null || accelerationOption.getValue()==null){
                    Notification errorNotification = new Notification("Select both a year and time period");
                    errorNotification.setHtmlContentAllowed(true);
                    errorNotification.show(UI.getCurrent().getPage());
                }
                else{
                    chartLayout.removeAllComponents();
                    Panel newPanel = new Panel("Comparison of both types of Accidents");
                    chartLayout.addComponent(newPanel);
                    drawMainChart(newPanel, yearOption.getValue().toString(), timeOption.getValue().toString(), accelerationOption.getValue().toString());
                }
            }
        });
        return layout;
    }

    /**
     * Method to create the original graph (i.e. before data is filtered)
     */
    private void displayComparisonData(VerticalLayout layout){

        // A panel to display the chart / graph
        panel = new Panel("Comparison of both types of Accidents");
        double ratio = 60.0/100.0;
        System.out.println(ratio);
        int width = (int ) (ratio * UI.getCurrent().getPage().getBrowserWindowWidth());
        System.out.println(width);
        panel.setWidth(width, Unit.PIXELS);
        layout.addComponent(panel);
        layout.setExpandRatio(panel, 1);

        // A chart to display the data
        ChartConfiguration chartConfig = new ChartConfiguration();
        chartConfig.setChartType(ChartType.COLUMN);
        chartConfig.removeBackgroundLines();

        Axis x_axis = new Axis(Axis.AxisType.xAxis);
        x_axis.setCategories(xAxisValues);

        chartConfig.setxAxis(x_axis);

        //The chart's margin. (LEFT - TOP - RIGHT - BOTTOM)
        chartConfig.setChartMargin(new Margin(30,10,10,70));
        chartConfig.setLegendEnabled(true);

        ColumnChartPlotOptions columnChartPlotOptions = new ColumnChartPlotOptions();
        columnChartPlotOptions.setDataLabelsFontColor(Colors.BLACK);
        chartConfig.setPlotOptions(columnChartPlotOptions);

        // Set the data here
        for(int i = 0; i < cAccidents.length; i++){
            cAccidentList.add(new IntData(cAccidents[i]));
            rAccidentList.add(new IntData(rAccidents[i]));
        }

        ColumnChartSeries carAccidentColumn = new ColumnChartSeries("Car Accidents", cAccidentList);
        ColumnChartSeries rAccidentColumn = new ColumnChartSeries("Running Accidents", rAccidentList);

        chartConfig.getSeriesList().add(carAccidentColumn);
        chartConfig.getSeriesList().add(rAccidentColumn);

        buildChart(panel, chartConfig);
    }

    // A method to add a chart onto a panel
    private void drawMainChart(Panel panel, String theYear, String theTime, String theAcceleration){

        double ratio = 60.0/100.0;
        System.out.println(ratio);
        int width = (int ) (ratio * UI.getCurrent().getPage().getBrowserWindowWidth());
        System.out.println(width);
        panel.setWidth(width, Unit.PIXELS);

        // A chart to display the data
        ChartConfiguration chartConfiguration = new ChartConfiguration();
        chartConfiguration.setChartType(ChartType.COLUMN);
        chartConfiguration.removeBackgroundLines();

        Axis x_axis = new Axis(Axis.AxisType.xAxis);
        x_axis.setCategories(xAxisValues);

        chartConfiguration.setxAxis(x_axis);

        //@param chartMargin The chart's margin. (LEFT - TOP - RIGHT - BOTTOM)
        chartConfiguration.setChartMargin(new Margin(30,10,10,70));
        chartConfiguration.setLegendEnabled(true);

        ColumnChartPlotOptions columnChartPlotOptions = new ColumnChartPlotOptions();
        columnChartPlotOptions.setDataLabelsFontColor(Colors.BLACK);
        chartConfiguration.setPlotOptions(columnChartPlotOptions);

        cAccidentList = new ArrayList<>();
        rAccidentList = new ArrayList<>();

        // set the data here
        int [] selectedCarData = readData.selectCarData(theYear, theTime, theAcceleration);
        for(int i = 0; i < selectedCarData.length; i++){
            cAccidentList.add(new IntData(selectedCarData[i]));
        }

        int  [] selectedRunningData = readData.selectRunningData(theYear, theTime);
        for(int i = 0; i < selectedRunningData.length; i++){
            rAccidentList.add(new IntData(selectedRunningData[i]));
        }

        ColumnChartSeries carAccidentColumn = new ColumnChartSeries("Car Accidents", cAccidentList);
        ColumnChartSeries rAccidentColumn = new ColumnChartSeries("Running Accidents", rAccidentList);

        chartConfiguration.getSeriesList().add(carAccidentColumn);
        chartConfiguration.getSeriesList().add(rAccidentColumn);

        buildChart(panel, chartConfiguration);
    }

    private Component buildCarAccidentGraph(){

        // Layout containing graph with car accidents
        VerticalLayout carLayout = new VerticalLayout();
        carLayout.setSpacing(true);

        VerticalLayout chartLayout = new VerticalLayout();
        chartLayout.setSpacing(true);

        carLayout.addComponent(chartLayout);

        Panel panel1 = new Panel("Car Accidents");

        double ratio = 40.0/100.0;
        System.out.println(ratio);
        int width = (int ) (ratio * UI.getCurrent().getPage().getBrowserWindowWidth());
        System.out.println(width);
        panel1.setWidth(width, Unit.PIXELS);

        chartLayout.addComponent(panel1);
        //carLayout.addComponent(buttonLayout);

        ChartConfiguration chartConfiguration = new ChartConfiguration();
        chartConfiguration.setChartType(ChartType.COLUMN);
        chartConfiguration.removeBackgroundLines();

        Axis x_axis = new Axis(Axis.AxisType.xAxis);
        x_axis.setCategories(xAxisValues);

        chartConfiguration.setxAxis(x_axis);

        //The chart's margin. (LEFT - TOP - RIGHT - BOTTOM)
        chartConfiguration.setChartMargin(new Margin(30,10,10,70));
        chartConfiguration.setLegendEnabled(true);


        ColumnChartPlotOptions columnChartPlotOptions = new ColumnChartPlotOptions();
        columnChartPlotOptions.setDataLabelsFontColor(Colors.BLACK);
        chartConfiguration.setPlotOptions(columnChartPlotOptions);

        ColumnChartSeries carAccidentColumn = new ColumnChartSeries("Car Accidents", cAccidentList);

        chartConfiguration.getSeriesList().add(carAccidentColumn);

        buildChart(panel1, chartConfiguration);

        // Interface elements to change the data set
        Panel selectData = new Panel("Select Data Set");
        double r = 50.0/100.0;
        int size = (int ) (r * width);// the width of the panel
        selectData.setWidth(size, Unit.PIXELS);

        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSpacing(true);
        ComboBox yearOptions = new ComboBox("Select Year", years);
        panelContent.addComponent(yearOptions);
        ComboBox timeOptions = new ComboBox("Select Time", time);
        panelContent.addComponent(timeOptions);
        ComboBox accelerationOptions = new ComboBox("Select Acceleration Value");
        accelerationOptions.addItem("All"); accelerationOptions.addItem("Below 30km/hr"); accelerationOptions.addItem("30km/hr & above");
        panelContent.addComponent(accelerationOptions);

        Button confirmData = new Button("Choose Data Set");
        panelContent.addComponent(confirmData);

        confirmData.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(yearOptions.getValue() == null || timeOptions.getValue() == null || accelerationOptions.getValue() == null){
                    Notification errorNotification = new Notification("Select both a year and time period");
                    errorNotification.setHtmlContentAllowed(true);
                    errorNotification.show(UI.getCurrent().getPage());
                }
                else{
                    chartLayout.removeAllComponents();
                    Panel newPanel = new Panel("Car Accidents");
                    chartLayout.addComponent(newPanel);
                    drawCarChart(newPanel, yearOptions.getValue().toString(), timeOptions.getValue().toString(), accelerationOptions.getValue().toString());
                }
            }
        });

        selectData.setContent(panelContent);
        carLayout.addComponent(selectData);

        return carLayout;
    }

    // A method to add a chart onto a panel
    private void drawCarChart(Panel panel, String theYear, String theTime, String theAcceleration){

        double ratio = 40.0/100.0;
        int width = (int ) (ratio * UI.getCurrent().getPage().getBrowserWindowWidth());
        System.out.println(width);
        panel.setWidth(width, Unit.PIXELS);

        // A chart to display the data
        ChartConfiguration chartConfiguration = new ChartConfiguration();
        chartConfiguration.setChartType(ChartType.COLUMN);
        chartConfiguration.removeBackgroundLines();

        Axis x_axis = new Axis(Axis.AxisType.xAxis);
        x_axis.setCategories(xAxisValues);

        chartConfiguration.setxAxis(x_axis);

        //@param chartMargin The chart's margin. (LEFT - TOP - RIGHT - BOTTOM)
        chartConfiguration.setChartMargin(new Margin(30,10,10,70));
        chartConfiguration.setLegendEnabled(true);

        ColumnChartPlotOptions columnChartPlotOptions = new ColumnChartPlotOptions();
        columnChartPlotOptions.setDataLabelsFontColor(Colors.BLACK);
        chartConfiguration.setPlotOptions(columnChartPlotOptions);

        cAccidentList = new ArrayList<>();

        //set the data here
        int [] selectedCarData = readData.selectCarData(theYear, theTime, theAcceleration);
        for(int i = 0; i < selectedCarData.length; i++){
            cAccidentList.add(new IntData(selectedCarData[i]));

        }

        ColumnChartSeries carAccidentColumn = new ColumnChartSeries("Car Accidents", cAccidentList);

        chartConfiguration.getSeriesList().add(carAccidentColumn);

        buildChart(panel, chartConfiguration);
    }


    private Component buildRunningAccidentGraph(){

        // Layout containing graph with running accidents
        VerticalLayout runLayout = new VerticalLayout();
        runLayout.setSpacing(true);

        VerticalLayout chartLayout = new VerticalLayout();
        chartLayout.setSpacing(true);

        runLayout.addComponent(chartLayout);


        Panel panel1 = new Panel("Running Accidents");

        double ratio = 40.0/100.0;
        System.out.println(ratio);
        int width = (int ) (ratio * UI.getCurrent().getPage().getBrowserWindowWidth());
        System.out.println(width);
        panel1.setWidth(width, Unit.PIXELS);

        chartLayout.addComponent(panel1);

        ChartConfiguration chartConfiguration = new ChartConfiguration();
        chartConfiguration.setChartType(ChartType.COLUMN);
        chartConfiguration.removeBackgroundLines();

        chartConfiguration.getColors().add(Color.GREEN);


        Axis x_axis = new Axis(Axis.AxisType.xAxis);
        x_axis.setCategories(xAxisValues);

        chartConfiguration.setxAxis(x_axis);

        //@param chartMargin The chart's margin. (LEFT - TOP - RIGHT - BOTTOM)
        chartConfiguration.setChartMargin(new Margin(30,10,10,70));
        chartConfiguration.setLegendEnabled(true);


        ColumnChartPlotOptions columnChartPlotOptions = new ColumnChartPlotOptions();
        columnChartPlotOptions.setDataLabelsFontColor(Colors.BLACK);
        chartConfiguration.setPlotOptions(columnChartPlotOptions);

        ColumnChartSeries runAccidentColumn = new ColumnChartSeries("Running Accidents", rAccidentList);

        chartConfiguration.getSeriesList().add(runAccidentColumn);

        buildChart(panel1, chartConfiguration);


        // Interface elements to change the data set
        Panel selectData = new Panel("Select Data Set");
        double r = 50.0/100.0;
        int size = (int ) (r * width);// the width of the panel
        selectData.setWidth(size, Unit.PIXELS);


        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSpacing(true);
        ComboBox yearOptions = new ComboBox("Select Year", years);
        panelContent.addComponent(yearOptions);
        ComboBox timeOptions = new ComboBox("Select Time", time);
        panelContent.addComponent(timeOptions);

        Button confirmData = new Button("Choose Data Set");
        panelContent.addComponent(confirmData);

        confirmData.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(yearOptions.getValue() == null || timeOptions.getValue() == null){
                    Notification errorNotification = new Notification("Select both a year and time period");
                    errorNotification.setHtmlContentAllowed(true);
                    errorNotification.show(UI.getCurrent().getPage());
                }
                else{
                    chartLayout.removeAllComponents();
                    Panel newPanel = new Panel("Running Accidents");
                    chartLayout.addComponent(newPanel);
                    drawRunChart(newPanel, yearOptions.getValue().toString(), timeOptions.getValue().toString());
                }
            }
        });

        selectData.setContent(panelContent);
        runLayout.addComponent(selectData);

        return runLayout;
    }

    // A method to add a chart onto a panel...for running accidents
    private void drawRunChart(Panel panel, String theYear, String theTime){

        double ratio = 40.0/100.0;
        int width = (int ) (ratio * UI.getCurrent().getPage().getBrowserWindowWidth());
        panel.setWidth(width, Unit.PIXELS);

        // A chart to display the data
        ChartConfiguration chartConfiguration = new ChartConfiguration();
        chartConfiguration.setChartType(ChartType.COLUMN);
        chartConfiguration.removeBackgroundLines();


        Axis x_axis = new Axis(Axis.AxisType.xAxis);
        x_axis.setCategories(xAxisValues);

        chartConfiguration.setxAxis(x_axis);


        //@param chartMargin The chart's margin. (LEFT - TOP - RIGHT - BOTTOM)
        chartConfiguration.setChartMargin(new Margin(30,10,10,70));
        chartConfiguration.setLegendEnabled(true);

        ColumnChartPlotOptions columnChartPlotOptions = new ColumnChartPlotOptions();
        columnChartPlotOptions.setDataLabelsFontColor(Colors.BLACK);
        chartConfiguration.setPlotOptions(columnChartPlotOptions);

        rAccidentList = new ArrayList<>();

        // set the data here
        int [] selectedRunningData = readData.selectRunningData(theYear, theTime);
        for(int i = 0; i < selectedRunningData.length; i++){
            rAccidentList.add(new IntData(selectedRunningData[i]));
        }

        ColumnChartSeries runAccidentColumn = new ColumnChartSeries("Running Accidents", rAccidentList);

        chartConfiguration.getSeriesList().add(runAccidentColumn);

        buildChart(panel, chartConfiguration);
    }


    /**
     * Method to build a graph given the data and a panel..created as it reused.
     */
    private void buildChart(Panel panel, ChartConfiguration chartConfiguration){
        try{
            HighChart chart = new HighChartFactory().renderChart(chartConfiguration);
            chart.setSizeFull();
            panel.setContent(null);
            panel.setContent(chart);
        }catch(HighChartsException e){
            e.printStackTrace();
        }
    }





    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
