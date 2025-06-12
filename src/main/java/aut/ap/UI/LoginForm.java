package aut.ap.UI;

import aut.ap.controller.EmailController;
import aut.ap.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class LoginForm {
    private static final Logger log = LoggerFactory.getLogger(LoginForm.class);
    private final UserController userController;
    private final EmailController emailController;

    public LoginForm(UserController userController, EmailController emailController) {
        this.userController = userController;
        this.emailController = emailController;
        createUI();
    }

    public void createUI() {
        // Create Frame and configure it.
        JFrame loginFrame = new JFrame();
        loginFrame.setTitle("Email App login");
        loginFrame.setSize(new Dimension(450, 300));
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);

        // Create tab page.
        JTabbedPane tabbedPane = new JTabbedPane();

        // Login Panel.
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Login panel field.
        JTextField loginEmail = new JTextField();
        JPasswordField loginPassword = new JPasswordField();
        JButton loginButton = new JButton("Login");

        // Register Panel.
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Register panel field.
        JTextField regUsername = new JTextField();
        JTextField regEmail = new JTextField();
        JPasswordField regPassword = new JPasswordField();
        JButton regButton = new JButton("Register");

        // fields.
        JLabel userName = new JLabel("Username:");
        JLabel email = new JLabel("Email:");
        JLabel password = new JLabel("Password");

        // Add component to login panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // فاصله بین اجزا
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(email, gbc);

        // Email Field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        loginPanel.add(loginEmail, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        loginPanel.add(password, gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        loginPanel.add(loginPassword, gbc);

        // Login Button
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        loginPanel.add(loginButton, gbc);

        // GridBagConstraints جدید برای ریجستر پنل
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5); // فاصله بین اجزا
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1.0;

        // Username Label
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        registerPanel.add(new JLabel("Username:"), gbc2);

        // Username Field
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        registerPanel.add(regUsername, gbc2);

        // Email Label
        gbc2.gridx = 0;
        gbc2.gridy = 1;
        registerPanel.add(new JLabel("Email:"), gbc2);

        // Email Field
        gbc2.gridx = 1;
        gbc2.gridy = 1;
        registerPanel.add(regEmail, gbc2);

        // Password Label
        gbc2.gridx = 0;
        gbc2.gridy = 2;
        registerPanel.add(new JLabel("Password:"), gbc2);

        // Password Field
        gbc2.gridx = 1;
        gbc2.gridy = 2;
        registerPanel.add(regPassword, gbc2);

        // Register Button (زیر همه، دو ستونه)
        gbc2.gridx = 0;
        gbc2.gridy = 3;
        gbc2.gridwidth = 2;
        registerPanel.add(regButton, gbc2);


        // Add Action listener.
        loginButton.addActionListener(action -> {
            String userEmail = loginEmail.getText();
            String userPassword = String.valueOf(loginPassword.getPassword());
            if (userController.login(userEmail, userPassword)) {
                loginFrame.dispose();
                // TODO: home page shows
                new HomePage(emailController, userController);
            }
            else {
                JOptionPane.showMessageDialog(loginFrame, "Login failed");
            }
        });

        regButton.addActionListener(action -> {
            String userUserName = regUsername.getText();
            String userEmail = regEmail.getText();
            String userPassword = String.valueOf(regPassword.getPassword());
            userController.register(userUserName, userEmail, userPassword);
            JOptionPane.showMessageDialog(loginFrame, "Congratulation! now login and enjoy.");
        });


        // Add panels to tabbed pane.
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Register", registerPanel);

        // Add tabbedPane to frame.
        loginFrame.add(tabbedPane);

        loginFrame.setVisible(true);
    }
}
