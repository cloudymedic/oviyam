<!--
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
* Portions created by the Initial Developer are Copyright (C) 2007
* the Initial Developer. All Rights Reserved.
*
* Contributor(s):
* Asgar Hussain B
* Babu Hussain A
* Bharathi B
* Manikandan P
* Prakash J
* Prakasam V
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
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page errorPage="ErrorPage.jsp" %>
    <%@ page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Echo &nbsp;${requestScope.echoURL } &nbsp; failed .</title>
</head>
<body bgcolor="#000000" style="color:#FFFFFF;">
<center>
		<form action="serverconfig.do" method="POST">
				<table>
						<tr>
						<td>AE Title</td><td><input  type="text" name="aeTitle" id="aeTitle" value="${applicationScope.serverConfig.aeTitle}"></td>
					</tr>
					<tr>
						<td>Host Name</td><td><input  type="text" name="hostName" id="hostName" value="${applicationScope.serverConfig.hostName}"></td>
					</tr>
					<tr>
						<td>Port</td><td><input type="text"  name="port" id="port" value="${applicationScope.serverConfig.port}"></td>
					</tr>
					<tr>
						<td>WADO Port</td><td><input  type="text" name="wadoPort" id="wadoPort" value="${applicationScope.serverConfig.wadoPort}"></td>
					</tr>

	<tr>
		<td>Dcm Protocol</td>
		<td><select name="dcmProtocol" id="dcmProtocol">
			<option value="DICOM" >DICOM</option>
			<option value="DICOM_TLS">DICOM_TLS</option>
			<option value="DICOM_TLS_3DES">DICOM_TLS_3DES</option>
			<option value="DICOM_TLS_AES">DICOM_TLS_AES</option>
			<option value="DICOM_TLS_NODES">DICOM_TLS_NODES</option>
			
		</select></td>
	</tr>
	<tr>
		<td colspan="2"><input type="submit" value="update"></td>
	</tr>
</table>
</form>
</center>


<center>
<table bgcolor="red">
	<tr height="150px">
		<td width="350px">Echo result:<br>
		<br>
		Echo &nbsp;${requestScope.echoURL } &nbsp; failed .<br>
		</td>
	</tr>
</table>

</center>




</body>
</html>