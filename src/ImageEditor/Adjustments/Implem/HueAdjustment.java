package ImageEditor.Adjustments.Implem;

import ImageEditor.Adjustments.AdjustmentStrategy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HueAdjustment extends AdjustmentStrategy {
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

        float scale = (value/100f);

        // Hue, Saturation, Brightness
        float [] hsb = new float[3];

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int argb = image.getRGB(x,y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                Color.RGBtoHSB(r, g, b, hsb);

                // to jest Hue, ma zakres [0, 1].
                hsb[0] = (hsb[0] + scale)%1f;

                if (hsb[0] < 0)
                    hsb[0] += 1;

                int out = (a << 24) | (Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0x00FFFFFF);

                image.setRGB(x, y, out);
            }
        }
    }
}
