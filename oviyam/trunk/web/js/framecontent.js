var imgInc=1, inc = 5,total,frameInc=1;
var seriesUid;
var data;
var state = {translationX : 0,translationY: 0,scale: 0,vflip: false,hflip: false,rotate: 0,invert: false,drag: false};
var navState = {screenNavImgFactor: 0.15,navigationImgFactor:0.3,scale:0,width:0,height:0,outline:{x:0,y:0,w:0,h:0},drag:false};
var doMouseWheel = true;
var loopTimer = null;
var isSlider = false;
var pixelBuffer = null, lookupObj = null, pixelData = null, tmpCanvas = null, numPixels = 0;
var windowCenter='',windowWidth,modifiedWC,modifiedWW,maxPix=0,minPix=0;
var mouseLocX;
var mouseLocY;
var mousePressed; 
var columns;

jQuery('#ImagePane').ready(function() {

	var ht = jQuery(window).height() - 3 + 'px';
    jQuery('body').css('height',ht );
    var width = jQuery(window).width()-3+'px';
    jQuery('body').css('width',width );

    jQuery("#frameSrc").html(window.location.href);    
    parent.window.addEventListener('selection',onTileSelection,false);
    parent.window.addEventListener('ToolSelection',onToolSelection,false); 
    parent.window.addEventListener('sync',synchronize,false);    
    window.parent.createEvent('selection',{"ImagePane":jQuery('#ImagePane')});
    loadImage(); 
    loadTextOverlay();
    
    var canvasDiv = jQuery('#canvasDiv');
    canvasDiv.on('mousewheel DOMMouseScroll', function (e) {
    	
    	if(jQuery('body').css('border').indexOf('none')>=0){ 
	    	window.parent.createEvent('selection',{"ImagePane":jQuery('#ImagePane')});
    	}
    	if(doMouseWheel) {
			if (e.originalEvent.wheelDelta < 0 || e.originalEvent.detail > 0) {
				nextImage();
			} else {
				prevImage();
			}		
		}
		//prevent page fom scrolling
		return false;	
	});
	
	jQuery(document).keydown(function(e) {
		if(doMouseWheel) {
		    if(e.which == 38 || e.which == 37) {        	
		        prevImage();
		    } else if(e.keyCode == 40 || e.keyCode == 39) {
			    nextImage();
		    }
	    }
    });
    
    var oy,ny;
    canvasDiv.mousedown(function(e) {
		if(jQuery('#tool').text()==='stackImage' && doMouseWheel) {
			if(e.which==1) {
				oy = e.pageY;
				state.drag = true;			
			}
			
			jQuery('#canvasDiv').mousemove(function(e1) {
				if(jQuery('#tool').text()==='stackImage' && state.drag) {
					ny = e1.pageY;
					if( (oy-ny) >inc) {
						prevImage();
						oy = ny;
					} else if( (oy-ny) < -(inc)) {
						nextImage();
						oy = ny;
					}
				}
			});
			
			jQuery('#canvasDiv').mouseup(function(e2) {
				state.drag = false;
			});
			
			e.preventDefault();
			e.stopPropagation();
		}
    }); 
    
    canvasDiv.click(function(e) {
    	window.parent.createEvent('selection',{"ImagePane":jQuery('#ImagePane')});
    });
    
    jQuery('#canvasLayer2').dblclick(function(e) {
    	toggleResolution();
    });
    
    window.addEventListener('resize', resizeCanvas, false);  
   	jQuery('#tool').html('');
   	
   	jQuery.get("UserConfig.do", {
        'settings':'viewerSlider',
        'todo':'READ'
    }, function(data){
        if(data.trim() == 'show') {
            isSlider = true;
            loadSlider();
        }
    },'text');	    
     
	loadInstanceText(true);
    
    if(window.parent.displayScout && (jQuery('#modalityDiv').html().indexOf('CT')>=0 || jQuery('#modalityDiv').html().indexOf('MR')>=0)) {
		Localizer.drawScout();
	}	
	
	if(window.parent.syncEnabled) {
			window.parent.createEvent('sync',{forUid:jQuery('#forUIDPanel').html(),fromTo:getFromToLoc()});
	}	
});

