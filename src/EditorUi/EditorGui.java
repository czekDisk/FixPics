package EditorUi;

import Exceptions.NonExistentPresetException;
import ImageEditor.Core.IOManager;
import ImageEditor.ImageEditor;
import ImageEditor.ImageState;
import ImageEditor.OperationTags.AdjustmentType;
import ImageEditor.OperationTags.FilterType;
import Exceptions.PresetAlreadyExistsException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;

public class EditorGui extends JFrame {
    private ImageEditor editor;
    private ImagePanel imageLabel;
    private EnumMap<AdjustmentType, JSlider> sliders;
    private EnumMap<AdjustmentType, JLabel> valueLabels;
    private JComboBox<FilterType> filterBox;
    private JButton openBtn;
    private JButton exportBtn;
    private JButton undoBtn;
    private JButton redoBtn;
    private int renderScale = 50;
    private SwingWorker<BufferedImage, Void> currentWorker;

    private JPanel mainPanel;
    private JPanel toolbar;
    private JPanel fileButtonsPanel;
    private JPanel toolbarLeft;
    private JPanel toolbarCenter;
    private JPanel toolbarRight;
    private JPanel left;
    private JPanel slidersPanel;
    private JPanel filterPanel;
    private JLabel filterLabel;
    private JToggleButton darkToggle;
    private boolean darkTheme = false;

    private JComboBox<String> presetBox;
    private JButton presetApplyBtn;
    private JButton presetSaveBtn;
    private JButton presetDeleteBtn;

    public EditorGui() {
        sliders = new EnumMap<>(AdjustmentType.class);
        valueLabels = new EnumMap<>(AdjustmentType.class);
        imageLabel = new ImagePanel();
        filterBox = new JComboBox<>(FilterType.values());
        filterBox.setPreferredSize(new Dimension(300, 40));
        filterBox.setBackground(Color.WHITE);
        filterBox.setForeground(Color.BLACK);
        undoBtn = new RoundedButton("Undo");
        redoBtn = new RoundedButton("Redo");
        presetBox = new JComboBox<>();
        presetApplyBtn = new RoundedButton("Apply");
        presetSaveBtn = new RoundedButton("Save");
        presetDeleteBtn = new RoundedButton("Delete");
        initUI();
    }

    private void initUI() {
        setTitle("FixPics");
        setSize(1000, 700);
        setMinimumSize(new Dimension(750, 525));
        try {
            setIconImage(ImageIO.read(new File("icon.png")));
        } catch (IOException e) {
            // Nic, pozostaje standardowa ikonka
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(imageLabel, BorderLayout.CENTER);

        toolbar = new JPanel(new BorderLayout());
        toolbarLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbarCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbarRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));

        openBtn = new RoundedButton("Open");
        exportBtn = new RoundedButton("Export");

        openBtn.addActionListener(e -> openFile());
        exportBtn.addActionListener(e -> exportFile());

        styleToolbarButton(openBtn);
        styleToolbarButton(exportBtn);
        styleToolbarButton(undoBtn);
        styleToolbarButton(redoBtn);

        fileButtonsPanel = new JPanel(new GridLayout(2, 2, 8, 6));
        fileButtonsPanel.setOpaque(true);
        fileButtonsPanel.add(openBtn);
        fileButtonsPanel.add(exportBtn);
        fileButtonsPanel.add(undoBtn);
        fileButtonsPanel.add(redoBtn);

        toolbarLeft.add(fileButtonsPanel);

        toolbarCenter.add(presetBox);
        toolbarCenter.add(presetApplyBtn);
        toolbarCenter.add(presetSaveBtn);
        toolbarCenter.add(presetDeleteBtn);

        darkToggle = new RoundedToggleButton("Dark");
        darkToggle.setFocusPainted(false);
        styleToolbarButton(darkToggle);
        darkToggle.addActionListener(e -> applyTheme(darkToggle.isSelected()));
        toolbarRight.add(darkToggle);

        toolbar.add(toolbarLeft, BorderLayout.WEST);
        toolbar.add(toolbarCenter, BorderLayout.CENTER);
        toolbar.add(toolbarRight, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout());
        top.add(toolbar, BorderLayout.CENTER);

        left = new JPanel(new BorderLayout());

