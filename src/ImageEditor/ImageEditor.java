package ImageEditor;

import ImageEditor.Adjustments.AdjustmentStrategy;
import ImageEditor.Adjustments.AdjustmentType;
import ImageEditor.Adjustments.Implem.*;
import ImageEditor.Core.HistoryManager;
import ImageEditor.Core.IOManager;
import ImageEditor.Core.ImageRenderer;
import ImageEditor.Filters.FilterStrategy;
import ImageEditor.Filters.FilterType;
import ImageEditor.Filters.Implem.*;
import ImageEditor.Core.Utils;

import java.awt.image.BufferedImage;
import java.util.EnumMap;


public class ImageEditor {
    private final BufferedImage originalImage;
    private ImageState state;

    private final EnumMap<FilterType, FilterStrategy> filterStrategies;
    private final EnumMap<AdjustmentType, AdjustmentStrategy> adjustmentStrategies;

    private HistoryManager history;
    private IOManager io;
    private ImageRenderer imageRenderer;


    public ImageEditor(String filepath) {
        io = new IOManager(filepath);

        originalImage = io.getImage();
        previewImage = Utils.scaleDown(originalImage, 60);

        adjustmentStrategies = new EnumMap<>(AdjustmentType.class);
        for(AdjustmentType type : AdjustmentType.values())
            adjustmentStrategies.put(type, Utils.adjustment_factory(type));

        filterStrategies = new EnumMap<>(FilterType.class);
        for(FilterType type : FilterType.values())
            filterStrategies.put(type, Utils.filter_factory(type));

        this.history = new HistoryManager();
        this.state = new  ImageState();
        this.imageRenderer = new ImageRenderer();
    }

    public void applyAdjustment(AdjustmentType type, int value){
        state.getAdjustmentState().put(type, value);
        history.update();
    }

    public void applyFilter(FilterType type){
        state.setFilter(type);
        history.update();
    }


}
