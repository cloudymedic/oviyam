var isSlider = false;
var inc = 5;
var timer;
var loopSpeed = 500;

//var instances = new Array();

jQuery(document).ready(function() {
    var ht = jQuery(window).height() - 3 + 'px';
    jQuery('body').css('height',ht );
    var width = jQuery(window).width()-3+'px';
    jQuery('body').css('width',width );

    jQuery("#frameSrc").html(window.location.href);
    //initFrame();
    loadContextMenu();

    jQuery.get("UserConfig.do", {
        'settings':'viewerSlider',
        'todo':'READ'
    }, function(data){
        if(data.trim() == 'show') {
            isSlider = true;
            loadSlider();
        }
    },'text');  

    jQuery('#containerBox .toolbarButton').hover(function() {
        var selected = jQuery('#containerBox').find('.current');
        jQuery(selected).attr('class', 'toolbarButton');
        jQuery(selected).children().attr('class', 'imgOff');
        jQuery(this).attr('class', 'toolbarButton current');
        jQuery(this).children().attr('class', 'imgOn');
        jQuery(this).css('cursor', 'pointer');
    }, function() {
        var selected = jQuery('#containerBox').find('.current');
        jQuery(selected).attr('class', 'toolbarButton');
        jQuery(selected).children().attr('class', 'imgOff');
        jQuery(this).css('cursor', 'auto');
    });
      

	if(window.addEventListener) {
		jQuery('#ImagePane').get(0).addEventListener('DOMMouseScroll', handleScroll, false);
	}
	jQuery('#ImagePane').get(0).onmousewheel = handleScroll;	

    jQuery(document).bind('keydown', function(e) {
        var iNo = jQuery(window.parent.jcanvas).parent().parent().find('#totalImages').html();
        iNo = iNo.substring(iNo.indexOf(':')+1, iNo.indexOf("/"));

        if(e.keyCode == 38 || e.keyCode == 37) {
            prevImage(iNo-1);
        } else if(e.keyCode == 40 || e.keyCode == 39) {
            nextImage(iNo-1);
        }
    });    


    //To disable zooming
    if(window.parent.zoomEnabled) {
        window.parent.zoomEnabled = false;
        window.parent.doMouseWheel = true;
        jQuery('#zoomIn', window.parent.document).removeClass('toggleOff');
        jQuery('#zoomIn', window.parent.document).children().attr('class', 'imgOff');
    }

    jQuery('#applyWLDiv').click(function() {
        var qryStrTmp = window.location.href;
        var tmp_seruid = getParameter(qryStrTmp, 'seriesUID') + "_1";
        tmp_seruid = tmp_seruid.replace(/\./g, '_');

        var serCont = jQuery('#' + tmp_seruid, window.parent.document).parent();
        var wc_ww = jQuery('#windowLevel').html().split('/');

        var wind_center = wc_ww[0].match('WL:(.*)')[1].trim();
        var wind_width = wc_ww[1].match('WW:(.*)')[1].trim();

        var imgSrcTmp = serCont.children().get(0).src;
        if(imgSrcTmp.indexOf('Image.do') == -1) {
            serCont.children().each(function() {
                var imgSrc = 'Image.do?serverURL=' + window.parent.pat.serverURL;
                imgSrc += '&study=' + window.parent.pat.studyUID;
                imgSrc += '&series=' + jQuery(this).attr('seruid');
                imgSrc += '&object=' + jQuery(this).attr('sopuid');

                if(imgSrc.indexOf('windowCenter') >= 0) {
                    imgSrc = imgSrc.substring(0, imgSrc.indexOf('&windowCenter='));
                }

                imgSrc += '&windowCenter=' + wind_center + '&windowWidth=' + wind_width;
                jQuery(this).attr('src', imgSrc);
            });
        } else {
            serCont.children().each(function() {
                var imgSrc = jQuery(this).attr('src');

                if(imgSrc.indexOf('windowCenter') >= 0) {
                    imgSrc = imgSrc.substring(0, imgSrc.indexOf('&windowCenter='));
                }

                imgSrc += '&windowCenter=' + wind_center + '&windowWidth=' + wind_width;
                jQuery(this).attr('src', imgSrc);
            });
        }

        window.parent.wlApplied = true;
        window.parent.doMouseWheel = true;
        window.parent.stopWLAdjustment();
    });        
	initFrame();
	window.addEventListener('resize', resizeCanvas, false);		

		 jQuery('#canvasLayer2').mousedown(function(e) {		 	
		 	if(window.parent.scrollImages) {
				var oy = e.pageY;
				var mousedown = true;
				
				jQuery('#canvasLayer2').mousemove(function(e1) {
					if(mousedown) {
						var ny = e1.pageY;
					
						if( (oy-ny) >inc) {
							iNo = jQuery(window.parent.jcanvas).parent().parent().find('#totalImages').html();
					        iNo = iNo.substring(iNo.indexOf(':')+1, iNo.indexOf("/"));
					        prevImage(iNo-1);
						} else if( (oy-ny) < -(inc)) {
							iNo = jQuery(window.parent.jcanvas).parent().parent().find('#totalImages').html();
							iNo = iNo.substring(iNo.indexOf(':')+1, iNo.indexOf("/"));
							nextImage(iNo-1);
						}
					}
				});
				
				jQuery('#canvasLayer2').mouseup(function(e2) {
					mousedown = false;
				});
				
				e.preventDefault();
			    e.stopPropagation();    
		    }	    
	  	  });		  	  

	  	  jQuery('#loopChkBox', window.parent.document).change(function() {	
  			doLoop(jQuery('#loopChkBox', window.parent.document).attr('checked'));	  	  			
	  	  }); 	  	 
  	   doLoop(jQuery('#loopChkBox', window.parent.document).attr('checked'));	  	  			
   		
});  //for document.ready

