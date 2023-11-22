import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BlankServer {
    public static void main(String[] args) {
        int portNumber = 2020; 
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
                System.out.println(br.readLine());
                clientSocket.close();
                serverSocket.close();
            }
        } catch (Exception e) {
            // JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
