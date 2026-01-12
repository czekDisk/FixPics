package ImageEditor.Adjustments.Implem;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Core.Utils;

import java.awt.image.BufferedImage;

public class SharpnessAdjustment extends AdjustmentStrategy {

    /**
     * funkcja aplikuje korektę na image o poziomie value
     * @param image obraz
     * @param value -100 - 100%
     */
    public void applyAdjustment(BufferedImage image, int value) {
        if (value == 0)
            return;

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage copy = Utils.deepCopyImage(image);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int center = copy.getRGB(x, y);

                int a = (center >>> 24) & 0xFF;
                int r = 0, g = 0, b = 0;

                r += 5 * ((center >>> 16) & 0xFF);
                g += 5 * ((center >>> 8)  & 0xFF);
                b += 5 * ((center)        & 0xFF);

                int xL = Utils.mirror(x - 1, width);
                int xR = Utils.mirror(x + 1, width);
                int yU = Utils.mirror(y - 1, height);
                int yD = Utils.mirror(y + 1, height);

                int p = copy.getRGB(x, yU);
                r -= (p >>> 16) & 0xFF;
                g -= (p >>> 8) & 0xFF;
                b -= p & 0xFF;

                p = copy.getRGB(x, yD);
                r -= (p >>> 16) & 0xFF;
                g -= (p >>> 8) & 0xFF;
                b -= p & 0xFF;

                p = copy.getRGB(xL, y);
                r -= (p >>> 16) & 0xFF;
                g -= (p >>> 8) & 0xFF;
                b -= p & 0xFF;

                p = copy.getRGB(xR, y);
                r -= (p >>> 16) & 0xFF;
                g -= (p >>> 8) & 0xFF;
                b -= p & 0xFF;

                r = Utils.clamp(r);
                g = Utils.clamp(g);
                b = Utils.clamp(b);

                int inR = (center >>> 16) & 0xFF;
                int inG = (center >>> 8) & 0xFF;
                int inB = center & 0xFF;

                r = inR + (r - inR) * value / 100;
                g = inG + (g - inG) * value / 100;
                b = inB + (b - inB) * value / 100;

                r = Utils.clamp(r);
                g = Utils.clamp(g);
                b = Utils.clamp(b);

                int out = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, out);
            }
        }
    }
}












