import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * UI frame for displaying image/video thumbnails attributes and download button when users click on a displayed FileDS object.
 */
public class ImageDetailsUI implements ActionListener {

    JFrame imgRootFrame;
    FileDS img;
    JLabel fullImage;
    JButton downloadButton;
    JList<String> infolist;
    private String tagsInString;
    

    /**
     * Creates an ImageDetailsUI object.
     * @param img
     */
    public ImageDetailsUI(FileDS img){

        this.img = img;
        SearchHistoryUI.addToHistory(img);
        imgRootFrame = new JFrame("Image Details");
        tagsInString = "";
        imgRootFrame.setSize(600,600);
        
        //Display Information about the image
        DisplayInfo();

        //Download button and download function
        downloadButton = new JButton("Download");
        downloadButton.setBounds(220, 340, 120, 30);
        downloadButton.addActionListener(this);

        //adds Jcomponents on the JFrame
        imgRootFrame.add(fullImage);
        imgRootFrame.add(infolist);
        imgRootFrame.add(downloadButton);
        imgRootFrame.setLayout(null);
        imgRootFrame.setVisible(true);
        

    }

    /**
     * Retrieves and displays FileDS attribute data and with a thumbnail (for images).
     */
    public void DisplayInfo(){
        //Displaying Image
        Image image;
        if(!img.getPath().contains(".mp4"))
            image = Toolkit.getDefaultToolkit().getImage(FileManagement.getDocPath() + img.getPath());
        else
            image = Toolkit.getDefaultToolkit().getImage("res/images/"+SearchResultsUI.defaultVideo);
        image = image.getScaledInstance(250,250, Image.SCALE_DEFAULT);
        fullImage = new JLabel(new ImageIcon(image));
        fullImage.setBounds(50,100, 200, 200);

        //Converting tag list into a string 
        for(String tag : img.getTags()){
            tagsInString += tag + " ,";
        }
        //Displaying the information
        DefaultListModel<String> info = new DefaultListModel<>();  
        info.addElement("Name : " + img.getName());
        info.addElement("Tags : " + tagsInString);
        info.addElement("User : "+ img.getUser());  
        info.addElement("License type : " + img.getLicenseType());  
        info.addElement("Resolution : "+img.getResolution());  
        info.addElement("Date added : " + img.getDateAdded().getString());  
        infolist = new JList<>(info);  
        infolist.setBounds(300,100, 200,200);  


    }

    /*
     * Manages download button function when a user clicks on it.
     */
    @Override
    public void actionPerformed(ActionEvent Ae) {
        if (Ae.getSource() == downloadButton){
            MainUI.filemanager.download(img.getPath());
            JOptionPane.showMessageDialog(MainUI.rootFrame,"Downloaded. Check your system downloads folder.");  
        }
    } 
    
}
