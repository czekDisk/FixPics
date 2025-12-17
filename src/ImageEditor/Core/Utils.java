package ImageEditor.Core;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Adjustments.AdjustmentType;
import ImageEditor.Adjustments.Implem.*;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.Filters.FilterType;
import ImageEditor.Filters.Implem.*;

import java.awt.image.BufferedImage;

public class Utils {

    //może dodaj dynamiczne skalowanie w zależności od wymiarów obrazu
    static public BufferedImage scaleDown(BufferedImage image, int value) {
        //TODO or use from std lib if too hard
    }

    static public BufferedImage deepCopyImage(BufferedImage image) {
        // TODO: Sprawdzic czy nie da sie optymalniej zrobic kopii
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int x=0; x<image.getWidth(); x++) {
            for (int y=0; y<image.getHeight(); y++) {
                copy.setRGB(x, y, image.getRGB(x, y));
            }
        }

        return copy;
    }


    static public FilterStrategy filter_factory(FilterType type){
        return switch (type) {
            case NOFILTER -> null;//?
            case BLUR -> new BlurFilter();
            case GRAYSCALE -> new GrayscaleFilter();
            case INVERT -> new InvertFilter();
            case NOISE -> new NoiseFilter();
            case PIXELBLUR -> new PixelBlurFilter();
            case SEPIA -> new SepiaFilter();
        };
    }

    static public AdjustmentStrategy adjustment_factory(AdjustmentType type){
        return switch (type){
            case  BRIGHTNESS -> new BrightnessAdjustment();
            case CONTRAST -> new ContrastAdjustment();
            case EXPOSURE -> new ExposureAdjustment();
            case HUE -> new HueAdjustment();
            case ILLUMINATION -> new IlluminationAdjustment();
            case SATURATION -> new SaturationAdjustment();
            case SHARPNESS -> new SharpnessAdjustment();
            case TEMPERATURE -> new TemperatureAdjustment();
        };
    }



}
