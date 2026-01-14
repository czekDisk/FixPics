package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

public class BinarizationFilter extends FilterStrategy {

    /**
     * funkcja nakłada na image filtr
     *
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        long sum = 0;


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                int jasnosc = (r + g + b) / 3;
                sum += jasnosc;
            }
        }

        int prog = (int) (sum / (width * height));


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                int jasnosc = (r + g + b) / 3;

                int outR, outG, outB;
                if (jasnosc > prog) {
                    outR = outG = outB = 255;
                } else {
                    outR = outG = outB = 0;
                }

                int out = (a << 24) | (outR << 16) | (outG << 8) | outB;
                image.setRGB(x, y, out);
            }
        }
    }
}