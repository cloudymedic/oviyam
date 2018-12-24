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
 * The Original Code is part of Oviyam, an web viewer for DICOM(TM) images
 * hosted at http://skshospital.net/pacs/webviewer/oviyam_0.6-src.zip
 *
 * The Initial Developer of the Original Code is
 * Raster Images
 * Portions created by the Initial Developer are Copyright (C) 2014
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * Babu Hussain A
 * Balamurugan R
 * Devishree V
 * Guruprasath R
 * Meer Asgar Hussain B
 * Prakash J
 * Suresh V
 * Yogapraveen K
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

package in.raster.oviyam.model;

import java.io.Serializable;
import java.util.Calendar;
import org.dcm4che.data.Dataset;
import org.dcm4che.dict.Tags;

/**
 * 
 * @author asgar
 */
public class StudyModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// local variables
	private String patientID;
	private String patientName;
	private String patientGender;
	private String patientBirthDate;
	private String physicianName;
	private String studyDate;
	private Calendar parsedDate = null;
	private String studyTime;
	private String studyDescription;
	private String modalitiesInStudy;
	// private String[] modality;
	private String studyRelatedInstances;
	private String accessionNumber;
	private String studyInstanceUID;
	private String studyRelatedSeries;

	// default constructor
	public StudyModel() {
	}

	// constructor
	public StudyModel(Dataset ds) {
		setPatientID(ds.getString(Tags.PatientID));
		setPatientName(ds.getString(Tags.PatientName));
		setPatientGender(ds.getString(Tags.PatientSex));
		setPatientBirthDate(ds.getString(Tags.PatientBirthDate));
		setPhysicianName(ds.getString(Tags.ReferringPhysicianName));
		setStudyDate(ds.getString(Tags.StudyDate));
		setParsedDate();

		setStudyTime(ds.getString(Tags.StudyTime));
		setStudyDescription(ds.getString(Tags.StudyDescription));

		setModalitiesInStudy(ds.getStrings(Tags.ModalitiesInStudy));

		setStudyRelatedInstances(ds
				.getString(Tags.NumberOfStudyRelatedInstances));
		setAccessionNumber(ds.getString(Tags.AccessionNumber));
		setStudyInstanceUID(ds.getString(Tags.StudyInstanceUID));
		setStudyRelatedSeries(ds.getString(Tags.NumberOfStudyRelatedSeries));
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getAccessionNumber() {
		return accessionNumber != null ? accessionNumber : "";
	}

	public void setModalitiesInStudy(String[] modalities) {
		if (modalities != null) {
			for (int i = 0; i < modalities.length; i++) {
				if (i == 0) {
					modalitiesInStudy = modalities[i];
				} else {
					modalitiesInStudy += "\\" + modalities[i];
				}
			}
		}
	}

	public String getModalitiesInStudy() {
		return modalitiesInStudy;
	}

	public void setPatientBirthDate(String birthDate) {
		this.patientBirthDate = birthDate;
	}

	public String getPatientBirthDate() {
		return patientBirthDate != null ? patientBirthDate : "";
	}

	public void setPatientGender(String gender) {
		this.patientGender = gender;
	}

	public String getPatientGender() {
		return patientGender != null ? patientGender : "-";
	}

	public void setPatientID(String patID) {
		this.patientID = patID;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientName(String patName) {
		this.patientName = patName;
	}

	public String getPatientName() {
		return patientName != null ? patientName.replace("^", " ") : "";
	}

	public void setPhysicianName(String physicianName) {
		this.physicianName = physicianName;
	}

	public String getPhysicianName() {
		return physicianName != null ? physicianName : "";
	}

	public void setStudyDate(String studyDate) {
		this.studyDate = studyDate;
	}

	public String getStudyDate() {
		return studyDate != null ? studyDate : "[No study date]";
	}

	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}

	public String getStudyDescription() {
		return studyDescription != null ? studyDescription
				.replace("^", " ") : "[No study description]";
	}

	public void setStudyInstanceUID(String studyInstanceUID) {
		this.studyInstanceUID = studyInstanceUID;
	}

	public String getStudyInstanceUID() {
		return studyInstanceUID;
	}

	public void setStudyRelatedInstances(String relatedInstances) {
		this.studyRelatedInstances = relatedInstances;
	}

	public String getStudyRelatedInstances() {
		return studyRelatedInstances != null ? studyRelatedInstances
				: "";
	}

	public void setStudyRelatedSeries(String relatedSeries) {
		this.studyRelatedSeries = relatedSeries;
	}

	public String getStudyRelatedSeries() {
		return studyRelatedSeries != null ? studyRelatedSeries : "";
	}

	public void setStudyTime(String studyTime) {
		this.studyTime = studyTime;
	}

	public String getStudyTime() {
		return studyTime != null ? studyTime : "";
	}

	public void setParsedDate() {
		parsedDate = Calendar.getInstance();

		if (studyDate != null && !studyDate.contains("[No study date]")) {
			try {
				
				String studyDate = this.studyDate.replace("-","").replace(":", "").replace(" ", "").replace(".", "");
				
				parsedDate.set(Integer.parseInt(studyDate.substring(0, 4)),
						Integer.parseInt(studyDate.substring(4, 6)) - 1,
						Integer.parseInt(studyDate.substring(6, 8)));
			} catch (Exception ex) {
				ex.printStackTrace();
				parsedDate = null;
			}
		} else {
			parsedDate = null;
		}
	}

	public Calendar getParsedDate() {
		return parsedDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final StudyModel other = (StudyModel) obj;
		if ((this.studyInstanceUID == null) ? (other.studyInstanceUID != null)
				: !this.studyInstanceUID.equals(other.studyInstanceUID)) {
			return false;
		}
		return true;
	}

}