        slidersPanel = new JPanel();
        slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.Y_AXIS));

        int LABEL_WIDTH = 120;
        int SLIDER_WIDTH = 200;
        for (AdjustmentType type : AdjustmentType.values()) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(Color.WHITE);

            JLabel l = new JLabel(type.name());
            l.setPreferredSize(new Dimension(LABEL_WIDTH, l.getPreferredSize().height));
            l.setForeground(Color.BLACK);

            JSlider s = new JSlider(-100, 100, 0);
            s.setUI(new CustomSliderUI(s));
            s.setFocusable(false);
            s.setMajorTickSpacing(100);
            s.setMinorTickSpacing(25);
            s.setPaintTicks(true);
            s.setPaintLabels(true);
            s.setPreferredSize(new Dimension(SLIDER_WIDTH, s.getPreferredSize().height));
            s.addChangeListener(new AdjustmentChangeListener(type));
            sliders.put(type, s);

            JLabel valueLabel = new JLabel("0");
            valueLabel.setPreferredSize(new Dimension(35, valueLabel.getPreferredSize().height));
            valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
            valueLabel.setForeground(Color.BLACK);
            valueLabel.setVisible(true);
            valueLabels.put(type, valueLabel);

            p.add(valueLabel, BorderLayout.CENTER);
            p.add(l, BorderLayout.WEST);
            p.add(s, BorderLayout.EAST);
            p.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
            slidersPanel.add(p);
        }


        filterPanel = new JPanel(new BorderLayout());
        filterLabel = new JLabel("Filter: ");
        filterPanel.add(filterLabel, BorderLayout.WEST);
        filterPanel.add(filterBox, BorderLayout.CENTER);

        JPanel leftCenter = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane(slidersPanel);
        leftCenter.add(scroll, BorderLayout.CENTER);
        leftCenter.add(filterPanel, BorderLayout.SOUTH);

        left.add(leftCenter, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        undoBtn.addActionListener(e -> doUndo());
        redoBtn.addActionListener(e -> doRedo());
        filterBox.addActionListener(e -> applySelectedFilter());

        presetApplyBtn.addActionListener(e -> applySelectedPreset());
        presetSaveBtn.addActionListener(e -> savePreset());
        presetDeleteBtn.addActionListener(e -> deletePreset());



        setControlsEnabled(false);
        refreshPresetBox();
        applyTheme(darkTheme);
        setVisible(true);
    }

    private void setControlsEnabled(boolean enabled) {
        undoBtn.setEnabled(enabled);
        redoBtn.setEnabled(enabled);
        filterBox.setEnabled(enabled);
        for (JSlider s : sliders.values()) s.setEnabled(enabled);

        presetBox.setEnabled(enabled);
        presetApplyBtn.setEnabled(enabled);
        presetSaveBtn.setEnabled(enabled);
        presetDeleteBtn.setEnabled(enabled);
    }

    private void styleToolbarButton(AbstractButton b) {
        Color bg = darkTheme ? new Color(45, 45, 45) : Color.WHITE;
        Color fg = darkTheme ? Color.WHITE : Color.BLACK;
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        if (b instanceof RoundedButton || b instanceof RoundedToggleButton) {
            b.setContentAreaFilled(false);
            b.setOpaque(false);
            b.setBorderPainted(false);
        } else {
            b.setContentAreaFilled(true);
            b.setOpaque(true);
            b.setBorder(BorderFactory.createLineBorder(darkTheme ? new Color(50, 53, 67) : new Color(200, 202, 207), 1));
        }
        b.setPreferredSize(new Dimension(100, 28));
    }

    private void styleToolbarCombo(JComboBox<?> box) {
        Color bg = darkTheme ? new Color(40, 43, 55) : new Color(245, 247, 252);
        Color fg = darkTheme ? Color.WHITE : Color.BLACK;
        box.setBackground(bg);
        box.setForeground(fg);
        box.setPreferredSize(new Dimension(160, 28));
        box.setFocusable(false);
        box.setBorder(BorderFactory.createLineBorder(darkTheme ? new Color(50, 53, 67) : new Color(200, 202, 207), 1));
    }

    private void applyTheme(boolean dark) {
        darkTheme = dark;
        Color bg = darkTheme ? new Color(15, 18, 28) : new Color(248, 250, 255);
        Color panelBg = darkTheme ? new Color(25, 28, 40) : new Color(250, 252, 255);
        Color buttonBg = darkTheme ? new Color(40, 43, 55) : new Color(245, 247, 252);
        Color fg = darkTheme ? Color.WHITE : Color.BLACK;
        getContentPane().setBackground(bg);
        if (mainPanel != null) mainPanel.setBackground(bg);
        darkToggle.setText(darkTheme ? "Light" : "Dark");
        imageLabel.setOpaque(true);
        imageLabel.setBackground(bg);
        imageLabel.setForeground(fg);

        if (toolbar != null) toolbar.setBackground(panelBg);
        if (toolbarLeft != null) toolbarLeft.setBackground(panelBg);
        if (toolbarCenter != null) toolbarCenter.setBackground(panelBg);
        if (toolbarRight != null) toolbarRight.setBackground(panelBg);
        if (fileButtonsPanel != null) fileButtonsPanel.setBackground(panelBg);
        if (left != null) left.setBackground(panelBg);
        if (slidersPanel != null) slidersPanel.setBackground(panelBg);
        if (filterPanel != null) {
            filterPanel.setBackground(panelBg);
            filterPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        }
        if (filterLabel != null) filterLabel.setForeground(fg);
        if (filterBox != null) {
            filterBox.setBackground(buttonBg);
            filterBox.setForeground(fg);
            filterBox.setBorder(BorderFactory.createLineBorder(darkTheme ? new Color(50, 53, 67) : new Color(200, 202, 207), 1));
        }
        if (presetBox != null) styleToolbarCombo(presetBox);
        if (toolbar != null) {
            for (Component c : toolbar.getComponents()) {
                if (c instanceof AbstractButton) {
                    AbstractButton b = (AbstractButton) c;
                    b.setBackground(buttonBg);
                    b.setForeground(fg);
                    if (!(b instanceof RoundedButton || b instanceof RoundedToggleButton)) {
                        b.setOpaque(true);
                        b.setBorder(BorderFactory.createLineBorder(darkTheme ? new Color(50, 53, 67) : new Color(200, 202, 207), 1));
                    }
                }
            }
        }
        styleToolbarButton(openBtn);
        styleToolbarButton(exportBtn);
        styleToolbarButton(undoBtn);
        styleToolbarButton(redoBtn);
        styleToolbarButton(darkToggle);
        styleToolbarButton(presetApplyBtn);
        styleToolbarButton(presetSaveBtn);
        styleToolbarButton(presetDeleteBtn);

        if (slidersPanel != null) {
            for (Component comp : slidersPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel p = (JPanel) comp;
                    p.setBackground(panelBg);
                    for (Component c : p.getComponents()) {
                        if (c instanceof JLabel) {
                            ((JLabel) c).setForeground(fg);
                            ((JLabel) c).setBackground(panelBg);
                        }
                    }
                }
            }
        }
        for (JSlider s : sliders.values()) {
            s.setBackground(panelBg);
            s.setForeground(fg);
            s.setOpaque(true);
            s.setBorder(null);
            s.repaint();
        }
        CustomSliderUI.setColors(fg, darkTheme ? new Color(75, 80, 100) : new Color(180, 185, 195));
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            try {
                editor = new ImageEditor(path);
                scheduleRender();
                setControlsEnabled(true);
                refreshPresetBox();
                refreshControls();
            } catch (IOException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Cannot open image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void scheduleRender() {

        if (currentWorker != null && !currentWorker.isDone()) {
            currentWorker.cancel(true);
        }

        currentWorker = new SwingWorker<BufferedImage, Void>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                return editor.getPreview(EditorGui.this.renderScale);
            }

            @Override
            protected void done() {
                if (isCancelled()) return;
                try {
                    BufferedImage img = get();

                    imageLabel.setImage(img);
                } catch (Exception ex) {
                    //
                }
            }
        };
        currentWorker.execute();
    }

    // Flag used to avoid reacting to programmatic control updates
    private boolean programmaticUpdate = false;

    private void doUndo() {
        if (editor == null) return;
        editor.undo();
        scheduleRender();
        refreshControls();
    }

    private void doRedo() {
        if (editor == null) return;
        editor.redo();
        scheduleRender();
        refreshControls();
    }

    private void applySelectedFilter() {
        if (programmaticUpdate) return; // ignore programmatic selections
        if (editor == null) return;
        FilterType preset = (FilterType) filterBox.getSelectedItem();
        if (preset == null) return;
        editor.applyFilter(preset);
        scheduleRender();
        refreshControls();
    }

    // Sync UI controls with editor state (doesn't trigger change listeners)
    private void refreshControls(){
        if (editor == null) return;
        // Update undo/redo availability
        undoBtn.setEnabled(editor.isUndoable());
        redoBtn.setEnabled(editor.isRedoable());

        programmaticUpdate = true;
        // Update sliders
        ImageState state = editor.getState();
        for (AdjustmentType type : AdjustmentType.values()){
            JSlider s = sliders.get(type);
            if (s != null){
                Integer val = state.getAdjustmentState().get(type);
                if (val != null) {
                    s.setValue(val);
                    valueLabels.get(type).setText(String.valueOf(val));
                } else {
                    valueLabels.get(type).setText("0");
                }
            }
        }

        // Update filter combobox; if non-preset filter is active, show NOFILTER
        if (state.getFilter() instanceof FilterType){
            filterBox.setSelectedItem((FilterType) state.getFilter());
        } else {
            filterBox.setSelectedItem(FilterType.NOFILTER);
        }

        programmaticUpdate = false;

        refreshPresetBox();
    }

    private void refreshPresetBox() {
        Object selectedBefore = presetBox.getSelectedItem();
        String selectedName = selectedBefore == null ? null : selectedBefore.toString();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        if (editor == null) {
            model.addElement("None");
            presetBox.setModel(model);
            presetApplyBtn.setEnabled(false);
            presetDeleteBtn.setEnabled(false);
            return;
        }

        ArrayList<String> names = new ArrayList<>(editor.getPresetNames());
        Collections.sort(names);
        if (names.isEmpty()) {
            model.addElement("None");
            presetApplyBtn.setEnabled(false);
            presetDeleteBtn.setEnabled(false);
        } else {
            for (String n : names) model.addElement(n);
            presetApplyBtn.setEnabled(true);
            presetDeleteBtn.setEnabled(true);
        }
        presetBox.setModel(model);
        styleToolbarCombo(presetBox);

        if (selectedName != null) {
            presetBox.setSelectedItem(selectedName);
        }
    }

    private void applySelectedPreset() {
        if (editor == null) return;
        Object selected = presetBox.getSelectedItem();
        if (selected == null) return;
        String name = selected.toString();
        if (name.equals("None")) return;

        try {
            editor.applyPreset(name);
            scheduleRender();
            refreshControls();
        } catch (NonExistentPresetException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            refreshPresetBox();
        }
    }

    private void savePreset() {
        if (editor == null) return;
        String name = JOptionPane.showInputDialog(this, "Preset name:");
        if (name == null) return;
        name = name.trim();
        if (name.isEmpty()) return;

        try {
            editor.savePreset(name);
            refreshPresetBox();
            presetBox.setSelectedItem(name);
        } catch (PresetAlreadyExistsException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePreset() {
        if (editor == null) return;
        Object selected = presetBox.getSelectedItem();
        if (selected == null) return;
        String name = selected.toString();
        if (name.equals("None")) return;

        int res = JOptionPane.showConfirmDialog(this, "Delete preset: " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (res != JOptionPane.YES_OPTION) return;

        editor.removePreset(name);
        refreshPresetBox();
    }

    private class AdjustmentChangeListener implements ChangeListener {
        private AdjustmentType type;

        AdjustmentChangeListener(AdjustmentType type) { this.type = type; }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (programmaticUpdate) return; // ignore when controls are being updated from state
            if (editor == null) return;
            JSlider s = (JSlider) e.getSource();
            int val = s.getValue();
            valueLabels.get(type).setText(String.valueOf(val));
//            valueLabels.get(type).setVisible(s.getValueIsAdjusting());
            s.repaint();  // niestety konieczne jest przerysowywanie przy zmianie, inaczej jest bug paska
            if (!s.getValueIsAdjusting()) {
                editor.applyAdjustment(type, val);
                scheduleRender();
                refreshControls();
            }
        }
    }

    public ImageEditor getEditor() {
        return editor;
    }

    private void exportFile() {
        if (editor == null) {
            JOptionPane.showMessageDialog(this, "No image loaded to export", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export image");
        chooser.setSelectedFile(new File("export.png"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG image", "png"));
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG image", "jpg", "jpeg"));

        int res = chooser.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;

        File f = chooser.getSelectedFile();
        String path = f.getAbsolutePath();
        String lower = path.toLowerCase();
        String format;
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) format = "jpg";
        else if (lower.endsWith(".png")) format = "png";
        else { format = "png"; path = path + ".png"; }

        String Path = path;
        String Format = format;

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                editor.export(Format, Path);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(EditorGui.this, "Exported: " + Path, "Export", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditorGui.this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
