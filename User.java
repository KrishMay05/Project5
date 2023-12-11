import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * User.java
 *
 * This is the genral class that will be extended by both the seller
 * and the consumer, it will have a few genral methods that can be used
 * by both of is child classes
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Krish Patel, Josh Rubow, Aun Ali, Hersh Tripathi
 * @version Nov 12 23
 */

public class User {
    public String name;
    public String password;
    public ArrayList<String> blockedUsers = new ArrayList<>();
    public User(String n, String name, String password) {
        // Constructor that creates and writes a new file with file name of n
        // name is stored on first line of file
        // password is stored on second line of file
        try (FileWriter fw = new FileWriter( n, true)) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("users5.txt", true));
            bw.append( n + "\n");
            bw.close();
            fw.write(name + "\n");
            fw.write(password + "\n");
            fw.write("\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.name = name;
        this.password = password;
    }

    // Constructor that creates a new User without writing to a new file
    public User(String n, String name, String password, ArrayList<String> blockedUsers, boolean exists) {
        for (String a : blockedUsers) {
            this.blockedUsers.add(a);
        }
        this.name = name;
        this.password = password;
    }


    // returns the password for the user
    public String getPassword() {
        return password;
    }

    // returns the name for the user
    public String getName() {
        return name;
    }
    
    public ArrayList<String> getBlocked() {
        return blockedUsers;
    } 

