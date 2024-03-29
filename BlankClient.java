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

/**
 * BlankClient.java
 *
 * This is the client side of our program, it runs
 * a JPanel front end and takes the user input from
 * it and sends it to BlankServer.java / BlankServerThread.java
 * to do backend calculations.
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Krish Patel, Josh Rubow, Aun Ali, Hersh Tripathi
 * @version Nov 12 23
 */

public class BlankClient extends JComponent implements Runnable {
    private JFrame frame;
    private Container content;
    private BlankClient blankClient;
    private JButton enterSystemButton;
    private JButton exitSystemButton;
    private JButton loginButton;
    private JButton signUpButton;
    private JButton submitNewInfo;
    private JButton submitLoginInfo;
    private JButton consumer;
    private JButton producer;
    private JButton submitStore;
    private JButton numStoreButton;
    private JButton backFromMessageButton;
    private JButton loginOptionButton;
    private JButton deleteButton;
    private JButton editButton;
    private JPanel welcomePanel;
    private JPanel loginSignUpPanel;
    private JPanel signUpPanel;
    private JPanel storeInfoPanel;
    private JPanel loginPanel;
    private JPanel messagePanel;
    private JPanel editPanel;
    private JPanel deletePanel;
    // private JPanel consumerProducerPanel;
    private JPanel consumerOrProduerPanel;
    private JPanel storeNumberPanel;
    private JPanel loginOptionPanel;
    private PrintWriter writer;
    private Socket socket;
    private BufferedReader bfr;
    private boolean currentIf;
    private boolean buttonClick = false;
    private boolean login = false;
    private boolean loginIf = false;
    private boolean login2 = false;
    private boolean consumerBool = true;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField usernameFieldLog;
    private JTextField passwordFieldLog;
    private String conditional = null;
    private String[] userInfo = { "", "" };
    private CountDownLatch latch;
    private String[] storesOrConsumers;
    private JButton loginBack;
    // private CountDownLatch latchlogin = new CountDownLatch(1);

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterSystemButton) {
                welcomePanel.setVisible(false);
                latch = new CountDownLatch(0);
                SwingUtilities.invokeLater(() -> currentIf = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                talkin();

            }
            if (e.getSource() == exitSystemButton) {
                try {
                    socket = new Socket("localhost", 2020);
                    writer = new PrintWriter(socket.getOutputStream());
                    int reply = JOptionPane.showConfirmDialog(null,
                            "Would You Like To Delete ALL DATA?", "Delete Info?", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        sendDataToServer("EXITDELETE");
                    } else {
                        sendDataToServer("EXITSAVE");
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Thank you for using the Blank Messaging",
                        "BlankMessaging", JOptionPane.INFORMATION_MESSAGE);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                SwingUtilities.invokeLater(() -> currentIf = false);
                SwingUtilities.invokeLater(() -> buttonClick = false);
            }
            if (e.getSource() == loginButton) {
                // latch = new CountDownLatch(1);
                // System.out.println("log" + latch.getCount());
                conditional = "Works";
                // latch.countDown();
                loginSignUpPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login2 = true);
                SwingUtilities.invokeLater(() -> login = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
                

            }
            if (e.getSource() == signUpButton) {
                // System.out.println("sign" + latch.getCount());
                // latch = new CountDownLatch(1);
                conditional = "Works";
                loginSignUpPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login = true);
                SwingUtilities.invokeLater(() -> login2 = false);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
                

            }
            if (e.getSource() == backFromMessageButton) {
                messagePanel.setVisible(false);
                latch = new CountDownLatch(0);
                content.removeAll();
                content.revalidate();
                content.repaint();
                displayLoginOptionPanel();
                content.revalidate();
                content.repaint();

            }
        }
    };

    public void displayLoginPanel() {
        latch = new CountDownLatch(1);
        submitLoginInfo = new JButton("Submit Info");
        submitLoginInfo.addActionListener(actionListener);
        submitLoginInfo.setFont(new Font("Serif", Font.PLAIN, 25));
        usernameFieldLog = new JTextField(20);
        usernameFieldLog.setBackground(Color.lightGray);
        usernameFieldLog.setFont(new Font("Serif", Font.PLAIN, 25));
        passwordFieldLog = new JTextField(20);
        passwordFieldLog.setBackground(Color.lightGray);
        passwordFieldLog.setFont(new Font("Serif", Font.PLAIN, 25));
        loginBack = new JButton("Back");
        loginBack.setFont(new Font("Serif", Font.PLAIN, 25));

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
        loginPanel.add(usernameFieldLog);
        loginPanel.add(userPasswordDisplay);
        loginPanel.add(passwordFieldLog);
        // loginPanel.add(userInstructions);
        loginPanel.add(loginBack);
        loginPanel.add(submitLoginInfo);
        content.add(loginPanel, BorderLayout.CENTER);

        loginBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                content.removeAll();
                displayWelcomePanel();
                content.repaint();
                content.revalidate();
            }
        });
        submitLoginInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!usernameFieldLog.getText().isEmpty() && !passwordFieldLog.getText().isEmpty()) {
                    userInfo[0] = usernameFieldLog.getText();
                    userInfo[1] = passwordFieldLog.getText();
                    // System.out.println(userInfo[0] + userInfo[1]);
                    try {
                        sendDataToServer("LOGIN " + userInfo[0] + " " + userInfo[1]);
                        String ln = bfr.readLine();
                        // System.out.println(ln);
                        if (!ln.contains("True")) {
                            userInfo[0] = "";
                            userInfo[1] = "";
                            JOptionPane.showMessageDialog(null,
                                    "There was an issue with your login request", "Login Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            ln = ln.replace("True", "");
                            ln += " exit";
                            // System.out.println(ln);
                            storesOrConsumers = ln.split(" ");
                            JOptionPane.showMessageDialog(null, "You have successfully logged in",
                                    "Login Success", JOptionPane.INFORMATION_MESSAGE);
                            loginPanel.setVisible(false);
                            latch.countDown();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a username and password",
                        "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void displayWelcomePanel() {
        content.removeAll();
        content.revalidate();
        content.repaint();
        latch = new CountDownLatch(0);
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
        content.removeAll();
        content.revalidate();
        content.repaint();
        latch = new CountDownLatch(0);
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

    public void displaySignUpPanel() {
        latch = new CountDownLatch(0);
        latch = new CountDownLatch(1);
        submitNewInfo = new JButton("Submit Info");
        submitNewInfo.addActionListener(actionListener);
        submitNewInfo.setFont(new Font("Serif", Font.PLAIN, 25));
        usernameField = new JTextField(20);
        usernameField.setBackground(Color.lightGray);
        usernameField.setFont(new Font("Serif", Font.PLAIN, 25));
        passwordField = new JTextField(20);
        passwordField.setBackground(Color.lightGray);
        passwordField.setFont(new Font("Serif", Font.PLAIN, 25));

        JLabel userNameDisplay = new JLabel("Please Enter Your Email[Must contain @]: ");
        userNameDisplay.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel userPasswordDisplay = new JLabel("Please Enter Your Password: ");
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
                    JOptionPane.showMessageDialog(null, "PLEASE PUT AN EMAIL", "EMAIL",
                            JOptionPane.ERROR_MESSAGE);
                } else if ((passwordField.getText().isBlank())) {
                    JOptionPane.showMessageDialog(null, "Password Can't be Empty",
                            "password", JOptionPane.ERROR_MESSAGE);
                } else {
                    userInfo[0] = usernameField.getText();
                    userInfo[1] = passwordField.getText();
                    try {
                        sendDataToServer("ISUNIQUE " + usernameField.getText());
                        if (bfr.readLine().toLowerCase().equals("true")) {
                            signUpPanel.setVisible(false);
                            latch.countDown();
                        } else {
                            JOptionPane.showMessageDialog(null, "Try a different username, this one is already taken!",
                                    "password", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });
    }

    public void displayStoreNumberPanel() {
        latch = new CountDownLatch(1);
        numStoreButton = new JButton("Submit Info");
        numStoreButton.addActionListener(actionListener);
        numStoreButton.setFont(new Font("Serif", Font.PLAIN, 25));
        Integer[] choices = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        JLabel storeNumberDisplay = new JLabel("Please Enter The Number of Stores You Have: ");
        storeNumberDisplay.setFont(new Font("Serif", Font.PLAIN, 25));
        storeNumberDisplay.setForeground(Color.WHITE);
        JComboBox<Integer> numberDropdownMenu = new JComboBox<Integer>(choices);
        numberDropdownMenu.setFont(new Font("Serif", Font.PLAIN, 25));
        storeNumberPanel = new JPanel();
        storeNumberPanel.setLayout(new GridLayout(3, 2));
        storeNumberPanel.setBackground(Color.BLACK);
        storeNumberPanel.add(storeNumberDisplay);
        storeNumberPanel.add(numberDropdownMenu);
        storeNumberPanel.add(numStoreButton);
        content.add(storeNumberPanel, BorderLayout.CENTER);

        numStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                storeNumberPanel.setVisible(false);
                content.removeAll();
                content.revalidate();
                content.repaint();
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
        storeInfoPanel.add(storeNameDisplay);

        ArrayList<JTextField> storeText = new ArrayList<>();
        for (int i = 0; i < storeNum; i++) {
            storeText.add(new JTextField(20));
            storeText.get(i).setBackground(Color.lightGray);
            storeText.get(i).setFont(new Font("Serif", Font.PLAIN, 25));
            storeInfoPanel.add(storeText.get(i));
        }
        storeNameDisplay.setForeground(Color.WHITE);
        storeInfoPanel.setBackground(Color.BLACK);
        // Add storeField to the panel
        storeInfoPanel.add(submitStore);
        content.add(storeInfoPanel, BorderLayout.CENTER);
        // Use a variable to store the user input

        submitStore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assign the text input to the variable when the button is pressed
                String l = "";
                for (int i = 0; i < storeNum; i++) {
                    l += " " + storeText.get(i).getText().replace(" ", "_");
                }
                storeInfoPanel.setVisible(false);
                content.removeAll();
                content.revalidate();
                content.repaint();
                latch.countDown();
                try {
                    sendDataToServer("SIGNUP" + userInfo[0] + " " + userInfo[1] + " Producer" + l);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                displayWelcomePanel();
                content.revalidate();
                content.repaint();
            }
        });

        // Wait for latch or do any other necessary operations

        // Return the user input
    }

    public void displayCoSPanel() {
        latch = new CountDownLatch(1);
        consumer = new JButton("Consumer");
        producer = new JButton("Producer");
        consumer.addActionListener(actionListener);
        producer.addActionListener(actionListener);
        consumer.setFont(new Font("Serif", Font.PLAIN, 25));
        producer.setFont(new Font("Serif", Font.PLAIN, 25));

        consumerOrProduerPanel = new JPanel();
        consumerOrProduerPanel.setLayout(new GridLayout(1, 2));
        consumerOrProduerPanel.setBackground(Color.BLACK);
        consumerOrProduerPanel.add(consumer);
        consumerOrProduerPanel.add(producer);
        content.add(consumerOrProduerPanel, BorderLayout.CENTER);
        consumer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumerOrProduerPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
                SwingUtilities.invokeLater(() -> consumerBool = true);
                content.removeAll();
                content.revalidate();
                content.repaint();
                try {
                    sendDataToServer("SIGNUP" + userInfo[0] + " " + userInfo[1] + " Consumer");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                displayWelcomePanel();
                content.revalidate();
                content.repaint();
            }
        });

        producer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumerOrProduerPanel.setVisible(false);
                SwingUtilities.invokeLater(() -> login = true);
                SwingUtilities.invokeLater(() -> buttonClick = true);
                SwingUtilities.invokeLater(() -> loginIf = true);
                SwingUtilities.invokeLater(() -> consumerBool = false);
                content.removeAll();
                content.revalidate();
                content.repaint();
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

    public void displayLoginOptionPanel() {
        latch = new CountDownLatch(1);
        JLabel loginInstrucions = new JLabel("Select an Operation you would like to do");
        loginInstrucions.setFont(new Font("Serif", Font.PLAIN, 25));
        loginInstrucions.setBackground(Color.BLACK);
        loginInstrucions.setForeground(Color.WHITE);

        loginOptionButton = new JButton("Submit Info");
        loginOptionButton.addActionListener(actionListener);
        loginOptionButton.setFont(new Font("Serif", Font.PLAIN, 25));
        String[] choices = { "Send Message(1)", "Edit Message(2)", "Delete Message(3)", "Read Messages(4)",
            "Export Conversation(5)", "Import Conversation(6)", "Search Users(7)", "Block Users(8)", "Exit(9)" };
        JComboBox<String> stringDropdownMenu = new JComboBox<String>(choices);
        stringDropdownMenu.setFont(new Font("Serif", Font.PLAIN, 25));
        loginOptionPanel = new JPanel();
        loginOptionPanel.setBackground(Color.BLACK);
        loginOptionPanel.setLayout(new GridLayout(3, 3));

        loginOptionPanel.add(loginInstrucions);
        loginOptionPanel.add(stringDropdownMenu);
        loginOptionPanel.add(loginOptionButton);
        content.add(loginOptionPanel, BorderLayout.CENTER);

        loginOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginOptionPanel.setVisible(false);
                content.removeAll();
                content.revalidate();
                content.repaint();
                try {
                    switch (stringDropdownMenu.getSelectedIndex()) {
                        // Send Message
                        case 0:
                            content.setBackground(getBackground());
                            String[] stores = storesOrConsumers;
                            JComboBox<String> storeDropdownMenu = new JComboBox<String>(stores);
                            storeDropdownMenu.setFont(new Font("Serif", Font.PLAIN, 25));
                            JLabel storeBox = new JLabel("Please Select a User to Message");
                            storeBox.setFont(new Font("Serif", Font.PLAIN, 25));
                            storeBox.setForeground(Color.WHITE);

                            JTextField messageBox = new JTextField("Enter Message:");
                            messageBox.setFont(new Font("Serif", Font.PLAIN, 25));
                            messageBox.setBackground(Color.BLACK);
                            messageBox.setForeground(Color.WHITE);
                            messageBox.setEditable(false);

                            JTextField message = new JTextField(5);
                            message.setFont(new Font("Serif", Font.PLAIN, 25));
                            message.setBackground(Color.lightGray);

                            JTextField placeHolder = new JTextField("");
                            placeHolder.setBackground(Color.BLACK);
                            placeHolder.setEditable(false);

                            JButton submitStoreInfo = new JButton("Submit Info");
                            submitStoreInfo.setFont(new Font("Serif", Font.PLAIN, 25));

                            JPanel messagePanel = new JPanel();
                            messagePanel.setLayout(new GridLayout(3, 2));
                            messagePanel.setBackground(Color.BLACK);

                            messagePanel.add(storeBox);
                            messagePanel.add(storeDropdownMenu);
                            messagePanel.add(messageBox);
                            messagePanel.add(message);
                            messagePanel.add(placeHolder);
                            messagePanel.add(submitStoreInfo);

                            content.add(messagePanel, BorderLayout.CENTER);
                            content.setVisible(true);
                            submitStoreInfo.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (storeDropdownMenu.getSelectedItem().equals("exit")) {
                                        content.removeAll();
                                        content.revalidate();
                                        content.repaint();
                                        // latch = new CountDownLatch(1);
                                        displayWelcomePanel();
                                        content.revalidate();
                                        content.repaint();
                                    } else {
                                        try {
                                            sendDataToServer("SENDMESSAGE" + " " +
                                                    storeDropdownMenu.getSelectedItem() +
                                                    " " + userInfo[0] + " " + message.getText());
                                            JOptionPane.showMessageDialog(null, "Message Sent",
                                                    "Message Sent", JOptionPane.INFORMATION_MESSAGE);
                                            content.removeAll();
                                            content.revalidate();
                                            content.repaint();
                                            displayLoginOptionPanel();
                                            content.revalidate();
                                            content.repaint();
                                        } catch (IOException e1) {
                                        }
                                    }
                                }
                            });
                            break;
                        case 1:
                            displayMessageSelection(1);

                            // sendDataToServer("MANAGEEDIT");
                            break;
                        case 2:
                            displayMessageSelection(2);
                            // sendDataToServer("MANAGEDELETE");
                            break;
                        case 3:
                            displayMessageSelection(3);
                            // sendDataToServer("MANAGEREAD");
                            break;
                        case 4:
                            displayMessageSelection(4);
                            // sendDataToServer("MANAGEEXPORT");
                            break;
                        case 5:
                            JPanel importPanel = new JPanel();
                            importPanel.setLayout(new GridLayout(3, 2));
                            importPanel.setBackground(Color.BLACK);

                            String[] receivers = storesOrConsumers;
                            JComboBox<String> receiverDropdownMenu = new JComboBox<String>(receivers);
                            receiverDropdownMenu.setFont(new Font("Serif", Font.PLAIN, 25));
                            JLabel receiver = new JLabel("Enter the Receiver's Name:");
                            receiver.setFont(new Font("Serif", Font.PLAIN, 25));
                            receiver.setForeground(Color.WHITE);

                            JLabel importLabel = new JLabel("Please Enter The File Name");
                            importLabel.setFont(new Font("Serif", Font.PLAIN, 25));
                            importLabel.setForeground(Color.WHITE);

                            JTextField importField = new JTextField(20);
                            importField.setBackground(Color.lightGray);
                            importField.setFont(new Font("Serif", Font.PLAIN, 25));

                            JButton placeholder = new JButton("");
                            placeholder.setBackground(Color.BLACK);
                            placeholder.setForeground(Color.BLACK);
                            placeholder.setVisible(false);

                            JButton importButton = new JButton("Submit Info");
                            importButton.setFont(new Font("Serif", Font.PLAIN, 25));

                            importPanel.add(receiver);
                            importPanel.add(receiverDropdownMenu);
                            importPanel.add(importLabel);
                            importPanel.add(importField);
                            importPanel.add(placeholder);
                            importPanel.add(importButton);
                            content.add(importPanel, BorderLayout.CENTER);

                            importButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        JOptionPane.showMessageDialog(null, "File Imported",
                                                "File Imported", JOptionPane.INFORMATION_MESSAGE);
                                        sendDataToServer("MANAGEIMPORT" + " " +
                                                receiverDropdownMenu.getSelectedItem() +
                                                " " + importField.getText());
                                        content.removeAll();
                                        content.revalidate();
                                        content.repaint();
                                        displayLoginOptionPanel();
                                        content.revalidate();
                                        content.repaint();
                                    } catch (Exception e1) {
                                    }
                                }
                            });
                            break;
                        case 6:
                            JPanel searchPanel = new JPanel();
                            searchPanel.setLayout(new GridLayout(2, 2));
                            searchPanel.setBackground(Color.BLACK);

                            JLabel searchLabel = new JLabel("Please Enter The User you're looking for.");
                            searchLabel.setFont(new Font("Serif", Font.PLAIN, 25));
                            searchLabel.setForeground(Color.WHITE);

                            JTextField searchField = new JTextField(20);
                            searchField.setBackground(Color.lightGray);
                            searchField.setFont(new Font("Serif", Font.PLAIN, 25));

                            JButton searchPlaceHolder = new JButton("");
                            searchPlaceHolder.setBackground(Color.BLACK);
                            searchPlaceHolder.setForeground(Color.BLACK);
                            searchPlaceHolder.setVisible(false);

                            JButton searchButton = new JButton("Submit Info");
                            searchButton.setFont(new Font("Serif", Font.PLAIN, 25));

                            searchPanel.add(searchLabel);
                            searchPanel.add(searchField);
                            searchPanel.add(searchPlaceHolder);
                            searchPanel.add(searchButton);
                            content.add(searchPanel, BorderLayout.CENTER);
                            content.setVisible(true);

                            searchButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        sendDataToServer("SEARCH" + " " + userInfo[0] + " "
                                                + searchField.getText());
                                        content.removeAll();
                                        content.revalidate();
                                        content.repaint();
                                        String searchReturn = bfr.readLine();
                                        // System.out.println(searchReturn);
                                        if (searchReturn.isEmpty()) {
                                            JOptionPane.showMessageDialog(null,
                                                    "No users matching your search were found",
                                                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
                                        } else {
                                            String[] searchReturnArray = searchReturn.split(" ");
                                            String formattedResults = "";
                                            for (String s : searchReturnArray) {
                                                formattedResults += "\n" + s;
                                            }
                                            // System.out.println(formattedResults);
                                            JOptionPane.showMessageDialog(null,
                                                    "Here is a list of Users matching your search:" +
                                                            formattedResults,
                                                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
                                            displayLoginOptionPanel();
                                        }
                                        displayLoginOptionPanel();
                                        content.revalidate();
                                        content.repaint();
                                    } catch (Exception e1) {
                                    }
                                }
                            });
                            break;
                        case 7:
                            // BLOCKING
                            JPanel blockPanel = new JPanel();
                            blockPanel.setLayout(new GridLayout(2, 2));
                            blockPanel.setBackground(Color.BLACK);

                            JLabel blockInst = new JLabel("Select a User to Block");
                            blockInst.setFont(new Font("Serif", Font.PLAIN, 25));
                            blockInst.setForeground(Color.WHITE);

                            String[] messages = storesOrConsumers;
                            JComboBox<String> blockDropdownMenu = new JComboBox<String>(messages);
                            blockDropdownMenu.setFont(new Font("Serif", Font.PLAIN, 25));

                            JTextField blockPlaceHolder = new JTextField("");
                            blockPlaceHolder.setBackground(Color.BLACK);
                            blockPlaceHolder.setEditable(false);

                            JButton blockButton = new JButton("Submit Info");
                            blockButton.setFont(new Font("Serif", Font.PLAIN, 25));

                            blockPanel.add(blockInst);
                            blockPanel.add(blockDropdownMenu);
                            blockPanel.add(blockPlaceHolder);
                            blockPanel.add(blockButton);
                            content.add(blockPanel, BorderLayout.CENTER);
                            content.setVisible(true);

                            blockButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        content.removeAll();
                                        content.revalidate();
                                        content.repaint();
                                        sendDataToServer("BLOCK" + " " + blockDropdownMenu.getSelectedItem() + " " +
                                                userInfo[0]);
                                        JOptionPane.showMessageDialog(null,
                                                "You have successfully Blocked this user",
                                                "Block Success", JOptionPane.INFORMATION_MESSAGE);
                                        displayLoginOptionPanel();
                                        content.revalidate();
                                        content.repaint();
                                    } catch (IOException el) {
                                        el.printStackTrace();
                                    }
                                }
                            });

                            break;
                        case 8:
                            content.removeAll();
                            content.revalidate();
                            content.repaint();
                            displayWelcomePanel();
                            content.revalidate();
                            content.repaint();
                            break;
                        default:
                            break;
                    }
                    latch.countDown();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        content.revalidate();
        content.repaint();
    }

    public void displayMessageSelection(int actionType) {
        content.setBackground(getBackground());
        String[] messages = storesOrConsumers;
        JComboBox<String> messageDropdownMenu = new JComboBox<String>(messages);
        messageDropdownMenu.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel pickMessageBox = new JLabel("Please Select a Conversation you want to preform the " +
                "designated action on.");
        pickMessageBox.setFont(new Font("Serif", Font.PLAIN, 25));
        pickMessageBox.setForeground(Color.WHITE);

        JTextField readPlaceHolder = new JTextField("");
        readPlaceHolder.setBackground(Color.BLACK);
        readPlaceHolder.setEditable(false);

        JButton submitPickedMessage = new JButton("Submit Info");
        submitPickedMessage.setFont(new Font("Serif", Font.PLAIN, 25));

        JPanel readPanel = new JPanel();
        readPanel.setLayout(new GridLayout(2, 2));
        readPanel.setBackground(Color.BLACK);

        readPanel.add(pickMessageBox);
        readPanel.add(messageDropdownMenu);
        readPanel.add(readPlaceHolder);
        readPanel.add(submitPickedMessage);

        content.add(readPanel, BorderLayout.CENTER);
        content.setVisible(true);

        submitPickedMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    content.removeAll();
                    content.revalidate();
                    content.repaint();
                    if (messageDropdownMenu.getSelectedItem().equals("exit")) {
                        displayLoginOptionPanel();
                        content.revalidate();
                        content.repaint();
                    } else {
                        switch (actionType) {
                            case 1:
                                content.removeAll();
                                content.revalidate();
                                content.repaint();
                                editMessagePanel((String) messageDropdownMenu.getSelectedItem());
                                // sendDataToServer("MANAGEEDIT " + " " + messageDropdownMenu.getSelectedItem()
                                // + " " +
                                // userInfo[0]);
                                // displayConversation();
                                // displayEditPanel
                                break;
                            case 2:
                                content.removeAll();
                                content.revalidate();
                                content.repaint();
                                deleteMessagePanel((String) messageDropdownMenu.getSelectedItem());
                                // displayDeletePanel
                                break;
                            case 3:
                                sendDataToServer("MANAGEREAD " + " " + messageDropdownMenu.getSelectedItem() + " " +
                                        userInfo[0]);
                                // System.out.println("WE ARE IN READ");
                                String ln = bfr.readLine();
                                ln = ln.replace(", ", "<br>");
                                content.removeAll();
                                content.revalidate();
                                content.repaint();
                                displaySpecificMessagePanel(ln);
                                // displayReadPanel
                                break;
                            case 4:
                                // System.out.println("Got to export");
                                sendDataToServer("MANAGEEXPORT " + messageDropdownMenu.getSelectedItem() + " " +
                                        userInfo[0]);
                                JOptionPane.showMessageDialog(null,
                                        "CSV File has been Exported", "Export Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                                content.removeAll();
                                content.revalidate();
                                content.repaint();
                                displayLoginOptionPanel();
                                content.repaint();
                                content.revalidate();

                                // displayExportPanel, might not even need that and can just do a JOptionPanel
                                break;
                        }
                    }
                    // displayLoginOptionPanel();
                    // content.revalidate();
                    // content.repaint();
                } catch (IOException el) {
                    el.printStackTrace();
                }
            }
        });
    }

    public void deleteMessagePanel(String name) {
        latch = new CountDownLatch(1);
        JLabel label = new JLabel("What Line Number Would You Like To Delete?");
        label.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel empty = new JLabel("");
        empty.setBackground(Color.BLACK);

        deleteButton = new JButton("SubmitInfo");
        deleteButton.addActionListener(actionListener);
        deleteButton.setFont(new Font("Serif", Font.PLAIN, 25));
        JTextField editField = new JTextField();
        editField.setBackground(Color.WHITE);
        editField.setEditable(true);

        deletePanel = new JPanel();
        label.setForeground(Color.WHITE);
        deletePanel.setBackground(Color.BLACK);
        deletePanel.setLayout(new GridLayout(3, 2));
        deletePanel.add(label);
        deletePanel.add(editField);
        deletePanel.add(empty);
        deletePanel.add(deleteButton);
        content.add(deletePanel, BorderLayout.CENTER);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println(editField.getText() + " text: " + editField2.getText());
                int lineNum;
                try {
                    lineNum = Integer.parseInt(editField.getText()) + 1;
                    // System.out.println(lineNum);
                    sendDataToServer("MANAGEDELETE " + name + " " + lineNum);
                    content.removeAll();
                    content.revalidate();
                    content.repaint();
                    displayLoginOptionPanel();
                    content.revalidate();
                    content.repaint();

                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, "Please enter a number " +
                            "in the box!", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void editMessagePanel(String name) {
        latch = new CountDownLatch(1);
        JLabel label = new JLabel("What Line Number Would You Like To Edit?");
        label.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel label2 = new JLabel("What would you like the new text to say?");
        label2.setFont(new Font("Serif", Font.PLAIN, 25));
        label2.setForeground(Color.WHITE);
        JLabel empty = new JLabel("");
        empty.setBackground(Color.BLACK);

        editButton = new JButton("SubmitInfo");
        editButton.addActionListener(actionListener);
        editButton.setFont(new Font("Serif", Font.PLAIN, 25));
        JTextField editField = new JTextField();
        editField.setBackground(Color.WHITE);
        editField.setEditable(true);
        JTextField editField2 = new JTextField();
        editField.setBackground(Color.WHITE);
        editField.setEditable(true);

        editPanel = new JPanel();
        label.setForeground(Color.WHITE);
        editPanel.setBackground(Color.BLACK);
        editPanel.setLayout(new GridLayout(3, 2));
        editPanel.add(label);
        editPanel.add(editField);
        editPanel.add(label2);
        editPanel.add(editField2);
        editPanel.add(empty);
        editPanel.add(editButton);
        content.add(editPanel, BorderLayout.CENTER);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // System.out.println(editField.getText() + " text: " + editField2.getText());
                int lineNum;
                try {
                    lineNum = Integer.parseInt(editField.getText());
                    sendDataToServer("MANAGEEDIT " + name + " " + (lineNum - 1) + " " + editField2.getText());
                    content.removeAll();
                    content.revalidate();
                    content.repaint();
                    displayLoginOptionPanel();
                    content.revalidate();
                    content.repaint();

                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(null, "Please enter a number " +
                            "in the first box!", "BlankMessaging", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void displaySpecificMessagePanel(String message) {
        latch = new CountDownLatch(1);
        JLabel label = new JLabel("<html>" + message + "</html>");
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setFont(new Font("Serif", Font.PLAIN, 25));
        backFromMessageButton = new JButton("Back");
        backFromMessageButton.addActionListener(actionListener);
        backFromMessageButton.setFont(new Font("Serif", Font.PLAIN, 25));
        messagePanel = new JPanel();
        label.setForeground(Color.WHITE);
        messagePanel.setBackground(Color.BLACK);
        messagePanel.setLayout(new GridLayout(2, 1));
        messagePanel.add(label);
        messagePanel.add(backFromMessageButton);
        content.add(messagePanel, BorderLayout.CENTER);
    }

    public void selectEditMessage() {
        content.setBackground(getBackground());

        String[] lineNums = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "exit" };
        JComboBox<String> lineNumsDropdown = new JComboBox<String>(lineNums);
        lineNumsDropdown.setFont(new Font("Serif", Font.PLAIN, 25));
        JLabel editBox = new JLabel("Please Select the Number of the Message you'd like to Edit");
        editBox.setFont(new Font("Serif", Font.PLAIN, 25));
        editBox.setForeground(Color.WHITE);

        JTextField editInstructions = new JTextField("Enter the Edited message");
        editInstructions.setFont(new Font("Serif", Font.PLAIN, 25));
        editInstructions.setBackground(Color.BLACK);
        editInstructions.setForeground(Color.WHITE);
        editInstructions.setEditable(false);

        JTextField editInput = new JTextField(5);
        editInput.setFont(new Font("Serif", Font.PLAIN, 25));
        editInput.setBackground(Color.lightGray);

        JTextField placeHolder = new JTextField("");
        placeHolder.setBackground(Color.BLACK);
        placeHolder.setEditable(false);

        JButton submitMessageSelection = new JButton("Submit Info");
        submitMessageSelection.setFont(new Font("Serif", Font.PLAIN, 25));

        JPanel displayConversationPanel = new JPanel();
        displayConversationPanel.setLayout(new GridLayout(3, 2));
        displayConversationPanel.setBackground(Color.BLACK);

        displayConversationPanel.add(editBox);
        displayConversationPanel.add(lineNumsDropdown);
        displayConversationPanel.add(editInstructions);
        displayConversationPanel.add(editInput);
        displayConversationPanel.add(placeHolder);
        displayConversationPanel.add(submitMessageSelection);

        content.add(displayConversationPanel, BorderLayout.CENTER);
        content.setVisible(true);
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        SwingUtilities.invokeLater(new BlankClient());
    } // main method

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
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    if (bfr != null)
                        bfr.close();
                    if (writer != null)
                        writer.close();
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    } // run

    public void talkin() {
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
                            content.removeAll();
                            content.revalidate();
                            content.repaint();
                            displayLoginSignUpPanel();
                            content.revalidate();
                            content.repaint();
                            // latch.await();
                            if (!(conditional == null)) {
                                if (loginIf) {
                                    conditional = null;
                                    loginIf = false;
                                    content.removeAll();
                                    content.revalidate();
                                    content.repaint();
                                    if (login && login2) {
                                        login = false;
                                        login2 = false;
                                        userInfo[0] = "";
                                        userInfo[1] = "";
                                        displayLoginPanel();
                                        content.revalidate();
                                        content.repaint();
                                        latch.await();
                                        content.removeAll();
                                        content.revalidate();
                                        content.repaint();
                                        displayLoginOptionPanel();
                                        content.revalidate();
                                        content.repaint();
                                        //// System.out.println(userInfo[0] + " " + userInfo[1]);
                                    } else if (login) {
                                        login = false;
                                        // ////System.out.println();
                                        // } else {
                                        userInfo[0] = "";
                                        //// System.out.println("here again");
                                        //// System.out.println("usklsdjfl " + userInfo[0]);
                                        displaySignUpPanel();
                                        content.revalidate();
                                        content.repaint();
                                        latch.await();

                                        content.removeAll();
                                        content.revalidate();
                                        content.repaint();
                                        //// System.out.println(userInfo[0]+" "+userInfo[1]);
                                        displayCoSPanel();
                                        content.revalidate();
                                        content.repaint();
                                        latch.await();
                                    }
                                }
                            }
                            SwingUtilities.invokeLater(() -> login = false);
                            login = false;
                            login2 = false;
                            loginIf = false;
                            // SwingUtilities.invokeLater(() -> loginIf = false);
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
                        if (bfr != null)
                            bfr.close();
                        if (writer != null)
                            writer.close();
                        if (socket != null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        worker.execute();
    }
}