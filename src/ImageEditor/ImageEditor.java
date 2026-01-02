package ImageEditor;

import ImageEditor.Filters.CustomFilters.CustomFilterManager;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.Core.HistoryManager;
import ImageEditor.Core.IOManager;
import ImageEditor.Core.ImageRenderer;
import ImageEditor.OperationTags.CustomFilterIndex;
import ImageEditor.OperationTags.FilterPresets;
import ImageEditor.OperationTags.FilterType;

import java.awt.image.BufferedImage;
import java.io.IOException;


public class ImageEditor {
    private final BufferedImage originalImage;
    private ImageState state;
    private HistoryManager history;
    private IOManager io;
    private ImageRenderer imageRenderer;
    private CustomFilterManager customFilterManager;


    public ImageEditor(String filepath) throws IOException, IllegalArgumentException {
        this.io = new IOManager(filepath);

        this.originalImage = io.getImage();
        this.state = new  ImageState();
        this.history = new HistoryManager(state);
        this.customFilterManager = new CustomFilterManager();
        this.imageRenderer = new ImageRenderer(originalImage, customFilterManager);

    }

    public void applyAdjustment(AdjustmentType type, int value){
        state.getAdjustmentState().put(type, value);
        history.update();
    }

    public int applyFilter(FilterType type){
        if (type.getClass() == FilterPresets.class){
            state.setFilter(type);
            history.update();
            return 0;
        }
        else {
            if (customFilterManager.getCustomFilters().get(type) == null){
                return 1;
            }
            else{
                state.setFilter(type);
                history.update();
                return 0;
            }
        }
    }

    public void undo(){
        history.undo();
    }

    public void redo(){
        history.redo();
    }

    public void changeCustomFilter(CustomFilterIndex index){
        customFilterManager.changeFilter(state, index);
    }


    public void export()throws IOException{
        io.exportImage(imageRenderer.render(state));
    }

    public void export(String fileFormat, String filename)throws IOException{
        io.exportImage(imageRenderer.render(state), fileFormat, filename);
    }


}
