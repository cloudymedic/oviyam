var wadoURL;
var mouseLocX;
var mouseLocY;

var wcenter;
var wwidth;
var rescale_Slope;
var rescale_Intercept;
var photometric_Interpretation;
var bits_Stored;
var lookupTable;
var huLookupTable;
var pixelBuffer = null;

var row;
var column;
var lookupObj;
var zoomPercent;

var canvas;
var ctx;
var myImageData;
var winEnabled = false;
var tmpCanvas;

String.prototype.replaceAll = function(pcFrom, pcTo){
    var i = this.indexOf(pcFrom);
    var c = this;
    while (i > -1){
        c = c.replace(pcFrom, pcTo);
        i = c.indexOf(pcFrom);
    }
    return c;
}

function mouseDownHandler(evt)
{
    mousePressed=1;
    jQuery('.contextMenu').hide();//To hide the popup if showing

    zoomPercent = jQuery(jcanvas).parent().parent().find('#zoomPercent').html();
    zoomPercent = zoomPercent.substring(zoomPercent.indexOf(":")+1, zoomPercent.indexOf("%"));
    zoomPercent = zoomPercent / 100;    


    if(imageLoaded==1)
    {   
        mouseLocX = evt.pageX;
        mouseLocY = evt.pageY;
        evt.target.style.cursor = "url(images/wincursor.png), auto";
    } else {
    	evt.target.style.cursor = "progress";
    }

    evt.preventDefault();
    evt.stopPropagation();    
}

function mouseupHandler(evt)
{
	mousePressed=0;
	wlApplied = false;
		
	if(imageLoaded == 1) {
		evt.target.style.cursor = "default";
	} else {
		evt.target.style.cursor = "progress";
	}
}

function mousemoveHandler(evt)
{

    try
    {
        if(imageLoaded==1)
        {
            showHUvalue(parseInt(evt.pageX/zoomPercent),parseInt(evt.pageY/zoomPercent));

                if(mousePressed==1)
                {                   
                    var diffX=parseInt((evt.pageX-mouseLocX)/zoomPercent);
                    var diffY=parseInt((mouseLocY-evt.pageY)/zoomPercent);      
                    
                    wc=parseInt(wc)+diffY;
                    ww=parseInt(ww)+diffX;                    

                    if(ww < 1) {
                        ww = 1;
                    }
                    showWindowingValue(wc,ww);
                    lookupObj.setWindowingdata(wc,ww);
                    //genImage();
                    renderImage();
                    mouseLocX=evt.pageX;
                    mouseLocY=evt.pageY;
                    jQuery('.selected').removeClass('selected');

                }          
        }
    }
    catch(err)
    {
    	console.log(err);
    }

}

function changePreset(presetValue)
{
    if(winEnabled) {
        applyPreset(parseInt(presetValue));
    }
}

function changePreset(presetDiv,presetValue)
{
    if(winEnabled) {
    	jQuery('.selected').removeClass('selected');
		jQuery(presetDiv).addClass("selected");
        applyPreset(parseInt(presetValue));
    }
}

function applyPreset(preset)
{
    switch (preset)
    {
        case 1:
            wc=wcenter;
            ww=wwidth;
            lookupObj.setWindowingdata(wc,ww);
            renderImage();
            break;

        case 2:
            wc=350;
            ww=40;
            lookupObj.setWindowingdata(wc,ww);
            renderImage();
            break;

        case 3:
            wc=-600;
            ww=1500;
            lookupObj.setWindowingdata(wc,ww);
            renderImage();
            break;

        case 4:
            wc=40;
            ww=80;
            lookupObj.setWindowingdata(wc,ww);
            renderImage();
            break;

        case 5:
            wc=480;
            ww=2500;
            lookupObj.setWindowingdata(wc,ww);
            renderImage();
            break;

        case 6:
            wc=90;
            ww=350;
            lookupObj.setWindowingdata(wc,ww);
            renderImage();
            break;
    }
    showWindowingValue(wc,ww);
    jQuery('#winContext').hide();	
}

