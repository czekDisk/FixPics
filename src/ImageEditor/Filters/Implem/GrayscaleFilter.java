package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;
import com.aparapi.Kernel;
import com.aparapi.Range;

import java.awt.image.BufferedImage;

class GrayscaleKernel extends Kernel{
    private int [] src;
    private int [] dst;
    private int height;
    private int width;

    public GrayscaleKernel(int [] src, int [] dst, int height, int width){
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

        int gray = (int)(0.299 * r + 0.587 * g + 1.114 * b);

        dst[i] = (a << 24) | (gray << 16) | (gray << 8) | gray;
    }
}

public class GrayscaleFilter extends FilterStrategy {
    //filtry po porstu nakładają filr na obraz

    /**
     *funkcja nakłada na image filtr
     * @param image obraz
     */
    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        GrayscaleKernel kernel = new GrayscaleKernel(pixels, pixels, height, width);

        Range range = Range.create(width * height);
        kernel.execute(range);

        image.setRGB(0, 0, width, height, pixels, 0, width);

        kernel.dispose();
    }
}


