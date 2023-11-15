import java.util.ArrayList;
/**
 * UserTest
 * This class tests other classes in the project. It tests the User, Consumer, and Seller classes.
 * <p>Purdue University -- Cs18000 -- Fall 2023</p>
 *
 * @author Krish Patel, Josh Rubow, Aun Ali, Hersh Tripathi
 * @version Nov 12 23
 */

public class UserTest {
    public static void main(String[] args) {
        ArrayList<String> stores = new ArrayList<>();
        Consumer c1 = new Consumer("Krish", "TopGun87");
        Consumer c2 = new Consumer("Aun", "Hello");
        Seller s1 = new Seller("Hersh", "password", stores);
        //Send Message Test
        s1.sendMessage(c1.cF(), "Hello ", c1.name);
        s1.sendMessage(c1.cF(), "Hello ", c1.name);
        //Block Method Test
        s1.addBlockedUser(s1.sF(), c2.cF());
            //Should not be ale to send because user is blocked.

        // Can message, called implicitly in User methods
        System.out.println(s1.canMessage(s1.sF(), c1.cF())); 
            // true because one is seller one is consumer 
        System.out.println(c2.canMessage(c2.cF(), c1.cF())); 
            // false becuase both are consumers & canMessage prints a custom error message 
        System.out.println(s1.canMessage(s1.sF(), c2.cF())); 
            // s1 blocked c2

        //readFile
        // Never ended up being used in final result
        // System.out.println(Arrays.toString(s1.readFile(s1.sF())));

        //importFile
        s1.importFile(c1.cF(), c1.getName(), "Hersh.txt");
            // should send contents of Hersh.txt to c1 as a message.
        s1.exportFile(s1.getName(), c1.getName());
            // Should print out a csv file for the message between s1 and c1

        //print Texts
        System.out.println();
        s1.printTexts(c1.getName());
            //print out all text messages between 2 users
        System.out.println();
        s1.printTextsLineNumbers(c1.getName());
            //print out all text messages between 2 users with numbers infront of each line
        
        //EditMessage
        s1.editMessage(c1.getName(), 1, "This message is edited");
            //First line of messaging should be edited.
        s1.printTextsLineNumbers(c1.getName());

        //DeleteMessage
        s1.deleteMessage(c1.getName(), 1);
        s1.printTextsLineNumbers(c1.getName()); // Should only be deleted in this print
        c1.printTextsLineNumbers(s1.getName());

        //Delete all files
        c1.deleteAllFiles();
        c2.deleteAllFiles();
        s1.deleteAllFiles();
    }
}