jQuery(document).mouseup(function(e) {
	window.parent.createEvent("ToolSelection",{tool:"mouseup"});
});

function loadImage() {
	var src = jQuery('#frameSrc').html();
	seriesUid = getParameter(src,'series');
	var instanceNo = getParameter(src,'instanceNumber');
	imgInc = instanceNo!=null ? parseInt(instanceNo)+1 : 1;	

	var img = jQuery('#' + (seriesUid+"_"+imgInc).replace(/\./g,'_'), window.parent.document).get(0);  	

	if(img.src.indexOf('SR_Latest.png')>=0) {
		loadSR(jQuery(img).attr('imgSrc'));
	} else if(img.src.indexOf('pdf.png')>=0) {
		loadPDF(jQuery(img).attr('imgSrc'));
	} else {
		eliminateRawData();
	}
	jQuery('#loadingView', window.parent.document).hide();	
	jQuery('iframe',window.parent.document).css('visibility','visible');
}

function eliminateRawData() {
	try {
		showImage(seriesUid + "_" + imgInc);
	} catch(err) {
		if(err=='rawdata') {
			imgInc = imgInc+1;
			eliminateRawData();
		}
	}
}

function showImage(imgSrc) {
	var image = jQuery('#' + imgSrc.replace(/\./g,'_'), window.parent.document).get(0);  	
	var canvas = document.getElementById('imageCanvas');

	
	var vWidth = canvas.parentNode.offsetWidth;
	var vHeight = canvas.parentNode.offsetHeight;  
	
	if(vWidth!=0 && vHeight!=0) {
		canvas.width = vWidth;
		canvas.height = vHeight;
		canvas.style.width = vWidth;
		canvas.style.height = vHeight;      
		
		var top = (canvas.parentNode.offsetHeight-canvas.height) / 2;
		canvas.style.marginTop = parseInt(top) + "px";

		var left = (canvas.parentNode.offsetWidth - canvas.width) / 2;
		canvas.style.marginLeft = parseInt(left) + "px";

		var siblings = jQuery(canvas).siblings();
		for(var j=0; j<siblings.length; j++) {    	
			var tmpCanvas = siblings.get(j);
			if(tmpCanvas != null) {
			    tmpCanvas.width = vWidth;
			    tmpCanvas.height = vHeight;
			    tmpCanvas.style.width = vWidth;
			    tmpCanvas.style.height = vHeight;
			    tmpCanvas.style.marginTop = parseInt(top) + "px";
			    tmpCanvas.style.marginLeft = parseInt(left) + "px";
			}
		};
	}

	state.scale = Math.min(canvas.width/image.naturalWidth, canvas.height/image.naturalHeight);	
	state.translationX = (canvas.width- state.scale * image.naturalWidth)/2;
	state.translationY = (canvas.height- state.scale * image.naturalHeight)/2;
	showImg(imgSrc,null,true);

	jQuery('#zoomPercent').html('Zoom: ' + parseInt(state.scale * 100) + '%');
	jQuery('#imageSize').html('Image size:' + image.naturalWidth + "x" + image.naturalHeight);
	jQuery('#viewSize').html('View size:' + canvas.width + "x" + canvas.height);
}

function showImg(imgSrc,image,updatePreview) {
	var image = imgSrc!=null ? jQuery('#'+imgSrc.replace(/\./g,'_'), window.parent.document).get(0) : image;
	if(image.src.indexOf('rawdata.png')>=0) {
		throw 'rawdata';
		return;
	}
	var canvas = document.getElementById('imageCanvas');
	var ctx = canvas.getContext('2d');
	
	ctx.save();
	ctx.setTransform(1,0,0,1,0,0);		
	ctx.clearRect(0,0,canvas.width,canvas.height);	

	if(state.vflip) {
		ctx.translate(0,canvas.height);
		ctx.scale(1,-1);
	}
	
	if(state.hflip) {
		ctx.translate(canvas.width,0);
		ctx.scale(-1,1);
	}
	
	if(state.rotate!=0) {
		ctx.translate(canvas.width/2,canvas.height/2);
		ctx.rotate(state.rotate===90 ? Math.PI/2 : state.rotate===180? Math.PI : (Math.PI*3)/2);
		ctx.translate(-canvas.width/2,-canvas.height/2);	   
	}	
	
	ctx.translate(state.translationX, state.translationY);	
	ctx.scale(state.scale,state.scale);		

	ctx.drawImage(image,0,0);
	ctx.restore();
	
	if(state.invert) {
		window.parent.doInvert(jQuery('#imageCanvas').get(0),false);
	}
	
	if(updatePreview===true) {
		jQuery('#zoomPercent').html('Zoom: ' + parseInt(state.scale * 100) + '%');
		jQuery('#imageSize').html('Image size:' + image.naturalWidth + "x" + image.naturalHeight);
		jQuery('#viewSize').html('View size:' + canvas.width + "x" + canvas.height);
		loadPreview(image);		
	}	
}

