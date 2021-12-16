package client.gui;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.event.LoginEvent;
import client.event.Subject;

/**
 * Panel containing components for a login operation.
 * Generates Login events.
 */
public class LoginPanel extends ContentPanel
{
    private static final String LOGO_PATH = "res/logo.png"; // path for logo image

    private JLabel logoLabel = new JLabel();              // game logo
    private JTextField loginNameField = new JTextField(); // login name field
    private JButton loginButton = new JButton();          // login button
    
    /**
     * Default constructor.
     * Initializes the components of the login panel and defines its layout.
     */
    public LoginPanel()
    {
        super("LoginPanel");
        registerSubject(Subject.Login); // generates login events
        
        initComponents();
        createLayout();
    }

    @Override
    public List<Subject> getEventsProduced()
    {
        return List.of(Subject.Login);
    }

    // Defines the layout for the login panel
    private void createLayout()
    {
        GroupLayout layoutManager = new GroupLayout(content);
        layoutManager.setAutoCreateContainerGaps(true);
        layoutManager.setAutoCreateGaps(true);
        
        layoutManager.setHorizontalGroup(layoutManager.createParallelGroup(Alignment.CENTER)
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(logoLabel)
            .addComponent(loginNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(loginButton)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        
        layoutManager.setVerticalGroup(layoutManager.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(logoLabel)
            .addComponent(loginNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(loginButton)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        
        layoutManager.linkSize(SwingConstants.VERTICAL, loginNameField, loginButton);
        content.setLayout(layoutManager);
    }

    // Initialize components of the login panel
    private void initComponents()
    {
        initLogoLabel();
        initLoginNameField();
        initLoginButton();
    }

    // Initializes the label containing the logo
    private void initLogoLabel()
    {
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        ImageIcon logo = new ImageIcon(LOGO_PATH);
        logoLabel.setIcon(logo);
    }

    // Initializes the login name field
    private void initLoginNameField()
    {
        loginNameField.setFont(DefaultFont.getPlain());
        loginNameField.setColumns(30);
        loginNameField.setHorizontalAlignment(JTextField.CENTER);
        loginNameField.addActionListener((event) -> sendLoginAttempt());
    }
    
    // Initializes the login name field
    private void initLoginButton()
    {
        loginButton.setFont(DefaultFont.getBold());
        loginButton.setText("Login");
        loginButton.addActionListener((event) -> sendLoginAttempt());
    }

    // Sends a login request to the game client
    private void sendLoginAttempt()
    {
        LoginEvent event = new LoginEvent();
        event.setName(loginNameField.getText());
        notify(event);
    }
}