function resizeCanvas() { //To resize the canvas on any screen size change
	var height = jQuery(window).height() - 3;
    jQuery('body').css('height',height +"px");
    var width = jQuery(window).width()-3;
    jQuery('body').css('width',width + "px");
    
    var currCanvas = document.getElementById('imageCanvas');
	
	var iNo = jQuery(window.parent.jcanvas).parent().parent().find('#totalImages').html();
    iNo = iNo.substring(iNo.indexOf(':')+1, iNo.indexOf("/")).trim();    

	//showImage(getParameter(jQuery('#frameSrc').html(),'seriesUID')+"_"+iNo, currCanvas);   
	    
    currCanvas.width = width-3;
    currCanvas.height = height-3;
    
    var imgSize = jQuery('#imageSize').html().substring(11).split("x");
    row = parseInt(imgSize[1]);
    column = parseInt(imgSize[0]);
    
    var scaleFac = Math.min(width/column, height/row);
    
    var dw = (scaleFac * column);
 	var dh = (scaleFac*row); 
 	
 	var src = getParameter(jQuery('#frameSrc').html(),'seriesUID')+"_"+iNo;
 	var img1 = jQuery('#' + src.replace(/\./g,'_'), window.window.parent.document).get(0);
 	currCanvas.getContext('2d').drawImage(img1, (width-dw)/2, (height-dh)/2, dw,dh);

    jQuery("#zoomPercent").html('Zoom: ' + parseInt(scaleFac * 100) + '%');   
    jQuery('#viewSize').html('View size: ' + width + ' x ' + height);     
}
jQuery('#canvasDiv').ready(function() {
    var queryString = window.location.href;
    var frameSize = getParameter(queryString, 'frameSize');
    if(frameSize == null) {
        setBorder(document.getElementById('imageCanvas'));
    } 
});

function loadSlider() {
    var qsTmp = window.location.href;
    var serUID = getParameter(qsTmp, 'seriesUID');
    var noOfInstances = 0;
    
	var seriesData = JSON.parse(sessionStorage[window.parent.pat.studyUID]);		
	for(var i=0;i<seriesData.length;i++) {
		if((seriesData[i])['seriesUID']==serUID) {
			if((seriesData[i])['totalInstances']>1) {
				jQuery('#trackbar1').slider({
					range: "min",
					value: imgInc+1,
					min: 1,
					max: (seriesData[i])['totalInstances'],
					slide: onTick
				});
			} else {
				jQuery('#footer').hide();
			}
			jQuery('.ui-slider-handle').css('height', '10px');
   			jQuery('.ui-slider-handle').css('width', '10px');
		    jQuery('.ui-slider-horizontal').css('height', '.4em');
		    jQuery('.ui-slider-horizontal').css('top', '8px');
		    jQuery('.ui-slider-horizontal').css('cursor', 'pointer');
			break;
		}
	}
}

/*function sliderHandler(trans, results) {

    var row = results.rows.item(0);

    if(row['NoOfSeriesRelatedInstances'] > 1) {
        jQuery('#trackbar1').slider( {
            range: "min",
            value: imgInc+1,
            min: 1,
            max: row['NoOfSeriesRelatedInstances'],
            slide: onTick
        });
    } else {
        jQuery('#footer').hide();
    }

    jQuery('.ui-slider-handle').css('height', '10px');
    jQuery('.ui-slider-handle').css('width', '10px');
    jQuery('.ui-slider-horizontal').css('height', '.4em');
    jQuery('.ui-slider-horizontal').css('top', '8px');
    jQuery('.ui-slider-horizontal').css('cursor', 'pointer');

}*/

