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

import in.raster.oviyam.delegate.EchoService;
import in.raster.oviyam.xml.handler.ServerHandler;
import in.raster.oviyam.xml.model.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dcm4che.util.DcmURL;
import org.json.JSONObject;

/**
 * Servlet implementation class EchoServlet
 */
public class EchoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public EchoServlet() {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String dcmURL = request.getParameter("dicomURL");

		String[] dcmUrl = dcmURL.split(":");

		String AETitle = dcmUrl[1].substring(2);
		String hostName = dcmUrl[2];

		hostName = hostName.substring(hostName.indexOf("@") + 1);
		String port = dcmUrl[3];

		ServerHandler sHandler = new ServerHandler();
		Server server = sHandler.findServerByAetIpPort(AETitle, hostName, port);

		String status = "";

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		if (server.getProtocol().equalsIgnoreCase("QIDO-RS")) {
			try {
				String wadoContext = server.getWadocontext();
				wadoContext = wadoContext.substring(0,
						wadoContext.lastIndexOf("/"));
				String wadoURL = hostName + ":" + server.getWadoport() + "/"
						+ wadoContext;

				wadoURL += "/dimse/" + server.getAetitle();
				wadoURL = "http://" + wadoURL;
				
				URL url = new URL(wadoURL);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200) {
					System.out.println("Echo failed");
					out.print("Echo failed");
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;

				String returnValue = "";
				while ((output = br.readLine()) != null) {
					returnValue += output;
				}

				JSONObject jsonObj = new JSONObject(returnValue);
				String result = jsonObj.getString("result");
				if (result.equalsIgnoreCase("0")) {
					status = "EchoSuccess";
				} else {
					status = "Echo failed";
				}

			} catch (Exception e) {
				status = "Echo failed";
			}
		} else {
			DcmURL url = new DcmURL(dcmURL);
			EchoService echo = new EchoService();
			echo.checkEcho(url);
			status = echo.getStatus().trim();

		}

		out.print(status);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}