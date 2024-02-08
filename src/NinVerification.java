import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Objects;

public class NinVerification extends JFrame {
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
    private final JPanel ninVerificationPane = new JPanel(new GridBagLayout());

    private final Border border = BorderFactory.createEmptyBorder(60, 60, 60, 60);
    private final JButton save_continue = new JButton("SAVE AND CONTINUE");
    private final JButton previous = new JButton("PREVIOUS");
    private final JButton verifyNIN = new JButton("VERIFY");
    private final JLabel ninIcon = new JLabel();
    private final String username;
    JDialog dialog = new JDialog();
    private String ninNumberValue;
    JLabel ninDataIndicator, bioDataIndicator, biometricDataIndicator, photoDataIndicator, summaryDataIndicator, printDataIndicator;

    public NinVerification(String mUsername){
        //get values from the constructor
        username = mUsername;
        // Set up frame properties
        setTitle("BENUE STATE SENIOR CITIZENS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        getContentPane().setBackground(Color.WHITE);

        // Set image icon
        Image icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("icons/LOGO.png"))).getImage();
        setIconImage(icon);

        //Remove default header
        //setUndecorated(true);

        //Add GUI components
        addComponents();
        //centerFrameOnScreen();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Disable maximize button
        //setResizable(false);
    }

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

        /*START OF NIN VERIFICATION PANEL*/
        ninVerificationPane.setBorder(border);
        ninVerificationPane.setBackground(Color.white);

        ninIcon.setSize(110, 100);
        ninIcon.setIcon(functions.ResizeImage(ninIcon,"icons/ninVef.png", null));
        ninIcon.setHorizontalAlignment(JLabel.CENTER);
        gbc.insets = new Insets(20,20,20,10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 50;
        gbc.ipady = 10;
        ninVerificationPane.add(ninIcon, gbc);

        JLabel ninHeaderLabel = new JLabel("NIN VERIFICATION");
        ninHeaderLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        ninHeaderLabel.setForeground(Color.decode(primary_color));
        ninHeaderLabel.setHorizontalAlignment(JLabel.CENTER);
        //ninHeaderLabel.setBounds(0,50, 200, 50);

        JTextFieldPlaceholder ninNumber = new JTextFieldPlaceholder();
        ninNumber.setPlaceholder("Enter NIN");
        ninNumber.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        ninNumber.setSize(300,50);
        ninNumber.setHorizontalAlignment(JTextFieldPlaceholder.CENTER);
        ninNumber.setBorder(lineBorder);
        ninNumber.setBorder(BorderFactory.createCompoundBorder(ninNumber.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.insets = new Insets(4, 2, 2,2);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.ipadx = 250;
        gbc.ipady = 20;
        ninVerificationPane.add(ninHeaderLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        verifyNIN.setBackground(Color.decode(primary_color));
        verifyNIN.setSize(300,50);
        verifyNIN.setBorder(null);
        verifyNIN.setForeground(Color.white);
        ninVerificationPane.add(ninNumber, gbc);

        gbc.insets = new Insets(10, 2, 2,2);
        gbc.gridy = 4;
        gbc.ipady = 20;
        gbc.ipadx = 220;
        ninVerificationPane.add(verifyNIN, gbc);
        cards.add("ninVerificationPane", ninVerificationPane);

        verifyNIN.addActionListener(e -> {
            ninNumberValue = ninNumber.getText();

            if(ninNumberValue != null && ninNumberValue.length() == 11){
                JOptionPane.showMessageDialog(null, "Verification Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                verifyNIN.setText("Loading...");
                verifyNIN.setEnabled(false);
                new Photograph(ninNumberValue, username).setVisible(true);
                dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Enter a valid NIN", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        /*END OF NIN VERIFICATION PANEL*/

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

        //Save and continue button
        save_continue.setBorder(null);
        save_continue.setBackground(Color.decode(primary_color));
        save_continue.setForeground(Color.white);
        save_continue.setBounds(width-210, height-170, 160, 40);

        //Previous button
        //Save and continue button
        previous.setBorder(null);
        previous.setBackground(Color.decode(primary_color));
        previous.setForeground(Color.white);
        previous.setBounds(width-380, height-170, 160, 40);


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

    //Show loading dialog
    private void loadingDialog(final Frame parent){
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setUndecorated(true);
        Icon icon = new ImageIcon(Objects.requireNonNull(parent.getClass().getResource("icons/loading_gear.gif")));
        JLabel label = new JLabel(icon);
        parent.setBackground(Color.white);
        label.setSize(20, 20);
        label.setBackground(Color.white);
        dialog.add(label);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
    }


}
