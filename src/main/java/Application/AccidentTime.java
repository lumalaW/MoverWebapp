package Application;

/**
 * Created by william
 * A Java POJO to represent the time of an accident
 */



public class AccidentTime {

    private int year;
    private int month;
    private int date;

    private int hour;
    private int minutes;
    private float seconds;

    public AccidentTime(int year, int month, int date, int hour, int minutes, float seconds){
        this.year = year;
        this.month = month;
        this.date = date;

        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    // get string value of the month
    public String getMonthValue(){
        String actualMonth = null;
        switch(month) {
            case 1:
                actualMonth = "Jan";
                break;
            case 2:
                actualMonth = "Feb";
                break;
            case 3:
                actualMonth = "Mar";
                break;
            case 4:
                actualMonth = "Apr";
                break;
            case 5:
                actualMonth = "May";
                break;
            case 6:
                actualMonth = "Jun";
                break;
            case 7:
                actualMonth = "Jul";
                break;
            case 8:
                actualMonth = "Aug";
                break;
            case 9:
                actualMonth = "Sep";
                break;
            case 10:
                actualMonth = "Oct";
                break;
            case 11:
                actualMonth = "Nov";
                break;
            case 12:
                actualMonth = "Dec";
                break;
        }
        return actualMonth;
    }


    public int getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public float getSeconds() {
        return seconds;
    }

    public String output(){
        String theMonth;
        if(month < 10){
            theMonth = String.format("%02d", month);
        }else {theMonth = ""+ month;}

        String theDay;
        if(date < 10){
            theDay = String.format("%02d", date);
        }else{theDay = "" + date;}

        String theHour;
        if(hour < 10){
            theHour = String.format("%02d", hour);
        }else{theHour = "" + hour;}

        String theMinute;
        if(minutes < 10){
            theMinute = String.format("%02d", minutes);
        }else{theMinute = "" + minutes;}

        String output = "" + year + "-" + theMonth + "-" + theDay + "         " + theHour + ":" + theMinute; // + ":" + theSecond;
        return output;
    }


    public int compare(AccidentTime otherTime) {
        if(this.getYear() < otherTime.getYear()){
            return -1;
        }
        else if(this.getYear() > otherTime.getYear()){
            return 0;
        }
        else{ // year is equal
            // check month
            if(this.getMonth() < otherTime.getMonth()){
                return -1;
            }
            else if(this.getMonth() > otherTime.getMonth()){
                return 1;
            }
            else{ // check day
                if(this.getDate() < otherTime.getDate()){
                    return -1;
                }
                else if(this.getDate() > otherTime.getDate()){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        }
    }

}
