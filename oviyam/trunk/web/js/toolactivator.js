function enableTool(tool) {
	jQuery('#tool').text(tool);
	jQuery('.toggleOff', window.parent.document).not('#scoutLine').removeClass('toggleOff');
	jQuery('#'+tool,window.parent.document).addClass('toggleOff');	
}

function disableTool(tool) {
	jQuery('#tool').html('');
	jQuery('#' + tool, parent.document).removeClass('toggleOff');
	jQuery('#canvasDiv').css('cursor',"default");	
}

function disableOtherTools(tool) {
	if(tool!='zoom') { // Deactivates Zoom
		stopZoom();
	}
	if(tool!='move') {	// Deactivates Move
		stopMove(document.getElementById('canvasLayer2'));
	}
	if(tool!='windowing') { //Deactivates windowing
		windowCenter = windowWidth = '';
		unbindWindowing('windowing');
	}
	if(tool!='measure') { // Deactivates Measure
		deactivateMeasure('measure');
	} 
}

function onToolSelection(e) {
	if(e.detail.tool==="txtOverlay") {
		jQuery('.textOverlay:not(#huDisplayPanel, #applyWLDiv)').toggle();
	} else if(e.detail.tool==="loop") {
		doLoop(e.detail.isLoop);
	}
	
	if(document!=undefined && document.body.style.border==='1px solid rgb(255, 138, 0)') { // Determines whether this is a Selected Tile
		switch(e.detail.tool) {
			case 'windowing':
				activateWindowing(e.detail.tool);
				break;
			case 'zoom':
				activateZoom(e.detail.tool);
				break;	
			case 'move':
				activateMove(e.detail.tool);
				break;	
			case 'stackImage':
				activatestack(e.detail.tool);
				break;	
			case 'vflip':
				activateVFlip(e.detail.tool);
				break;
			case 'hflip':
				activateHFlip(e.detail.tool);
				break;
			case 'rotateLeft':
				activateLeftRotation(e.detail.tool);
				break;
			case 'rotateRight':
				activateRightRotation(e.detail.tool);
				break;
			case 'metaData':
				window.parent.showMetaData(jQuery('#frameSrc').html());
				break;
			case 'reset':
				doReset(tool);
				break;
			case 'invert':
				doInvert(tool);				
				break;			
			case 'fullScreen':
				window.parent.doFullScreen(window.parent.document);
				break;
			case 'scoutLine':
				doScout(e.detail.tool);
				break;
			case 'measure':
				activateMeasure(e.detail.tool);
				break;
			case 'ruler':
				setShape(e.detail.tool);
				break;
			case 'rectangle':
				setShape(e.detail.tool);
				break;
			case 'oval':
				setShape(e.detail.tool);
				break;
			case 'preset':
				applyPreset(e.detail.wc,e.detail.ww);
				break;
			case 'clearAll':
				clearMeasurements();
				init(jQuery('#totalImages').html().indexOf('Frame')<0 ? imgInc : frameInc);
				break;
			case 'mouseup':
				state.drag = false;
				break;
			case 'delete':
				deleteSelectedMeasurement();
				break;
		}
	}
}

function activateZoom(toolid) {	
	if(jQuery('#tool').html()!=toolid) {
		disableOtherTools(toolid);
		enableTool(toolid);		
		doLoop(false);
		window.parent.document.getElementById('loopChkBox').checked = false;
		doZoom();
	} else {
		disableTool(toolid);				
		stopZoom();
	}
}

function activateMove(toolid) {
	if(jQuery('#tool').html()!=toolid) {
		disableOtherTools(toolid);
		enableTool(toolid);			
		doLoop(false);
		window.parent.document.getElementById('loopChkBox').checked = false;
		doMove();
	} else {
		disableTool(toolid);		
		stopMove();
	}
}

function activatestack(tool) {
	if(jQuery('#tool').html()!=tool) {
		disableOtherTools(tool);		
		enableTool(tool);		
		doLoop(false);
		window.parent.document.getElementById('loopChkBox').checked = false;
		doMouseWheel = true;
	} else {
		disableTool(tool);
	}
}

function activateVFlip(tool) {
	var image = seriesUid + "_" + imgInc;
	state.vflip = state.vflip? false : true;
	showImg(image);
	flipOrientationToVertical();
}

