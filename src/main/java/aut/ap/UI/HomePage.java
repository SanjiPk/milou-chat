package aut.ap.UI;

import aut.ap.controller.EmailController;
import aut.ap.controller.UserController;
import aut.ap.entity.Email;
import aut.ap.entity.User;
import aut.ap.repository.UserRepository;
import com.sun.jdi.event.ExceptionEvent;
import org.hibernate.mapping.Collection;

import javax.print.attribute.standard.JobOriginatingUserName;
import javax.swing.*;
import java.awt.*;
import java.text.CollationElementIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class HomePage {
    protected final EmailController emailController;
    protected final UserController userController;
    private final JPanel emailPanel;
    private final JPanel userListPanel;

    public HomePage(EmailController emailController, UserController userController) {
        this.emailController = emailController;
        this.userController = userController;

        JFrame homePage = configureMainFrame();

        JPanel leftPanel = configureUserPanel();

        JPanel rightPanel = new JPanel(new BorderLayout());

        userListPanel = configureUserListPanel();

        emailPanel = configureEmailPanel();

        JPanel emailTopPanel = configureTopEmailPanel();

        // User scroll and email scroll
        JScrollPane userScrollPane = new JScrollPane(leftPanel);
        JScrollPane emailScrollPane = new JScrollPane(emailPanel);

        userScrollPane.setPreferredSize(new Dimension(270, 0));
        emailScrollPane.setPreferredSize(new Dimension(510, 0));

        // New conversion button.
        JButton newConvBtn = new JButton("New Conversation");
        newConvBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        newConvBtn.addActionListener(action -> newConversionAction(homePage));

        // Setting button.
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> openSettingsDialog());

        // Email type button.
        JButton emailType = configureEmailTypeButton();

        // Send email button.
        JButton sendEmail = new JButton("send email");
        sendEmail.addActionListener(action -> sendEmailAction(homePage));

        // Add setting button to setting panel.
        emailTopPanel.add(settingsButton, BorderLayout.EAST);
        emailTopPanel.add(emailType, BorderLayout.WEST);

        // Add Users.
        addUserToUserList();

        // Add UnreadEmails.
        unreadEmail();

        // Add field to rightPanel.
        rightPanel.add(emailTopPanel, BorderLayout.NORTH);
        rightPanel.add(emailScrollPane, BorderLayout.CENTER);
        rightPanel.add(sendEmail, BorderLayout.SOUTH);

        // Add field to leftPanel.
        leftPanel.add(userListPanel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(newConvBtn);

        // Add field to Frame.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 1.15;
        homePage.add(userScrollPane, gbc);

        gbc.gridx = 1;
        gbc.weightx = 2;
        homePage.add(rightPanel, gbc);

        homePage.setVisible(true);
    }

    private JFrame configureMainFrame() {
        JFrame homePage = new JFrame("Home Page");
        homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homePage.setSize(new Dimension(800, 600));
        homePage.setLocationRelativeTo(null);
        homePage.setLayout(new GridBagLayout());
        return homePage;
    }

    private JPanel configureUserPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(Color.BLACK);
        return userPanel;
    }

    private JPanel configureUserListPanel() {
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setOpaque(false);
        return userListPanel;
    }

    private JPanel configureEmailPanel() {
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        emailPanel.setBackground(Color.WHITE);
        return emailPanel;
    }

    private JPanel configureTopEmailPanel() {
        JPanel emailTopPanel = new JPanel(new BorderLayout());
        emailTopPanel.setBackground(Color.WHITE);
        return emailTopPanel;
    }

    private JButton configureEmailTypeButton() {
        JButton emailType = new JButton("Unread email");
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem unreadEmail = new JMenuItem("Unread email");
        JMenuItem allEmail = new JMenuItem("All emails");
        JMenuItem findEmailById = new JMenuItem("find by id");

        unreadEmail.addActionListener(action -> {
            try {
                List<Email> emails = emailController.getUnreadEmail();
                emailType.setText("Unread email");
                emailPanel.removeAll();
                if (emails == null) {
                    JOptionPane.showMessageDialog(null, "There is no unread email here.");
                } else {
                    for (Email email : emails) {
                        EmailComponent tmp = new EmailComponent(email);
                        emailPanel.add(tmp);
                    }
                    emailPanel.revalidate();
                    emailPanel.repaint();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Can't show unread message.");
            }
        });

        allEmail.addActionListener(action -> {
            try {
                List<Email> emails = emailController.getAllEmail();
                emailType.setText("All emails");
                emailPanel.removeAll();
                if (emails == null) {
                    JOptionPane.showMessageDialog(null, "There is no email here.");
                } else {
                    for (Email email : emails) {
                        EmailComponent tmp = new EmailComponent(email);
                        emailPanel.add(tmp);
                    }
                    emailPanel.revalidate();
                    emailPanel.repaint();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Can't show any message.");
            }
        });

        findEmailById.addActionListener(action -> {
            String sId = JOptionPane.showInputDialog(null, "Enter email id: ");
            int id;
            try {
                id = Integer.parseInt(sId);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid input.");
                return;
            }

            try {
                Email email = emailController.findEmailByCode(id);
                EmailComponent tmp = new EmailComponent(email);
                emailType.setText(String.format("email id: %d", email.getId()));
                emailPanel.removeAll();
                emailPanel.add(tmp);
                emailPanel.revalidate();
                emailPanel.repaint();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Can't find email.");
            }
        });

        popupMenu.add(unreadEmail);
        popupMenu.add(allEmail);
        popupMenu.add(findEmailById);

        emailType.addActionListener(action -> {
            popupMenu.show(emailType, 0, emailType.getHeight());
        });

        return emailType;
    }

    private void addUserToUserList() {
        // Add users to user panel.
        List<User> contact;
        try {
            contact = userController.showContact();
            userListPanel.removeAll();
            for (User usr : contact) {
                UserComponent tmp = new UserComponent(usr);
                userListPanel.add(tmp);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error for showing Contact.");
        }
    }

    private void newConversionAction(JFrame homePage) {
        User newContact = findUser(homePage);

//        assert newContact != null;
        if (newContact != null && isUserAlreadyAdded(newContact.getEmail())) {
            UserComponent tmp = new UserComponent(newContact);
            userListPanel.add(tmp);
            userListPanel.revalidate();
            userListPanel.repaint();
        }
    }

    private void sendEmailAction(JFrame homePage) {
        User receiver = findUser(homePage);
        if (receiver != null) {
            new SendEmailDialog(homePage, receiver, null);
            addUserToUserList();
            userListPanel.revalidate();
            userListPanel.repaint();
        }
    }

    private void openSettingsDialog() {

    }

    private void unreadEmail() {
        try {
            List<Email> emails = emailController.getUnreadEmail();
            emailPanel.removeAll();
            for (Email email : emails) {
                EmailComponent tmp = new EmailComponent(email);
                emailPanel.add(tmp);
            }
            emailPanel.revalidate();
            emailPanel.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Can't show unread message.");
        }
    }

    private boolean isUserAlreadyAdded(String email) {
        for (Component c : userListPanel.getComponents()) {
            if (c instanceof UserComponent uc && (uc.getUser().getUserName().equals(email) || uc.getUser().getEmail().equals(email))) {
                return true;
            }
        }
        return false;
    }

    private User findUser(JFrame homePage) {
        String emailAddress = JOptionPane.showInputDialog(null, "enter the email: ");

        User receiver = null;
        if (emailAddress == null || emailAddress.trim().isEmpty()) {
            return null;
        }
        try {
            receiver = userController.findUser(emailAddress);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(homePage, "There is an error to find user.");
        }
        if (receiver == null) {
            JOptionPane.showMessageDialog(homePage, "User not exist.");
            return null;
        }
        return receiver;
    }


    class UserComponent extends JPanel {
        private final User user;

        public UserComponent(User user) {
            this.user = user;
            configureMainPanel();

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);

            // Field for UserComponent
            JLabel username = new JLabel("Username: " + user.getUserName());
            JLabel email = new JLabel("Email: " + user.getEmail());
            JButton openChat = new JButton("Open Chat");

            // Add Action.
            openChat.addActionListener(action -> openChatAction());

            // Add fields to parents.
            infoPanel.add(username);
            infoPanel.add(email);

            add(infoPanel, BorderLayout.CENTER);
            add(openChat, BorderLayout.EAST);

            setMaximumSize(new Dimension(Short.MAX_VALUE, 70));
        }

        private void configureMainPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(230, 230, 230));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        private void openChatAction() {
            try {
                List<Email> emails = emailController.showContactEmail(user);
                Collections.sort(emails);
                emailPanel.removeAll();

                for (Email e : emails) {
                    EmailComponent tmp = new EmailComponent(e);
                    emailPanel.add(tmp);
                }
                emailPanel.revalidate();
                emailPanel.repaint();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error occur.");
            }
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
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            User sender = email.getSender();
            String senderEmail = sender != null ? sender.getEmail() : "Unknown";

            JLabel subject = new JLabel(email.getSubject());
            subject.setFont(subject.getFont().deriveFont(Font.BOLD, 20f));

            JLabel senderLabel = new JLabel("<" + senderEmail + ">");

            panel.add(subject);
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

            JButton markAsRead = new JButton("Mark as read");

            JButton replyButton = new JButton("Reply");
            replyButton.addActionListener(action -> replyAction(markAsRead));

            JButton forwardButton = new JButton("Forward");
            forwardButton.addActionListener(action -> forwardAction(markAsRead));


            markAsRead.addActionListener(action -> {
                try {
                    emailController.readEmail(email);
                    markAsRead.setBackground(Color.green);
                    revalidate();
                    repaint();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error occur.");
                }
            });

            buttonsPanel.add(replyButton);
            buttonsPanel.add(forwardButton);
            if (!email.getSender().getEmail().equals(UserController.getCurrentUser().getEmail())) {
                if (emailController.isRead(email)) {
                    markAsRead.setBackground(Color.green);
                } else {
                    markAsRead.setBackground(Color.red);
                }
                buttonsPanel.add(markAsRead);
            }

            bottomPanel.add(buttonsPanel, BorderLayout.EAST);
            return bottomPanel;
        }

        private void replyAction(JButton markAsRead) {
            try {
                new SendEmailDialog(null, email.getSender(), email);
                emailController.readEmail(email);
                markAsRead.setBackground(Color.green);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Replying didn't work.");
            }
        }

        private void forwardAction(JButton markAsRead) {
            String emailAddress = JOptionPane.showInputDialog(null, "Enter email: ");

            User receiver = null;
            try {
                receiver = userController.findUser(emailAddress);
                if (receiver == null) {
                    JOptionPane.showMessageDialog(null, "User not found.");
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error to find user. 404!");
                return;
            }
            try {
                emailController.forwardEmail(email.clone(), receiver);
                emailController.readEmail(email);
                markAsRead.setBackground(Color.green);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error to forward email for "
                        + receiver.getUserName() + ".");
            }
        }

        public Email getEmail() {
            return email;
        }
    }

    class SendEmailDialog extends JDialog {
        private final JTextField subjectField;
        private final JTextArea bodyArea;

        public SendEmailDialog(JFrame parent, User receiver, Email repliedEmail) {
            super(parent, "Send Email", true);
            setSize(400, 300);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            // Subject Panel
            JPanel subjectPanel = new JPanel(new BorderLayout());
            subjectPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
            subjectPanel.add(new JLabel("Subject:"), BorderLayout.NORTH);

            subjectField = new JTextField();
            subjectPanel.add(subjectField, BorderLayout.CENTER);

            // Body Panel
            JPanel bodyPanel = new JPanel(new BorderLayout());
            bodyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
            bodyPanel.add(new JLabel("Email Body:"), BorderLayout.NORTH);

            bodyArea = new JTextArea();
            bodyArea.setLineWrap(true);
            bodyArea.setWrapStyleWord(true);

            JScrollPane bodyScroll = new JScrollPane(bodyArea);
            bodyScroll.setPreferredSize(new Dimension(380, 150));
            bodyPanel.add(bodyScroll, BorderLayout.CENTER);

            // Send Button Panel
            JPanel buttonPanel = new JPanel();
            JButton sendButton = new JButton("Send");
            sendButton.addActionListener(action -> {
                String subject = subjectField.getText();
                String body = bodyArea.getText();
                try {
                    emailController.sendEmail(subject, body, UserController.getCurrentUser(), repliedEmail, null, receiver.getEmail());
                    JOptionPane.showMessageDialog(this, "Email sent!");
                    dispose();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            });

            buttonPanel.add(sendButton);

            // Add all panels
            add(subjectPanel, BorderLayout.NORTH);
            add(bodyPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            setVisible(true);
        }
    }
}