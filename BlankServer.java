import java.net.ServerSocket;
import java.net.Socket;

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