function flipOrientationToVertical() {
	var tmpTop = jQuery('#imgOriTop').text();
	
	jQuery('#imgOriTop').text(jQuery('#imgOriBottom').text());
	jQuery('#imgOriBottom').text(tmpTop);
}

function activateHFlip(tool) {
	var image = seriesUid + "_" + imgInc;
	state.hflip = state.hflip? false : true;
	showImg(image);
	flipOrientationToHorizontal();
}

function flipOrientationToHorizontal() {
	var tmpLeft = jQuery('#imgOriLeft').text();
	
	jQuery('#imgOriLeft').text(jQuery('#imgOriRight').text());
	jQuery('#imgOriRight').text(tmpLeft);
}

function activateLeftRotation(tool) {
	var image = seriesUid + "_" + imgInc;
	state.rotate-=90;
	switch(state.rotate) {
		case -90:
			state.rotate = 270;
			break;
		case -180:
			state.rotate = 180;
		case -270:
			state.rotate = 90;
			break;
	}
	showImg(image);
	rotateLeftTextOverlay();
}

function rotateLeftTextOverlay() {
	var tmpLeft = jQuery('#imgOriLeft').text();
	var tmpTop = jQuery('#imgOriTop').text();
	
	jQuery('#imgOriTop').text(jQuery('#imgOriRight').text());
	jQuery('#imgOriRight').text(jQuery('#imgOriBottom').text());
	jQuery('#imgOriBottom').text(tmpLeft);
	jQuery('#imgOriLeft').text(tmpTop);
}

function activateRightRotation(tool) {
	var image = seriesUid + "_" + imgInc;
	state.rotate+=90;
	if(state.rotate>=360) {
		state.rotate=0;
	}
	showImg(image);
	rotateRightTextOverlay();
}

function rotateRightTextOverlay() {
	var tmpTop = jQuery('#imgOriTop').text();
	var tmpBottom = jQuery('#imgOriBottom').text();
	
	jQuery('#imgOriTop').text(jQuery('#imgOriLeft').text());
	jQuery('#imgOriBottom').text(jQuery('#imgOriRight').text());
	jQuery('#imgOriRight').text(tmpTop);	
	jQuery('#imgOriLeft').text(tmpBottom);	
}

function doReset(toolid) {
	if(jQuery('#tool').html()!='') {
		jQuery('#' + jQuery('#tool').html(), window.parent.document).removeClass('toggleOff');
		jQuery('#tool').html('');	
	}
	doMouseWheel = true;

	disableOtherTools(toolid);	
	state = {translationX : 0,translationY: 0,scale: 0,vflip: false,hflip: false,rotate: 0};
	showImage(seriesUid+"_"+imgInc);	

	loadInstanceText(false);
	drawoutline();
	
	doLoop(false);
	window.parent.document.getElementById('loopChkBox').checked = false;	

	if(jQuery('#applyWLDiv').attr('divState')=="true") {
		modifiedWC = windowCenter;
		modifiedWW = windowWidth;
		getWLAppliedImages();
		jQuery('#applyWLDiv').attr('divState',"false");
	}
}

function doInvert(toolid) {
	state.invert = state.invert ? false : true;
	window.parent.doInvert(jQuery('#imageCanvas').get(0),jQuery('#tool').html()==='windowing');	
}

function doMove() {
	var img = null;
	var canvasLayer2 = document.getElementById('canvasLayer2');
	
	var startCoords = [];	
	
	canvasLayer2.onmousedown = function(e) {
		if(e.which==1) {
			state.drag = true;
			 img = jQuery('#' + (seriesUid + "_" + imgInc).replace(/\./g,'_'), window.parent.document).get(0);
	
	        startCoords = [
	        e.pageX - state.translationX,
	        e.pageY - state.translationY
	        ];
	
	        e.preventDefault();
	        e.stopPropagation();
	        e.target.style.cursor = "url(images/move.png), auto";
		}
	};
	
	 canvasLayer2.onmouseup = function(e) {
        state.drag = false;   
        e.target.style.cursor = "default";
    };
    
    canvasLayer2.onmousemove = function(e) {
        if(!state.drag) return;
        var x = e.pageX;
        var y = e.pageY;
        
        state.translationX = x-startCoords[0];
        state.translationY = y-startCoords[1];
        
		showImg(null,img);
		drawoutline();
    };	
}

