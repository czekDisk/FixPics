package ImageEditor;

import Exceptions.NonExistantPresetException;
import Exceptions.PresetAlreadyExistsException;
import ImageEditor.Core.PresetManager;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.Core.HistoryManager;
import ImageEditor.Core.IOManager;
import ImageEditor.Core.ImageRenderer;
import ImageEditor.OperationTags.FilterType;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;


public class ImageEditor {
    private final BufferedImage originalImage;
    private ImageState state;
    private HistoryManager history;
    private IOManager io;
    private ImageRenderer imageRenderer;
    private PresetManager presetManager;

    public ImageEditor(String filepath) throws IOException {
        this.io = new IOManager(filepath);

        this.originalImage = io.getImage();
        this.state = new  ImageState();
        this.history = new HistoryManager(state);
        this.presetManager = new PresetManager();
        this.imageRenderer = new ImageRenderer(originalImage);

    }

    public void applyAdjustment(AdjustmentType type, int value){
        state.getAdjustmentState().put(type, value);
        history.update();
    }

    public void applyFilter(FilterType type){
            state.setFilter(type);
            history.update();
    }

    public void undo(){
        history.undo();
    }

    public boolean isUndoable(){
        return history.isUndoable();
    }

    public void redo(){
        history.redo();
    }

    public boolean isRedoable(){
        return history.isRedoable();
    }

    public void exportPreview(int scale) throws IOException {
        io.exportImage(imageRenderer.renderPreview(state, scale));
    }
    public void exportPreview(String fileFormat, String filename, int scale)throws IOException{
        io.exportImage(imageRenderer.renderPreview(state, scale), fileFormat, filename);
    }

    public void export() throws IOException{
        io.exportImage(imageRenderer.render(state));
    }
    public void export(String fileFormat, String filename) throws IOException{
        io.exportImage(imageRenderer.render(state), fileFormat, filename);
    }

    
    public BufferedImage getPreview(int scale){
        return imageRenderer.renderPreview(state, scale);
    }

    // Return a copy of the current state so callers can inspect it without mutating internal state
    public ImageState getState(){
        return new ImageState(state);
    }





    public void applyPreset(String presetName) throws NonExistantPresetException {
        if (presetManager.containsPreset(presetName)){
            throw new NonExistantPresetException("there's no such preset");
        }
        state = presetManager.getPreset(presetName);
        history.update();
    }

    public void savePreset(String presetName) throws PresetAlreadyExistsException {
        presetManager.createPreset(presetName, state);
    }

    public void removePreset(String presetName){
        presetManager.removePreset(presetName);
    }

    public Set<String> getPresetNames(){
        return presetManager.getPresetNames();
    }


}



