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
package in.raster.mayam.delegate;

import in.raster.mayam.context.ApplicationContext;
import in.raster.mayam.model.Instance;
import in.raster.mayam.model.Series;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class ExportToDcmDelegate {

    public ExportToDcmDelegate() {
    }

    public void studyExportAsDicom(String studyIUID, String ouputFilePath) {
        String patientName = ApplicationContext.databaseRef.getPatientNameBasedonStudyUID(studyIUID);
        File patientNameFile = new File(ouputFilePath, patientName);
        if (!patientNameFile.exists()) {
            patientNameFile.mkdirs();
        }        
        ArrayList<Series> seriesList = ApplicationContext.databaseRef.getSeriesList(studyIUID);
        Iterator<Series> seriesItr = seriesList.iterator();
        while (seriesItr.hasNext()) {
            Series series = seriesItr.next();
            Iterator<Instance> imgitr = series.getImageList().iterator();
            while (imgitr.hasNext()) {
                Instance img = imgitr.next();
                doExport(img.getFilepath(), patientNameFile.getAbsolutePath());
            }
        }
    }

    public void seriesExportAsDicom(String studyIUID, String seriesIUID, String outputFilePath) {
        ArrayList<Series> seriesList = ApplicationContext.databaseRef.getSeriesList(studyIUID);
        Iterator<Series> seriesItr = seriesList.iterator();
        while (seriesItr.hasNext()) {
            Series series = seriesItr.next();
            if (series.getSeriesInstanceUID().equalsIgnoreCase(seriesIUID)) {
                Iterator<Instance> imgitr = series.getImageList().iterator();
                while (imgitr.hasNext()) {
                    Instance img = imgitr.next();
                    doExport(img.getFilepath(), outputFilePath);
                }
            }
        }
    }

    public void instanceExportAsDicom(String inputFilePath, String outputFilePath) {
        doExport(inputFilePath, outputFilePath);
    }

    private void doExport(String inputFilePath, String outputFilePath) {
        InputStream in = null;
        OutputStream out=null;
        try {
            DestinationFinder destFinder=new DestinationFinder();
            File inputFile=new File(destFinder.getFileDestination(new File(inputFilePath)));
            in = new FileInputStream(inputFile);
            out = new FileOutputStream(new File(outputFilePath,inputFile.getName()));
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }         
        }
        catch(FileNotFoundException ex)
        {
            Logger.getLogger(ExportToDcmDelegate.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(ExportToDcmDelegate.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ExportToDcmDelegate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

