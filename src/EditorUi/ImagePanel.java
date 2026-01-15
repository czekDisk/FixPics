package EditorUi;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

class ImagePanel extends JPanel {

    private BufferedImage image;
    private String placeholder = "No image loaded";

    public ImagePanel() {
        setBackground(Color.DARK_GRAY);
    }

    public void setImage(BufferedImage img) {
        this.image = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image == null) {
            g.setColor(Color.LIGHT_GRAY);
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(placeholder)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2;
            g.drawString(placeholder, x, y);
            return;
        }

        int w = getWidth();
        int h = getHeight();

        double s = 1;
        if (w < this.image.getWidth() || h < this.image.getHeight()) {

            double sx = (double) w / image.getWidth();
            double sy = (double) h / image.getHeight();
            s = Math.min(sx, sy);

        }

        int newW = (int) (image.getWidth() * s);
        int newH = (int) (image.getHeight() * s);

        int x = (w - newW) / 2;
        int y = (h - newH) / 2;

        g.drawImage(image, x, y, newW, newH, null);
    }
}

