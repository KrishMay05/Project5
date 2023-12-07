import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        // System.out.println("Server Thread is running");
        updateUsers();
        try {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pw = new PrintWriter(clientSocket.getOutputStream());
            String line;

            while ((line = br.readLine()) != null) {
                // System.out.println(line); //reads "hello server" currently
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
                    int i = 0;
                    if (loggedUser instanceof Seller) {
                        // System.out.println("SELLER");
                        for (User alpha: users) {
                            if (alpha instanceof Seller) {
                                String information[] = line.split(" ");
                                // System.out.println("THIS IS NO. @ " + information[2]);
                                for (User Sel: users) {
                                    if ( Sel instanceof Consumer) {
                                        // System.out.println(((Consumer) Sel).getName());
                                        if (((Consumer) Sel).getName().contains(information[1].
                                            replace(" ", ""))) {
                                            // System.out.println("This seems to work");
                                            if (i == 0) {
                                                // System.out.println("Send Message, Check File");
                                                ((Seller) loggedUser).sendMessage(((Consumer) Sel).cF(), 
                                                    (Arrays.toString(Arrays.copyOfRange( information, 3, 
                                                    information.length  ))).replace("[","").
                                                    replace("]", ""), ((Consumer) Sel).name);
                                                i++;
                                            }
                                        }
                                    }

                                }
                                // pw.println(alpha.getName());
                                // pw.flush();
                            }
                        }
                    } else {
                        // if the messaging user is a seller, print all consumers
                        // System.out.println("Consumer");
                        for (User alpha: users) {
                            if (alpha instanceof Consumer) {
                                String information[] = line.split(" ");
                                // System.out.println("THIS IS NO. @ " + information[1]);
                                for (User Sel: users) {
                                    if ( Sel instanceof Seller) {
                                        if (((Seller) Sel).getStores().contains(information[1]
                                            .replace(" ", ""))) {
                                            if (i == 0) {
                                                ((Consumer) loggedUser).sendMessage(((Seller) Sel).sF(), 
                                                    (Arrays.toString(Arrays.copyOfRange( information, 3, 
                                                    information.length ))).replace("[","").
                                                    replace("]", ""), ((Seller) Sel).name);
                                                i++;
                                            }
                                        }
                                    }

                                }
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
                    System.out.println(line);
                    int i = 0;
                    if (loggedUser instanceof Seller) {
                        // System.out.println("SELLER");
                        for (User alpha: users) {
                            if (alpha instanceof Seller) {
                                String information[] = line.split(" ");
                                // System.out.println("THIS IS NO. @ " + information[2]);
                                for (User Sel: users) {
                                    if ( Sel instanceof Consumer) {
                                        // System.out.println(((Consumer) Sel).getName());
                                        if (((Consumer) Sel).getName().contains(information[1].
                                            replace(" ", ""))) {
                                            // System.out.println("This seems to work");
                                            if (i == 0) {
                                                // System.out.println("Send Message, Check File");
                                                System.out.println(((Seller) loggedUser).printTextsLineNumbers(((Consumer) Sel).cF()).toString());
                                                pw.println(((Seller) loggedUser).printTextsLineNumbers(((Consumer) Sel).cF()).toString());
                                                pw.flush();
                                                i++;
                                            }
                                        }
                                    }

                                }
                                // pw.println(alpha.getName());
                                // pw.flush();
                            }
                        }
                    } else {
                        // if the messaging user is a seller, print all consumers
                        // System.out.println("Consumer");
                        for (User alpha: users) {
                            if (alpha instanceof Consumer) {
                                String information[] = line.split(" ");
                                // System.out.println("THIS IS NO. @ " + information[1]);
                                for (User Sel: users) {
                                    if ( Sel instanceof Seller) {
                                        if (((Seller) Sel).getStores().contains(information[1]
                                            .replace(" ", ""))) {
                                            if (i == 0) {
                                                System.out.println(((Consumer) loggedUser).printTextsLineNumbers(((Seller) Sel).sF()).toString());
                                                pw.println(((Consumer) loggedUser).printTextsLineNumbers(((Seller) Sel).sF()).toString());
                                                pw.flush();
                                                i++;
                                            }
                                        }
                                    }

                                }
                                // pw.println(alpha.getName());
                                // pw.flush();
                            }
                        }
                    }
                }
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
            } catch (Exception e) {
            e.printStackTrace();
        }
            
    }

    private void signup(String line) {
        // System.out.println("Signup");
        try {
            String userType = line; //reads "Consumer" or "Producer" and name/password
            String[] userInfo = userType.split(" ");
            // System.out.println(Arrays.toString(userInfo));
            if (userInfo[2].contains("Consumer")) {
                users.add(new Consumer(userInfo[0].replaceAll("SIGNUP", ""), userInfo[1]));
            } else {
                users.add(new Seller(userInfo[0].replaceAll("SIGNUP", ""), userInfo[1],
                    new ArrayList<>(Arrays.asList(Arrays.copyOfRange(userInfo, 3, userInfo.length)))));
                    // System.out.println(new ArrayList<>(Arrays.asList(Arrays.copyOfRange(userInfo, 3, userInfo.length ))));
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
                for ( User us : users.search(split[2], u)) {
                    results += us.getName() + " ";
                }
            }
        }
        pw.print(results);
        pw.println();
        pw.flush();

        
    }

    private void login(String line) {
        // System.out.println("Login");
        // System.out.println(line);
        String consumers = "";
        String sellers = ""; 
        try {
            String username = line.split(" ")[1];
            String password = line.split(" ")[2];
            // System.out.println("username is {" + username + "}");
            // System.out.println("password is {" + password + "}");
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
            // System.out.println();
            // System.out.println(consumers);
            // System.out.println(sellers);
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

    public void updateUsers() {
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
            // System.out.println("Issue reading file");
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