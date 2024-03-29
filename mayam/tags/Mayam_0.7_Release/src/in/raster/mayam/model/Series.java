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
package in.raster.mayam.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class Series implements Serializable {

    private String SeriesInstanceUID;
    private String StudyInstanceUID;
    private String SeriesNumber;
    private String Modality;
    private String institutionName;
    private List<Instance> imageList;
    private String seriesDesc;
    private String bodyPartExamined;
    private int seriesRelatedInstance;

    public Series() {
        SeriesInstanceUID = "";
        StudyInstanceUID="";
        Modality = "";
        SeriesNumber = "";
        seriesDesc="";
        imageList = new ArrayList<Instance>();
    }

    /**
     * @return the SeriesInstanceUID
     */
    public String getSeriesInstanceUID() {
        return SeriesInstanceUID;
    }

    /**
     * @param SeriesInstanceUID the SeriesInstanceUID to set
     */
    public void setSeriesInstanceUID(String SeriesInstanceUID) {
        this.SeriesInstanceUID = SeriesInstanceUID;
    }

    /**
     * @return the Modality
     */
    public String getModality() {
        return Modality;
    }

    /**
     * @param Modality the Modality to set
     */
    public void setModality(String Modality) {
        this.Modality = Modality;
    }

   
    /**
     * 
     * @param image
     */
    public void addImage(Instance image) {
        this.imageList.add(image);
    }    
   

    public String getSeriesNumber() {
        return SeriesNumber;
    }
    /**
     * 
     * @param seriesNumber
     */
    public void setSeriesNumber(String seriesNumber) {
        SeriesNumber = seriesNumber;
    }   
   
    /**
     * @return the imageList
     */
    public List<Instance> getImageList() {
        return imageList;
    }

    /**
     * @param imageList the imageList to set
     */
    public void setImageList(List<Instance> imageList) {
        this.imageList = imageList;
    }

    public String getStudyInstanceUID() {
        return StudyInstanceUID;
    }

    public void setStudyInstanceUID(String StudyInstanceUID) {
        this.StudyInstanceUID = StudyInstanceUID;
    }

    public String getSeriesDesc() {
        return seriesDesc;
    }

    public void setSeriesDesc(String seriesDesc) {
        this.seriesDesc = seriesDesc;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public int getSeriesRelatedInstance() {
        return seriesRelatedInstance;
    }

    public void setSeriesRelatedInstance(int seriesRelatedInstance) {
        this.seriesRelatedInstance = seriesRelatedInstance;
    }

    public String getBodyPartExamined() {
        return bodyPartExamined;
    }

    public void setBodyPartExamined(String bodyPartExamined) {
        this.bodyPartExamined = bodyPartExamined;
    }

}