function stopMove() {
	var canvasLayer2 = document.getElementById('canvasLayer2');
	
	canvasLayer2.onmousedown = function(e) {
		 if(e.preventDefault) {
        	e.preventDefault();
    	}
    };
    
    canvasLayer2.onmouseup = function(e) {
		 if(e.preventDefault) {
        	e.preventDefault();
    	}
    };
    
    canvasLayer2.onmousemove = function(e) {
		 if(e.preventDefault) {
        	e.preventDefault();
    	}
    };
}

function doLoop(loop) {
	clearInterval(loopTimer);
	if(loop && document.body.style.border==='1px solid rgb(255, 138, 0)') { // Determines whether this is a Selected Tile) 		
		loopTimer = setInterval(function() {
			nextImage();			
		}, parent.loopSpeed);		
	} 
}

function loadSlider() {
	if(isSlider) {			
		if(total>1) {
			if(jQuery('#multiframe').css('visibility')==="hidden") {
				setSliderRange(imgInc, total);	
			} else {
				setSliderRange(frameInc, total);	
			}
		} else {
			jQuery('#footer').hide();
		}
	}
	
	jQuery('.ui-slider-handle').css('height', '10px');
	jQuery('.ui-slider-handle').css('width', '10px');
	jQuery('.ui-slider-horizontal').css('height', '.4em');
	jQuery('.ui-slider-horizontal').css('top', '8px');
	jQuery('.ui-slider-horizontal').css('cursor', 'pointer');
}

function setSliderRange(val,maxVal) {
	jQuery('#trackbar1').slider({
		range: "min",
		value: val,
		min: 1,
		max: maxVal,
		slide: onTick
	});
}

function onTick(event, ui) {	
	if(jQuery('#multiframe').css('visibility')==="hidden") {
		imgInc = ui.value;
		showImg(seriesUid + '_' + imgInc);
	} else {
		frameInc = ui.value;
		showImg(getParameter(jQuery('#frameSrc').html(),'object') + '_' + frameInc);	
	}
	loadInstanceText(false);
}

function setSliderValue() {
	if(jQuery('#multiframe').css('visibility')==="hidden") {
		jQuery('#trackbar1').slider("option","value",imgInc);
	} else {
		jQuery('#trackbar1').slider("option","value",frameInc);
	}
}

