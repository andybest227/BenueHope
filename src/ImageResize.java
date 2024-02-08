import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ImageResize {
    public ImageResize(){

    }
    public ImageIcon ResizeImage(JLabel label, String imagePath, byte[] pic){
        ImageIcon myImage;
        if (imagePath != null){
            myImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
        }else{
            myImage = new ImageIcon(pic);
        }
        Image img = myImage.getImage();
        Image img2 = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        return new ImageIcon(img2);
    }

    public void paint(Graphics g){
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.drawOval(150, 150, 100, 100);
    }
}