function loadTextOverlay() {
	jQuery('#patName').html(window.parent.pat.pat_Name);
	jQuery('#patID').html(window.parent.pat.pat_ID);
	jQuery("#patGender").html(window.parent.pat.pat_gender);
	jQuery("#studyDate").html(window.parent.pat.studyDate);
	jQuery("#studyDesc").html(window.parent.pat.studyDesc);

	var src = jQuery('#frameSrc').html();
	jQuery('#seriesDesc').html(getParameter(src,'seriesDesc'));
	jQuery('#modalityDiv').html(getParameter(src,'modality'));
	total = parseInt(getParameter(src,'images'));
	jQuery('#totalImages').html(total>1 ? 'Images:' + (imgInc) + '/ ' + total :'Image:' + (imgInc) + '/ ' + total);
}

function loadInstanceText(checkForUpdate) {
	data = sessionStorage[seriesUid];
	if(data) {
		data = JSON.parse(data)[imgInc-1];
		if(checkForUpdate===true) {
			loadContextMenu();
		}
		if(data) {
			if(data['imageOrientation']!=undefined && data['imageOrientation']!='') {
				var imgOrient = data['imageOrientation'].split("\\");
			
				jQuery('#imgOriRight').html(imgOrient[0]);
		        jQuery('#imgOriBottom').html(imgOrient[1]);
		        jQuery('#imgOriLeft').html(getOppositeOrientation(imgOrient[0]));
		        jQuery('#imgOriTop').html(getOppositeOrientation(imgOrient[1]));
			}
		
			if(windowCenter=='') {
				windowCenter = data['windowCenter'];
				windowWidth = data['windowWidth'];
		
				if(windowCenter && windowCenter.indexOf('|') >=0) {
			   	 	windowCenter = windowCenter.substring(0, windowCenter.indexOf('|'));
		   		}

				if(windowWidth && windowWidth.indexOf('|') >=0) {
					windowWidth = windowWidth.substring(0, windowWidth.indexOf('|'));
				}				
			} 
			jQuery("#windowLevel").html('WL: ' + windowCenter + ' / ' + 'WW: ' + windowWidth);
			
			if(data['numberOfFrames'] != undefined && data['numberOfFrames'] != '') {
				jQuery("#totalImages").html('Frames: ' + frameInc + ' / ' + data['numberOfFrames']);
				total = data['numberOfFrames'];
				jQuery('#multiframe').css('visibility','visible');
				
				var frmSrc = jQuery('#frameSrc').text();				
				if(getParameter(frmSrc,'object')==null) {
					jQuery('#frameSrc').html(frmSrc + "&object=" + data['SopUID']);					
				}
				
				var frameTime = parseFloat(data['frameTime']); 
				if(frameTime>0.0) {
					doAutoplay(frameTime);
				} else {
					doAutoplay(15); // 15 FPS by default
				}
			} else {
				jQuery('#totalImages').html(total>1 ? 'Images:' + (imgInc) + '/ ' + total :'Image:' + (imgInc) + '/ ' + total);
				jQuery('#multiframe').css('visibility','hidden');				
			}
			
			var sliceInfo = '';
			
			if(data['sliceThickness'] != undefined && data['sliceThickness'] != '') {
		    	sliceInfo = 'Thick: ' + parseFloat(data['sliceThickness']).toFixed(2) + ' mm ';
			}

			if(data['sliceLocation'] != undefined && data['sliceLocation'] != '') {
			    sliceInfo += 'Loc: ' + parseFloat(data['sliceLocation']).toFixed(2) + ' mm';
			}  
			
			jQuery('#thickLocationPanel').html(sliceInfo);
			
			if(data['frameOfReferenceUID'] != undefined) {
				jQuery('#forUIDPanel').html(data['frameOfReferenceUID']);
			}
			
			if(data['refSOPInsUID'] != undefined) {
				jQuery('#refSOPInsUID').html(data['refSOPInsUID']);
			}
			
			if(jQuery('#imgType').html()!=data['imageType']) {
				jQuery('#imgType').html(data['imageType']);
				Localizer.toggleLevelLine();	
			}
			
			jQuery('#imgPosition').html(data['imagePositionPatient']);
		    jQuery('#imgOrientation').html(data['imageOrientPatient']);		    
		    jQuery('#pixelSpacing').html(data['pixelSpacing']);		    
	    }  else if(checkForUpdate===true){
	    	setTimeout("loadInstanceText("+checkForUpdate+")",20);
	    } else {
	    	jQuery('#totalImages').html(total>1 ? 'Images:' + (imgInc) + '/ ' + total :'Image:' + (imgInc) + '/ ' + total);
	    } 
	} else if(checkForUpdate===true) {
		setTimeout("loadInstanceText("+checkForUpdate+")",20);
	} else {
		jQuery('#totalImages').html(total>1 ? 'Images:' + (imgInc) + '/ ' + total :'Image:' + (imgInc) + '/ ' + total);
	}
	setSliderValue();
	jQuery('#serId').html(seriesUid+'_'+imgInc);
	window.parent.setSeriesIdentification();

	if(window.parent.displayScout && (jQuery('#modalityDiv').html().indexOf('CT')>=0 || jQuery('#modalityDiv').html().indexOf('MR')>=0)) {
		Localizer.drawScout();
	}
}

