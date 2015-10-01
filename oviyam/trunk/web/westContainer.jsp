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
* Portions created by the Initial Developer are Copyright (C) 2014
* the Initial Developer. All Rights Reserved.
*
* Contributor(s):
* Babu Hussain A
* Devishree V
* Meer Asgar Hussain B
* Prakash J
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

<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@page errorPage="ErrorPage.jsp"%>
<%@taglib prefix="ser" uri="/WEB-INF/tlds/SeriesDetails.tld"%>
<%@taglib prefix="img" uri="/WEB-INF/tlds/ImageInfo.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
	String patName = new String(request.getParameter("patientName")
			.getBytes("ISO-8859-1"), "UTF-8");
	String studyDesc = new String(request.getParameter("studyDesc")
			.getBytes("ISO-8859-1"), "UTF-8");
%>

<html>
	<head>
	
		<style type="text/css">
			.heading {
				font-family: Arial;
			    font-size: 13px;
			    font-weight: bold;
			    padding-left: 3px;
			    width: 100%;
			    white-space: nowrap;
			    text-overflow: ellipsis;
			    overflow: hidden;
			}
			
			.seriesTable {
				table-layout: fixed;
				width: 100%;
				font-family: Arial;
				font-size: 12px;
				border-spacing: 0px;
				padding-left: 2px;
			}
			
			.scale-image {
				width: 30%;
				height: 30%;
			}
		</style>

		<script type="text/javascript">
			var series = new Array();
		
			$(document).ready(function() {
				$($('.image').get(0)).load(function() {
					$($('.image').get(0)).click();
				});
				
				var noOfIter = parseInt($('#iterator').html());
		        for(var i=1;i<noOfIter;i++) {
				  	var img = $('.other' + "_" + i);			      	
		    	  	$(img).each(function() {		    	  		
		      			$(this).attr('src',$(this).attr('link'));
		      		});	          				
		      	}
		      	
		      	jQuery('.other_multiframe').each(function() {
		      		$(this).attr('src',$(this).attr('link'));
		      	});	          
		
				var lastImg = $('.other_'+(noOfIter-1)).last();
				if(lastImg.length==0) { // All images are Multiframe 
					lastImg = $('.other_multiframe').last();
					if(lastImg.length==0) {
						lastImg = $('.image').last();
					}
				}
				
				lastImg.load(function() {
					sessionStorage[pat.studyUID] = JSON.stringify(series);			
					storeSer(series);			
				});		
			}); //document.ready 
		
			$(document).mouseup(function(e) {
				createEvent("ToolSelection", {
					tool : "mouseup"
				});
			});
		
			function changeImgView(but) {
				//var table = $(but).parent().parent().parent().parent();
				//console.log(table.children().find('tr:nth-child(2)').children());
		
				var imgBut = $(but).attr('src');
				var imgCount = 0;
		
				if (imgBut.indexOf("all.png") >= 0) {
					$(but).attr('src', 'images/one.png');
		
					$(but).parent().prev().children().each(
							function() {
								if (imgCount == 0) {
									$(this).css('background-color', '#00F');
								} else {
									$(this).css(
											'background-color',
											!$(this).hasClass('waiting') ? '#a6a6a6'
													: '#464646');
								}
								imgCount++;
							});
		
					imgCount = 0;
		
					$(but).parent().parent().next().children().children().each(
							function() {
								if (imgCount == 0) {
									$(this).css('display', 'inline');
								} else {
									$(this).css('display', 'none');
								}
								imgCount++;
							});
				} else if (imgBut.indexOf("one.png") >= 0) {
					$(but).attr('src', 'images/three.png');
					var serDivs = $(but).parent().prev().children();
					var totserDivs = serDivs.length;
					serDivs.each(function() {
						if (imgCount == 0 || imgCount == Math.round(totserDivs / 2) - 1
								|| imgCount == totserDivs - 1) {
							$(this).css('background-color', '#00F');
						} else {
							$(this).css(
									'background-color',
									!$(this).hasClass('waiting') ? '#a6a6a6'
											: '#464646');
						}
						imgCount++;
					});
		
					imgCount = 0;
		
					var serImgs = $(but).parent().parent().next().children().children();
					var serInsCnt = serImgs.length;
					serImgs.each(function() {
						if (imgCount == 0 || imgCount == Math.round(serInsCnt / 2) - 1
								|| imgCount == serInsCnt - 1) {
							$(this).css('display', 'inline');
						} else {
							$(this).css('display', 'none');
						}
						imgCount++;
					});
				} else {
					$(but).attr('src', 'images/all.png');
		
					$(but).parent().prev().children().each(function() {
						$(this).css('background-color', '#00F');
					});
		
					$(but).parent().parent().next().children().children().each(
							function() {
								$(this).css('display', 'inline');
							});
				}
		
				/* var tmp = $(but).attr('name');
				var tmp = tmp.split("|");
				var tagUrl = "tableContainer.jsp?patient=${param.patient}&study=${param.study}&dcmURL=${param.dcmURL}";
				tagUrl += "&wadoUrl=${param.wadoUrl}";
				tagUrl += "&series=" + tmp[0].trim() + "&numberOfImages=" + tmp[1].trim()+"&toggleImageView=0";
				table.load(encodeURI(tagUrl));*/
			}
		
			function changeSeries(image) {
				var imgSrc = image.src;
		
				if (imgSrc.indexOf('images/SR_Latest.png') > 0) {
					imgSrc = jQuery(image).attr('imgSrc');
				}
		
				if (imgSrc.indexOf('images/pdf.png') > 0) {
					imgSrc = jQuery(image).attr('imgSrc');
				}
		
				parent.selectedFrame = null;//For IE            
		
				var url = 'frameContent.html?';
				url += imgSrc.substring(imgSrc.indexOf('?') + 1);
				url += '&instanceNumber=' + parseInt(image.name - 1);
		
				var actFrame = getActiveFrame();
				$(actFrame).css("visibility", "hidden");
				jQuery('#loadingView', window.parent.document).show();
				actFrame.src = url;
				$('.toggleOff').removeClass('toggleOff');
				$('.imgOn').addClass('imgOff').removeClass('imgOn');
			}
		
			function storeInstanceInfo(totIns, serId) {
				for ( var i = 0; i < series.length; i++) {
					if (serId == series[i].seriesUID) {
						if (series[i].totalImages == totIns) {
							insertInstances('${param.patient}', '${param.study}', serId);
							delete series[i].seriesUID;
							delete series[i].totalImages;
						} else {
							series[i].totalImages = series[i].totalImages + 1;
						}
						break;
					}
				}
			}
		
			function toggle(divider) {
				var div = $(divider);
				if (div.attr('title') === 'Close') {
					div.attr('title', 'Open');
					div.parent().css('width', '1%');
					div.parent().next().css('width', '99%');
					div.css('background', 'url("images/showleft.png")');
					$('#previews').hide();
					$('.heading').hide();
					$('#studyTable').hide();
				} else {
					div.attr('title', 'Close');
					div.parent().css('width', '20%');
					div.parent().next().css('width', '80%');
					div.css('background', 'url("images/hideleft.png")');
					$('#previews').show();
					$('.heading').show();
					$('#studyTable').show();
				}
			}
		
			function showRawDataAlert() {
				jAlert('Raw data storage cannot be shown in viewer!', 'ERROR');
			}
		</script>
	</head>
	<body>
    	<div title="Close" style="width: 17px; height: 17px; cursor: pointer; float: right; background: url('images/hideleft.png');" onclick="toggle(this);"></div>    
        <div id="patName" class="heading" style="color: #FF8A00; padding: 2px 0 3px 3px;"><%=patName%></div>
        <div id="patID" class="heading" style="color:#FF8A00; padding: 0 0 3px 3px;">ID: ${param.patient}</div>
        <table id="studyTable" style="font-family: Arial,Helvitica,Serif; font-size:13px; width: 100%;color: #FF8A00; padding-bottom:3px; border: none;">
            <tbody>
                <tr>
                    <td colspan="2"><%=studyDesc%></td>
                </tr>
                <tr>
                    <td>${param.studyDate}</td>
                    <td align="right" style="padding-right: 15px;">${param.totalSeries} Series</td>
                </tr>
            </tbody>
        </table>
        
        <div id="previews" style="overflow: auto; height: 89%; border-top: 2px solid black;">
        	<c:set var="noOfIteration" value="0"/>        	        	
   		 	<ser:Series patientId="${param.patient}" study="${param.study}" dcmURL="${param.dcmURL}">
        		<c:set var="middle" value="${(numberOfImages+0.5)/2}" />
        		<fmt:formatNumber var="middle" maxFractionDigits="0" value="${middle}" />
       			<fmt:parseNumber var="total" type="number" value="${numberOfImages}" />
			
				<c:if test="${noOfIteration lt total}">
					<c:set var="noOfIteration" value="${total}"/>
				</c:if>

	            <script type="text/javascript">
		            series.push({
			            "seriesUID" : '${seriesId}',
						"totalInstances" : '${numberOfImages}',
						"seriesDesc" : '${seriesDesc}',
						"modality" : '${modality}',
						"seriesNumber" : '${seriesNumber}',
						"seriesDate" : '${seriesDate}',
						"bodyPart" : '${bodyPart}'
		            });
	            </script>

        		<table class="seriesTable" id="${fn:replace(seriesId, '.','_')}_table">
            		<tbody>
               			<img:Image patientId="${param.patient}" study="${studyId}" series="${seriesId}" dcmURL="${param.dcmURL}">   
			        		<c:choose>        
			        			<c:when test="${multiframe=='yes'}">
			        				<tr style="cursor: default; color: #FF8A00; font-size: 12px;">
					    				<td>Multiframe</td>
					    				<td align="right"> <div id="totalImgs" style="width: 90px;">${numberOfFrames} Frames </div></td>
									</tr>
										
									<tr>
										<td colspan="2">
											<table style="table-layout: fixed; width: 100%;">
												<tr>
													<td id="${fn:replace(imageId, '.','_')}" class="seriesImgsIndex" style="width: 94%;" name="${imageId}">
														<div id="${fn:replace(imageId, '.','-')}_${i}" style="background: #00F; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>				    									
													</td>
													<td align="right" style="vertical-align: top;">
														<img class="toggleImgView" src="images/all.png" name="Multiframe" />
													</td>
													</tr>													
														<c:set var="cnt" value="1"/>
													<tr>
													<td colspan="2">
			        			</c:when>
			        			<c:otherwise>
					    			<c:if test="${instanceNumber==1}">
					    				<tr style="cursor: default; color: #FF8A00">
											<td>${seriesDesc}</td>
											<td align="right"> <div id="totalImgs">${numberOfImages} Imgs </div></td>
										</tr>
										
										<tr>
											<td colspan="2">
												<table style="table-layout: fixed; width: 100%;">
													<tr>
														<td id="${fn:replace(seriesId, '.','_')}" class="seriesImgsIndex" style="width: 94%;">
															<c:forEach var="i" begin="1" end="${numberOfImages}">
																<c:choose>
																	<c:when test="${(i == middle) || (i==1) || (i==numberOfImages)}">
																		<div style="background: #00F; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>
																	</c:when>
																	<c:otherwise>
																		<div id="${fn:replace(seriesId, '.','-')}_${i}" style="background: #a6a6a6; width: 5px; height: 5px; float: left;margin: 0 1px 1px;"></div>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</td>
														<td align="right" style="vertical-align: top;">
															<c:choose>
																<c:when test="${numberOfImages > 3}">
																	<img class="toggleImgView" src="images/three.png" name="${seriesId} | ${numberOfImages}" onclick="changeImgView(this)" />
																</c:when>            									
																<c:otherwise>
																	<img class="toggleImgView" src="images/all.png" name="${seriesId} | ${numberOfImages}" />
																</c:otherwise>
															</c:choose>
														</td>
													</tr>
													
													<c:set var="cnt" value="1"/>
													<tr>
														<td colspan="2">
									</c:if>
		    					</c:otherwise>
	        				</c:choose>		            		
											
							<c:choose>
								<c:when test="${modality == 'SR'}"> <!-- Structured Report -->
	                   				 <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="height: 75px;" src="images/SR_Latest.png" class="image" imgSrc="Image.do?serverURL=${param.wadoUrl}&study=${studyId}&series=${seriesId}&object=${imageId}&sopClassUID=${sopClassUID}"  onclick="changeSeries(this);"/>
	               				 </c:when>
	                
	               				<c:when test="${sopClassUID == '1.2.840.10008.5.1.4.1.1.104.1'}"> <!-- Enacpsulated PDF -->
	                    			<img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="${thumbSize}" src="images/pdf.png" class="image" imgSrc="Image.do?serverURL=${param.wadoUrl}&study=${studyId}&series=${seriesId}&object=${imageId}&sopClassUID=${sopClassUID}"  onclick="changeSeries(this)"/>
	               				 </c:when>
	               				 
	               				 <c:when test="${fn:contains(sopClassUID,'1.2.840.10008.5.1.4.1.1.9')}"> <!-- Wave Forms -->
							        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="${thumbSize}" src="images/pdf.png" class="image" imgSrc="Image.do?serverURL=${param.wadoUrl}&study=${studyId}&series=${seriesId}&object=${imageId}&sopClassUID=${sopClassUID}&rid=true"  onclick="changeSeries(this)" />
							    </c:when>
	                
	                			<c:when test="${sopClassUID == '1.2.840.10008.5.1.4.1.1.66'}"> <!-- Raw Data Storage -->
							        <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="${thumbSize}" src="images/rawdata.png" onclick="showRawDataAlert();"/>
							    </c:when>
							    
							    <c:when test="${multiframe == 'yes'}"> <!-- Multiframe -->
							    	<c:choose>
								    	<c:when test="${param.wadoUrl == 'C-GET' || param.wadoUrl == 'C-MOVE'}">
									       <img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="${thumbSize}" class="image" src="Wado.do?dicomURL=${param.dcmURL}&study=${param.study}&series=${seriesId}&object=${imageId}&retrieveType=${param.wadoUrl}&sopClassUID=${sopClassUID}&seriesDesc=${seriesDesc}&images=${numberOfImages}&modality=${modality}"  onclick="changeSeries(this)"/>
									       
									        <c:forEach var="i" begin="1" end="${numberOfFrames}"> 
							                        <img name="${i}" id="${fn:replace(imageId, '.','_')}_${i}" style="${thumbSize} display: none;" class="other_multiframe" link="Wado.do?dicomURL=${param.dcmURL}&study=${param.study}&series=${seriesId}&object=${imageId}&retrieveType=${param.wadoUrl}&frameNumber=${i}"/> 
						                        </c:forEach>
					                        </c:when>
					                        <c:otherwise>
					                        	<img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="${thumbSize}"  src="Image.do?serverURL=${param.wadoUrl}&study=${param.study}&series=${seriesId}&object=${imageId}&sopClassUID=${sopClassUID}&seriesDesc=${seriesDesc}&images=${numberOfImages}&modality=${modality}" class="image" onclick="changeSeries(this)" />
											<c:forEach var="i" begin="1" end="${numberOfFrames}"> 
					        	                <img name="${i}" id="${fn:replace(imageId, '.','_')}_${i}" style="${thumbSize} display: none;" class="other_multiframe" link="Image.do?serverURL=${param.wadoUrl}&study=${param.study}&series=${seriesId}&object=${imageId}&frameNumber=${i}"/>
				        	                </c:forEach> 
					                        </c:otherwise>
				                        </c:choose>			 							                        
				                        </td></tr></table>
							    </c:when>
								                
							    <c:otherwise>	<!-- Normal images -->
							    	<c:choose>	
							    		<c:when test="${param.wadoUrl == 'C-GET' || param.wadoUrl == 'C-MOVE'}">
							    			<c:choose>	
							    				<c:when test="${(instanceNumber == middle) || (instanceNumber==1) || (instanceNumber==numberOfImages)}">
							    					<img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" class="image" style="${thumbSize};" src="Wado.do?dicomURL=${param.dcmURL}&study=${param.study}&series=${seriesId}&object=${imageId}&retrieveType=${param.wadoUrl}&sopClassUID=${sopClassUID}&seriesDesc=${seriesDesc}&images=${numberOfImages}&modality=${modality}" onclick="changeSeries(this)" />
							    				</c:when> 
							    				<c:otherwise>
							    					<img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" class="other_${cnt}" class="image" style="${thumbSize}; display: none;" src="Wado.do?dicomURL=${param.dcmURL}&study=${param.study}&series=${seriesId}&object=${imageId}&retrieveType=${param.wadoUrl}&sopClassUID=${sopClassUID}&seriesDesc=${seriesDesc}&images=${numberOfImages}&modality=${modality}" onclick="changeSeries(this)" />
							    				</c:otherwise>
							    			</c:choose>	
							    		</c:when>
							    		<c:otherwise>
							    			<c:choose>
							    				<c:when test="${(instanceNumber == middle) || (instanceNumber==1) || (instanceNumber==numberOfImages)}">
							    					<img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" style="${thumbSize}" class="image" src="Image.do?serverURL=${param.wadoUrl}&study=${studyId}&series=${seriesId}&object=${imageId}&sopClassUID=${sopClassUID}&seriesDesc=${seriesDesc}&images=${numberOfImages}&modality=${modality}"  onclick="changeSeries(this)"/>
							    				</c:when> 
							    				<c:otherwise>	
							    					<img name="${instanceNumber}" id="${fn:replace(seriesId, '.','_')}_${instanceNumber}" class="other_${cnt} image" style="${thumbSize} display: none;" link="Image.do?serverURL=${param.wadoUrl}&study=${studyId}&series=${seriesId}&object=${imageId}&sopClassUID=${sopClassUID}&seriesDesc=${seriesDesc}&images=${numberOfImages}&modality=${modality}"  onclick="changeSeries(this)"/>
							    				</c:otherwise> 
							    			</c:choose> 
							    		</c:otherwise> 														    		
							    	</c:choose>	 
	                			</c:otherwise>
							</c:choose>
														
					    	<c:set var="cnt" value="${cnt+1}"/>					    							    	
							</img:Image>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</tbody>
	</table>
	</ser:Series>
	<div id="iterator" style="display: none;">${noOfIteration}</div>
	</div>
	</body>
</html>