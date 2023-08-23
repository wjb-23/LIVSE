import java.io.Serializable;

/**
 * Date object that will be associated with each image/video.
 */
public class DateHolder implements Serializable{
    public int day; 
    public int month ; 
    public int year; 

    /**
     * Creates DateHolder object.
     * @param day
     * @param month
     * @param year
     */
    public DateHolder(int day, int month, int year){
        this.day = day; 
        this.month = month ; 
        this.year = year; 
    }
    public String getString(){
        return this.day + "-" + this.month + "-" + this.year;
    }
    public int getDay(){
        return this.day;
    }
    public int getMonth(){
        return this.month;
    }
    public int getYear(){
        return this.year;
    }

}
