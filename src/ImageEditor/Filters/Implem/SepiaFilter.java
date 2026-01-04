package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

public class SepiaFilter extends FilterStrategy {

    /**
     *funkcja nakłada na image filtr
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

                int outR = (int)(0.393 * r + 0.769 * g + 0.189 * b);
                outR = Math.min(outR, 255);

                int outG = (int)(0.349 * r + 0.686 * g + 0.168 * b);
                outG = Math.min(outG, 255);

                int outB = (int)(0.272 * r + 0.534 * g + 0.131 * b);
                outB = Math.min(outB, 255);

                int out = (a << 24) | (outR << 16) | (outG << 8) | outB;
                image.setRGB(x, y, out);
            }
        }
    }
}