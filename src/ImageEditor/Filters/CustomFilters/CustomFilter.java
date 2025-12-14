package ImageEditor.Filters.CustomFilters;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;

public class CustomFilter extends FilterStrategy {
    //TODO filtr przechowujący ustawione wartości dla funkcji z BasicEditor
    // i nakładający funkcje o tych wartościach na obraz

    private int exposureValue = 0;
    private int brightnessValue = 0;
    private int contrastValue = 0;
    private int illuminationValue = 0;
    private int saturationValue = 0;
    private int temperatureValue = 0;
    private int hueValue = 0;
    private int sharpnessValue = 0;



    public BufferedImage applyFilter(BufferedImage img) {
        //TODO
    }

}
