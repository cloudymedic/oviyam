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

/**
 *
 * @author Devishree
 * @version 2.2
 */
public class ProgressBar extends javax.swing.JDialog {

    /**
     * Creates new form ProgressBar
     */
    public ProgressBar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public ProgressBar(java.awt.Frame parent, boolean modal, String progress1Text, String progress2Text) {
        super(parent, modal);
        initComponents();
        progressLbl1.setText(progress1Text);
        progressLbl2.setText(progress2Text);

        jProgressBar1.setValue(0);
        jProgressBar1.setMaximum(0);

        progressPanel2.setVisible(false);
        setSize(getWidth(), progressPanel2.getY());
    }

    public void setProgressMaximum(int max) {
        jProgressBar1.setMaximum(max);
    }

    public void showProgress(int i) {
        jProgressBar1.setValue(i);
        jProgressBar1.repaint();
        validate();
    }

    public void update() {
        jProgressBar1.setValue(jProgressBar1.getValue() + 1);
    }

    public void setProgressText(String text) {
        progressLbl1.setText(text);
    }

    public void setIndeterminate(String text) {
        jProgressBar1.setIndeterminate(true);
        jProgressBar1.setString(text);
    }
    
    public void setDeterminate() {
        jProgressBar1.setIndeterminate(false);
        jProgressBar1.setString("");
    }

    public void enableMutliframeProgress() {
        progressPanel2.setVisible(true);
        setSize(getWidth(), progressPanel2.getY() + progressPanel1.getHeight() + 20);
        validate();
        repaint();
    }

    public void setMultiframeMaximum(int max) {
        jProgressBar2.setMaximum(max);
    }

    public void showMultiframeProgress(int i) {
        jProgressBar2.setValue(i);
    }

    public void endProgress() {
        for (int i = jProgressBar1.getValue(); i <= jProgressBar1.getMaximum(); i++) {
            jProgressBar1.setValue(i);
            jProgressBar1.repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressPanel1 = new javax.swing.JPanel();
        progressLbl1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        progressPanel2 = new javax.swing.JPanel();
        progressLbl2 = new javax.swing.JLabel();
        jProgressBar2 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        progressPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        progressLbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        progressLbl1.setText("jLabel1");

        jProgressBar1.setBackground(new java.awt.Color(124, 129, 141));
        jProgressBar1.setForeground(new java.awt.Color(175, 125, 105));
        jProgressBar1.setPreferredSize(new java.awt.Dimension(148, 25));
        jProgressBar1.setStringPainted(true);

        javax.swing.GroupLayout progressPanel1Layout = new javax.swing.GroupLayout(progressPanel1);
        progressPanel1.setLayout(progressPanel1Layout);
        progressPanel1Layout.setHorizontalGroup(
            progressPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(progressPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressLbl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE))
                .addContainerGap())
        );
        progressPanel1Layout.setVerticalGroup(
            progressPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressLbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        progressPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        progressLbl2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        progressLbl2.setText("jLabel1");

        jProgressBar2.setBackground(new java.awt.Color(124, 129, 141));
        jProgressBar2.setForeground(new java.awt.Color(175, 125, 105));
        jProgressBar2.setPreferredSize(new java.awt.Dimension(148, 25));
        jProgressBar2.setStringPainted(true);

        javax.swing.GroupLayout progressPanel2Layout = new javax.swing.GroupLayout(progressPanel2);
        progressPanel2.setLayout(progressPanel2Layout);
        progressPanel2Layout.setHorizontalGroup(
            progressPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(progressPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        progressPanel2Layout.setVerticalGroup(
            progressPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(progressPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressLbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(progressPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JLabel progressLbl1;
    private javax.swing.JLabel progressLbl2;
    private javax.swing.JPanel progressPanel1;
    private javax.swing.JPanel progressPanel2;
    // End of variables declaration//GEN-END:variables
}