function nextImage() {
	var isMultiframe = jQuery('#totalImages').html().indexOf('Frame')>=0;	 
	var iNo = !isMultiframe ? imgInc : frameInc;
	
	if(isMultiframe) {
		iNo = (frameInc<total)  ? frameInc+1 : 1;
		showImg(getParameter(jQuery('#frameSrc').html(),'object')+"_"+iNo,null,true);		
		this.frameInc = iNo;
	} else {	
		iNo = (iNo<total) ? iNo+1 : 1;
		try {
			showImg(seriesUid + "_" + iNo,null,true);
		} catch(err) { // Some times it might be raw data
			if(err=='rawdata') {
				iNo = imgInc;
				loadInstanceText(false);
				return;
			}
		}
	
		if(window.parent.syncEnabled) {
			window.parent.createEvent('sync',{forUid:jQuery('#forUIDPanel').html(),fromTo:getFromToLoc()});
		}
		this.imgInc = iNo;
	}
	loadInstanceText(false);
	clearMeasurements();		
	
}

function prevImage() {
	var isMultiframe = jQuery('#totalImages').html().indexOf('Frame')>=0;
	var iNo = !isMultiframe ? imgInc : frameInc;	 

	if(isMultiframe) { 	
		iNo = (iNo>1) ? iNo-1 : total;
		showImg(getParameter(jQuery('#frameSrc').html(),'object')+"_"+iNo,null,true);		
		this.frameInc = iNo;
	} else {
		iNo = (iNo>1) ? iNo-1 : total;
		try {
			showImg(seriesUid + "_" + iNo,null,true);		
		} catch(err) { // Some times it might be raw data
			if(err=='rawdata') {
				iNo = imgInc;
				loadInstanceText(false);
				return;
			}
		}
		
		if(window.parent.syncEnabled) {
			window.parent.createEvent('sync',{forUid:jQuery('#forUIDPanel').html(),fromTo:getFromToLoc()});
		}
		this.imgInc = iNo;
	}
	loadInstanceText(false);
	clearMeasurements();
}

function getParameter(queryString, parameterName) {
    //Add "=" to the parameter name (i.e. parameterName=value);
    var parameterName = parameterName + "=";
    if(queryString.length > 0) {
        //Find the beginning of the string
        var begin = queryString.indexOf(parameterName);
        if(begin != -1) {
            //Add the length (integer) to the beginning
            begin += parameterName.length;
            var end = queryString.indexOf("&", begin);
            if(end == -1) {
                end = queryString.length;
            }
            return unescape(queryString.substring(begin, end));
        }

        return null;
    }
}

