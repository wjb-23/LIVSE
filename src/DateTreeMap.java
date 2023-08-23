import java.io.Serializable;
import java.util.*;

/**
 * TreeMap of DateHolders that are associated with image/video objects (sorted in entrySet()).
 */
public class DateTreeMap implements Serializable{
    public Map<DateHolder, List<FileDS>> map;

    /**
     * Creates DateTreeMap object.
     */
    public DateTreeMap(){
        map = new TreeMap<DateHolder, List<FileDS>>(new DateComparator());
    }

    /**
     * Creates a sorted list of existing FileDS in chronological order.
     * @return
     */
    public List<FileDS> getAllOrdered(){
        List<FileDS> finList = new LinkedList<>();
        for (Map.Entry<DateHolder, List<FileDS>> entry : map.entrySet()) {
            finList.addAll(entry.getValue());
        }
        return finList;
    }
    /**
     * Creates a list of existing FileDS that have an identical DateHolder.
     * @param d
     * @return
     */
    public List<FileDS> getImagesAtDate(DateHolder d){
        if(!map.keySet().contains(d))
            return new LinkedList<FileDS>();
        return map.get(d);
    }

    /**
     * Adds an image/video to a list of FileDS associated with the same DateHolder key.
     * @param im
     */
    public void add(FileDS im){
        List<FileDS> t;
        if(map.keySet().contains(im.getDateAdded())){
            t = map.get(im.getDateAdded());
            t.add(im);
        }else{
            t = new LinkedList<>();
            t.add(im);
        }
        map.put(im.getDateAdded(),t);
    }
    
}