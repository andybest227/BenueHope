import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class BiometricsEnrollment extends JFrame{
    //colors
    String primary_color = "#043e45";
    String secondary_color = "#11856b";

    //Get screen size
    double full_width = Toolkit.getDefaultToolkit().getScreenSize().width;
    double full_height = Toolkit.getDefaultToolkit().getScreenSize().height;

    //size
    int width  = (int) full_width;
    int height = (int) full_height;
    private final CardLayout cardLayout = new CardLayout();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JPanel cards = new JPanel(cardLayout);
    private final JPanel biometricPane = new JPanel(new GridBagLayout());
    private ImagePanel m_imagePanel;
    private JLabel ninDataIndicator;
    private JLabel bioDataIndicator;
    private JLabel biometricDataIndicator;
    private JLabel photoDataIndicator;
    private JLabel summaryDataIndicator;
    private JLabel printDataIndicator;
    private JButton saveBiometrics;

    public BiometricsEnrollment(){
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 2, 2,2);
        // Set up frame properties
        setTitle("BENUE STATE SENIOR CITIZENS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        getContentPane().setBackground(Color.WHITE);

        // Set image icon
        Image icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("icons/coa.png"))).getImage();
        setIconImage(icon);

        //Remove default header
        //setUndecorated(true);

        //Add GUI components
        addComponents();
        //centerFrameOnScreen();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //Initiate components
    private void addComponents() {
        ImageResize functions = new ImageResize();
        //home panel layout
        JPanel homePanel = new JPanel();
        homePanel.setSize(width, height);
        homePanel.setLayout(null);

        //Create background
        JLabel bg_label = new JLabel();
        bg_label.setBounds(0, 40, width, height);
        bg_label.setIcon(functions.ResizeImage(bg_label,"icons/ng_bg.jpg", null));

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


        //title text
        JLabel titleText = new JLabel("CITIZEN'S DATA COLLECTION FORM ");
        titleText.setForeground(Color.white);
        titleText.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        titleText.setBounds(0, 0, width, 40);
        titleText.setHorizontalAlignment(JLabel.CENTER);

        //Corporations
        JLabel ncss = new JLabel();
        ncss.setBounds(10, height-180, 70, 70);
        ncss.setIcon(functions.ResizeImage(ncss,"icons/bn_logo.jpg", null));

        JLabel bn = new JLabel();
        bn.setBounds(100, height-180, 80, 70);
        bn.setIcon(functions.ResizeImage(bn,"icons/nscc_logo.png", null));

        //Side nav holder
        JLabel sideNavHolder = new JLabel();
        LineBorder lineBorder = new LineBorder(Color.decode(secondary_color), 2, true);
        sideNavHolder.setBorder(lineBorder);
        sideNavHolder.setBounds(25, 50, 250, height-400);

        JLabel navHeaderLabel = new JLabel("NAVIGATION");
        navHeaderLabel.setForeground(Color.white);
        navHeaderLabel.setBounds(20, 38, 100, 30);

        JPanel navHeaderPane = new JPanel();
        navHeaderPane.setBackground(Color.decode(secondary_color));
        navHeaderPane.setBounds(95, 38, 100, 30);
        navHeaderPane.add(navHeaderLabel);

        //create Content Pane
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setContentPane(contentPane);

        cards.setBounds(300, 50, width-350, height-250);
        cards.setBorder(lineBorder);

        /*END OF SIDE NAVIGATION HOLDER*/

        /*START OF BIOMETRICS PANE*/
        biometricPane.setBackground(Color.white);

        //label icon
        JLabel lbl_icon = new JLabel();
        lbl_icon.setSize(80, 80);
        lbl_icon.setIcon(functions.ResizeImage(lbl_icon,"icons/fingerprint.png", null));
        lbl_icon.setHorizontalAlignment(JLabel.CENTER);
        gbc.insets = new Insets(10,5,10,5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        biometricPane.add(lbl_icon, gbc);

        //label title
        JLabel labelTitle = new JLabel("BIOMETRIC CAPTURING");
        labelTitle.setSize(80, 80);
        labelTitle.setForeground(Color.decode(primary_color));
        labelTitle.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        gbc.insets = new Insets(2, 2, 0,2);
        gbc.gridy = 1;
        biometricPane.add(labelTitle, gbc);

        //ImagePane border
        JLabel imgPanelBorder = new JLabel();
        imgPanelBorder.setBorder(lineBorder);
        imgPanelBorder.setPreferredSize(new Dimension(110, 310));


        //Biometric image Pane
        m_imagePanel = new ImagePanel();
        m_imagePanel.setPreferredSize(new Dimension(100, 300));
        m_imagePanel.setBorder(lineBorder);
        m_imagePanel.setBackground(Color.lightGray);
        //m_imagePanel.showImage(evt.capture_result.image);
        //gbc.insets = new Insets(0, 200, 0,0);
        gbc.gridy = 2;
        gbc.ipadx = 100;
        gbc.ipady = 250;
        imgPanelBorder.add(m_imagePanel);
        biometricPane.add(imgPanelBorder, gbc);

        //capture output information
        //Place right thumb on the Reader
        JLabel captureMsg = new JLabel("Enrollment completed");
        captureMsg.setSize(80, 80);
        captureMsg.setForeground(Color.decode(secondary_color));
        captureMsg.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        captureMsg.setBorder(lineBorder);
        gbc.gridy = 3;
        gbc.ipady = 0;
        gbc.ipadx = 0;
        biometricPane.add(captureMsg, gbc);

        //Save Biometrics button
        saveBiometrics = new JButton("SAVE AND CONTINUE");
        saveBiometrics.setBackground(Color.decode(primary_color));
        saveBiometrics.setForeground(Color.white);
        saveBiometrics.setBorder(null);

        //configure save biometrics button
        saveBiometrics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (captureMsg.getText().equals("Enrollment completed")){
                    /*IMPLEMENT SAVE BIOMETRICS LOGIC HERE*/
                    JOptionPane.showMessageDialog(null, "Saved successfully", "Captured", JOptionPane.INFORMATION_MESSAGE);
                    //new Photograph("123456").setVisible(true);
                    dispose();
                }else {
                    JOptionPane.showMessageDialog(null, "Wait until Enrollment is completed", "Please wait", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.insets = new Insets(15, 0, 0,0);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.ipadx = 50;
        gbc.ipady = 20;
        biometricPane.add(saveBiometrics, gbc);
        cards.add(biometricPane);
        /*END OF BIOMETRICS PANE*/

        //Nav buttons indicators
        //nin indicator
        ninDataIndicator = createIndicatorLabel("NIN VERIFICATION");
        ninDataIndicator.setBounds(30, 50, 200, 30);

        //bio data indicator
        bioDataIndicator = createIndicatorLabel("BIO-DATA");
        bioDataIndicator.setBounds(30, 100, 200, 30);

        //biometrics indicator
        biometricDataIndicator = createIndicatorLabel("BIOMETRICS");
        biometricDataIndicator.setBounds(30, 150, 200, 30);

        //photograph indicator
        photoDataIndicator = createIndicatorLabel("PHOTOGRAPH");
        photoDataIndicator.setBounds(30, 200, 200, 30);

        //summary indicator
        summaryDataIndicator = createIndicatorLabel("SUMMARY");
        summaryDataIndicator.setBounds(30, 250, 200, 30);

        //print view indicator
        printDataIndicator = createIndicatorLabel("PRINT PREVIEW");
        printDataIndicator.setBounds(30, 300, 200, 30);

        sideNavHolder.add(ninDataIndicator);
        sideNavHolder.add(bioDataIndicator);
        sideNavHolder.add(biometricDataIndicator);
        sideNavHolder.add(photoDataIndicator);
        sideNavHolder.add(summaryDataIndicator);
        sideNavHolder.add(printDataIndicator);

        //Collect all indicator
        JLabel[] indicators = {ninDataIndicator, bioDataIndicator, biometricDataIndicator, photoDataIndicator, summaryDataIndicator, printDataIndicator};

        //Add corporation icon to bg label
        bg_label.add(navHeaderPane);
        bg_label.add(sideNavHolder);
        bg_label.add(cards);
        bg_label.add(ncss);
        bg_label.add(bn);

        //add components to the header
        control_box.add(titleText);
        control_box.add(menu_icon);
        control_box.add(file);
        control_box.add(help);

        //Add components to the homePanel
        homePanel.add(control_box);
        homePanel.add(bg_label);
        add(homePanel);
        activateIndicator(indicators[0]);
        activateIndicator(indicators[1]);
        activateIndicator(indicators[2]);
    }

    //Create Indicators button
    private JLabel createIndicatorLabel(String text) {
        JLabel indicatorLabel = new JLabel(text);
        //indicatorLabel.setOpaque(true);
        indicatorLabel.setBackground(Color.white);
        indicatorLabel.setBorder(new LineBorder(Color.gray, 2, true));
        indicatorLabel.setForeground(Color.gray);
        indicatorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        indicatorLabel.setHorizontalAlignment(JLabel.CENTER);
        return indicatorLabel;
    }

    //Activate indicator
    private void activateIndicator(JLabel indicator) {
        indicator.setOpaque(true);
        indicator.setBackground(Color.decode(primary_color));
        indicator.setBorder(new LineBorder(Color.decode(secondary_color), 2, true));
        indicator.setForeground(Color.white);
        indicator.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        indicator.setHorizontalAlignment(JLabel.CENTER);
    }
}
