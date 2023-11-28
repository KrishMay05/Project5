import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class BlankClient extends JComponent implements Runnable {
    private JFrame frame;
    private Container content;
    private BlankClient blankClient;
    private JButton enterSystemButton;
    private JButton exitSystemButton;
    private JButton loginButton;
    private JButton signUpButton;
    private JButton submitNewInfo;
    private JPanel welcomePanel;
    private JPanel loginSignUpPanel;
    private JPanel signUpPanel;
    private PrintWriter writer;
    private Socket socket;
    private BufferedReader bfr;
    private boolean currentIf;
    private boolean buttonClick = false;
    private boolean login = false;
    private boolean loginIf = false;
    private JTextField usernameField;
    private JTextField passwordField;
    private String userInfo[] = new String[2];
    private CountDownLatch latch = new CountDownLatch(1);

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterSystemButton) {
                welcomePanel.setVisible(false);
                SwingUtilities.invokeLater(() -> currentIf = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
            }
            if (e.getSource() == exitSystemButton) {
                JOptionPane.showMessageDialog(null, "Thank you for using the Blank Messaging",
                        "BlankMessaging", JOptionPane.INFORMATION_MESSAGE);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                SwingUtilities.invokeLater(() -> currentIf = false);
                SwingUtilities.invokeLater(() -> buttonClick = false);
            }
            if (e.getSource() == loginButton) {
                loginSignUpPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
            }
            if (e.getSource() == signUpButton) {
                loginSignUpPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login = false);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
            }
        }
    };
    

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
    } // displayWelcomePanel

    public void displayLoginSignUpPanel() {
        JLabel label = new JLabel("Welcome, would you like access to login or signup?");
        label.setHorizontalAlignment(JLabel.CENTER);
        loginButton = new JButton("Login");
        loginButton.addActionListener(actionListener);
        signUpButton = new JButton("Signup");
        signUpButton.addActionListener(actionListener);
        loginSignUpPanel = new JPanel();
        loginSignUpPanel.setLayout(new GridLayout(3, 1));
        loginSignUpPanel.add(label);
        loginSignUpPanel.add(loginButton);
        loginSignUpPanel.add(signUpButton);
        content.add(loginSignUpPanel, BorderLayout.CENTER);
    } 

    public void displaySignUpPanel() {
        submitNewInfo = new JButton("Submit Info");
        submitNewInfo.addActionListener(actionListener);
        usernameField = new JTextField("Please input Email: ", 20);
        usernameField.setBackground(Color.lightGray);
        passwordField = new JTextField("Please input Password: ", 20);
        passwordField.setBackground(Color.lightGray);
        signUpPanel = new JPanel();
        signUpPanel.setLayout(new GridLayout(3, 1));
        signUpPanel.add(usernameField);
        signUpPanel.add(passwordField);
        signUpPanel.add(submitNewInfo);
        content.add(signUpPanel, BorderLayout.CENTER);
        submitNewInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assign the text input to the variable when the button is pressed
                userInfo[0] = usernameField.getText();
                userInfo[1] = passwordField.getText();
                signUpPanel.setVisible(false);
                latch.countDown();
            }
        });
    }

    public void sendDataToServer(String data) throws IOException {
        writer.println(data);
        writer.flush();
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        SwingUtilities.invokeLater(new BlankClient());
    }// main method

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
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (!isCancelled()) {
                    if (buttonClick) {
                        SwingUtilities.invokeLater(() -> buttonClick = false);
                        try {
                            socket = new Socket("localhost", 2020);
                            bfr = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                            writer = new PrintWriter(socket.getOutputStream());

                            if (currentIf) {
                                sendDataToServer("Hello, server!");
                                SwingUtilities.invokeLater(() -> currentIf = false);
                            }
                            content.remove(welcomePanel);  // Remove the previous panel
                            displayLoginSignUpPanel();     // Display the new panel
                            content.revalidate();
                            content.repaint();
                            if (loginIf) {
                                System.out.println("eval pls");
                                content.remove(loginSignUpPanel);
                                SwingUtilities.invokeLater(() -> loginIf = false);
                                if (login) {
                                    sendDataToServer("Login");
                                } else {
                                    displaySignUpPanel();
                                    content.revalidate();
                                    content.repaint();
                                    latch.await();
                                    sendDataToServer("Sign Up");
                                    System.out.println(userInfo[0]);
                                }
                                SwingUtilities.invokeLater(() -> login = false);
                            }
                            SwingUtilities.invokeLater(() -> loginIf = false);

                        } catch (ConnectException a) {
                            JOptionPane.showMessageDialog(null, "There has been an issue connecting " +
                                    "to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                            return null;
                        } catch (UnknownHostException b) {
                            JOptionPane.showMessageDialog(null, "There has been an issue connecting " +
                                    "to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                            return null;
                        } catch (IOException c) {
                            JOptionPane.showMessageDialog(null, "There has been an issue reading " +
                                    "or writing to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                            return null;
                        }
                    }
                    }
                return null;
            }
        };
        worker.execute();

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    if (bfr != null) bfr.close();
                    if (writer != null) writer.close();
                    if (socket != null) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    } // run
}