function onTileSelection(e) {
	if(e.detail.ImagePane.is(jQuery('#ImagePane'))) {
		jQuery('body').css('border','1px solid rgb(255, 138, 0)');
		if(window && window.parent.displayScout && jQuery('#tool').text()!='measure') {
			Localizer.hideScoutLine();
			Localizer.drawScout();
		}	
		var tool = jQuery('#tool').text();
		jQuery('.toggleOff',window.parent.document).not('#scoutLine').removeClass('toggleOff');
		if(tool!='') {
			jQuery('#'+tool,window.parent.document).addClass('toggleOff');	
		}
		if(tool=='measure') {
			init(jQuery('#totalImages').html().indexOf('Frame')>=0 ? frameInc : imgInc);
		}
	} else {
		jQuery('body').css('border','none');
	}
}

function resizeCanvas() { //To resize the canvas on any screen size change
	var height = jQuery(window).height() - 3;
    jQuery('body').css('height',height +"px");
    var width = jQuery(window).width()-3;
    jQuery('body').css('width',width + "px");
    
    var imgSrc = seriesUid + "_" + imgInc;
    showImage(imgSrc);
}

function loadPDF(src) {
	var imgSrc = 'Image.do?serverURL=' + parent.pat.serverURL + '&study=' + getParameter(src,'study') + '&series=' + seriesUid + '&object=' + getParameter(src,'object') + '&rid=' + getParameter(src,'rid');
	var html = '<object id="PDFPlugin" type="application/pdf" data="' + imgSrc + '"';
    html += ' width="770" height="550">';
    html += '</object>';

    jQuery('#PDFContent').html(html);
    jQuery('#PDFContent').css('visibility','visible');
    
     if(jQuery('#canvasDiv').html() != null) {
        var pHeight = jQuery('#PDFContent').parent().css('height');
        jQuery('#PDFPlugin').css('height',pHeight);
    }
    jQuery('#canvasDiv').css('height','0px');
}

function loadSR(src) {
	var imgSrc = 'Image.do?serverURL=' + parent.pat.serverURL + '&study=' + getParameter(src,'study') + '&series=' + seriesUid + '&object=' + getParameter(src,'object');
	imgSrc += '&contentType=text/html';
    jQuery('#SRContent').load(imgSrc);
    jQuery('#SRContent').css('visibility','visible');
    jQuery('#SRContent').css('background-color','white');
    jQuery('#SRContent').css('overflow','auto');
    jQuery('#canvasDiv').css('height','0px');
    jQuery('#canvasDiv').css('display','none');
    jQuery('.textOverlay:not(#huDisplayPanel)').hide();
}

function toggleResolution() {
	if(jQuery('#tool').html()!='measure') {
		var canvas = document.getElementById('imageCanvas');
		var image = jQuery('#'+(seriesUid + "_" + imgInc).replace(/\./g,'_'), window.parent.document).get(0);			
		
		if(state.scale==1.0) { 			
			var scaleFac = Math.min(canvas.width/image.naturalWidth, canvas.height/image.naturalHeight);
			state.scale = scaleFac;
			state.translationX = (canvas.width- state.scale * image.naturalWidth)/2;
			state.translationY = (canvas.height- state.scale * image.naturalHeight)/2;
			showImg(null,image,false);
		} else {
			state.scale = 1.0;
			state.translationX = (canvas.width- state.scale * image.naturalWidth)/2;
			state.translationY = (canvas.height- state.scale * image.naturalHeight)/2;
			showImg(null,image,false);
			if(jQuery('#tool').html()!="move") {
				activateMove("move");
			}
		}
		jQuery('#zoomPercent').html('Zoom: ' + parseInt(state.scale * 100) + '%');
		drawoutline();
	}
}

String.prototype.replaceAll = function(pcFrom, pcTo){
    var i = this.indexOf(pcFrom);
    var c = this;
    while (i > -1){
        c = c.replace(pcFrom, pcTo);
        i = c.indexOf(pcFrom);
    }
    return c;
}

