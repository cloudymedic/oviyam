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

package in.raster.oviyam.util;

import in.raster.oviyam.delegate.ImageOrientation;
import in.raster.oviyam.model.InstanceModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.dcm4che2.data.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author yogapraveen
 * 
 */
public class ParseMetadata {

	private static Logger log = Logger.getLogger(ParseMetadata.class);
	private JSONObject jsonObj = null;
	private URL url = null;

	public ParseMetadata(URL url) {
		this.url = url;
	}

	public JSONObject readImageData(InstanceModel im) {
		try {
			jsonObj = new JSONObject();

			jsonObj.put("SopUID", im.getSopIUID());
			jsonObj.put("InstanceNo", im.getInstanceNumber());
			jsonObj.put("SopClassUID", im.getSopClassUID());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				return null;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;

			String returnValue = "";
			while ((output = br.readLine()) != null) {
				returnValue += output;
			}

			JSONArray jsonArray = new JSONArray(returnValue);
			JSONObject value = jsonArray.getJSONObject(0);

			parseDICOMData(value);

			conn.disconnect();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Unable to retrieve Data From Web Query", e);
			return null;
		}

		return jsonObj;
	}

	private void parseDICOMData(JSONObject value) {
		try {
			ParseJSON parseJSON = new ParseJSON(value);

			String windowCenter = parseJSON.getValue(Tag.WindowCenter);
			String windowWidth = parseJSON.getValue(Tag.WindowWidth);
			String rows = parseJSON.getValue(Tag.Rows);
			String column = parseJSON.getValue(Tag.Columns);
			String imgOrient = parseJSON.getValue(Tag.ImageOrientationPatient);
			String imgPosition = parseJSON.getValue(Tag.ImagePositionPatient);
			String sliceThickness = parseJSON.getValue(Tag.SliceThickness);
			String forUID = parseJSON.getValue(Tag.FrameOfReferenceUID);
			String resclaeslp = parseJSON.getValue(Tag.RescaleSlope);
			String resclaeintercept = parseJSON.getValue(Tag.RescaleIntercept);
			String bitsStored = parseJSON.getValue(Tag.BitsStored);
			String photometricInterpretation = parseJSON
					.getValue(Tag.PhotometricInterpretation);

			String pixelRep = parseJSON.getValue(Tag.PixelRepresentation);
			String frmTime = parseJSON.getValue(Tag.FrameTime);
			String pixelSpacing = parseJSON.getValue(Tag.PixelSpacing);
			String imgPixelSpacing = parseJSON.getValue(Tag.ImagerPixelSpacing);
			String totalFrames = parseJSON.getValue(Tag.NumberOfFrames);
			String imgLtr = parseJSON.getValue(Tag.ImageLaterality);
			String viewPos = parseJSON.getValue(Tag.ViewPosition);
			String modality = parseJSON.getValue(Tag.Modality);
			String imageType = parseJSON.getValue(Tag.ImageType);

			String refImageSeq = parseJSON
					.getValue(Tag.ReferencedImageSequence);

			windowCenter = windowCenter != null ? windowCenter : "";
			windowWidth = windowWidth != null ? windowWidth : "";
			int nativeRows = Integer.valueOf(rows != null ? rows : "0");
			int nativeColumns = Integer.valueOf(rows != null ? column : "0");
			imgOrient = imgOrient != null ? imgOrient : "";
			imgOrient = imgOrient.replace("|", "\\");

			String sliceLoc = "";
			String imagePosition = "";
			if (imgPosition != null) {
				imagePosition = imgPosition;
				sliceLoc = imagePosition.substring(imagePosition
						.lastIndexOf("|") + 1);
			}

			sliceThickness = sliceThickness != null ? sliceThickness : "";
			forUID = forUID != null ? forUID : "";
			resclaeslp = resclaeslp != null ? resclaeslp : "1.0";
			resclaeintercept = resclaeintercept != null ? resclaeintercept
					: "0.0";
			int bitsStored1 = Integer.valueOf(bitsStored != null ? bitsStored
					: "12");
			boolean monochrome = photometricInterpretation != null ? photometricInterpretation
					.trim().equalsIgnoreCase("MONOCHROME1") ? true : false
					: false;
			String photometric = photometricInterpretation != null ? photometricInterpretation
					.trim() : " ";
			int pixRep = Integer.valueOf(pixelRep != null ? pixelRep : "-1");
			double frameTime = Double.valueOf(frmTime != null ? frmTime : "0");

			pixelSpacing = pixelSpacing != null ? pixelSpacing : "";
			imgPixelSpacing = imgPixelSpacing != null ? imgPixelSpacing : "";
			totalFrames = totalFrames != null ? totalFrames : "";
			boolean multiFrame = true;
            if(totalFrames.trim().equalsIgnoreCase("1") || totalFrames.trim().length() == 0){
            	 multiFrame = false;
            }
			imgLtr = imgLtr != null ? imgLtr : "";
			viewPos = viewPos != null ? viewPos : "";
			modality = modality != null ? modality : "";

			String referSopInsUid = "";
			if (refImageSeq != null) {
				JSONObject reqImg = new JSONObject(refImageSeq);
				String hexa = Integer.toHexString(Tag.ReferencedSOPInstanceUID);
				hexa = String.format("%0$8s", hexa).replace(" ", "0")
						.toUpperCase();
				reqImg = reqImg.getJSONObject(hexa);

				JSONArray array = reqImg.getJSONArray("Value");
				for (int i = 0; i < array.length(); i++) {
					if (i != 0) {
						referSopInsUid += ",";
					}
					referSopInsUid += array.getString(i);
				}
			}

			String image_type = "";
			if (imageType != null) {
				image_type = imageType;

				String[] img_types = image_type.split("\\|");
				if (img_types.length >= 3) {
					image_type = img_types[2];
				}
			}

			jsonObj.put("windowCenter", windowCenter.replaceAll("\\\\", "|"));
			jsonObj.put("windowWidth", windowWidth.replaceAll("\\\\", "|"));
			jsonObj.put("nativeRows", nativeRows);
			jsonObj.put("nativeColumns", nativeColumns);

			if (imgOrient.length() > 0)
				jsonObj.put("imageOrientation",
						ImageOrientation.getOrientation(imgOrient));
			else
				jsonObj.put("imageOrientation", imgOrient);

			jsonObj.put("sliceLocation", sliceLoc);
			jsonObj.put("sliceThickness", sliceThickness);
			jsonObj.put("frameOfReferenceUID", forUID.replaceAll("\u0000", ""));

			jsonObj.put("imagePositionPatient",
					imagePosition.replace("|", "\\"));
			jsonObj.put("imageOrientPatient", imgOrient);
			jsonObj.put("pixelSpacing", pixelSpacing.replace("|", "\\"));
			jsonObj.put("imagerPixelSpacing", imgPixelSpacing);
			jsonObj.put("refSOPInsUID", referSopInsUid.replaceAll("\u0000", ""));
			jsonObj.put("imageType", image_type);
			jsonObj.put("numberOfFrames", totalFrames);

			jsonObj.put("rescaleSlope", resclaeslp);
			jsonObj.put("rescaleIntercept", resclaeintercept);
			jsonObj.put("BitsStored", bitsStored1);
			jsonObj.put("monochrome1", monochrome);
			jsonObj.put("PixelRepresentation", pixRep);
			jsonObj.put("frameTime", frameTime);
			jsonObj.put("imgLaterality", imgLtr);
			jsonObj.put("viewPosition", viewPos);
			jsonObj.put("modality", modality);
			jsonObj.put("photometric", photometric);
			jsonObj.put("multiframe", multiFrame);

		} catch (Exception e) {
			log.error("Unable to add data into json", e);
		}
	}

}
