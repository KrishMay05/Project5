import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class BlankServer {
    public static boolean a = true;
    public static Database users = new Database();
    public static void main(String[] args) {
        int portNumber = 2020; 
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
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
                String line;
                Socket clientSocket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
                line = br.readLine();
                System.out.println(line); //reads "hello server" currently
                if (line.contains("EXIT")) {
                    clientSocket.close();
                    serverSocket.close();
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
                    return;
                }
                if (line.contains("LOGIN")) {
                    String userInfo[] = line.split(" ");
                    boolean a = false;
                    for (User user : users) {
                        if (userInfo[2].equals(user.getPassword()) && userInfo[1].equals(user.getName())) {
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
                }
                if (line.contains("SIGNUP")) {
                    String userType = br.readLine(); //reads "Consumer" or "Producer" and name/password
                    String[] userInfo = userType.split(" ");
                    System.out.println(Arrays.toString(userInfo));
                    if (userInfo[2].contains("Consumer")) {
                        users.add(new Consumer(userInfo[1], userInfo[2]));
                    } else {
                        users.add(new Seller(userInfo[1], userInfo[2],
                            new ArrayList<>(Arrays.asList(Arrays.copyOfRange(userInfo, 3, userInfo.length - 1 )))));
                    }
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
            // JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void updateUsers() {
        users.reset();
        ArrayList<String> userFileNames = new ArrayList<>();
        /**
         * Creates arraylist of userFileNames
         */
        try (BufferedReader bfr = new BufferedReader(new FileReader("users5.txt"))) {
            String line = bfr.readLine();
            while (line != null) {
                userFileNames.add(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Issue reading file");
        }

        for (int i = 0; i < userFileNames.size(); i++) {
            String fileName = userFileNames.get(i);
            try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
                String line = bfr.readLine();
                int lineNum = 0;
                String name = "";
                String password = "";
                ArrayList<String> blockedUsers = new ArrayList<>();
                String rawStores = "";
                if (fileName.contains("Consumer")) {
                    while (line != null) {
                        switch (lineNum) {
                            case 0:
                                name = line;
                                break;
                            case 1:
                                password = line;
                                break;
                            case 2:
                                for (String blockedUser : line.split(" ")) {
                                    if (blockedUser.contains("Producer") && blockedUser.contains(".txt")) {
                                        blockedUsers.add(blockedUser.split("Producer")[1].split(".txt")
                                                [0]);
                                    }
                                }

                        }
                        lineNum++;
                        line = bfr.readLine();
                    }
                    users.add(new Consumer(name, password, blockedUsers, true));
                } else if (fileName.contains("Producer")) {
                    ArrayList<String> stores = new ArrayList<>();
                    while (line != null) {
                        switch (lineNum) {
                            case 0:
                                name = line;
                                break;
                            case 1:
                                password = line;
                                break;
                            case 2:
                                for (String blockedUser : line.split(" ")) {
                                    if (blockedUser.contains("Consumer") && blockedUser.contains(".txt")) {
                                        blockedUsers.add(blockedUser.split("Consumer")[1].split(".txt")[0]
                                        );
                                    }
                                }
                                break;
                            case 3:
                                rawStores = line;
                                String[] storesArray = rawStores.split(" ");
                                for (int j = 0; j < storesArray.length; j++) {
                                    stores.add(j, storesArray[j]);
                                }
                                break;
                        }
                        lineNum++;
                        line = bfr.readLine();
                    }
                    users.add(new Seller(name, password, stores, blockedUsers, true));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}