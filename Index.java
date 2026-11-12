package WindowsCode;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import MainCode.User;


public class Index {
    JFrame fenetre  = new JFrame("Plagiarism Detector - Index");
    public void createWindows(){
        fenetre.setSize(1000, 1000);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setLayout(null);

        JLabel greeting = new JLabel("Welcome to Plagiarism Detector");
        greeting.setFont(new Font("Arial", Font.BOLD, 24));
        greeting.setBounds(320, 30, 400, 40);
        fenetre.add(greeting);

        JButton button1 = new JButton("Log In");
        button1.setBounds(400, 100, 200, 50);
        fenetre.add(button1);
        button1.addActionListener(e -> showLoginDialog());

        JButton button2 = new JButton("Inscription");
        button2.setBounds(400, 170, 200, 50);
        fenetre.add(button2);
        button2.addActionListener(e -> showInscriptionDialog());

        JButton button3 = new JButton("Exit");
        button3.setBounds(400, 240, 200, 50);
        fenetre.add(button3);
        button3.addActionListener(e -> System.exit(0));

        fenetre.setVisible(true);
    }

    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(fenetre, "Log In", true);
        loginDialog.setSize(400, 300);
        loginDialog.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 30);
        loginDialog.add(usernameLabel);

        // Username Field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 30);
        loginDialog.add(usernameField);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 30);
        loginDialog.add(passwordLabel);

        // Password Field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 30);
        loginDialog.add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Log In");
        loginButton.setBounds(150, 160, 100, 40);
        loginDialog.add(loginButton);

        loginButton.addActionListener(e -> {
            User user1 = new User(usernameField.getText(), new String(passwordField.getPassword()));
            if (user1.connection()) {
                loginDialog.dispose();
                fenetre.dispose();
                Home home_window = new Home();
                home_window.createHomeWindow(user1.getUsername(), fenetre);
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid username or password.");
            }
        });

        loginDialog.setVisible(true);
    }

    private void showInscriptionDialog() {
        JDialog inscriptionDialog = new JDialog(fenetre, "Inscription", true);
        inscriptionDialog.setSize(400, 300);
        inscriptionDialog.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 30);
        inscriptionDialog.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 30);
        inscriptionDialog.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 30);
        inscriptionDialog.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 30);
        inscriptionDialog.add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 160, 100, 40);
        inscriptionDialog.add(registerButton);

        registerButton.addActionListener(e -> {
            User user1 = new User(usernameField.getText(), new String(passwordField.getPassword()));
            if(user1.inscription()){
                JOptionPane.showMessageDialog(inscriptionDialog, "Registration Successful!");
                inscriptionDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(inscriptionDialog, "Username already exists. Choose another Username!");
            }
        });

        inscriptionDialog.setVisible(true);
    }
}
