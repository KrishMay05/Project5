import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * User
 *
 * This is a class that will run all of our console GUI prompts
 *
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Krish Patel, Josh Rubow, Aun Ali, Hersh Tripathi
 * @version Nov 12 23
 */

public class GUI {
    public static boolean a = true;
    public static Database users = new Database();
    public static Scanner scan = new Scanner(System.in);


    // Calls all essential functions and runs the GUI
    public static void main(String[] args) {
        System.out.println("Welcome to BLANK!");
        updateUsers();
        while (a) {
            dialog();
        }
    }


    /**
     * Reads from all User files and creates adds a list of Users to the database
     */
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

    /**
     * Prompts user for dialogue on whether they want to sign up or login
     */
    public static void dialog() {

        String connect = "";
        do {
            System.out.println("\nDo you want access? (Y/N)");
            connect = scan.nextLine();

            if (!(connect.toLowerCase().equals("y") || connect.toLowerCase().equals("yes") ||
                    connect.toLowerCase().equals("n") || connect.toLowerCase().equals("no"))) {
                System.out.println("Invalid input, try again!");
            }
        } while (!(connect.toLowerCase().equals("y") || connect.toLowerCase().equals("yes") ||
                connect.toLowerCase().equals("n") || connect.toLowerCase().equals("no")));

        if (connect.toLowerCase().equals("y") || connect.toLowerCase().equals("yes")) {
            String lors = "";
            do {
                System.out.println("Login or Signup? (L/S)");
                lors = scan.nextLine();

                if (!(lors.toLowerCase().equals("s") || lors.toLowerCase().equals("l"))) {
                    System.out.println("Invalid input, try again!");
                }

            } while (!(lors.toLowerCase().equals("s") || lors.toLowerCase().equals("l")));

            if (lors.toLowerCase().equals("l")) {
                String username = "";
                String password = "";
                User loggedIn = null;
                do {
                    System.out.println("Please enter a username:");
                    username = scan.nextLine();

                    System.out.println("Please enter a password:");
                    password = scan.nextLine();

                    loggedIn = login(username, password);
                    if (loggedIn != null) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Login failed, try again!");
                        return;
                    }
                } while (loggedIn == null);

                if (loggedIn instanceof Consumer) {
                    // consumerDialog((Consumer) loggedIn);
                    consumerMenu((Consumer) loggedIn);
                } else if (loggedIn instanceof Seller) {
                    sellerMenu((Seller) loggedIn);
                }

            } else {
                String type = "";
                do {
                    System.out.println("Are you a consumer or a seller? (C/S)");
                    type = scan.nextLine();

                    if (!(type.toLowerCase().equals("c") || type.toLowerCase().equals("s"))) {
                        System.out.println("Invalid input, try again!");
                    }
                } while (!(type.toLowerCase().equals("c") || type.toLowerCase().equals("s")));

                if (type.toLowerCase().equals("c")) {
                    String username = "";
                    String password = "";
                    boolean consumerUserIsEmail = false;
                    do {
                        System.out.println("Please enter a Email as a username:");
                        username = scan.nextLine();

                        System.out.println("Please enter a password:");
                        password = scan.nextLine();

                        if (!users.isUniqueUsername(username)) {
                            System.out.println("Username already taken, try again!");
                        }
                        if (!username.contains("@")) {
                            System.out.println("Username must be your Email, try again!");
                            consumerUserIsEmail = false;
                        } else {
                            consumerUserIsEmail = true;
                        }
                        if (password.isEmpty()) {
                            System.out.println("Password cannot be empty, try again");
                        }
                    } while (!(users.isUniqueUsername(username) && consumerUserIsEmail) || (password.isEmpty()));

                    Consumer consumer = consumerSignUp(username, password);

                    if (consumer != null) {
                        System.out.println("Signup successful!");
                    } else {
                        System.out.println("Signup failed, try again!");
                    }


                } else {
                    String username = "";
                    String password = "";
                    ArrayList<String> stores = new ArrayList<>();
                    boolean sellerUserIsEmail = false;
                    do {
                        System.out.println("Please enter a username:");
                        username = scan.nextLine();

                        System.out.println("Please enter a password:");
                        password = scan.nextLine();

                        boolean invalidNumStores = true;
                        while (invalidNumStores) {
                            System.out.println("How many stores would you like to add:");
                            try {
                                int numStores = Integer.parseInt(scan.nextLine());
                                for (int i = 0; i < numStores; i++) {
                                    System.out.println("Enter your store (" + (i + 1) + ") name:");
                                    stores.add(scan.nextLine().replace(" ", "_"));
                                }
                                invalidNumStores = false;
                            } catch (Exception e) {
                                System.out.println("Invalid input, try again!");
                                invalidNumStores = true;
                            }
                        }

                        if (!users.isUniqueUsername(username)) {
                            System.out.println("Username already taken, try again!");
                        }
                        if (!username.contains("@")) {
                            System.out.println("Username must be your Email");
                            sellerUserIsEmail = false;
                        } else {
                            sellerUserIsEmail = true;
                        }
                        if (password.isEmpty()) {
                            System.out.println("Must input a password");
                        }
                    } while (!(users.isUniqueUsername(username) && sellerUserIsEmail) || password.isEmpty());

                    Seller seller = sellerSignUp(username, password, stores);

                    if (seller != null) {
                        System.out.println("Signup successful!");
                    } else {
                        System.out.println("Signup failed, try again!");
                    }
                }
            }

        } else {
            a = false;
            System.out.println("Would you like to save your data or delte all written files? (S/D)");
            String delOrSave = scan.nextLine().toLowerCase();
            if (delOrSave.equals("d")) {
                for (User alpha: users) {
                    if (alpha instanceof Seller) {
                        ((Seller)alpha).deleteAllFiles();
                    } else {
                        ((Consumer)alpha).deleteAllFiles();
                    }
                }
            }
            System.out.println("Thank you for using BLANK!");
        }
    }


    /**
     * Prints dialog a Consumer will see once they login
     * @param user
     */
    public static void consumerDialog(Consumer user) {
        // Displays list of stores and allows for messaging
        System.out.println("Here is the list of stores!");
        ArrayList<Seller> copy = new ArrayList<>();
        int counter = 1;
        for (User u : users) {
            if (u instanceof Seller) {
                for (String store : ((Seller) u).getStores()) {
                    System.out.println("(" + counter + ") " + store);
                    copy.add((Seller) u);
                    counter++;
                }

            }
        }
        if (!copy.isEmpty()) {
            int recipient = -1;
            do {

                while ( recipient == -1 ) {
                    try {
                        System.out.println("Who would you like to message?");
                        recipient = Integer.parseInt(scan.nextLine());
                    } catch (Exception e) {
                        System.out.println("Not valid input");
                    }
                }

                if (recipient < 1 || recipient > copy.size()) {
                    System.out.println("Invalid input, try again!");
                } else {
                    System.out.println("You are messaging " + copy.get(recipient - 1).sF());
                }
            } while (recipient < 1 || recipient > copy.size());


            if (user.canMessage(user.cF(), copy.get(recipient - 1).getName())) {
                System.out.println("What would you like to say?");
                String message = scan.nextLine();
                System.out.println("Message sent!");
                user.sendMessage(copy.get(recipient - 1).sF(), message, copy.get(recipient - 1).getName());
            } else {
                System.out.println("You cannot message this user!");
            }
        } else {
            System.out.println("No Open Messages.");
        }
    }

    /**
     * Prints dialog a seller will see once they login
     * @param user
     */
    public static void sellerDialog(Seller user) {
        // Creates list of consumers
        System.out.println("Here is the list of customers!");
        ArrayList<Consumer> consumers = new ArrayList<>();

        // Adds consumers to list of consumers
        int counter = 1;
        for (User u : users) {
            if (u instanceof Consumer) {
                System.out.println("(" + counter + ") " + u.getName());
                counter++;
                consumers.add((Consumer) u);
            }
        }

        // Prompts user for recipient
        if (!consumers.isEmpty()) {
            int recipient = -1;
            do {
                System.out.println("Who would you like to message?");
                recipient = Integer.parseInt(scan.nextLine());
            } while (recipient < 1 || recipient > consumers.size());

            // Prompts user for message
            // Sends message
            if (user.canMessage(user.sF(), consumers.get(recipient - 1).getName())) {
                System.out.println("What would you like to say?");
                String message = scan.nextLine();
                user.sendMessage(consumers.get(recipient - 1).cF(), message, consumers.get(recipient - 1).getName());
                System.out.println("Message sent!");
            } else {
                System.out.println("You cannot message this user!");
            }
        } else {
            System.out.println("No Open Messages");
        }
    }

    public static User login(String name, String password) {
        for (User user : users) {
            if (password.equals(user.getPassword()) && name.equals(user.getName())) {
                return user;
            }
        }

        return null;
    }

    // Method that calls the signup and udpateUsers functions for a seller
    public static Seller sellerSignUp(String name, String password, ArrayList<String> stores) {
        try {
            Seller newSeller = new Seller(name, password, stores);
            updateUsers();
            return newSeller;
        } catch (Exception e) {
            return null;
        }
    }

    // `Method that calls the signup and udpateUsers functions for a consumer
    public static Consumer consumerSignUp(String name, String password) {
        try {
            Consumer newSeller = new Consumer(name, password);
            updateUsers();
            return newSeller;
        } catch (Exception e) {
            return null;
        }
    }


    // Manages key functions for a Consumer
    public static void consumerMenu(Consumer loggedIn) {
        boolean ae = true;
        while (ae) {
            System.out.println("\nWhich of the following would you like to do? \n1. Send a new message.");
            System.out.println("2. Search for a Seller\n3. Block Seller\n4. Manage Conversations\n5. Exit System");
            int option = 0;
            try {
                option = Integer.parseInt(scan.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid Input please try again");
            }
            switch (option) {
                // Send a new Message
                case 1:
                    consumerDialog(loggedIn);
                    break;

                // Search for a seller
                case 2:
                    System.out.println("Enter the name of the user you want to search for:");
                    String searchName = scan.nextLine();

                    ArrayList<User> results = users.search(searchName, loggedIn);
                    if (results.size() == 0) {
                        System.out.println("No results found.");
                    } else {
                        System.out.println("Please select a user:");
                        int counter = 1;
                        for (User result : results) {
                            if (result instanceof Seller) {
                                System.out.println("(" + counter + ")" + result.getName());
                                counter++;
                            }
                        }
                    }

                    // Loops through options to interact with message history
                    boolean choiceBo = true;
                    while (choiceBo && results.size() != 0) {
                            int userNeed = -1;
                            while ( userNeed == -1 ) {
                                try {
                                    System.out.println("From the users listed above, please enter the number " +
                                            "of the user you want to interact with.");
                                    userNeed = Integer.parseInt(scan.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Not valid input");
                                }
                            }
                            int choice = -1;
                            while ( choice == -1 ) {
                                try {
                                    System.out.println("Would you like to Send(1), Edit(2), Delete(3), Read(4), " +
                                            "Export(5), Import(6) a conversation. Or Exit(7)");
                                    choice = Integer.parseInt(scan.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Not valid input");
                                }
                            }
                            scan.nextLine();
                            Seller recSel = (Seller) results.get(userNeed - 1);
                            if (recSel != null) {
                                switch (choice) {
                                    // Send a message
                                    case 1:
                                        System.out.println("What message do you want to send?");
                                        String messageSent = scan.nextLine();
                                        loggedIn.sendMessage(recSel.getName(), messageSent , recSel.name);
                                        choiceBo = false;
                                        break;
                                    // Edit a message
                                    case 2:
                                        //System.out.println("test");
                                        loggedIn.printTextsLineNumbers(recSel.getName());
                                        int messageLine = -1;
                                        while (messageLine == -1) {
                                            try {
                                                System.out.println("What line do you want to edit?");
                                                messageLine = Integer.parseInt(scan.nextLine());
                                            } catch (Exception e) {
                                                System.out.println("Not valid input");
                                            }
                                        }

                                        System.out.println("What do you want to say instead.");
                                        String messageEdit = scan.nextLine();
                                        loggedIn.editMessage(recSel.sF(), messageLine , messageEdit);
                                        choiceBo = false;
                                        break;
                                    // Delete a message
                                    case 3:
                                        loggedIn.printTextsLineNumbers(recSel.getName());
                                        int delLine = -1;
                                        while (delLine == -1) {
                                            try {
                                                System.out.println("What line do you want to delete?");
                                                delLine = Integer.parseInt(scan.nextLine());
                                            } catch (Exception e) {
                                                System.out.println("Not valid input");
                                            }
                                        }
                                        loggedIn.deleteMessage(recSel.sF(), delLine);
                                        choiceBo = false;
                                        break;
                                    // Read a message
                                    case 4:
                                        loggedIn.printTexts(recSel.getName());
                                        choiceBo = false;
                                        break;
                                    case 5:
                                        loggedIn.exportFile(loggedIn.getName(), recSel.getName());
                                        choiceBo = false;
                                        break;

                                    // Import a message
                                    case 6:
                                        System.out.println("What is the address of the file you want " +
                                                "to import and send?");
                                        String importFileName = scan.nextLine();
                                        loggedIn.importFile(recSel.sF(), recSel.getName(), importFileName);
                                        choiceBo = false;
                                        break;
                                    default:
                                        System.out.println("Invalid Input");
                                        break;
                                }
                            } else {
                                System.out.println("Not a valid user!");
                            }
                        }
                    break;

                // Block a seller
                case 3:
                    ArrayList<Seller> blockList = users.printSellersNumbered();
                    System.out.println("From the list above, who would you like to block?");
                    try {
                        int blockedNum = Integer.parseInt(scan.nextLine());
                        loggedIn.addBlockedUser(loggedIn.cF(), blockList.get(blockedNum - 1).getName());
                        System.out.println(blockList.get(blockedNum - 1).getName() + " has been blocked.");
                        updateUsers();
                    } catch (Exception e) {
                        System.out.println("Invalid input please try again.");
                    }
                    break;

                // View current conversations
                case 4:
                    ArrayList<String> messagePeople = loggedIn.printAllMessages();
                    ArrayList<String> sellerList = new ArrayList<>();
                    if (!messagePeople.isEmpty()) {
                        boolean choiceBool = true;
                        int counter = 0;
                        while (choiceBool) {
                            int choice = -1;
                            while (choice == -1) {
                                try {
                                    System.out.println("Would you like to Send(1), Edit(2), Delete(3), Read(4), " +
                                            "Export(5), Import(6) a conversation. Or Exit(7)");
                                    choice = Integer.parseInt(scan.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Not valid input");
                                }
                            }
                            if (messagePeople.size() > 1) {
                                for (String a: messagePeople) {
                                    if (0 != counter) {
                                        if (!messagePeople.get(counter).equals(messagePeople.get(counter - 1))) {
                                            String b = a.replace(".txt", "");
                                            b = b.replace(loggedIn.name,"");
                                            System.out.println((counter + 1) + ". " + b );
                                            sellerList.add(b);
                                            counter++;
                                        }
                                    } else {
                                        String b = a.replace(".txt", "");
                                        b = b.replace(loggedIn.name,"");
                                        System.out.println((counter + 1) + ". " + b );
                                        sellerList.add(b);
                                        counter++;
                                    }
                                }
                            } else {
                                String c = messagePeople.get(0).replace(".txt", "");
                                c = c.replace(loggedIn.name,"");
                                System.out.println("1."  + c );
                                sellerList.add(c);
                            }
                            int userNeed = -1;
                            String recNem = "";
                            while (userNeed == -1) {
                                try {
                                    System.out.println("From the users listed above, please enter the number " +
                                            "of the user you want to interact with.");
                                    userNeed = Integer.parseInt(scan.nextLine());
                                    recNem = sellerList.get(userNeed - 1);
                                } catch (Exception e) {
                                    System.out.println("Not valid input");
                                }
                            }
                            //String recNem = sellerList.get(userNeed - 1);
                            Seller recSel = null;
                            for (User rec: users) {
                                if(rec instanceof Seller) {
                                    if(((Seller)rec).name.equals(recNem)) {
                                        recSel = (Seller) rec;
                                    }
                                }
                            }
                            if (recSel != null) {
                                switch (choice) {
                                    // Send a message
                                    case 1:
                                        System.out.println("What message do you want to send?");
                                        String messageSent = scan.nextLine();
                                        loggedIn.sendMessage(recSel.getName(), messageSent , recSel.name);
                                        choiceBool = false;
                                        break;

                                    // Edit a message
                                    case 2:
                                        loggedIn.printTextsLineNumbers(recSel.getName());
                                        int messageLine = -1;
                                        while (messageLine == -1) {
                                            try {
                                                System.out.println("What line do you want to edit?");
                                                messageLine = Integer.parseInt(scan.nextLine());
                                            } catch (Exception e) {
                                                System.out.println("Not valid input");
                                            }
                                        }
                                        System.out.println("What do you want to say instead.");
                                        String messageEdit = scan.nextLine();
                                        loggedIn.editMessage(recSel.getName(), messageLine , messageEdit);
                                        choiceBool = false;
                                        break;

                                    // Delete a message
                                    case 3:
                                        loggedIn.printTextsLineNumbers(recSel.getName());
                                        int delLine = -1;
                                        while (delLine == -1) {
                                            try {
                                                System.out.println("What line do you want to delete?");
                                                delLine = Integer.parseInt(scan.nextLine());
                                            } catch (Exception e) {
                                                System.out.println("Not valid input");
                                            }
                                        }
                                        loggedIn.deleteMessage(recSel.getName(), delLine);
                                        choiceBool = false;
                                        break;

                                    // Read a message
                                    case 4:
                                        loggedIn.printTexts(recSel.getName());
                                        choiceBool = false;
                                        break;

                                    // Export a message
                                    case 5:
                                        loggedIn.exportFile(loggedIn.getName(), recSel.getName());
                                        choiceBool = false;
                                        break;

                                    // Import a message
                                    case 6:
                                        System.out.println("What is the address of the file you want to import " +
                                                "and send?");
                                        String importFileName = scan.nextLine();
                                        loggedIn.importFile(recSel.sF(), recSel.getName(), importFileName);
                                        choiceBool = false;
                                        break;

                                    // Exit
                                    case 7:
                                        System.out.println("Exiting");
                                        choiceBool = false;
                                        break;
                                    default:
                                        System.out.println("Invalid Input");
                                        break;
                                }
                            } else {
                                System.out.println("Not a valid user!");
                            }
                        }
                        break;
                    } else {
                        System.out.println("No current messages");
                    }
                    case 5:
                        ae = false;
                        break;
                    default:
                        break;
            }
        }

    }

    // Manages key functions for a Seller
    public static void sellerMenu(Seller loggedIn) {
        boolean ae = true;
        while (ae) {
            int option = -1;
            while (option == -1) {
                try {
                    System.out.println("\nWhich of the following would you like to do? \n1. Send a new message.");
                    System.out.println("2. Search for a Consumer\n3. Block Consumer\n4. Manage " +
                            "Conversations\n5. Exit System");
                    option = Integer.parseInt(scan.nextLine());
                } catch (Exception e) {
                    System.out.println("Not valid input");
                }
            }
            switch (option) {
                // Send a new Message
                case 1:
                    sellerDialog(loggedIn);
                    break;

                // Search for a seller
                case 2:
                    System.out.println("Enter the name of the user you want to search for:");
                    String searchName = scan.nextLine();

                    ArrayList<User> results = users.search(searchName, loggedIn);
                    if (results.size() == 0) {
                        System.out.println("No results found.");
                    } else {
                        System.out.println("Please select a user:");
                        int counter = 1;
                        for (User result : results) {
                            if (result instanceof Consumer) {
                                System.out.println("(" + counter + ")" + result.getName());
                                counter++;
                            }
                        }
                    }
                    boolean choiceBo = true;
                    while (choiceBo && !results.isEmpty()) {
                            int userNeed = -1;
                            while (userNeed == -1) {
                                try {
                                    System.out.println("From the users listed above, please enter the number of " +
                                            "the user you want to interact with.");
                                    userNeed = Integer.parseInt(scan.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Not valid input");
                                }
                            }
                            int choice = -1;
                            while (choice == -1) {
                                try {
                                    System.out.println("Would you like to Send(1), Edit(2), Delete(3), Read(4), " +
                                            "Export(5), Import(6) a conversation. Or Exit(7)");
                                    choice = Integer.parseInt(scan.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Not valid input");
                                }
                            }
                            Consumer recCon = (Consumer) results.get(userNeed - 1);
                            if (recCon != null) {
                                switch (choice) {
                                    // Send a message
                                    case 1:
                                        System.out.println("What message do you want to send?");
                                        String messageSent = scan.nextLine();
                                        loggedIn.sendMessage(recCon.getName(), messageSent , recCon.getName());
                                        choiceBo = false;
                                        break;
                                    // Edit a message
                                    case 2:
                                        //System.out.println("test");
                                        loggedIn.printTextsLineNumbers(recCon.getName());
                                        int messageLine = -1;
                                        while (messageLine == -1) {
                                            try {
                                                System.out.println("What line do you want to edit?");
                                                messageLine = Integer.parseInt(scan.nextLine());
                                            } catch (Exception e) {
                                                System.out.println("Not valid input");
                                            }
                                        }
                                        System.out.println("What do you want to say instead.");
                                        String messageEdit = scan.nextLine();
                                        loggedIn.editMessage(recCon.cF(), messageLine , messageEdit);
                                        choiceBo = false;
                                        break;
                                    // Delete a message
                                    case 3:
                                        loggedIn.printTextsLineNumbers(recCon.getName());
                                        int delLine = -1;
                                        while (delLine == -1) {
                                            try {
                                                System.out.println("What line do you want to delete?");
                                                delLine = Integer.parseInt(scan.nextLine());
                                            } catch (Exception e) {
                                                System.out.println("Not valid input");
                                            }
                                        }
                                        loggedIn.deleteMessage(recCon.cF(), delLine);
                                        choiceBo = false;
                                        break;

                                    // Read a message
                                    case 4:
                                        loggedIn.printTextsLineNumbers(recCon.getName());
                                        choiceBo = false;
                                        break;

                                    // Export a message
                                    case 5:
                                        loggedIn.exportFile(loggedIn.getName(), recCon.getName());
                                        choiceBo = false;
                                        break;

                                    // Import a message
                                    case 6:
                                        System.out.println("What is the address of the file you want to import " +
                                                "and send?");
                                        String importFileName = scan.nextLine();
                                        loggedIn.importFile(recCon.cF(), recCon.getName(), importFileName);
                                        choiceBo = false;
                                        break;
                                    case 7:
                                        choiceBo = false;
                                        break;
                                    default:
                                        System.out.println("Invalid Input");
                                        break;
                                }
                            } else {
                                System.out.println("Not a valid user!");
                            }
                        }
                    break;

                // Block a seller
                case 3:
                    ArrayList<Consumer> blockList = users.printConsumersNumbered();
                    System.out.println("From the list above, who would you like to block?");
                    try {
                        int blockedNum = Integer.parseInt(scan.nextLine());
                        loggedIn.addBlockedUser(loggedIn.sF(), blockList.get(blockedNum - 1).getName());
                        System.out.println(blockList.get(blockedNum - 1).getName() + " has been blocked.");
                        updateUsers();
                    } catch (Exception e) {
                        System.out.println("Invalid input please try again.");
                    }
                    break;

                // View current conversations
                case 4:
                    ArrayList<String> messagePeople = loggedIn.printAllMessages();
                    ArrayList<String> sellerList = new ArrayList<>();
                    if (!messagePeople.isEmpty()) {
                        boolean choiceBool = true;
                        int counter = 0;
                        while (choiceBool) {
                            int choice = -1;
                            while (choice == -1) {
                                try {
                                    System.out.println("Would you like to Send(1), Edit(2), Delete(3), " +
                                            "Read(4), Export(5), Import(6) a conversation. Or Exit(7)");
                                    choice = Integer.parseInt(scan.nextLine());
                                } catch (Exception e) {
                                    System.out.println("Not valid input");
                                }
                            }
                            if (messagePeople.size() > 1) {
                                for (String a: messagePeople) {
                                    if (0 != counter) {
                                        if (!messagePeople.get(counter).equals(messagePeople.get(counter - 1))) {
                                            String b = a.replace(".txt", "");
                                            b = b.replace(loggedIn.name,"");
                                            System.out.println((counter + 1) + ". " + b );
                                            sellerList.add(b);
                                            counter++;
                                        }
                                    } else {
                                        String b = a.replace(".txt", "");
                                        b = b.replace(loggedIn.name,"");
                                        System.out.println((counter + 1) + ". " + b );
                                        sellerList.add(b);
                                        counter++;
                                    }
                                }
                            } else {
                                String c = messagePeople.get(0).replace(".txt", "");
                                c = c.replace(loggedIn.name,"");
                                System.out.println("1."  + c );
                                sellerList.add(c);
                            }
                            int userNeed = -1;
                            String recNem = "";
                            while (userNeed == -1) {
                                try {
                                    System.out.println("From the users listed above, please enter the " +
                                            "number of the user you want to interact with.");
                                    userNeed = Integer.parseInt(scan.nextLine());
                                    recNem = sellerList.get(userNeed - 1);
                                } catch (Exception e) {
                                    System.out.println("Not valid input");
                                }
                            }
                            //String recNem = sellerList.get(userNeed - 1);
                            Consumer recCon = null;
                            for (User rec: users) {
                                if(rec instanceof Consumer) {
                                    if(((Consumer) rec).getName().equals(recNem)) {
                                        recCon = (Consumer) rec;
                                    }
                                }
                            }
                            if (recCon != null) {
                                switch (choice) {
                                    // Send a message
                                    case 1:
                                        System.out.println("What message do you want to send?");
                                        String messageSent = scan.nextLine();
                                        loggedIn.sendMessage(recCon.cF(), messageSent , recCon.getName());
                                        choiceBool = false;
                                        break;

                                    //  Edit a message
                                    case 2:
                                        loggedIn.printTextsLineNumbers(recCon.getName());
                                        int messageLine = -1;
                                        while (messageLine == -1) {
                                            try {
                                                System.out.println("What line do you want to edit?");
                                                messageLine = Integer.parseInt(scan.nextLine());
                                            } catch (Exception e) {
                                                System.out.println("Not valid input");
                                            }
                                        }
                                        System.out.println("What do you want to say instead.");
                                        String messageEdit = scan.nextLine();
                                        loggedIn.editMessage(recCon.getName(), messageLine , messageEdit);
                                        choiceBool = false;
                                        break;

                                    // Delete a message
                                    case 3:
                                        loggedIn.printTextsLineNumbers(recCon.getName());
                                        int delLine = -1;
                                        while (delLine == -1) {
                                            try {
                                                System.out.println("What line do you want to delete?");
                                                delLine = Integer.parseInt(scan.nextLine());
                                            } catch (Exception e) {
                                                System.out.println("Not valid input");
                                            }
                                        }
                                        scan.nextLine();
                                        loggedIn.deleteMessage(recCon.cF(), delLine);
                                        choiceBool = false;
                                        break;

                                    // Read a message
                                    case 4:
                                        loggedIn.printTextsLineNumbers(recCon.getName());
                                        choiceBool = false;
                                        break;
                                    case 5:
                                        loggedIn.exportFile(loggedIn.getName(), recCon.getName());
                                        choiceBo = false;
                                        break;

                                    // Import a message
                                    case 6:
                                        System.out.println("What is the address of the file you " +
                                                "want to import and send?");
                                        String importFileName = scan.nextLine();
                                        loggedIn.importFile(recCon.cF(), recCon.getName(), importFileName);
                                        choiceBo = false;
                                        break;
                                    case 7:
                                        choiceBo = false;
                                        break;
                                    default:
                                        System.out.println("Invalid Input");
                                        break;
                                }
                            } else {
                                System.out.println("Not a valid user!");
                            }
                        }
                        break;
                    } else {
                        System.out.println("No current messages");
                    }
                    case 5:
                        ae = false;
                        break;
                    default:
                        break;
            }
        }

    }


}