function parseDicom(imageData,sopUID) {	
	var wadoUrl = window.parent.pat.serverURL + "/wado?requestType=WADO&contentType=application/dicom&studyUID=" + window.parent.pat.studyUID + "&seriesUID=" + seriesUid + "&objectUID=" + (imageData!=null ? imageData['SopUID'] : sopUID);

	var reader = new DicomInputStreamReader();

	if (!(!(wadoUrl.indexOf('C-GET') >= 0) && !(wadoUrl.indexOf('C-MOVE') >= 0))) {
		var urlTmp = "Wado.do?study=" + getParameter(wadoUrl, "studyUID")
				+ "&object=" + getParameter(wadoUrl, "objectUID")
				+ "&contentType=application/dicom";
		reader.readDicom(urlTmp);
	} else {
		reader.readDicom("DcmStream.do?wadourl="
						+ wadoUrl.replaceAll("&", "_"));
	}	

	var dicomParser = new DicomParser(reader.getInputBuffer(), reader.getReader());
	dicomParser.parseAll();	
	imageData = dicomParser.imgData;	
	
	this.minPix = dicomParser.minPix;
	this.maxPix = dicomParser.maxPix;	
	this.pixelBuffer = dicomParser.pixelBuffer;	
	this.lookupObj = new LookupTable();
	
	lookupObj.setPixelInfo(this.minPix,this.maxPix,imageData['monochrome1']);
	columns = imageData['nativeColumns'];
	return imageData;
}

function renderImg() {
	var canvas = document.getElementById('imageCanvas');
	var ctx = canvas.getContext('2d');
	ctx.save();
	ctx.setTransform(1,0,0,1,0,0);
	ctx.clearRect(0,0,canvas.width,canvas.height);
	
	if(state.vflip) {
		ctx.translate(0,canvas.height);
		ctx.scale(1,-1);
	}
	
	if(state.hflip) {
		ctx.translate(canvas.width,0);
		ctx.scale(-1,1);
	}
	
	if(state.rotate!=0) {
		ctx.translate(canvas.width/2,canvas.height/2);
		ctx.rotate(state.rotate===90 ? Math.PI/2 : state.rotate===180? Math.PI : (Math.PI*3)/2);
		ctx.translate(-canvas.width/2,-canvas.height/2);	   
	}	
	
	ctx.translate(state.translationX, state.translationY);	
	ctx.scale(state.scale,state.scale);	

	ctx.drawImage(tmpCanvas,0,0);
	ctx.restore();	
}

function iterateOverPixels() {
	var canvasIndex = 3, pixelIndex = 0;
	var localData = pixelData.data;
	
	lookupObj.calculateLookup();
    var lookupTable=lookupObj.ylookup;    
	
	while(pixelIndex<numPixels) {
		localData[canvasIndex] = lookupTable[pixelBuffer[pixelIndex++]];
		canvasIndex+=4;
	}
	tmpCanvas.getContext('2d').putImageData(pixelData,0,0);
}

function doAutoplay(frameTime) {
	if(loopTimer==null) {
		jQuery('#loopSlider',window.parent.document).slider({max:frameTime*2,value: frameTime});
		parent.loopSpeed = frameTime;
		window.parent.document.getElementById('loopChkBox').checked = true;
		doLoop(true);
	}
}

//Preview
function loadPreview(image) {
	var imageCanvas = document.getElementById("imageCanvas");
	var previewCanvas = document.getElementById("previewCanvas");
	var highlightCanvas = document.getElementById("highlightCanvas");
	navState.width = parseInt(imageCanvas.width*navState.navigationImgFactor);
	navState.height = parseInt(navState.width*image.naturalHeight/image.naturalWidth);
	var scrNavImgWidth = parseInt(imageCanvas.width*navState.screenNavImgFactor);
	navState.scale = scrNavImgWidth/navState.width;
	previewCanvas.width = highlightCanvas.width = getScreenNavImageWidth();
	previewCanvas.height = highlightCanvas.height = getScreenNavImageHeight();
	var context = previewCanvas.getContext('2d');
	context.drawImage(image,0,0,getScreenNavImageWidth(),getScreenNavImageHeight());	
	drawoutline();
	addNavigationListener(highlightCanvas); 
}

