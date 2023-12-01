import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Database
 *
 * This class manages the list of user and implements search and unique user methods 
 * as well as other methods
 *
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Krish Patel, Josh Rubow, Aun Ali, Hersh Tripathi
 * @version Nov 12 23
 */


public class Database implements Iterable<User> {
    private ArrayList<User> users = new ArrayList<>();


    public Database() {
        // read in all users to the database from the txt files
        try {
            BufferedReader userReader = new BufferedReader(new FileReader("users.txt"));
            while (userReader != null) {
                String line = userReader.readLine();
                if (line == null) {
                    break;
                }

                BufferedReader userFileReader = new BufferedReader(new FileReader(line));
                if (line.contains("Producer")) {
                    String name = userFileReader.readLine();
                    String password = userFileReader.readLine();
                    Seller seller = new Seller(name, password, new ArrayList<String>(), new ArrayList<String>(), true);
                    users.add(seller);
                } else if (line.contains("Consumer")) {
                    String name = userFileReader.readLine();
                    String password = userFileReader.readLine();
                    Consumer consumer = new Consumer(name, password);
                    users.add(consumer);
                }
                userFileReader.close();
            }

            userReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // initits the databse
    public synchronized void add(User user) {
        users.add(user);
    }

    // This method searches through all users in the database and returns an ArrayList of users whose names
    // matched the search term
    public ArrayList<User> search(String name, User senderName) {
        ArrayList<User> results = new ArrayList<>();
        for (User u : users) {

            if (u.getName().contains(name)) {
                if (!u.getBlocked().contains(senderName.getName()) && !u.getClass().equals(senderName.getClass())) {
                    results.add(u);
                }
            }
        }

        return results;
    } 


    // makes it so the databse object is iterable
    @Override
    public Iterator<User> iterator() {
        return users.iterator();
    }


    // this method checks if the given parameter is alread in the database or not
    public boolean isUniqueUsername(String name) {
        for (User user : users) {
            if (name.equals(user.getName())) {
                return false;
            }
        }

        return true;
    }

    // prints out all the sellers in the database but numbered
    public ArrayList<Seller> printSellersNumbered() {
        ArrayList<Seller> sellers = new ArrayList<>();
        int count = 1;
        for (User user : users) {
            if (user.getClass().equals(Seller.class)) {
                System.out.println("(" + count + ") " + user.getName());
                sellers.add((Seller) user);
                count++;
            }
        }

        return sellers;
    }

    // prints out all the consumers in the database but numbered
    public ArrayList<Consumer> printConsumersNumbered() {
        int count = 1;
        ArrayList<Consumer> consumers = new ArrayList<>();
        for (User user : users) {
            if (user.getClass().equals(Consumer.class)) {
                System.out.println("(" + count + ") " + user.getName());
                consumers.add((Consumer) user);
                count++;
            }
        }

        return consumers;
    }

    // this resets the database to an empty arraylist
    public synchronized void reset() {
        users = new ArrayList<>();
    }

    // returns the size of the database
    public int size() {
        return users.size();
    }

}
