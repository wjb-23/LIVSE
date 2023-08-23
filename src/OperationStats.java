/*
 * Object for storing processing speeds of search operations.
 */
public class OperationStats {
    public String operation ;
    public String timeTakenInNanoSeconds;

    /**
     * This creates a OperationStats object.
     * @param oper
     * @param t
     */
    public OperationStats(String oper, long t){
        this.operation = oper; 
        this.timeTakenInNanoSeconds = Long.toString(t);
    }

}
