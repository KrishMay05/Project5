import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class BlankServerThread extends Thread {
    private Socket clientSocket;
    private Database users;
    private BufferedReader br;
    private PrintWriter pw;


    public BlankServerThread(Socket clientSocket, Database users) {
        this.clientSocket = clientSocket;
        this.users = users;
    }
    public void run() {
        System.out.println("Server Thread is running");

        try {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pw = new PrintWriter(clientSocket.getOutputStream());
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line); //reads "hello server" currently
                if (line.contains("EXIT")) {
                    exit(line);
                    return;
                }
                if (line.contains("LOGIN")) {
                    login(line);
                }
                if (line.contains("SIGNUP")) {
                    signup(line);
                }
                if (line.contains("SEARCHUSER")) {

                }
                if (line.contains("SENDMESSAGE")) {

                }
                if (line.contains("BLOCK")) {

                }
                if (line.contains("MANAGESEND")) {

                }
                if (line.contains("MANAGEEDIT")) {

                }
                if (line.contains("MANAGEDELETE")) {

                }
                if (line.contains("MANAGEREAD")) {

                }
                if (line.contains("MANAGEEXPORT")) {

                }
                if (line.contains("MANAGEIMPORT")) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            
    }

    private void signup(String line) {
        System.out.println("Signup");
        try {
            String userType = line; //reads "Consumer" or "Producer" and name/password
            String[] userInfo = userType.split(" ");
            System.out.println(Arrays.toString(userInfo));
            if (userInfo[2].contains("Consumer")) {
                users.add(new Consumer(userInfo[1], userInfo[2]));
            } else {
                users.add(new Seller(userInfo[1], userInfo[2],
                    new ArrayList<>(Arrays.asList(Arrays.copyOfRange(userInfo, 3, userInfo.length - 1 )))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login(String line) {
        System.out.println("Login");
        try {
            String username = line.split(" ")[1];
            String password = line.split(" ")[2];
            boolean a = false;
            for (User user : users) {
                if (password.equals(user.getPassword()) && username.equals(user.getName())) {
                    a = true;
                }
            }
            if (a) {
                pw.println("True");
                pw.flush();
            } else {
                pw.println("False");
                pw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exit(String line) {
        try {
            clientSocket.close();
            br.close();
            pw.close();
            if (line.contains("DELETE")) {
                for (User alpha: users) {
                    if (alpha instanceof Seller) {
                        ((Seller)alpha).deleteAllFiles();
                    } else {
                        ((Consumer)alpha).deleteAllFiles();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
