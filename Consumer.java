 import java.io.File;
import java.util.ArrayList;

 /**
 * Consumer
 * This is the class that extends User, it will be 
 * the consumer class with its own specialized methods
 * it will also implement FileEdit 
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Krish Patel, Josh Rubow, Aun Ali, Hersh Tripathi
 * @version Nov 12 23
 */

public class Consumer extends User {
    public String consumerFile; 
    public String name;

    // instantates a Consumer object and writes to a file
    public Consumer(String name, String password) {
        super("Consumer" + name + ".txt", name,  password);
        consumerFile = "Consumer" + name + ".txt";
        this.name = name;
    }

    // instantates a Consumer object without writing to a file
    public Consumer(String name, String password, ArrayList<String> blockedUsers, boolean exists) {
        super("Consumer" + name + ".txt", name,  password, blockedUsers, exists);
        consumerFile = "Consumer" + name + ".txt";
        this.name = name;
    }


    // returns the consumerFile name this object is associated with
    public String cF() {
        return consumerFile;
    }

    // formats the message file name and calls the super method
    public void printTexts(String recName) {
        super.printTexts(name + recName + ".txt" );
    }

    // formats the message file name and calls the super method
    public ArrayList<String> printTextsLineNumbers(String recName) {
        return super.printTextsLineNumbers(name + recName + ".txt" );
    }

    // formats the consumer File parameters and calls the super method
    public void deleteAllFiles() { 
        super.deleteAllFiles(new File(consumerFile), consumerFile);
    }

    // formats the parameters for the super method and calls it
    public void sendMessage(String recipentFile, String message, String reciver) {
        // recipitentFile = seller.sF()
        // message = whatever message you want to send
        // reciver = seller.name
        super.sendMessage(consumerFile, recipentFile, message, name, reciver);
    }

    // formats the parameters for the super method and calls it
    public void editMessage(String reciver, int lineNum, String message) {
        // recipitentFile = seller.sF()
        // lineNum = number of line that is being edited (starts at 0 for time being)
        // message = whatever message you want to send
        super.editMessage(name + reciver + ".txt", name, lineNum, message, reciver + name + ".txt");
    }

    // formats the parameters for the super method and calls it - specifically the file name
    public void deleteMessage(String reciver, int lineNum) {
        // recipitentFile = seller.sF()
        // lineNum = number of line that is being edited (starts at 0 for time being)
        super.deleteMessage(name + reciver + ".txt", name, lineNum);
    }

    // formats the parameters for the super method and calls it
    public void importFile(String recipentFile, String reciver, String file) {     
        super.importFile(consumerFile, recipentFile, name, reciver, file);
    }

    // inputs the parameters for the super and calls it
    public ArrayList<String> printAllMessages() {
        return super.printAllMessages(consumerFile);
    }
}
