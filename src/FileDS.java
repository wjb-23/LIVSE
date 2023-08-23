import java.io.Serializable;
import java.util.*;

/*
 * Fundamental data structure for an image/video added to LIVSE application.
 */
public class FileDS implements Serializable{
    public String name; 
    public List<String> tags; 
    public String user ; 
    public String path;
    public DateHolder dateAdded; 
    public String licenseType;
    public String resolution ; 

    /*
     * Creates a FileDS object.
     */
    public FileDS(String name,String path,  List<String> tags, String user, DateHolder dateAdded, String licenseType, String resolution){
        this.name = name; 
        this.path = path; 
        this.licenseType = licenseType; 
        this.tags = tags; 
        this.resolution = resolution; 
        this.user = user; 
        this.dateAdded = dateAdded;
    }

    /*
     * FileDS constructor without tags list.
     */
    public FileDS(String name, String path,String user, DateHolder dateAdded, String licenseType, String resolution){
        this.name = name; 
        this.path = path;
        this.user = user; 
        this.tags = new ArrayList<String>();
        this.dateAdded = dateAdded ; 
        this.resolution = resolution; 
        this.licenseType = licenseType ; 
    }
    
    public void addTag(String tag){
        tags.add(tag);
    }
    
    public void addTags(List<String> subTag){
        for(String tag : subTag){
            this.tags.add(tag);
        }   
    }

    public void setPath(String p){
        this.path = p; 
    }
    public void setName(String n){
        this.name = n; 
    }
    public void setUser(String u){
        this.user = u;
    }
    public void setDateAdded(DateHolder d){
        this.dateAdded = d; 
    }
    public void setResolution(String r){
        this.resolution = r;
    }
    public void setLicenseType(String l){
        this.licenseType = l;
    }
    public void setTags(List<String> st){
        this.tags = st;
    }

    public String getPath(){
        return this.path; 
    }
    public String getName(){
        return this.name;
    }
    public String getLicenseType(){
        return this.licenseType;
    }
    public DateHolder getDateAdded(){
        return this.dateAdded;
    }
    public String getResolution(){
        return this.resolution; 
    }
    public List<String> getTags(){
        return this.tags;
    }
    public String getUser(){
        return this.user;
    }
} 