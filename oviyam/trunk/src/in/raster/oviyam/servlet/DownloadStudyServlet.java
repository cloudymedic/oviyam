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
* Devishree V
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

package in.raster.oviyam.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.raster.oviyam.handler.DownloadHandler;
import in.raster.oviyam.model.DownloadModel;

/**
 *
 * @author Yogapraveen
 */
public class DownloadStudyServlet extends HttpServlet {

   
	private static final long serialVersionUID = 1L;
	
    private static Logger log = Logger.getLogger(DownloadStudyServlet.class);
    
    PrintWriter out = null;
    DownloadModel downloadModel;
	DownloadHandler downloadHandler;
	String returnValue = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    	try {
    		
    		if(!(request.getParameterMap().containsKey("downloadRequest"))) {
    			RequestDispatcher rd = request.getRequestDispatcher("/download.do");
    			rd.forward(request, response);
    		} else {
    			processRequest(request, response);
    		}
    	}catch (Exception e) {
			log.error("ERROR IN PROCESSING IMAGE",e);
			out.print("ERROR");
			out.close();
		}
    }       
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
	 * @param request
	 * @param response
	 */
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			String downloadRequest = new String(request
					.getParameter("downloadRequest").trim()
					.getBytes("ISO-8859-1"), "UTF-8");

			out = response.getWriter();
			HttpSession session = request.getSession(true);
			String sessionID = session.getId();

			downloadModel = new DownloadModel();
			downloadHandler = new DownloadHandler(downloadModel);
			downloadModel.setSessionID(sessionID);

			initiateDownloadProcess(downloadRequest);

			processZipCreation();

			setDownloadInfo(session);
			log.info("ZIP FILE CREATION COMPLETED....");
			log.info("WAITING FOR CONFIRMATION OF DOWNLOAD...");
			out.print(returnValue);
			out.close();
		} catch (Exception e) {
			log.error("Error", e);
		}
	}

	/**
	 * initiates the image fetch process by getting the information about the
	 * study/series which is to be downloaded.
	 * 
	 * @param downloadInfo
	 */
	private void initiateDownloadProcess(String downloadInfo) {
		try {
			JSONObject requestedValues = new JSONObject(downloadInfo);
			String patientInfo = requestedValues.getString("patientInfo");
			String selectedSeries;

			selectedSeries = requestedValues.getString("selectedSeries");

			String downloadFileType = requestedValues
					.getString("downloadFileType");
			downloadModel.setDownloadFileType(downloadFileType);
			JSONObject patientInfoObj = new JSONObject(patientInfo);
			downloadModel.setPatientInfo(patientInfoObj);

			JSONArray jsonArray = null;
			try {
				jsonArray = new JSONArray(selectedSeries);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					downloadModel.setSelectedSeries(jsonObject);
					downloadHandler.processSeries();
				}
			} catch (Exception e) {
				log.error("Error in Fetching Image", e);
				out.print("ERROR");
				out.close();
			}
		} catch (JSONException e1) {
			log.error("Error in parsing Json Data", e1);
			out.print("ERROR");
			out.close();
		}
	}

	/**
	 * initaites the zip file creation.
	 */
	private void processZipCreation() {
		downloadHandler.createZipFile();
		File dir = new File(downloadModel.getDownloadPath());
		downloadHandler.deleteFile(dir);
		if (checkPathExist(downloadModel.getZipFilePath()))
			downloadHandler.calculateFileSize();
		else {
			out.print("ERROR");
			out.close();
		}
	}

	/**
	 * get the time of the request to differentiate the each request.
	 * 
	 * @return requestedTime
	 */
	private String getRequestedTime() {
		AtomicLong counter = new AtomicLong(System.currentTimeMillis());
		long currTime = counter.getAndIncrement();
		String requestedTime = String.valueOf(currTime);
		return requestedTime;
	}

	/**
	 * set the information about the download in return value and session
	 * attributes to process the download.
	 * 
	 * @param session
	 */
	private void setDownloadInfo(HttpSession session) {
		String requestedTime = getRequestedTime();
		returnValue = downloadModel.getZipFileName();
		returnValue += "|" + downloadModel.getFileSize();
		returnValue += "|" + requestedTime;
		session.setAttribute(requestedTime + "_downloadPath",
				downloadModel.getZipFilePath());
		session.setAttribute(requestedTime + "_sessionIDFilePath",
				downloadModel.getSessionIDFilePath());
		session.setAttribute(requestedTime + "_fileName",
				downloadModel.getZipFileName());
		session.setAttribute("tempFilePath", downloadModel.getTempFilePath());
	}

	/**
	 * check whether the file path exist or not.
	 * 
	 * @param path
	 * @return boolean
	 */
	private boolean checkPathExist(String path) {
		File file = new File(path);
		if (file.exists())
			return true;
		return false;
	}
}