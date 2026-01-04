package ImageEditor.Core;

import ImageEditor.ImageState;

import java.awt.*;
import java.util.Stack;


public class HistoryManager {
    private ImageState currState;
    private Stack<ImageState> undo;
    private Stack<ImageState> redo;


    public HistoryManager(ImageState state){
        this.currState = state;
        this.redo = new Stack<>();
        this.undo = new Stack<>();
    }


    public void update(){
        redo.clear();

        undo.push(new ImageState(currState));
    }

    public boolean isUndoable(){
        return !undo.empty();
    }

    public boolean isRedoable(){
        return !redo.empty();
    }

    public void undo(){
        if (!isUndoable()) return;
        redo.push(undo.pop());
        if (!undo.empty()){
            currState.copyFrom(undo.peek());
        }
        else{
            currState.clear();
        }

    }

    public void redo(){
        if (!isRedoable()) return;
        undo.push(redo.pop());
        currState.copyFrom(undo.peek());
    }

}


