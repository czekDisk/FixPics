package ImageEditor.Adjustments.Implem;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Core.Utils;

import java.awt.image.BufferedImage;

public class IlluminationAdjustment extends AdjustmentStrategy {
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

        double scale = (value/300f);

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int argb = image.getRGB(x,y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                double gray = Utils.calc_luminance(r, g, b);
//                double gray = 0.2126*r + 0.7152*g + 0.0722*b;
                double shadowMask = 1f - gray/255.0;

                shadowMask *= shadowMask;  // smoother mask

                r += (int)((255-r)*scale*shadowMask);
                g += (int)((255-g)*scale*shadowMask);
                b += (int)((255-b)*scale*shadowMask);

                r = Utils.clamp(r);
                g = Utils.clamp(g);
                b = Utils.clamp(b);

                int out = (a << 24) | (r << 16) | (g << 8) | b;

                image.setRGB(x, y, out);
            }
        }
    }

    // zamiana 0-255 sRGB na 0.0-1.0 linear RGB
    private static double srgbToLinear(int c) {
        double v = c / 255.0;
        if (v <= 0.04045)
            return v / 12.92;
        else
            return Math.pow((v + 0.055) / 1.055, 2.4);
    }

    // zamiana 0.0-1.0 linear RGB na 0-255 sRGB
    private static int linearToSrgb(double v) {
        v = Math.max(0, Math.min(1, v)); // clamp
        if (v <= 0.0031308)
            v *= 12.92;
        else
            v = 1.055 * Math.pow(v, 1.0 / 2.4) - 0.055;
        return (int)(v * 255 + 0.5);
    }
}
