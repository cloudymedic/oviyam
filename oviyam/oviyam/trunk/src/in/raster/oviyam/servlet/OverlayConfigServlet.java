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

import in.raster.oviyam.xml.handler.OverlayTextHandler;
import in.raster.oviyam.xml.model.ImageLaterality;
import in.raster.oviyam.xml.model.OverlayText;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class OverlayConfigServlet
 */

public class OverlayConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static Logger log = Logger.getLogger(ServerConfigServlet.class);
	
	private String imgLtrDisplay;
	private String imgLtrModality;
	private String imgLtrModalityList;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		PrintWriter out = response.getWriter();
		
		JSONObject textOverlayJSON = new JSONObject();
		JSONObject imageLaterJSON;
		
		String result ="" ;
		try{
			if(action.equals("UPDATE")){
				String data = request.getParameter("data");
			
				textOverlayJSON = new JSONObject(data);
				imageLaterJSON = textOverlayJSON.getJSONObject("imageLaterality");
			
				OverlayText ot = new OverlayText();
				ImageLaterality imgLaterality = ot.getImageLaterality();
				imgLaterality.setDisplay(imageLaterJSON.getString("display"));
				imgLaterality.setModality(imageLaterJSON.getString("modality"));
				imgLaterality.setModalityList(imageLaterJSON.getString("modalityList"));
				
				OverlayTextHandler overlay = new OverlayTextHandler();
				overlay.setOverlayText(ot);
				textOverlayJSON = new JSONObject();
				result = "Success";
				
			}else if(action.equals("RESET")){
				OverlayText ot = new OverlayText();
				textOverlayJSON = new JSONObject();
				imageLaterJSON = new JSONObject();
		
				ot = resetOverlayText();
				
				textOverlayJSON = readData(ot);
				result = "Success";
			}else{
				OverlayTextHandler overlayHandler = new OverlayTextHandler();
				textOverlayJSON = new JSONObject();
				
				OverlayText overlayText = (OverlayText) overlayHandler.getOvrelayText();
			
				if(overlayText == null){
					overlayText = resetOverlayText();					
				}
				textOverlayJSON = readData(overlayText);
				result = "Success";
			}
		
		} catch(Exception e){
			log.error(e);
			result = "Failure";
		}
		try {
			textOverlayJSON.put("result", result);		
		} catch (JSONException e) {
			log.error(e);
		}
		
		out.println(textOverlayJSON);
		
	}
	
	private JSONObject readData(OverlayText overlayText){
		JSONObject textOverlayJSON;
		JSONObject imageLaterJSON;
		
		ImageLaterality imgLaterality = overlayText.getImageLaterality();
		imgLtrDisplay = imgLaterality.getDisplay();
		imgLtrModality = imgLaterality.getModality();
		imgLtrModalityList = imgLaterality.getModalityList();	
		
		textOverlayJSON = new JSONObject();
		imageLaterJSON = new JSONObject();
		try{
			imageLaterJSON.put("display", imgLtrDisplay);
			imageLaterJSON.put("modality", imgLtrModality);
			imageLaterJSON.put("modalityList", imgLtrModalityList);
		
			textOverlayJSON.put("imageLaterality",imageLaterJSON);
			return textOverlayJSON;
		}catch(JSONException e){
			log.error(e);
		}
		return textOverlayJSON;
	}
	
	private OverlayText resetOverlayText(){
		OverlayText  overlay = new OverlayText();
		
		imgLtrDisplay = getServletContext().getInitParameter("imgLateralityDisplay");
		imgLtrModality = getServletContext().getInitParameter("imgLateralityModality");
		imgLtrModalityList = getServletContext().getInitParameter("imgLateralityModalityList");
		
		ImageLaterality imgLaterality = overlay.getImageLaterality();
		imgLaterality.setDisplay(imgLtrDisplay);
		imgLaterality.setModality(imgLtrModality);
		imgLaterality.setModalityList(imgLtrModalityList);
		
		OverlayTextHandler overlayHandler = new OverlayTextHandler();
		overlayHandler.setOverlayText(overlay);
		
		return overlay;
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
