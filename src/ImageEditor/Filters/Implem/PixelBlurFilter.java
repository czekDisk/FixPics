package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

public class PixelBlurFilter extends FilterStrategy {
    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int BLOCK_SIZE = 5;  // TODO: PixelBlur ma roznej wielkosci bloki, a nie ma jak przekazac tego przekazac
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Jeden piksel nadpisuje caly blok, znajdujemy piksel calego bloku i go ustawiamy

                int blockRow = (y / BLOCK_SIZE) * BLOCK_SIZE;
                int blockCol = (x / BLOCK_SIZE) * BLOCK_SIZE;

                int pixel = image.getRGB(blockCol, blockRow);

                image.setRGB(x, y, pixel);
            }
        }
    }
}