function getScreenNavImageWidth() {
	return parseInt(navState.scale*navState.width);
}

function getScreenNavImageHeight() {
	return parseInt(navState.scale*navState.height);
}

function getScreenImageWidth() {
	return parseInt(state.scale*parseInt(jQuery('#imageSize').html().split("x")[0].split(":")[1]));
}

function getScreenImageHeight() {
	return parseInt(state.scale*jQuery('#imageSize').html().split("x")[1]);
}

function addNavigationListener(highlightCanvas) {
	var context = highlightCanvas.getContext('2d');	
	var startCoords = [],img = null;
	
	jQuery(highlightCanvas).mousedown(function(e) {	
		if(e.which==1) {
			e.preventDefault();
			e.stopPropagation();	
			if(e.offsetX>=navState.outline.x && e.offsetX*navState.scale<=navState.outline.x+(navState.outline.w*navState.scale) && e.offsetY>=navState.outline.y && e.offsetY*navState.scale<=navState.outline.y+(navState.outline.h*navState.scale)) {
				navState.drag = true;	
				 startCoords = [
			   	 	e.offsetX - navState.outline.x,
			   		e.offsetY - navState.outline.y
			    ];
			    jQuery(highlightCanvas).css('cursor','move');
			    img = jQuery('#' + (seriesUid + "_" + imgInc).replace(/\./g,'_'), window.parent.document).get(0);
			}
		}
	}).mousemove(function(e1) {
		e1.preventDefault();
		e1.stopPropagation();
		if(navState.drag) {			
			var x = e1.offsetX;
			var y = e1.offsetY;	
			
			navState.outline.x = x-startCoords[0];
			navState.outline.y = y-startCoords[1];	

			context.clearRect(0,0,highlightCanvas.width,highlightCanvas.height);
			context.strokeRect(navState.outline.x,navState.outline.y,navState.outline.w,navState.outline.h);
			
			var point = {x:navState.outline.x,y:navState.outline.y};
			var scrImgPoint = navToScaledImgCoords(point);
			state.translationX = -scrImgPoint.x;
			state.translationY = -scrImgPoint.y;
			showImg(null,img,false);
			drawAllShapes();
		}
	}).mouseup(function(e2) {
		e2.preventDefault();
		e2.stopPropagation();
		navState.drag = false;
		jQuery(highlightCanvas).css('cursor','default');
	});
}

function navToScaledImgCoords(point) {
	return {x:point.x * getScreenImageWidth() / getScreenNavImageHeight(),y: point.y * getScreenImageHeight() / getScreenNavImageHeight()};
}

function needPreview(canvasWidth,canvasHeight) {
	return !(state.translationX>=0 && (state.translationX+getScreenImageWidth())<=canvasWidth && state.translationY>=0 && (state.translationY+getScreenImageHeight())<=canvasHeight);
}

function drawoutline() {
	var imageCanvas = document.getElementById("imageCanvas");
	if(needPreview(imageCanvas.width,imageCanvas.height)) {	
		var highlightCanvas = document.getElementById('highlightCanvas');
		jQuery('#previewCanvas').show();
		jQuery(highlightCanvas).show();
		var x = -state.translationX * getScreenNavImageWidth()/getScreenImageWidth();
		var y = -state.translationY * getScreenNavImageHeight()/getScreenImageHeight();
		var w = imageCanvas.width * getScreenNavImageWidth()/getScreenImageWidth();
		var h = imageCanvas.height * getScreenNavImageHeight()/getScreenImageHeight();
		if(x<0) {
			x = 0;			
		}
		if(y<=0) {
			y =0;
		}

		if(x+w>highlightCanvas.width) {
			w = highlightCanvas.width-x;
		}
		if(y+h>highlightCanvas.height) {
			h = highlightCanvas.height-y;
		}
		navState.outline = {x:x,y:y,w:w,h:h};
		var context = highlightCanvas.getContext('2d');
		context.clearRect(0,0,highlightCanvas.width,highlightCanvas.height);		
		context.strokeStyle="yellow";
		context.strokeRect(x,y,w,h);
	} else {
		jQuery('#previewCanvas').hide();
		jQuery('#highlightCanvas').hide();
	}
}