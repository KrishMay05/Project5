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
    private User loggedUser; // User that is logged in


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
                if (line.contains("SEARCH")) {
                    search(line);
                }
                if (line.contains("SENDMESSAGE")) {
                    // SENDMESSAGE Receiver Sender Message
                    // if the messaging user is a consumer, print all stores
                    if (line.contains("Consumer")) {
                        for (User alpha: users) {
                            if (alpha instanceof Seller) {
                                String information[] = line.split(" ");
                                System.out.println("THIS IS NO. @ " + information[2]);
                                // if (((Seller) alpha).getStores().contains(line))
                            }
                        }
                    } else {
                        // if the messaging user is a seller, print all consumers
                        for (User alpha: users) {
                            if (alpha instanceof Consumer) {
                                // pw.println(alpha.getName());
                                // pw.flush();
                            }
                        }
                    }
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
                    // MANAGEIMPORT Receiver Sender File
                    String[] info = line.split(" ");
                    String receiver = info[1];
                    String sender = info[2];
                    String file = info[3];

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
                users.add(new Consumer(userInfo[0].replaceAll("SIGNUP", ""), userInfo[1]));
            } else {
                users.add(new Seller(userInfo[0].replaceAll("SIGNUP", ""), userInfo[1],
                    new ArrayList<>(Arrays.asList(Arrays.copyOfRange(userInfo, 3, userInfo.length)))));
                    System.out.println(new ArrayList<>(Arrays.asList(Arrays.copyOfRange(userInfo, 3, userInfo.length ))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search(String line ) {
        String[] split = line.split(" ");
        String results = "";
        for (User u : users) {
            if (u.getName().equals(split[1])) {
                for (User us : users.search(split[2], u)) {
                    results += us.getName() + " ";
                }
            }
        }
        System.out.println("TEST");
        System.out.println(results);
        pw.print(results);
        pw.println();

        
    }

    private void login(String line) {
        System.out.println("Login");
        System.out.println(line);
        String consumers = "";
        String sellers = ""; 
        try {
            String username = line.split(" ")[1];
            String password = line.split(" ")[2];
            System.out.println("username is {" + username + "}");
            System.out.println("password is {" + password + "}");
            boolean a = false;
            for (User user : users) {
                if (password.equals(user.getPassword()) && username.equals(user.getName())) {
                    a = true;
                    if (user instanceof Consumer) {
                        loggedUser = (Consumer) user;
                    } else {
                        loggedUser = (Seller) user;
                    }
                } else {
                    if (user instanceof Consumer) {
                        consumers += " " + user.getName();
                    } else {
                        Seller temp = (Seller) user;
                        for (String d: temp.getStores()) {
                            sellers += " " + d;
                        }
                    }
                }
            }
            System.out.println();
            System.out.println(consumers);
            System.out.println(sellers);
            if (a) {
                if (loggedUser instanceof Consumer) {
                    pw.println("True" + sellers);
                    pw.flush();
                } else {
                    pw.println("True" + consumers);
                    pw.flush();
                }
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