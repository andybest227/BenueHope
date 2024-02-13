/*public class NewEnrollment {
}*/

import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class NewEnrollment extends JPanel implements ActionListener {
    //colors
    String primary_color = "#043e45";
    private final CustomDialog wait_dialog = new CustomDialog("Initializing WebCam...", Color.ORANGE);

    public class EnrollmentThread extends Thread implements
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

        protected EnrollmentThread(Reader reader, ActionListener listener) {
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
                javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        m_listener.actionPerformed(evt);
                    }
                });
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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
    public static String ninNumberValue, username, biometricPath;

    public com.digitalpersona.uareu.Fmd enrollmentFMD;
    private final EnrollmentThread m_enrollment;
    private final Reader m_reader;
    private JDialog m_dlgParent;
    private final JTextArea m_text;
    private boolean m_bJustStarted;
    private final JButton m_save;
    private final ImagePanel m_imagePanel;

    private NewEnrollment(String mUsername, String mNinNumber, Reader reader) {
        m_reader = reader;
        m_bJustStarted = true;
        m_enrollment = new EnrollmentThread(m_reader, this);
        username = mUsername;
        ninNumberValue = mNinNumber;
        final int vgap = 5;
        final int width = 380;
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        m_imagePanel = new ImagePanel();
        m_imagePanel.setPreferredSize(new Dimension(100, 300));
        add(m_imagePanel);

        m_text = new JTextArea(22, 1);
        m_text.setEditable(false);
        JScrollPane paneReader = new JScrollPane(m_text);
        add(paneReader);
        Dimension dm = paneReader.getPreferredSize();
        dm.width = width;
        paneReader.setPreferredSize(dm);

        add(Box.createVerticalStrut(vgap));

        m_save = new JButton("SAVE AND CONTINUE");
        m_save.setBackground(Color.decode(primary_color));
        m_save.setSize(160, 40);
        m_save.setForeground(Color.white);
        m_save.setActionCommand(ACT_SAVE);
        m_save.addActionListener(this);
        m_save.setEnabled(false);
        add(m_save);


        add(Box.createVerticalStrut(vgap));
        setOpaque(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getActionCommand().equals(ACT_SAVE)) {
            saveDataToFile(enrollmentFMD.getData());
            this.m_save.setEnabled(false);
            return;
        } else {

            EnrollmentThread.EnrollmentEvent evt = (EnrollmentThread.EnrollmentEvent) e;

            if (e.getActionCommand().equals(EnrollmentThread.ACT_PROMPT)) {
                if (m_bJustStarted) {
                    m_text.append("Enrollment started\n");
                    m_text.append("    Put your any finger on the reader\n");
                } else {
                    m_text.append("    Put the same finger on the reader\n");
                }
                m_bJustStarted = false;
            } else if (e.getActionCommand()
                    .equals(EnrollmentThread.ACT_CAPTURE)) {

                if (null != evt.capture_result)
                    if (evt.capture_result.image != null)
                        m_imagePanel.showImage(evt.capture_result.image);
                System.out.println("Score is " + evt.capture_result.score);
                System.out.println("Qualityis " + evt.capture_result.quality);

                if (null != evt.capture_result) {
                    // MessageBox.BadQuality(evt.capture_result.quality);
                } else if (null != evt.exception) {

                    // MessageBox.DpError("Capture", evt.exception);
                } else if (null != evt.reader_status) {
                    // MessageBox.BadStatus(evt.reader_status);
                }

                m_bJustStarted = false;
            } else if (e.getActionCommand().equals(
                    EnrollmentThread.ACT_FEATURES)) {
                if (null == evt.exception) {
                    m_text.append("    fingerprint captured, features extracted\n\n");
                } else {
                    MessageBox.DpError("Feature extraction", evt.exception);
                }
                m_bJustStarted = false;
            }

            else if (e.getActionCommand().equals(EnrollmentThread.ACT_DONE)) {
                if (null == evt.exception) {
                    String str = String
                            .format("    Enrollment template created, size: %d\n\n\nPlease save to file or verify.",
                                    evt.enrollment_fmd.getData().length);
                    enrollmentFMD = evt.enrollment_fmd;
                    m_enrollment.cancel();
                    this.m_save.setEnabled(true);
                    m_text.append(str);
                } else {
                    MessageBox.DpError("Enrollment template creation",
                            evt.exception);
                }
                m_bJustStarted = true;
            } else if (e.getActionCommand().equals(
                    EnrollmentThread.ACT_CANCELED)) {
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
    private void saveDataToFile(byte[] data) {
        File filePath = new File("src/files/biometrics_files/"+ninNumberValue);

        biometricPath = "src/files/biometrics_files/"+ninNumberValue;
        OutputStream output;
        try {
            output = new BufferedOutputStream(new FileOutputStream(
                    filePath));
            output.write(data);
            output.close();
            JOptionPane.showMessageDialog(null, "Biometrics saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            wait_dialog.setVisible(true);
            worker.execute();
            m_dlgParent.dispose();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
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
    public static Image icon = new ImageIcon(Objects.requireNonNull(NewEnrollment.class.getResource("icons/LOGO.png"))).getImage();
    public static Fmd Run(String mUsername, String ninNumber, Reader reader) {
        JDialog dlg = new JDialog((JDialog) null, "Biometric Enrollment", true);
        dlg.setIconImage(icon);
        NewEnrollment enrollment = new NewEnrollment(mUsername, ninNumber, reader);
        enrollment.doModal(dlg);
        return enrollment.enrollmentFMD;
    }

    // Create and execute the background task
    SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() {
            // Perform your background task here
            new Photograph(ninNumberValue, username, biometricPath).setVisible(true);
            return null;
        }

        @Override
        protected void done() {
            // Close the dialog box when the background task is completed
            wait_dialog.dispose();
            //JOptionPane.showMessageDialog(null, "Task completed!");
        }
    };
}
