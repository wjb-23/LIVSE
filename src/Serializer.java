import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/*
 * Serializes the images existing in the GenHashMap objects associated with FileDS characteristics.
 */
public class Serializer {
    public static String[] serializedObj = {"tagMap.ser", "userNameMap.ser","dateMap.ser","licenseMap.ser","resoMap.ser","nameMap.ser","searchHistory.ser"};
    public Serializer(){

    }

    /**
     * 
     * @return
     */
    public boolean checkIfSerializedExists(){
        boolean checkPassed = true;
        for(String serObj : serializedObj){
            File f = new File(FileManagement.getDocPath() + serObj);
            if(!f.exists()){
                checkPassed = false;
                break;
            }

        }
        return checkPassed;
    }

    public void createSerObj(Serializable sObj, String serName){
        String fPath = FileManagement.getDocPath() + serName;

        try{
            File tF = new File(fPath);
            tF.delete();
        }catch(Exception e) {
            //File Doesn't exist. No need to delete.
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(fPath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(sObj);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + fPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Serializable> getAllSerObj(){

        //Serialized objects will be returned in the order : "tagMap.ser", "userNameMap.ser","dateMap.ser","licenseMap.ser","resoMap.ser","nameMap.ser","searchHistory.ser"
        List<Serializable> resList = new LinkedList<Serializable>();
        for(String serName : serializedObj){
            String filePath = FileManagement.getDocPath() + serName;
            try {
                FileInputStream fileIn = new FileInputStream(filePath);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Serializable s = (Serializable) in.readObject();
                in.close();
                fileIn.close();
                resList.add(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resList;
    }
}
