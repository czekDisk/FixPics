package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

public class InvertFilter extends FilterStrategy {
    /**
     * funkcja nakłada na image filtr
     *
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);

                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                int outR = 255 - r;
                int outG = 255 - g;
                int outB = 255 - b;

                int out = (a << 24) | (outR << 16) | (outG << 8) | (outB);

                image.setRGB(x, y, out);
            }
        }
    }
}

