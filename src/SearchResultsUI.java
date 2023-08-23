import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/*
 * UI for window displaying FileDS search results (very similar to SearchHistoryUI).
 */
public class SearchResultsUI implements ActionListener {
    public JFrame srRootFrame; 
    public List<FileDS> imgList ;
    public static boolean isLightSearch = false;
    public static String defaultImage = "defaultImg.png";
    public static String defaultVideo = "defaultVid.png";


    /**
     * Creates a UI for accessing FileDS objects that appear in an input list.
     * @param imgList
     */
    public SearchResultsUI(List<FileDS> imgList){
        srRootFrame = new JFrame("Search Results");
        srRootFrame.setSize(600,600);
        srRootFrame.setVisible(true);  
        JScrollPane scrollPane = new JScrollPane();
        JPanel imagePanel = new JPanel();

        // Add images to the panel
        for (FileDS im : imgList) {
            String imagePath = im.getPath();
            String imageName = im.getName();
        
            Image image = Toolkit.getDefaultToolkit().getImage("res/images/"+defaultImage);
            if(!isLightSearch && !imagePath.contains(".mp4"))
                image = Toolkit.getDefaultToolkit().getImage((new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + MainUI.filemanager.sep+"Viste" + MainUI.filemanager.sep) + imagePath);
        
            if(imagePath.contains(".mp4")){
                image = Toolkit.getDefaultToolkit().getImage("res/images/"+defaultVideo);
            }
        
            image = image.getScaledInstance(150, 150, Image.SCALE_DEFAULT);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
        
            // add a new JLabel for the text
            JLabel textLabel = new JLabel(imageName);
        
            // create a new JPanel for the image/text pair
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(imageLabel, BorderLayout.CENTER);
            panel.add(textLabel, BorderLayout.SOUTH);
        
            //Opens new image details window when clicked
            panel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    ImageDetailsUI imgDetWindow = new ImageDetailsUI(im);
                }
            });
            panel.setSize(150,150);
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            imagePanel.add(panel); 
        }
        // Add the image panel to the scroll pane
        int numImages = imgList.size();
        int numColumns = 3;
        int numRows = (int) Math.ceil((double) numImages / numColumns);
        imagePanel.setLayout(new GridLayout(numRows, numColumns));
        imagePanel.setPreferredSize(new Dimension(numColumns * 150, numRows * 150));

        // Set the scrollbar policies
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane.setViewportView(imagePanel);
        
        srRootFrame.add(scrollPane);

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }


    
}
