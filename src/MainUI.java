import java.awt.*;  
import java.awt.event.*; 
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.lang.reflect.Field;

/**
 * Contains the core user interface of the Local Image and Video Search Engine (LIVSE).
 * It launches the socket server on startup and attempts to install required python packages.
 * Once it is connected, you will be able to add images and videos to the application.
 * These files will then be parsed by the pre-trained Machine Learning object detection models and categorized.
 */
public class MainUI implements ActionListener{  

    //Main Frame 
    private String searchMode = "Tags";
    private Map<String, JComponent> mapper = new HashMap<String, JComponent>();
    public static JFrame rootFrame = new JFrame("Image Genie");
    private String imagePath;

    //Menu Bar 
    private JMenu menu;
    private JMenuBar menuBar; 
    private JMenuItem help; 
    private JMenuItem settings;

    //Search Fields and options 
    private JTextField tagField;
    private JTextField userField;
    private JTextField dateField;
    private JTextField nameField;
    private JComboBox resolution; 
    private JComboBox licenseType; 
    private JComboBox cb;
    private JButton chooseImOrVidButton;
    private JLabel imgPreview;
    private ImageIcon imgIcon;
    private JButton addPictureOrVideoButton;
    private JComponent currentActive;

    //SearchButton 
    private JButton searchButton; 
    private JButton looseSearchButton;

    //Get All ordered, add folder, choose your own model option 
    private JButton getAllOrderedButton;
    private JButton addFolderOption;
    private JButton saveStateButton;
    private JButton lightMode;
    private JButton getOperationsPerformedButton;



    //Temporary data structures 
    private GenHashMap tagMap= new GenHashMap();
    private GenHashMap userMap = new GenHashMap();
    private GenHashMap nameMap = new GenHashMap();
    private GenHashMap licenseMap = new GenHashMap();
    private GenHashMap resoMap = new GenHashMap();
    private DateTreeMap datemap = new DateTreeMap();
    private Field tagPubField;
    private Field namePubField;
    private Field userPubField;
    private Field licensePubField;
    private Field ResoPubField;
    
    public static final Font largeFont = new Font("SansSerif", Font.BOLD, 30);
    public static final Font mediumFont = new Font("SansSerif", Font.PLAIN, 20);
    public static final Font smallFont = new Font("SansSerif", Font.PLAIN, 10);

    public static Client javClient;
    public static FileManagement filemanager;

    //test purpose 
    public static Serializer serializationManager;
    public static List<OperationStats>operList = new LinkedList<OperationStats>();


