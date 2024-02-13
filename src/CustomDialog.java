import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class CustomDialog extends JDialog {
    public CustomDialog(String text, Color border_color){
        setUndecorated(true);
        setModal(false);
        setBackground(Color.white);
        JLabel loading_lbl = new JLabel();
        setContentPane(loading_lbl);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(new Dimension(250, 50));
        setLocationRelativeTo(null);
        //Format loading dialog
        LineBorder lineBorder = new LineBorder(border_color, 2, true);
        loading_lbl.setSize(250, 45);
        loading_lbl.setText(text);
        loading_lbl.setBackground(Color.white);
        loading_lbl.setBorder(lineBorder);
        String primary_color = "#043e45";
        loading_lbl.setForeground(Color.decode(primary_color));
        loading_lbl.setFont(new Font("Bahnschrift", Font.BOLD, 20));
        loading_lbl.setHorizontalAlignment(JLabel.CENTER);
    }
}
