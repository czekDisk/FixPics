package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;
import java.awt.image.BufferedImage;
import ImageEditor.Core.Utils;


public class GrayscaleFilter extends FilterStrategy {
    //filtry po porstu nakładają filr na obraz

    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int argb = image.getRGB(x,y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                int gray = (int)(Utils.calc_luminance(r, g, b));
                int out = (a << 24) | (gray << 16) | (gray << 8) | gray;

                image.setRGB(x, y, out);
            }
        }
    }
}


