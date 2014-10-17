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
 * Portions created by the Initial Developer are Copyright (C) 2014
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
package in.raster.mayam.form.dialogs;

import in.raster.mayam.context.ApplicationContext;
import in.raster.mayam.delegates.DicomImageReader;
import in.raster.mayam.form.LayeredCanvas;
import in.raster.mayam.form.ViewerJPanel;
import in.raster.mayam.form.display.Display;
import in.raster.mayam.models.ServerModel;
import in.raster.mayam.param.TextOverlayParam;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import org.dcm4che2.net.ConfigurationException;
import org.dcm4che2.tool.dcmsnd.DcmSnd;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.media.Buffer;
import javax.media.ConfigureCompleteEvent;
import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import static javax.media.Duration.DURATION_UNKNOWN;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.Time;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullBufferStream;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Devishree
 * @version 2.1
 */
public class ExportDicom extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    private LayeredCanvas selectedCanvas = null;
    private File patientNameFile = null;
    private HashSet<String> selectedServers = null;
    private HashSet<ServerModel> serversToSend = null;
    private ProgressBar progress = null;

    private ImageIcon image = null;

    /**
     * Creates new form ExportDicom
     */
    public ExportDicom(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public ExportDicom(LayeredCanvas selectedCanvas) {
        super(ApplicationContext.viewer, true);
        this.selectedCanvas = selectedCanvas;
        initComponents();
        Display.alignScreen(this, ApplicationContext.viewer);
        loadServers();
        imageLbl.setVisible(false);

    }

    private void initializeProgress() {
        progress = new ProgressBar(ApplicationContext.viewer, true, ApplicationContext.currentBundle.getString("ExportingProgress.exportingLabel.text"), ApplicationContext.currentBundle.getString("ExportingProgress.multiframeExportingLabel.text"));
        Display.alignScreen(progress, ApplicationContext.viewer);
    }

    private void toggleImage() {
        imageLbl.setIcon(image);
        imageLbl.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formatGroup = new javax.swing.ButtonGroup();
        imageLevelGroup = new javax.swing.ButtonGroup();
        frameRategroup = new javax.swing.ButtonGroup();
        sendToDicomNodesRadio = new javax.swing.JRadioButton();
        destinationLabel = new javax.swing.JLabel();
        saveAsDicomRadio = new javax.swing.JRadioButton();
        saveAsJpegRadio = new javax.swing.JRadioButton();
        exportLevelLabel = new javax.swing.JLabel();
        singleImageRadio = new javax.swing.JRadioButton();
        entireSeriesRadio = new javax.swing.JRadioButton();
        entireStudyRadio = new javax.swing.JRadioButton();
        cancelButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        serverPanel = new javax.swing.JPanel();
        exportWithTextOverlayChk = new javax.swing.JCheckBox();
        exportWithAnnotationOverlayChk = new javax.swing.JCheckBox();
        exportMultiframesAsAVI = new javax.swing.JCheckBox();
        frameRateText = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        imageLbl = new javax.swing.JLabel();
        useNativeFrameRateRadio = new javax.swing.JRadioButton();
        usePreferredFrameRateRadio = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(ApplicationContext.currentBundle.getString("ExportDialog.title.text")); // NOI18N
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/in/raster/mayam/form/images/fav_mayam.png")));
        setResizable(false);

        formatGroup.add(sendToDicomNodesRadio);
        sendToDicomNodesRadio.setText(ApplicationContext.currentBundle.getString("ExportDialog.toDicomNodeRadio.text")); // NOI18N
        sendToDicomNodesRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sendToDicomNodesRadioItemStateChanged(evt);
            }
        });

        destinationLabel.setFont(new Font("Lucida Grande",Font.BOLD,14));
        destinationLabel.setText(ApplicationContext.currentBundle.getString("ExportDialog.destinationLbl.text")); // NOI18N
        destinationLabel.setPreferredSize(new java.awt.Dimension(83, 20));

        formatGroup.add(saveAsDicomRadio);
        saveAsDicomRadio.setText(ApplicationContext.currentBundle.getString("ExportDialog.dicomRadio.text")); // NOI18N

        formatGroup.add(saveAsJpegRadio);
        saveAsJpegRadio.setText(ApplicationContext.currentBundle.getString("ExportDialog.jpgRadio.text")); // NOI18N
        saveAsJpegRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                saveAsJpegRadioItemStateChanged(evt);
            }
        });

        exportLevelLabel.setFont(new Font("Lucida Grande",Font.BOLD,14));
        exportLevelLabel.setText(ApplicationContext.currentBundle.getString("ExportDialog.selectionLbl.text")); // NOI18N
        exportLevelLabel.setPreferredSize(new java.awt.Dimension(86, 20));

        imageLevelGroup.add(singleImageRadio);
        singleImageRadio.setText(ApplicationContext.currentBundle.getString("ExportDialog.currentImageRadio.text")); // NOI18N
        singleImageRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                singleImageRadioItemStateChanged(evt);
            }
        });

        imageLevelGroup.add(entireSeriesRadio);
        entireSeriesRadio.setText(ApplicationContext.currentBundle.getString("ExportDialog.allImagesRadio.text")); // NOI18N
        entireSeriesRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                entireSeriesRadioItemStateChanged(evt);
            }
        });

        imageLevelGroup.add(entireStudyRadio);
        entireStudyRadio.setText(ApplicationContext.currentBundle.getString("ExportDialog.entireStudy.text")); // NOI18N
        entireStudyRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                entireStudyRadioItemStateChanged(evt);
            }
        });

        cancelButton.setText(ApplicationContext.currentBundle.getString("CancelButtons.text")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        exportButton.setText(ApplicationContext.currentBundle.getString("ExportDialog.exportButton.text")); // NOI18N
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout serverPanelLayout = new javax.swing.GroupLayout(serverPanel);
        serverPanel.setLayout(serverPanelLayout);
        serverPanelLayout.setHorizontalGroup(
            serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 347, Short.MAX_VALUE)
        );
        serverPanelLayout.setVerticalGroup(
            serverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 242, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(serverPanel);

        exportWithTextOverlayChk.setText(ApplicationContext.currentBundle.getString("ExportDialog.exportWithTextOverlayChk.text")); // NOI18N
        exportWithTextOverlayChk.setEnabled(false);

        exportWithAnnotationOverlayChk.setText(ApplicationContext.currentBundle.getString("ExportDialog.exportWithAnnotationOverlayChk.text")); // NOI18N
        exportWithAnnotationOverlayChk.setEnabled(false);

        exportMultiframesAsAVI.setText(ApplicationContext.currentBundle.getString("ExportDialog.exportMultiframesAsAVIChk.text")); // NOI18N
        exportMultiframesAsAVI.setEnabled(false);
        exportMultiframesAsAVI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                exportMultiframesAsAVIItemStateChanged(evt);
            }
        });

        frameRateText.setText("25");
        frameRateText.setEnabled(false);

        jLabel1.setFont(new Font("Lucida Grande",Font.BOLD,14));
        jLabel1.setText(ApplicationContext.currentBundle.getString("ExportDialog.optionsLbl.text")); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(51, 20));

        imageLbl.setPreferredSize(new java.awt.Dimension(126, 126));

        frameRategroup.add(useNativeFrameRateRadio);
        useNativeFrameRateRadio.setSelected(true);
        useNativeFrameRateRadio.setText(ApplicationContext.currentBundle.getString("ExportDialog.useNativeFrameRateRadio.text")); // NOI18N
        useNativeFrameRateRadio.setEnabled(false);
        useNativeFrameRateRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useNativeFrameRateRadioItemStateChanged(evt);
            }
        });

        frameRategroup.add(usePreferredFrameRateRadio);
        usePreferredFrameRateRadio.setText(ApplicationContext.currentBundle.getString("ExportDialog.usePreferredFrameRateRadio.text")); // NOI18N
        usePreferredFrameRateRadio.setEnabled(false);
        usePreferredFrameRateRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                usePreferredFrameRateRadioItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(exportWithAnnotationOverlayChk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(exportWithTextOverlayChk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(exportMultiframesAsAVI, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(destinationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(sendToDicomNodesRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(saveAsJpegRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(saveAsDicomRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(43, 43, 43)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(usePreferredFrameRateRadio)
                                                .addGap(18, 18, 18)
                                                .addComponent(frameRateText, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(useNativeFrameRateRadio)))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(exportLevelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(entireSeriesRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(singleImageRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(entireStudyRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(48, 48, 48)
                                        .addComponent(imageLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 127, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(destinationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sendToDicomNodesRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveAsDicomRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveAsJpegRadio))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(exportLevelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(singleImageRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(entireSeriesRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(entireStudyRadio))
                    .addComponent(imageLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(exportMultiframesAsAVI)
                .addGap(10, 10, 10)
                .addComponent(useNativeFrameRateRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usePreferredFrameRateRadio)
                    .addComponent(frameRateText, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(exportWithTextOverlayChk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(exportWithAnnotationOverlayChk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(exportButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportActionPerformed
        if (entireStudyRadio.isSelected() || entireSeriesRadio.isSelected() || singleImageRadio.isSelected()) {
            if (sendToDicomNodesRadio.isSelected()) { //Send to DICOM nodes
                String selectedServerStr = "";
                Iterator<String> iterator = selectedServers.iterator();
                try {
                    selectedServerStr = "'" + iterator.next() + "'";
                    while (iterator.hasNext()) {
                        selectedServerStr += ",'" + iterator.next() + "'";
                    }
                    serversToSend = ApplicationContext.databaseRef.getServersToSend(selectedServerStr);
                    initializeProgress();
                    ExportToPacs exportToPacs = new ExportToPacs();
                    exportToPacs.start();
                    setVisible(false);
                    progress.setVisible(true);
                } catch (NoSuchElementException e) {
                    JOptionPane.showOptionDialog(this, ApplicationContext.currentBundle.getString("ExportDialog.noServerSelectedMsg"), ApplicationContext.currentBundle.getString("ErrorTitles.text"), JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{ApplicationContext.currentBundle.getString("OkButtons.text")}, "default");
                }
            } else if (saveAsDicomRadio.isSelected() || saveAsJpegRadio.isSelected()) { //Save in local
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getActionCommand().equalsIgnoreCase("CancelSelection")) {
                            doClose();
                        } else {
                            String selectedFolderPath = fileChooser.getSelectedFile().getAbsolutePath();
                            startExport(selectedFolderPath);
                        }
                    }

                    private void startExport(String selectedFolderPath) {
                        patientNameFile = new File(selectedFolderPath + File.separator + selectedCanvas.textOverlay.getPatientName());
                        patientNameFile.mkdirs();
                        initializeProgress();
                        Exporter exporter = new Exporter();
                        exporter.start();
                        progress.setVisible(true);
                    }
                });
                setVisible(false);
                fileChooser.showSaveDialog(this);
            } else {
                JOptionPane.showOptionDialog(this, ApplicationContext.currentBundle.getString("ExportDialog.noDestinationMsg"), ApplicationContext.currentBundle.getString("ErrorTitles.text"), JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{ApplicationContext.currentBundle.getString("OkButtons.text")}, "default");
            }
        } else {
            JOptionPane.showOptionDialog(this, ApplicationContext.currentBundle.getString("ExportDialog.noneSelectedMsg"), ApplicationContext.currentBundle.getString("ErrorTitles.text"), JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{ApplicationContext.currentBundle.getString("OkButtons.text")}, "default");
        }
    }//GEN-LAST:event_exportActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        doClose();
    }//GEN-LAST:event_cancelActionPerformed

    private void sendToDicomNodesRadioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sendToDicomNodesRadioItemStateChanged
        if (sendToDicomNodesRadio.isSelected()) {
            toggleServers(true);
//            selectedServers = new HashSet<String>(0, 1);
//            ArrayList<String> serverLabels = ApplicationContext.databaseRef.getAllServerNames();
//            if (serverLabels.size() < 4) {
//                serverPanel.setLayout(new GridLayout(4, 1, 2, 2));
//            } else {
//                serverPanel.setLayout(new GridLayout(serverLabels.size(), 1, 2, 2));
//            }
//            jScrollPane1.setVisible(true);
//            for (int i = 0; i < serverLabels.size(); i++) {
//                JCheckBox checkBox = new JCheckBox(serverLabels.get(i));
//                serverPanel.add(checkBox);
//                checkBox.addItemListener(serverSelectionListner);
//            }
//            validate();
        } else {
            toggleServers(false);
        }
    }//GEN-LAST:event_sendToDicomNodesRadioItemStateChanged

    private void saveAsJpegRadioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_saveAsJpegRadioItemStateChanged
        toggleOverlays(saveAsJpegRadio.isSelected());
    }//GEN-LAST:event_saveAsJpegRadioItemStateChanged

    private void exportMultiframesAsAVIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_exportMultiframesAsAVIItemStateChanged
        toggleFrameRateInfo(exportMultiframesAsAVI.isSelected());
    }//GEN-LAST:event_exportMultiframesAsAVIItemStateChanged

    private void singleImageRadioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_singleImageRadioItemStateChanged
        image = new ImageIcon(getClass().getResource("/in/raster/mayam/form/images/single.png"));
        toggleImage();
    }//GEN-LAST:event_singleImageRadioItemStateChanged

    private void entireSeriesRadioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_entireSeriesRadioItemStateChanged
        image = new ImageIcon(getClass().getResource("/in/raster/mayam/form/images/series.png"));
        toggleImage();
    }//GEN-LAST:event_entireSeriesRadioItemStateChanged

    private void entireStudyRadioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_entireStudyRadioItemStateChanged
        image = new ImageIcon(getClass().getResource("/in/raster/mayam/form/images/entire.png"));
        toggleImage();
    }//GEN-LAST:event_entireStudyRadioItemStateChanged

    private void usePreferredFrameRateRadioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_usePreferredFrameRateRadioItemStateChanged
        toggleFrameRateText(usePreferredFrameRateRadio.isSelected());
    }//GEN-LAST:event_usePreferredFrameRateRadioItemStateChanged

    private void useNativeFrameRateRadioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_useNativeFrameRateRadioItemStateChanged
        if (useNativeFrameRateRadio.isSelected()) {
            usePreferredFrameRateRadio.setSelected(false);
        }
    }//GEN-LAST:event_useNativeFrameRateRadioItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel destinationLabel;
    private javax.swing.JRadioButton entireSeriesRadio;
    private javax.swing.JRadioButton entireStudyRadio;
    private javax.swing.JButton exportButton;
    private javax.swing.JLabel exportLevelLabel;
    private javax.swing.JCheckBox exportMultiframesAsAVI;
    private javax.swing.JCheckBox exportWithAnnotationOverlayChk;
    private javax.swing.JCheckBox exportWithTextOverlayChk;
    private javax.swing.ButtonGroup formatGroup;
    private javax.swing.JTextField frameRateText;
    private javax.swing.ButtonGroup frameRategroup;
    private javax.swing.JLabel imageLbl;
    private javax.swing.ButtonGroup imageLevelGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JRadioButton saveAsDicomRadio;
    private javax.swing.JRadioButton saveAsJpegRadio;
    private javax.swing.JRadioButton sendToDicomNodesRadio;
    private javax.swing.JPanel serverPanel;
    private javax.swing.JRadioButton singleImageRadio;
    private javax.swing.JRadioButton useNativeFrameRateRadio;
    private javax.swing.JRadioButton usePreferredFrameRateRadio;
    // End of variables declaration//GEN-END:variables

    private void doClose() {
        setVisible(false);
        dispose();
    }

    private void exportDcm(String inputFile, String outputFile) {
//        outputFile = outputFile.replaceAll("[^\\p{L}\\p{Z}]", "");//Regex Filter to replace everything that is not a letter in any language
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile + File.separator + inputFile.substring(inputFile.lastIndexOf(File.separator) + 1));
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void exportJpeg(String outputPath, BufferedImage bimg) {
        /*outputPath = outputPath.replaceAll("[^\\p{L}\\p{Z}]", ""); //Regex Filter to replace everything that is not a letter in any language
         OutputStream output = null;
         try {
         File outputJpegFile = new File(outputPath + ".jpg");
         BufferedImage jpegImage = bimg;
         output = new BufferedOutputStream(new FileOutputStream(outputJpegFile));
         ImageIO.write(jpegImage, "jpeg", outputJpegFile);
         } catch (IOException ex) {
         Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
         } catch (Exception ex) {
         Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
         try {
         if (output != null) {
         output.close();
         }
         } catch (IOException ex) {
         ex.printStackTrace();
         }
         }
         try {
         FileOutputStream fout = new FileOutputStream(outputPath);

         JPEGImageEncoder jencoder = JPEGCodec.createJPEGEncoder(fout);
         JPEGEncodeParam enParam = jencoder.getDefaultJPEGEncodeParam(bimg);

         enParam.setQuality(1.0F, true);
         jencoder.setJPEGEncodeParam(enParam);
         jencoder.encode(bimg);

         fout.close();
         } catch (FileNotFoundException ex) {
         Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
         Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
         } */
//        outputPath = outputPath.replaceAll("[^\\p{L}\\p{Z}]", ""); //Regex Filter to replace everything that is not a letter in any language
        try {
            ImageWriter imageWriter = null;
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
            if (iter.hasNext()) {
                imageWriter = iter.next();
            }
            ImageOutputStream ios = ImageIO.createImageOutputStream(new File(outputPath + ".jpg"));
            imageWriter.setOutput(ios);
            ImageWriteParam iwParam = new JPEGImageWriteParam(ApplicationContext.currentLocale);
            iwParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwParam.setCompressionQuality(1F);
            imageWriter.write(null, new IIOImage(convertToRGB(bimg), null, null), iwParam);
            ios.flush();
            imageWriter.dispose();
            ios.close();
        } catch (IOException ex) {
            Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exportAsJPEGImages(ArrayList<String> instances, boolean isMultiframes) {
        try {
            ImageWriter imageWriter = null;
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
            if (iter.hasNext()) {
                imageWriter = iter.next();
            }
            ImageWriteParam iwParam = new JPEGImageWriteParam(ApplicationContext.currentLocale);
            iwParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwParam.setCompressionQuality(1F);

            for (int inst_Iter = 0; inst_Iter < instances.size(); inst_Iter++) {
                String outputPath = instances.get(inst_Iter).substring(instances.get(inst_Iter).lastIndexOf(File.separator) + 1);
                ImageOutputStream ios = null;
                if (!isMultiframes) {
                    ios = ImageIO.createImageOutputStream(new File(patientNameFile.getAbsolutePath() + File.separator + outputPath + ".jpg"));
                    imageWriter.setOutput(ios);
                    imageWriter.write(null, new IIOImage(convertToRGB(DicomImageReader.readDicomFile(new File(instances.get(inst_Iter)))), null, null), iwParam);
                } else {
                    BufferedImage[] multiframes = DicomImageReader.readMultiFrames(new File(instances.get(inst_Iter)));

                    progress.setMultiframeMaximum(multiframes.length);
                    for (int i = 0; i < multiframes.length; i++) {
                        ios = ImageIO.createImageOutputStream(new File(patientNameFile.getAbsolutePath() + File.separator + outputPath + "_" + i + ".jpg"));
                        imageWriter.setOutput(ios);
                        imageWriter.write(null, new IIOImage(convertToRGB(multiframes[i]), null, null), iwParam);
                        progress.showMultiframeProgress(i);
                    }
                }
                ios.flush();
                ios.close();
                progress.update();
            }
            imageWriter.dispose();
        } catch (IOException ex) {
            Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void toggleOverlays(boolean status) {
        exportWithTextOverlayChk.setEnabled(status);
        exportWithAnnotationOverlayChk.setEnabled(status);
        exportMultiframesAsAVI.setEnabled(status);
        useNativeFrameRateRadio.setEnabled(status);
        usePreferredFrameRateRadio.setEnabled(status);
    }

    private void toggleFrameRateInfo(boolean status) {
        useNativeFrameRateRadio.setEnabled(status);
        usePreferredFrameRateRadio.setEnabled(status);
    }

    private void toggleFrameRateText(boolean status) {
        frameRateText.setEnabled(status);
    }

    private void loadServers() {
        selectedServers = new HashSet<String>(0, 1);
        ArrayList<String> serverLabels = ApplicationContext.databaseRef.getAllServerNames();
//            if (serverLabels.size() < 4) {
//                serverPanel.setLayout(new GridLayout(4, 1, 2, 2));
//            } else {
        serverPanel.setLayout(new GridLayout(serverLabels.size(), 1, 2, 2));
//            }
        for (int i = 0; i < serverLabels.size(); i++) {
            JCheckBox checkBox = new JCheckBox(serverLabels.get(i));
            serverPanel.add(checkBox);
            checkBox.setEnabled(false);
            checkBox.addItemListener(serverSelectionListner);
        }
        validate();
    }

    private void toggleServers(boolean status) {
        for (int i = 0; i < serverPanel.getComponentCount(); i++) {
            serverPanel.getComponent(i).setEnabled(status);
        }
    }

    public class Exporter extends Thread {

        @Override
        public void run() {
            if (!entireStudyRadio.isSelected()) {
                if (entireSeriesRadio.isSelected()) { // A SERIES                    
                    exportSeries();
                } else { //Single image
                    exportCurrentImage();
                }
            } else { //whole study
                exportStudy();
            }
            progress.dispose();
        }
    }
    private ItemListener serverSelectionListner = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox selectedChk = ((JCheckBox) e.getSource());
            if (selectedChk.isSelected()) {
                selectedServers.add(selectedChk.getText());
            } else {
                selectedServers.remove(selectedChk.getText());
            }
        }
    };

    private class ExportToPacs extends Thread {

        @Override
        public void run() {
            Iterator<ServerModel> iterator = serversToSend.iterator();
            if (entireStudyRadio.isSelected()) { //Whole STUDY                                
                ArrayList<String> instances = ApplicationContext.databaseRef.getInstances(selectedCanvas.imgpanel.getStudyUID(), null, null);
                progress.setProgressMaximum(instances.size() * serversToSend.size());
                while (iterator.hasNext()) {
                    sendFiles(instances, iterator.next(), null);
                }
            } else if (entireSeriesRadio.isSelected()) { //A SERIES
                ArrayList<String> instances = ApplicationContext.databaseRef.getInstances(selectedCanvas.imgpanel.getStudyUID(), selectedCanvas.imgpanel.getSeriesUID(), null);
                progress.setProgressMaximum(instances.size() * serversToSend.size());
                while (iterator.hasNext()) {
                    sendFiles(instances, iterator.next(), null);
                }
            } else if (singleImageRadio.isSelected()) { //Single Image
                File file = new File(selectedCanvas.imgpanel.getDicomFileUrl());
                progress.setProgressMaximum(serversToSend.size());
                while (iterator.hasNext()) {
                    sendFiles(null, iterator.next(), file);
                }
            }
            progress.dispose();
            dispose();
        }

        private void sendFiles(ArrayList<String> instances, ServerModel server, File dcmFile) {
            DcmSnd dcmSnd = new DcmSnd("DCMSND");
            dcmSnd.setCalledAET(server.getAeTitle());
            dcmSnd.setRemoteHost(server.getHostName());
            dcmSnd.setRemotePort(server.getPort());
            dcmSnd.setOfferDefaultTransferSyntaxInSeparatePresentationContext(false);
            dcmSnd.setSendFileRef(false);
            dcmSnd.setStorageCommitment(false);
            dcmSnd.setPackPDV(true);
            dcmSnd.setTcpNoDelay(true);
            try {
                for (int i = 0; i < instances.size(); i++) {
                    dcmSnd.addFile(new File(instances.get(i)));
                    progress.update();
                }
            } catch (NullPointerException ex) {  //Single image export                
                dcmSnd.addFile(dcmFile);
                progress.update();
            }
            dcmSnd.configureTransferCapability();
            try {
                dcmSnd.start();
            } catch (IOException ex) {
                Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                dcmSnd.open();
            } catch (IOException ex) {
                Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ConfigurationException ex) {
                Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                progress.setIndeterminate("Please wait...");
                dcmSnd.send();
                dcmSnd.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                dcmSnd.stop();
                progress.setDeterminate();
            }
        }
    }

    private BufferedImage convertToRGB(BufferedImage srcImage) {
        BufferedImage newImage = new BufferedImage(srcImage.getWidth(null),
                srcImage.getHeight(null), BufferedImage.TYPE_INT_BGR);
        Graphics bg = newImage.getGraphics();
        bg.drawImage(srcImage, 0, 0, null);
        bg.dispose();
        return newImage;
    }

    private void exportCurrentImage() {
        progress.setProgressMaximum(1);
        if (saveAsDicomRadio.isSelected()) { //AS DICOM
            exportDcm(selectedCanvas.imgpanel.getDicomFileUrl(), patientNameFile.getAbsolutePath());
        } else { // AS JPEG
            if (exportWithTextOverlayChk.isSelected() || exportWithAnnotationOverlayChk.isSelected()) {
                exportCurrentImageWithOvarlays();
            } else {
                if (!selectedCanvas.imgpanel.isMultiFrame()) {
                    exportJpeg(patientNameFile.getAbsolutePath() + File.separator + selectedCanvas.imgpanel.getInstanceUID(), selectedCanvas.imgpanel.getCurrentbufferedimage());
                } else { //Indicates the current file is Multiframe
                    if (!exportMultiframesAsAVI.isSelected()) { //Export current frame only
                        exportJpeg(patientNameFile.getAbsolutePath() + File.separator + selectedCanvas.imgpanel.getInstanceUID() + "_" + selectedCanvas.imgpanel.getCurrentInstanceNo(), selectedCanvas.imgpanel.getCurrentbufferedimage());
                    } else { //Export all frames as avi
                        BufferedImage[] multiframes = DicomImageReader.readMultiFrames(new File(selectedCanvas.imgpanel.getDicomFileUrl()));
                        int frameRate = (int) (useNativeFrameRateRadio.isSelected() ? selectedCanvas.imgpanel.getFrameRate() : Integer.parseInt(frameRateText.getText().trim()));
                        progress.update();
                        MovieCreator movieCreator = new MovieCreator(multiframes, frameRate, "file://" + patientNameFile + File.separator + selectedCanvas.imgpanel.getInstanceUID() + ".avi");
                    }
                }
            }
        }
    }

    /*
     * Exports the current image with overlays in its native resolution
     */
    private void exportCurrentImageWithOvarlays() {
        LayeredCanvas tempCanvas = new LayeredCanvas(new File(selectedCanvas.imgpanel.getDicomFileUrl()), 0, false);
        if (exportWithAnnotationOverlayChk.isSelected()) { // Export JPEG with Annotaions
            tempCanvas.imgpanel.setCurrentSeriesAnnotation(((ViewerJPanel) selectedCanvas.getParent().getParent().getParent().getParent()).getSeriesAnnotaions(tempCanvas.imgpanel.getSeriesUID()));
        }

        JPanel panel = new JPanel(null);
        panel.add(tempCanvas);

        float fontSize = Math.max(tempCanvas.imgpanel.getColumns(), tempCanvas.imgpanel.getRows()) / 512.0f * 15;
        int hei = (int) (fontSize + fontSize) * 5;

        panel.setBounds(0, 0, tempCanvas.imgpanel.getColumns() + hei, tempCanvas.imgpanel.getRows() + hei);
        tempCanvas.setBounds(0, 0, tempCanvas.imgpanel.getColumns() + hei, tempCanvas.imgpanel.getRows() + hei);
        tempCanvas.canvas.disablePaint();

        if (!tempCanvas.imgpanel.isMultiFrame()) {
            exportJpeg(patientNameFile + File.separator + tempCanvas.imgpanel.getInstanceUID(), getComponentAsImage(panel, tempCanvas, fontSize));
        } else {
            if (exportMultiframesAsAVI.isSelected()) {
                BufferedImage[] images = new BufferedImage[selectedCanvas.imgpanel.getTotalFrames()];
                for (int i = 0; i < images.length; i++) {
                    tempCanvas.imgpanel.displayFrame(i);
                    images[i] = getComponentAsImage(panel, tempCanvas, fontSize);
                }
                int frameRate = (int) (useNativeFrameRateRadio.isSelected() ? tempCanvas.imgpanel.getFrameRate() : Integer.parseInt(frameRateText.getText().trim()));
                MovieCreator movieCreator = new MovieCreator(images, frameRate, "file://" + patientNameFile.getAbsolutePath() + File.separator + tempCanvas.imgpanel.getInstanceUID() + ".avi");
            } else {
                tempCanvas.imgpanel.displayFrame(selectedCanvas.imgpanel.getCurrentInstanceNo());
                exportJpeg(patientNameFile.getAbsolutePath() + File.separator + tempCanvas.imgpanel.getInstanceUID() + "_" + selectedCanvas.imgpanel.getCurrentInstanceNo(), getComponentAsImage(panel, tempCanvas, fontSize));
            }
        }
    }

    private void exportSeries() {
        if (saveAsDicomRadio.isSelected()) { // Save series as DICOM
            ArrayList<String> instances = ApplicationContext.databaseRef.getInstances(selectedCanvas.imgpanel.getStudyUID(), selectedCanvas.imgpanel.getSeriesUID(), null);
            progress.setProgressMaximum(instances.size());
            for (int i = 0; i < instances.size(); i++) {
                exportDcm(instances.get(i), patientNameFile.getAbsolutePath());
                progress.showProgress(i);
            }
        } else { // Save series as JPEG
            if (selectedCanvas.imgpanel.isEncapsulatedDocument()) {
                int i = 1;
                ArrayList<BufferedImage> pdfArray = selectedCanvas.imgpanel.createPDFArray();
                for (BufferedImage b : pdfArray) {
                    exportJpeg(patientNameFile.getAbsolutePath() + File.separator + i, b);
                    i++;
                }
            } else {
                if (!(exportWithTextOverlayChk.isSelected() || exportWithAnnotationOverlayChk.isSelected())) {
                    ArrayList<String> normalInstances = ApplicationContext.databaseRef.getInstances(selectedCanvas.imgpanel.getStudyUID(), selectedCanvas.imgpanel.getSeriesUID(), "false");
                    ArrayList<String> multiframeInstances = ApplicationContext.databaseRef.getInstances(selectedCanvas.imgpanel.getStudyUID(), selectedCanvas.imgpanel.getSeriesUID(), "true");
                    progress.setProgressMaximum(normalInstances.size() + multiframeInstances.size());
                    exportAsJPEGImages(normalInstances, false);
                    if (!exportMultiframesAsAVI.isSelected()) {
                        progress.enableMutliframeProgress();
                        exportAsJPEGImages(multiframeInstances, true);
                    } else {
                        for (int i = 0; i < multiframeInstances.size(); i++) {
                            String filePath = multiframeInstances.get(i);
                            BufferedImage[] images = DicomImageReader.readMultiFrames(new File(filePath));
                            int frameRate = 0;
                            try {
                                if (useNativeFrameRateRadio.isSelected()) {
                                    frameRate = (int) (1000 / Double.parseDouble(DicomImageReader.getCurrentFrameTime()));
                                } else {
                                    frameRate = Integer.parseInt(frameRateText.getText().trim());
                                }
                            } catch (Exception ex) {
                                System.err.println("Invalid frame rate : " + ex.getMessage());
                                frameRate = 20;
                            }
                            progress.update();
                            MovieCreator movieCreator = new MovieCreator(images, frameRate, "file://" + patientNameFile + File.separator + filePath.substring(filePath.lastIndexOf(File.separator) + 1) + ".avi");
                        }
                    }
                } else {
                    exportSeriesWithOverlays();
                }
            }
        }
    }

    /*
     * Exports current series with overlays in image's native resolution
     */
    private void exportSeriesWithOverlays() {
        LayeredCanvas tempCanvas = new LayeredCanvas(new File(ApplicationContext.databaseRef.getFirstInstanceLocation(selectedCanvas.imgpanel.getStudyUID(), selectedCanvas.imgpanel.getSeriesUID())), 0, false);
        if (exportWithAnnotationOverlayChk.isSelected()) { // Export JPEG with Annotaions
            tempCanvas.imgpanel.setCurrentSeriesAnnotation(((ViewerJPanel) selectedCanvas.getParent().getParent().getParent().getParent()).getSeriesAnnotaions(tempCanvas.imgpanel.getSeriesUID()));
        }

        JPanel panel = new JPanel(null);
        panel.add(tempCanvas);

        progress.setProgressMaximum(tempCanvas.imgpanel.getTotalInstance());

        exportSeriesWithOverlays(panel, tempCanvas);
    }

    /*
     * Exports individual series with overlays in image's native resolution
     */
    private void exportSeriesWithOverlays(JPanel panel, LayeredCanvas tempCanvas) {
        float fontSize = Math.max(tempCanvas.imgpanel.getColumns(), tempCanvas.imgpanel.getRows()) / 512.0f * 15;
        int hei = (int) (fontSize + fontSize) * 5;

        panel.setBounds(0, 0, tempCanvas.imgpanel.getColumns() + hei, tempCanvas.imgpanel.getRows() + hei);
        tempCanvas.setBounds(0, 0, tempCanvas.imgpanel.getColumns() + hei, tempCanvas.imgpanel.getRows() + hei);
        tempCanvas.canvas.disablePaint();

        if (!tempCanvas.imgpanel.isMultiFrame()) {
            int inst_Iter = 0;
            exportJpeg(patientNameFile + File.separator + tempCanvas.imgpanel.getInstanceUID(), getComponentAsImage(panel, tempCanvas, fontSize));
            for (inst_Iter = 1; inst_Iter < tempCanvas.imgpanel.getTotalInstance(); inst_Iter++) {
                tempCanvas.imgpanel.showImage(inst_Iter);
                if (exportWithAnnotationOverlayChk.isSelected()) {
                    tempCanvas.imgpanel.setCurrentInstanceAnnotation();
                }
                exportJpeg(patientNameFile + File.separator + tempCanvas.imgpanel.getInstanceUID(), getComponentAsImage(panel, tempCanvas, fontSize));
                progress.update();
            } //End of instance iteration      
        } //End of instance if

        //Multiframe    
        ArrayList<String> multiframeInstances = ApplicationContext.databaseRef.getInstances(tempCanvas.imgpanel.getStudyUID(), tempCanvas.imgpanel.getSeriesUID(), "true");
        for (int multiframe_Iter = 0; multiframe_Iter < multiframeInstances.size(); multiframe_Iter++) {
            tempCanvas.createSubComponents(multiframeInstances.get(multiframe_Iter), 0, false);
            fontSize = Math.max(tempCanvas.imgpanel.getColumns(), tempCanvas.imgpanel.getRows()) / 512.0f * 15;
            hei = (int) (fontSize + fontSize) * 5;

            panel.setBounds(0, 0, tempCanvas.imgpanel.getColumns() + hei, tempCanvas.imgpanel.getRows() + hei);
            tempCanvas.setBounds(0, 0, tempCanvas.imgpanel.getColumns() + hei, tempCanvas.imgpanel.getRows() + hei);

            if (exportWithAnnotationOverlayChk.isSelected()) { // Export JPEG with Annotaions
                tempCanvas.imgpanel.setCurrentSeriesAnnotation(((ViewerJPanel) selectedCanvas.getParent().getParent().getParent().getParent()).getSeriesAnnotaions(tempCanvas.imgpanel.getSeriesUID()));
            }

            if (!exportMultiframesAsAVI.isSelected()) {
                int frame_Iter = 0;
                progress.enableMutliframeProgress();
                exportJpeg(patientNameFile + File.separator + tempCanvas.imgpanel.getInstanceUID() + "_" + frame_Iter, getComponentAsImage(panel, tempCanvas, fontSize));
                progress.setMultiframeMaximum(tempCanvas.imgpanel.getTotalFrames());
                for (frame_Iter = 1; frame_Iter < tempCanvas.imgpanel.getTotalFrames(); frame_Iter++) {
                    tempCanvas.imgpanel.displayFrame(frame_Iter);
                    exportJpeg(patientNameFile + File.separator + tempCanvas.imgpanel.getInstanceUID() + "_" + frame_Iter, getComponentAsImage(panel, tempCanvas, fontSize));
                    progress.showMultiframeProgress(frame_Iter);
                } //End of frame iteration                                
            } else {
                int frame_Iter = 0;
                BufferedImage[] images = new BufferedImage[tempCanvas.imgpanel.getTotalFrames()];
                images[frame_Iter] = getComponentAsImage(panel, tempCanvas, fontSize);

                for (frame_Iter = 1; frame_Iter < images.length; frame_Iter++) {
                    tempCanvas.imgpanel.displayFrame(frame_Iter);
                    images[frame_Iter] = getComponentAsImage(panel, tempCanvas, fontSize);
                }

                if (useNativeFrameRateRadio.isSelected()) {
                    MovieCreator movieCreator = new MovieCreator(images, (int) tempCanvas.imgpanel.getFrameRate(), "file://" + patientNameFile + File.separator + tempCanvas.imgpanel.getInstanceUID() + ".avi");
                } else {
                    int frameRate = 0;
                    try {
                        frameRate = Integer.parseInt(frameRateText.getText().trim());
                    } catch (Exception ex) {
                        System.out.println("Invalid Frame rate : " + ex.getMessage());
                        frameRate = 20;
                    }
                    MovieCreator movieCreator = new MovieCreator(images, frameRate, "file://" + patientNameFile + File.separator + tempCanvas.imgpanel.getInstanceUID() + ".avi");
                }
            }
            progress.update();
        } //End of Multiframe iteration
    }

    /*
     * Export entire study as JPEG With Overlays in image's original resolution
     */
    private void exportStudyWithOverlays() {
        progress.setProgressMaximum(ApplicationContext.databaseRef.getTotalInstances(selectedCanvas.imgpanel.getStudyUID()));
        ArrayList<String> seriesFileURL = ApplicationContext.databaseRef.getSeriesFileURL(selectedCanvas.imgpanel.getStudyUID());
        LayeredCanvas tempCanvas = new LayeredCanvas(new File(seriesFileURL.get(0)), 0, false);

        if (exportWithAnnotationOverlayChk.isSelected()) { // Export JPEG with Annotaions
            tempCanvas.imgpanel.setCurrentSeriesAnnotation(((ViewerJPanel) selectedCanvas.getParent().getParent().getParent().getParent()).getSeriesAnnotaions(tempCanvas.imgpanel.getSeriesUID()));
        }

        JPanel panel = new JPanel(null);
        panel.add(tempCanvas);

        exportSeriesWithOverlays(panel, tempCanvas);

        for (int study_Iter = 1; study_Iter < seriesFileURL.size(); study_Iter++) {
            tempCanvas.createSubComponents(seriesFileURL.get(study_Iter), 0, false);
            if (exportWithAnnotationOverlayChk.isSelected()) { // Export JPEG with Annotaions
                tempCanvas.imgpanel.setCurrentSeriesAnnotation(((ViewerJPanel) selectedCanvas.getParent().getParent().getParent().getParent()).getSeriesAnnotaions(tempCanvas.imgpanel.getSeriesUID()));
            }
            exportSeriesWithOverlays(panel, tempCanvas);
        }
    }

    private void exportStudy() {
        if (saveAsDicomRadio.isSelected()) { //Save as DICOM
            ArrayList<String> instances = ApplicationContext.databaseRef.getInstances(selectedCanvas.imgpanel.getStudyUID(), null, null);
            progress.setProgressMaximum(instances.size());
            for (int i = 0; i < instances.size(); i++) {
                exportDcm(instances.get(i), patientNameFile.getAbsolutePath());
                progress.showProgress(i);
            }
        } else { //Save as JPEG
            if (!(exportWithTextOverlayChk.isSelected() || exportWithAnnotationOverlayChk.isSelected())) {
                ArrayList<String> instances = ApplicationContext.databaseRef.getInstances(selectedCanvas.imgpanel.getStudyUID(), null, "false");
                ArrayList<String> multiframeInstances = ApplicationContext.databaseRef.getInstances(selectedCanvas.imgpanel.getStudyUID(), null, "true");
                progress.setProgressMaximum(instances.size() + multiframeInstances.size());
                exportAsJPEGImages(instances, false);
                if (!exportMultiframesAsAVI.isSelected()) {
                    progress.enableMutliframeProgress();
                    exportAsJPEGImages(multiframeInstances, true);
                } else {
                    for (int i = 0; i < multiframeInstances.size(); i++) {
                        String filePath = multiframeInstances.get(i);
                        BufferedImage[] images = DicomImageReader.readMultiFrames(new File(filePath));
                        int frameRate = 0;
                        try {
                            if (useNativeFrameRateRadio.isSelected()) {
                                frameRate = (int) (1000 / Double.parseDouble(DicomImageReader.getCurrentFrameTime()));
                            } else {
                                frameRate = Integer.parseInt(frameRateText.getText().trim());
                            }
                        } catch (Exception ex) {
                            System.err.println("Invalid frame rate : " + ex.getMessage());
                            frameRate = 20;
                        }
                        MovieCreator movieCreator = new MovieCreator(images, frameRate, "file://" + patientNameFile + File.separator + filePath.substring(filePath.lastIndexOf(File.separator) + 1) + ".avi");
                        progress.update();
                    }
                }
            } else { //Export with Overlays
                exportStudyWithOverlays();
            }

        }
        progress.dispose();
    }

    /*
     * Gives the component with needed overlays
     */
    private BufferedImage getComponentAsImage(JPanel panel, LayeredCanvas tempCanvas, float fontSize) {
        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_BGR);
        Graphics2D g2 = img.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        try {
            panel.update(g2); //Null pointer occurs when the panel could not be updated with children
        } catch (NullPointerException ex) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex1) {
                Logger.getLogger(ExportDicom.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return getComponentAsImage(panel, tempCanvas, fontSize);
        }

        panel.printAll(g2);

        if (exportWithTextOverlayChk.isSelected()) { //Export with Text Overlay

            TextOverlayParam textOverlayParam = tempCanvas.textOverlay.getTextOverlayParam();

            Font font = new Font(Font.SERIF, Font.PLAIN, (int) fontSize);

            g2.setFont(font);
            g2.setColor(Color.WHITE);
            FontMetrics fontMetrics = g2.getFontMetrics(font);
            int height = fontMetrics.getHeight();

            //Left Top Text
            g2.drawString(textOverlayParam.getPatientName(), 5, height * 2);
            g2.drawString(textOverlayParam.getPatientID(), 5, height * 3);
            g2.drawString(textOverlayParam.getSex(), 5, height * 4);

            //Left Bottom Text            
            g2.drawString(textOverlayParam.getInstanceNoTxt(), 5, tempCanvas.getHeight() - height * 3);
            g2.drawString(textOverlayParam.getWindowingTxt(), 5, tempCanvas.getHeight() - height * 2);
            g2.drawString(textOverlayParam.getSlicePosition(), 5, tempCanvas.getHeight() - height);

            //Right Top Text
            g2.drawString(textOverlayParam.getInstitutionName(), tempCanvas.getWidth() - fontMetrics.stringWidth(textOverlayParam.getInstitutionName()) - 2, height * 2);
            g2.drawString(textOverlayParam.getStudyDate(), tempCanvas.getWidth() - fontMetrics.stringWidth(textOverlayParam.getStudyDate()) - 2, height * 3);
            g2.drawString(textOverlayParam.getStudyDescription(), tempCanvas.getWidth() - fontMetrics.stringWidth(textOverlayParam.getStudyDescription()) - 2, height * 4);
            g2.drawString(textOverlayParam.getSeriesDescription(), tempCanvas.getWidth() - fontMetrics.stringWidth(textOverlayParam.getSeriesDescription()) - 2, height * 5);

            //Right Bottom Text
            g2.drawString(textOverlayParam.getImageSize(), tempCanvas.getWidth() - fontMetrics.stringWidth(textOverlayParam.getImageSize()) - 2, tempCanvas.getHeight() - height * 3);
            g2.drawString(textOverlayParam.getZoomLevel(), tempCanvas.getWidth() - fontMetrics.stringWidth(textOverlayParam.getZoomLevel()) - 2, tempCanvas.getHeight() - height * 2);
            g2.drawString(textOverlayParam.getPatientPosition(), tempCanvas.getWidth() - fontMetrics.stringWidth(textOverlayParam.getPatientPosition()) - 2, tempCanvas.getHeight() - height);

            if (textOverlayParam.isMultiframe()) { //Export Multiframes
                g2.setColor(Color.YELLOW);
                String multiframeTxt = ApplicationContext.currentBundle.getString("ImageView.textOverlay.multiframeLabel.text");
                g2.fillRect(tempCanvas.getWidth() - fontMetrics.stringWidth(multiframeTxt) - 2, tempCanvas.getHeight() - height * 5 + 5, fontMetrics.stringWidth(multiframeTxt) + 2, height);
                g2.setColor(Color.RED);
                g2.drawString(multiframeTxt, tempCanvas.getWidth() - fontMetrics.stringWidth(multiframeTxt) - 2, tempCanvas.getHeight() - height * 4);
            }
        }

        g2.dispose();
        return img;
    }

    private class MovieCreator implements ControllerListener, DataSinkListener {

        int width = 512, height = 512, frameRate = 10;
        Object waitSync = new Object();
        boolean stateTransitionOK = true;
        Object waitFileSync = new Object();
        boolean fileDone = false;
        boolean fileSuccess = true;
        String outputURL = null;
        BufferedImage[] images = null;

        public MovieCreator(BufferedImage[] images, int frameRate, String outputURL) {
            this.images = images;
            this.frameRate = frameRate;
            this.outputURL = outputURL;
            this.width = images[0].getWidth();
            this.height = images[1].getHeight();

            // Generate the output media locators.
            MediaLocator oml;

            if ((oml = createMediaLocator(outputURL)) == null) {
                System.err.println("Cannot build media locator from: " + outputURL);
                System.exit(0);
            }
            doIt(oml);
        }

        @Override
        public void controllerUpdate(ControllerEvent evt) {
            if (evt instanceof ConfigureCompleteEvent
                    || evt instanceof RealizeCompleteEvent
                    || evt instanceof PrefetchCompleteEvent) {
                synchronized (waitSync) {
                    stateTransitionOK = true;
                    waitSync.notifyAll();
                }
            } else if (evt instanceof ResourceUnavailableEvent) {
                synchronized (waitSync) {
                    stateTransitionOK = false;
                    waitSync.notifyAll();
                }
            } else if (evt instanceof EndOfMediaEvent) {
                evt.getSourceController().stop();
                evt.getSourceController().close();
            }
        }

        @Override
        public void dataSinkUpdate(DataSinkEvent evt) {
            if (evt instanceof EndOfStreamEvent) {
                synchronized (waitFileSync) {
                    fileDone = true;
                    waitFileSync.notifyAll();
                }
            } else if (evt instanceof DataSinkErrorEvent) {
                synchronized (waitFileSync) {
                    fileDone = true;
                    fileSuccess = false;
                    waitFileSync.notifyAll();
                }
            }
        }

        /**
         * Block until the processor has transitioned to the given state. Return
         * false if the transition failed.
         */
        boolean waitForState(Processor p, int state) {
            synchronized (waitSync) {
                try {
                    while (p.getState() < state && stateTransitionOK) {
                        waitSync.wait();
                    }
                } catch (Exception e) {
                    System.out.println("Exception waitForState(Processor p,int state) : " + e.getMessage());
                }
            }
            return stateTransitionOK;
        }

        /**
         * Block until file writing is done.
         */
        boolean waitForFileDone() {
            synchronized (waitFileSync) {
                try {
                    while (!fileDone) {
                        waitFileSync.wait();
                    }
                } catch (Exception e) {
                }
            }
            return fileSuccess;
        }

        /**
         * Create the DataSink.
         */
        DataSink createDataSink(Processor p, MediaLocator outML) {

            DataSource ds;

            if ((ds = p.getDataOutput()) == null) {
                System.err
                        .println("Something is really wrong: the processor does not have an output DataSource");
                return null;
            }

            DataSink dsink;

            try {
                System.err.println("- create DataSink for: " + outML);
                dsink = Manager.createDataSink(ds, outML);
                dsink.open();
            } catch (Exception e) {
                System.err.println("Cannot create the DataSink: " + e);
                return null;
            }

            return dsink;
        }

        class ImageDataSource extends PullBufferDataSource {

            ImageSourceStream streams[];

            ImageDataSource(int width, int height, int frameRate, int nFrames) {
                streams = new ImageSourceStream[1];
                streams[0] = new ImageSourceStream(width, height, frameRate, nFrames);
            }

            @Override
            public void setLocator(MediaLocator source) {
            }

            @Override
            public MediaLocator getLocator() {
                return null;
            }

            @Override
            public PullBufferStream[] getStreams() {
                return streams;
            }

            @Override
            public String getContentType() {
                return ContentDescriptor.RAW;
            }

            @Override
            public void connect() throws IOException {
            }

            @Override
            public void disconnect() {
            }

            @Override
            public void start() throws IOException {
            }

            @Override
            public void stop() throws IOException {
            }

            @Override
            public Object getControl(String string) {
                return null;
            }

            @Override
            public Object[] getControls() {
                return new Object[0];
            }

            @Override
            public Time getDuration() {
                return DURATION_UNKNOWN;
            }
        }

        class ImageSourceStream implements PullBufferStream {

            int width, height, frameRate;
            VideoFormat format;
            boolean ended = false;
            int nFrames;
            int position = 0;

            public ImageSourceStream(int width, int height, int frameRate, int nFrames) {
                this.width = width;
                this.height = height;
                this.frameRate = frameRate;
                this.nFrames = nFrames;
                format = new VideoFormat(VideoFormat.JPEG, new Dimension(width,
                        height), Format.NOT_SPECIFIED, Format.byteArray,
                        (float) frameRate);
            }

            @Override
            public boolean willReadBlock() {
                return false;
            }

            @Override
            public void read(Buffer buf) throws IOException {
                if (position >= images.length) {
                    // We are done. Set EndOfMedia.
                    System.err.println("Done reading all images.");
                    buf.setEOM(true);
                    buf.setOffset(0);
                    buf.setLength(0);
                    ended = true;
                    return;
                }
                byte data[] = null;

                // Check the input buffer type & size.
                if (buf.getData() instanceof byte[]) {
                    data = (byte[]) buf.getData();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(images[position], "jpeg", baos);
                baos.flush();
                byte[] imageData = baos.toByteArray();
                baos.close();
                position++;

                if (data == null || data.length < imageData.length) {
                    data = new byte[(int) imageData.length];
                    buf.setData(data);
                }

                System.arraycopy(imageData, 0, data, 0, imageData.length);

                buf.setOffset(0);
                buf.setLength((int) imageData.length);
                buf.setFormat(format);
                buf.setFlags(buf.getFlags() | Buffer.FLAG_KEY_FRAME);
            }

            @Override
            public Format getFormat() {
                return format;
            }

            @Override
            public ContentDescriptor getContentDescriptor() {
                return new ContentDescriptor(ContentDescriptor.RAW);
            }

            @Override
            public long getContentLength() {
                return 0;
            }

            @Override
            public boolean endOfStream() {
                return ended;
            }

            @Override
            public Object[] getControls() {
                return new Object[0];
            }

            @Override
            public Object getControl(String string) {
                return null;
            }
        }

        private MediaLocator createMediaLocator(String url) {
            MediaLocator ml;

            if (url.indexOf(":") > 0 && (ml = new MediaLocator(url)) != null) {
                return ml;
            }

            if (url.startsWith(File.separator)) {
                if ((ml = new MediaLocator("file:" + url)) != null) {
                    return ml;
                }
            } else {
                String file = "file:" + System.getProperty("user.dir")
                        + File.separator + url;
                if ((ml = new MediaLocator(file)) != null) {
                    return ml;
                }
            }
            return null;
        }

        private boolean doIt(MediaLocator outML) {
            ImageDataSource ids = new ImageDataSource(width, height, frameRate, images.length);
            Processor p;
            try {
                System.err.println("- create processor for the image datasource ...");
                p = Manager.createProcessor(ids);
            } catch (Exception e) {
                System.err.println("Cannot create a processor from the data source : " + e.getMessage());
                return false;
            }

            p.addControllerListener(this);
            // Put the Processor into configured state so we can set
            // some processing options on the processor.
            p.configure();
            if (!waitForState(p, Processor.Configured)) {
                System.err.println("Failed to configure the processor.");
                return false;
            }
            // Set the output content descriptor to QuickTime.
            p.setContentDescriptor(new ContentDescriptor(
                    FileTypeDescriptor.QUICKTIME));// FileTypeDescriptor.MSVIDEO
            // Query for the processor for supported formats.
            // Then set it on the processor.
            TrackControl tcs[] = p.getTrackControls();
            Format f[] = tcs[0].getSupportedFormats();
            if (f == null || f.length <= 0) {
                System.err.println("The mux does not support the input format: "
                        + tcs[0].getFormat());
                return false;
            }

            tcs[0].setFormat(f[0]);

            System.err.println("Setting the track format to: " + f[0]);

            // We are done with programming the processor. Let's just
            // realize it.
            p.realize();
            if (!waitForState(p, Controller.Realized)) {
                System.err.println("Failed to realize the processor.");
                return false;
            }
            // Now, we'll need to create a DataSink.
            DataSink dsink;
            if ((dsink = createDataSink(p, outML)) == null) {
                System.err
                        .println("Failed to create a DataSink for the given output MediaLocator: "
                                + outML);
                return false;
            }

            dsink.addDataSinkListener(this);
            fileDone = false;

            System.err.println("start processing...");
            // OK, we can now start the actual transcoding.
            try {
                p.start();
                dsink.start();
            } catch (IOException e) {
                System.err.println("IO error during processing");
                return false;
            }

            // Wait for EndOfStream event.
            waitForFileDone();

            // Cleanup.
            try {
                dsink.close();
            } catch (Exception e) {
            }
            p.removeControllerListener(this);

            System.err.println("...done processing.");

            return true;
        }
    }
}
