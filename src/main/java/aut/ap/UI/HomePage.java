package aut.ap.UI;

import aut.ap.controller.EmailController;
import aut.ap.controller.UserController;
import aut.ap.entity.Email;
import aut.ap.entity.User;
import aut.ap.repository.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomePage {
    protected final EmailController emailController;
    protected final UserController userController;
    private final JPanel emailPanel;
    private final JPanel userListPanel;

    public HomePage(EmailController emailController, UserController userController) {
        this.emailController = emailController;
        this.userController = userController;

        JFrame homePage = new JFrame("Home Page");
        homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homePage.setSize(new Dimension(800, 600));
        homePage.setLocationRelativeTo(null);
        homePage.setLayout(new GridBagLayout());

        // User panel (left side).
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(Color.BLACK);

        // User list panel
        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setOpaque(false);

        // New conversion button.
        JButton newConvBtn = new JButton("New Conversation");
        newConvBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        newConvBtn.addActionListener(e -> openNewConversationDialog());

        newConvBtn.addActionListener(action -> {
            String emailAddress = JOptionPane.showInputDialog(null, "enter the email: ");

            if (emailAddress != null && !emailAddress.trim().isEmpty()) {
                User newContact = userController.findUser(emailAddress);
                if (newContact != null) {
                    if (!isUserAlreadyAdded(emailAddress)) {
                        UserComponent tmp = new UserComponent(newContact);
                        userListPanel.add(tmp);
                        userListPanel.revalidate();
                        userListPanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Contact already exist.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "not found.");
                }
            }
        });

        // Email panel (right side).
        emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        emailPanel.setBackground(Color.WHITE);

        // Setting menu (up right of panel).
        JPanel emailTopPanel = new JPanel(new BorderLayout());
        emailTopPanel.setBackground(Color.WHITE);

        // Setting button.
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> openSettingsDialog());

        // User scroll and email scroll
        JScrollPane userScrollPane = new JScrollPane(userPanel);
        JScrollPane emailScrollPane = new JScrollPane(emailPanel);

        userScrollPane.setPreferredSize(new Dimension(270, 0));
        emailScrollPane.setPreferredSize(new Dimension(510, 0));

        // Send email button.
        JButton sendEmail = new JButton("send email");

        sendEmail.addActionListener(action -> {

        });

        // Add setting button to setting panel.
        emailTopPanel.add(settingsButton, BorderLayout.EAST);

        // Add users to user panel.
        List<User> contact = userController.showContact();
        for (User usr : contact) {
            UserComponent tmp = new UserComponent(usr);
            userListPanel.add(tmp);
        }

        userPanel.add(userListPanel);
        userPanel.add(Box.createVerticalStrut(10));
        userPanel.add(newConvBtn);


        // Add field to Frame.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 1.15;
        homePage.add(userScrollPane, gbc);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(emailTopPanel, BorderLayout.NORTH);
        rightPanel.add(emailScrollPane, BorderLayout.CENTER);
        rightPanel.add(sendEmail, BorderLayout.SOUTH);

        gbc.gridx = 1;
        gbc.weightx = 2;
        homePage.add(rightPanel, gbc);

        homePage.setVisible(true);
    }

    private void openNewConversationDialog() {

    }

    private void openSettingsDialog() {

    }

    private boolean isUserAlreadyAdded(String email) {
        for (Component c : userListPanel.getComponents()) {
            if (c instanceof UserComponent uc && (uc.getUser().getUserName().equals(email) || uc.getUser().getEmail().equals(email))) {
                return true;
            }
        }
        return false;
    }


    class UserComponent extends JPanel {
        private final User user;

        public UserComponent(User user) {
            this.user = user;
            setLayout(new BorderLayout());
            setBackground(new Color(230, 230, 230));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            // Field for UserComponent
            JLabel username = new JLabel("Username: " + user.getUserName());
            JLabel email = new JLabel("Email: " + user.getEmail());
            JButton openChat = new JButton("Open Chat");

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);

            // Add Action.
            openChat.addActionListener(action -> {
                List<Email> emails = emailController.showContactEmail(user);
                emailPanel.removeAll();

                for (Email e : emails) {
                    EmailComponent tmp = new EmailComponent(e);
                    emailPanel.add(tmp);
                }
                emailPanel.revalidate();
                emailPanel.repaint();
            });

            // Add fields to parents.
            infoPanel.add(username);
            infoPanel.add(email);

            add(infoPanel, BorderLayout.CENTER);
            add(openChat, BorderLayout.EAST);

            setMaximumSize(new Dimension(Short.MAX_VALUE, 70));
        }

        public User getUser() {
            return user;
        }
    }

    class EmailComponent extends JPanel {
        private final Email email;

        public EmailComponent(Email email) {
            this.email = email;

            configureMailPanel();
            add(senderPanel(), BorderLayout.NORTH);
            add(body(), BorderLayout.CENTER);
            add(buttonPanel(), BorderLayout.SOUTH);
        }

        private void configureMailPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.WHITE);
            setMaximumSize(new Dimension(Short.MAX_VALUE, 350));
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }

        private JPanel senderPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setOpaque(false);

            User sender = email.getSender();
            String senderName = sender != null ? sender.getUserName() : "Unknown";
            String senderEmail = sender != null ? sender.getEmail() : "Unknown";

            JLabel senderLabel = new JLabel(senderName + " <" + senderEmail + ">");
            senderLabel.setFont(senderLabel.getFont().deriveFont(Font.BOLD, 14f));

            panel.add(senderLabel);

            return panel;
        }

        private JPanel refPanel() {
            JPanel referencePanel = new JPanel();

            Email replyTo = email.getReply();
            Email forwarded = email.getForward();

            if (replyTo != null || forwarded != null) {
                Email refEmail = replyTo != null ? replyTo : forwarded;
                String refType = replyTo != null ? "Reply to:" : "Forwarded from:";

                User refSender = refEmail.getSender();
                String refSenderName = refSender != null ? refSender.getUserName() : "Unknown";
                String refSenderEmail = refSender != null ? refSender.getEmail() : "Unknown";

                String refBodySnippet = refEmail.getBody();
                if (refBodySnippet.length() > 100) {
                    refBodySnippet = refBodySnippet.substring(0, 100) + "...";
                }


                referencePanel.setLayout(new BorderLayout());
                referencePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                referencePanel.setBackground(new Color(245, 245, 245));
                referencePanel.setMaximumSize(new Dimension(400, 100));
                referencePanel.setPreferredSize(new Dimension(400, 80));
                referencePanel.setMinimumSize(new Dimension(400, 80));

                JLabel refLabel = new JLabel("<html><b>" + refType + "</b> " + refSenderName + " &lt;" + refSenderEmail + "&gt;</html>");
                JTextArea refBodyArea = new JTextArea(refBodySnippet);
                refBodyArea.setLineWrap(true);
                refBodyArea.setWrapStyleWord(true);
                refBodyArea.setEditable(false);
                refBodyArea.setBackground(new Color(245, 245, 245));
                refBodyArea.setFont(new Font("Serif", Font.ITALIC, 12));
                refBodyArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                referencePanel.add(refLabel, BorderLayout.NORTH);
                referencePanel.add(refBodyArea, BorderLayout.CENTER);

            }
            return referencePanel;
        }

        private JPanel body() {
            JPanel body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setOpaque(false);

            JTextArea emailBodyArea = new JTextArea();
            emailBodyArea.setLineWrap(true);
            emailBodyArea.setWrapStyleWord(true);
            emailBodyArea.setEditable(false);
            emailBodyArea.setFont(new Font("Serif", Font.PLAIN, 14));
            emailBodyArea.setText(email.getBody());
            emailBodyArea.setCaretPosition(0);
            emailBodyArea.setMaximumSize(new Dimension(400, Short.MAX_VALUE));

            JScrollPane bodyScrollPane = new JScrollPane(emailBodyArea);
            bodyScrollPane.setBorder(BorderFactory.createEmptyBorder());
            bodyScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            bodyScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            bodyScrollPane.setPreferredSize(new Dimension(400, 200));

            body.add(refPanel());
            body.add(bodyScrollPane);

            return body;
        }

        private JPanel buttonPanel() {
            JPanel bottomPanel = new JPanel(new BorderLayout());

            LocalDateTime sendTime = email.getSendTime();
            String formattedDate = sendTime != null
                    ? sendTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    : "Unknown Date";

            JLabel dateLabel = new JLabel(formattedDate);
            dateLabel.setFont(dateLabel.getFont().deriveFont(Font.PLAIN, 10f));

            bottomPanel.add(dateLabel, BorderLayout.WEST);
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            buttonsPanel.setOpaque(false);

            JButton replyButton = new JButton("Reply");
            replyButton.addActionListener(e -> {

            });

            JButton forwardButton = new JButton("Forward");
            forwardButton.addActionListener(e -> {
                String emailAddress = JOptionPane.showInputDialog(null, "Enter email: ");
                if (emailAddress != null && !emailAddress.trim().isEmpty()) {
                    emailController.sendEmail(email.getSubject(), email.getBody(), UserController.getCurrentUser(), email.getReply(), email, emailAddress);
                }
            });

            JButton markAsRead = new JButton("Mark as read");
            markAsRead.addActionListener(action -> {
                markAsRead.setBackground(Color.green);
                emailController.readEmail(email);
            });

            buttonsPanel.add(replyButton);
            buttonsPanel.add(forwardButton);
            if (!email.getSender().getEmail().equals(UserController.getCurrentUser().getEmail())) {
                // TODO: check if is read turn to green
                buttonsPanel.setBackground(Color.RED);
                buttonsPanel.add(markAsRead);
            }

            bottomPanel.add(buttonsPanel, BorderLayout.EAST);

            return bottomPanel;
        }

        private void replyEmail() {
        }
    }
}
