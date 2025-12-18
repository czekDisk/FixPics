package ImageEditor.Filters;

import java.awt.image.BufferedImage;

public interface FilterStrategy {
    public abstract void applyFilter(BufferedImage image);
}
