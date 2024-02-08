import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.digitalpersona.uareu.Engine;
import com.digitalpersona.uareu.Fid;
import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;

public class BiometricExample extends JFrame implements ActionListener {
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
    private JLabel captureMsg = new JLabel();



    public class BiometricExampleThread extends Thread implements
            Engine.EnrollmentCallback {
        public static final String ACT_PROMPT = "enrollment_prompt";
        public static final String ACT_CAPTURE = "enrollment_capture";
        public static final String ACT_FEATURES = "enrollment_features";
        public static final String ACT_DONE = "enrollment_done";
        public static final String ACT_CANCELED = "enrollment_canceled";

        public class EnrollmentEvent extends ActionEvent {
            private static final long serialVersionUID = 102;

            public Reader.CaptureResult capture_result;
            public Reader.Status reader_status;
            public UareUException exception;
            public Fmd enrollment_fmd;

            public EnrollmentEvent(Object source, String action, Fmd fmd,
                                   Reader.CaptureResult cr, Reader.Status st, UareUException ex) {
                super(source, ActionEvent.ACTION_PERFORMED, action);
                capture_result = cr;
                reader_status = st;
                exception = ex;
                enrollment_fmd = fmd;
            }
        }

        private final Reader m_reader;
        private CaptureThread m_capture;
        private final ActionListener m_listener;
        private boolean m_bCancel;

        protected BiometricExampleThread(Reader reader, ActionListener listener) {
            m_reader = reader;
            m_listener = listener;
        }

        @Override
        public Engine.PreEnrollmentFmd GetFmd(Fmd.Format format) {
            Engine.PreEnrollmentFmd prefmd = null;
            while (null == prefmd && !m_bCancel) {
                // start capture thread
                m_capture = new CaptureThread(m_reader, false,
                        Fid.Format.ISO_19794_4_2005,
                        Reader.ImageProcessing.IMG_PROC_DEFAULT);
                m_capture.start(null);

                // prompt for finger
                SendToListener(ACT_PROMPT, null, null, null, null);

                // wait till done
                m_capture.join(0);

                // check result
                CaptureThread.CaptureEvent evt = m_capture
                        .getLastCaptureEvent();
                if (null != evt.capture_result) {
                    if (Reader.CaptureQuality.CANCELED == evt.capture_result.quality) {
                        // capture canceled, return null
                        break;
                    } else if (null != evt.capture_result.image
                            && Reader.CaptureQuality.GOOD == evt.capture_result.quality) {
                        // Send image
                        SendToListener(ACT_CAPTURE, null, evt.capture_result,
                                null, null);

                        // acquire engine
                        Engine engine = UareUGlobal.GetEngine();

                        try {
                            // extract features

                            Fmd fmd = engine.CreateFmd(
                                    evt.capture_result.image,
                                    Fmd.Format.DP_PRE_REG_FEATURES);

                            // return prefmd
                            prefmd = new Engine.PreEnrollmentFmd();
                            prefmd.fmd = fmd;
                            prefmd.view_index = 0;

                            // send success
                            SendToListener(ACT_FEATURES, null, null, null, null);
                        } catch (UareUException e) {
                            // send extraction error
                            SendToListener(ACT_FEATURES, null, null, null, e);
                        }
                    } else {
                        // send quality result
                        SendToListener(ACT_CAPTURE, null, evt.capture_result,
                                evt.reader_status, evt.exception);
                    }
                } else {
                    // send capture error
                    SendToListener(ACT_CAPTURE, null, evt.capture_result,
                            evt.reader_status, evt.exception);
                }
            }

            return prefmd;
        }

        public void cancel() {
            m_bCancel = true;
            if (null != m_capture)
                m_capture.cancel();
        }

        private void SendToListener(String action, Fmd fmd,
                                    Reader.CaptureResult cr, Reader.Status st, UareUException ex) {
            if (null == m_listener || null == action || action.equals(""))
                return;

            final EnrollmentEvent evt = new EnrollmentEvent(this, action, fmd,
                    cr, st, ex);

            // invoke listener on EDT thread
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> m_listener.actionPerformed(evt));
            } catch (InvocationTargetException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            // acquire engine
            Engine engine = UareUGlobal.GetEngine();

            try {
                m_bCancel = false;
                while (!m_bCancel) {
                    // run enrollment
                    Fmd fmd = engine.CreateEnrollmentFmd(
                            Fmd.Format.DP_REG_FEATURES, this);

                    // send result
                    if (null != fmd) {

                        SendToListener(ACT_DONE, fmd, null, null, null);
                    } else {
                        SendToListener(ACT_CANCELED, null, null, null, null);
                        break;
                    }
                }
            } catch (UareUException e) {
                JOptionPane.showMessageDialog(null,
                        "Exception during creation of data and import");
                SendToListener(ACT_DONE, null, null, null, e);
            }
        }
    }

    private static final long serialVersionUID = 6;
    private static final String ACT_SAVE = "save";

    public com.digitalpersona.uareu.Fmd enrollmentFMD;
    private BiometricExampleThread m_enrollment;
    private Reader m_reader;
    private JDialog m_dlgParent;
    private boolean m_bJustStarted;

    BiometricExample(Reader reader) {
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
        addComponents(reader);
        //centerFrameOnScreen();
        setExtendedState(JFrame.MAXIMIZED_BOTH);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ACT_SAVE)) {
            saveDataToFile(enrollmentFMD.getData());
            return;
        } else {

            BiometricExampleThread.EnrollmentEvent evt = (BiometricExampleThread.EnrollmentEvent) e;

            if (e.getActionCommand().equals(BiometricExampleThread.ACT_PROMPT)) {
                if (m_bJustStarted) {
                    captureMsg.setText("Enrollment started");
                    captureMsg.setText("Put your any finger on the reader");
                } else {
                    captureMsg.setText("Put the same finger on the reader");
                }
                m_bJustStarted = false;
            } else if (e.getActionCommand()
                    .equals(BiometricExampleThread.ACT_CAPTURE)) {

                if (null != evt.capture_result)
                    if (evt.capture_result.image != null)
                        m_imagePanel.showImage(evt.capture_result.image);
                if (null != evt.capture_result) {
                    // MessageBox.BadQuality(evt.capture_result.quality);
                } else if (null != evt.exception) {

                    // MessageBox.DpError("Capture", evt.exception);
                } else if (null != evt.reader_status) {
                    // MessageBox.BadStatus(evt.reader_status);
                }

                m_bJustStarted = false;
            } else if (e.getActionCommand().equals(
                    BiometricExampleThread.ACT_FEATURES)) {
                if (null == evt.exception) {
                    captureMsg.setText("Fingerprint captured");
                } else {
                    MessageBox.DpError("Feature extraction", evt.exception);
                }
                m_bJustStarted = false;
            }

            else if (e.getActionCommand().equals(BiometricExampleThread.ACT_DONE)) {
                if (null == evt.exception) {
                    enrollmentFMD = evt.enrollment_fmd;
                    m_enrollment.cancel();
                    captureMsg.setText("Enrollment completed");
                } else {
                    MessageBox.DpError("Enrollment template creation",
                            evt.exception);
                }
                m_bJustStarted = true;
            } else if (e.getActionCommand().equals(
                    BiometricExampleThread.ACT_CANCELED)) {
                // canceled, destroy dialog
                m_dlgParent.setVisible(false);
            }

            // cancel enrollment if any exception or bad reader status
            if (null != evt.exception) {
                m_dlgParent.setVisible(false);
            } else if (null != evt.reader_status
                    && Reader.ReaderStatus.READY != evt.reader_status.status
                    && Reader.ReaderStatus.NEED_CALIBRATION != evt.reader_status.status) {
                m_dlgParent.setVisible(false);
            }
        }
    }


    //Save biometrics
    private void saveDataToFile(byte[] data) {
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream("biometrics_files/"+ "1245632" +"biometric.wsq"))) {
            // Convert the data to WSQ format using JNBIS library

            //FileHandler pngFingerPrint = Jnbis.wsq().decode(data).toPng();
            // Save the WSQ data to the specified file path
            output.write(data);
            JOptionPane.showMessageDialog(null, "Saved successfully.", "Captured", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doModal(JDialog dlgParent) {
        // open reader
        try {
            m_reader.Open(Reader.Priority.EXCLUSIVE);
        } catch (UareUException e) {
            MessageBox.DpError("Reader.Open()", e);
        }

        // start enrollment thread
        m_enrollment.start();

        // bring up modal dialog
        m_dlgParent = dlgParent;
        m_dlgParent.setContentPane(this);
        m_dlgParent.pack();
        m_dlgParent.setLocationRelativeTo(null);
        m_dlgParent.setVisible(true);
        m_dlgParent.dispose();
        // stop enrollment thread
        m_enrollment.cancel();

        // close reader
        try {
            m_reader.Close();
        } catch (UareUException e) {
            MessageBox.DpError("Reader.Close()", e);
        }
    }


    public static Fmd Run(Reader reader) {
        JDialog dlg = new JDialog((JDialog) null, "Enrollment", true);
        BiometricExample enrollment = new BiometricExample(reader);
        enrollment.doModal(dlg);
        return enrollment.enrollmentFMD;
    }

    //MY UI METHODS
    //Initiate components
    private void addComponents(Reader reader) {

        LineBorder lineBorder = new LineBorder(Color.decode(secondary_color), 2, true);
        //OLD READER CLASS PROPERTIES
        m_reader = reader;
        m_bJustStarted = true;
        m_enrollment = new BiometricExampleThread(m_reader, this);

        //Biometric image Pane
        m_imagePanel = new ImagePanel();
        m_imagePanel.setPreferredSize(new Dimension(100, 300));
        m_imagePanel.setBorder(lineBorder);
        m_imagePanel.setBackground(Color.lightGray);


        //MY CODES

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


        //gbc.insets = new Insets(0, 200, 0,0);
        gbc.gridy = 2;
        gbc.ipadx = 100;
        gbc.ipady = 250;
        imgPanelBorder.add(m_imagePanel);
        biometricPane.add(imgPanelBorder, gbc);

        //capture output information
        //Place right thumb on the Reader
        captureMsg.setSize(80, 80);
        captureMsg.setForeground(Color.decode(secondary_color));
        captureMsg.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        captureMsg.setBorder(lineBorder);
        gbc.gridy = 3;
        gbc.ipady = 10;
        gbc.ipadx = 0;
        biometricPane.add(captureMsg, gbc);

        //Save Biometrics button
        saveBiometrics = new JButton("SAVE AND CONTINUE");
        saveBiometrics.setBackground(Color.decode(primary_color));
        saveBiometrics.setForeground(Color.white);
        saveBiometrics.setBorder(null);

        //configure save biometrics button
        saveBiometrics.setActionCommand(ACT_SAVE);
       /* saveBiometrics.addActionListener(e -> {
            if (captureMsg.getText().equals("Enrollment completed")){
                *//*IMPLEMENT SAVE BIOMETRICS LOGIC HERE*//*
                JOptionPane.showMessageDialog(null, "Saved successfully", "Captured", JOptionPane.INFORMATION_MESSAGE);
                //new Photograph("123456").setVisible(true);
                //dispose();
            }else {
                JOptionPane.showMessageDialog(null, "Wait until Enrollment is completed", "Please wait", JOptionPane.ERROR_MESSAGE);
            }
        });*/

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
