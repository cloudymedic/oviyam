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

package in.raster.oviyam;

import in.raster.oviyam.model.SeriesModel;
import in.raster.oviyam.util.ParseJSON;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.dcm4che2.data.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author yogapraveen
 * 
 */
public class SeriesInfoWeb {

	private static Logger log = Logger.getLogger(SeriesInfoWeb.class);

	private HashMap<Integer, SeriesModel> series;

	// Constructor
	public SeriesInfoWeb() {
		series = new HashMap<Integer, SeriesModel>();
	}

	public void callWithWebQuery(String PatientID, String studyID,
			String wadoURL) {

		wadoURL += "/rs/studies/" + studyID;
		wadoURL += "/series?includefield=all&orderby=SeriesNumber";
		URL url;
		try {
			url = new URL(wadoURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				return;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;

			String returnValue = "";
			while ((output = br.readLine()) != null) {
				returnValue += output;
			}

			JSONArray jsonArray = new JSONArray(returnValue);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject value = jsonArray.getJSONObject(i);
				series.put(i, parseDICOMData(value));
			}
			conn.disconnect();

		} catch (Exception e) {
			log.error("Unable to retrieve Data From Web Query", e);
			return;
		}

	}

	private SeriesModel parseDICOMData(JSONObject value) {
		SeriesModel series = new SeriesModel();
		ParseJSON parseJSON = new ParseJSON(value);

		series.setSeriesIUID(parseJSON.getValue(Tag.SeriesInstanceUID));
		series.setSeriesNumber(parseJSON.getValue(Tag.SeriesNumber));
		series.setSeriesDate(parseJSON.getValue(Tag.SeriesDate));
		series.setSeriesTime(parseJSON.getValue(Tag.SeriesTime));
		series.setSeriesDescription(parseJSON.getValue(Tag.SeriesDescription));
		series.setModality(parseJSON.getValue(Tag.Modality));
		series.setNumberOfInstances(parseJSON
				.getValue(Tag.NumberOfSeriesRelatedInstances));
		series.setBodyPartExamined(parseJSON.getValue(Tag.BodyPartExamined));
		return series;
	}

	/**
	 * It returns the collections of SeriesModel.
	 * 
	 * @returns ArrayList<SeriesModel>
	 */
	public ArrayList<SeriesModel> getSeriesList() {
		return new ArrayList<SeriesModel>(series.values());
	}

}
