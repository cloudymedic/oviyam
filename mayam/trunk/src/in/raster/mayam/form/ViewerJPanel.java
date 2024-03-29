/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 *
 * The Initial Developer of the Original Code is
 * Raster Images
 * Portions created by the Initial Developer are Copyright (C) 2009-2010
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * Babu Hussain A
 * Devishree V
 * Meer Asgar Hussain B
 * Prakash J
 * Suresh V
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package in.raster.mayam.form;

import in.raster.mayam.context.ApplicationContext;
import in.raster.mayam.delegates.Buffer;
import in.raster.mayam.delegates.LocalizerDelegate;
import in.raster.mayam.dicomtags.DicomTags;
import in.raster.mayam.dicomtags.DicomTagsReader;
import in.raster.mayam.form.dialogs.ExportDicom;
import in.raster.mayam.form.display.Display;
import in.raster.mayam.models.SeriesAnnotations;
import in.raster.mayam.models.StudyAnnotation;
import in.raster.mayam.param.TextOverlayParam;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author Devishree
 * @version 2.1
 */
public class ViewerJPanel extends javax.swing.JPanel {

    private ImageToolbar imageToolbar = null;
    private JPanel container = null;
    private ImagePreviewPanel imagePreviewPanel = null;
    private Border selectionBorder = BorderFactory.createLineBorder(new Color(255, 138, 0));
    private JSplitPane splitPane = null;
    private GridLayout gLayout = null;
    private LayeredCanvas selectedCanvas = null;
    //Annotations
    private StudyAnnotation studyAnnotations = null;
    private String tool = "";

