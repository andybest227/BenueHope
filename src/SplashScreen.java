import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SplashScreen extends JFrame {
    //colors
    String primary_color = "#043e45";
    String secondary_color = "#11856b";
    public JProgressBar progressBar;
    public JLabel percentage;

    //size
    int width = 500;
    int height = 300;

    public SplashScreen(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setBackground(Color.white);
        addComponents();
        centerFrameOnScreen();

        // Set image icon
        Image icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("icons/LOGO.png"))).getImage();
        setIconImage(icon);

        //Remove default header
        setUndecorated(true);

        // Disable maximize button
        setResizable(false);

    }

    //Add Componnets
    //Add components
    private void addComponents() {

        ImageResize functions = new ImageResize();
        //home panel layout
        progressBar = new JProgressBar();
        progressBar.setBounds(280, 220,200, 25);
        progressBar.setBorderPainted(false);

        Icon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("icons/loading_gear.gif")));
        JLabel loadindgGear = new JLabel(icon);
        loadindgGear.setBounds(300, 10, 200, 200);


        JLabel lbl = new JLabel();
        lbl.setSize(width, height);
        lbl.setIcon(functions.ResizeImage(lbl,"icons/Splashscreen barner.png", null));
        lbl.add(loadindgGear);

        percentage = new JLabel("0%");
        percentage.setForeground(Color.decode(secondary_color));
        percentage.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        percentage.setBounds(280, 250, 200, 40);
        percentage.setHorizontalAlignment(JLabel.CENTER);


        /*//title text
        JLabel titleText = new JLabel("BENUE STATE SENIOR CITIZENS CENTER");
        titleText.setForeground(Color.white);
        titleText.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        titleText.setBounds(0, 20, width, 40);
        titleText.setHorizontalAlignment(JLabel.CENTER);*/

        JPanel homePanel = new JPanel();
        homePanel.setSize(width, height);
        homePanel.setBackground(Color.decode(primary_color));
        homePanel.setLayout(null);
        lbl.add(progressBar);
        lbl.add(percentage);
        homePanel.add(lbl);
        add(homePanel);
    }

    //Center the frame
    private void centerFrameOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }
}

