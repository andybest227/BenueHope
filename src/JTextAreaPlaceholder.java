import javax.swing.*;
import java.awt.*;
public class JTextAreaPlaceholder extends JTextArea{
    private String placeholder = "Placeholder";

    @Override
    public void paint(Graphics g){
        super.paint(g);
        if(getText().length() == 0){
            int h = getHeight();
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            int bg = getBackground().getRGB();
            int fg = getForeground().getRGB();
            int m = 0xfefefefe;
            int c = ((bg&m) >>>1)+ ((fg&m) >>>1);
            g.setColor(new Color(c));
            g.drawString(getPlaceholder(), ins.left, h/2+fm.getAscent()/2-2);
        }
    }

    public String getPlaceholder(){
        return placeholder;
    }

    public void setPlaceholder (String placeholder){
        this.placeholder = placeholder;
    }
}
