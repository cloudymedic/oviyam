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
package in.raster.mayam.models.table.renderer;

import in.raster.mayam.context.ApplicationContext;
import in.raster.mayam.models.treetable.TreeTable;
import in.raster.mayam.models.treetable.TreeTableModelAdapter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Devishree
 * @version 2.0
 */
public class IconRenderer extends DefaultTableCellRenderer {

    ImageIcon localIcon = new ImageIcon(getClass().getResource("/in/raster/mayam/form/images/local.png"));
    ImageIcon downloadingIcon = new ImageIcon(getClass().getResource("/in/raster/mayam/form/images/downloading.png"));
    private TreeTable treeTable;
    Font font = new Font("Lucida Grande", Font.PLAIN, 13);
    private Color whiteColor = new Color(254, 254, 254);
    private Color alternateColor = new Color(237, 243, 254);
    private Color selectedColor = new Color(142, 104, 104);

    public IconRenderer(TreeTable treeTable) {
        this.treeTable = treeTable;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel comp = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); // To get the default rendering
        comp.setFont(font);
        comp.setFocusable(false);
        comp.setBorder(noFocusBorder);
        comp.setOpaque(true);
        comp.setHorizontalAlignment(SwingConstants.LEFT);
        comp.setIcon(null);
        comp.setText((String) value);

        switch (column) {
            case 1:
                String studyUid = (String) ((TreeTableModelAdapter) treeTable.getModel()).getValueAt(row, 10);
                if (!ApplicationContext.isLocal && column == 1 && studyUid != null) {
                    if (ApplicationContext.databaseRef.checkRecordExists("Study", "StudyInstanceUID", studyUid)) {
                        if (ApplicationContext.databaseRef.isDownloadPending(studyUid)) {
                            comp.setIcon(localIcon);
                        } else { //The study was on download
                            comp.setIcon(downloadingIcon);
                        }
                    }
                }
                break;
            case 9:
                comp.setHorizontalAlignment(SwingConstants.RIGHT);
                break;
        }

        if ((Boolean) ((TreeTableModelAdapter) treeTable.getModel()).getValueAt(row, 13)) {
            switch (column) {
                case 2:
                    comp.setText(ApplicationContext.currentBundle.getString("MainScreen.seriesNoColumn.text"));
                    break;
                case 3:
                    comp.setText(ApplicationContext.currentBundle.getString("MainScreen.seriesDescColumn.text"));
                    break;
                case 4:
                    comp.setText(ApplicationContext.currentBundle.getString("MainScreen.seriesDateColumn.text"));
                    break;
                case 5:
                    comp.setText(ApplicationContext.currentBundle.getString("MainScreen.seriesTimeColumn.text"));
                    break;
                case 6:
                    comp.setText(ApplicationContext.currentBundle.getString("MainScreen.bodyPartColumn.text"));
                    break;
                case 7:
                    comp.setText(ApplicationContext.currentBundle.getString("MainScreen.modalityColumn.text"));
                    break;
                case 8:
                    comp.setText(ApplicationContext.currentBundle.getString("MainScreen.imagesColumn.text"));
                    break;
            }
            comp.setForeground(new Color(235, 138, 0));
            comp.setBackground(Color.BLACK);
            comp.setFont(new Font("Lucida Grande", Font.BOLD, 12));
            return comp;
        } else if (isSelected) {
            comp.setForeground(Color.WHITE);
            comp.setBackground(selectedColor);
        } else {
            comp.setBackground((row % 2 == 0 ? alternateColor : whiteColor));
            comp.setForeground(Color.BLACK);
        }
        return comp;
    }
}