    // adds a blocked user to the blockedUsers arraylist
    public void addBlockedUser(String blockerFile, String blockedFile) {
        // blockerFile is the current user
        // blockedFile is file getting blocked
        // Edits the third line of the blockerFile to add a blocked user
        //System.out.println("test 1");
        try (BufferedReader br = new BufferedReader(new FileReader(blockerFile))) {
            // 
            String line;
            ArrayList<String> lines = new ArrayList<>();
            int x = 0;
            line = br.readLine();
            while (line != null) {
                if (x != 2) {
                    lines.add(line);
                } else {
                    if (line.equals("")) {
                        lines.add(line + blockedFile);
                    } else {
                        lines.add(line + " " + blockedFile);
                    }
                    
                }
                line = br.readLine();
                x++;
            }
            try (FileWriter fr = new FileWriter(blockerFile)) {
                for ( x = 0; x < lines.size(); x++) {
                    fr.write(lines.get(x) + "\n");
                    fr.flush();
                }
            } catch (IOException e) {
                // Handle the exception
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Tests to see if the userFile is a consumer or producer
    public boolean isConsumer(String userFile) {
        return userFile.contains("Consumer");
    }

    // Tests to see if the user sending the file is blocked or not
    public boolean canMessage(String userFile, String blockedFile) {
        // userFile represents the file of the user who is using the system
        // bufferedReader will the read the userFile to see if blockedFile is there.
        try {
            BufferedReader br = new BufferedReader(new FileReader(userFile));
            String line;
            for (int x = 0; x < 3; x++) { 
                // It iterates to the third line of the file 
                // because that is there the blocked users are stored 
                line = br.readLine();
                if (x == 2) {
                    //3rd line of the file
                    if (line != null) {
                        String[] blockedUsersArray = line.split(",");
                        for (String a: blockedUsersArray) {
                            if (a.equals(blockedFile)) {
                                br.close();
                                System.out.println("Yikes... This user has been blocked.");
                                return false;
                                // If blockedFile is equal to one of the blocked user names method returns false
                            }
                        }
                    }
                }
            }
            br.close();
            if ( isConsumer(userFile) && !isConsumer(blockedFile)) {
                // if userFile is a consumer & blockedFile is a producer 
                return true;
            } else if ( !isConsumer(userFile) && isConsumer(blockedFile)) {
                // if userFile is a producer & blockedFile is a consumer 
                return true;
            } else {
                // if user and blocked are both consumers or both producers 
                System.out.println("You Can't Message This User.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // exports a csv file containing message time stamps, participants and history
    public void exportFile(String userName, String recName) {
        // Selects the file that represents a certain text message file between user and rec
        // Writes, edits, and exports the file as a csv
        try (BufferedReader reader = new BufferedReader(new FileReader(userName + recName + ".txt"));
            FileWriter writer = new FileWriter(userName + recName + ".csv")) {
            String line;
            writer.append("Participants");
            writer.append(",");
            writer.append("Sender");
            writer.append(",");
            writer.append("Time");
            writer.append(",");
            writer.append("Contents");
            writer.append(",");
            writer.append("\n");
            while ((line = reader.readLine()) != null) {
                // Split the line using the specified delimiter
                String[] values = line.split(" ");
                writer.append(userName + " & " + recName);
                writer.append(",");
                // Write the values to the CSV file
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replace(",", "");
                    writer.append(values[i]);
                    if (i < values.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            System.out.println("The csv file for the selected convo is stored in : " + userName + recName + ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    // imports a csv file containing message time stamps, participants and history
    public void importFile(String userFile, String recipentFile, String userName, String recName, String file) {
        try {
            // Given .txt file read contents and paste them as one message
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String lines = "";
            String line = reader.readLine();
            while (line != null) {
                lines += line;
                line = reader.readLine();
            }
            sendMessage(userFile, recipentFile, lines, userName, recName);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // sends a message to a user and writes the output to a file
    public void sendMessage(String userFile, String recipentFile, String message, String userName, String recName) {
        //userFile & reciptentFile will get updated with a new message
        // need to enter name of the user so the texts can see who sent what message.
        if (canMessage(userFile, recipentFile)) {
            // Checks if user and recipent are even allowed to message. 
            try {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String time = dateFormat.format(date);
                BufferedWriter fw = new BufferedWriter(new FileWriter(userFile, true));
                BufferedWriter fw2 = new BufferedWriter(new FileWriter(recipentFile, true));
                BufferedWriter fwmessage = new BufferedWriter(new FileWriter(userName + recName + ".txt",
                        true));
                BufferedWriter fw2message = new BufferedWriter(new FileWriter(recName + userName + ".txt",
                        true));
                BufferedWriter messageFile = new BufferedWriter(new FileWriter("messages5.txt", true));
                // Write a line of data to the file.
                fw.append(userName + recName + ".txt\n");
                fw2.append(recName + userName + ".txt\n");
                messageFile.append(userName + recName + ".txt\n" + recName + userName + ".txt\n" );
                fw2message.append(userName + " [" + time + "]: " + message + "\n");
                fwmessage.append(userName  + " [" + time + "]: " + message + "\n");
                // Close the file.
                messageFile.close();
                fwmessage.close();
                fw2message.close();
                fw.close();
                fw2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // // returns all the names of message files that a user is a part of 
    // public String[] readFile(String file) {
    //     // reads all text message files in a user's main file and returns
    //     // the files that contain textmessages the user is apart of as an array of strings
    //     boolean consumer = isConsumer(file);
    //     int x = 4;
    //     if (consumer) {
    //         x = 3;
    //     }
    //     String[] a = new String[x];
    //     try (BufferedReader br = new BufferedReader( new FileReader(file) )) {
    //         for ( int i = 0; i < x; i++ ) {
    //             a[i] = br.readLine();
    //         }
    //     } catch (Exception e) {
    //         a = null;
    //     }
    //     // returns all information in a file (line by line) that arent
    //     // messages (ex: name, password, blocked users, stores (if applicable) )
    //     return a;
    // }

    // prints all the text messages in a certain file
    public void printTexts(String chatfile) {
        try (BufferedReader br = new BufferedReader(new FileReader(chatfile))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    // prints all the text messages in a certain file with the line numbers
    public ArrayList<String> printTextsLineNumbers(String chatfile) {
        ArrayList<String> temp = new ArrayList<>();
        try (BufferedReader br = new BufferedReader( new FileReader( chatfile ))) { 
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                temp.add("(" + (count + 1) + ") " + line);
                count++;
            }
            return temp;
        } catch (Exception e) {
            temp.add("EMPTY");
            return temp;
            
        }
    }

    // returns an arraylist of the all the message file a user is a part of 
    public ArrayList<String> printAllMessages(String userFile) {
        // Parameter tells method if the User is a consumer or not,
        // If a consumer the file is read slightly diffrently then Producer
        try (BufferedReader br = new BufferedReader( new FileReader( userFile ))) {
            int a = 4;
            if (isConsumer(userFile)) {
                a = 3;
            }
            for (int x = 0; x < a; x++) {
                br.readLine();
            }
            ArrayList<String> file = new ArrayList<>();
            int count = 0;
            String line;
            while ((line = br.readLine()) != null) {
                file.add(line);
                count++;
            }
            if (count == 0 ) {
                System.out.println("There are no messages go display.");
            }
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    // edits a message in a message file
    public void editMessage(String file, String name2, int lineNum, String newMessage, String file2) {
    // file = file name that is being edited, name = name of the sender or editor, LineNum = # of line that is deleted
    // newMessage =  new edited message, file2 = name of the other file that is also being edited 
        System.out.println("EDITMESSAGESTUFF");
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        newMessage = newMessage.replaceAll("\n", " ");
        newMessage = newMessage.replaceAll(", ", " ");
        newMessage = newMessage.substring(1, newMessage.length() - 1);
        System.out.println(newMessage);
        Date date = new Date();
        String time = dateFormat.format(date);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            String oldMessage = "";
            // lineNum--;
            
            int x = 0;
            ArrayList<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (x != lineNum) {
                    lines.add(line);
                } else {
                    oldMessage = line;
                    lines.add(name2 + " [" + time + "]: " + newMessage + " (EDITED) ");
                }
                x++;
            }
            // Store all lines of the old file in an ArrayList with the specified line being deleted.
            if ((oldMessage).startsWith(name2)) {
                try (FileWriter fr = new FileWriter(file)) {
                    try (FileWriter fr1 = new FileWriter(file2)) {
                        for (x = 0; x < lines.size(); x++) {
                            fr.write(lines.get(x) + " \n");
                            fr.flush();
                            fr1.write(lines.get(x) + " \n");
                            fr1.flush();
                        }
                        fr1.close();
                    }
                    fr.close();
                }
            } else {
                System.out.println("You can't edit a message you didn't send.");
            }
        // Update both files if the user is able to.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // delets a message in a message file
    public void deleteMessage(String file, String name2, int lineNum) {
        // file = file name that is being edited, name = name of the sender or editor, LineNum = # of line that is
        // deleted
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            String oldMessage = "";
            lineNum--;
            int x = 0;
            ArrayList<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (x != lineNum) {
                    lines.add(line);
                } else {
                    oldMessage = line;
                    lines.add(name2 + ": This message was deleted!");
                }
                x++;
            }
            // Store all lines of the old file in an ArrayList with the specified line being deleted.
            if ((oldMessage).startsWith(name2)) {
                try (FileWriter fr = new FileWriter(file)) {
                    for (x = 0; x < lines.size(); x++) {
                        fr.write(lines.get(x) + " \n");
                        fr.flush();
                        
                    }
                    fr.close();
                }
            } else {
                System.out.println("You can't delete a message you didn't send.");
            }
            // Update the file if the user is able to.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // deletes all message files a user is asscoieated with
    public void deleteAllFiles(File file, String fileName) {
        try {
            file.delete();
            FileWriter fw = new FileWriter("users5.txt", false);
            fw.write("");
            fw.flush();
            fw.close();
            BufferedReader br = new BufferedReader( new FileReader( "messages5.txt" ));
            String line;
            while ((line = br.readLine()) != null) {
                file = new File(line);
                file.delete();
            }
            FileWriter fw2 = new FileWriter("messages5.txt", false);
            fw2.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
