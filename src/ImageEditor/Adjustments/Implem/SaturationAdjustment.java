package ImageEditor.Adjustments.Implem;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Core.Utils;

import java.awt.image.BufferedImage;

public class SaturationAdjustment extends AdjustmentStrategy {
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

        double scale = ((value+100)/100f);

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int argb = image.getRGB(x,y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                double gray = Utils.calc_luminance(r, g, b);

                r = (int)(gray + (r-gray)*scale);
                g = (int)(gray + (g-gray)*scale);
                b = (int)(gray + (b-gray)*scale);

                r = Utils.clamp(r);
                g = Utils.clamp(g);
                b = Utils.clamp(b);

                int out = (a << 24) | (r << 16) | (g << 8) | b;

                image.setRGB(x, y, out);
            }
        }
    }
}
