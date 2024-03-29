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
package in.raster.mayam.listeners;

import in.raster.mayam.context.ApplicationContext;
import in.raster.mayam.models.ServerModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import org.dcm4che.util.DcmURL;

/**
 *
 * @author Devishree
 * @version 2.0
 */
public class ServerTabListener extends MouseAdapter {

    JTabbedPane serverTab;
    JPopupMenu verifyPopup;
    JMenuItem verifyItem;

    public ServerTabListener(JTabbedPane serverTab) {
        this.serverTab = serverTab;
        createServerPopup();
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (SwingUtilities.isRightMouseButton(me) && serverTab.getSelectedIndex() != 0) {
            verifyItem.setText(ApplicationContext.currentBundle.getString("MainScreen.verifyPopup.text"));
            verifyPopup.show(me.getComponent(), me.getX(), me.getY() + 10);
        }
    }

    private void createServerPopup() {
        verifyPopup = new JPopupMenu();
        verifyItem = new JMenuItem(ApplicationContext.currentBundle.getString("MainScreen.verifyPopup.text"));
        verifyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServerModel serverdetails = ApplicationContext.databaseRef.getServerDetails(serverTab.getTitleAt(serverTab.getSelectedIndex()));
                DcmURL url = ApplicationContext.communicationDelegate.constructURL(serverdetails.getAeTitle(), serverdetails.getHostName(), serverdetails.getPort());
                boolean result = ApplicationContext.communicationDelegate.verifyServer(url);
                if (result) {
                    JOptionPane.showOptionDialog(ApplicationContext.mainScreenObj, "Echo dicom://" + serverdetails.getAeTitle() + "@" + serverdetails.getHostName() + ":" + serverdetails.getPort() + " " + ApplicationContext.currentBundle.getString("MainScreen.verifyServerSuccess.text"), ApplicationContext.currentBundle.getString("MainScreen.verifyServer.title.text"), JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{ApplicationContext.currentBundle.getString("OkButtons.text")}, "default");
                } else {
                    JOptionPane.showOptionDialog(ApplicationContext.mainScreenObj, "Echo dicom://" + serverdetails.getAeTitle() + "@" + serverdetails.getHostName() + ":" + serverdetails.getPort() + " " + ApplicationContext.currentBundle.getString("MainScreen.verifyServerFailiure.text"), ApplicationContext.currentBundle.getString("MainScreen.verifyServer.title.text"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{ApplicationContext.currentBundle.getString("OkButtons.text")}, "default");
                }
            }
        });
        verifyPopup.add(verifyItem);
    }
}