    /**
     * Creates a new MainUI.
     */
    MainUI() {  

        //Setup - Server, client initializers
        filemanager = new FileManagement();
        FileManagement.ensureAppDocFolderExists();
        serializationManager = new Serializer();
        if(serializationManager.checkIfSerializedExists()){
            loadSavedState();
            System.out.println("Serialized Objects Exist");
        }


        JOptionPane serverLoadingMes = new JOptionPane("Connecting to Local Server. Please wait.", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = serverLoadingMes.createDialog(rootFrame, "Please Wait");
        Thread serverMessageThread = new Thread(new Runnable() {
            public void run() {
                dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                dialog.setModal(true);
                dialog.setVisible(true);
            }
        });
        serverMessageThread.start();
        
        javClient = new Client(); 
        javClient.startServer();
        javClient.createConnection();
        serverMessageThread.interrupt();
        dialog.dispose();
        
        initFields();
        rootFrame.setSize(900,900); 
        
        //MenuBar
        menuBar = new JMenuBar(); 
        menu = new JMenu("Menu");
        help = new JMenuItem("Help");
        settings = new JMenuItem("Settings");
        menuBar.add(menu);
        menu.add(help);
        menu.add(settings);

        //mapper and respective Jcomponents
        tagField = new JTextField("Enter tags in the format tag1,tag2 ...");
        userField = new JTextField("Enter username in the format user1,user2 ...");
        dateField = new JTextField("Enter date in the formamt mm-dd-yyyy");
        nameField = new JTextField("Enter the name of the image ...");
        String resolutionOptions[] = {"Low","Medium", "High"};
        resolution = new JComboBox(resolutionOptions);
        String licenseOptions[] = {"Free","CreativeCommon","NotUsable"};
        licenseType = new JComboBox(licenseOptions);
        chooseImOrVidButton = new JButton("Choose Image/Video");

        imgIcon = null; 
        imgPreview = new JLabel(imgIcon);
        chooseImOrVidButton.addActionListener(this);
        mapper.put("Tags",tagField);
        mapper.put("User",userField);
        mapper.put("Date added", dateField);
        mapper.put("License", licenseType);
        mapper.put("Resolution",resolution);
        mapper.put("Image/Video",chooseImOrVidButton);
        mapper.put("Name", nameField);
        imagePath = null;

        //Search Area
        currentActive = tagField;
    
        String searchOptions[]={"Tags","Image/Video","Name","User","Date added","License","Resolution"};  

        cb=new JComboBox(searchOptions);    
        cb.setBounds(465, 150,150,30); 
        cb.addActionListener(this);
        cb.setFont(mediumFont);

        // Default frame
        rootFrame.add(currentActive);
        currentActive.setBounds(195,220,400,40);
        currentActive.setFont(mediumFont);
        
        JLabel searchOptionLabel  = new JLabel("Search by : ");
        searchOptionLabel.setBounds(275,140, 300, 40);;
        searchOptionLabel.setFont(largeFont);

        //Search Button 
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchButton.setBounds(600, 220, 150,40);
        searchButton.setFont(mediumFont);
        looseSearchButton = new JButton("Loose Search");
        looseSearchButton.addActionListener(this);
        looseSearchButton.setBounds(600, 270, 180,40);
        looseSearchButton.setFont(mediumFont);

        getOperationsPerformedButton = new JButton("Operations Performed");
        getOperationsPerformedButton.addActionListener(this);
        getOperationsPerformedButton.setBounds(600, 320, 180,40);
        getOperationsPerformedButton.setFont(smallFont);

        //Add Button 
        addPictureOrVideoButton = new JButton("Add Image/Video");
        addPictureOrVideoButton.addActionListener(this);
        addPictureOrVideoButton.setBounds(300,375,150,20);
        
        //Recent Search Label 
        JLabel recentSHistoryLabel = new JLabel("Recent Searches : ");
        recentSHistoryLabel.setFont(largeFont); 
        recentSHistoryLabel.setBounds(150, 450,300,30);

        JLabel moreOptionsLabel = new JLabel("More Options : ");
        moreOptionsLabel.setFont(mediumFont);
        moreOptionsLabel.setBounds(150,370,300,30);

        //Additional settings and options 
        addFolderOption = new JButton("Add Folder");
        addFolderOption.addActionListener(this);
        addFolderOption.setBounds(460,375,100,20);

        getAllOrderedButton = new JButton("Get All Ordered");
        getAllOrderedButton.addActionListener(this);
        getAllOrderedButton.setBounds(360,300,200,30);
        getAllOrderedButton.setFont(mediumFont);

        saveStateButton = new JButton("Save State");
        saveStateButton.addActionListener(this);
        saveStateButton.setBounds(570,375,100,20);

        lightMode = new JButton("Light Mode : Off");
        lightMode.addActionListener(this);
        lightMode.setBounds(680,375,120,20);
        lightMode.setFont(smallFont);


        if(serializationManager.checkIfSerializedExists()){
            SearchHistoryUI.createSearchHistoryUI();
        }

        //Adding everything to the rootFrame
        rootFrame.add(searchButton);
        rootFrame.add(looseSearchButton);
        rootFrame.add(addPictureOrVideoButton);
        rootFrame.setJMenuBar(menuBar);
        rootFrame.add(cb);
        rootFrame.add(addFolderOption);
        rootFrame.add(moreOptionsLabel);
        rootFrame.add(searchOptionLabel);
        rootFrame.add(recentSHistoryLabel);
        rootFrame.add(saveStateButton);
        rootFrame.add(getAllOrderedButton);
        rootFrame.add(lightMode);
        rootFrame.add(getOperationsPerformedButton);
        rootFrame.setLayout(null);  
        rootFrame.setVisible(true);  
    }
    /**
     * Checks if the user has interacted with any UI element and handles their intended function.
     * @param event
     */  
    public void actionPerformed(ActionEvent event){  
        if(event.getSource() == lightMode){
            if(SearchResultsUI.isLightSearch == false){
                lightMode.setText("Light Mode : On");
                SearchResultsUI.isLightSearch = true;
            }else{
                lightMode.setText("Light Mode : Off");
                SearchResultsUI.isLightSearch = false;
            }
        
        }else if(event.getSource() == getOperationsPerformedButton){
            JFrame listFrame = new JFrame("Operation Stats");
            DefaultListModel<String> listModel = new DefaultListModel<>();

            for(OperationStats op : operList){
                listModel.addElement(op.operation + ". Time taken in milliseconds " + (Long.valueOf( op.timeTakenInNanoSeconds)/1000000f));
            }

            JList<String> operationList = new JList<String>(listModel);
            JScrollPane scrollPane = new JScrollPane(operationList);
            listFrame.add(scrollPane);

            listFrame.setSize(400, 400);
            listFrame.setLocationRelativeTo(null);
            listFrame.setVisible(true);

        }else if (event.getSource() == chooseImOrVidButton){
            File f ;
            JFileChooser fc=new JFileChooser();  
            int i=fc.showOpenDialog(rootFrame);    
            if(i==JFileChooser.APPROVE_OPTION){    
                f=fc.getSelectedFile();    
                String filepath=f.getPath();  
                imagePath = filepath; 
                chooseImOrVidButton.setText(f.getName());  
                rootFrame.remove(imgPreview);
                System.out.println(imagePath);
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image image = toolkit.getImage(imagePath);
                image = image.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
                imgPreview = new JLabel(new ImageIcon(image));
                rootFrame.add(imgPreview);
                imgPreview.setBounds(50,200,100,100);
                
            }    
        }else if(event.getSource() == searchButton){
            String searchText = "";
            if (currentActive instanceof JTextField){
                searchText = ((JTextField) currentActive).getText();
            } else if (currentActive instanceof JComboBox){
                searchText = (String) ((JComboBox) currentActive).getSelectedItem();
            } else if ( currentActive instanceof JButton){  
                searchText = imagePath;   
            }
            searchText = processString(searchText);
            Search(searchMode, searchText);
        }else if(event.getSource() == looseSearchButton){
            String searchText = "";
            if (currentActive instanceof JTextField){
                searchText = ((JTextField) currentActive).getText();
            } else if (currentActive instanceof JComboBox){
                searchText = (String) ((JComboBox) currentActive).getSelectedItem();
            } else if ( currentActive instanceof JButton){  
                searchText = imagePath;   
            }
            searchText = processString(searchText);
            looseSearch(searchMode, searchText);
        }else if(event.getSource() == saveStateButton){
            saveState();
            JOptionPane.showMessageDialog(rootFrame,"Saved Current State of Serializable Objects");
        }else if (event.getSource() == addPictureOrVideoButton){
            File f ;
            JFileChooser fc=new JFileChooser();  
            int i=fc.showOpenDialog(rootFrame);    
            if(i==JFileChooser.APPROVE_OPTION){    
                f=fc.getSelectedFile();    
                String fP=f.getPath();
                String fN = f.getName();
                filemanager.addSingleImage(fP, fN);
                System.out.println(fP);
                System.out.println(fN);
                javClient.sendFilePath(fN);
                DummyDataGenerator dummy = new DummyDataGenerator();
                java.util.List<String> imgResTag =  javClient.sendFilePath(fN);
                FileDS newImgDS = new FileDS(fN, fN, imgResTag, dummy.generateUserName(), dummy.generateDateHolder(), dummy.generateLicenseType(), dummy.generateReso());
                addNewFileToModel(newImgDS);
                JOptionPane.showMessageDialog(rootFrame,
                fN + " added now!" + " Tags generated are : " + imgResTag.toString());
            }    
        }else if (event.getSource() == cb){
            String selectedOption = (String) cb.getSelectedItem();
            
            rootFrame.remove(currentActive);
            rootFrame.revalidate();
            rootFrame.repaint();
            searchMode = selectedOption;
            currentActive = mapper.get(selectedOption);
            rootFrame.add(currentActive);
            if(searchMode == "Image/Video"){
                getAllOrderedButton.setEnabled(false);
            }else{
                getAllOrderedButton.setEnabled(true);
            }
            if(searchMode == "Date added"){
                looseSearchButton.setEnabled(false);
            }else{
                looseSearchButton.setEnabled(true);
            }
            currentActive.setBounds(195,220,400,40);
            currentActive.setFont(mediumFont);
            if (currentActive != chooseImOrVidButton){
                try {
                    rootFrame.remove(imgPreview);
                } catch (Exception err) {
                    // not in frame
                }
            }
        }else if(event.getSource() == getAllOrderedButton){
            Object j = null;
            if (searchMode == "Tags"){
                j = tagMap;
            }else if (searchMode == "License"){
                j = licenseMap;
            }else if (searchMode == "Name"){
                j = nameMap;
            }else if (searchMode == "Resolution"){
                j = resoMap;
            }else if (searchMode == "User"){
                j = userMap;
            }else if(searchMode == "Date added"){
                j = datemap;
            }
            
            if (j instanceof GenHashMap){
                long startTime = System.nanoTime();
                List<FileDS> li = ((GenHashMap)j).getAllOrdered();
                if(li.size() > 100){
                     li = ((GenHashMap)j).getAllOrdered().subList(0, 100);
                }
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                operList.add(new OperationStats("Get all Ordered Gen Hash Map", duration));
                SearchResultsUI temp = new SearchResultsUI(li);
            }else if(j instanceof DateTreeMap){
                long startTime = System.nanoTime();
                List<FileDS> li = ((DateTreeMap)j).getAllOrdered().subList(0, 100);
                if(li.size() > 100){
                    li = ((DateTreeMap)j).getAllOrdered().subList(0, 100);
               }
                SearchResultsUI temp = new SearchResultsUI(li);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                operList.add(new OperationStats("Get all Ordered Date Tree map", duration));
            }
        }
    }  
    /**
     * Initializes Field objects of File characteristics (user, name, licenseType, etc.) 
     */
    private void initFields(){        
        try {
            
            userPubField= FileDS.class.getDeclaredField("user");
            namePubField= FileDS.class.getField("name");
            licensePubField= FileDS.class.getField("licenseType");
            tagPubField= FileDS.class.getField("tags");
            ResoPubField= FileDS.class.getField("resolution");

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }  
    }

    /**
     * Manages searches using the GenHashMap get() method and DateTreeMap getImagesAtDate() method based on query selection.
     * @param searchMode
     * @param searchText
     */
    public void Search(String searchMode, String searchText){
        //Call the search algorithm and get the result image path list 
        //Search Algorithm is called here
        java.util.List<FileDS> res = new LinkedList<>();
        String[] userSubmittedattr = searchText.split(",");
        java.util.List<String> userSTList = new ArrayList<>();
        for(String s: userSubmittedattr){
            userSTList.add(s);
        }
        long startTime = System.nanoTime();
        if(searchMode == "Tags"){
            res = tagMap.get(userSTList, tagPubField);
            
        }else if(searchMode == "License"){
            res = licenseMap.get(userSTList, licensePubField);
        }else if (searchMode == "Name"){
            res = nameMap.get(userSTList,namePubField);
        }else if (searchMode == "Resolution"){
            res = resoMap.get(userSTList,ResoPubField);
        }else if(searchMode == "User"){
            res = userMap.get(userSTList, userPubField);
        }else if(searchMode.equals("Date added")){
            try{
                String dateSplitted[]= searchText.split("-");
                DateHolder d = new DateHolder(Integer.parseInt(dateSplitted[0]),Integer.parseInt(dateSplitted[1]), Integer.parseInt(dateSplitted[2]));
                res = datemap.getImagesAtDate(d);
            }catch(Exception e){
                JOptionPane.showMessageDialog(rootFrame,"Invalid input format!");
            }
            
        } else if(searchMode.equals("Image/Video")){
            System.out.println("Seachign for image / video");
            String tempName = "";
            if (imagePath.contains(".mp4")){
                tempName = "sample.mp4";
            }else{
                tempName = "sample.jpg";
            }
            filemanager.addSingleImage(imagePath, tempName);
            
            res = tagMap.get(javClient.sendFilePath(tempName), tagPubField);
            
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        operList.add(new OperationStats("Search", duration));
        
        if(res.size() != 0 ){
            SearchResultsUI temp = new SearchResultsUI(res);
        }else{
            JOptionPane.showMessageDialog(rootFrame,"This tag doesn't exist. Please try another tag.");
        }
        
    }
    /**
     * Manages searches using the GenHashMap getLooseSearch() method based on query selection.
     * @param searchMode
     * @param searchText
     */
    public void looseSearch(String searchMode, String searchText){

        //Call the search algorithm and get the result image path list 
        //Search Algorithm is called here
        java.util.List<FileDS> res = new LinkedList<>();
        String[] userSubmittedattr = searchText.split(",");
        java.util.List<String> userSTList = new ArrayList<>();
        for(String s: userSubmittedattr){
            userSTList.add(s);
        }
        long startTime = System.nanoTime();
        if(searchMode == "Tags"){
            res = tagMap.getLooseSearch(userSTList, tagPubField);
            
        }else if(searchMode == "License"){
            res = licenseMap.getLooseSearch(userSTList, licensePubField);
        }else if (searchMode == "Name"){
            res = nameMap.getLooseSearch(userSTList,namePubField);
        }else if (searchMode == "Resolution"){
            res = resoMap.getLooseSearch(userSTList,ResoPubField);
        }else if(searchMode == "User"){
            res = userMap.getLooseSearch(userSTList, userPubField);
        }
         else if(searchMode.equals("Image/Video")){
            System.out.println("Seachign for image / video");
            String tempName = "";
            if (imagePath.contains(".mp4")){
                tempName = "sample.mp4";
            }else{
                tempName = "sample.jpg";
            }
            filemanager.addSingleImage(imagePath, tempName);
            
            res = tagMap.getLooseSearch(javClient.sendFilePath(tempName), tagPubField);
            
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        operList.add(new OperationStats("Loose Search", duration));
        if(res.size() != 0 ){
            SearchResultsUI temp = new SearchResultsUI(res);
        }else{
            JOptionPane.showMessageDialog(rootFrame,"This tag doesn't exist. Please try another tag.");
        }
        
    }

    /**
     * Creates a list of FileDS objects for testing using DummyDataGenerator.
     * @return
     */
    public java.util.List<FileDS> sampleGenerator2(){
        java.util.List<FileDS> sampleList = new LinkedList<>();
        String csvFile = "data/file_list.csv";
        String csvDelimiter = ",";
        
        try (InputStream inputStream = getClass().getResourceAsStream(csvFile);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] file = line.split(csvDelimiter);
                String name = file[0];
                System.out.println(name);
                
                
                java.util.List<String> tagss = new LinkedList<>();
                tagss = javClient.sendFilePath(name);
                DummyDataGenerator dumdum = new DummyDataGenerator();
                

                if(tagss!=null || !tagss.get(0).equals("Na")){
                    FileDS temp = new FileDS(name + dumdum.generateReso(), name , tagss,dumdum.generateUserName(),dumdum.generateDateHolder(),dumdum.generateLicenseType(),dumdum.generateReso() );
                    addNewFileToModel(temp);
                }
            
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampleList;
    }

    /**
     * Creates a list of FileDS objects for testing using DummyDataGenerator.
     * @return
     */
    public java.util.List<FileDS> sampleGenerator() {
        java.util.List<FileDS> sampleList = new LinkedList<>();
        String csvFile = "data/pokemon.csv";
        String csvDelimiter = ",";
        
        try (InputStream inputStream = getClass().getResourceAsStream(csvFile);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] pokemon = line.split(csvDelimiter);
                String name = processString(pokemon[0]);
                String type1 = pokemon[1];
                String type2 = "";
                
                if (pokemon.length >= 3) {
                    type2 = pokemon[2];
                }
                java.util.List<String> tagss = new LinkedList<>();
                tagss.add(processString(type1));
                if(type2 != "")
                tagss.add(processString(type2)); 
            
                DummyDataGenerator dumdum = new DummyDataGenerator();
                
                FileDS temp = new FileDS(name, name + ".png", tagss,dumdum.generateUserName(),dumdum.generateDateHolder(),dumdum.generateLicenseType(),dumdum.generateReso() );
                addNewFileToModel(temp);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampleList;
    }

    /**
     * Adds a new File to all GenHashMap objects. 
     * @param fiDS
     */
    private void addNewFileToModel(FileDS fiDS){
        tagMap.add(fiDS,tagPubField);
        userMap.add(fiDS, userPubField);
        nameMap.add(fiDS, namePubField);
        licenseMap.add(fiDS, licensePubField);
        resoMap.add(fiDS, ResoPubField);
        datemap.add(fiDS);
    }

    /**
     * Serializes previous user searches into a 'saved' state.
     */
    private void saveState(){
        serializationManager.createSerObj(tagMap, "tagMap.ser");
        serializationManager.createSerObj(userMap, "userNameMap.ser");
        serializationManager.createSerObj(datemap, "dateMap.ser");
        serializationManager.createSerObj(licenseMap, "licenseMap.ser");
        serializationManager.createSerObj(resoMap, "resoMap.ser");
        serializationManager.createSerObj(nameMap, "nameMap.ser");
        serializationManager.createSerObj((Serializable)SearchHistoryUI.searchHistory, "searchHistory.ser");

    }

    /**
     * Loads the previously serialized information.
     */
    private void loadSavedState(){ 
        List<Serializable> serList = serializationManager.getAllSerObj();
        tagMap = (GenHashMap) serList.get(0);
        userMap  = (GenHashMap) serList.get(1);
        datemap = (DateTreeMap) serList.get(2);
        licenseMap = (GenHashMap) serList.get(3);
        resoMap = (GenHashMap) serList.get(4);
        nameMap = (GenHashMap) serList.get(5);
        SearchHistoryUI.searchHistory = (Deque<FileDS>)serList.get(6);
       
    }

    /**
     * Processes an input string by making every character lowercase and replacing whitespaces.
     * @param input
     * @return
     */
    public static String processString(String input) {
        String lowerCaseString = input.toLowerCase();
        String processedString = lowerCaseString.replaceAll("\\s", "");
    
        return processedString;
    }

    public static void main(String[] args) {  
        new MainUI();   
    }  
}  