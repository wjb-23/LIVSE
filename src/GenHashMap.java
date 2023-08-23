import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/*
 * Hash map that stores <file tag, image/video> entry pairs and acts as a basis for most of LIVSE's sorting algorithms.
 */
public class GenHashMap implements Serializable {
    public Map<String,List<FileDS>> map = new HashMap();

    /**
     * Creates a GenHashMap object.
     */
    public GenHashMap(){
    }

    /**
     * Returns a list of FileDS created by looping through the list of input attributes and taking all elements
     * in the list value associated with every attribute key in the attribute list.
     * @param attributeList
     * @param f
     * @return
     */
    public List<FileDS> getLooseSearch(List<String> attributeList, Field f){
        System.out.println(attributeList.toString());
        System.out.println("performing loose search");
        
        List<FileDS> fin = new LinkedList<>();

        System.out.println(map.keySet().toString());
        System.out.println(attributeList.toString());
        for(String attr : attributeList){
            if(map.keySet().contains(attr))
            fin.addAll(fin.size(), map.get(attr));
        }
        return fin;
    }

    /**
     * Returns a list of FileDS created by looping through the list of elements that are associated with the first attribute in the list
     * and taking those which are associated with all attributes in the given attribute list.
     * @param attributeList
     * @param f
     * @return
     */
    public List<FileDS> get(List<String> attributeList, Field f){
        System.out.println(attributeList.toString());
        if(!map.keySet().contains(attributeList.get(0)))
            return new LinkedList<>();
        List<FileDS> unProc = map.get(attributeList.get(0));
        
        List<FileDS> fin = new LinkedList<>();
        for (FileDS img : unProc){
            boolean contains = true;
            Object v = null;
            try {
                v = f.get(img);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            
            for(String attr: attributeList){

                if(v instanceof String){
                    
                    if(attr.equals((String) v) ==false && contains == true){
                        contains = false ;
                    }
                }else if(v instanceof List){
                    if(img.getTags().contains(attr)==false&& contains == true){
                        contains = false;
                    }
                    }
                }
            if (contains == true){
                fin.add(img);
            }
                
        }
        return fin;
    }

    /**
     * Returns list of FileDS sorted by attributes (other than dates) in alphabetical order that are first put into a set
     * to avoid duplicates in the final search.
     * @return
     */
    public List<FileDS> getAllOrdered(){
        List<FileDS> fin = new LinkedList<FileDS>();
        Set<String> orderedAttr = new TreeSet<>();
        for(String s : map.keySet()){
            orderedAttr.add(s);
        }
        System.out.println(orderedAttr.toString());
        for(String attr : orderedAttr){
            fin.addAll(map.get(attr));
        }
       
        return fin;
    }



    /**
     * Adds a FileDS to a hash map, the image/video is added to a List<FileDS> value based on a corresponding attribute key field.
     * @param im
     * @param f
     */
    public void add(FileDS im, Field f){
        Object value = null;
        try {
            value = f.get(im);
        } catch (IllegalAccessException e) {
            System.err.println("Error accessing field: " + e.getMessage());
        }
        
        if(value instanceof String){
            String atr = (String) value;
            List<FileDS> t;
            if(map.keySet().contains(atr)){
                t = map.get(atr);
                t.add(im);
            }else{
                t = new LinkedList<>();
                t.add(im);
            }
            map.put(atr,t);
        }else if(value instanceof List){
            for(String attribut : im.getTags()){
                List<FileDS> t;
                if(map.keySet().contains(attribut)){
                    t = map.get(attribut);
                    t.add(im);
                }else{
                    t = new LinkedList<>();
                    t.add(im);
                }
                map.put(attribut,t);
            }
        }
        
    }
}