function showHUvalue(x,y)
{
    var t=(y*column)+x;    
    var huValue = "X :"+x+" Y :"+y+" HU :"+huLookupTable[pixelBuffer[t]];
    selectedFrame.find('#huDisplayPanel').html(huValue);
}

function showWindowingValue(wcenter,wwidth)
{
    var winValue = "WL: "+wcenter+" / WW: "+wwidth;
    selectedFrame.find('#windowLevel').html(winValue);
}

function loadDicom() {
    //stop zoom if zoom enabled
    if(zoomEnabled) {
        var zDiv = jQuery('#zoomIn').get(0);
        stopZoom(zDiv);
    }

    // stop move if move enabled
    if(moveEnabled) {
        var mvDiv = jQuery('#move').get(0);
        stopMove(mvDiv);
    }
    
    //Stop stack navigation if enabled
    if(scrollImages) {
    	var mvDiv = jQuery('#stackImage').get(0);
    	doStack(mvDiv);
    }
    
    if(measureEnabled) {
    	doMeasurement(jQuery('#ruler').get(0));
    }
    
    var imgSize = jQuery(jcanvas).parent().parent().find('#imageSize').html().substring(11).split("x");
    row = parseInt(imgSize[1]);
    column = parseInt(imgSize[0]);    

    var queryString = jQuery(jcanvas).parent().parent().find("#frameSrc").html();

    var seriesUID = getParameter(queryString, 'seriesUID');   
    
    var objectUID = getParameter(queryString, 'objectUID');

    var layerCanvas = jQuery(jcanvas).parent().children().get(2);   	

    if(!winEnabled) {    	
        winEnabled = true;
        doMouseWheel = false;        
        if(pat.serverURL.indexOf("wado")>0) {
        	jQuery(jcanvas).parent().parent().find('#applyWLDiv').show();
        }
        jQuery(jcanvas).parent().parent().find('#huDisplayPanel').show();
        jQuery(jcanvas).parent().parent().find('#thickLocationPanel').hide();
           
        jQuery('#windowing').addClass('toggleOff');
		jQuery('#lblWindowing').removeClass('imgOff').addClass('imgOn');  		

		if(objectUID=='null') {
			var instanceNo = parseInt(jQuery(jcanvas).parent().parent().find('#totalImages').html().split(':')[1].split('/')[0])-1;		
			var instData = JSON.parse(sessionStorage[seriesUID])[instanceNo];
			objectUID = instData['SopUID'];
			var windowCenter = instData['windowCenter'].indexOf('|')>=0 ? instData['windowCenter'].substring(0,instData['windowCenter'].indexOf('|')) : instData['windowCenter'];
			var windowWidth = instData['windowWidth'].indexOf('|')>=0 ? instData['windowWidth'].substring(0,instData['windowWidth'].indexOf('|')) : instData['windowWidth'];
			jQuery(jcanvas).parent().parent().find('#windowLevel').html('WL:' + windowCenter + " / WW: " + windowWidth);
		}  

        wadoURL = pat.serverURL + "/wado?requestType=WADO&contentType=application/dicom&studyUID=" + pat.studyUID + "&seriesUID=" + seriesUID + "&objectUID=" + objectUID; 
		parseAndLoadDicom();
		
       jQuery(layerCanvas).mouseup(function(evt) {
            mouseupHandler(evt);
        }).mousedown(function(evt) {
            mouseDownHandler(evt);
        }).mousemove(function(evt) {
            mousemoveHandler(evt);
        });
    } else {
        stopWLAdjustment();
    }
}

function stopWLAdjustment() {
    winEnabled = false;
    doMouseWheel = true;
    var layerCanvas = jQuery(jcanvas).parent().children().get(2);    
    jQuery(jcanvas).parent().parent().find('#applyWLDiv').hide();
    jQuery(jcanvas).parent().parent().find('#thickLocationPanel').show();
    jQuery(jcanvas).parent().parent().find('#huDisplayPanel').hide();    
    jQuery(layerCanvas).unbind('mousedown').unbind('mouseup');

    jQuery('#windowing').removeClass('toggleOff');
    jQuery('#lblWindowing').removeClass('imgOn').addClass('imgOff');   
    
}

