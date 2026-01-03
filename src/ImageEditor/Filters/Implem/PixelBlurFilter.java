package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;
import com.aparapi.Kernel;
import com.aparapi.Range;

public class PixelBlurFilter extends FilterStrategy {
    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        PixelBlurKernel kernel = new PixelBlurKernel(pixels, pixels, height, width);

        Range range = Range.create(width * height);
        kernel.execute(range);

        image.setRGB(0, 0, width, height, pixels, 0, width);

        kernel.dispose();
    }
}


class PixelBlurKernel extends Kernel {
    private int [] src;
    private int [] dst;
    private int height;
    private int width;
    private int BLOCK_SIZE;  // TODO: PixelBlur ma roznej wielkosci bloki, a nie ma jak przekazac tego przekazac

    public PixelBlurKernel(int [] src, int [] dst, int height, int width){
        this.src = src;
        this.dst = dst;
        this.height = height;
        this.width = width;
        this.BLOCK_SIZE = 5;  // 10px
    }

    @Override
    public void run(){
        int i = getGlobalId();

        // Jeden piksel nadpisuje caly blok, znajdujemy piksel calego bloku i go ustawiamy

        int pixelRow = i / width;
        int pixelCol = i % width;

        int blockRow = (pixelRow / BLOCK_SIZE) * BLOCK_SIZE;
        int blockCol = (pixelCol / BLOCK_SIZE) * BLOCK_SIZE;

        int blockIdx = blockRow*width + blockCol;

        dst[i] = src[blockIdx];
    }
}
