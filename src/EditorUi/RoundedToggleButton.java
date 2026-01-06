package EditorUi;

import java.awt.*;
import javax.swing.*;

public class RoundedToggleButton extends JToggleButton {
    private final int arc = 12;

    public RoundedToggleButton(String text) {
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
        if (getModel().isPressed() || isSelected()) bg = bg.darker();
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // Border
        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(2f));
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
