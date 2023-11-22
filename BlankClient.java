import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
    JFrame frame;
    Container content;
    BlankClient blankClient;
    JButton enterSystemButton;
    JButton exitSystemButton;
    JPanel welcomePanel;
    private PrintWriter writer;
    private Socket socket;
    private BufferedReader bfr;
    public boolean currentIf;

    ActionListener actionListener = new ActionListener() {
        @Override public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterSystemButton) {
                welcomePanel.setVisible(false);
                String hostname = "localhost"; //change for different hosts
                int portNumber = 2020; //change for different port numbers
                try {
                    socket = new Socket(hostname, portNumber);
                    bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream());
                    sendDataToServer("Hello, server!");
                    // THIS IS WHERE WE WILL IMPLEMENT ALL THE REST OF THE FRONT END LOGIC 
                    // ^^^^^^
                    // ^^^^^^
                    // ^^^^^^
                    // ^^^^^^
                } catch (ConnectException a) {
                    JOptionPane.showMessageDialog(null, "There has been an issue connecting " +
                        "to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                } catch (UnknownHostException b) {
                    JOptionPane.showMessageDialog(null, "There has been an issue connecting " +
                        "to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                } catch (IOException c) {
                    JOptionPane.showMessageDialog(null, "There has been an issue reading " +
                        "or writing to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getSource() == exitSystemButton) {
                JOptionPane.showMessageDialog(null, "Thank you for using the Blank Messaging",
                        "BlankMessaging", JOptionPane.INFORMATION_MESSAGE);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }
    }; //actionListener

    public void displayWelcomePanel() {
        JLabel label = new JLabel("Welcome, would you like access to Blank Messaging?");
        label.setHorizontalAlignment(JLabel.CENTER);
        enterSystemButton = new JButton("Yes");
        enterSystemButton.addActionListener(actionListener);
        exitSystemButton = new JButton("No");
        exitSystemButton.addActionListener(actionListener);
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridLayout(3, 1));
        welcomePanel.add(label);
        welcomePanel.add(enterSystemButton);
        welcomePanel.add(exitSystemButton);
        content.add(welcomePanel, BorderLayout.CENTER);

    } //displayWelcomePanel

    public void sendDataToServer(String data) throws IOException {
        writer.println(data);
        writer.flush();
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        SwingUtilities.invokeLater(new BlankClient());
    }//main method

    public void run() {
        frame = new JFrame("BlankMessaging");
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        blankClient = new BlankClient();
        content.add(blankClient, BorderLayout.CENTER);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        displayWelcomePanel();


    } //run
}//BlankClient