function onTick(event, ui) {
    nextImage(ui.value - 2);
}

function initFrame() {
	jQuery('#patName').html(window.parent.pat.pat_Name);
	jQuery('#patID').html(window.parent.pat.pat_ID);
	jQuery("#patGender").html(window.parent.pat.pat_gender);
	jQuery("#studyDate").html(window.parent.pat.studyDate);
	jQuery("#studyDesc").html(window.parent.pat.studyDesc);
	imageHandler();
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

function setBorder(canvas) {
    var frames = jQuery(window.parent.document).find('iframe');
    for(var i=0; i<frames.length; i++) {
        jQuery(frames[i]).contents().find('#contextmenu1').css('display', 'none');
    }
    frames = null;

    if(window.parent.selectedFrame != null) {
        window.parent.selectedFrame.css('border','none');
    }
    window.parent.selectedFrame = jQuery('canvas').parent().parent().parent().parent();
    window.parent.selectedFrame.css('border','1px solid rgb(255, 138, 0)');
    
    window.parent.unBindWindowing();
    
    var modality = null;

    if(jQuery(canvas).attr('id') != 'imageCanvas') {
        window.parent.jcanvas = jQuery(canvas).parent().find('#imageCanvas').get(0);

        modality = jQuery('#modalityDiv').html();

        if(window.parent.displayScout) {
            if(modality.indexOf("CT") >= 0) {
                Localizer.hideScoutLine();
                Localizer.drawScoutLineWithBorder();
            } else {
                MRLocalizer.hideScoutLine();
                MRLocalizer.drawScoutLineWithBorder();
            }
        }

       /* if(window.parent.scrollImages) {
            window.parent.startStack(jQuery('#canvasLayer2').get(0));
        }*/
        
        if(window.parent.zoomEnabled) {
        	window.parent.bindZoom(window.parent.jcanvas);
        }
        
        if(window.parent.winEnabled) {
        	window.parent.bindWindowing();
        }
        
        if(window.parent.measureEnabled) {
        	window.parent.checkInstance();
        }
        doLoop(jQuery('#loopChkBox', window.parent.document).attr('checked'));	  	  			
        return;
    }

    window.parent.jcanvas = canvas;

    if(window.parent.displayScout) {
        modality = window.parent.pat.modality;
        if(modality.indexOf("CT") >= 0) {
            Localizer.hideScoutLine();
            Localizer.drawScoutLineWithBorder();
        } else {
            MRLocalizer.hideScoutLine();
            MRLocalizer.drawScoutLineWithBorder();
        }
    }    

    /*if(window.parent.scrollImages) {
        window.parent.startStack(jQuery('#canvasLayer2').get(0));
    }*/
}

function handleScroll(event) {
	if(window.parent.doMouseWheel) {
		var frmBdr = jQuery(event.target).parent().parent().parent().parent().css('border');
        if(frmBdr.indexOf('none') >= 0) {
            setBorder(event.target);
        }
            
		var delta = 0;
		if(!event) event = window.event;
		if(event.wheelDelta) {
			delta = event.wheelDelta / 120;
		} else if(event.detail) {
			delta = -event.detail /3;		
		}
		if(delta) {
			var iNo = null;
			if(delta>0) {
				iNo = jQuery(window.parent.jcanvas).parent().parent().find('#totalImages').html();
		        iNo = iNo.substring(iNo.indexOf(':')+1, iNo.indexOf("/"));
		        prevImage(iNo-1);
			} else {
				iNo = jQuery(window.parent.jcanvas).parent().parent().find('#totalImages').html();
		        iNo = iNo.substring(iNo.indexOf(':')+1, iNo.indexOf("/"));
		        nextImage(iNo-1);
			}
		}
		event.returnValue = false;
	}
}

function doLoop(isChecked) {
	clearInterval(timer);
	if(isChecked) {	
		timer = setInterval(function() {
			if(jQuery('canvas').parent().parent().parent().parent().css('border')==='1px solid rgb(255, 138, 0)') {
				if(loopSpeed === window.parent.loopSpeed) {		
					var iNo = jQuery(window.parent.jcanvas).parent().parent().find('#totalImages').html();
					iNo = iNo.substring(iNo.indexOf(':')+1, iNo.indexOf("/"));
					nextImage(iNo-1);
				} else {
					loopSpeed = window.parent.loopSpeed;
					doLoop(isChecked);
				}
			} else {
				clearInterval(timer);
			}
		}, window.parent.loopSpeed);		
	}	
}