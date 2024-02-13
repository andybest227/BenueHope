import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Login extends JFrame {
    //colors

    private final String primary_color = "#043e45";
    private String mUsername;
    private String mPassword;
    private SwingWorker<Void, Void> worker;
    CustomDialog customDialog = new CustomDialog("Authenticating...", Color.ORANGE);

    //size
    int width = 800;
    int height = 600;

    private JButton close;
    private JButton minimize;
    private JButton login_btn;
    //Functions functions = new Functions();

    public Login() {
        // Set up frame properties
        setTitle("BENUE STATE SENIOR CITIZENS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        getContentPane().setBackground(Color.WHITE);

        centerFrameOnScreen();

        // Set image icon
        Image icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("icons/LOGO.png"))).getImage();
        setIconImage(icon);

        //Remove default header
        setUndecorated(true);

        // Disable maximize button
        setResizable(false);

        addComponents();
    }

    //Add components
    private void addComponents() {

        ImageResize functions = new ImageResize();
        //home panel layout
        int width_percentage = width/100;
        JPanel homePanel = new JPanel();
        homePanel.setSize(width, height);
        homePanel.setLayout(null);

        //Create background
        JLabel bg_label = new JLabel();
        bg_label.setBounds(0, 40, width, height);
        bg_label.setIcon(functions.ResizeImage(bg_label,"icons/ng_bg.jpg", null));

        //Corporations
        JLabel ncss = new JLabel();
        ncss.setBounds(10, 480, 70, 70);
        ncss.setIcon(functions.ResizeImage(ncss,"icons/bn_logo.jpg", null));

        JLabel bn = new JLabel();
        bn.setBounds(100, 480, 80, 70);
        bn.setIcon(functions.ResizeImage(bn,"icons/nscc_logo.png", null));

        //menu bar labels
        JLabel menu_icon = new JLabel();
        menu_icon.setBounds(5, 1, 45, 40);
        menu_icon.setIcon(functions.ResizeImage(menu_icon,"icons/coa.png", null));

        //menu tabs and control box panel
        JPanel control_box = new JPanel();
        control_box.setSize(width, 20);
        control_box.setBackground(Color.decode(primary_color));
        control_box.setBounds(0, 0, width, 40);
        control_box.setLayout(null);

        //Create the file Menu tab button
        JLabel file = new JLabel("File");
        file.setBounds(65, 10, 35, 25);
        file.setForeground(Color.yellow);
        file.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        file.setCursor(new Cursor(Cursor.HAND_CURSOR));


        //Create the Help Menu tab button
        JLabel help = new JLabel("Help");
        help.setBounds(105, 10, 35, 25);
        help.setForeground(Color.yellow);
        help.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        help.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //control box labels
        minimize = new JButton("-");
        minimize.setBackground(Color.decode("#043e45"));
        minimize.setForeground(Color.white);
        minimize.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        minimize.setBounds(width-100, 0, 50, 40);
        minimize.setBorder(null);
        minimize.setBorderPainted(false);
        minimize.setFocusPainted(false);

        close = new JButton("x");
        close.setBackground(Color.decode("#043e45"));
        close.setForeground(Color.white);
        close.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        close.setBounds(width-50, 0, 50, 40);
        close.setBorder(null);


        //title text
        JLabel titleText = new JLabel("BENUE SENIOR CITIZENS");
        titleText.setForeground(Color.white);
        titleText.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        titleText.setBounds(width_percentage*30, 0, 300, 40);
        titleText.setHorizontalAlignment(JLabel.CENTER);

        //Login Label text
        JLabel loginText = new JLabel("Authentication");
        loginText.setForeground(Color.white);
        loginText.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        loginText.setBounds(10, 5, 300, 40);
        //loginText.setHorizontalAlignment(JLabel.CENTER);


        //Header icon
        JLabel header_icon = new JLabel();
        header_icon.setBounds(345, 5, 40, 40);
        header_icon.setIcon(functions.ResizeImage(menu_icon,"icons/user.png", null));


        //Create login Panel header
        JPanel loginPanelHeader = new JPanel();
        loginPanelHeader.setSize(width, 400);
        String secondary_color = "#11856b";
        loginPanelHeader.setBackground(Color.decode(secondary_color));
        loginPanelHeader.setBounds(200, 100, 400, 50);
        loginPanelHeader.setLayout(null);
        loginPanelHeader.add(loginText);
        loginPanelHeader.add(header_icon);

        //Login Panel body
        //Create login Panel
        JPanel loginPanelBody = new JPanel();
        loginPanelBody.setSize(width, 400);
        loginPanelBody.setBackground(Color.decode(primary_color));
        loginPanelBody.setBounds(200, 150, 400, 350);
        loginPanelBody.setLayout(null);

        JLabel username_txt = new JLabel("Username");
        username_txt.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        username_txt.setForeground(Color.yellow);
        username_txt.setBounds(70, 40, 200, 35);

        JLabel password_txt = new JLabel("Password");
        password_txt.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
        password_txt.setForeground(Color.yellow);
        password_txt.setBounds(70, 140, 200, 35);

        //Create the login text filed
        JTextFieldPlaceholder username = new JTextFieldPlaceholder();
        username.setPlaceholder("Enter username");
        username.setBounds(70, 70, 250, 35);
        username.setBorder(BorderFactory.createCompoundBorder(username.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));
        username.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        username.setForeground(Color.BLACK);

        //password
        JPasswordFieldPlaceholder password = new JPasswordFieldPlaceholder();
        password.setPlaceholder("Enter password");
        password.setBounds(70, 170, 250, 35);
        password.setBorder(null);
        password.setBorder(BorderFactory.createCompoundBorder(username.getBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));
        password.setFont(new Font("Bahnschrift", Font.BOLD, 14));
        password.setForeground(Color.BLACK);

        //Login Button
        login_btn = new JButton("LOGIN");
        login_btn.setBackground(Color.decode(secondary_color));
        login_btn.setForeground(Color.white);
        login_btn.setBorder(null);
        login_btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        login_btn.setFont(new Font("Bahnschrift", Font.BOLD, 18));
        login_btn.setBounds(70, 250, 250,45);


        //Add username filed to login body
        loginPanelBody.add(username_txt);
        loginPanelBody.add(username);

        loginPanelBody.add(password_txt);
        loginPanelBody.add(password);

        loginPanelBody.add(login_btn);

        //Close Application
        //minimize button config
        minimize.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setState(JFrame.ICONIFIED);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                minimize.setBackground(Color.lightGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                minimize.setBackground(Color.decode(primary_color));
            }
        });
        close.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Frame frame = new JFrame("Exit");
                frame.setBackground(Color.decode("#042745"));
                if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Senior Citizens", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                close.setBackground(Color.red);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                close.setBackground(Color.red);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setBackground(Color.decode(primary_color));
            }
        });

        //add components to the header
        control_box.add(titleText);
        control_box.add(menu_icon);
        control_box.add(file);
        control_box.add(help);
        control_box.add(minimize);
        control_box.add(close);

        //Add components to the homePanel
        bg_label.add(loginPanelHeader);
        bg_label.add(loginPanelBody);
        bg_label.add(ncss);
        bg_label.add(bn);
        homePanel.add(control_box);
        homePanel.add(bg_label);
        add(homePanel);

        //Handle logging button click
        login_btn.addActionListener(e -> {
            mUsername = username.getText();
            mPassword = new String(password.getPassword());
            if (!mUsername.isEmpty() && !mPassword.isEmpty()){

                /*PERFORM ALL LOGGING AUTHENTICATIONS HERE*/
                //Check Internet connection
                if (InternetConnection.isInternetConnected()){
                    worker = new SwingWorker<>() {
                        @Override
                        protected Void doInBackground() {
                            loginUser();
                            return null;
                        }

                        @Override
                        protected void done() {
                            // Close the dialog box when the background task is completed
                            customDialog.dispose();
                            //JOptionPane.showMessageDialog(null, "Task completed!");
                        }
                    };
                    customDialog.setVisible(true);
                    login_btn.setEnabled(false);
                    worker.execute();
                }else{
                    JOptionPane.showMessageDialog(null, "Please review your internet connection", "Network Error", JOptionPane.WARNING_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Username or Password can not be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    //Center the frame
    private void centerFrameOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }

    private void loginUser(){
        // Perform your background task here
        String endpoint = "https://bsscc.ng/api/user-validation";
        Index index = new Index(mUsername);
        try {
            URL url = new URL(endpoint + "?email=" + mUsername + "&password=" + mPassword);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                //If logging is successful
                worker.cancel(true);
                customDialog.setVisible(false);
                login_btn.setEnabled(true);
                index.setVisible(true);
                dispose();
            } else {
                // Handle error response
                customDialog.setVisible(false);
                login_btn.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Login failed!", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error code: " + responseCode);
                worker.cancel(true);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //Handle login in the background
}