function convertSplChars(str)
{
	if(typeof str!="undefined") {
		str = str.replace(/&/g, "&amp;");
		str = str.replace(/>/g, "&gt;");
		str = str.replace(/</g, "&lt;");
		str = str.replace(/"/g, "&quot;");
		str = str.replace(/'/g, "&#039;");
    }
    return str;
}

function getOppositeOrientation(str) {
    var strTmp = '';
    for(i=0; i<str.length; i++) {
        switch(str.charAt(i)) {
            case 'P':
                strTmp += 'A';
                break;
            case 'A':
                strTmp += 'P';
                break;
            case 'I':
                strTmp += 'S';
                break;
            case 'S':
                strTmp += 'I';
                break;
            case 'F':
                strTmp += 'H';
                break;
            case 'H':
                strTmp += 'F';
                break;
            case 'L':
                strTmp += 'R';
                break;
            case 'R':
                strTmp += 'L';
                break;
        }
    }

    return strTmp;
}

function getFromToLoc() {
	var selThickLoc = jQuery('#thickLocationPanel').text();
		
	if(selThickLoc==null || selThickLoc.length == 0) {   
        return {from:undefined, to:undefined,sliceLoc:undefined};
	}
	
	var selThick = selThickLoc.match("Thick:(.*)mm Loc");
	if(selThick!=null) {
		selThick = selThick[1];
	} else {
		selThick = "";
	}
	var selLoc = selThickLoc.match("Loc:(.*)mm")[1];

	return {from:parseFloat(selLoc) - parseFloat(selThick), to:parseFloat(selLoc) + parseFloat(selThick),sliceLoc:parseFloat(selLoc)};	
}

function synchronize(e) {
	if(jQuery('body').css('border')!='1px solid rgb(255, 138, 0)' && e.detail.forUid===jQuery('#forUIDPanel').html()) {
		var fromTo = e.detail.fromTo;		
		var data = sessionStorage!=null ? sessionStorage[seriesUid] : false;
		if(data) {
			data = JSON.parse(data);
			for(var i=0;i<data.length;i++) {
				var sliceLoc = (data[i])['sliceLocation'];
			
				if(sliceLoc>=fromTo['from'] && sliceLoc<=fromTo['to'] && parseFloat(sliceLoc)-parseFloat(fromTo['sliceLoc'])<128) {
					imgInc = (i+1);
					showImg(seriesUid+ '_' + imgInc,null,true);
					loadInstanceText(false);
					break;
				}
			}
		}
	}
}

function doScout(toolId) {
	var modality = jQuery('#modalityDiv').html();
	if(jQuery(window.parent.document).find('iframe').length>1 && (modality.indexOf('CT')>=0 || modality.indexOf('MR')>=0)) {
		if(!window.parent.displayScout) {
			window.parent.displayScout = true;			
			jQuery('#scoutLine', window.parent.document).addClass('toggleOff');
			Localizer.drawScout();
		} else {
			window.parent.displayScout = false;
			jQuery('#scoutLine', window.parent.document).removeClass('toggleOff');
			Localizer.hideScoutLine();
		}
	}
}

function activateWindowing(toolId) {
	if(jQuery('#tool').html()!=toolId) {
		disableOtherTools(toolId);
		enableTool(toolId);	
		doMouseWheel = false;

		jQuery('#thickLocationPanel').hide();		
		jQuery('#loadingView',window.parent.document).show();
		doLoop(false);
		window.parent.document.getElementById('loopChkBox').checked = false;
		setTimeout(function() {
			var imageData = null;
			try {
				imageData = JSON.parse(sessionStorage[seriesUid])[imgInc-1];

				var url = "pixel.do?requestType=WADO&contentType=application/dicom&study=" + window.parent.pat.studyUID + "&series=" + seriesUid + "&object=" + imageData['SopUID'] + "&transferSyntax=1.2.840.10008.1.2.1" + "&retrieveType=" + window.parent.pat.serverURL;				

				if(jQuery('#multiframe').css('visibility')!="hidden") { // Multiframe
					url+="&frameNumber=" + (frameInc-1);
				}
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", url, true);
				xhr.responseType = "arraybuffer";  
				
				xhr.onload = function(e) {
					if (this.status == 200) {
						if(imageData['PixelRepresentation']==0 && imageData['BitsStored']==8) {
							pixelBuffer = new Uint8Array(this.response);
						} else if(imageData['PixelRepresentation']==0 && imageData['BitsStored']>8) {
							pixelBuffer = new Uint16Array(this.response);
						} else if(imageData['PixelRepresentation']==1 && imageData['BitsStored']>8) {
							pixelBuffer = new Int16Array(this.response);
						} else {
							pixelBuffer = this.response;
						}
						
						for(var i=0;i<pixelBuffer.length;i++) {
							var pix = pixelBuffer[i];
							minPix = Math.min(minPix,pix);
							maxPix = Math.max(maxPix,pix);
						}
							
						lookupObj = new LookupTable();
						lookupObj.setPixelInfo(minPix,maxPix,imageData['monochrome1']);
						columns = imageData['nativeColumns'];
						doWindowing(imageData,jQuery('#huDisplayPanel'),jQuery('#windowLevel'));
					}
				};
				
				xhr.send();
			} catch(exception) { 
				var imgSrc = jQuery('#' + (seriesUid + "_" + imgInc).replace(/\./g,'_'),window.parent.document).attr('src');
				imageData = parseDicom(null,getParameter(imgSrc,'object'));
				doWindowing(imageData,jQuery('#huDisplayPanel'),jQuery('#windowLevel'));
			}
			
			//doWindowing(imageData,jQuery('#huDisplayPanel'),jQuery('#windowLevel'));
		},1);
	} else {
		unbindWindowing(toolId);
	}
}

function unbindWindowing(toolId) {	
	disableTool(toolId);
	jQuery('#huDisplayPanel').hide();
	jQuery('#thickLocationPanel').show();
	jQuery('#canvasLayer2').unbind('mousemove').unbind('mousedown').unbind('mouseup');
	window.parent.disableWindowingContext();
	doMouseWheel = true;
}

function activateMeasure(toolId) {
	if(jQuery('#tool').html()!=toolId) {
		disableOtherTools(toolId);
		enableTool(toolId);	
		doMouseWheel = false;
		
		jQuery('#thickLocationPanel').hide();
		jQuery('#loadingView', window.parent.document).show();
		doLoop(false);
		window.parent.document.getElementById('loopChkBox').checked = false;
		setTimeout(function() {
			var imageData = null;
			try {
				imageData = JSON.parse(sessionStorage[seriesUid])[imgInc-1];
				var url = "pixel.do?requestType=WADO&contentType=application/dicom&study=" + window.parent.pat.studyUID + "&series=" + seriesUid + "&object=" + imageData['SopUID'] + "&transferSyntax=1.2.840.10008.1.2.1" + "&retrieveType=" + window.parent.pat.serverURL;
				if(jQuery('#multiframe').css('visibility')!="hidden") { // Multiframe
					url+="&frameNumber=" + (frameInc-1);
				}
				
				var xhr = new XMLHttpRequest();
				xhr.open("POST", url, true);
				xhr.responseType = "arraybuffer";  
				
				xhr.onload = function(e) {
					if (this.status == 200) {
						if(imageData['PixelRepresentation']==0 && imageData['BitsStored']==8) {
							pixelBuffer = new Uint8Array(this.response);
						} else if(imageData['PixelRepresentation']==0 && imageData['BitsStored']>8) {
							pixelBuffer = new Uint16Array(this.response);
						} else if(imageData['PixelRepresentation']==1 && imageData['BitsStored']>8) {
							pixelBuffer = new Int16Array(this.response);
						} else {
							pixelBuffer = this.response;
						}
						
						for(var i=0;i<pixelBuffer.length;i++) {
							var pix = pixelBuffer[i];
							minPix = Math.min(minPix,pix);
							maxPix = Math.max(maxPix,pix);
						}
							
						lookupObj = new LookupTable();
						lookupObj.setPixelInfo(minPix,maxPix,imageData['monochrome1']);
						columns = imageData['nativeColumns'];
						loadLookUp(imageData);
						measure(document.getElementById('canvasLayer2'),jQuery('#totalImages').html().indexOf('Frame')<0 ? imgInc : frameInc, imageData['nativeColumns']);
					}
				};
				
				xhr.send();
			} catch(exception) {			
				var imgSrc = jQuery('#' + (seriesUid + "_" + imgInc).replace(/\./g,'_'),window.parent.document).attr('src');
				imageData = parseDicom(null,getParameter(imgSrc,'object'));
				if(imageData['pixelSpacing']!=undefined) {
					jQuery('#pixelSpacing').html((imageData['pixelSpacing'].length>1) ?  imageData['pixelSpacing'][0]+"\\"+imageData['pixelSpacing'][1] : imageData['pixelSpacing']);
				} else {
					jQuery('#pixelSpacing').html("1.0\\1.0");
				}
				columns = imageData['nativeColumns'];
				loadLookUp(imageData);
				measure(document.getElementById('canvasLayer2'),jQuery('#totalImages').html().indexOf('Frame')<0 ? imgInc : frameInc, imageData['nativeColumns']);
			}
			window.parent.enableMeasureContext();
		},1);
	} else {
		deactivateMeasure(toolId);
		doMouseWheel = true;
	}
}

function deactivateMeasure(toolId) {
	disableTool(toolId);
	jQuery('#huDisplayPanel').hide();
	jQuery('#thickLocationPanel').show();
	jQuery('#canvasLayer2').unbind('mousedown').unbind('mousemove').unbind('mouseup');
	window.parent.disableMeasureContext();
	clearMeasurements();
}

function doWindowing(imageData,huDisplay,wlDisplay) {
	var rescaleSlope = parseFloat(imageData['rescaleSlope']), rescaleIntercept = parseFloat(imageData['rescaleIntercept']);
	/*if(rescaleSlope===undefined) {
		rescaleSlope = 1.0;
	}
	
	if(rescaleIntercept===undefined) {
		rescaleIntercept = 0.0;
	}*/
			
	modifiedWC = imageData['windowCenter'],modifiedWW = imageData['windowWidth'];
	
	if(modifiedWC!=undefined && modifiedWC.length>0) {
		if(modifiedWC && modifiedWC.indexOf('|') >=0) {
   	 		modifiedWC = parseInt(modifiedWC.substring(0, modifiedWC.indexOf('|')));
		}
		
		if(modifiedWW && modifiedWW.indexOf('|') >=0) {
		    modifiedWW = parseInt(modifiedWW.substring(0, modifiedWW.indexOf('|')));
		}
	} else {
		var maxVoi = maxPix * rescaleSlope + rescaleIntercept;
		var minVoi = minPix * rescaleSlope + rescaleIntercept;
		
		modifiedWC = (maxVoi+minVoi) /2;
		modifiedWW = maxVoi - minVoi;
		
		wlDisplay.html("WL: "+modifiedWC+" / WW: "+modifiedWW);
	}

	this.windowCenter = modifiedWC;
	this.windowWidth = modifiedWW;	

	lookupObj.setData(modifiedWC, modifiedWW, rescaleSlope, rescaleIntercept, imageData['BitsStored'],imageData['monochrome1']);
	
	this.tmpCanvas = document.createElement('canvas');
	tmpCanvas.width = tmpCanvas.style.width = imageData['nativeColumns'];
	tmpCanvas.height = tmpCanvas.style.height = imageData['nativeRows'];
	
	var tmpCtx = tmpCanvas.getContext('2d');
	
	tmpCtx.fillStyle = 'white';
	tmpCtx.fillRect(0, 0, tmpCanvas.width, tmpCanvas.height);
	
	this.pixelData = tmpCtx.getImageData(0, 0, tmpCanvas.width, tmpCanvas.height);
	this.numPixels = tmpCanvas.width*tmpCanvas.height;
	
	iterateOverPixels();
	
	renderImg();
	jQuery('#loadingView', window.parent.document).hide();
	huDisplay.show();
	jQuery('#applyWLDiv').show();
	
	jQuery('#canvasLayer2').mouseup(function(evt) {
		state.drag = false;
		evt.target.style.cursor = "default";
		jQuery('.contextMenu').hide();
		jQuery('.selected').removeClass('selected');
	}).mousedown(function(evt) {
		if(evt.which==1) {
			doMouseWheel = false;
			state.drag = true;		
			mouseLocX = evt.pageX;
			mouseLocY = evt.pageY; 
	
	       evt.target.style.cursor = "url(images/wincursor.png), auto";
	       
	       evt.preventDefault();
	       evt.stopPropagation();    
		}
	}).mousemove(function(evt) {
		jQuery('.selected',window.parent.document).removeClass('selected');
		var x = parseInt((evt.pageX-state.translationX)/state.scale);
		var y = parseInt((evt.pageY-state.translationY)/state.scale);
		huDisplay.html("X :"+x+" Y :"+y+" HU :"+lookupObj.getPixelAt(pixelBuffer[(y*imageData['nativeColumns'])+x]));

		if(state.drag) {
			var diffX = parseInt(evt.pageX-mouseLocX);
			var diffY = parseInt(evt.pageY-mouseLocY);
			
			modifiedWC=parseInt(modifiedWC)+diffY;
            modifiedWW=parseInt(modifiedWW)+diffX;                    

            if(modifiedWW < 1) {
                modifiedWW = 1;
            }
		
			lookupObj.setWindowingdata(modifiedWC,modifiedWW);
			iterateOverPixels();
			renderImg();
			
			mouseLocX=evt.pageX;
            mouseLocY=evt.pageY;
            
           wlDisplay.html("WL: "+modifiedWC+" / WW: "+modifiedWW);
		}		
	});
	window.parent.enableWindowingContext();
}

function applyPreset(wc,ww) {
	jQuery('.selected',window.parent.document).removeClass('selected');
	jQuery('.contextMenu',window.parent.document).hide();
	if(wc!=undefined) {	
		this.modifiedWC = wc;
		this.modifiedWW = ww;	
		lookupObj.setWindowingdata(modifiedWC,modifiedWW);		
	} else {
		this.modifedWC = windowCenter;
		this.modifiedWW = windowWidth;
		lookupObj.setWindowingdata(windowCenter,windowWidth);
	}
	jQuery('#windowLevel').html("WL: "+modifiedWC+" / WW: "+modifiedWW);
	iterateOverPixels();
	renderImg();
}

function loadLookUp(imageData) {
	lookupObj.setData(modifiedWC, modifiedWW, parseFloat(imageData['rescaleSlope']), parseFloat(imageData['rescaleIntercept']), imageData['BitsStored'],imageData['monochrome1']);	
}

function getPixelValAt(i,j) {

	return lookupObj.getPixelAt(pixelBuffer[j*columns+i]);
}

function doZoom() {
	var img = null;	
	var canvasLayer2 = document.getElementById('canvasLayer2');	
	var canvas = document.getElementById('imageCanvas');		
	var lastY = 0,mY = 0,zoomInc = 1,scaleFac = 1.01,originX = 0,originY = 0;
	
	
	jQuery(canvasLayer2).mousedown(function(e) {
		if(e.which==1) {
			document.body.style.mozUserSelect = document.body.style.webkitUserSelect = document.body.style.userSelect = 'none';
			img = jQuery('#' + (seriesUid + "_" + imgInc).replace(/\./g,'_'), window.parent.document).get(0);
			originX = e.offsetX || (e.pageX-canvas.offsetLeft);
			originY = lastY = e.offsetY || (e.pageY-canvas.offsetTop);
			mY = lastY;
			state.drag = true;
			
			e.preventDefault();
	   		e.stopPropagation();
	   		e.target.style.cursor = "url(images/zoomin.png), auto";
	   		loadPreview(img);
		}
	}).mousemove(function(e1) {	
		lastY = e1.offsetY || (e1.pageY-canvas.offsetTop);
		
		if(state.drag) {
			if(lastY<mY) {
				zoomInc = 1;
			} else {
				zoomInc = -1;
			}
			
			var imgPosX = (originX-state.translationX)/state.scale;
			var imgPosY = (originY-state.translationY)/state.scale;		
		
			state.scale*=Math.pow(scaleFac,zoomInc);
		
			var newX = (imgPosX * state.scale)+state.translationX;
			var newY = (imgPosY * state.scale)+state.translationY;
		
			state.translationX+=(originX-newX);
			state.translationY+=(originY-newY);			
			
			/*renderOffScreenCanvas(offScreenCanvas,img);
			ctx.save();
			ctx.clearRect(0,0,canvas.width,canvas.height);			
			ctx.drawImage(offScreenCanvas,0,0);
			ctx.restore();*/
			
			showImg(null,img);
			jQuery('#zoomPercent').html('Zoom: ' + parseInt(state.scale * 100) + '%');
			
		
			mY = lastY;
			
			drawoutline();
			drawAllShapes();
		}
	}).mouseup(function(e3) {
		state.drag = false;
		e3.target.style.cursor = "default";
	});
}

function stopZoom() {
	jQuery('#canvasLayer2').unbind('mousedown mousemove mouseup');
}

/*function renderOffScreenCanvas(offScreenCanvas,image) {	
	var ctx = offScreenCanvas.getContext('2d');
	
	ctx.save();
	ctx.setTransform(1,0,0,1,0,0);		
	ctx.clearRect(0,0,offScreenCanvas.width,offScreenCanvas.height);	

	if(state.vflip) {
		ctx.translate(0,offScreenCanvas.height);
		ctx.scale(1,-1);
	}
	
	if(state.hflip) {
		ctx.translate(offScreenCanvas.width,0);
		ctx.scale(-1,1);
	}
	
	if(state.rotate!=0) {
		ctx.translate(offScreenCanvas.width/2,offScreenCanvas.height/2);
		ctx.rotate(state.rotate===90 ? Math.PI/2 : state.rotate===180? Math.PI : (Math.PI*3)/2);
		ctx.translate(-offScreenCanvas.offScreenCanvas/2,-canvas.height/2);	   
	}	
	
	ctx.translate(state.translationX, state.translationY);	
	ctx.scale(state.scale,state.scale);		

	ctx.drawImage(image,0,0);
	ctx.restore();
}*/

function applyWL() {
	windowCenter = modifiedWC;
	windowWidth = modifiedWW;	
	getWLAppliedImages();
	doMouseWheel = true;	
}

function getWLAppliedImages() {
	var tmp_Ser_Uid = seriesUid.replace(/\./g, '_')+"_1";
	var imgs = jQuery('#' + tmp_Ser_Uid,parent.document).parent().children();	
	jQuery('#applyWLDiv').attr('divState','true');
	
	imgs.each(function(){
		var imgSrc = jQuery(this).attr('src');
		
		if(imgSrc.indexOf('windowCenter') >= 0) {
            imgSrc = imgSrc.substring(0, imgSrc.indexOf('&windowCenter='));
        }
		
		imgSrc += '&windowCenter=' + windowCenter + '&windowWidth=' + windowWidth;
        jQuery(this).attr('src', imgSrc);
	});	
}