    /**
     * Creates new form ViewerJPanel
     */
    public ViewerJPanel() {
        initComponents();
        setLayout(new BorderLayout(0, 0));
        imageToolbar = new ImageToolbar(this);
        add(imageToolbar, BorderLayout.NORTH);
        gLayout = new GridLayout(1, 1);
        container = new JPanel(gLayout);
        container.setBackground(Color.BLACK);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setRightComponent(container);
        add(splitPane, BorderLayout.CENTER);
        imageToolbar.disableMultiSeriesTools();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 765, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 458, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    //create image canvas
    public boolean isCanvasCreated() {
        return (selectedCanvas != null);
    }

    public void addLayeredCanvas(LayeredCanvas layeredCanvas) {
        container.removeAll();
        JPanel seriesLevelPanel = new JPanel(gLayout);
        seriesLevelPanel.add(layeredCanvas);
        container.add(seriesLevelPanel);
        splitPane.setRightComponent(container);
        layeredCanvas.annotationPanel.setBorder(selectionBorder);
        selectedCanvas = layeredCanvas;
        imageToolbar.designPresetContext();
        imageToolbar.designAnnotationContext();
        doWindowing();
        String fileLocation = selectedCanvas.imgpanel.getSeriesLocation();
        fileLocation = fileLocation.substring(0, fileLocation.lastIndexOf(File.separator));
        readAnnotations(getName(), fileLocation);
        selectedCanvas.imgpanel.setCurrentSeriesAnnotation();
        imagePreviewPanel.setSeriesIdentification(container);
        if (selectedCanvas.imgpanel.isMultiFrame()) {
            imageToolbar.doAutoPlay();
        }
    }

    public void updateTextOverlay() {
        selectedCanvas.imgpanel.updateTextOverlay();
    }

    //Previews
    public void setLeftComponent(ImagePreviewPanel imagePreviewPanel) {
        this.imagePreviewPanel = imagePreviewPanel;
        splitPane.setLeftComponent(this.imagePreviewPanel);
        imagePreviewPanel.setMinimumSize(new Dimension(270, splitPane.getHeight()));
        splitPane.setDividerLocation(270);
        splitPane.setDividerSize(15);
    }

    public void displayPreviews() {
        imagePreviewPanel.displayAllPreviews();
    }

    public void displayPreview(String seriesInstanceUid) {
        imagePreviewPanel.displayPreiew(seriesInstanceUid);
    }

    public void enableMultiSeriesTools() {
        imagePreviewPanel.convertVideos();
        imageToolbar.enableMultiSeriesTools();
    }

    //Viewer Tools
    public void doWindowing() {
        tool = tool.equalsIgnoreCase("windowing") ? "" : "windowing";
        if (tool.equalsIgnoreCase("windowing")) {
            imageToolbar.doWindowing(true);
        } else {
            imageToolbar.doWindowing(false);
        }
        selectedCanvas.annotationPanel.disableAnnotations();
    }

    public void setWindowing() {
        tool = "windowing";
        imageToolbar.deselectTools();
    }

    public boolean doProbe() {
        tool = tool.equals("probe") ? "" : "probe";
        selectedCanvas.annotationPanel.setDefaultCursor();
        return tool.equals("probe");
    }

    public void doVerticalFlip() {
        JPanel parent = (JPanel) selectedCanvas.getParent();
        for (int i = 0; i < parent.getComponentCount(); i++) {
            LayeredCanvas tempCanvas = (LayeredCanvas) parent.getComponent(i);
            if (tempCanvas != null && tempCanvas.annotationPanel != null && tempCanvas.imgpanel != null) {
                tempCanvas.annotationPanel.setDefaultCursor();
                tempCanvas.imgpanel.flipVertical();
                tempCanvas.annotationPanel.doFlipVertical();
                tempCanvas.imgpanel.repaint();
                tempCanvas.textOverlay.repaint();
            }
        }
    }

    public void doHorizontalFlip() {
        JPanel parent = (JPanel) selectedCanvas.getParent();
        for (int i = 0; i < parent.getComponentCount(); i++) {
            LayeredCanvas tempCanvas = (LayeredCanvas) parent.getComponent(i);
            if (tempCanvas != null && tempCanvas.annotationPanel != null && tempCanvas.imgpanel != null) {
                tempCanvas.annotationPanel.setDefaultCursor();
                tempCanvas.imgpanel.flipHorizontal();
                tempCanvas.annotationPanel.doFlipHorizontal();
                tempCanvas.imgpanel.repaint();
                tempCanvas.textOverlay.repaint();
            }
        }
    }

    public void doRotateLeft() {
        JPanel parent = (JPanel) selectedCanvas.getParent();
        LayeredCanvas tempCanvas;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            tempCanvas = (LayeredCanvas) parent.getComponent(i);
            if (tempCanvas != null && tempCanvas.annotationPanel != null && tempCanvas.imgpanel != null) {
                tempCanvas.annotationPanel.setDefaultCursor();
                tempCanvas.imgpanel.rotateLeft();
                tempCanvas.annotationPanel.doRotateLeft();
            }
        }
        tempCanvas = null;
    }

