package ImageEditor.Filters.Implem;

import ImageEditor.Core.Utils;
import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

public class EdgeDetectionFilter extends FilterStrategy {

    private final int[][] SOBEL_X_KERNEL = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    private final int[][] SOBEL_Y_KERNEL = {
            {-1, -2, -1},
            { 0,  0,  0},
            { 1,  2,  1}
    };

    /**
     *funkcja nakłada na image filtr wykrywania krawedzi (metoda Sobela) + odcinanie ponizej sredniej
     * @param image obraz
     */

    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // dla kazdego piksela przechowujemy jego magnitude
        int[][] magnitudes = new int[height][width];
        long magnitudesSum = 0;

        for (int y = 1; y < height-1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int Gx = 0;
                int Gy = 0;

                // Wartosci Gx i Gy dla maski pionowej i poziomej
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        int pixel = image.getRGB(x + i, y + j);

                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;

                        int gray = (int) (Utils.calc_luminance(r, g, b));

                        Gx += gray * SOBEL_X_KERNEL[j + 1][i + 1];
                        Gy += gray * SOBEL_Y_KERNEL[j + 1][i + 1];
                    }
                }

                int curMagnitude = (int)(Math.sqrt(Gx * Gx + Gy * Gy));
                magnitudes[y][x] = Utils.clamp(curMagnitude);

                magnitudesSum += curMagnitude;
            }
        }

        float averageMagnitude = ((float)magnitudesSum)/(height*width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int magnitude = magnitudes[y][x];
                int alpha = image.getRGB(x, y) & 0xFF000000;
                int pixel = alpha | (magnitude << 16) | (magnitude << 8) | magnitude;

                if (magnitude < averageMagnitude) {
                    pixel = alpha;
                }

                image.setRGB(x, y, pixel);
            }
        }

    }
}