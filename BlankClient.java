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
    private JButton Consumer;
    private JButton Producer;
    private JPanel welcomePanel;
    private JPanel loginSignUpPanel;
    private JPanel signUpPanel;
    private JPanel consumerOrProduerPanel;
    private PrintWriter writer;
    private Socket socket;
    private BufferedReader bfr;
    private boolean currentIf;
    private boolean buttonClick = false;
    private boolean login = false;
    private boolean loginIf = false;
    private boolean consumerBool = true;
    private JTextField usernameField;
    private JTextField passwordField;
    private String userInfo[] = {"", ""};
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
                try {
                    socket = new Socket("localhost", 2020);
                    writer = new PrintWriter(socket.getOutputStream());
                    sendDataToServer("EXIT");
                } catch (Exception e2) {}
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
        label.setForeground(Color.WHITE);
        welcomePanel.setBackground(Color.BLACK);
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
        label.setForeground(Color.WHITE);
        loginSignUpPanel.setBackground(Color.BLACK);
        loginSignUpPanel.setLayout(new GridLayout(3, 1));
        loginSignUpPanel.add(label);
        loginSignUpPanel.add(loginButton);
        loginSignUpPanel.add(signUpButton);
        content.add(loginSignUpPanel, BorderLayout.CENTER);
    } 

    public void displaySignUpPanel() {
        submitNewInfo = new JButton("Submit Info");
        submitNewInfo.addActionListener(actionListener);
        usernameField = new JTextField(20);
        usernameField.setBackground(Color.lightGray);
        passwordField = new JTextField( 20);
        passwordField.setBackground(Color.lightGray);

        JLabel userNameDisplay = new JLabel("Please Eneter Your Email[Must contain @]: ");
        JLabel userPasswordDisplay = new JLabel("Please Eneter Your Password: ");
        JLabel userInstructions = new JLabel("Hit This Button When You Are Done");
        userInstructions.isPreferredSizeSet();
        signUpPanel = new JPanel();
        signUpPanel.setLayout(new GridLayout(3, 2));

        userNameDisplay.setForeground(Color.WHITE);
        userPasswordDisplay.setForeground(Color.WHITE);
        userInstructions.setForeground(Color.WHITE);
        signUpPanel.setBackground(Color.BLACK);
        signUpPanel.add(userNameDisplay);
        signUpPanel.add(usernameField);
        signUpPanel.add(userPasswordDisplay);
        signUpPanel.add(passwordField);
        signUpPanel.add(userInstructions);
        signUpPanel.add(submitNewInfo);
        content.add(signUpPanel, BorderLayout.CENTER);
        submitNewInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assign the text input to the variable when the button is pressed
                if (!usernameField.getText().contains("@")) {
                    JOptionPane.showMessageDialog(null, "PLEASE PUT AN EMAIL", "EMAIL", JOptionPane.ERROR_MESSAGE);
                } else {
                    userInfo[0] = usernameField.getText();
                    userInfo[1] = passwordField.getText();
                    signUpPanel.setVisible(false);
                    latch.countDown();
                }
            }
        });
    }
    public void displayCoSPanel() {
        Consumer = new JButton("Consumer");
        Producer = new JButton("Producer");
        Consumer.addActionListener(actionListener);
        Producer.addActionListener(actionListener);

        consumerOrProduerPanel = new JPanel();
        consumerOrProduerPanel.setLayout(new GridLayout(1, 2));
        consumerOrProduerPanel.setBackground(Color.BLACK);
        consumerOrProduerPanel.add(Consumer);
        consumerOrProduerPanel.add(Producer);
        content.add(consumerOrProduerPanel, BorderLayout.CENTER);
        Consumer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latch.countDown();
                consumerOrProduerPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
                SwingUtilities.invokeLater(() -> consumerBool = true);
                latch.countDown();
                content.remove(consumerOrProduerPanel);
            }
        });
        Producer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumerOrProduerPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
                SwingUtilities.invokeLater(() -> consumerBool = false);
                latch.countDown();
                content.remove(consumerOrProduerPanel);
                SwingUtilities.invokeLater(() -> {
                    System.out.println(consumerBool);
                });
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
                                    // sendDataToServer("Login");
                                } else {
                                    userInfo[0] = "";
                                    displaySignUpPanel();
                                    content.revalidate();
                                    content.repaint();
                                    latch.await();
                                    while (!userInfo[0].contains("@")) {
                                        userInfo[0] = "";
                                        displaySignUpPanel();
                                        content.revalidate();
                                        content.repaint();
                                        latch.await();
                                        // content.remove(signUpPanel);
                                    }
                                    content.remove(signUpPanel);
                                    System.out.println(userInfo[0]+" "+userInfo[1]);
                                    displayCoSPanel();
                                    content.revalidate();
                                    content.repaint();
                                    latch.await();
                                    System.out.println(consumerBool);
                                    return null;
                                    
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