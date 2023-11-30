import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
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
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JButton submitStore;
    private JButton numStoreButton;
    private JPanel welcomePanel;
    private JPanel loginSignUpPanel;
    private JPanel signUpPanel;
    private JPanel storeInfoPanel;
    // private JPanel consumerProducerPanel;
    private JPanel consumerOrProduerPanel;
    private JPanel storeNumberPanel;
    private JPanel loginPanel;
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
                    int reply = JOptionPane.showConfirmDialog(null, "Would You Like To Delete ALL DATA?"
                            , "Delete Info?", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        sendDataToServer("EXITDELETE");
                    } else {
                        sendDataToServer("EXITSAVE");
                    }
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
        label.setFont(new Font("Serif", Font.PLAIN, 25));
        enterSystemButton = new JButton("Yes");
        enterSystemButton.addActionListener(actionListener);
        enterSystemButton.setFont(new Font("Serif", Font.PLAIN, 25));
        exitSystemButton = new JButton("No");
        exitSystemButton.addActionListener(actionListener);
        exitSystemButton.setFont(new Font("Serif", Font.PLAIN, 25));
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
        label.setFont(new Font("Serif", Font.PLAIN, 25));
        loginButton = new JButton("Login");
        loginButton.addActionListener(actionListener);
        loginButton.setFont(new Font("Serif", Font.PLAIN, 25));
        signUpButton = new JButton("Signup");
        signUpButton.addActionListener(actionListener);
        signUpButton.setFont(new Font("Serif", Font.PLAIN, 25));

        loginSignUpPanel = new JPanel();
        label.setForeground(Color.WHITE);
        loginSignUpPanel.setBackground(Color.BLACK);
        loginSignUpPanel.setLayout(new GridLayout(3, 1));
        loginSignUpPanel.add(label);
        loginSignUpPanel.add(loginButton);
        loginSignUpPanel.add(signUpButton);
        content.add(loginSignUpPanel, BorderLayout.CENTER);
    } 

    public void displayLoginPanel() {
        submitNewInfo = new JButton("Submit Info");
        submitNewInfo.addActionListener(actionListener);
        submitNewInfo.setFont(new Font("Serif", Font.PLAIN, 25));
        usernameField = new JTextField(20);
        usernameField.setBackground(Color.lightGray);
        usernameField.setFont(new Font("Serif", Font.PLAIN, 25));
        passwordField = new JTextField( 20);
        passwordField.setBackground(Color.lightGray);
        passwordField.setFont(new Font("Serif", Font.PLAIN, 25));

        JLabel userNameDisplay = new JLabel("Email: ");
        userNameDisplay.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel userPasswordDisplay = new JLabel("Password: ");
        userPasswordDisplay.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel userInstructions = new JLabel("");
        userInstructions.setFont(new Font("Serif", Font.PLAIN, 25));
        userInstructions.isPreferredSizeSet();
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));

        userNameDisplay.setForeground(Color.WHITE);
        userPasswordDisplay.setForeground(Color.WHITE);
        userInstructions.setForeground(Color.WHITE);
        loginPanel.setBackground(Color.BLACK);
        loginPanel.add(userNameDisplay);
        loginPanel.add(usernameField);
        loginPanel.add(userPasswordDisplay);
        loginPanel.add(passwordField);
        loginPanel.add(userInstructions);
        loginPanel.add(submitNewInfo);
        content.add(loginPanel, BorderLayout.CENTER);
        submitNewInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assign the text input to the variable when the button is pressed
                if (!usernameField.getText().contains("@")) {
                    JOptionPane.showMessageDialog(null, "Please enter an Email", "Email only", JOptionPane.ERROR_MESSAGE);
                } else {
                    userInfo[0] = usernameField.getText();
                    userInfo[1] = passwordField.getText();
                    loginPanel.setVisible(false);
                    latch.countDown();
                }
            }
        });
    }

    public void displaySignUpPanel() {
        submitNewInfo = new JButton("Submit Info");
        submitNewInfo.addActionListener(actionListener);
        submitNewInfo.setFont(new Font("Serif", Font.PLAIN, 25));
        usernameField = new JTextField(20);
        usernameField.setBackground(Color.lightGray);
        usernameField.setFont(new Font("Serif", Font.PLAIN, 25));
        passwordField = new JTextField( 20);
        passwordField.setBackground(Color.lightGray);
        passwordField.setFont(new Font("Serif", Font.PLAIN, 25));

        JLabel userNameDisplay = new JLabel("Please Eneter Your Email[Must contain @]: ");
        userNameDisplay.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel userPasswordDisplay = new JLabel("Please Eneter Your Password: ");
        userPasswordDisplay.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel userInstructions = new JLabel("Hit This Button When You Are Done");
        userInstructions.setFont(new Font("Serif", Font.PLAIN, 25));
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

    public void displayStoreNumberPanel() {
        numStoreButton = new JButton("Submit Info");
        numStoreButton.addActionListener(actionListener);
        numStoreButton.setFont(new Font("Serif", Font.PLAIN, 25));
        Integer[] choices = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        JComboBox<Integer> numberDropdownMenu = new JComboBox<Integer>(choices);
        numberDropdownMenu.setFont(new Font("Serif", Font.PLAIN, 25));
        storeNumberPanel = new JPanel();
        storeNumberPanel.setLayout(new GridLayout(3, 2));
        storeNumberPanel.setBackground(Color.BLACK);
        storeNumberPanel.add(numberDropdownMenu);
        storeNumberPanel.add(numStoreButton);
            content.add(storeNumberPanel, BorderLayout.CENTER);
    
        numStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                storeNumberPanel.setVisible(false);
                content.remove(storeNumberPanel);
                displayStoreInfoPanel(numberDropdownMenu.getSelectedIndex() + 1);
                latch.countDown();
            }
        });
        content.revalidate();
        content.repaint();
    }
    
    public void displayStoreInfoPanel(int storeNum) {
        submitStore = new JButton("Submit Store");
        submitStore.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel storeNameDisplay = new JLabel("Please Enter The Name of The Store: ");
        storeNameDisplay.setFont(new Font("Serif", Font.PLAIN, 25));
        storeInfoPanel = new JPanel();
        storeInfoPanel.setLayout(new GridLayout(storeNum + 2, 1));
        ArrayList<JTextField> storeText = new ArrayList<>();
        for ( int i = 0; i < storeNum; i++) {
            storeText.add(new JTextField(20));
            storeText.get(i).setBackground(Color.lightGray);
            storeText.get(i).setFont(new Font("Serif", Font.PLAIN, 25));
            storeInfoPanel.add(storeText.get(i));
        }
        storeNameDisplay.setForeground(Color.WHITE);
        storeInfoPanel.setBackground(Color.BLACK);
        storeInfoPanel.add(storeNameDisplay);
        // Add storeField to the panel
        storeInfoPanel.add(submitStore);
        content.add(storeInfoPanel, BorderLayout.CENTER);
        // Use a variable to store the user input
    
        submitStore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assign the text input to the variable when the button is pressed
                String l = "";
                for ( int i = 0; i < storeNum; i++) {
                    l += " " + storeText.get(i).getText().replace(" ", "_");
                }
                storeInfoPanel.setVisible(false);
                content.remove(storeInfoPanel);
                latch.countDown();
                try {
                    sendDataToServer("SIGNUP" + userInfo[0] + " " + userInfo[1] + " Producer" + l);
                } catch (IOException e1) {}
                displayWelcomePanel();
                content.revalidate();
                content.repaint();
            }
        });
    
        // Wait for latch or do any other necessary operations
    
        // Return the user input
    }
    
    
    public void displayCoSPanel() {
        Consumer = new JButton("Consumer");
        Producer = new JButton("Producer");
        Consumer.addActionListener(actionListener);
        Producer.addActionListener(actionListener);
        Consumer.setFont(new Font("Serif", Font.PLAIN, 25));
        Producer.setFont(new Font("Serif", Font.PLAIN, 25));
    
        consumerOrProduerPanel = new JPanel();
        consumerOrProduerPanel.setLayout(new GridLayout(1, 2));
        consumerOrProduerPanel.setBackground(Color.BLACK);
        consumerOrProduerPanel.add(Consumer);
        consumerOrProduerPanel.add(Producer);
        content.add(consumerOrProduerPanel, BorderLayout.CENTER);
        Consumer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumerOrProduerPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
                consumerBool = true;
                content.remove(consumerOrProduerPanel);
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            sendDataToServer("SIGNUP" + userInfo[0] + " " + userInfo[1] + " Consumer");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        latch.countDown();
                        return null;
                    }
                    @Override
                    protected void done() {
                        displayWelcomePanel();
                        content.revalidate();
                        content.repaint();
                    }
                };
                worker.execute();
            }
        });
        Producer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumerOrProduerPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
                consumerBool = false;
                content.remove(consumerOrProduerPanel);
                displayStoreNumberPanel();
                content.revalidate();
                content.repaint();
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        latch.countDown();
                        return null;
                    }
                };
                worker.execute();
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
                try {
                    socket = new Socket("localhost", 2020);
                    bfr = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    writer = new PrintWriter(socket.getOutputStream());
        
                    while (!isCancelled()) {
                        if (currentIf) {
                            sendDataToServer("Hello, server!");
                            SwingUtilities.invokeLater(() -> currentIf = false);
                        }
                        if (buttonClick) {
                            buttonClick = false;
                            content.remove(welcomePanel);
                            displayLoginSignUpPanel();
                            content.revalidate();
                            content.repaint();
                        }
        
                        if (loginIf) {
                            loginIf = false;
                            content.remove(loginSignUpPanel);
                            if (login) {
                                // Handle login logic
                                
                                displayLoginPanel();
                                content.revalidate();
                                content.repaint();
                                latch.await();
                                while (!userInfo[0].contains("@")) {
                                    userInfo[0] = "";
                                    displayLoginPanel();
                                    content.revalidate();
                                    content.repaint();
                                    latch.await();
                                }
                                content.remove(loginPanel);
                                System.out.println(userInfo[0] + " " + userInfo[1]);

                                writer.write("post:Signup" + "\nusername:" + userInfo[0] + "\npassword:" + userInfo[1]);
                                writer.flush();

                                if(bfr.readLine().equals("OK")) {
                                    JOptionPane.showMessageDialog(null, "You have successfully logged in", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "There was an issue with your login request", "Login Error", JOptionPane.ERROR_MESSAGE);
                                }


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
                                }
                                content.remove(signUpPanel);
                                System.out.println(userInfo[0]+" "+userInfo[1]);
                                displayCoSPanel();
                                content.revalidate();
                                content.repaint();
                                latch.await();
                                SwingUtilities.invokeLater(() -> {
                                    System.out.println(consumerBool);
                                });
                            }
                            SwingUtilities.invokeLater(() -> login = false);
                        }

                        if (consumerBool) {
                            // Handle consumer logic
                        } else {
                            // Handle producer logic
                        }
                    }
                } catch (ConnectException a) {
                    JOptionPane.showMessageDialog(null, "There has been an issue connecting " +
                            "to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                } catch (UnknownHostException b) {
                    JOptionPane.showMessageDialog(null, "There has been an issue connecting " +
                            "to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                } catch (IOException c) {
                    JOptionPane.showMessageDialog(null, "There has been an issue reading " +
                            "or writing to the server", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try {
                        if (bfr != null) bfr.close();
                        if (writer != null) writer.close();
                        if (socket != null) socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
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