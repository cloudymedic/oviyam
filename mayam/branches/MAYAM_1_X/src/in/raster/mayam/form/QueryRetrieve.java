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
import in.raster.mayam.delegate.CgetDelegate;
import in.raster.mayam.delegate.DicomServerDelegate;
import in.raster.mayam.delegate.EchoService;
import in.raster.mayam.delegate.QueryService;
import in.raster.mayam.delegate.MoveDelegate;
import in.raster.mayam.delegate.WadoRetrieveDelegate;
import in.raster.mayam.form.dialog.EchoStatus;
import in.raster.mayam.form.display.Display;
import in.raster.mayam.model.AEModel;
import in.raster.mayam.model.StudyModel;
import in.raster.mayam.model.table.ServerTableModel;
import in.raster.mayam.model.table.StudyListModel;
import in.raster.mayam.param.QueryParam;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import org.dcm4che.data.Dataset;
import org.dcm4che.util.DcmURL;
import in.raster.mayam.model.table.renderer.CellRenderer;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class QueryRetrieve extends javax.swing.JFrame implements ServerChangeListener, ListSelectionListener {

    /** Creates new form QueryRetrieve */
    public QueryRetrieve() {
        initComponents();
        refreshModels();
        addSearchDateitemListener();
        addModalityitemListener();
    }

    private void addSearchDateitemListener() {
        SearchDaysHandler searchDaysHandler = new SearchDaysHandler();
        betweenRadio.addItemListener(searchDaysHandler);
        lastmonthRadio.addItemListener(searchDaysHandler);
        lastweekRadio.addItemListener(searchDaysHandler);
        yesterdayRadio.addItemListener(searchDaysHandler);
        todayRadio.addItemListener(searchDaysHandler);
        anydateRadio.addItemListener(searchDaysHandler);
    }

    private void addModalityitemListener() {
        ModalityHandler modalityHandler = new ModalityHandler();
        ctCheckBox.addItemListener(modalityHandler);
        mrCheckBox.addItemListener(modalityHandler);
        xaCheckBox.addItemListener(modalityHandler);
        crCheckBox.addItemListener(modalityHandler);
        scCheckBox.addItemListener(modalityHandler);
        nmCheckBox.addItemListener(modalityHandler);
        rfCheckBox.addItemListener(modalityHandler);
        dxCheckBox.addItemListener(modalityHandler);
        pxCheckBox.addItemListener(modalityHandler);
        usCheckBox.addItemListener(modalityHandler);
        otCheckBox.addItemListener(modalityHandler);
        drCheckBox.addItemListener(modalityHandler);
        srCheckBox.addItemListener(modalityHandler);
        mgCheckBox.addItemListener(modalityHandler);
        rgCheckBox.addItemListener(modalityHandler);
    }

    public void refreshModels() {
        setServerTableModel();
        setServerName();
        setSpinnerDateModel();
    }

    private void setServerTableModel() {
        ServerTableModel serverTableModel = new ServerTableModel();
        serverTableModel.setEditable(false);
        serverTableModel.setData(ApplicationContext.databaseRef.getServerList());
        serverListTable.setModel(serverTableModel);
        serverListTable.getSelectionModel().addListSelectionListener(this);
        serverListTable.getColumnModel().getSelectionModel().addListSelectionListener(this);
        if (this.serverListTable.getRowCount() > 0) {
            serverListTable.setRowSelectionInterval(0, 0);
        }
    }

    private void setSpinnerDateModel() {
        SpinnerDateModel spm1 = new SpinnerDateModel();
        SpinnerDateModel spm2 = new SpinnerDateModel();
        SpinnerDateModel spm3 = new SpinnerDateModel();
        fromSpinner.setModel(spm1);
        fromSpinner.setEditor(new JSpinner.DateEditor(fromSpinner, "dd/MM/yyyy"));
        toSpinner.setModel(spm2);
        toSpinner.setEditor(new JSpinner.DateEditor(toSpinner, "dd/MM/yyyy"));
        birthDateSpinner.setModel(spm3);
        birthDateSpinner.setEditor(new JSpinner.DateEditor(birthDateSpinner, "dd/MM/yyyy"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchDaysGroup = new javax.swing.ButtonGroup();
        modalityGroup = new javax.swing.ButtonGroup();
        jPanel9 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        anydateRadio = new javax.swing.JRadioButton();
        todayRadio = new javax.swing.JRadioButton();
        yesterdayRadio = new javax.swing.JRadioButton();
        lastweekRadio = new javax.swing.JRadioButton();
        lastmonthRadio = new javax.swing.JRadioButton();
        betweenRadio = new javax.swing.JRadioButton();
        fromSpinner = new javax.swing.JSpinner();
        toSpinner = new javax.swing.JSpinner();
        jPanel10 = new javax.swing.JPanel();
        birthDateSpinner = new javax.swing.JSpinner();
        patientNameLabel = new javax.swing.JLabel();
        patientNameText = new javax.swing.JTextField();
        patientIDLabel = new javax.swing.JLabel();
        patientIDText = new javax.swing.JTextField();
        accessionLabel = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        dobLabel = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        ctCheckBox = new javax.swing.JCheckBox();
        mrCheckBox = new javax.swing.JCheckBox();
        xaCheckBox = new javax.swing.JCheckBox();
        crCheckBox = new javax.swing.JCheckBox();
        scCheckBox = new javax.swing.JCheckBox();
        nmCheckBox = new javax.swing.JCheckBox();
        rfCheckBox = new javax.swing.JCheckBox();
        dxCheckBox = new javax.swing.JCheckBox();
        pxCheckBox = new javax.swing.JCheckBox();
        usCheckBox = new javax.swing.JCheckBox();
        otCheckBox = new javax.swing.JCheckBox();
        drCheckBox = new javax.swing.JCheckBox();
        srCheckBox = new javax.swing.JCheckBox();
        mgCheckBox = new javax.swing.JCheckBox();
        rgCheckBox = new javax.swing.JCheckBox();
        modalityText = new javax.swing.JTextField();
        serverlistScroll = new javax.swing.JScrollPane();
        serverListTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        verifyButton = new javax.swing.JButton();
        queryButton = new javax.swing.JButton();
        retrieveButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        studyListTable = new javax.swing.JTable();
        serverNameLabel = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();
        queryFilterLabel = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("in/raster/mayam/form/i18n/Bundle",ApplicationContext.currentLocale); // NOI18N
        setTitle(bundle.getString("QueryRetrieve.title_1")); // NOI18N
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/in/raster/mayam/form/images/fav_mayam.png")));

        jPanel9.setMaximumSize(new java.awt.Dimension(1200, 1400));

        jPanel7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        searchDaysGroup.add(anydateRadio);
        anydateRadio.setSelected(true);
        anydateRadio.setText(bundle.getString("QueryRetrieve.Anydate.text_1")); // NOI18N
        anydateRadio.setName("Anydate"); // NOI18N

        searchDaysGroup.add(todayRadio);
        todayRadio.setText(bundle.getString("QueryRetrieve.Today.text_1")); // NOI18N
        todayRadio.setName("Today"); // NOI18N

        searchDaysGroup.add(yesterdayRadio);
        yesterdayRadio.setText(bundle.getString("QueryRetrieve.Yesterday.text_1")); // NOI18N
        yesterdayRadio.setName("Yesterday"); // NOI18N

        searchDaysGroup.add(lastweekRadio);
        lastweekRadio.setText(bundle.getString("QueryRetrieve.Last week.text_1")); // NOI18N
        lastweekRadio.setName("Last week"); // NOI18N

        searchDaysGroup.add(lastmonthRadio);
        lastmonthRadio.setText(bundle.getString("QueryRetrieve.Last month.text_1")); // NOI18N
        lastmonthRadio.setName("Last month"); // NOI18N

        searchDaysGroup.add(betweenRadio);
        betweenRadio.setText(bundle.getString("QueryRetrieve.Between.text_1")); // NOI18N
        betweenRadio.setName("Between"); // NOI18N

        fromSpinner.setEnabled(false);

        toSpinner.setEnabled(false);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(todayRadio)
                    .add(anydateRadio)
                    .add(yesterdayRadio)
                    .add(lastweekRadio)
                    .add(lastmonthRadio)
                    .add(betweenRadio)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(25, 25, 25)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, toSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .add(fromSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {anydateRadio, betweenRadio, lastmonthRadio, lastweekRadio, todayRadio, yesterdayRadio}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(anydateRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(todayRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(yesterdayRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lastweekRadio)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lastmonthRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(betweenRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(fromSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(toSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {anydateRadio, betweenRadio, lastmonthRadio, lastweekRadio, todayRadio, yesterdayRadio}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        birthDateSpinner.setEnabled(false);

        patientNameLabel.setText(bundle.getString("QueryRetrieve.patientNameLabel.text_1")); // NOI18N

        patientIDLabel.setText(bundle.getString("QueryRetrieve.patientIDLabel.text_1")); // NOI18N

        accessionLabel.setText(bundle.getString("QueryRetrieve.accessionLabel.text_1")); // NOI18N

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("in/raster/mayam/form/i18n/Bundle"); // NOI18N
        dobLabel.setText(bundle1.getString("QueryRetrieve.dobLabel.text")); // NOI18N
        dobLabel.setName("Date Of Birth"); // NOI18N
        dobLabel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dobLabelItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(31, 31, 31)
                        .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(patientIDLabel)
                            .add(patientNameLabel)
                            .add(accessionLabel)))
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(12, 12, 12)
                        .add(dobLabel)))
                .add(18, 18, 18)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(jTextField3)
                        .add(patientIDText)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, patientNameText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(birthDateSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(patientNameText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(patientNameLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(patientIDText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(patientIDLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(accessionLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dobLabel)
                    .add(birthDateSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        ctCheckBox.setText(bundle.getString("QueryRetrieve.ctCheckBox.text_1")); // NOI18N

        mrCheckBox.setText(bundle.getString("QueryRetrieve.mrCheckBox.text_1")); // NOI18N

        xaCheckBox.setText(bundle.getString("QueryRetrieve.xaCheckBox.text_1")); // NOI18N

        crCheckBox.setText(bundle.getString("QueryRetrieve.crCheckBox.text_1")); // NOI18N

        scCheckBox.setText(bundle.getString("QueryRetrieve.scCheckBox.text_1")); // NOI18N

        nmCheckBox.setText(bundle.getString("QueryRetrieve.nmCheckBox.text_1")); // NOI18N

        rfCheckBox.setText(bundle.getString("QueryRetrieve.rfCheckBox.text_1")); // NOI18N

        dxCheckBox.setText(bundle.getString("QueryRetrieve.dxCheckBox.text_1")); // NOI18N

        pxCheckBox.setText(bundle.getString("QueryRetrieve.pxCheckBox.text_1")); // NOI18N

        usCheckBox.setText(bundle.getString("QueryRetrieve.usCheckBox.text_1")); // NOI18N

        otCheckBox.setText(bundle.getString("QueryRetrieve.otCheckBox.text_1")); // NOI18N

        drCheckBox.setText(bundle.getString("QueryRetrieve.drCheckBox.text_1")); // NOI18N

        srCheckBox.setText(bundle.getString("QueryRetrieve.srCheckBox.text_1")); // NOI18N

        mgCheckBox.setText(bundle.getString("QueryRetrieve.mgCheckBox.text_1")); // NOI18N

        rgCheckBox.setText(bundle.getString("QueryRetrieve.rgCheckBox.text_1")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(ctCheckBox)
                            .add(mrCheckBox)
                            .add(xaCheckBox)
                            .add(crCheckBox)
                            .add(scCheckBox))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(nmCheckBox)
                            .add(rfCheckBox)
                            .add(dxCheckBox)
                            .add(pxCheckBox)
                            .add(usCheckBox))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(otCheckBox)
                            .add(drCheckBox)
                            .add(srCheckBox)
                            .add(mgCheckBox)
                            .add(rgCheckBox)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(modalityText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(otCheckBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(drCheckBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(srCheckBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(mgCheckBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rgCheckBox))
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel2Layout.createSequentialGroup()
                            .add(nmCheckBox)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(rfCheckBox)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(dxCheckBox)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(pxCheckBox)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(usCheckBox)
                                .add(scCheckBox)))
                        .add(jPanel2Layout.createSequentialGroup()
                            .add(ctCheckBox)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(mrCheckBox)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(xaCheckBox)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(crCheckBox))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                .add(modalityText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(9, 9, 9)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(jPanel7Layout.createSequentialGroup()
                            .add(8, 8, 8)
                            .add(jPanel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        serverListTable.setModel(new ServerTableModel());
        serverListTable.setDefaultRenderer(Object.class, new CellRenderer());
        serverListTable.getTableHeader().setPreferredSize(new Dimension(this.getWidth(), 25));
        Font ff=new Font("Lucida Grande",Font.BOLD,12);
        serverListTable.getTableHeader().setFont(ff);
        serverListTable.setRowHeight(20);
        serverListTable.getTableHeader().setForeground(new Color(255,138,0));
        serverListTable.getTableHeader().setBackground(new Color(0,0,0));
        serverlistScroll.setViewportView(serverListTable);

        verifyButton.setText(bundle.getString("QueryRetrieve.verifyButton.text_1")); // NOI18N
        verifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyButtonActionPerformed(evt);
            }
        });

        queryButton.setText(bundle.getString("QueryRetrieve.queryButton.text_1")); // NOI18N
        queryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queryButtonActionPerformed(evt);
            }
        });

        retrieveButton.setText(bundle.getString("QueryRetrieve.retrieveButton.text_1")); // NOI18N
        retrieveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retrieveButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(verifyButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(queryButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(retrieveButton)
                .add(47, 47, 47)
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 198, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(173, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(verifyButton)
                .add(queryButton)
                .add(retrieveButton)
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        studyListTable.setModel(new StudyListModel());
        studyListTable.setDefaultRenderer(Object.class, new CellRenderer());
        studyListTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studyListTableMouseClicked(evt);
            }
        });
        studyListTable.getTableHeader().setPreferredSize(new Dimension(this.getWidth(), 25));
        studyListTable.getTableHeader().setFont(new Font("Lucida Grande",Font.BOLD,12));
        studyListTable.setRowHeight(25);
        studyListTable.getTableHeader().setForeground(new Color(255,138,0));
        studyListTable.getTableHeader().setBackground(new Color(0,0,0));
        jScrollPane2.setViewportView(studyListTable);

        serverNameLabel.setBackground(new java.awt.Color(0, 0, 0));
        serverNameLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        serverNameLabel.setForeground(new java.awt.Color(255, 138, 0));
        serverNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serverNameLabel.setText(bundle.getString("QueryRetrieve.serverNameLabel.text_1")); // NOI18N
        serverNameLabel.setOpaque(true);

        headerLabel.setBackground(new java.awt.Color(0, 0, 0));
        headerLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        headerLabel.setForeground(new java.awt.Color(255, 138, 0));
        headerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabel.setText(bundle.getString("QueryRetrieve.headerLabel.text_1")); // NOI18N
        headerLabel.setOpaque(true);

        queryFilterLabel.setBackground(new java.awt.Color(0, 0, 0));
        queryFilterLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        queryFilterLabel.setForeground(new java.awt.Color(255, 138, 0));
        queryFilterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        queryFilterLabel.setText(bundle.getString("QueryRetrieve.queryFilterLabel.text_1")); // NOI18N
        queryFilterLabel.setOpaque(true);

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1161, Short.MAX_VALUE)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, serverlistScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, serverNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, headerLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(queryFilterLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(headerLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(queryFilterLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(serverlistScroll, 0, 0, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(serverNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setPatientInfoToQueryParam() {
        queryParam.setPatientId(patientIDText.getText());
        queryParam.setPatientName(patientNameText.getText());
        queryParam.setAccessionNo(jTextField3.getText());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        Date d1 = (Date) birthDateSpinner.getModel().getValue();
//        String dateOfBirth = sdf.format(d1);
//        queryParam.setBirthDate(dateOfBirth);
        if (!queryParam.getSearchDays().equalsIgnoreCase("Between")) {
            resetFromAndToDate();
        } else {
            setFromToDate();
        }
        if (!dobLabel.isSelected()) {
            resetBirthDate();
        } else {
            setBirthDate();
        }
    }
    private boolean startSearch = false;
    private void queryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryButtonActionPerformed
        String serverName = ((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0);
        try {
            startSearch = true;
            int noFilterQuery = 0;
            AEModel ae = ApplicationContext.databaseRef.getServerDetail(serverName);
            DcmURL url = new DcmURL("dicom://" + ae.getAeTitle() + "@" + ae.getHostName() + ":" + ae.getPort());
            EchoService echo = new EchoService();
            echo.checkEcho(url);
            if (echo.getStatus().trim().equalsIgnoreCase("EchoSuccess")) {
                doQuery(ae, url, noFilterQuery);
            } else {
                JOptionPane.showMessageDialog(this, "Server is not available", "Server Status", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            System.out.println("Select a Server");
            e.printStackTrace();
        }

        startSearch = false;
}//GEN-LAST:event_queryButtonActionPerformed
    private void doQuery(AEModel ae, DcmURL url, int noFilterQuery) {
        QueryService qs = new QueryService();
        setPatientInfoToQueryParam();
        //System.out.println("Birth Date Filter :" + queryParam.getBirthDate());
        if (queryParam.getPatientId().equalsIgnoreCase("") && queryParam.getPatientName().equalsIgnoreCase("") && queryParam.getSearchDate().equalsIgnoreCase("") && queryParam.getBirthDate().equalsIgnoreCase("") && modalityText.getText().equalsIgnoreCase("") && queryParam.getAccessionNo().equalsIgnoreCase("")) {
            noFilterQuery = JOptionPane.showConfirmDialog(this, "No filters have been selected. It will take long time to query and display result...!", "Confirm Dialog", JOptionPane.YES_NO_OPTION);
        }
        if (noFilterQuery == 0) {
            qs.callFindWithQuery(queryParam.getPatientId(), queryParam.getPatientName(), queryParam.getBirthDate(), queryParam.getSearchDate(), modalityText.getText(), queryParam.getAccessionNo(), null, url);
            Vector studyList = new Vector();
            for (int dataSetCount = 0; dataSetCount < qs.getDatasetVector().size(); dataSetCount++) {
                try {
                    Dataset dataSet = (Dataset) qs.getDatasetVector().elementAt(dataSetCount);
                    StudyModel studyModel = new StudyModel(dataSet);
                    studyList.addElement(studyModel);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            StudyListModel studyListModel = new StudyListModel();
            studyListModel.setData(studyList);
            studyListTable.setModel(studyListModel);
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(studyListModel);
            studyListTable.setRowSorter(sorter);
            boolean dicomServerDetailAlreadyPresentInArray = false;
            jLabel6.setText(qs.getDatasetVector().size() + " Studies found");
            if (dicomServerArray != null) {
                for (int i = 0; i < dicomServerArray.size(); i++) {
                    if (dicomServerArray.get(i).getName().equalsIgnoreCase(ae.getServerName())) {
                        dicomServerDetailAlreadyPresentInArray = true;
                        dicomServerArray.get(i).setAe(ae);
                        dicomServerArray.get(i).setStudyListModel(studyListModel);
                    }
                }
            }
            if (!dicomServerDetailAlreadyPresentInArray) {
                DicomServerDelegate dsd = new DicomServerDelegate(ae.getServerName());
                dsd.setAe(ae);
                dsd.setStudyListModel(studyListModel);
                dicomServerArray.add(dsd);
            }
        }
    }

    /**
     * This routine is the handler for retrieve button.
     * @param evt
     */
    private void retrieveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retrieveButtonActionPerformed
        String serverName = ((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0);
        String[] s = ApplicationContext.databaseRef.getListenerDetails();
        if (dicomServerArray != null) {
            for (int i = 0; i < dicomServerArray.size(); i++) {
                if (dicomServerArray.get(i).getName().equalsIgnoreCase(serverName)) {
                    int index[] = studyListTable.getSelectedRows();
                    for (int j = 0; j < index.length; j++) {
                        index[j] = studyListTable.convertRowIndexToModel(index[j]);
                    }
                    for (int tempI = 0; tempI < index.length; tempI++) {

                        String cmoveParam[] = new String[]{
                            "dicom" + "://" + dicomServerArray.get(i).getAe().getAeTitle() + "@" + dicomServerArray.get(i).getAe().getHostName() + ":" + dicomServerArray.get(i).getAe().getPort(),
                            "--dest", s[0], "--pid", dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 0), "--suid",
                            dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8)};

                        String cgetParam[] = new String[]{"-L", s[0] + ":" + s[1], dicomServerArray.get(i).getAe().getAeTitle() + "@" + dicomServerArray.get(i).getAe().getHostName() + ":" + dicomServerArray.get(i).getAe().getPort(),
                            "-cget", "-qModalitiesInStudy=" + dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 6), "-qStudyInstanceUID=" + dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8),
                            "-qPatientID=" + dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 0), "-rel"};
                        try {
                            if (!ApplicationContext.databaseRef.checkRecordExists("study", "StudyInstanceUID", dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8))) {
                                MainScreen.sndRcvFrm.setVisible(true);
                                MoveDelegate moveDelegate = null;
                                CgetDelegate cgetDelegate = null;
                                if (dicomServerArray.get(i).getAe().getRetrieveType().equalsIgnoreCase("C-MOVE")) {
                                    moveDelegate = new MoveDelegate(cmoveParam);
                                } else if (dicomServerArray.get(i).getAe().getRetrieveType().equalsIgnoreCase("WADO")) {
                                    WadoRetrieveDelegate wadoRetrieveDelegate = new WadoRetrieveDelegate();
                                    wadoRetrieveDelegate.retrieveStudy(serverName, dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 0), dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8));
                                } else if (dicomServerArray.get(i).getAe().getRetrieveType().equalsIgnoreCase("C-GET")) {
                                    cgetDelegate = new CgetDelegate(cgetParam);
                                }
                            } else {
                                MainScreen.sndRcvFrm.setVisible(true);
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_retrieveButtonActionPerformed

    /**
     * This routine used to set the server name
     */
    private void setServerName() {
        if (serverListTable.getSelectedRow() == -1) {
            serverNameLabel.setText(((ServerTableModel) serverListTable.getModel()).getValueAt(0, 0));
        } else {
            serverNameLabel.setText(((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0));
        }
    }
    private void verifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyButtonActionPerformed
        try {
            String serverName = ((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0);
            AEModel ae = ApplicationContext.databaseRef.getServerDetail(serverName);
            DcmURL url = new DcmURL("dicom://" + ae.getAeTitle() + "@" + ae.getHostName() + ":" + ae.getPort());
            EchoService echo = new EchoService();
            EchoStatus echoStatus = new EchoStatus(this, true);
            Display.alignScreen(echoStatus);
            echo.checkEcho(url);
            echoStatus.setTitle("Echo Status");
            try {
                if (echo.getStatus().trim().equalsIgnoreCase("EchoSuccess")) {
                    echoStatus.status.setText("Echo dicom://" + ae.getAeTitle() + "@" + ae.getHostName() + ":" + ae.getPort() + " successfully!");
                    echoStatus.setVisible(true);
                } else {
                    echoStatus.status.setText("Echo dicom://" + ae.getAeTitle() + "@" + ae.getHostName() + ":" + ae.getPort() + " not successfully!");
                    echoStatus.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Select a Server");
        }
    }//GEN-LAST:event_verifyButtonActionPerformed

    private void serverSelectionPerformed() {
        setServerName();
        StudyListModel studyList = new StudyListModel();
        studyListTable.setModel(studyList);
        if (serverListTable.getSelectedRow() > -1) {
            if (dicomServerArray != null) {
                for (int i = 0; i < dicomServerArray.size(); i++) {
                    if (dicomServerArray.get(i).getName().equalsIgnoreCase(((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0))) {
                        studyListTable.setModel(dicomServerArray.get(i).getStudyListModel());
                        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(studyList);
                        studyListTable.setRowSorter(sorter);
                    }
                }
            }
        }
    }
    private void studyListTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studyListTableMouseClicked
        String serverName = ((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0);
        String[] s = ApplicationContext.databaseRef.getListenerDetails();
        if (evt.getClickCount() == 2) {
            if (dicomServerArray != null) {
                for (int i = 0; i < dicomServerArray.size(); i++) {
                    if (dicomServerArray.get(i).getName().equalsIgnoreCase(serverName)) {
                        int index[] = studyListTable.getSelectedRows();
                        for (int j = 0; j < index.length; j++) {
                            index[j] = studyListTable.convertRowIndexToModel(index[j]);
                        }
                        for (int tempI = 0; tempI < index.length; tempI++) {

                            String cmoveParam[] = new String[]{
                                "dicom" + "://" + dicomServerArray.get(i).getAe().getAeTitle() + "@" + dicomServerArray.get(i).getAe().getHostName() + ":" + dicomServerArray.get(i).getAe().getPort(),
                                "--dest", s[0], "--pid", dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 0), "--suid",
                                dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8)};

                            String cgetParam[] = new String[]{"-L", s[0] + ":" + s[1], dicomServerArray.get(i).getAe().getAeTitle() + "@" + dicomServerArray.get(i).getAe().getHostName() + ":" + dicomServerArray.get(i).getAe().getPort(),
                                "-cget", "-qModalitiesInStudy=" + dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 6), "-qStudyInstanceUID=" + dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8),
                                "-qPatientID=" + dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 0), "-rel"};
                            try {
                                if (!ApplicationContext.databaseRef.checkRecordExists("study", "StudyInstanceUID", dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8))) {
                                    MainScreen.sndRcvFrm.setVisible(true);
                                    MoveDelegate moveDelegate = null;
                                    CgetDelegate cgetDelegate = null;
                                    if (dicomServerArray.get(i).getAe().getRetrieveType().equalsIgnoreCase("C-MOVE")) {
                                        moveDelegate = new MoveDelegate(cmoveParam);
                                    } else if (dicomServerArray.get(i).getAe().getRetrieveType().equalsIgnoreCase("WADO")) {
                                        WadoRetrieveDelegate wadoRetrieveDelegate = new WadoRetrieveDelegate();
                                        wadoRetrieveDelegate.retrieveStudy(serverName, dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 0), dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8));
                                    } else if (dicomServerArray.get(i).getAe().getRetrieveType().equalsIgnoreCase("C-GET")) {
                                        cgetDelegate = new CgetDelegate(cgetParam);
                                    }
                                } else {
                                    MainScreen.sndRcvFrm.setVisible(true);
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_studyListTableMouseClicked

    private void dobLabelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dobLabelItemStateChanged
        if (evt.getStateChange() == 1) {
            birthDateSpinner.setEnabled(true);
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            //Date d1 = (Date) birthDateSpinner.getModel().getValue();
            //dateOfBirth = sdf.format(d1);
            //queryParam.setBirthDate("Date Of Birth");
        } else {
            birthDateSpinner.setEnabled(false);
            //queryParam.setBirthDate(null);
        }
    }//GEN-LAST:event_dobLabelItemStateChanged

    private class SearchDaysHandler implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (searchDaysGroup.getSelection() == ((JRadioButton) e.getItem()).getModel()) {
                    if (((JRadioButton) e.getItem()).getName().equalsIgnoreCase("Between")) {
                        fromSpinner.setEnabled(true);
                        toSpinner.setEnabled(true);
                    } else {
                        fromSpinner.setEnabled(false);
                        toSpinner.setEnabled(false);
                    }
                    queryParam.setSearchDays(((JRadioButton) e.getItem()).getName());
                }
            }
        }
    }
    
    private class ModalityHandler implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            String selectedModality = getModality();
            if (selectedModality.startsWith("\\")) {
                selectedModality = selectedModality.substring(1);
            }
            modalityText.setText(selectedModality);
        }
    }

    private String getModality() {
        String modalityString = "";
        if (ctCheckBox.isSelected()) {
            modalityString = ctCheckBox.getActionCommand();
        }
        if (mrCheckBox.isSelected()) {
            modalityString += "\\" + mrCheckBox.getActionCommand();
        }
        if (xaCheckBox.isSelected()) {
            modalityString += "\\" + xaCheckBox.getActionCommand();
        }
        if (crCheckBox.isSelected()) {
            modalityString += "\\" + crCheckBox.getActionCommand();
        }
        if (scCheckBox.isSelected()) {
            modalityString += "\\" + scCheckBox.getActionCommand();
        }
        if (nmCheckBox.isSelected()) {
            modalityString += "\\" + nmCheckBox.getActionCommand();
        }
        if (rfCheckBox.isSelected()) {
            modalityString += "\\" + rfCheckBox.getActionCommand();
        }
        if (dxCheckBox.isSelected()) {
            modalityString += "\\" + dxCheckBox.getActionCommand();
        }
        if (pxCheckBox.isSelected()) {
            modalityString += "\\" + pxCheckBox.getActionCommand();
        }
        if (usCheckBox.isSelected()) {
            modalityString += "\\" + usCheckBox.getActionCommand();
        }
        if (otCheckBox.isSelected()) {
            modalityString += "\\" + otCheckBox.getActionCommand();
        }
        if (drCheckBox.isSelected()) {
            modalityString += "\\" + drCheckBox.getActionCommand();
        }
        if (srCheckBox.isSelected()) {
            modalityString += "\\" + srCheckBox.getActionCommand();
        }
        if (mgCheckBox.isSelected()) {
            modalityString += "\\" + mgCheckBox.getActionCommand();
        }
        if (rgCheckBox.isSelected()) {
            modalityString += "\\" + rgCheckBox.getActionCommand();
        }

        return modalityString;
    }

    private void osSpecifics() {
        this.setSize(1030, 750);
        if (System.getProperty("os.name").startsWith("Mac")) {
            Border border = UIManager.getBorder("InsetBorder.aquaVariant");
            if (border == null) {
                border = new BevelBorder(1);
            }
            jPanel1.setBorder(border);
            jPanel2.setBorder(border);
            jPanel9.setBackground(new Color(216, 216, 216));
            jPanel1.setBackground(new Color(216, 216, 216));
            jPanel2.setBackground(new Color(216, 216, 216));
            jPanel7.setBackground(new Color(216, 216, 216));
            jPanel8.setBackground(new Color(216, 216, 216));
            serverlistScroll.setBackground(new Color(216, 216, 216));
        }
    }

    /**
     * This is implemented handler method for ServerChangeListener.
     */
    public void onServerChange() {
        setServerTableModel();
        setServerName();
    }

    public void valueChanged(ListSelectionEvent e) {
        serverSelectionPerformed();
    }

    public void setFromToDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date d1 = (Date) fromSpinner.getModel().getValue();
        Date d2 = (Date) toSpinner.getModel().getValue();
        String from = sdf.format(d1);
        String to = sdf.format(d2);
        queryParam.setFrom(from);
        queryParam.setTo(to);
    }

    /**
     *This routine used to reset the from and to date.
     */
    public void resetFromAndToDate() {
        queryParam.setFrom(null);
        queryParam.setTo(null);
    }
    public void setBirthDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date d1 = (Date) birthDateSpinner.getModel().getValue();
        String birthDate = sdf.format(d1);
        queryParam.setBirthDate(birthDate);
    }

    /**
     *This routine used to reset the from and to date.
     */
    public void resetBirthDate() {
        queryParam.setBirthDate(null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new QueryRetrieve().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accessionLabel;
    private javax.swing.JRadioButton anydateRadio;
    private javax.swing.JRadioButton betweenRadio;
    private javax.swing.JSpinner birthDateSpinner;
    private javax.swing.JCheckBox crCheckBox;
    private javax.swing.JCheckBox ctCheckBox;
    private javax.swing.JCheckBox dobLabel;
    private javax.swing.JCheckBox drCheckBox;
    private javax.swing.JCheckBox dxCheckBox;
    private javax.swing.JSpinner fromSpinner;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JRadioButton lastmonthRadio;
    private javax.swing.JRadioButton lastweekRadio;
    private javax.swing.JCheckBox mgCheckBox;
    private javax.swing.ButtonGroup modalityGroup;
    private javax.swing.JTextField modalityText;
    private javax.swing.JCheckBox mrCheckBox;
    private javax.swing.JCheckBox nmCheckBox;
    private javax.swing.JCheckBox otCheckBox;
    private javax.swing.JLabel patientIDLabel;
    private javax.swing.JTextField patientIDText;
    private javax.swing.JLabel patientNameLabel;
    private javax.swing.JTextField patientNameText;
    private javax.swing.JCheckBox pxCheckBox;
    private javax.swing.JButton queryButton;
    private javax.swing.JLabel queryFilterLabel;
    private javax.swing.JButton retrieveButton;
    private javax.swing.JCheckBox rfCheckBox;
    private javax.swing.JCheckBox rgCheckBox;
    private javax.swing.JCheckBox scCheckBox;
    private javax.swing.ButtonGroup searchDaysGroup;
    private javax.swing.JTable serverListTable;
    private javax.swing.JLabel serverNameLabel;
    private javax.swing.JScrollPane serverlistScroll;
    private javax.swing.JCheckBox srCheckBox;
    private javax.swing.JTable studyListTable;
    private javax.swing.JSpinner toSpinner;
    private javax.swing.JRadioButton todayRadio;
    private javax.swing.JCheckBox usCheckBox;
    private javax.swing.JButton verifyButton;
    private javax.swing.JCheckBox xaCheckBox;
    private javax.swing.JRadioButton yesterdayRadio;
    // End of variables declaration//GEN-END:variables
    public ArrayList<DicomServerDelegate> dicomServerArray = new ArrayList<DicomServerDelegate>();
    private QueryParam queryParam = new QueryParam();
}
