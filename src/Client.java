import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;

/**
 * Sends file paths to server launched by ObjectDetection.py and receives corresponding tags from ML models.
 */
public class Client {

    public Socket socket; 
    public DataOutputStream out; 
    public DataInputStream in ; 


    /**
     * Encodes file path of added image/video, sends file path to server, 
     * and converts the received string of corresponding file tags to a string.
     * @param path
     * @return
     */
    public List<String> sendFilePath(String path){
        
        createConnection();
        if(socket!=null){
            try {
                out.writeUTF(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String resp = null; 
        try {
            resp = receiveResponse();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(resp.split(","));
    }

    /**
     * Receives list of tags for a image/video as an encoded string from server.
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String receiveResponse() throws IOException, ClassNotFoundException {
        
        String response = null;
        try {
            byte[] bytes = new byte[1024];
            int count = in.read(bytes);
            response = new String(bytes, 0, count, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
        socket = null;
        closeConnection();
        return response;
       
    }

    /**
     * Creates connection to server launched by ObjectDetection.py.
     * Constructs input-output stream at server address.
     */
    public void createConnection(){
        
        if(socket == null){
            boolean isConnected = false;
            while (!isConnected) {
                try {
                    socket = new Socket("localhost", 5000);
                    out = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());
                    isConnected = true; // if successful, set isConnected to true to exit the loop
                } catch (UnknownHostException e) {
                    System.err.println("Unknown host. Please check your connection settings.");
                } catch (IOException e) {
                    System.err.println("Establishing connection with local server. Please wait...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } 
                }
            }
        }else{
            while (!socket.isConnected()) {
                try {
                    socket = new Socket("localhost", 5000);
                    out = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());
                } catch (UnknownHostException e) {
                    System.err.println("Unknown host. Please check your connection settings.");
                } catch (IOException e) {
                    System.err.println("Unable to establish connection. Retrying in 5 seconds...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } 
                }
            }
        }
            
        
    }

    /**
     *  Closes input-output stream between Client.java and server.
     */
    public void closeConnection(){
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Runs ObjectDetection.py to launch server for generating image/video tags.
     */
    public void startServer(){
       
        if (FileManagement.getOS().contains("Windows")){
            String command2 = "python ."+ MainUI.filemanager.sep+"src"+MainUI.filemanager.sep+"ObjectDetection.py ."+FileManagement.getDocPath();
            System.out.println(command2);
            try {
            
                Runtime.getRuntime().exec(command2);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            String command2 = "python3 ."+ MainUI.filemanager.sep+"src"+MainUI.filemanager.sep+"ObjectDetection.py ."+FileManagement.getDocPath();
            System.out.println(command2);
            try {
            
                Runtime.getRuntime().exec(command2);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

    }
}