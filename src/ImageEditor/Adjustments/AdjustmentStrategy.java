package ImageEditor.Adjustments;

import java.awt.image.BufferedImage;

public abstract class AdjustmentStrategy {
    public abstract void applyAdjustment(BufferedImage image, int value);
}


//DODATKOWE POMYSŁY: przycinanie, obracanie
