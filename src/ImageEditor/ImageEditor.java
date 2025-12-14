package ImageEditor;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Adjustments.AdjustmentType;
import ImageEditor.Adjustments.Implem.*;
import ImageEditor.Core.HistoryManager;
import ImageEditor.Core.IOManager;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.Filters.FilterType;
import ImageEditor.Filters.Implem.*;
import modules.Utils;

import java.awt.image.BufferedImage;


public class ImageEditor {
    private final BufferedImage originalImage;
    private BufferedImage previewImage;
    private BufferedImage copiedImage;
    ImageState state;

    private final FilterStrategy[] filterStrategies;
    private FilterStrategy filter;
    private final AdjustmentStrategy[] adjustmentStrategies;
    private AdjustmentStrategy adjustment;

    private HistoryManager history;
    private IOManager io;


    public ImageEditor(String filepath) {
        io = new IOManager(filepath);

        originalImage = ;
        previewImage = Utils.scaleDown(originalImage, 60);


        // TODO: Wyglada jak cos co mozna rodzielic na metode initStrategies()
        filterStrategies = new FilterStrategy[FilterType.values().length];
        filterStrategies[FilterType.BLUR.ordinal()] = new BlurFilter();
        filterStrategies[FilterType.GRAYSCALE.ordinal()] = new GrayscaleFilter();
        filterStrategies[FilterType.INVERT.ordinal()] = new InvertFilter();
        filterStrategies[FilterType.NOISE.ordinal()] = new NoiseFilter();
        filterStrategies[FilterType.PIXELBLUR.ordinal()] = new PixelBlurFilter();
        filterStrategies[FilterType.SEPIA.ordinal()] = new SepiaFilter();

        adjustmentStrategies = new AdjustmentStrategy[AdjustmentType.values().length];
        adjustmentStrategies[AdjustmentType.BRIGHTNESS.ordinal()] = new BrightnessAdjustment();
        adjustmentStrategies[AdjustmentType.CONTRAST.ordinal()] = new ContrastAdjustment();
        adjustmentStrategies[AdjustmentType.EXPOSURE.ordinal()] = new ExposureAdjustment();
        adjustmentStrategies[AdjustmentType.HUE.ordinal()] = new HueAdjustment();
        adjustmentStrategies[AdjustmentType.ILLUMINATION.ordinal()] = new IlluminationAdjustment();
        adjustmentStrategies[AdjustmentType.SATURATION.ordinal()] = new SaturationAdjustment();
        adjustmentStrategies[AdjustmentType.SHARPNESS.ordinal()] = new SharpnessAdjustment();
        adjustmentStrategies[AdjustmentType.TEMPERATURE.ordinal()] = new TemperatureAdjustment();

        this.history = new HistoryManager();
    }


    public BufferedImage renderOriginal() {
        return render(originalImage);
    }

    public BufferedImage renderPreview() {
        return render(previewImage);
    }

    private BufferedImage render(BufferedImage image) {
        copiedImage = Utils.deepCopyImage(image);  // nie zmieniamy oryginalu

        for (int i=0; i<adjustmentStrategies.length; i++) {
            if (state.adjustmentsUsed[i] != 0)
                adjustmentStrategies[i].applyAdjustment(copiedImage, state.adjustmentsUsed[i]);
        }

        for (int i=0; i<state.filtersUsed.size(); i++) {
            // TODO: Zmienic tak zeby gdzies przechowane value dla filtra bylo uzywane
            filterStrategies[state.filtersUsed.get(i).ordinal()].applyFilter(copiedImage, 0);
        }

        return copiedImage;
    }


    public void applyFilter(FilterType type) {
        state.filtersUsed.add(type);
    }


    public void applyAdjustment(AdjustmentType type, int value){
        switch (type){
            case BRIGHTNESS:
                // TODO: EnumMap/Map<AdjustmentType, int> bedzie bardziej czytelne
                state.adjustmentsUsed[AdjustmentType.BRIGHTNESS.ordinal()] = value;
                break;
            case CONTRAST:
                state.adjustmentsUsed[AdjustmentType.CONTRAST.ordinal()] = value;
                break;
            case EXPOSURE:
                state.adjustmentsUsed[AdjustmentType.EXPOSURE.ordinal()] = value;
                break;
            case HUE:
                state.adjustmentsUsed[AdjustmentType.HUE.ordinal()] = value;
                break;
            case ILLUMINATION:
                state.adjustmentsUsed[AdjustmentType.ILLUMINATION.ordinal()] = value;
                break;
            case SATURATION:
                state.adjustmentsUsed[AdjustmentType.SATURATION.ordinal()] = value;
                break;
            case SHARPNESS:
                state.adjustmentsUsed[AdjustmentType.SHARPNESS.ordinal()] = value;
                break;
            case TEMPERATURE:
                state.adjustmentsUsed[AdjustmentType.TEMPERATURE.ordinal()] = value;
                break;
        }

//        updatePreview();
    }

    public void changeFilter(FilterType type){
        switch (type){
            case BLUR, GRAYSCALE, INVERT,
                 NOISE, PIXELBLUR, SEPIA:
                filter = filterStrategies[type.ordinal()];
                break;
            default:

        }
    }



}
