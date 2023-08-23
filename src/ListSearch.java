import java.lang.reflect.Field;
import java.util.*;

/*
 * A List implementation of FileDS sorting algorithms used to assess the efficiency of our main sorting methods.
 * Methods are identical in purpose to GenHashMap.
 */
public class ListSearch {

    private List<FileDS> map = new LinkedList();

    public List<FileDS> getLooseSearch(List<String> attributeList, Field f){
        List<FileDS> fin = new LinkedList<>();
        
        for(String attr : attributeList){
            for (FileDS img : map){
                if (img.tags.contains(attr)){
                    fin.add(fin.size()-1, img);
                }
            }
        }
        return fin;
    }


    public List<FileDS> get(List<String> attributeList, Field f){
        List<FileDS> fin = new LinkedList<>();
        for (FileDS img : map){
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

    public void add(FileDS im, Field f){
        if (!map.contains(im)){
            map.add(im);
        }
        
    }
    
}