function unBindWindowing() {
	if (jQuery(jcanvas).parent().parent().parent().parent().css('border') != '1px solid rgb(255, 138, 0)') {
		jQuery(jQuery(jcanvas).parent().children().get(2)).unbind('mousedown')
				.unbind('mouseup').unbind('mousemove');

		jQuery(jcanvas).parent().parent().find('#applyWLDiv').hide();
		jQuery(jcanvas).parent().parent().find('#thickLocationPanel').show();
		jQuery(jcanvas).parent().parent().find('#huDisplayPanel').hide();
	}
}

function bindWindowing() {
	if (jQuery(jQuery(jcanvas).parent().children().get(2)).data('events') == null) {
		var imgSize = jQuery(jcanvas).parent().parent().find('#imageSize')
				.html().substring(11).split("x");
		row = parseInt(imgSize[1]);
		column = parseInt(imgSize[0]);
		var queryString = jQuery(jcanvas).parent().parent().find("#frameSrc")
				.html();
		wadoURL = pat.serverURL
				+ "/wado?requestType=WADO&contentType=application/dicom&studyUID="
				+ pat.studyUID + "&seriesUID="
				+ getParameter(queryString, 'seriesUID') + "&objectUID="
				+ getParameter(queryString, 'objectUID');
		parseAndLoadDicom();

		if(pat.serverURL.indexOf("wado")>0) {
			jQuery(jcanvas).parent().parent().find('#applyWLDiv').show();
		}
		jQuery(jcanvas).parent().parent().find('#huDisplayPanel').show();
		jQuery(jcanvas).parent().parent().find('#thickLocationPanel').hide();

		jQuery(jQuery(jcanvas).parent().children().get(2)).mouseup(
				function(evt) {
					mouseupHandler(evt);
				}).mousedown(function(evt) {
			mouseDownHandler(evt);
		}).mousemove(function(evt) {
			mousemoveHandler(evt);
		});
	}
}

function getContextPath()
{
    var path = top.location.pathname;
    if (document.all) {
        path = path.replace(/\\/g,"/");
    }
    path = path.substr(0,path.lastIndexOf("/")+1);

    return path;
}

function parseAndLoadDicom()
{
    //alert(wadoURL);
    var reader=new DicomInputStreamReader();

    if( !(!(wadoURL.indexOf('C-GET') >= 0) && !(wadoURL.indexOf('C-MOVE') >= 0))) {
        //var urlTmp = "DcmFile.do?study=" + getParameter(wadoURL, "studyUID") + "&object=" + getParameter(wadoURL, "objectUID");
        var urlTmp = "Wado.do?study=" + getParameter(wadoURL, "studyUID") + "&object=" + getParameter(wadoURL, "objectUID") + "&contentType=application/dicom";
    	reader.readDicom(urlTmp);
    } else {
    	 reader.readDicom("DcmStream.do?wadourl="+wadoURL.replaceAll("&","_"));
    }


    var dicomBuffer=reader.getInputBuffer();
    var dicomReader=reader.getReader();
    var dicomParser=new DicomParser(dicomBuffer,dicomReader);
    dicomParser.parseAll();
    var elementindex=0;
    for(;elementindex<dicomParser.dicomElement.length;elementindex++)
    {
        var dicomElement=dicomParser.dicomElement[elementindex];
        if(dicomElement.name=="windowWidth")
        {
            wwidth=ww=parseFloat(dicomElement.value[0]);
        }
        else if(dicomElement.name=="windowCenter")
        {
            wcenter=wc=parseFloat(dicomElement.value[0]);
        }
        else if(dicomElement.name=="rescaleIntercept")
        {
            rescale_Intercept=parseFloat(dicomElement.value);                        
        }
        else if(dicomElement.name=="rescaleSlope")
        {
            rescale_Slope=parseFloat(dicomElement.value);                        
        } else if(dicomElement.name=="BitsStored") 
        {
        	bits_Stored = parseInt(dicomElement.value);
        } else if(dicomElement.name=="photometricInterpretation")
	    {
	    	photometric_Interpretation = dicomElement.value;
	        if($.trim(photometric_Interpretation) == "MONOCHROME1")
	      	{
	        	invert = true;
	       	}
	    }
    }
    var minPix = dicomParser.minPix;
    var maxPix = dicomParser.maxPix;
    
    pixelBuffer=dicomParser.pixelBuffer;

    lookupObj=new LookupTable();
    
    if(typeof rescale_Slope==="undefined") {
		rescale_Slope = 1.0;
		rescale_Intercept = 0.0;
	}
    
    if(typeof wc==="undefined") {    	

    	var maxVoi = maxPix * rescale_Slope + rescale_Intercept;
    	var minVoi = minPix * rescale_Slope + rescale_Intercept;

    	this.wwidth = ww = maxVoi - minVoi;
    	this.Wcenter = wc = (maxVoi+minVoi) /2;    	
    	
    	
    	showWindowingValue(wc,ww);
    }     

    lookupObj.setData(wc,ww,rescale_Slope,rescale_Intercept, bits_Stored,invert,minPix,maxPix);  
    
    lookupObj.calculateHULookup();
    
    huLookupTable=lookupObj.huLookup;

    ctx = jcanvas.getContext("2d");
    var iNewWidth = jcanvas.width;
    var iNewHeight = jcanvas.height;

    jcanvas.width = column;
    jcanvas.height = row;

	ctx.fillStyle="black";
    ctx.fillRect(0, 0, column, row);

    jcanvas.width = iNewWidth;
    jcanvas.height = iNewHeight;

    initialize();    
    imageLoaded=1;
}

