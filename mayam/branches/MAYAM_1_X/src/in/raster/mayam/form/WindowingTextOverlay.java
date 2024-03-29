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
import in.raster.mayam.param.TextOverlayParam;
import java.awt.Graphics;

/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class WindowingTextOverlay extends javax.swing.JPanel {

    /** Creates new form DateFormatPanel */
    private TextOverlayParam textOverlayParam;
    WindowingLayeredCanvas layeredCanvas;
    private boolean firstTime = true;
    private boolean textOverlay = true;
    private boolean showProbeFlag = false;

    public WindowingTextOverlay(WindowingLayeredCanvas l) {
        initComponents();
        textOverlayParam = new TextOverlayParam();
        this.setOpaque(false);
        layeredCanvas = l;
    }

    public void setWindowingParameter(String WL, String WW) {
        textOverlayParam.setWindowingParameter(WL, WW);
        this.repaint();
    }

    public TextOverlayParam getTextOverlayParam() {
        return textOverlayParam;
    }

    public void setTextOverlayParam(TextOverlayParam textOverlayParam) {
        this.textOverlayParam = textOverlayParam;
    }

    public void doTextOverlay() {
        if (textOverlay) {
            textOverlay = false;
            this.repaint();
        } else {
            textOverlay = true;
            this.repaint();
        }
    }

    public void updateCurrentInstanceNo(int currentInstanceNo) {
        textOverlayParam.setCurrentInstance(currentInstanceNo);
        this.repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        patientNameLabel = new javax.swing.JLabel();
        patientSexLabel = new javax.swing.JLabel();
        patientIDLabel = new javax.swing.JLabel();
        studyTimeLabel = new javax.swing.JLabel();
        studyDateLabel = new javax.swing.JLabel();
        institutionLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        patientPositionLabel = new javax.swing.JLabel();
        instanceNoLabel = new javax.swing.JLabel();
        windowingLabel = new javax.swing.JLabel();
        slicePositionLabel = new javax.swing.JLabel();
        frameNumberText = new javax.swing.JLabel();
        multiframeStatusText = new javax.swing.JLabel();

        patientNameLabel.setBackground(new java.awt.Color(0, 0, 0));
        patientNameLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        patientNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        patientNameLabel.setText("Name");

        patientSexLabel.setBackground(new java.awt.Color(0, 0, 0));
        patientSexLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        patientSexLabel.setForeground(new java.awt.Color(255, 255, 255));
        patientSexLabel.setText("Sex");

        patientIDLabel.setBackground(new java.awt.Color(0, 0, 0));
        patientIDLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        patientIDLabel.setForeground(new java.awt.Color(255, 255, 255));
        patientIDLabel.setText("ID");

        studyTimeLabel.setBackground(new java.awt.Color(0, 0, 0));
        studyTimeLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        studyTimeLabel.setForeground(new java.awt.Color(255, 255, 255));
        studyTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        studyTimeLabel.setText("Study Time");

        studyDateLabel.setBackground(new java.awt.Color(0, 0, 0));
        studyDateLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        studyDateLabel.setForeground(new java.awt.Color(255, 255, 255));
        studyDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        studyDateLabel.setText("Study Date");

        institutionLabel.setBackground(new java.awt.Color(0, 0, 0));
        institutionLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        institutionLabel.setForeground(new java.awt.Color(255, 255, 255));
        institutionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        institutionLabel.setText("Institution");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        patientPositionLabel.setBackground(new java.awt.Color(0, 0, 0));
        patientPositionLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        patientPositionLabel.setForeground(new java.awt.Color(255, 255, 255));
        patientPositionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        patientPositionLabel.setText("Patient Position");

        instanceNoLabel.setBackground(new java.awt.Color(0, 0, 0));
        instanceNoLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        instanceNoLabel.setForeground(new java.awt.Color(255, 255, 255));
        instanceNoLabel.setText("Im");

        windowingLabel.setBackground(new java.awt.Color(0, 0, 0));
        windowingLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        windowingLabel.setForeground(new java.awt.Color(255, 255, 255));
        windowingLabel.setText("Windowing");

        slicePositionLabel.setBackground(new java.awt.Color(0, 0, 0));
        slicePositionLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        slicePositionLabel.setForeground(new java.awt.Color(255, 255, 255));
        slicePositionLabel.setText("Slice Position");

        frameNumberText.setForeground(java.awt.Color.white);
        frameNumberText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        frameNumberText.setText("Frame");

        multiframeStatusText.setBackground(new java.awt.Color(255, 255, 153));
        multiframeStatusText.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        multiframeStatusText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        multiframeStatusText.setText("Multiframe");
        multiframeStatusText.setOpaque(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(patientNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(patientIDLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(windowingLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .add(slicePositionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .add(instanceNoLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, frameNumberText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 147, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, patientPositionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, multiframeStatusText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(151, Short.MAX_VALUE)
                .add(studyTimeLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(152, Short.MAX_VALUE)
                .add(studyDateLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(300, 300, 300)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel8)
                    .add(layout.createSequentialGroup()
                        .add(institutionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(patientSexLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(286, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(institutionLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(studyDateLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(studyTimeLabel))
                    .add(layout.createSequentialGroup()
                        .add(patientNameLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(patientIDLabel)
                        .add(5, 5, 5)
                        .add(patientSexLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel8)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 40, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(instanceNoLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(windowingLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(slicePositionLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(multiframeStatusText)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(frameNumberText)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(patientPositionLabel)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {patientSexLabel, studyTimeLabel}, org.jdesktop.layout.GroupLayout.VERTICAL);

        layout.linkSize(new java.awt.Component[] {instanceNoLabel, slicePositionLabel, windowingLabel}, org.jdesktop.layout.GroupLayout.VERTICAL);

    }// </editor-fold>//GEN-END:initComponents

    private void setTextOverlay() {
        patientNameLabel.setText(" " + textOverlayParam.getPatientName());
        patientIDLabel.setText(" " + "ID: " + textOverlayParam.getPatientID());
        if (textOverlayParam.getSex() != null && !textOverlayParam.getSex().equalsIgnoreCase("")) {
            patientSexLabel.setText(" Sex: " + textOverlayParam.getSex());
        } else {
            patientSexLabel.setText("");
        }
        patientPositionLabel.setText("Position:" + textOverlayParam.getPatientPosition() + "");
        studyDateLabel.setText("Study Date: " + textOverlayParam.getStudyDate() + " ");
        //studyTimeLabel.setText("Study Time: " + textOverlayParam.getStudyTime() + " ");
        slicePositionLabel.setText(" " + "Slice pos: " + textOverlayParam.getSlicePosition());
        int currentInstanceNo;
        if (!ApplicationContext.databaseRef.getMultiframeStatus()) {
            currentInstanceNo = (textOverlayParam.getCurrentInstance() != 0) ? textOverlayParam.getCurrentInstance() : 1;
        } else {
            currentInstanceNo = textOverlayParam.getCurrentInstance() + 1;
        }
        int totalNo = Integer.parseInt(textOverlayParam.getTotalInstance());
        instanceNoLabel.setText(" " + "Im :" + currentInstanceNo + "/" + totalNo);
        institutionLabel.setText(textOverlayParam.getInstitutionName() + " ");
        windowingLabel.setText(" " + "W " + textOverlayParam.getWindowWidth() + "/ C " + textOverlayParam.getWindowLevel());
        if (!textOverlayParam.getFramePosition().equalsIgnoreCase("")) {
            frameNumberText.setText("Frame: " + textOverlayParam.getFramePosition());
        } else {
            frameNumberText.setText("");
        }
    }

    private void setTextOverlayToNull() {
        patientNameLabel.setText("");
        patientIDLabel.setText("");
        patientSexLabel.setText("");
        patientPositionLabel.setText("");
        studyDateLabel.setText("");
        studyTimeLabel.setText("");
        slicePositionLabel.setText("");
        instanceNoLabel.setText("");
        institutionLabel.setText("");
        windowingLabel.setText("");

    }

    public void resizeHandler() {
        this.firstTime = true;
        this.repaint();
    }

    public void multiframeStatusDisplay(boolean status) {
        multiframeStatusText.setVisible(status);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (textOverlay) {
            setTextOverlay();
        } else {
            setTextOverlayToNull();
        }
        if (firstTime) {
            this.setSize(layeredCanvas.getSize().width, layeredCanvas.getSize().height);
            firstTime = false;
            setTextOverlayToNull();
            repaint();
        }
        showProbeFlag = false;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel frameNumberText;
    private javax.swing.JLabel instanceNoLabel;
    private javax.swing.JLabel institutionLabel;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel multiframeStatusText;
    private javax.swing.JLabel patientIDLabel;
    private javax.swing.JLabel patientNameLabel;
    private javax.swing.JLabel patientPositionLabel;
    private javax.swing.JLabel patientSexLabel;
    private javax.swing.JLabel slicePositionLabel;
    private javax.swing.JLabel studyDateLabel;
    private javax.swing.JLabel studyTimeLabel;
    private javax.swing.JLabel windowingLabel;
    // End of variables declaration//GEN-END:variables
}
