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
package in.raster.oviyam.xml.handler;

import in.raster.oviyam.xml.model.Configuration;
import in.raster.oviyam.xml.model.OverlayText;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * @author yogapraveen
 *
 */
public class OverlayTextHandler {

	// Initialize logger
	private static Logger log = Logger.getLogger(OverlayTextHandler.class);

	private Serializer serializer = null;
	// private File source = null;
	private Configuration config = null;

	// Constructor
	public OverlayTextHandler(){
		try {
			serializer = new Persister();
			config = serializer.read(Configuration.class,
					LanguageHandler.source);
		} catch (Exception e) {
			log.error("Unable to read XML document", e);
			return;
		}
	}
		
	public void setOverlayText(OverlayText list){
		try {
			OverlayText overlayText = config.getOverlayText();
			if(overlayText == null){
				overlayText = new OverlayText();
			}
			overlayText.setImageLaterality(list.getImageLaterality());
			config.setOverlayText(overlayText);
			serializer.write(config, LanguageHandler.source);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	public OverlayText getOvrelayText() {
		return config!=null ? config.getOverlayText() : null;
	}
		
}
