package EditorUi;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class RoundedButton extends JButton {
    private final int arc = 12;

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color bg = getBackground();
        if (getModel().isPressed()) bg = bg.darker();
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // Border - smoother and paler
        Color borderColor = new Color(120, 120, 130); // Pale bluish gray
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public Insets getInsets() {
        Insets i = super.getInsets();
        return new Insets(Math.max(4, i.top), Math.max(8, i.left), Math.max(4, i.bottom), Math.max(8, i.right));
    }
}
