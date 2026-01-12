package ImageEditor.Adjustments.Implem;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Core.Utils;

import java.awt.image.BufferedImage;

public class ContrastAdjustment extends AdjustmentStrategy {
    //integer zmiany przyjmuje wartości od -100 do 100 jako procent zmiany
    //dodaj więc skalowanie w sumacyjnych funkcjach


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

        // ten wzor dziala dla value w zakresie [-255, 255], dlatego newValue [-100, 100]*2.55
        double newValue = value;
        double factor = 259.0*(newValue + 255)/(255.0*(259 - newValue));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                r = (int)(factor*(r - Utils.MID_COLOR_VAL) + Utils.MID_COLOR_VAL);
                g = (int)(factor*(g - Utils.MID_COLOR_VAL) + Utils.MID_COLOR_VAL);
                b = (int)(factor*(b - Utils.MID_COLOR_VAL) + Utils.MID_COLOR_VAL);

                r = Utils.clamp(r);
                g = Utils.clamp(g);
                b = Utils.clamp(b);

                int out = (a << 24) | (r << 16) | (g << 8) | b;

                image.setRGB(x, y, out);
            }
        }
    }
}
