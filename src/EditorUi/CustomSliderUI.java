package EditorUi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;


public class CustomSliderUI extends BasicSliderUI {
    private static final int NORMAL_DIAM = 10;
    private static final int ACTIVE_DIAM = 16;

    public CustomSliderUI(JSlider b) {
        super(b);
    }

    @Override
    protected void calculateThumbSize() {
        int s = Math.max(NORMAL_DIAM, ACTIVE_DIAM);
        thumbRect.setSize(s, s);
    }

    private static Color TRACK_COLOR = Color.BLACK;
    private static Color THUMB_COLOR = Color.BLACK;

    public static void setColors(Color trackColor, Color thumbColor) {
        TRACK_COLOR = trackColor;
        THUMB_COLOR = thumbColor;
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int cy = trackRect.y + trackRect.height / 2;
        int left = trackRect.x;
        int right = trackRect.x + trackRect.width;
        g2.setColor(TRACK_COLOR);
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(left, cy, right, cy);
        g2.dispose();
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean active = slider.getValueIsAdjusting();
        int diam = active ? ACTIVE_DIAM : NORMAL_DIAM;

        int cx = thumbRect.x + thumbRect.width / 2;
        int cy = thumbRect.y + thumbRect.height / 2;
        int x = cx - diam / 2;
        int y = cy - diam / 2;
        Shape circle = new Ellipse2D.Float(x, y, diam, diam);

        g2.setColor(THUMB_COLOR);
        g2.fill(circle);
        g2.setColor(THUMB_COLOR);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(circle);
        g2.dispose();
    }
}
