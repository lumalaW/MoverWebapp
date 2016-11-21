package Application;

import DataAccess.FakeData;
import DataAccess.FetchData;

import javax.swing.plaf.synth.SynthStyle;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by william.
 * A java class to sort the arrayList and filter data accordingly.
 * Used on the Google Maps page and the Data Visualization page
 */


// A class to organise the data values to be displayed.
public class ReadData {

    // Collections to hold the data
    private ArrayList<CarAccident> carAccidents = new ArrayList<>();
    private ArrayList<RunningAccident> runningAccidents = new ArrayList<>();
    private ArrayList<Accident> allAccidents = new ArrayList<>();

    //Collections to hold the data of the past 12 months
    private List<String> allDates = new ArrayList<>();
    private List<Integer> carAccidentsList = new ArrayList<>();
    private List<Integer> runningAccidentsList = new ArrayList<>();


    // Constructor...fill the arrayLists with the all the data
    public ReadData(){
        // Get all the data from the DataAccess package.
        carAccidents = FakeData.getCAccidents();
        runningAccidents = FakeData.getRAccidents();

        for(CarAccident element : carAccidents) allAccidents.add(element);

        for(RunningAccident element : runningAccidents) allAccidents.add(element);

        SimpleDateFormat monthDate = new SimpleDateFormat("MMM-yyyy");
        Date date = new Date();
        String maxDate = monthDate.format(date);
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(monthDate.parse(maxDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= 12; i++) {
            String month_name1 = monthDate.format(cal.getTime());
            allDates.add(month_name1);
            runningAccidentsList.add(0); // Initialize the positions of the array
            carAccidentsList.add(0);
            cal.add(Calendar.MONTH, -1);
        }
        Collections.reverse(allDates);
    }

    // An array containing 12 values.
    // Each values represents the number of accidents per month of a given year or all the years (cumulative total by month)
    // For car accidents
    public int [] getCAccidentData(int year){
        int [] data = new int[12];
        if(year == 0){ // get the cumulative data i.e all the data
            for(CarAccident element : carAccidents){
                int theMonth = element.getAccidentTime().getMonth();
                fillArray(data, theMonth);
            }
        }else{
            for(CarAccident element : carAccidents){
                if(element.getAccidentTime().getYear() == year) {
                    int theMonth = element.getAccidentTime().getMonth();
                    fillArray(data, theMonth);
                }
                else{
                    continue;
                }
            }
        }
        return data;
    }

    // An array containing 12 values.
    // Each values represents the number of accidents per month of a given year or all the years (cumulative total by month)
    // For running accidents
    public int [] getRAccidentData(int year){
        int [] data = new int[12];

        if(year == 0){ // get the cumulative data i.e all the data
            for(RunningAccident element : runningAccidents){
                int theMonth = element.getAccidentTime().getMonth();
                fillArray(data, theMonth);
            }
        }else{
            for(RunningAccident element : runningAccidents){
                if(element.getAccidentTime().getYear() == year){
                    int theMonth = element.getAccidentTime().getMonth();
                    fillArray(data, theMonth);
                }
                else{
                    continue;
                }
            }
        }
        return data;
    }

    // A private method to fill an array with the monthly data
    private void fillArray(int [] array, int month){
        switch (month){
            case 1:
                array[0] += 1;
                break;
            case 2:
                array[1] += 1;
                break;
            case 3:
                array[2] += 1;
                break;
            case 4:
                array[3] += 1;
                break;
            case 5:
                array[4] += 1;
                break;
            case 6:
                array[5] += 1;
                break;
            case 7:
                array[6] += 1;
                break;
            case 8:
                array[7] += 1;
                break;
            case 9:
                array[8] += 1;
                break;
            case 10:
                array[9] += 1;
                break;
            case 11:
                array[10] += 1;
                break;
            case 12:
                array[11] += 1;
                break;
            default:
                break;
        }

    }

    private void setDates(String time, String startTime, String endTime, int start, int end){
        if(!time.equals("All")){
            char [] array = time.toCharArray();
            startTime = "" + array[0] + array[1];
            endTime = "" + array[5] + array[6];
            start = Integer.parseInt(startTime);
            end = Integer.parseInt(endTime);
        }
    }

    // A method to select which data to display on the main graph
    public int [] selectCarData(String year, String time, String acceleration){
        // Get time range
        String startTime = null;
        String endTime = null;
        int start =0 ; int end = 0;

        setDates(time, startTime, endTime, start, end);

        int data [] = new int[12];

        if(year.equals("All")){ // all the data
            if(time.equals("All")){
                for(CarAccident element : carAccidents){
                    int theMonth = element.getAccidentTime().getMonth();
                    if(checkAcceleration(element, acceleration)){
                        fillArray(data, theMonth);
                    }else{
                        continue;
                    }
                }
            }
            else{
                // use the start and end time to get an array of 12 accidents
                for(CarAccident element : carAccidents){
                    int theMonth = element.getAccidentTime().getMonth();
                    int theTime = element.getAccidentTime().getHour();

                    if(theTime >= start && theTime < end){
                        if(checkAcceleration(element, acceleration)){
                            fillArray(data, theMonth);
                        }else{
                            continue;
                        }
                    }
                    else{
                        continue;
                    }
                }
            }
        }
        else{ // a specific year
            int Year = Integer.parseInt(year);
            if(time.equals("All")){
                for(CarAccident element : carAccidents){
                    int theMonth = element.getAccidentTime().getMonth();
                    int theYear = element.getAccidentTime().getYear();

                    if(theYear == Year){
                        if(checkAcceleration(element, acceleration)){
                            fillArray(data, theMonth);
                        }else{
                            continue;
                        }
                    }
                    else{
                        continue;
                    }
                }
            }
            else{

                for(CarAccident element : carAccidents){
                    int theYear = element.getAccidentTime().getYear();
                    int theTime = element.getAccidentTime().getHour();
                    int theMonth = element.getAccidentTime().getMonth();

                    if(theYear == Year && ( (theTime >= start) && (theTime < end) ) ){
                        if(checkAcceleration(element, acceleration)){
                            fillArray(data, theMonth);
                        }else{
                            continue;
                        }
                    }
                }
            }
        }
        return data;
    }

    // A private method to check the acceleration of a car accident against a given threshold.
    private boolean checkAcceleration(Accident accident, String acceleration){
        boolean acc;
        if(acceleration.equals("All")){
            acc = true;
        }
        else{
            if(acceleration.equals("Below 30km/hr")){
                if(accident.getAcceleration() < 30){
                    acc = true;
                }
                else{
                    acc = false;
                }
            }
            else{
                if(accident.getAcceleration() >= 30){
                    acc = true;
                }
                else{
                    acc = false;
                }
            }
        }
        return acc;
    }

    // A method to select which data to display
    public int [] selectRunningData(String year, String time){
        // Get time range
        String startTime = null;
        String endTime = null;
        int start =0 ; int end = 0;

        setDates(time,startTime, endTime, start, end);



        int data [] = new int[12];

        if(year.equals("All")){ // all the data
            if(time.equals("All")){
                for(RunningAccident element : runningAccidents){
                    int theMonth = element.getAccidentTime().getMonth();
                    fillArray(data, theMonth);
                }
            }
            else{
                // use the start and end time to get an array of 12 accidents
                for(RunningAccident element : runningAccidents){
                    int theMonth = element.getAccidentTime().getMonth();
                    int theTime = element.getAccidentTime().getHour();

                    if(theTime >= start && theTime < end){
                        fillArray(data, theMonth);
                    }
                    else{
                        continue;
                    }
                }
            }
        }
        else{ // a specific year
            int Year = Integer.parseInt(year);
            if(time.equals("All")){
                for(RunningAccident element : runningAccidents){
                    int theMonth = element.getAccidentTime().getMonth();
                    int theYear = element.getAccidentTime().getYear();

                    if(theYear == Year){
                        fillArray(data, theMonth);
                    }
                    else{
                        continue;
                    }
                }
            }
            else{

                for(RunningAccident element : runningAccidents){
                    int theYear = element.getAccidentTime().getYear();
                    int theTime = element.getAccidentTime().getHour();
                    int theMonth = element.getAccidentTime().getMonth();

                    if(theYear == Year && ( (theTime >= start) && (theTime < end) ) ){
                        fillArray(data, theMonth);
                    }
                }
            }
        }
        return data;
    }


    // A method to filter the data shown on Google Maps
    public ArrayList<Accident> seasonData(String accidentType, String acceleration, String startDate, String endDate, String time){

        ArrayList<Accident> dataSet = new ArrayList<>();

        // Get time range
        String startTime = null;
        String endTime = null;
        int start = 0;
        int end = 0;

        setDates(time, startTime, endTime, start, end);

        System.out.println(startDate);
        String startArray[] = startDate.split(" ");
        AccidentTime startDay = new AccidentTime(Integer.parseInt(startArray[5]), getMonth(startArray[1]), Integer.parseInt(startArray[2]),0,0,0);
        System.out.println(endDate);
        String endArray[] = endDate.split(" ");
        AccidentTime endDay = new AccidentTime(Integer.parseInt(endArray[5]), getMonth(endArray[1]), Integer.parseInt(endArray[2]),0,0,0);

        if (accidentType.equals("All")) {

            for(Accident element : allAccidents){
                if (time.equals("All")) {
                    // check year, month, date
                    if(element.getAccidentTime().compare(startDay) == -1){
                    }
                    else if((element.getAccidentTime().compare(startDay) == 0 || element.getAccidentTime().compare(startDay) == 1) && element.getAccidentTime().compare(endDay) == -1 ){
                        if(element instanceof CarAccident){
                            if(checkAcceleration(element, acceleration)){
                                dataSet.add(element);
                            }
                        }
                        else{
                            dataSet.add(element);
                        }
                    }
                } else {
                    if (element.getAccidentTime().getHour() >= start && element.getAccidentTime().getHour() < end) {
                        // check  year, month, date
                        // check year, month, date
                        if(element.getAccidentTime().compare(startDay) == -1){
                        }
                        else if((element.getAccidentTime().compare(startDay) == 0 || element.getAccidentTime().compare(startDay) == 1) && element.getAccidentTime().compare(endDay) == -1 ){
                            if(element instanceof CarAccident){
                                if(checkAcceleration(element, acceleration)){
                                    dataSet.add(element);
                                }
                                else{
                                }
                            }
                            else{
                                dataSet.add(element);
                            }
                        }
                    }
                }
            };

        }
        else if (accidentType.equals("Car accidents")) {
            for (CarAccident element : carAccidents) {
                if (time.equals("All")) {
                    // check year, month, date
                    if(element.getAccidentTime().compare(startDay) == -1){

                    }
                    else if((element.getAccidentTime().compare(startDay) == 0 || element.getAccidentTime().compare(startDay) == 1) && element.getAccidentTime().compare(endDay) == -1 ){
                        if(element instanceof CarAccident){
                            if(checkAcceleration(element, acceleration)){
                                dataSet.add(element);
                            }
                            else{
                            }
                        }
                        else{
                            dataSet.add(element);
                        }
                    }

                } else {
                    if (element.getAccidentTime().getHour() >= start && element.getAccidentTime().getHour() < end) {
                        //
                        // check year, month, date
                        if(element.getAccidentTime().compare(startDay) == -1){
                            continue;
                        }
                        else if((element.getAccidentTime().compare(startDay) == 0 || element.getAccidentTime().compare(startDay) == 1) && element.getAccidentTime().compare(endDay) == -1 ){
                            if(element instanceof CarAccident){
                                if(checkAcceleration(element, acceleration)){
                                    dataSet.add(element);
                                }
                                else{
                                }
                            }
                            else{
                                dataSet.add(element);
                            }
                        }
                    }
                }
            }

        }

        else if (accidentType.equals("Running accidents")) {
            for (RunningAccident element : runningAccidents) {
                if (time.equals("All")){
                    // check year, month, date
                    if(element.getAccidentTime().compare(startDay) == -1){
                        continue;
                    }
                    else if((element.getAccidentTime().compare(startDay) == 0 || element.getAccidentTime().compare(startDay) == 1) && element.getAccidentTime().compare(endDay) == -1 ){
                        dataSet.add(element);
                    }
                } else {
                    if (element.getAccidentTime().getHour() >= start && element.getAccidentTime().getHour() < end) {
                        //
                        // check year, month, date
                        if(element.getAccidentTime().compare(startDay) == -1){
                            continue;
                        }
                        else if((element.getAccidentTime().compare(startDay) == 0 || element.getAccidentTime().compare(startDay) == 1) && element.getAccidentTime().compare(endDay) == -1 ){
                            dataSet.add(element);
                        }
                    }
                }
            }
        }
        return dataSet;

    }

    // A method to return the month number given a String...used in another method(s)
    private int getMonth(String month){
        int actualMonth = 0;
        switch(month) {
            case "Jan":
                actualMonth = 1;
                break;
            case "Feb":
                actualMonth = 2;
                break;
            case "Mar":
                actualMonth = 3;
                break;
            case "Apr":
                actualMonth = 4;
                break;
            case "May":
                actualMonth = 5;
                break;
            case "Jun":
                actualMonth = 6;
                break;
            case "Jul":
                actualMonth = 7;
                break;
            case "Aug":
                actualMonth = 8;
                break;
            case "Sep":
                actualMonth = 9;
                break;
            case "Oct":
                actualMonth = 10;
                break;
            case "Nov":
                actualMonth = 11;
                break;
            case "Dec":
                actualMonth = 12;
                break;
        }
        return actualMonth;
    }

    // Get the trend over the past 12 months for car accidents
    public List<Integer> getCarTrend(){

        for(CarAccident element : getCarAccidents()){
            String monthValue = element.getAccidentTime().getMonthValue();
            String monthYear = monthValue + "-" + element.getAccidentTime().getYear();

            if(allDates.contains(monthYear)){
                int index = allDates.indexOf(monthYear);
                int current = carAccidentsList.get(index);
                carAccidentsList.set(index, current + 1);
            }else{
                continue;
            }
        }
        return carAccidentsList;
    }

    // Get the trend for running accdents over the past 12 months
    public List<Integer> getRunningTrend(){

        for(RunningAccident element : getRunningAccidents()){
            String monthValue = element.getAccidentTime().getMonthValue();
            String monthYear = monthValue + "-" + element.getAccidentTime().getYear();

            if(allDates.contains(monthYear)){
                int index = allDates.indexOf(monthYear);
                int current = runningAccidentsList.get(index);
                runningAccidentsList.set(index, current + 1);
            }else{
                continue;
            }
        }
        return runningAccidentsList;
    }


    public List<String> getAllDates(){
        return  allDates;
    }

    // Getters for the values
    public ArrayList<Accident> getAllAccidents() {
        return allAccidents;
    }

    public ArrayList<RunningAccident> getRunningAccidents() {
        return runningAccidents;
    }

    public ArrayList<CarAccident> getCarAccidents(){
        return carAccidents;
    }

}
