import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Index extends JFrame {
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
    private final JPanel homePane = new JPanel(new GridBagLayout());
    private JButton uploadRecords;
    private final String FILE_PATH = "src/files/temporal_records.csv";
    private final String username;
    private SwingWorker<Void, Void> worker;
    // Column names
    private final Object[] columnNames = {"NIN", "BVN", "FIRST NAME", "MIDDLE NAME", "LAST NAME", "GENDER", "PHONE NUMBER", "LGA", "DATE CAPTURED"};
    private final Object[][] data = readRecords(FILE_PATH);
    private JTable table;
    private final CustomDialog dialog = new CustomDialog("Uploading record(s).\n Please wait...", Color.ORANGE);
    public Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dsssjumkh",
            "api_key", "819342592533741",
            "api_secret", "B9Lyp1tcx6v5ippcAb_HJRsaQlQ"
    ));

    public Index(String mUsername){
        //get values from the constructor
        username = mUsername;
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
    private void addComponents(){
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

        JLabel navHeaderLabel = new JLabel("ACTIONS");
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

        /*START OF HOME PANE*/
        homePane.setBackground(Color.white);

        //label icon
        JLabel lbl_icon = new JLabel();
        lbl_icon.setSize(80, 80);
        lbl_icon.setIcon(functions.ResizeImage(lbl_icon,"icons/checked.png", null));
        lbl_icon.setHorizontalAlignment(JLabel.CENTER);
        gbc.insets = new Insets(30,5,10,5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        homePane.add(lbl_icon, gbc);

        //label title
        JLabel labelTitle = new JLabel("Welcome back");
        labelTitle.setSize(80, 80);
        labelTitle.setForeground(Color.decode(primary_color));
        labelTitle.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
        labelTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.insets = new Insets(2, 2, 0,2);
        gbc.gridy = 1;
        homePane.add(labelTitle, gbc);

        JLabel pendingRecords = new JLabel("Pending Records");
        pendingRecords.setSize(80, 80);
        pendingRecords.setForeground(Color.decode(primary_color));
        pendingRecords.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
        gbc.insets = new Insets(2, 2, 0,2);
        gbc.gridy = 2;
        homePane.add(pendingRecords, gbc);


        //CREATE TABLE TO HOLD RECORD

        // Create JTable with the DefaultTableModel
        table = new JTable();
        addDataToTable(table, data, columnNames);
        // Set up JFrame with JTable
        JScrollPane scrollPane = new JScrollPane(table);
        gbc.gridy = 3;
        gbc.ipadx = width-400;
        gbc.ipady = height-500;
        homePane.add(scrollPane, gbc);


        cards.add(homePane);

        //Nav buttons indicators
        //nin indicator
        JButton newRecord = createIndicatorLabel("ADD NEW RECORD");
        newRecord.setBounds(30, 50, 200, 30);
        newRecord.addActionListener(e ->{
            new NinVerification(username).setVisible(true);
            dispose();
        });

        //bio data indicator
        uploadRecords = createIndicatorLabel("UPLOAD RECORD TO SERVER");
        uploadRecords.setBounds(30, 100, 200, 30);

        uploadRecords.addActionListener(e -> {
            uploadRecords.setText("Uploading, please wait...");
            uploadRecords.setEnabled(false);
            if (InternetConnection.isInternetConnected()) {
                dialog.setVisible(true);
                // Create and execute the background task
                worker = new SwingWorker<>() {
                            @Override
                            protected Void doInBackground() {
                                // Perform your background task here
                                readAndDeleteRecords();
                                return null;
                            }

                            @Override
                            protected void done() {
                                // Close the dialog box when the background task is completed
                                dialog.dispose();
                                //JOptionPane.showMessageDialog(null, "Task completed!");
                            }
                        };
                worker.execute();
            } else {
                JOptionPane.showMessageDialog(null, "No internet connection", "Error", JOptionPane.ERROR_MESSAGE);
                uploadRecords.setText("UPLOAD RECORD TO SERVER");
                uploadRecords.setEnabled(true);
            }

        });

        if (readRecords(FILE_PATH)[0][0] == ""){
            uploadRecords.setEnabled(false);
        }

        //biometrics indicator
        JButton logOut = createIndicatorLabel("LOGOUT");
        logOut.setBounds(30, 150, 200, 30);
        logOut.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        sideNavHolder.add(newRecord);
        sideNavHolder.add(uploadRecords);
        sideNavHolder.add(logOut);

        //Collect all indicator
        JButton[] indicators = {newRecord, uploadRecords, logOut};

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
    private JButton createIndicatorLabel(String text) {
        JButton indicatorLabel = new JButton(text);
        indicatorLabel.setBackground(Color.white);
        indicatorLabel.setBorder(new LineBorder(Color.gray, 2, true));
        indicatorLabel.setForeground(Color.gray);
        indicatorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        indicatorLabel.setHorizontalAlignment(JLabel.CENTER);
        return indicatorLabel;
    }

    //Activate indicator
    private void activateIndicator(JButton indicator) {
        indicator.setOpaque(true);
        indicator.setBackground(Color.decode(primary_color));
        indicator.setBorder(new LineBorder(Color.decode(secondary_color), 2, true));
        indicator.setForeground(Color.white);
        indicator.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        indicator.setHorizontalAlignment(JLabel.CENTER);
    }


    //READ RECORD FROM A CSV FILE
    public Object[][] readRecords(String FILE_PATH) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(FILE_PATH)).build()) {
            List<String[]> records = reader.readAll();

            // Check if records are present
            if (records.isEmpty()) {
                return new Object[][]{{"","","","","NO PENDING RECORD","","","",""}};
            }

            // Create a 2D array with the size of the records
            Object[][] data = new Object[records.size()][records.get(0).length];

            // Populate the 2D array with the records
            for (int i = 0; i < records.size(); i++) {
                data[i] = records.get(i);
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[][]{{"", "", "", "","UNABLE TO READ RECORDS", "", "", "", ""}};
        }
    }

    //Add data to table
    private void addDataToTable(JTable table, Object[][] mData, Object[] columnsNames){
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnsNames);

        model.setRowCount(0);
        for (Object[] row : mData){
            model.addRow(row);
        }
        table.setModel(model);

        if (mData.length == 0){
            // Center and bold the "NO RECORD" text
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            centerRenderer.setFont(centerRenderer.getFont().deriveFont(Font.BOLD));

            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }

    private void refreshTableData (){
        // Clear the current data in the table
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        // Add the new data to the table
        addDataToTable(table, readRecords(FILE_PATH), columnNames);
    }

    //Upload photo to cloudinary
    private String cloudinary_photo_path(String path, String publicId){

        // Upload the image
        try {
            Map uploadResult = cloudinary.uploader().upload(path, ObjectUtils.asMap(
                    "public_id", publicId
            ));
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to establish connection with the server", "Error", JOptionPane.ERROR_MESSAGE);
            uploadRecords.setText("UPLOAD RECORD TO SERVER");
            uploadRecords.setEnabled(true);
            e.printStackTrace();
            return null;
        }
// Get the URL of the uploaded image
    }

    private String cloudinary_raw_file(String path, String publicId){
        try {
            Map uploadResult = cloudinary.uploader().upload(path, ObjectUtils.asMap("resource_type", "raw", "public_id", publicId));
            return (String) uploadResult.get("url");
        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "Unable to establish connection with the server", "Error", JOptionPane.ERROR_MESSAGE);
            uploadRecords.setText("UPLOAD RECORD TO SERVER");
            uploadRecords.setEnabled(true);
            e.printStackTrace();
            return null;
        }
    }

    //Delete records from CSV file
    public void deletePendingRecords(String FILE_PATH) {
        try(CSVWriter ignored = new CSVWriter(new FileWriter(FILE_PATH, false))){
            JOptionPane.showMessageDialog(null, "Upload completed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTableData ();
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "An error occurred", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Push record to server
    private int uploadRecord(String[] records){
        //Push image to cloudinary and return path
        String cloudinaryImagePath = cloudinary_photo_path(records[37], records[0]+"_photograph");
        if (cloudinaryImagePath == null){
            return 600;
        }

        //Push biometric to cloudinary and return path
        String cloudinaryBiometricPath = cloudinary_raw_file(records[38], records[0]+"_biometric");
        if (cloudinaryBiometricPath == null){
            return 600;
        }
        try {
            var url = new URI("https://bsscc.ng/api/save-citizens").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type" , "application/json");
            conn.setRequestProperty("Accept" , "application/json");
            conn.setRequestProperty("User-Agent" , "Mozilla");
            String input = String.format(
                    "{\"NIN\":\"%s\",\"BVN\":\"%s\",\"title\":\"%s\",\"surname\":\"%s\",\"first_name\":\"%s\",\"middle_name\":\"%s\",\"gender\":\"%s\",\"phone_no\":\"%s\",\"email\":\"%s\",\"tribal_mark\":\"%s\",\"hair_colour\":\"%s\",\"hunch_back\":\"%s\",\"soo\":\"%s\",\"lga\":\"%s\",\"ward\":\"%s\",\"dob\":\"%s\",\"age\":\"%s\",\"marital_status\":\"%s\",\"number_of_children\":\"%s\",\"religion\":\"%s\",\"qualification\":\"%s\",\"profession_handwork\":\"%s\",\"is_pensioner\":\"%s\",\"pension_place\":\"%s\",\"disability\":\"%s\",\"nok_surname\":\"%s\",\"nok_firstname\":\"%s\",\"nok_middlename\":\"%s\",\"nok_relationship\":\"%s\",\"nok_phone\":\"%s\",\"bank\":\"%s\",\"account_name\":\"%s\",\"account_no\":\"%s\",\"longitude\":\"%s\",\"latitude\":\"%s\",\"photo\":\"%s\",\"fingerprint\":\"%s\",\"captured_by\":\"%s\",\"date_captured\":\"%s\",\"isdisabled\":\"%s\"}",
                    records[0], records[1], records[36], records[2], records[4], records[3], records[5], records[6], records[25], records[20], records[18], records[19], records[14], records[7], records[15], records[9], records[10], records[11], records[13], records[12], records[27], records[26], records[29], records[28], records[16], records[21], records[22], records[22], records[24], records[23], records[31], records[32], records[33], records[35], records[36], cloudinaryImagePath, cloudinaryBiometricPath, records[39], records[8], records[16]
            );
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            if(conn.getResponseCode() == 200){
                deleteImageFile(records[0]+".png");
                deleteBiometricFile(records[38]);
                return conn.getResponseCode();
            }else{
                JOptionPane.showMessageDialog(null, "Operation failed!", "Error", JOptionPane.ERROR_MESSAGE);
                refreshTableData();
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 500;
    }


    //Read record from CSV file
    public void readAndDeleteRecords() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(FILE_PATH)).build()) {
            List<String[]> records = reader.readAll();

            // Iterate over the rows in reverse order
            int size = records.size();
            for (int i = records.size() - 1; i >= 0; i--) {
                String[] record = records.get(i);

                // Call the uploadRecord method here
                if (uploadRecord(record) == 200){
                    // Delete the last row in the CSV file
                    deleteLastRow();
                    size--;
                }else{
                    worker.cancel(true);
                }
            }

            if(size==0){
                JOptionPane.showMessageDialog(null, "Record uploaded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                uploadRecords.setText("UPLOAD RECORD TO SERVER");
                refreshTableData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Delete pushed record from csv file
    private void deleteLastRow() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(FILE_PATH)).build()) {
            List<String[]> records = reader.readAll();

            if (!records.isEmpty()) {
                // Remove the last row
                records.remove(records.size() - 1);

                // Rewrite the CSV file without the last row
                try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
                    writer.writeAll(records);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //A method to delete image file after uploading
    private void deleteImageFile(String fileName) {
        Path filePath = Paths.get("src/files/photo_files", fileName);
        try {
            Files.delete(filePath);
        } catch (NoSuchFileException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error deleting file: " + filePath);
            e.printStackTrace();
        }
    }


    //Delete local biometric file
    private void deleteBiometricFile(String path){
        File file = new File(path);
        if (file.exists()){
            if (!file.delete()){
                System.err.println("Unable to delete biometric file at: "+path);
            }
        }else{
            System.err.println("Biometric file does not exist at : "+path);
        }
    }
}
