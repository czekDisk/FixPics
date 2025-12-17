package ImageEditor.Filters;

import java.awt.image.BufferedImage;

public abstract class FilterStrategy {
    public abstract void applyFilter(BufferedImage image);
}
