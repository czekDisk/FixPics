package ImageEditor.Filters;

import ImageEditor.ImageState;

import java.awt.image.BufferedImage;

public abstract class FilterStrategy {

    public FilterStrategy(){}
    public FilterStrategy(ImageState state){}
    public abstract void applyFilter(BufferedImage image);
}
