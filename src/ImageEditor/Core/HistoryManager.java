package ImageEditor.Core;

import ImageEditor.ImageState;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class HistoryManager {
    //TODO zaimplementuj undo i redo
    // proszę zrób to za pomocą stosu

    private ArrayList<ImageState> history = new ArrayList<>(5);
    private int index;

    public void push(BufferedImage image) {

    }

    public void undo(){
        history = history.
    }

    public void redo(){

    }
}
