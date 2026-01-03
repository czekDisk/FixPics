package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import com.aparapi.Kernel;

import com.aparapi.Range;

import java.awt.image.BufferedImage;

public class InvertFilter extends FilterStrategy {
    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        InvertKernel kernel = new InvertKernel(pixels, pixels, height, width);

        Range range = Range.create(width * height);
        kernel.execute(range);

        image.setRGB(0, 0, width, height, pixels, 0, width);

        kernel.dispose();
    }
}


class InvertKernel extends Kernel {
    private int [] src;
    private int [] dst;
    private int height;
    private int width;

    public InvertKernel(int [] src, int [] dst, int height, int width){
        this.src = src;
        this.dst = dst;
        this.height = height;
        this.width = width;
    }

    @Override
    public void run(){
        int i = getGlobalId();

        int argb = src[i];

        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        int outR = 255 - r;
        int outG = 255 - g;
        int outB = 255 - b;

        dst[i] = (a << 24) | (outR << 16) | (outG << 8) | (outB);
    }
}

