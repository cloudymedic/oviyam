var language;
function searchClick(searchBtn) {
	var lang = getCookie('language');
	if (typeof lang == 'undefined' || lang.trim() == 'en_GB') {
	    $.getScript('js/i18n/Bundle.js', function () {
	        language = languages;
	    });
	} else {
	    var fileName = 'js/i18n/' + "Bundle_" + lang + ".js";
	    $.getScript(fileName, function () {
	        language = languages;
	        console.log(language);
	    });
	}
	$('#buttonContainer').find('.ui-state-active').removeClass('ui-state-active');
    var inputFields = $(searchBtn).parent().parent().parent().find('input');
    var searchURL = "queryResult.jsp?";
    inputFields.each(function() {
        if(this.id == 'patId') {
            if(this.value.trim() != '') {
                searchURL += 'patientId=' + this.value.trim();
            }
        } else if(this.id == 'patName') {
            if(this.value.trim() != '') {
                searchURL += '&patientName=' + this.value.trim();
            }
        } else if(this.id == 'accessionNo') {
            if(this.value.trim() != '') {
                searchURL += '&accessionNumber=' + this.value.trim();
            }
       // } else if($(this).siblings()[0].innerHTML == 'Birth Date') {
        } else if($(this).prop('class') == 'bdate hasDatepicker') {
            if(this.value.trim() != '') {
                searchURL += '&birthDate=' + convertToDcm4cheeDate(this.value.trim());
            }
        //} else if($(this).siblings()[0].innerHTML == 'Study Date (From)') {
        } else if($(this).prop('class') == 'fsdate hasDatepicker') {
            if(this.value.trim() != '') {
                searchURL += '&searchDays=between&from=' + convertToDcm4cheeDate(this.value.trim());
            }
        //} else if($(this).siblings()[0].innerHTML == 'Study Date (To)') {
        } else if($(this).prop('class') == 'tsdate hasDatepicker') {
            if(this.value.trim() != '') {
                searchURL += '&to=' + convertToDcm4cheeDate(this.value.trim());
            }
        } else if(this.id == 'studyDesc') {
            if(this.value.trim() != '') {
                searchURL += '&studyDesc=' + this.value.trim();
            }
        } else if(this.id == 'referPhysician') {
            if(this.value.trim() != '') {
                searchURL += '&referPhysician=' + this.value.trim();
            }
        }
    });  // for each
    
    //var modalities = $(searchBtn).parent().prev().find('span')[1].innerHTML.replace(/, /g, '\\');
    //var modalities = $(searchBtn).parent().prev().find('span').html().replace(/\, /g,'\\');
    var modalities = '';
    $(searchBtn).parent().prev().find('.gentleselect-dialog li.selected').each(function(index) {
    	if(index>0) {
    		modalities+='\\' + $(this).text();
    	} else {
    		modalities=$(this).text();
    	}
    });
    if(modalities!='') {
        searchURL += '&modality=' + modalities.trim();
    }   
    
    var divContent = $('.ui-tabs-selected').find('a').attr('href') + '_content';
    $(divContent).html('');   
    
    var dUrl = $('.ui-tabs-selected').find('a').attr('name');

    if (dUrl == null) {
        var msg = "Please select remote server!!!";
        noty({
            text: msg,
            layout: 'topRight',
            type: 'error'
        });

        return;
    }

    $.get("Echo.do?dicomURL=" + dUrl, function (data) {
        if (data == "EchoSuccess") {
        	 if(searchURL.trim()==('queryResult.jsp?')) {
        	    	jConfirm(language['Message'], language['Creteria'], function (doQry) {
        				if(doQry==true) {
        					var wado = $('.ui-tabs-selected').find('a').attr('wadoUrl');
        					searchURL += "&serverURL=" + $('.ui-tabs-selected').find('a').attr('wadoUrl');
        				    searchURL += '&ris=' + wado.substring(0,wado.indexOf('wado'))+"ris/Report.do?studyUID=";
        					doQuery(searchURL,divContent);
        				} 
        		    });   	
        	    } else {
        	    	var wado = $('.ui-tabs-selected').find('a').attr('wadoUrl');
        	    	searchURL += "&serverURL=" + $('.ui-tabs-selected').find('a').attr('wadoUrl');
        	    	searchURL += '&ris=' + wado.substring(0,wado.indexOf('wado'))+"ris/Report.do?studyUID=";
        	    	doQuery(searchURL,divContent);
        	    }  
        } else {
        	var msg = languages.serverUnavailable;
            noty({
                text: msg,
                layout: 'topRight',
                type: 'error'
            });

            return;
        }
        
    });

   
   
} // end of searchClick()

function doQuery(searchURL,divContent) {
	searchURL += "&dcmURL=" + $('.ui-tabs-selected').find('a').attr('name');
	searchURL += '&tabName=' + $('.ui-tabs-selected').find('a').attr('href').replace('#','');
	searchURL += '&tabIndex=' + $('#tabs_div').data('tabs').options.selected;
	searchURL += '&preview=' + $('.ui-tabs-selected').find('a').attr('preview');
	searchURL += "&search=true";

	$(divContent).html('<div id="loading" style="height: 100%; width: 100%; text-align: center; z-index: 10000;"><div style="position: absolute; left: 45%; top: 45%;"><img src="images/overlay_spinner.gif" alt=""><div style="font-size: 12px; font-weight: bold;">Querying...</div></div></div>');
	$('#westPane').html('');

	$(divContent).load(encodeURI(searchURL), function() {
		clearInterval(timer);
		//checkLocalStudies();
	});
}

function resetClick(resetBtn, div) {
    var inputFields = $(resetBtn).parent().parent().parent().find('input');
    inputFields.each(function() {
        this.value = '';
    }); //for each
	$(div).val(0).gentleSelect("update");
}

function convertToDcm4cheeDate(givenDate) {
	let retVal = moment(givenDate, languages.dateFormat).format('YYYYMMDD');
    
    return(retVal);
}

function checkLocalStudies() {
    var myDB = initDB();
    var sql = "select StudyInstanceUID from study";
    myDB.transaction(function(tx) {
        tx.executeSql(sql, [], function(trans, results) {
            for(var i=0; i<results.rows.length; i++) {
                var row = results.rows.item(i);
                var img = document.getElementById(row['StudyInstanceUID']);
                if(img != null) {
                    img.style.visibility = 'visible';
                }
            }
        }, errorHandler);
    });
}