function initialize() {    
	tmpCanvas = document.createElement('canvas');
	var tmpCxt = tmpCanvas.getContext('2d');
	
	tmpCanvas.width = column;
    tmpCanvas.height = row;

    tmpCanvas.style.width = column;
    tmpCanvas.style.height = row;

    tmpCxt.fillStyle = "white";
    tmpCxt.fillRect(0,0,column,row);
    
    myImageData = tmpCxt.getImageData(0,0,column,row);    

    renderImage();
}


function getRenderCanvas() {    
    var canvasImageDataIndex = 3;
     var storedPixelDataIndex = 4;
     var numPixels = column * row;
     
     lookupObj.calculateLookup();
    lookupTable=lookupObj.ylookup;
    var localData = myImageData.data;
    
     while(storedPixelDataIndex<numPixels) {
	     localData[canvasImageDataIndex] = lookupTable[pixelBuffer[storedPixelDataIndex++]];
     	canvasImageDataIndex+=4;
     }   
     
    var tmpCxt = tmpCanvas.getContext('2d'); 
    tmpCxt.putImageData(myImageData,0,0);
}

function renderImage() {
	ctx.setTransform(1,0,0,1,0,0);
	ctx.fillStyle = 'black';
	ctx.fillRect(0,0,jcanvas.width, jcanvas.height);	
	
	getRenderCanvas();
	
	var sw = jcanvas.width;
    var sh = jcanvas.height;
    var xScale = sw / column;
    var yScale = sh / row;
    
    var scaleFac = Math.min(xScale,yScale);    
	
	var dw = (scaleFac * column);
	var dh = (scaleFac * row);
	
	var sx = (sw-dw)/2;
	var sy = (sh-dh)/2;	

	ctx.drawImage(tmpCanvas, 0, 0, column, row, sx, sy, dw, dh);
	    imageLoaded=1;
    jQuery(jQuery(jcanvas).parent().children().get(2)).css('cursor','default');
}

function getWindowingValue() {
    var divVal = selectedFrame.find('#windowLevel').html();
    var values = divVal.split("/");
    wc = values[0].substring(values[0].indexOf(':')+1).trim();
    ww = values[1].substring(values[1].indexOf(':')+1).trim();
}

