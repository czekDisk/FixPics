package ImageEditor.Adjustments.Implem;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Core.Utils;

import java.awt.image.BufferedImage;

public class SharpnessAdjustment extends AdjustmentStrategy {
    //integer zmiany przyjmuje wartości od 0 do 100 jako procent zmiany
    //dodaj więc skalowanie w sumacyjnych funkcjach

    private int clamp(int v) {
        if (v < 0) return 0;
        if (v > 255) return 255;
        return v;
    }

    private int mirror(int x, int max) {
        if (x < 0) return -x - 1;
        if (x >= max) return 2 * max - x - 1;
        return x;
    }

    /**
     * funkcja aplikuje korektę na image o poziomie value
     * @param image obraz
     * @param value 0 - 100%
     */
    public void applyAdjustment(BufferedImage image, int value) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage copy = Utils.deepCopyImage(image);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int center = copy.getRGB(x, y);

                int a = (center >>> 24) & 0xFF;
                int r = 0, g = 0, b = 0;

                // Środek
                r += 5 * ((center >>> 16) & 0xFF);
                g += 5 * ((center >>> 8)  & 0xFF);
                b +=      (center         & 0xFF);

                // współrzędne mirror
                int xL = mirror(x - 1, width);
                int xR = mirror(x + 1, width);
                int yU = mirror(y - 1, height);
                int yD = mirror(y + 1, height);

                // góra
                int p = copy.getRGB(x, yU);
                r -= (p >>> 16) & 0xFF;
                g -= (p >>> 8) & 0xFF;
                b -= p & 0xFF;

                // dół
                p = copy.getRGB(x, yD);
                r -= (p >>> 16) & 0xFF;
                g -= (p >>> 8) & 0xFF;
                b -= p & 0xFF;

                // lewo
                p = copy.getRGB(xL, y);
                r -= (p >>> 16) & 0xFF;
                g -= (p >>> 8) & 0xFF;
                b -= p & 0xFF;

                // prawo
                p = copy.getRGB(xR, y);
                r -= (p >>> 16) & 0xFF;
                g -= (p >>> 8) & 0xFF;
                b -= p & 0xFF;

                r = clamp(r);
                g = clamp(g);
                b = clamp(b);

                int inR = (center >>> 16) & 0xFF;
                int inG = (center >>> 8) & 0xFF;
                int inB = center & 0xFF;

                // skalowanie wg value
                r = inR + (r - inR) * value / 100;
                g = inG + (g - inG) * value / 100;
                b = inB + (b - inB) * value / 100;

                r = clamp(r);
                g = clamp(g);
                b = clamp(b);

                int out = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, out);
            }
        }
    }
}












