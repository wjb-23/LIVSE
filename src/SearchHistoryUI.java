import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;

/*
 * Stores previously searched FileDS objects using a Stack and displays them in a window underneath the application
 * search bar.
 */
public class SearchHistoryUI {
    public static final int CAPACITY = 10 ; 
    public static  Deque<FileDS> searchHistory = new LinkedList<FileDS>();
    public static JScrollPane scrollPane;
    public static JPanel imagePanel;
    public static void addToHistory(FileDS img){

        if(searchHistory.contains(img)!=true){
            if(searchHistory.size() >=CAPACITY){
                searchHistory.removeFirst();
            }
            searchHistory.addLast(img);
            createSearchHistoryUI();
        }
        
        
    }
    
    /**
     * Creates a UI for accessing FileDS objects that appeared in previous searches.
     */
    public static void createSearchHistoryUI(){
        if(scrollPane != null){
            MainUI.rootFrame.remove(scrollPane);
        }
        scrollPane = new JScrollPane();
        imagePanel = new JPanel();

        // Add images to the panel
        Iterator<FileDS> iter =  searchHistory.descendingIterator();
        while(iter.hasNext()){
            FileDS im  = iter.next();
            String imagePath = im.getPath();
            Image image;
            if(!imagePath.contains(".mp4"))
                image = Toolkit.getDefaultToolkit().getImage(FileManagement.getDocPath()+ imagePath);
            else
                image = Toolkit.getDefaultToolkit().getImage("res/images/"+SearchResultsUI.defaultVideo);
            image = image.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
            JLabel imageLabel = new JLabel(new ImageIcon(image));

            //Opens new image details swindow when clicked
            imageLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    ImageDetailsUI imgDetWindow = new ImageDetailsUI(im);
                }
            });
            
            JLabel textLabel = new JLabel(im.name);
        
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
        int numImages = searchHistory.size();
        int numColumns = 3;
        int numRows = (int) Math.ceil((double) numImages / numColumns);
        imagePanel.setLayout(new GridLayout(numRows, numColumns));
        imagePanel.setPreferredSize(new Dimension(numColumns * 100, numRows * 100));

        // Set the scrollbar policies
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane.setViewportView(imagePanel);
        MainUI.rootFrame.add(scrollPane);
        scrollPane.setBounds(160,500,600,300);
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
        
    }
}
