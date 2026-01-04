package ImageEditor.Adjustments.Implem;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Core.Utils;

import java.awt.image.BufferedImage;

public class ExposureAdjustment extends AdjustmentStrategy {
    //integer zmiany przyjmuje wartości od -100 do 100 jako procent zmiany
    //dodaj więc skalowanie w sumacyjnych funkcjach

    private int [] exposureLUT;

    /**
     * funkcja aplikuje korektę na image o poziomie value
     * @param image obraz
     * @param value -100 - 100%
     */
    public void applyAdjustment(BufferedImage image, int value) {
        if (value == 0)
            return;

        exposureLUT = new int[256];

        double ev = value/50.0;  // zakres value [-100, 100]/50 => zakres EV [-2, 2]

        for (int i = 0; i < 256; i++) {
            exposureLUT[i] = Utils.clamp((int)(i*Math.pow(2.0, ev)));
        }

        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                r = exposureLUT[r];
                g = exposureLUT[g];
                b = exposureLUT[b];

                int out = (a << 24) | (r << 16) | (g << 8) | b;

                image.setRGB(x, y, out);
            }
        }
    }

}
