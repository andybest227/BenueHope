import com.opencsv.CSVWriter;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BioData extends JFrame {
    //colors
    String primary_color = "#043e45";
    String secondary_color = "#11856b";

    //Get screen size
    double full_width = Toolkit.getDefaultToolkit().getScreenSize().width;
    double full_height = Toolkit.getDefaultToolkit().getScreenSize().height;

    //size
    int width  = (int) full_width;
    int height = (int) full_height;

    private final Map<String, String[]> localGovernmentsMap, wardsMap;

    private JComboBox<String> title, stateComboBox, gender, mStatus, nokRelationship, religion,colorOfHair,tribalMark, localGovernmentComboBox, wardsComboBox, statusComboBox, disabilityComboBox, qualification, bankName;
    private JTextFieldPlaceholder firstName, middle_name, last_name, no_children, phone_number, email_address, hunchback,nonFirstName, nonOtherNames,nonPhone, profession, placeOfPensionTextField;
    private JDatePickerImpl datePicker;
    private JTextAreaPlaceholder contactAddress;
    private ButtonGroup buttonGroup;
    private JTextFieldPlaceholder accountNumber,accountName, bvn;
    private final String cPhotoPath, cBiometricPath, nin, mUsername;
    private final String FILE_PATH = "src/files/temporal_records.csv";


    private final CardLayout cardLayout = new CardLayout();
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JPanel cards = new JPanel(cardLayout);
    private final JPanel bio_dataPane = new JPanel(new GridBagLayout());
    private final Border border = BorderFactory.createEmptyBorder(60, 60, 60, 60);
    private final JButton submit_btn = new JButton("SUBMIT");
    JLabel ninDataIndicator, bioDataIndicator, biometricDataIndicator, photoDataIndicator, summaryDataIndicator, printDataIndicator;


    public BioData(String ninNumberValue, String photoPath, String biometricPath, String username){

        //get values passed in the constructor
        nin = ninNumberValue;
        cPhotoPath = photoPath;
        cBiometricPath = biometricPath;
        mUsername = username;

        //CREATE COMPONENTS
        localGovernmentsMap = createLocalGovernmentsMap();
        wardsMap = createWardsMap();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 2, 2,2);
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


        /*START OF BIO DATA FORM*/
        bio_dataPane.setBorder(border);
        bio_dataPane.setBackground(Color.white);

        JLabel personalInfo = new JLabel("PERSONAL INFORMATION");
        personalInfo.setSize(80, 80);
        personalInfo.setForeground(Color.decode(primary_color));
        personalInfo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        gbc.insets = new Insets(2, 2, 2,2);
        gbc.gridy = 0;
        gbc.gridx = 0;
        bio_dataPane.add(personalInfo, gbc);

        //title
        title = new JComboBox<>(new String[]{"-select title-","MR", "MRS", "MASTER", "MISS", "MS"});
        title.setBackground(Color.white);
        title.setSize(300, 50);
        title.setBorder(lineBorder);
        gbc.gridy = 1;
        gbc.gridx = 0;
        bio_dataPane.add(title, gbc);

        //Date Picker
        JLabel dob = new JLabel("Date of Birth");
        dob.setSize(80, 80);
        dob.setForeground(Color.decode(primary_color));
        dob.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        dob.setHorizontalAlignment(JLabel.CENTER);
        //gbc.insets = new Insets(2, 2, 2,2);
        gbc.gridy = 2;
        gbc.gridx = 3;
        bio_dataPane.add(dob, gbc);


        firstName = new JTextFieldPlaceholder();
        firstName.setPlaceholder("FIRST NAME");
        firstName.setBorder(lineBorder);
        firstName.setSize(300,50);
        firstName.setBorder(lineBorder);
        firstName.setBorder(BorderFactory.createCompoundBorder(firstName.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.insets = new Insets(4, 2, 2,2);
        gbc.gridx = 0;
        gbc.ipadx = 200;
        gbc.ipady = 5;
        bio_dataPane.add(firstName, gbc);

        middle_name = new JTextFieldPlaceholder();
        middle_name.setPlaceholder("MIDDLE NAME");
        middle_name.setBorder(lineBorder);
        middle_name.setSize(300,50);
        middle_name.setBorder(lineBorder);
        middle_name.setBorder(BorderFactory.createCompoundBorder(middle_name.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 1;
        bio_dataPane.add(middle_name, gbc);

        last_name = new JTextFieldPlaceholder();
        last_name.setPlaceholder("LAST NAME");
        last_name.setBorder(lineBorder);
        last_name.setSize(300,50);
        last_name.setBorder(lineBorder);
        last_name.setBorder(BorderFactory.createCompoundBorder(last_name.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 2;
        bio_dataPane.add(last_name, gbc);

        //Date of Birth
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel);
        datePicker.setSize(30, 30);
        datePicker.setBorder(lineBorder);
        datePicker.setBackground(Color.white);
        gbc.gridy = 3;
        gbc.gridx = 3;
        bio_dataPane.add(datePicker, gbc);

        //Forth Row
        //gender
        String[] gender_lbl = {"-select gender-", "MALE", "FEMALE"};
        gender = new JComboBox<>(gender_lbl);
        gender.setBorder(lineBorder);
        gender.setBackground(Color.white);
        gender.setSize(100, 50);
        gbc.insets = new Insets(4, 2, 2,2);
        gbc.gridx = 0;
        gbc.ipadx = 20;
        gbc.ipady = 5;
        bio_dataPane.add(gender, gbc);

        //Marital status
        String[] marital_lbl = {"-marital status-", "SINGLE", "MARRIED", "WIDOW", "WIDOWER", "SEPARATED"};
        mStatus = new JComboBox<>(marital_lbl);
        mStatus.setBorder(lineBorder);
        mStatus.setBackground(Color.white);
        mStatus.setSize(100, 50);
        gbc.gridx = 1;
        bio_dataPane.add(mStatus, gbc);

        //Religion
        String[] religion_lbl = {"-religion-", "CHRISTIANITY", "ISLAM", "TRADITIONAL", "OTHERS"};
        religion = new JComboBox<>(religion_lbl);
        religion.setBorder(lineBorder);
        religion.setBackground(Color.white);
        religion.setSize(100, 50);
        gbc.gridx = 2;
        bio_dataPane.add(religion, gbc);

        //Fifth Row
        //Number of Children
        no_children = new JTextFieldPlaceholder();
        no_children.setPlaceholder("NUMBER OF CHILDREN");
        no_children.setBorder(lineBorder);
        no_children.setSize(300,50);
        no_children.setBorder(lineBorder);
        no_children.setBorder(BorderFactory.createCompoundBorder(no_children.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridy = 4;
        gbc.gridx = 0;
        bio_dataPane.add(no_children, gbc);

        //color of hair
        colorOfHair = new JComboBox<>(new String[]{"-color of hair-", "BLACK", "BROWN", "GRAY", "WHITE"});
        colorOfHair.setBorder(lineBorder);
        colorOfHair.setSize(300,50);
        colorOfHair.setBorder(lineBorder);
        gbc.gridx = 1;
        bio_dataPane.add(colorOfHair, gbc);

        //Hunchback
        hunchback = new JTextFieldPlaceholder();
        hunchback.setPlaceholder("HUNCHBACK");
        hunchback.setBorder(lineBorder);
        hunchback.setSize(300,50);
        hunchback.setBorder(lineBorder);
        hunchback.setBorder(BorderFactory.createCompoundBorder(hunchback.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 2;
        bio_dataPane.add(hunchback, gbc);

        //Tribal mark
        //color of hair
        tribalMark = new JComboBox<>(new String[]{"-tribal mark?-", "NO", "YES"});
        tribalMark.setBorder(lineBorder);
        tribalMark.setSize(300,50);
        tribalMark.setBorder(lineBorder);
        gbc.gridx = 3;
        bio_dataPane.add(tribalMark, gbc);


        //Next of kin Information
        JLabel nokInfo = new JLabel("NEXT OF KIN INFORMATION");
        nokInfo.setSize(80, 80);
        nokInfo.setForeground(Color.decode(primary_color));
        nokInfo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        gbc.insets = new Insets(2, 2, 2,2);
        gbc.gridy = 6;
        gbc.gridx = 0;
        bio_dataPane.add(nokInfo, gbc);


        //NOK First name
        nonFirstName = new JTextFieldPlaceholder();
        nonFirstName.setPlaceholder("NOK FIRST NAME");
        nonFirstName.setBorder(lineBorder);
        nonFirstName.setSize(300,50);
        nonFirstName.setBorder(lineBorder);
        nonFirstName.setBorder(BorderFactory.createCompoundBorder(nonFirstName.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridy = 7;
        gbc.gridx = 0;
        bio_dataPane.add(nonFirstName, gbc);

        //NOK other names
        nonOtherNames = new JTextFieldPlaceholder();
        nonOtherNames.setPlaceholder("NOK OTHER NAMES");
        nonOtherNames.setBorder(lineBorder);
        nonOtherNames.setSize(300,50);
        nonOtherNames.setBorder(lineBorder);
        nonOtherNames.setBorder(BorderFactory.createCompoundBorder(nonOtherNames.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 1;
        bio_dataPane.add(nonOtherNames, gbc);

        //NOK phone
        nonPhone = new JTextFieldPlaceholder();
        nonPhone.setPlaceholder("NOK PHONE");
        nonPhone.setBorder(lineBorder);
        nonPhone.setSize(300,50);
        nonPhone.setBorder(lineBorder);
        nonPhone.setBorder(BorderFactory.createCompoundBorder(nonPhone.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 2;
        bio_dataPane.add(nonPhone, gbc);

        //NOK Address
        nokRelationship = new JComboBox<>(new String[]{"-relationship with NOK-","SON", "DAUGHTER", "BROTHER", "SISTER", "FRIEND", "FATHER", "MOTHER", "GRAND SON", "GRAND DAUGHTER"});
        nokRelationship.setSize(300,50);
        nokRelationship.setBorder(lineBorder);
        gbc.gridx = 3;
        bio_dataPane.add(nokRelationship, gbc);

        //Sixth Row
        JLabel contact_Info = new JLabel("CONTACT INFORMATION");
        contact_Info.setSize(80, 80);
        contact_Info.setForeground(Color.decode(primary_color));
        contact_Info.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        gbc.insets = new Insets(5, 2, 2,2);
        gbc.gridy = 8;
        gbc.gridx = 0;
        bio_dataPane.add(contact_Info, gbc);

        //Phone number
        phone_number = new JTextFieldPlaceholder();
        phone_number.setPlaceholder("PHONE NUMBER");
        phone_number.setBorder(lineBorder);
        phone_number.setSize(300,50);
        phone_number.setBorder(lineBorder);
        phone_number.setBorder(BorderFactory.createCompoundBorder(phone_number.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridy = 9;
        gbc.gridx = 0;
        bio_dataPane.add(phone_number, gbc);

        //email address
        email_address = new JTextFieldPlaceholder();
        email_address.setPlaceholder("EMAIL ADDRESS");
        email_address.setBorder(lineBorder);
        email_address.setSize(300,50);
        email_address.setBorder(lineBorder);
        email_address.setBorder(BorderFactory.createCompoundBorder(phone_number.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 1;
        bio_dataPane.add(email_address, gbc);

        //State of origin
        stateComboBox = new JComboBox<>(new String[]{"-state of origin-","Abia",
                "Adamawa",
                "Akwa Ibom",
                "Anambra",
                "Bauchi",
                "Bayelsa",
                "Benue",
                "Borno",
                "Cross River",
                "Delta",
                "Ebonyi",
                "Edo",
                "Ekiti",
                "Enugu",
                "FCT - Abuja",
                "Gombe",
                "Imo",
                "Jigawa",
                "Kaduna",
                "Kano",
                "Katsina",
                "Kebbi",
                "Kogi",
                "Kwara",
                "Lagos",
                "Nasarawa",
                "Niger",
                "Ogun",
                "Ondo",
                "Osun",
                "Oyo",
                "Plateau",
                "Rivers",
                "Sokoto",
                "Taraba",
                "Yobe",
                "Zamfara"});
        stateComboBox.addItemListener(new StateComboBoxListener());

        localGovernmentComboBox = new JComboBox<>();
        updateLocalGovernmentComboBox("-state of origin-");

        wardsComboBox = new JComboBox<>();
        stateComboBox.setBorder(lineBorder);
        stateComboBox.setBackground(Color.white);
        stateComboBox.setSize(100, 50);
        gbc.gridx = 2;
        bio_dataPane.add(stateComboBox, gbc);

        updateWardsComboBox("-local government-");

        //Local government
        localGovernmentComboBox.addItemListener(new LgaComboBoxListener());
        //updateWardComboBox("Ukum");
        localGovernmentComboBox.setBorder(lineBorder);
        localGovernmentComboBox.setBackground(Color.white);
        localGovernmentComboBox.setSize(100, 50);
        gbc.gridx = 3;
        bio_dataPane.add(localGovernmentComboBox, gbc);

        //Council ward in Benue state
        wardsComboBox.setBorder(lineBorder);
        wardsComboBox.setBackground(Color.white);
        wardsComboBox.setSize(100, 50);
        gbc.gridy = 10;
        gbc.gridx = 0;
        bio_dataPane.add(wardsComboBox, gbc);

        //Contact address
        contactAddress = new JTextAreaPlaceholder();
        contactAddress.setPlaceholder("HOME ADDRESS");
        contactAddress.setBorder(lineBorder);
        contactAddress.setSize(300,50);
        contactAddress.setBorder(lineBorder);
        contactAddress.setBorder(BorderFactory.createCompoundBorder(contactAddress.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 1;
        bio_dataPane.add(contactAddress, gbc);

        //Disability
        String[] disabilityStatusOptions = {"-disability status-", "None", "Disabled"};
        String[] disabilities = {"None","Albinism","Blind", "Dumb", "Paralyzed", "Physical Disability", "Mobility Impairment", "Others"};

        statusComboBox = new JComboBox<>(disabilityStatusOptions);
        statusComboBox.setBorder(lineBorder);
        statusComboBox.setSize(300, 50);
        statusComboBox.setBackground(Color.white);
        gbc.gridx = 2;
        bio_dataPane.add(statusComboBox, gbc);

        disabilityComboBox = new JComboBox<>(disabilities);
        disabilityComboBox.setBorder(lineBorder);
        disabilityComboBox.setSize(300, 50);
        disabilityComboBox.setBackground(Color.white);
        // Initially hide the disabilityComboBox
        disabilityComboBox.setVisible(false);
        gbc.gridx = 3;
        bio_dataPane.add(disabilityComboBox, gbc);

        //Toggle disabilities
        statusComboBox.addActionListener(e -> {
            // Check the selected disability status
            String selectedStatus = (String) statusComboBox.getSelectedItem();

            // Toggle the visibility of disabilityComboBox based on the selected status
            disabilityComboBox.setVisible("Disabled".equals(selectedStatus));
        });


        //Seventh Row
        JLabel prof_qua = new JLabel("PROFESSION/QUALIFICATION");
        prof_qua.setSize(80, 80);
        prof_qua.setForeground(Color.decode(primary_color));
        prof_qua.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        gbc.insets = new Insets(5, 2, 0,2);
        gbc.gridy = 11;
        gbc.gridx = 0;
        bio_dataPane.add(prof_qua, gbc);
        // Question Label
        JLabel questionLabel = new JLabel("Are you a pensioner?");
        questionLabel.setSize(80, 80);
        questionLabel.setForeground(Color.decode(primary_color));
        questionLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 11));
        questionLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 2;
        bio_dataPane.add(questionLabel, gbc);

        //Care Giver Address
        profession = new JTextFieldPlaceholder();
        profession.setPlaceholder("PROFESSION/HANDWORK");
        profession.setBorder(lineBorder);
        profession.setSize(300,50);
        profession.setBorder(lineBorder);
        profession.setBorder(BorderFactory.createCompoundBorder(profession.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 0;
        gbc.gridy = 12;
        bio_dataPane.add(profession, gbc);


        //Qualification
        String[] qualification_lbl = {"-qualification-", "NONE", "FSLC", "SSCE", "ND", "NCE", "HND", "B.Sc", "M.Sc", "Ph.D", "Others"};
        qualification = new JComboBox<>(qualification_lbl);
        qualification.setBorder(lineBorder);
        qualification.setBackground(Color.white);
        qualification.setSize(100, 50);
        gbc.gridx = 1;
        bio_dataPane.add(qualification, gbc);

        //Pension status
        // Radio Buttons
        JRadioButton yesRadioButton = new JRadioButton("Yes");
        JRadioButton noRadioButton = new JRadioButton("No");
        yesRadioButton.setActionCommand("YES");
        noRadioButton.setActionCommand("NO");

        yesRadioButton.setBackground(Color.white);
        noRadioButton.setBackground(Color.white);
        // Button Group to ensure only one option is selected at a time
        buttonGroup = new ButtonGroup();
        buttonGroup.add(yesRadioButton);
        buttonGroup.add(noRadioButton);

        JPanel radioPanel = new JPanel();
        radioPanel.setBackground(Color.white);
        radioPanel.add(yesRadioButton);
        radioPanel.add(noRadioButton);
        gbc.gridx = 2;
        bio_dataPane.add(radioPanel, gbc);

        // TextField for Place of Pension
        placeOfPensionTextField = new JTextFieldPlaceholder();
        placeOfPensionTextField.setEnabled(false); // Initially disabled
        placeOfPensionTextField.setPlaceholder("PLACE OF PENSION");
        placeOfPensionTextField.setSize(300,50);
        placeOfPensionTextField.setBorder(lineBorder);
        placeOfPensionTextField.setBorder(BorderFactory.createCompoundBorder(placeOfPensionTextField.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 3;
        bio_dataPane.add(placeOfPensionTextField, gbc);

        // ActionListener to enable/disable the TextField based on radio button selection
        ActionListener radioButtonListener = e -> placeOfPensionTextField.setEnabled(yesRadioButton.isSelected());

        yesRadioButton.addActionListener(radioButtonListener);
        noRadioButton.addActionListener(radioButtonListener);

        //Eight Row
        JLabel bankLabel = new JLabel("BANK DETAILS");
        bankLabel.setSize(80, 80);
        bankLabel.setForeground(Color.decode(primary_color));
        bankLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        gbc.insets = new Insets(5, 2, 0,2);
        gbc.gridy = 13;
        gbc.gridx = 0;
        bio_dataPane.add(bankLabel, gbc);

        //Array of banks in Nigeria
        String[] banks = {
                "-bank name-",
                "Access Bank Plc",
                "Fidelity Bank Plc",
                "First City Monument Bank Plc",
                "First Bank Plc",
                "Guaranty Trust Bank Plc",
                "Union Bank of Nigeria Plc",
                "United Bank for Africa Plc",
                "Zenith Bank Plc",
                "Citibank Ng. Limited",
                "Ecobank Nigeria Plc",
                "Heritage Banking",
                "Keystone Bank Limited",
                "Polaris Bank Limited",
                "Stanbic IBTC Bank Plc",
                "Standard Chartered",
                "Sterling Bank Plc",
                "Titan Trust Bank Limited",
                "Unity Bank Plc",
                "Wema Bank Plc",
                "Globus Bank Limited",
                "SunTrust Bank Ng. Limited",
                "Providus Bank Limited",
                "Jaiz Bank Plc",
                "Taj Bank Limited",
                "Coronation Merchant Bank",
                "FBNQuest Merchant Bank",
                "FSDH Merchant Bank",
                "Rand Merchant Bank",
                "Nova Merchant Bank"
        };
        // Sort the array
        Arrays.sort(banks);

        bankName = new JComboBox<>(banks);
        bankName.setBorder(lineBorder);
        bankName.setBackground(Color.white);
        bankName.setSize(50, 50);
        gbc.gridx = 0;
        gbc.gridy = 14;
        bio_dataPane.add(bankName, gbc);

        // TextField for ACCOUNT NUMBER
        accountNumber = new JTextFieldPlaceholder();
        accountNumber.setPlaceholder("ACCOUNT NUMBER");
        accountNumber.setSize(300,50);
        accountNumber.setBorder(lineBorder);
        accountNumber.setBorder(BorderFactory.createCompoundBorder(accountNumber.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 1;
        bio_dataPane.add(accountNumber, gbc);

        // TextField for account name
        accountName = new JTextFieldPlaceholder();
        accountName.setPlaceholder("ACCOUNT NAME");
        accountName.setSize(300,50);
        accountName.setBorder(lineBorder);
        accountName.setBorder(BorderFactory.createCompoundBorder(accountName.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 2;
        bio_dataPane.add(accountName, gbc);


        //TextFiled for BVN
        // TextField for ACCOUNT NUMBER
        bvn = new JTextFieldPlaceholder();
        bvn.setPlaceholder("ENTER BVN");
        bvn.setSize(300,50);
        bvn.setBorder(lineBorder);
        bvn.setBorder(BorderFactory.createCompoundBorder(bvn.getBorder(), BorderFactory.createEmptyBorder(3,3,3,3)));
        gbc.gridx = 3;
        bio_dataPane.add(bvn, gbc);

        cards.add(bio_dataPane, "Personal Information");
        /*END OF BIO DATA FORM*/


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
        submit_btn.setBorder(null);
        submit_btn.setBackground(Color.decode(primary_color));
        submit_btn.setForeground(Color.white);
        submit_btn.setBounds(width-210, height-170, 160, 40);


        //Add listener to save and continue button
        submit_btn.addActionListener(e -> {
            if (writeRecord(getDetails())){
                JOptionPane.showMessageDialog(null, "Submitted Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new Index(mUsername).setVisible(true);
                dispose();
            }
        });

        //Add corporation icon to bg label
        bg_label.add(navHeaderPane);
        bg_label.add(sideNavHolder);
        bg_label.add(cards);
        bg_label.add(submit_btn);
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
    }

    private void updateLocalGovernmentComboBox(String selectedState) {
        String[] localGovernments = localGovernmentsMap.getOrDefault(selectedState, new String[0]);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(localGovernments);
        localGovernmentComboBox.setModel(model);
    }

    private void updateWardsComboBox(String selectedLocalGovernment) {
        String[] wards = wardsMap.getOrDefault(selectedLocalGovernment, new String[0]);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(wards);
        wardsComboBox.setModel(model);
    }

    private Map<String, String[]> createWardsMap(){
        Map<String, String[]> map = new HashMap<>();

        map.put("-local government-", new String[]{
                "-council ward-"
        });

        map.put("Ado", new String[]{
                "Akoge/Ogbilolo",
                "Apa",
                "Ekile",
                "Igumale I",
                "Igumale II",
                "Ijigban",
                "Ogege",
                "Royongo",
                "Ukwonyo",
                "Ulayi"
        });
        map.put("Agatu", new String[]{
                "Egba",
                "Enungba",
                "Obagaji",
                "Odugbeho",
                "Ogbaulu",
                "Ogwule Ogbaulu",
                "Ogwule-Kaduna",
                "Okokolo",
                "Oshigbudu",
                "Usha"
        });
        map.put("Apa", new String[]{
                "Akpete/Ojantelle",
                "Auke",
                "Edikwu I",
                "Edikwu II",
                "Igah-Okpaya",
                "Igoro",
                "Ikobi",
                "Oba",
                "Ofoke",
                "Oiji",
                "Ugbokpo",
        });

        map.put("Buruku", new String[]{
                "Binev",
                "Etulo",
                "Mbaade",
                "Mbaakura",
                "Mbaapen",
                "Mbaatirkyaa",
                "Mbaazagee",
                "Mbaikyongo/Nyifon",
                "Mbaityough",
                "Mbakyaan",
                "Mbaya",
                "Mbayaka",
                "Shorov"
        });
        map.put("Gboko", new String[] {
                "Gboko/Central Market",
                "Gboko East",
                "Gboko North West",
                "Gboko South",
                "Igyorov",
                "Mbaa Varakaa",
                "Mbaanku",
                "Mbadam",
                "Mbadim",
                "Mbakper",
                "Mbakwen",
                "Mbatan",
                "Mbatser",
                "Mbatyu",
                "Ukpekpe",
                "Yandev North",
                "Yandev South",
        });
        map.put("Guma", new String[] {
                "Abinsi",
                "Kaambe",
                "Mbabai",
                "Mbadwem",
                "Mbawa",
                "Mbayer/Yandev",
                "Nyiev",
                "Nzorov",
                "Saghev",
                "Uvir"
        });
        map.put("Gwer East", new String[] {
                "Akpach’ayi",
                "Aliade Town",
                "Gbemacha",
                "Ikyogbajir",
                "Ikyonov",
                "Mbabur",
                "Mbaiase",
                "Mbaikyaan",
                "Mbaikyu",
                "Mbalom",
                "Mbasombo",
                "Mbayom",
                "Shough",
                "Ugee"
        });
        map.put("Gwer West", new String[] {
                "Avihijime",
                "Gaambe – Ushin",
                "Gbaange/Tongov",
                "Ikyaghev",
                "Isambe/Mbasev",
                "Ityoughatee/Injaha",
                "Mbabuande",
                "Mbachohon",
                "Mbanyamshi",
                "Mbapa",
                "Merkyen",
                "Sagher/Ukusu",
                "Sengev",
                "Sengev/Yengev",
                "Tijime"
        });

        map.put("Katsina-Ala", new String[] {
                "Ikurav Tiev I",
                "Ikurav Tiev II",
                "Iwar(Tongov I)",
                "Katsina-Ala Town",
                "Mbacher",
                "Mbajir",
                "Mbatula/Mberev",
                "Mbayongo",
                "Michihe",
                "Tiir(Tongov II)",
                "Utange",
                "Yooyo"
        });
        map.put("Konshisha", new String[]{
                "Ikyurav/Mbatwer",
                "Mbagusa/Mbatser",
                "Mbaikyase",
                "Mbaiwarnyam",
                "Mbake",
                "Mbanor",
                "Mbatsen",
                "Mbavoa",
                "Mbawar",
                "Mbayegh/Mbaikyer",
                "Tse-Agberagba"
        });



        map.put("Kwande", new String[]{
                "Adikpo Metropolis",
                "Kumakwagh",
                "Liev I",
                "Liev II",
                "Mbadura",
                "Mbagba/Mbaikyan",
                "Mbaikyor",
                "Mbaketsa",
                "Mbayoo",
                "Menev",
                "Moon",
                "Tondov I",
                "Tondov II",
                "Usar",
                "Yaav"
        });
        map.put("Logo", new String[]{
                "Mbadyul",
                "Mbagber",
                "Mbater",
                "Mbavuur",
                "Mbayam",
                "Nenzev",
                "Tombo",
                "Turan",
                "Ukembergya/Tswarev",
                "Yonov"
        });
        map.put("Makurdi", new String[]{
                "Agan",
                "Ankpa/Wadata",
                "Bar",
                "Central/South Mission",
                "Clerks/Market",
                "Fildi",
                "Mbalagh",
                "Modern Market",
                "North Bank I",
                "North Bank II",
                "Wailomayo"
        });
        map.put("Obi", new String[]{
                "Adiko",
                "Adum West",
                "Ikwokwu",
                "Irabi",
                "Itogo",
                "Obarike",
                "Obeko",
                "Odiapa",
                "Ogore",
                "Okwutungbe",
                "Orihi"
        });
        map.put("Ogbadibo", new String[]{
                "Ai-Oodo I",
                "Ai-Oodo II",
                "Ai-Oono I",
                "Ai-Oono II",
                "Ai-Oono III",
                "Ehaje I",
                "Ehaje II",
                "Itabono I",
                "Itabono II",
                "Olachagbaha",
                "Orokam I",
                "Orokam II",
                "Orokam III"
        });
        map.put("Ohimini", new String[]{
                "Agadagba",
                "Awume Ehaje",
                "Awume Icho",
                "Ehatokpe",
                "Idekpa",
                "Ochobo",
                "Oglewu Ehaje",
                "Oglewu Icho",
                "Onyagede Icho (Ogoli)",
                "Onyagede-Ehaje (Alle)",
        });
        map.put("Oju", new String[]{
                "Adokpa",
                "Ainu",
                "Ibilla",
                "Idelle",
                "Iyeche",
                "Oboru/Oye",
                "Oju",
                "Okpokpo",
                "Okwudu",
                "Owo",
                "Ukpa/Ainu Ette",
        });
        map.put("Okpokwu", new String[]{
                "Amejo",
                "Eke",
                "Ichama II",
                "Ojigo",
                "Ojoga",
                "Okonobo",
                "Okpaile/Ingle",
                "Okpoga Central",
                "Okpoga North",
                "Okpoga South",
                "Okpoga West",
                "Ugbokolo"
        });

        map.put("Otukpo", new String[]{
                "Adoka-Haje",
                "Adoka-Icho",
                "Allan",
                "Entekpa",
                "Ewulo",
                "Okete",
                "Otobi",
                "Otukpo Town Central",
                "Otukpo Town East",
                "Otukpo Town West",
                "Ugboju-Ehaje",
                "Ugboju-Icho",
                "Ugboju-Otahe"
        });
        map.put("Tarka", new String[]{
                "Mbaajir Akaa",
                "Mbaayo",
                "Mbachaver Ikyondo",
                "Mbaigba",
                "Mbaikyaa",
                "Mbaikyo/Mbayia",
                "Mbakwakem",
                "Mbanyagber",
                "Shitile",
                "Tongov"
        });

        map.put("Ukum", new String[]{
                "Aterayange",
                "Azendeshi",
                "Borikyo",
                "Ityuluv",
                "Kendev",
                "Kundav",
                "Lumbuv",
                "Mbatian",
                "Mbayenge",
                "Mbazun",
                "Tsaav",
                "Ugbaam",
                "Uyam"
        });
        map.put("Ushongo", new String[]{
                "Atikyese",
                "Ikyov",
                "Lessel",
                "Mbaaka",
                "Mbaanyam",
                "Mbagba",
                "Mbagwaza",
                "Mbagwe",
                "Mbakuha",
                "Mbayegh",
                "Utange"
        });

        map.put("Vandeikya", new String[]{
                "Mbadede",
                "Mbagbam",
                "Mbagbera",
                "Mbajor",
                "Mbakaange",
                "Mbakyaha",
                "Mbanyumangbagh",
                "Mbatyough",
                "Mbayongo",
                "Ningev",
                "Tsambe",
                "Vandeikya Township"
        });
        return map;
    }

    private Map<String, String[]> createLocalGovernmentsMap() {
        Map<String, String[]> map = new HashMap<>();
        map.put("-state of origin-", new String[]{"-local government-"});
        map.put("Abia", new String[]{"Aba North",
                "Aba South",
                "Arochukwu",
                "Bende",
                "Ikwuano",
                "Isiala-Ngwa North",
                "Isiala-Ngwa South",
                "Isuikwato",
                "Obi Nwa",
                "Ohafia",
                "Osisioma",
                "Ngwa",
                "Ugwunagbo",
                "Ukwa East",
                "Ukwa West",
                "Umuahia North",
                "Umuahia South",
                "Umu-Neochi"});
        map.put("Adamawa", new String[]{"Demsa",
                "Fufore",
                "Ganaye",
                "Gireri",
                "Gombi",
                "Guyuk",
                "Hong",
                "Jada",
                "Lamurde",
                "Madagali",
                "Maiha",
                "Mayo-Belwa",
                "Michika",
                "Mubi North",
                "Mubi South",
                "Numan",
                "Shelleng",
                "Song",
                "Toungo",
                "Yola North",
                "Yola South"});
        map.put("Anambra", new String[]{"Aguata",
                "Anambra East",
                "Anambra West",
                "Anaocha",
                "Awka North",
                "Awka South",
                "Ayamelum",
                "Dunukofia",
                "Ekwusigo",
                "Idemili North",
                "Idemili south",
                "Ihiala",
                "Njikoka",
                "Nnewi North",
                "Nnewi South",
                "Ogbaru",
                "Onitsha North",
                "Onitsha South",
                "Orumba North",
                "Orumba South",
                "Oyi"});
        map.put("Akwa Ibom", new String[]{"Abak",
                "Eastern Obolo",
                "Eket",
                "Esit Eket",
                "Essien Udim",
                "Etim Ekpo",
                "Etinan",
                "Ibeno",
                "Ibesikpo Asutan",
                "Ibiono Ibom",
                "Ika",
                "Ikono",
                "Ikot Abasi",
                "Ikot Ekpene",
                "Ini",
                "Itu",
                "Mbo",
                "Mkpat Enin",
                "Nsit Atai",
                "Nsit Ibom",
                "Nsit Ubium",
                "Obot Akara",
                "Okobo",
                "Onna",
                "Oron",
                "Oruk Anam",
                "Udung Uko",
                "Ukanafun",
                "Uruan",
                "Urue-Offong/Oruko ",
                "Uyo"});
        map.put("Bauchi", new String[]{"Alkaleri",
                "Bauchi",
                "Bogoro",
                "Damban",
                "Darazo",
                "Dass",
                "Ganjuwa",
                "Giade",
                "Itas/Gadau",
                "Jama'are",
                "Katagum",
                "Kirfi",
                "Misau",
                "Ningi",
                "Shira",
                "Tafawa-Balewa",
                "Toro",
                "Warji",
                "Zaki"});
        map.put("Bayelsa", new String[]{"Brass",
                "Ekeremor",
                "Kolokuma/Opokuma",
                "Nembe",
                "Ogbia",
                "Sagbama",
                "Southern Jaw",
                "Yenegoa"});
        map.put("Benue", new String[]{
                "Ado",
                "Agatu",
                "Apa",
                "Buruku",
                "Gboko",
                "Guma",
                "Gwer East",
                "Gwer West",
                "Katsina-Ala",
                "Konshisha",
                "Kwande",
                "Logo",
                "Makurdi",
                "Obi",
                "Ogbadibo",
                "Oju",
                "Okpokwu",
                "Ohimini",
                "Oturkpo",
                "Tarka",
                "Ukum",
                "Ushongo",
                "Vandeikya"});
        map.put("Borno", new String[]{"Abadam",
                "Askira/Uba",
                "Bama",
                "Bayo",
                "Biu",
                "Chibok",
                "Damboa",
                "Dikwa",
                "Gubio",
                "Guzamala",
                "Gwoza",
                "Hawul",
                "Jere",
                "Kaga",
                "Kala/Balge",
                "Konduga",
                "Kukawa",
                "Kwaya Kusar",
                "Mafa",
                "Magumeri",
                "Maiduguri",
                "Marte",
                "Mobbar",
                "Monguno",
                "Ngala",
                "Nganzai",
                "Shani"});
        map.put("Cross River", new String[]{"Akpabuyo",
                "Odukpani",
                "Akamkpa",
                "Biase",
                "Abi",
                "Ikom",
                "Yarkur",
                "Odubra",
                "Boki",
                "Ogoja",
                "Yala",
                "Obanliku",
                "Obudu",
                "Calabar South",
                "Etung",
                "Bekwara",
                "Bakassi",
                "Calabar Municipality"});
        map.put("Delta", new String[]{"Oshimili",
                "Aniocha",
                "Aniocha South",
                "Ika South",
                "Ika North-East",
                "Ndokwa West",
                "Ndokwa East",
                "Isoko south",
                "Isoko North",
                "Bomadi",
                "Burutu",
                "Ughelli South",
                "Ughelli North",
                "Ethiope West",
                "Ethiope East",
                "Sapele",
                "Okpe",
                "Warri North",
                "Warri South",
                "Uvwie",
                "Udu",
                "Warri Central",
                "Ukwani",
                "Oshimili North",
                "Patani"});
        map.put("Ebonyi", new String[]{"Edda",
                "Afikpo",
                "Onicha",
                "Ohaozara",
                "Abakaliki",
                "Ishielu",
                "lkwo",
                "Ezza",
                "Ezza South",
                "Ohaukwu",
                "Ebonyi",
                "Ivo"});
        map.put("Enugu", new String[]{"Enugu South",
                "Igbo-Eze South",
                "Enugu North",
                "Nkanu",
                "Udi Agwu",
                "Oji-River",
                "Ezeagu",
                "IgboEze North",
                "Isi-Uzo",
                "Nsukka",
                "Igbo-Ekiti",
                "Uzo-Uwani",
                "Enugu Eas",
                "Aninri",
                "Nkanu East",
                "Udenu"});
        map.put("Edo", new String[]{"Esan North-East",
                "Esan Central",
                "Esan West",
                "Egor",
                "Ukpoba",
                "Central",
                "Etsako Central",
                "Igueben",
                "Oredo",
                "Ovia SouthWest",
                "Ovia South-East",
                "Orhionwon",
                "Uhunmwonde",
                "Etsako East",
                "Esan South-East"});
        map.put("Ekiti", new String[]{"Ado",
                "Ekiti-East",
                "Ekiti-West",
                "Emure/Ise/Orun",
                "Ekiti South-West",
                "Ikere",
                "Irepodun",
                "Ijero,",
                "Ido/Osi",
                "Oye",
                "Ikole",
                "Moba",
                "Gbonyin",
                "Efon",
                "Ise/Orun",
                "Ilejemeje"});
        map.put("FCT - Abuja", new String[]{"Abaji",
                "Abuja Municipal",
                "Bwari",
                "Gwagwalada",
                "Kuje",
                "Kwali"});
        map.put("Gombe", new String[]{"Akko",
                "Balanga",
                "Billiri",
                "Dukku",
                "Kaltungo",
                "Kwami",
                "Shomgom",
                "Funakaye",
                "Gombe",
                "Nafada/Bajoga",
                "Yamaltu/Delta"});
        map.put("Imo", new String[]{"Aboh-Mbaise",
                "Ahiazu-Mbaise",
                "Ehime-Mbano",
                "Ezinihitte",
                "Ideato North",
                "Ideato South",
                "Ihitte/Uboma",
                "Ikeduru",
                "Isiala Mbano",
                "Isu",
                "Mbaitoli",
                "Mbaitoli",
                "Ngor-Okpala",
                "Njaba",
                "Nwangele",
                "Nkwerre",
                "Obowo",
                "Oguta",
                "Ohaji/Egbema",
                "Okigwe",
                "Orlu",
                "Orsu",
                "Oru East",
                "Oru West",
                "Owerri-Municipal",
                "Owerri North",
                "Owerri West"});
        map.put("Jigawa", new String[]{"Auyo",
                "Babura",
                "Birni Kudu",
                "Biriniwa",
                "Buji",
                "Dutse",
                "Gagarawa",
                "Garki",
                "Gumel",
                "Guri",
                "Gwaram",
                "Gwiwa",
                "Hadejia",
                "Jahun",
                "Kafin Hausa",
                "Kaugama Kazaure",
                "Kiri Kasamma",
                "Kiyawa",
                "Maigatari",
                "Malam Madori",
                "Miga",
                "Ringim",
                "Roni",
                "Sule-Tankarkar",
                "Taura",
                "Yankwashi"});
        map.put("Kaduna", new String[]{"Birni-Gwari",
                "Chikun",
                "Giwa",
                "Igabi",
                "Ikara",
                "jaba",
                "Jema'a",
                "Kachia",
                "Kaduna North",
                "Kaduna South",
                "Kagarko",
                "Kajuru",
                "Kaura",
                "Kauru",
                "Kubau",
                "Kudan",
                "Lere",
                "Makarfi",
                "Sabon-Gari",
                "Sanga",
                "Soba",
                "Zango-Kataf",
                "Zaria"});
        map.put("Kano", new String[]{"Ajingi",
                "Albasu",
                "Bagwai",
                "Bebeji",
                "Bichi",
                "Bunkure",
                "Dala",
                "Dambatta",
                "Dawakin Kudu",
                "Dawakin Tofa",
                "Doguwa",
                "Fagge",
                "Gabasawa",
                "Garko",
                "Garum",
                "Mallam",
                "Gaya",
                "Gezawa",
                "Gwale",
                "Gwarzo",
                "Kabo",
                "Kano Municipal",
                "Karaye",
                "Kibiya",
                "Kiru",
                "kumbotso",
                "Ghari",
                "Kura",
                "Madobi",
                "Makoda",
                "Minjibir",
                "Nasarawa",
                "Rano",
                "Rimin Gado",
                "Rogo",
                "Shanono",
                "Sumaila",
                "Takali",
                "Tarauni",
                "Tofa",
                "Tsanyawa",
                "Tudun Wada",
                "Ungogo",
                "Warawa",
                "Wudil"});
        map.put("Katsina", new String[]{"Bakori",
                "Batagarawa",
                "Batsari",
                "Baure",
                "Bindawa",
                "Charanchi",
                "Dandume",
                "Danja",
                "Dan Musa",
                "Daura",
                "Dutsi",
                "Dutsin-Ma",
                "Faskari",
                "Funtua",
                "Ingawa",
                "Jibia",
                "Kafur",
                "Kaita",
                "Kankara",
                "Kankia",
                "Katsina",
                "Kurfi",
                "Kusada",
                "Mai'Adua",
                "Malumfashi",
                "Mani",
                "Mashi",
                "Matazuu",
                "Musawa",
                "Rimi",
                "Sabuwa",
                "Safana",
                "Sandamu",
                "Zango"});
        map.put("Kebbi", new String[]{"Aleiro",
                "Arewa-Dandi",
                "Argungu",
                "Augie",
                "Bagudo",
                "Birnin Kebbi",
                "Bunza",
                "Dandi",
                "Fakai",
                "Gwandu",
                "Jega",
                "Kalgo",
                "Koko/Besse",
                "Maiyama",
                "Ngaski",
                "Sakaba",
                "Shanga",
                "Suru",
                "Wasagu/Danko",
                "Yauri",
                "Zuru"});
        map.put("Kogi", new String[]{"Adavi",
                "Ajaokuta",
                "Ankpa",
                "Bassa",
                "Dekina",
                "Ibaji",
                "Idah",
                "Igalamela-Odolu",
                "Ijumu",
                "Kabba/Bunu",
                "Kogi",
                "Lokoja",
                "Mopa-Muro",
                "Ofu",
                "Ogori/Mangongo",
                "Okehi",
                "Okene",
                "Olamabolo",
                "Omala",
                "Yagba East",
                "Yagba West"});
        map.put("Kwara", new String[]{"Asa",
                "Baruten",
                "Edu",
                "Ekiti",
                "Ifelodun",
                "Ilorin East",
                "Ilorin West",
                "Irepodun",
                "Isin",
                "Kaiama",
                "Moro",
                "Offa",
                "Oke-Ero",
                "Oyun",
                "Pategi"});
        map.put("Lagos", new String[]{"Agege",
                "Ajeromi-Ifelodun",
                "Alimosho",
                "Amuwo-Odofin",
                "Apapa",
                "Badagry",
                "Epe",
                "Eti-Osa",
                "Ibeju/Lekki",
                "Ifako-Ijaye",
                "Ikeja",
                "Ikorodu",
                "Kosofe",
                "Lagos Island",
                "Lagos Mainland",
                "Mushin",
                "Ojo",
                "Oshodi-Isolo",
                "Shomolu",
                "Surulere"});
        map.put("Nasarawa", new String[]{"Akwanga",
                "Awe",
                "Doma",
                "Karu",
                "Keana",
                "Keffi",
                "Kokona",
                "Lafia",
                "Nasarawa",
                "Nasarawa-Eggon",
                "Obi",
                "Toto",
                "Wamba"});
        map.put("Niger", new String[]{"Agaie",
                "Agwara",
                "Bida",
                "Borgu",
                "Bosso",
                "Chanchaga",
                "Edati",
                "Gbako",
                "Gurara",
                "Katcha",
                "Kontagora",
                "Lapai",
                "Lavun",
                "Magama",
                "Mariga",
                "Mashegu",
                "Mokwa",
                "Muya",
                "Pailoro",
                "Rafi",
                "Rijau",
                "Shiroro",
                "Suleja",
                "Tafa",
                "Wushishi"});
        map.put("Ogun", new String[]{"Abeokuta North",
                "Abeokuta South",
                "Ado-Odo/Ota",
                "Yewa North",
                "Yewa South",
                "Ewekoro",
                "Ifo",
                "Ijebu East",
                "Ijebu North",
                "Ijebu North East",
                "Ijebu Ode",
                "Ikenne",
                "Imeko-Afon",
                "Ipokia",
                "Obafemi-Owode",
                "Ogun Waterside",
                "Odeda",
                "Odogbolu",
                "Remo North",
                "Shagamu"});
        map.put("Ondo", new String[]{"Akoko North East",
                "Akoko North West",
                "Akoko South Akure East",
                "Akoko South West",
                "Akure North",
                "Akure South",
                "Ese-Odo",
                "Idanre",
                "Ifedore",
                "Ilaje",
                "Ile-Oluji",
                "Okeigbo",
                "Irele",
                "Odigbo",
                "Okitipupa",
                "Ondo East",
                "Ondo West",
                "Ose",
                "Owo"});
        map.put("Osun", new String[]{"Aiyedade",
                "Aiyedire",
                "Atakumosa East",
                "Atakumosa West",
                "Boluwaduro",
                "Boripe",
                "Ede North",
                "Ede South",
                "Egbedore",
                "Ejigbo",
                "Ife Central",
                "Ife East",
                "Ife North",
                "Ife South",
                "Ifedayo",
                "Ifelodun",
                "Ila",
                "Ilesha East",
                "Ilesha West",
                "Irepodun",
                "Irewole",
                "Isokan",
                "Iwo",
                "Obokun",
                "Odo-Otin",
                "Ola-Oluwa",
                "Olorunda",
                "Oriade",
                "Orolu",
                "Osogbo"});
        map.put("Oyo", new String[]{"Afijio",
                "Akinyele",
                "Atiba",
                "Atisbo",
                "Egbeda",
                "Ibadan Central",
                "Ibadan North",
                "Ibadan North West",
                "Ibadan South East",
                "Ibadan South West",
                "Ibarapa Central",
                "Ibarapa East",
                "Ibarapa North",
                "Ido",
                "Irepo",
                "Iseyin",
                "Itesiwaju",
                "Iwajowa",
                "Kajola",
                "Lagelu Ogbomosho North",
                "Ogbomosho South",
                "Ogo Oluwa",
                "Olorunsogo",
                "Oluyole",
                "Ona-Ara",
                "Orelope",
                "Ori Ire",
                "Oyo East",
                "Oyo West",
                "Saki East",
                "Saki West",
                "Surulere"});
        map.put("Plateau", new String[]{"Barikin Ladi",
                "Bassa",
                "Bokkos",
                "Jos East",
                "Jos North",
                "Jos South",
                "Kanam",
                "Kanke",
                "Langtang North",
                "Langtang South",
                "Mangu",
                "Mikang",
                "Pankshin",
                "Qua'an Pan",
                "Riyom",
                "Shendam",
                "Wase"});
        map.put("Rivers", new String[]{"Abua/Odual",
                "Ahoada East",
                "Ahoada West",
                "Akuku Toru",
                "Andoni",
                "Asari-Toru",
                "Bonny",
                "Degema",
                "Emohua",
                "Eleme",
                "Etche",
                "Gokana",
                "Ikwerre",
                "Khana",
                "Obio/Akpor",
                "Ogba/Egbema/Ndoni",
                "Ogu/Bolo",
                "Okrika",
                "Omumma",
                "Opobo/Nkoro",
                "Oyigbo",
                "Port-Harcourt",
                "Tai"});
        map.put("Sokoto", new String[]{"Binji",
                "Bodinga",
                "Dange-shnsi",
                "Gada",
                "Goronyo",
                "Gudu",
                "Gawabawa",
                "Illela",
                "Isa",
                "Kware",
                "kebbe",
                "Rabah",
                "Sabon birni",
                "Shagari",
                "Silame",
                "Sokoto North",
                "Sokoto South",
                "Tambuwal",
                "Tqngaza",
                "Tureta",
                "Wamako",
                "Wurno",
                "Yabo"});
        map.put("Taraba", new String[]{"Ardo-kola",
                "Bali",
                "Donga",
                "Gashaka",
                "Cassol",
                "Ibi",
                "Jalingo",
                "Karin-Lamido",
                "Kurmi",
                "Lau",
                "Sardauna",
                "Takum",
                "Ussa",
                "Wukari",
                "Yorro",
                "Zing"});
        map.put("Yobe", new String[]{"Bade",
                "Bursari",
                "Damaturu",
                "Fika",
                "Fune",
                "Geidam",
                "Gujba",
                "Gulani",
                "Jakusko",
                "Karasuwa",
                "Karawa",
                "Machina",
                "Nangere",
                "Nguru Potiskum",
                "Tarmua",
                "Yunusari",
                "Yusufari"});
        map.put("Zamfara", new String[]{"Anka",
                "Bakura",
                "Birnin Magaji",
                "Bukkuyum",
                "Bungudu",
                "Gummi",
                "Gusau",
                "Kaura",
                "Namoda",
                "Maradun",
                "Maru",
                "Shinkafi",
                "Talata Mafara",
                "Tsafe",
                "Zurmi"});
        return map;
    }

    private class StateComboBoxListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedState = (String) stateComboBox.getSelectedItem();
                updateLocalGovernmentComboBox(selectedState);

                assert selectedState != null;
                if (selectedState.equals("Benue")){
                    String selectedLga = (String) localGovernmentComboBox.getSelectedItem();
                    updateWardsComboBox(selectedLga);
                }else{
                    updateWardsComboBox("-local government-");
                }
            }
        }
    }

    private class LgaComboBoxListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedState = (String) stateComboBox.getSelectedItem();
                String selectedLga = (String) localGovernmentComboBox.getSelectedItem();
                assert selectedState != null;
                if (selectedState.equals("Benue")){
                    updateWardsComboBox(selectedLga);
                }else{
                    updateWardsComboBox("-local government-");
                }
            }
        }
    }

    //Create Indicators button
    private JLabel createIndicatorLabel(String text) {
        JLabel indicatorLabel = new JLabel(text);
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

    private String getGoeCoordinate(){
        String ip_address = "";
        //double [] geoCoordinate = new double[2];
        /*try {
            URL url = new URL("https://freegeoip.app/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = reader.lines().collect(Collectors.joining());

                JsonObject geoData = new JsonParser().parse(response).getAsJsonObject();

                // Extract relevant information
                *//*String countryCode = geoData.get("country_code").getAsString();
                String city = geoData.get("city").getAsString();*//*
                double latitude = geoData.get("latitude").getAsDouble();
                double longitude = geoData.get("longitude").getAsDouble();

                geoCoordinate[0] = latitude;
                geoCoordinate[1] = longitude;

            } else {
                JOptionPane.showMessageDialog(null, "Error: Unable to fetch location data. Response Code: " + responseCode, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            ip_address = localhost.getHostAddress();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://free-geo-ip.p.rapidapi.com/json/197.210.84.90"))
                    .header("X-RapidAPI-Key", "378b3a6a4emsh049d7e5cc80ecb8p153cfbjsn6f4920c0d9bf")
                    .header("X-RapidAPI-Host", "free-geo-ip.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return ip_address;
        //return  geoCoordinate;

        //197.210.84.90
    }

    private String[] getDetails(){
        String cTitle = (String) title.getSelectedItem();
        assert cTitle != null;
        if(cTitle.equals("-select title-")){
            JOptionPane.showMessageDialog(null, "Please select Title.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String fName = firstName.getText().toUpperCase(Locale.ROOT);
        if(fName.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please Enter first name.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String mName = middle_name.getText().toUpperCase(Locale.ROOT);
        if(mName.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please Enter middle name.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String lName = last_name.getText().toUpperCase(Locale.ROOT);
        if(lName.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please Enter last name.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String sex = Objects.requireNonNull(gender.getSelectedItem()).toString();
        if(sex.equals("-select gender-")){
            JOptionPane.showMessageDialog(null, "Please select gender.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String maritalStatus = (String) mStatus.getSelectedItem();

        assert maritalStatus != null;
        if(maritalStatus.equals("-marital status-")){
            JOptionPane.showMessageDialog(null, "Please select marital status.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String cReligion = (String) religion.getSelectedItem();
        assert cReligion != null;
        if(cReligion.equals("-religion-")){
            JOptionPane.showMessageDialog(null, "Please select religion.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        /*Handle date of birth*/
        String dob;
        //Get Date from date picker
        // Get the date from the DatePicker
        Object dateObject = datePicker.getModel().getValue();
        // Check if the dateObject is not null
        if (dateObject != null) {
            // Convert the dateObject to a Date
            Date date = (Date) dateObject;
            // Format the date as "dd/MM/yyyy"
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dob = dateFormat.format(date);
        } else {
            JOptionPane.showMessageDialog(null, "Please selected Date of Birth.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        /*CALCULATE AGE*/
        // Parse the date of birth string to LocalDate
        LocalDate localDate = LocalDate.parse(dob, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Calculate age
        int intAge = calculateAge(localDate);
        String age = String.valueOf(intAge);




        String nOfChildren = no_children.getText();
        if(nOfChildren.isEmpty()){
            JOptionPane.showMessageDialog(null, "Enter number of children.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String hairColor = (String) colorOfHair.getSelectedItem();
        assert hairColor != null;
        if(hairColor.equals("-color of hair-")){
            JOptionPane.showMessageDialog(null, "Please select color of hair.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String hunch = hunchback.getText().toUpperCase(Locale.ROOT);
        if(hunch.isEmpty() || hunch.length()<2){
            JOptionPane.showMessageDialog(null, "Invalid entry for Hunchback.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String tMark = (String) tribalMark.getSelectedItem();
        assert tMark != null;
        if(tMark.equals("-tribal mark?-")){
            JOptionPane.showMessageDialog(null, "Select tribal mark.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String nokfName = nonFirstName.getText().toUpperCase(Locale.ROOT);
        if(nokfName.isEmpty() || nokfName.length()<2){
            JOptionPane.showMessageDialog(null, "Enter next of kin first name", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String nokoNames = nonOtherNames.getText().toUpperCase(Locale.ROOT);
        if(nokoNames.isEmpty() || nokoNames.length()<2){
            JOptionPane.showMessageDialog(null, "Enter next of kin other names", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String nokPhone = nonPhone.getText();
        if(nokPhone.isEmpty() || nokPhone.length()<11){
            JOptionPane.showMessageDialog(null, "Enter valid phone NOK phone number", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String nokRel = (String) nokRelationship.getSelectedItem();
        assert nokRel != null;
        if(nokRel.equals("-relationship with NOK-")){
            JOptionPane.showMessageDialog(null, "Select relationship with NOK", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String phone = phone_number.getText();
        if(phone.isEmpty() || phone.length()<11){
            JOptionPane.showMessageDialog(null, "Enter valid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String email = email_address.getText();
        if(email.isEmpty() || email.length()<2){
            JOptionPane.showMessageDialog(null, "Enter a valid Email address", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String cState = (String) stateComboBox.getSelectedItem();
        assert cState != null;
        if(cState.equals("-state of origin-")){
            JOptionPane.showMessageDialog(null, "Please select State of origin.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String cLga = (String) localGovernmentComboBox.getSelectedItem();
        assert cLga != null;
        if(cLga.equals("-local government-")){
            JOptionPane.showMessageDialog(null, "Please select Local government of origin.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String cWard = (String) wardsComboBox.getSelectedItem();
        assert cWard != null;
        if(cWard.equals("-council ward-")){
            JOptionPane.showMessageDialog(null, "Please select Council ward of origin.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String prof = profession.getText().toUpperCase(Locale.ROOT);
        if(prof.isEmpty() || prof.length()<2){
            JOptionPane.showMessageDialog(null, "Enter a valid Profession", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String qual = (String) qualification.getSelectedItem();
        assert qual != null;
        if(qual.equals("-qualification-")){
            JOptionPane.showMessageDialog(null, "Select qualification", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String address = contactAddress.getText().toUpperCase(Locale.ROOT);
        if(address.isEmpty() || address.length()<2){
            JOptionPane.showMessageDialog(null, "Enter a valid contact address", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String dStatus = (String) statusComboBox.getSelectedItem();
        assert dStatus != null;
        if(dStatus.equals("-disability status-")){
            JOptionPane.showMessageDialog(null, "Please select disability status.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String disability = (String) disabilityComboBox.getSelectedItem();


        //Handle pension status
        String pensionStatus;
        ButtonModel selectedButton = buttonGroup.getSelection();
        if (selectedButton != null){
            pensionStatus = selectedButton.getActionCommand();
        }else{
            JOptionPane.showMessageDialog(null, "Please select pension status.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }

        String pensionPlace = placeOfPensionTextField.getText().toUpperCase(Locale.ROOT);
        String bank = (String) bankName.getSelectedItem();
        assert bank != null;
        if(bank.equals("-bank name-")){
            JOptionPane.showMessageDialog(null, "Select bank name.", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String acctNo = accountNumber.getText();
        if(acctNo.isEmpty() || acctNo.length()<10){
            JOptionPane.showMessageDialog(null, "Provide a valid account number", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String acctName = accountName.getText().toUpperCase(Locale.ROOT);
        if(acctName.length()<2){
            JOptionPane.showMessageDialog(null, "Enter account name", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        String cBvn = bvn.getText().toUpperCase(Locale.ROOT);
        if(cBvn.length()<11){
            JOptionPane.showMessageDialog(null, "Provide a valid BVN", "Warning", JOptionPane.WARNING_MESSAGE);
            return new String[0];
        }
        // Get today's date
        LocalDate today = LocalDate.now();

        // Format the date as "dd/MM/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);

        String[] getCoordinate = {generateRandomDouble(), generateRandomDouble()};

        return new String[]{nin, cTitle, fName, mName, lName, sex, phone, cLga, formattedDate, dob, age, maritalStatus, cReligion, nOfChildren,
                cState, cWard, dStatus, disability,hairColor, hunch, tMark, nokfName, nokoNames, nokPhone, nokRel,
        email, prof, qual, address, pensionStatus, pensionPlace, bank, acctName, acctNo, cBvn, getCoordinate[0], getCoordinate[1], cPhotoPath, cBiometricPath, mUsername};
    }

    /*WRITE RECORD TO THE CSV FILE*/
    private boolean writeRecord(String[] citizenDetails){
        boolean status = false;
        if (citizenDetails.length==0){
            return false;
        }
        try(CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))){
            writer.writeNext(citizenDetails);
            status = true;
        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "Unable to submit record.", "Error", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
        return status;
    }

    private int calculateAge(LocalDate dob) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the period between the date of birth and the current date
        Period period = Period.between(dob, currentDate);

        // Return the age
        return period.getYears();
    }
    private String generateRandomDouble(){
        Random random = new Random();
        int integerPart = random.nextInt(10);
        double decimalPart = random.nextDouble();
        return  String.valueOf(integerPart+decimalPart);
    }

    //Check internet connection
    public static boolean isInternetConnected(){
        try{
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(5000);
        }catch (IOException e){
            return false;
        }
    }
}

