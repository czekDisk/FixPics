package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

public class BinarizationFilter extends FilterStrategy {
    private static int[] wyodrebnijKolorIJasnosc(int pixel) {
        int a = (pixel >> 24) & 0xFF;
        int r = (pixel >> 16) & 0xFF;
        int g = (pixel >> 8) & 0xFF;
        int b = pixel & 0xFF;
        int jasnosc = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        return new int[]{a, r, g, b, jasnosc};
    }
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
                int[] components = wyodrebnijKolorIJasnosc(image.getRGB(x, y));
                sum += components[4];
            }
        }

        int prog = (int) (sum / (width * height));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] components = wyodrebnijKolorIJasnosc(image.getRGB(x, y));

                int outR, outG, outB;
                if (components[4] > prog) {
                    outR = outG = outB = 255;
                } else {
                    outR = outG = outB = 0;
                }

                int out = (components[0] << 24) | (outR << 16) | (outG << 8) | outB;
                image.setRGB(x, y, out);
            }
        }
    }
}