    public void doRotateRight() {
        JPanel parent = (JPanel) selectedCanvas.getParent();
        LayeredCanvas tempCanvas;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            tempCanvas = (LayeredCanvas) parent.getComponent(i);
            if (tempCanvas != null && tempCanvas.annotationPanel != null && tempCanvas.imgpanel != null) {
                tempCanvas.annotationPanel.setDefaultCursor();
                tempCanvas.imgpanel.rotateRight();
                tempCanvas.annotationPanel.doRotateRight();
            }
        }
        tempCanvas = null;
    }

    public boolean doZoom() {
        selectedCanvas.annotationPanel.disableAnnotations();
        tool = tool.equals("zooming") ? "" : "zooming";
        return tool.equals("zooming");
    }

    public boolean doPan() {
        selectedCanvas.annotationPanel.disableAnnotations();
        tool = tool.equals("panning") ? "" : "panning";
        return tool.equals("panning");
    }

    public boolean doInvert() {
        JPanel parent = (JPanel) selectedCanvas.getParent();
        boolean negative = false;
        LayeredCanvas tempCanvas;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            tempCanvas = (LayeredCanvas) parent.getComponent(i);
            if (tempCanvas != null && tempCanvas.annotationPanel != null && tempCanvas.imgpanel != null) {
                tempCanvas.annotationPanel.setDefaultCursor();
                negative = tempCanvas.imgpanel.negative();
            }
        }
        tempCanvas = null;
        return negative;
    }

    public void toggleAnnotations() {
        selectedCanvas.annotationPanel.toggleAnnotation();
        if (selectedCanvas.annotationPanel.isShowAnnotation()) {
            imageToolbar.showAnnotationTools();
        } else {
            imageToolbar.hideAnnotationTools();

        }
    }

    public void doReset() {
        disableAnnotations();
        JPanel parent = (JPanel) selectedCanvas.getParent();
        for (int i = 0; i < parent.getComponentCount(); i++) {
            LayeredCanvas tempcanCanvas = (LayeredCanvas) parent.getComponent(i);
            if (tempcanCanvas != null && tempcanCanvas.annotationPanel != null && tempcanCanvas.imgpanel != null) {
                tempcanCanvas.imgpanel.reset();
            }
        }
        imageToolbar.deselectTools();
    }

    //For cine loop
    public void doNext() {
        selectedCanvas.imgpanel.doNext();
    }

    public void doPrevious() {
        selectedCanvas.imgpanel.doPrevious();
    }

    public void toggleTextOverlay() {
        LayeredCanvas tempCanvas = null;
        for (int i = 0; i < container.getComponentCount(); i++) {
            if (container.getComponent(i) instanceof LayeredCanvas) {
                tempCanvas = ((LayeredCanvas) container.getComponent(i));
                if (tempCanvas.textOverlay != null) {
                    tempCanvas.textOverlay.toggleTextOverlay();
                }
            }
            JPanel seriesLevelPanel = (JPanel) container.getComponent(i);
            for (int k = 0; k < seriesLevelPanel.getComponentCount(); k++) {
                if (seriesLevelPanel.getComponent(k) instanceof LayeredCanvas) {
                    tempCanvas = (LayeredCanvas) seriesLevelPanel.getComponent(k);
                    if (tempCanvas.textOverlay != null) {
                        tempCanvas.textOverlay.toggleTextOverlay();
                    }
                }
            }
        }
    }

    public boolean doStack() {
        disableAnnotations();
        tool = tool.equals("stack") ? "" : "stack";
        return tool.equals("stack");
    }

    public boolean doScout() {
        selectedCanvas.annotationPanel.setDefaultCursor();
        if (!ImagePanel.isDisplayScout() && container.getComponentCount() > 1 && (selectedCanvas.getParent()).getComponentCount() == 1) {
            ImagePanel.setDisplayScout(true);
            LocalizerDelegate localizer = new LocalizerDelegate(false);
            localizer.drawScoutLineWithBorder(selectedCanvas, container);
            return true;
        } else {
            ImagePanel.setDisplayScout(false);
            LocalizerDelegate.hideAllScoutLines(container);
            return false;
        }
    }

    public void showMetaData() {
        ArrayList<DicomTags> dcmTags = DicomTagsReader.getTags(new File(selectedCanvas.imgpanel.getDicomFileUrl()));
        DicomTagsViewer dicomTagsViewer = new DicomTagsViewer(dcmTags);
        Display.alignScreen(dicomTagsViewer);
        dicomTagsViewer.setVisible(true);
    }

    public void doAnnotation() {
        tool = "annotations";
        selectedCanvas.annotationPanel.setMouseLocX1(-1);
        selectedCanvas.annotationPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void stopAnnotation() {
        this.tool = "";
        selectedCanvas.annotationPanel.stopAnnotation();
    }

    public void clearMeasurements() {
        selectedCanvas.annotationPanel.clearAllMeasurement();
    }

    public void doRuler(boolean addArrow) {
        selectedCanvas.annotationPanel.setMouseLocX1(-1);
        if (addArrow) {
            tool = "arrow";
            selectedCanvas.annotationPanel.addArrow();
        } else {
            tool = "ruler";
            selectedCanvas.annotationPanel.addRuler();
        }
    }

    public void doRectangle() {
        tool = "rectangle";
        selectedCanvas.annotationPanel.setMouseLocX1(-1);
        selectedCanvas.annotationPanel.addRect();
    }

    public void doEllipse() {
        tool = "ellipse";
        selectedCanvas.annotationPanel.setMouseLocX1(-1);
        selectedCanvas.annotationPanel.addEllipse();
    }

    public void disableAnnotations() {
        this.tool = "";
        selectedCanvas.annotationPanel.disableAnnotations();
    }

    public void deleteselectedAnnotations() {
        selectedCanvas.annotationPanel.deleteSelectedAnnotation();
    }

    public boolean dodeleteMeasurements() {
        return selectedCanvas.annotationPanel.doDeleteMeasurement();
    }

    public void hideAnnotationTools() {
        imageToolbar.hideAnnotationTools();
    }

    public void doExport() {
        ExportDicom exporter = new ExportDicom(selectedCanvas);
//        Display.alignScreen(exporter);
        exporter.setVisible(true);
    }

    public void doSynchronize() {
        selectedCanvas.annotationPanel.setDefaultCursor();
        selectedCanvas.imgpanel.doSynchronize();
    }

    public boolean isHideAnnotations() {
        return selectedCanvas.annotationPanel.isShowAnnotation();
    }

    public void changeImageLayout(int row, int col) {
        ArrayList<String> tempRef = ApplicationContext.databaseRef.getSeriesInstancesLocation(getName());
        boolean showTextOverlay = true;

        selectedCanvas.imgpanel.storeAnnotation();
        showTextOverlay = selectedCanvas.textOverlay.isTextVisible();
        terminateThreads();

        if (ApplicationContext.buffer != null) {
            ApplicationContext.buffer.terminateTileLayoutThread();
            ApplicationContext.buffer = null;
        }
        container = new JPanel(new GridLayout(row, col));
        splitPane.setRightComponent(container);
        for (int i = 0; i < (row * col); i++) {
            JPanel newPanel = new JPanel(gLayout);
            if (i < tempRef.size()) {
                LayeredCanvas canvas = new LayeredCanvas(new File(tempRef.get(i)), 0, false);
                newPanel.add(canvas);
                container.add(newPanel);
                canvas.imgpanel.setCurrentSeriesAnnotation();
                if (!showTextOverlay) {
                    canvas.textOverlay.toggleTextOverlay();
                }
                selectedCanvas = canvas;
            } else {
                container.add(newPanel);
            }
        }
        selectedCanvas.annotationPanel.setBorder(selectionBorder);
        imagePreviewPanel.setSeriesIdentification(container);
    }

    public void changeTileLayout(final int row, final int col) {
        try {
            selectedCanvas.imgpanel.buffer.terminateThread();
        } catch (NullPointerException ex) {
            ApplicationContext.logger.log(Level.INFO, "ViewerJPanel - Thread Termination");
        }
        try {
            ApplicationContext.buffer.terminateThread();
        } catch (NullPointerException ex) {
            ApplicationContext.logger.log(Level.INFO, "ViewerJPanel - Buffer not exist");
        }
        final boolean textOverlay = selectedCanvas.textOverlay.isTextVisible();

        final JPanel seriesLevelPanel = (JPanel) selectedCanvas.getParent();
        seriesLevelPanel.removeAll();
        seriesLevelPanel.setLayout(new GridLayout(row, col));

        //First canvas
        selectedCanvas = new LayeredCanvas(new File(ApplicationContext.databaseRef.getFirstInstanceLocation(selectedCanvas.imgpanel.getStudyUID(), selectedCanvas.imgpanel.getSeriesUID())), 0, true);
        seriesLevelPanel.add(selectedCanvas);
        selectedCanvas.imgpanel.setIsNormal(false);
        final TextOverlayParam textOverlayParam = selectedCanvas.imgpanel.getTextOverlayParam();

        if (!textOverlay) {
            selectedCanvas.textOverlay.toggleTextOverlay();
        }

        ApplicationContext.buffer = new Buffer(selectedCanvas.imgpanel);
        ApplicationContext.buffer.setBufferSize((row * col) + (row * col) + (row * col));
        ApplicationContext.buffer.createThread(-1);

        final ArrayList<String> instanceUidList = selectedCanvas.imgpanel.getInstanceUidList();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < (row * col); i++) {
                    if (i < instanceUidList.size()) {
                        LayeredCanvas canvas = new LayeredCanvas(true, selectedCanvas.imgpanel.getStudyUID(), selectedCanvas.imgpanel.getSeriesUID());
                        seriesLevelPanel.add(canvas);
                        selectedCanvas.imgpanel.setInfo(canvas.imgpanel);
                        canvas.textOverlay.setTextOverlayParam(new TextOverlayParam(textOverlayParam.getPatientName(), textOverlayParam.getPatientID(), textOverlayParam.getSex(), textOverlayParam.getStudyDate(), textOverlayParam.getStudyDescription(), textOverlayParam.getSeriesDescription(), textOverlayParam.getBodyPartExamined(), textOverlayParam.getInstitutionName(), textOverlayParam.getWindowLevel(), textOverlayParam.getWindowWidth(), i, textOverlayParam.getTotalInstance(), textOverlayParam.isMultiframe()));
                        if (!textOverlay) {
                            canvas.textOverlay.toggleTextOverlay();
                        }
                        canvas.imgpanel.setImage(i);
                        selectedCanvas = canvas;
                    } else {
                        seriesLevelPanel.add(new JPanel());
                    }
                }
                imagePreviewPanel.setSeriesIdentification(container);
                selectedCanvas.annotationPanel.setBorder(selectionBorder);
            }
        });
        LocalizerDelegate.hideAllScoutLines(container);
    }

    public String getModality() {
        return selectedCanvas.imgpanel.getModality();
    }

    public JPanel getCanvasParent() {
        return (JPanel) selectedCanvas.getParent();
    }

    public void setSelection(LayeredCanvas canvas) {
        selectedCanvas.annotationPanel.setBorder(null);
        selectedCanvas = canvas;
        selectedCanvas.annotationPanel.setBorder(selectionBorder);
        if (ImagePanel.isDisplayScout()) {
            try {
                LocalizerDelegate localizerDelegate = new LocalizerDelegate(false);
                localizerDelegate.drawScoutLineWithBorder(selectedCanvas, container);
            } catch (ArrayIndexOutOfBoundsException ex) {
                ApplicationContext.logger.log(Level.INFO, "ViewerJPanel");
            }
        }
        if (tool.equals("ruler")) {
            selectedCanvas.annotationPanel.addRuler();
        } else if (tool.equals("rectangle")) {
            selectedCanvas.annotationPanel.addRect();
        } else if (tool.equals("ellipse")) {
            selectedCanvas.annotationPanel.addEllipse();
        } else if (tool.equals("arrow")) {
            selectedCanvas.annotationPanel.addArrow();
        }
    }

    //Annotations
    public void readAnnotations(String studyUid, String annotationLoc) {
        try {
            FileInputStream fis = new FileInputStream(annotationLoc + File.separator + "info.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            studyAnnotations = (StudyAnnotation) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            //ignore IO Exception occurs when the study does not have any annotations yet.
        } catch (ClassNotFoundException ex) {
            ApplicationContext.logger.log(Level.INFO, "ViewerJPanel", ex);
        }

    }

    public SeriesAnnotations getSeriesAnnotaions(String seriesUid) {
        if (studyAnnotations == null) {
            studyAnnotations = new StudyAnnotation(getName());
        }
        if (studyAnnotations.getSeriesAnnotation(seriesUid) == null) {
            studyAnnotations.putSeriesAnnotation(seriesUid, new SeriesAnnotations(seriesUid));
        }
        return studyAnnotations.getSeriesAnnotation(seriesUid);
    }

    public void putSeriesAnnotation(String seriesUid, SeriesAnnotations seriesAnnotations) {
        studyAnnotations.putSeriesAnnotation(seriesUid, seriesAnnotations);
    }

    public void saveAnnotations() {
        for (int i = 0; i < container.getComponentCount(); i++) {
            JPanel seriesLevelPanel = (JPanel) container.getComponent(i);
            for (int j = 0; j < seriesLevelPanel.getComponentCount(); j++) {
                Component component = seriesLevelPanel.getComponent(j);
                if (component instanceof LayeredCanvas) {
                    ((LayeredCanvas) component).imgpanel.storeAllAnnotations();
                }
            }
        }
        writeToFile(selectedCanvas.imgpanel.getSeriesLocation());
    }

    private void writeToFile(String fileLoc) {
        if (fileLoc.contains(ApplicationContext.getAppDirectory())) {
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                fos = new FileOutputStream(fileLoc.substring(0, fileLoc.lastIndexOf(File.separator)) + File.separator + "info.ser");
                oos = new ObjectOutputStream(fos);
                oos.writeObject(studyAnnotations);
            } catch (FileNotFoundException ex) {
                ApplicationContext.logger.log(Level.INFO, "ViewerJPanel", ex);
            } catch (IOException ex) {
                ApplicationContext.logger.log(Level.INFO, "ViewerJPanel", ex);
            } finally {
                if (fos != null && oos != null) {
                    try {
                        fos.close();
                        oos.close();
                    } catch (IOException ex) {
                        ApplicationContext.logger.log(Level.INFO, "ViewerJPanel", ex);
                    }
                }
            }
        }
    }

    public void terminateThreads() {
        for (int i = 0; i < container.getComponentCount(); i++) {
            try {
                LayeredCanvas tempCanvas = ((LayeredCanvas) ((JPanel) container.getComponent(i)).getComponent(0));
                tempCanvas.imgpanel.buffer.terminateThread();
                tempCanvas.imgpanel.shutDown();
            } catch (Exception ex) {
                ApplicationContext.logger.log(Level.INFO, "ViewerJPanel - Thread termination");
            }
        }
        if (ApplicationContext.buffer != null) {
            ApplicationContext.buffer.terminateTileLayoutThread();
            ApplicationContext.buffer = null;
        }
    }

    //For series changes
    public void thumbnailClicked() {
        imageToolbar.deselectLoopChk();
        imageToolbar.enableImageTools();
    }

    public boolean isTileLayout() {
        return selectedCanvas.getParent().getComponentCount() > 1;
    }

    public String getSelectedSeriesUid() {
        return selectedCanvas.imgpanel.getSeriesUID();
    }

    public LayeredCanvas getSelectedCanvas() {
        return selectedCanvas;
    }

    public void mouseDragged(MouseEvent me) {
        if (tool.equalsIgnoreCase("windowing")) {
            selectedCanvas.annotationPanel.setWindowingCursor();
            selectedCanvas.imgpanel.mouseDragWindowing(me.getX(), me.getY());
        } else if (tool.equalsIgnoreCase("zooming")) {
            selectedCanvas.annotationPanel.setZoomCursor();
            selectedCanvas.imgpanel.mouseDragZoom(me.getX(), me.getY());
        } else if (tool.equalsIgnoreCase("panning")) {
            selectedCanvas.annotationPanel.setPanCursor();
            selectedCanvas.imgpanel.panImage(me.getPoint());
        } else if (tool.equals("stack")) {
            selectedCanvas.annotationPanel.setStackCursor();
            selectedCanvas.imgpanel.mouseDragStack(me.getX(), me.getY());
        }
    }

    public String getTool() {
        return tool;
    }

    public void setSeriesIdentification() {
        imagePreviewPanel.setSeriesIdentification(container);
    }

    public void keyEventHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
            selectedCanvas.imgpanel.setHints = false;
            doPrevious();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            selectedCanvas.imgpanel.setHints = false;
            doNext();
        } else if (e.getKeyCode() == KeyEvent.VK_O) {
            imageToolbar.setScoutSelected(doScout());
        } else if (e.getKeyCode() == KeyEvent.VK_I) {
            toggleTextOverlay();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            doReset();
        } else if (e.getKeyCode() == KeyEvent.VK_C) {
            selectedCanvas.imgpanel.setHints = false;
            imageToolbar.doLoop();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            selectedCanvas.imgpanel.setHints = false;
            imageToolbar.doStack();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            imageToolbar.doRuler(false);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            imageToolbar.doRuler(true);
        } else if (e.getKeyCode() == KeyEvent.VK_T) {
            imageToolbar.doPan();
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            deleteselectedAnnotations();
        }
    }

    public void handleContextSelection(String toolName) {
        if (toolName.equalsIgnoreCase(ApplicationContext.currentBundle.getString("ImageView.windowingButton.toolTipText"))) {
            setWindowing();
        } else if (toolName.equalsIgnoreCase(ApplicationContext.currentBundle.getString("ImageView.panButton.toolTipText"))) {
            imageToolbar.doPan();
        } else if (toolName.equalsIgnoreCase(ApplicationContext.currentBundle.getString("ImageView.stackButton.toolTipText"))) {
            imageToolbar.doStack();
        } else if (toolName.equalsIgnoreCase(ApplicationContext.currentBundle.getString("ImageView.rulerButton.toolTipText"))) {
            imageToolbar.doRuler(false);
        } else if (toolName.equalsIgnoreCase(ApplicationContext.currentBundle.getString("ImageView.rectangleButton.toolTipText"))) {
            imageToolbar.doRectangle();
        } else if (toolName.equalsIgnoreCase(ApplicationContext.currentBundle.getString("ImageView.ellipseButton.toolTipText"))) {
            imageToolbar.doEllipse();
        } else if (toolName.equalsIgnoreCase(ApplicationContext.currentBundle.getString("ImageView.arrowButton.toolTipText"))) {
            imageToolbar.doRuler(true);
        }
    }

    public void applyLocaleChange() {
        imageToolbar.applyLocale();
        selectedCanvas.repaint();
    }

    //Video
    public void createVideoCanvas(String videoFilePath, String studyUid) {
        imageToolbar.disableAllTools();
        MediaPlayer videoPlayer = new MediaPlayer();
        splitPane.setRightComponent(videoPlayer);
        videoPlayer.playMedia(videoFilePath);
        selectedCanvas = null;
        imagePreviewPanel.setVideoIdentification(videoPlayer.getName());
    }

    public boolean isVideoDisplay() {
        return (splitPane.getRightComponent() instanceof MediaPlayer);
    }

    public void stopVideoTimer() {
        ((MediaPlayer) splitPane.getRightComponent()).stopTimer();
    }

    public void enableAllImageTools() {
        imageToolbar.enableImageTools();
    }

    //Loop
    public void doAutoPlay() {
        imageToolbar.doAutoPlay();
    }

    public void nextFrame() {
        selectedCanvas.imgpanel.showNextFrame();
    }

    public boolean isMultiframe() {
        return selectedCanvas.imgpanel.isMultiFrame();
    }

    public void setRenderingQuality(boolean isRender) {
        selectedCanvas.imgpanel.setHints = isRender;
    }

    public int getTotalFrames() {
        return selectedCanvas.imgpanel.getTotalFrames();
    }

    public void showFrame(int i) {
        selectedCanvas.imgpanel.displayFrame(i);
    }

    public void stopAutoPlay() {
        imageToolbar.stopAutoPlay();
    }

    public int getTotalImages() {
        return selectedCanvas.imgpanel.getTotalInstance();
    }

    public int getCurrentInstanceOrFrame() {
        return selectedCanvas.imgpanel.getCurrentInstanceNo();
    }

    public void showImage(int i) {
        selectedCanvas.imgpanel.showImage(i);
        if (ImagePanel.isDisplayScout()) {
            try {
                LocalizerDelegate localizerDelegate = new LocalizerDelegate(false);
                localizerDelegate.drawScoutLineWithBorder(selectedCanvas, container);
            } catch (ArrayIndexOutOfBoundsException ex) {
                ApplicationContext.logger.log(Level.INFO, "ViewerJPanel");
            }
        }
    }

    public void forwardLoopBack() {
        selectedCanvas.imgpanel.buffer.forwardLoopBack();
    }

    public void setTool(String tool) {
        this.tool = tool;
    }
}
