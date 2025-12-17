package ImageEditor;

import ImageEditor.Adjustments.AdjustmentType;
import ImageEditor.Adjustments.Implem.*;
import ImageEditor.Core.HistoryManager;
import ImageEditor.Core.IOManager;
import ImageEditor.Core.ImageRenderer;
import ImageEditor.Filters.FilterType;
import ImageEditor.Filters.Implem.*;

import java.awt.image.BufferedImage;
import java.io.IOException;


public class ImageEditor {
    private final BufferedImage originalImage;
    private ImageState state;
    private HistoryManager history;
    private IOManager io;
    private ImageRenderer imageRenderer;


    public ImageEditor(String filepath) throws IOException, IllegalArgumentException {
        this.io = new IOManager(filepath);

        this.originalImage = io.getImage();

        this.history = new HistoryManager();
        this.state = new  ImageState();
        this.imageRenderer = new ImageRenderer(originalImage);
    }

    public void applyAdjustment(AdjustmentType type, int value){
        state.getAdjustmentState().put(type, value);
//        history.update();
    }

    public void applyFilter(FilterType type){
        state.setFilter(type);
//        history.update();
    }

    public void export(String fileFormat, String filename) throws IOException{
        io.exportImage(imageRenderer.render(state), fileFormat, filename);
    }


}
