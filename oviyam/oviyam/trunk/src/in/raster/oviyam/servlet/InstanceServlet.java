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

package in.raster.oviyam.servlet;

import in.raster.oviyam.ImageInfo;
import in.raster.oviyam.model.InstanceModel;
import in.raster.oviyam.util.InstanceComparator;
import in.raster.oviyam.util.ParseImgFileData;
import in.raster.oviyam.xml.handler.LanguageHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;

/**
 * 
 * @author asgar
 */
public class InstanceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(InstanceServlet.class);

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String patID = request.getParameter("patientId");
		String studyUID = request.getParameter("studyUID");
		String seriesUID = request.getParameter("seriesUID");
		String dcmURL = request.getParameter("dcmURL");
		String wadoURL = request.getParameter("serverURL");

		ImageInfo imageInfo = new ImageInfo();
		
		ArrayList<InstanceModel> instanceList = null;
		
		imageInfo.callFindWithQuery(patID, studyUID, seriesUID, null, dcmURL);
		instanceList = imageInfo.getInstancesList();
		
		Collections.sort(instanceList, new InstanceComparator());

		String fname = "";
		if (!(!wadoURL.equals("C-MOVE") && !wadoURL.equals("C-GET"))) {
			String dest = LanguageHandler.source.getAbsolutePath();
			fname = dest.substring(0, dest.indexOf("oviyam2-7-config.xml") - 1)
					+ File.separator + "oviyam2";
			fname += File.separator + studyUID;
		} else {
			wadoURL += "?requestType=WADO&contentType=application/dicom&studyUID="
					+ studyUID + "&seriesUID=" + seriesUID;
		}

		JSONArray jsonArray = new JSONArray();

		InputStream is = null;

		try {
			for (int i = 0; i < instanceList.size(); i++) {
				InstanceModel im = (InstanceModel) instanceList.get(i);

				String objectUID = im.getSopIUID();
				
				String UrlTmp = null;

				try {
					if (!(!wadoURL.equals("C-MOVE") && !wadoURL.equals("C-GET"))) {
						UrlTmp = fname + File.separator + objectUID;
						is = new FileInputStream(new File(UrlTmp));
					} else {
						UrlTmp = wadoURL + "&objectUID=" + objectUID
								+ "&transferSyntax=1.2.840.10008.1.2.1";
						URL url = new URL(UrlTmp);
						is = url.openStream();
					}
					ParseImgFileData dicomFile = new ParseImgFileData(is);
					jsonArray.put(dicomFile.readDicomFile(im));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			PrintWriter out = response.getWriter();
			out.print(jsonArray);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}