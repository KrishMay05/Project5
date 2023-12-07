import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Seller
 * <p>Contains all the methods and variables for a Seller object</p>
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Krish Patel, Josh Rubow, Aun Ali, Hersh Tripathi
 * @version Nov 12 23
 */

public class Seller extends User {
    public String sellerFile;
    public ArrayList<String> stores;
    public String name;

    // instantates a Seller object and writes to a file
    public Seller(String name, String password, ArrayList<String> stores) {
        super("Producer" + name + ".txt", name,  password);
        sellerFile = "Producer" + name + ".txt";
        this.stores = stores;
        this.name = name;
        try (FileWriter fw = new FileWriter( sellerFile, true)) {
            for (String a: stores) {
                fw.write(a + " ");
            }
            fw.write("\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // instantates a Seller object without writing to a file
    public Seller(String name, String password, ArrayList<String> stores, ArrayList<String> blockedUsers, boolean
            exists) {
        super("Producer" + name + ".txt", name,  password, blockedUsers, exists);
        sellerFile = "Producer" + name + ".txt";
        this.stores = stores;
        this.name = name;
    }

    public String sF() {
        return sellerFile;
    }
    public ArrayList<String> getStores() {
        return stores;
    }

    public void printTexts(String sell) {
        // rec name = consumer.cF()
        super.printTexts(name + sell +  ".txt" );
    }

    public ArrayList<String> printTextsLineNumbers(String sell) {
        // rec name = consumer.cF()
        return super.printTextsLineNumbers(name + sell +  ".txt" );
    }

    public void editMessage(String reciver, int lineNum, String message) {
        // recipitentFile = consumer.getName()
        // lineNum = number of line that is being edited (starts at 0 for time being)
        // message = whatever message you want to send
        super.editMessage(name + reciver + ".txt", name, lineNum, message, reciver + name + ".txt");
    }
    public void deleteAllFiles() {
        super.deleteAllFiles(new File(sellerFile), name);
    }

    public void sendMessage(String recipentFile, String message, String reciver) {
        // recipitentFile = consumer.cF()
        // message = whatever message you want to send
        // reciver = seller.name
        super.sendMessage(sellerFile, recipentFile, message, name, reciver);
    }
    public void deleteMessage(String reciver, int lineNum) {
        // recipitentFile = consumer.cF()
        // lineNum = number of line that is being edited (starts at 0 for time being)
        super.deleteMessage(name + reciver + ".txt", name, lineNum);
    }

    public void importFile(String recipentFile, String reciver, String file) {
        super.importFile(sellerFile, recipentFile, name, reciver, file);
    }
    public ArrayList<String> printAllMessages() {
        return super.printAllMessages(sellerFile);
    }
}
