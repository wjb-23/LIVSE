import java.io.Serializable;
import java.util.*;

/*
 * Compares DateHolder objects.
 */
public class DateComparator implements Comparator<DateHolder>, Serializable{

    @Override
    public int compare(DateHolder arg0, DateHolder arg1) {
        if(arg0.getYear()==arg1.getYear()){
            if(arg0.getMonth() == arg1.getMonth()){
                if(arg0.getDay() > arg1.getDay()){
                    return 1;
                }else if(arg0.getDay() == arg1.getDay()){
                    return 0;
                }else{
                    return -1;
                }
            }else{
                if(arg0.getMonth() > arg1.getMonth()){
                    return 1;
                }else{
                    return -1;
                }
            }
        }else{
            if(arg0.getYear()>arg1.getYear()){
                return 1;
            }else{
                return -1;
            }
        }
        
    }

    
}