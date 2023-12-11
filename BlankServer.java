import java.net.ServerSocket;
import java.net.Socket;
/**
 * BlankServer.java
 *
 * This is the class that will be used to run the server. It 
 * will create a server socket and wait for a client to connect.
 * Once a client connects, it will create a new thread for that.
 * 
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Krish Patel, Josh Rubow, Aun Ali, Hersh Tripathi
 * @version December 11, 2023
 */
public class BlankServer {
    public static Database users = new Database();
    public static ServerSocket serverSocket;
    public static void main(String[] args) {
        int portNumber = 2020; 
        try {
            serverSocket = new ServerSocket(portNumber);

            while (true) {
                /*
                 * LOGIN
                 * SIGNUP
                 * SEARCHUSER 
                 * SENDMESSAGE
                 * BLOCK
                 * MANAGESEND 
                 * MANAGEEDIT 
                 * MANAGEDELETE 
                 * MANAGEREAD 
                 * MANAGEEXPORT 
                 * MANAGEIMPORT 
                 * EXIT
                 */
                Socket clientSocket = serverSocket.accept();

                Thread client = new BlankServerThread(clientSocket, users);
                client.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}