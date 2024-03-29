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
package in.raster.mayam.form.dialog;

import in.raster.mayam.context.ApplicationContext;
import in.raster.mayam.model.AEModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class ServerListDialog extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    //public String keyName = "";
    private AEModel ae = null;

    /** Creates new form AEManagementDialog */
    public ServerListDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        updateTreeModel();
    }

    public AEModel getAe() {
        return ae;
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    public void updateTreeModel() {
        Object Root[] = ApplicationContext.databaseRef.retrieveAEList();
        DefaultMutableTreeNode root = addServerNameToTree(Root);
        DefaultTreeModel tm = new DefaultTreeModel(root);
        serversInfoTree.setModel(tm);
        serversInfoTree.setRootVisible(false);
        serversInfoTree.revalidate();
        serversInfoTree.setSelectionRow(1);
    }

    public DefaultMutableTreeNode addServerNameToTree(Object[] obj) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode serverName;
        Object temp[];
        for (int i = 0; i < obj.length; i++) {
            temp = (Object[]) obj[i];
            serverName = new DefaultMutableTreeNode(temp[0]);
            rootNode.add(serverName);
        }
        return rootNode;
    }

    public DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode("");
        DefaultMutableTreeNode child;
        for (int i = 0; i < hierarchy.length; i++) {
            Object nodeSpecifier = hierarchy[i];
            if (nodeSpecifier instanceof Object[]) // Ie node with children
            {
                child = processHierarchy((Object[]) nodeSpecifier);
            } else {
                child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
            }
            tempNode.add(child);
        }
        return (tempNode);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        serversInfoTree = new javax.swing.JTree();

        setTitle("Dicom Nodes");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(serversInfoTree);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 202, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(okButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(cancelButton)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {cancelButton, okButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(okButton)
                    .add(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param evt-action performed event
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) serversInfoTree.getLastSelectedPathComponent();
        if (node != null) {
            ae = ApplicationContext.databaseRef.getServerDetail(node.getUserObject().toString());
            doClose(RET_OK);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    /**
     *
     * @param evt-action performed event
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog
    /**
     * This routine used to check the status of the dicom aetitle
     * @param evt
     */
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ServerListDialog dialog = new ServerListDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JTree serversInfoTree;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