function retrieveImage1(studyUID, seriesUID, instanceUID) {

    window.requestFileSystem  = window.requestFileSystem || window.webkitRequestFileSystem;

    /*var selServer = $("#availableServers input[type=checkbox]:checked").parent().parent();
    var host = selServer.find('td:nth-child(4)').html();
    var wadoPort = selServer.find('td:nth-child(6)').html(); */

    var xhr = new XMLHttpRequest();
    //var url = 'Image.do?serverURL=http://' + host + ':' + wadoPort + '&study=' + studyUID + '&series=' + seriesUID + '&object=' + instanceUID;
    var url = 'Image.do?serverURL=' + wadoURL.substring(0, wadoURL.indexOf('wado')-1) + '&study=' + studyUID + '&series=' + seriesUID + '&object=' + instanceUID;
    url = url + '&windowCenter=' + wc + '&windowWidth=' + ww;

    xhr.open('GET', url, true);
    xhr.responseType = 'arraybuffer';

    xhr.onload = function(e) {
        if(this.status == 200) {
            window.requestFileSystem(window.TEMPORARY, 1024*1024, function(fs) {
                var fn = '';
                /*if(sopClassUID == '1.2.840.10008.5.1.4.1.1.104.1') {
                fn = instanceUID+'.pdf';
            } else { */
                fn = instanceUID+'.jpg';
                //}

                fs.root.getFile(fn, {
                    create:true
                }, function(fileEntry) {
                    fileEntry.createWriter(function(writer) {
                        writer.onwriteend = function(e) {
                            console.log(fileEntry.fullPath + " created");
                        //updateProgress();
                        }
                        writer.onerror = function(e) {
                            console.log(e.toString());
                        }

                        var bb;
                        if(window.BlobBuilder) {
                            bb = new BlobBuilder();
                        } else if(window.WebKitBlobBuilder) {
                            bb = new WebKitBlobBuilder();
                        }
                        bb.append(xhr.response);

                        /*if(sopClassUID == '1.2.840.10008.5.1.4.1.1.104.1') {
                      	writer.write(bb.getBlob('application/pdf'));
                     } else { */
                        writer.write(bb.getBlob('image/jpeg'));
                    //}
                    }, fileErrorHandler);
                }, fileErrorHandler);
            }, fileErrorHandler);
        }
    };
    xhr.send();
}

function constructWadoUrl() { // Load the dicom file if wado url is null
    //stop zoom if zoom enabled
    if(zoomEnabled) {
        var zDiv = jQuery('#zoomIn').get(0);
        stopZoom(zDiv);
    }

    // stop move if move enabled
    if(moveEnabled) {
        var mvDiv = jQuery('#move').get(0);
        stopMove(mvDiv);
    }

    var imgSize = jQuery(jcanvas).parent().parent().find('#imageSize').html().substring(11).split("x");
    row = parseInt(imgSize[1]);
    column = parseInt(imgSize[0]);
    var queryString = jQuery(jcanvas).parent().parent().find("#frameSrc").html();
    var seriesUID = getParameter(queryString, 'seriesUID');
    var objectUID = getParameter(queryString, 'objectUID');
    
    if(objectUID=='null') {
			var instanceNo = parseInt(jQuery(jcanvas).parent().parent().find('#totalImages').html().split(':')[1].split('/')[0])-1;		
			var instData = JSON.parse(sessionStorage[seriesUID])[instanceNo];
			objectUID = instData['SopUID'];
			var windowCenter = instData['windowCenter'].indexOf('|')>=0 ? instData['windowCenter'].substring(0,instData['windowCenter'].indexOf('|')) : instData['windowCenter'];
			var windowWidth = instData['windowWidth'].indexOf('|')>=0 ? instData['windowWidth'].substring(0,instData['windowWidth'].indexOf('|')) : instData['windowWidth'];
			jQuery(jcanvas).parent().parent().find('#windowLevel').html('WL:' + windowCenter + " / WW: " + windowWidth);
			jQuery(jcanvas).parent().parent().find('#pixelSpacing').html(instData['pixelSpacing']);
		}  

    wadoURL = pat.serverURL + "/wado?requestType=WADO&contentType=application/dicom&studyUID=" + pat.studyUID + "&seriesUID=" + seriesUID + "&objectUID=" + objectUID;      
	parseAndLoadDicom();
}

function getPixelAt(x,y) {
	var t = (y*column)+x;
	return huLookupTable[pixelBuffer[t]];
}