import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BlankServer {
    public static boolean a = true;
    public static Database users = new Database();
    public static void main(String[] args) {
        int portNumber = 2020; 
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
                System.out.println(br.readLine());
                // clientSocket.close();
                // serverSocket.close();
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
 (BufferedReader bfr = new BufferedReader(new FileReader("users5.txt"))) {
                       e.printStackTrace();
           