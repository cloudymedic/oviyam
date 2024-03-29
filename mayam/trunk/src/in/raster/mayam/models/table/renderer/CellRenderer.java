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
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author BabuHussain
 * @version 0.5
 *
 */
public class CellRenderer extends DefaultTableCellRenderer {

    private Color whiteColor = new Color(254, 254, 254);
    private Color alternateColor = new Color(237, 243, 254);
    private Color selectedColor = new Color(142, 104, 104);
    private Color cellColor = new Color(176, 141, 141);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        super.getTableCellRendererComponent(table, value, selected, false, row, column);
        Color bg;
        if (!selected) {
            bg = (row % 2 == 0 ? alternateColor : whiteColor);
        } else {
            bg = selectedColor;
        }
        setBackground(bg);
        if (focused && table.isCellEditable(row, column)) {
            setBackground(cellColor);
            setBorder(BorderFactory.createLineBorder(cellColor));
            setForeground(Color.BLACK);
        } else {
            Color fg;
            if (selected) {
                fg = Color.white;
            } else {
                fg = Color.black;
            }
            setForeground(fg);
        }
        setFont(ApplicationContext.textFont);
        setHorizontalAlignment(SwingConstants.CENTER);
        return this;
    }
}