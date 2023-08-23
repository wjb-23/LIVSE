import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
 * Manages image/video files for downloading capability, checks for user operating system
 * to ensure that file path formats are correct, and verifies if system Documents folder exists.
 */
public class FileManagement {
    public static String sep;

    /**
     * Creates a FileManagement object.
     */
    public FileManagement(){
        sep = FileSystems.getDefault().getSeparator();
    }
    
    private  void moveFile(String source, String target){
        try {
            Files.copy(Paths.get(source), Paths.get(target),StandardCopyOption.REPLACE_EXISTING );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addSingleImage(String filePath, String fileName){
        String target = getDocPath()+fileName;
        moveFile(filePath, target);

    }
    public void download(String imageFile){
        String downloadDir = System.getProperty("user.home")+sep+"Downloads"+sep+imageFile;
        String source = getDocPath()+imageFile;
        moveFile(source, downloadDir);
    }

    public static String getDocPath(){
        if(getOS().contains("Windows")){
            return (new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + sep+"Viste")+sep;
            
        }else{
            return (new JFileChooser().getFileSystemView().getDefaultDirectory().toString())+sep+"Documents" + sep+"Viste"+sep;
        }
        
    }

    public static void ensureAppDocFolderExists(){
        String dir = "";
        if(getOS().contains("Windows")){
            dir= (new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + sep+"Viste");
            
        }else{
            dir = (new JFileChooser().getFileSystemView().getDefaultDirectory().toString())+sep+"Documents" + sep+"Viste";
        }
        File directory = new File(dir);
        if(!directory.exists()){
            boolean dirRes = directory.mkdir();
            if(dirRes){
                JOptionPane.showMessageDialog(MainUI.rootFrame,"New documents folder created!");
            }else{
                JOptionPane.showMessageDialog(MainUI.rootFrame, "Document folder creation failed. Restart the application...");
            }
        }else{
            //no worries directory exists. 
        }
        
    }

    public static String getOS(){
        String os = System.getProperty("os.name");
        return os;
    }

}
