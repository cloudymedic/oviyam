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
import in.raster.mayam.models.ServerModel;
import in.raster.mayam.models.combo.TransferSyntaxModel;
import java.awt.event.KeyEvent;

/**
 *
 * @author BabuHussain
 * @version 0.5
 *
 */
public class WadoInformation extends javax.swing.JDialog {

    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    private ServerModel serverModel;
    private int returnStatus = RET_CANCEL;

    /**
     * Creates new form WadoInformation
     */
    public WadoInformation(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Wado Information");
        initComponents();
    }

    public WadoInformation(java.awt.Frame parent, boolean modal, ServerModel serverModel) {
        super(parent, modal);
        setTitle("Wado Information");
        initComponents();
        this.serverModel = serverModel;
        updateWadoInformation();
    }

    private String[] getRetrieveTransferSyntaxArray() {
        String[] retrieveSyntaxArray = new String[]{"Explicit VR Little Endian", "Implicit VR Little Endian", "Original Syntax"};
        return retrieveSyntaxArray;
    }

    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        wadoUrlTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        wadoPortTxt = new javax.swing.JTextField();
        wadoProtocolChk = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        retrieveSyntaxCombo = new javax.swing.JComboBox();
        cancelBtn = new javax.swing.JButton();
        okBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(ApplicationContext.currentBundle.getString("WadoInformation.title.text")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                WindowClosing(evt);
            }
        });

        jLabel1.setText(ApplicationContext.currentBundle.getString("WadoInformation.wadoUrlLabel.text")); // NOI18N

        jLabel2.setText(ApplicationContext.currentBundle.getString("WadoInformation.wadoPortLabel.text")); // NOI18N

        wadoProtocolChk.setText("https");

        jLabel3.setText(ApplicationContext.currentBundle.getString("WadoInformation.encryptionLabel.text")); // NOI18N

        jLabel4.setText(ApplicationContext.currentBundle.getString("WadoInformation.retrieveSyntaxLabel.text")); // NOI18N

        retrieveSyntaxCombo.setModel(new TransferSyntaxModel(getRetrieveTransferSyntaxArray()));

        cancelBtn.setText(ApplicationContext.currentBundle.getString("WadoInformation.cancelButton.text")); // NOI18N
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        cancelBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cancelBtnKeyPressed(evt);
            }
        });

        okBtn.setText(ApplicationContext.currentBundle.getString("WadoInformation.okButton.text")); // NOI18N
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });
        okBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                okBtnKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(wadoUrlTxt)
                            .addComponent(wadoPortTxt)
                            .addComponent(wadoProtocolChk)
                            .addComponent(retrieveSyntaxCombo, 0, 220, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelBtn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(wadoUrlTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(wadoPortTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(wadoProtocolChk))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(retrieveSyntaxCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn)
                    .addComponent(okBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void WindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowClosing
        closeDialog(evt);
    }//GEN-LAST:event_WindowClosing

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
        storeWadoInformation();
        doClose(RET_OK);
    }//GEN-LAST:event_okBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void okBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_okBtnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            storeWadoInformation();
            doClose(RET_OK);
        }
    }//GEN-LAST:event_okBtnKeyPressed

    private void cancelBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cancelBtnKeyPressed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelBtnKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton okBtn;
    private javax.swing.JComboBox retrieveSyntaxCombo;
    private javax.swing.JTextField wadoPortTxt;
    private javax.swing.JCheckBox wadoProtocolChk;
    private javax.swing.JTextField wadoUrlTxt;
    // End of variables declaration//GEN-END:variables

    private void updateWadoInformation() {
        wadoUrlTxt.setText(serverModel.getWadoURL());
        wadoPortTxt.setText(String.valueOf(serverModel.getWadoPort()));
        if (serverModel.getWadoProtocol().equals("https")) {
            wadoProtocolChk.setSelected(true);
        }
        if (!serverModel.getRetrieveTransferSyntax().equalsIgnoreCase("")) {
            retrieveSyntaxCombo.getModel().setSelectedItem(serverModel.getRetrieveTransferSyntax());
        }
    }

    public void storeWadoInformation() {
        String wadoProtocol = this.wadoProtocolChk.isSelected() ? "https" : "http";
        serverModel.setWadoContextPath(this.wadoUrlTxt.getText());
        try {
            serverModel.setWadoPort(Integer.parseInt(wadoPortTxt.getText()));
        } catch (NumberFormatException e) {
            serverModel.setWadoPort(0);
        }
        serverModel.setWadoProtocol(wadoProtocol);
        serverModel.setRetrieveTransferSyntax((String) this.retrieveSyntaxCombo.getSelectedItem());
    }

    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose(RET_CANCEL);
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        dispose();
    }
}