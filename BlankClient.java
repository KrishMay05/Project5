import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * BlankClient.java
 * <p>
 * Runs all JFrame GUI prompting elements, sends gathered information to the server, and uses returned info to
 * display messages to the user.
 *
 * @author Krish Patel, Josh Rubow, Aun Ali, Hersh Tripathi
 * @version [COMPLETION DATE]
 */
public class BlankClient extends JComponent implements Runnable{
    BlankClient blankClient;
    JButton enterSystemButton;
    JButton exitSystemButton;


    ActionListener actionListener = new ActionListener() {
        @Override public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterSystemButton) {

            }
            if (e.getSource() == exitSystemButton) {

            }
        }
    };


    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        SwingUtilities.invokeLater(new BlankClient());
        /*
        String hostname = "localhost" //change for different hosts
        int portNumber = 7059; //change for different port numbers

        try {
            Socket socket = new Socket(hostname, portNumber);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter write = new PrintWriter(socket.getOutputStream());

        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, "There has been an issue connecting " +
                            "to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "There has been an issue connecting " +
                    "to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
        }

         */
    }

    public void run() {
        JFrame frame = new JFrame("BlankMessaging");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        blankClient = new BlankClient();
        content.add(blankClient, BorderLayout.CENTER);

        JLabel label = new JLabel("Welcome, would you like access to Blank Messaging?");
        label.setHorizontalAlignment(JLabel.CENTER);
        enterSystemButton = new JButton("Yes");
        enterSystemButton.addActionListener(actionListener);
        exitSystemButton = new JButton("No");
        exitSystemButton.addActionListener(actionListener);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 1));
        
        topPanel.add(label);
        topPanel.add(enterSystemButton);
        topPanel.add(exitSystemButton);


        content.add(topPanel, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
