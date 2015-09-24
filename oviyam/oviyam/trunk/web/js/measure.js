var mouseLocX, mouseLocY,curr_Img = -1,nativeColumns;
var drawCanvas = null, context = null;
var tool = '';

var ruler = null,rect = null, oval = null;
var selectedShape = null, selectedHandle = -1;

function measure(canvasLayer2,iNo, columns) {
	drawCanvas = canvasLayer2;
	this.context = drawCanvas.getContext('2d');
	
	init(iNo);
	jQuery('#huDisplayPanel').show();
	jQuery('#loadingView',window.parent.document).hide();
	jQuery(drawCanvas).mousedown(function(e) {
		mousedownHandler(e);
	}).mousemove(function(e){
		mousemoveHandler(e);
	}).mouseup(function(e) {
		mouseupHandler(e);
	});
	tool = jQuery(jQuery('.selectedShape',window.parent.document).get(0)).html().toLowerCase();
	nativeColumns = columns;
}

function init(iNo) {
	var tagPxlSpacing = jQuery('#pixelSpacing').html();
	if(tagPxlSpacing==='') {
		loadInstanceText();
		tagPxlSpacing = jQuery('#pixelSpacing').html();
	}
	var pxlSpacing = tagPxlSpacing.split('\\');
	
	var xPxl = pxlSpacing.length>0?pxlSpacing[0]:1, yPxl = pxlSpacing.length>1?pxlSpacing[1]:1;
	
	if(curr_Img==-1 || curr_Img!=iNo) {
		clearMeasurements();
		curr_Img = iNo;
		ruler = new ovm.shape.ruler(xPxl,yPxl);
		rect = new ovm.shape.rect(xPxl,yPxl);
		oval = new ovm.shape.oval(xPxl,yPxl);
	}
}

function mousedownHandler(evt) {
	jQuery('.contextMenu',window.parent.dcoument).hide();
	state.drag = true;
	
	mouseLocX = evt.pageX - drawCanvas.offsetLeft;
	mouseLocY = evt.pageY - drawCanvas.offsetTop;
	
	if(selectedShape!=null) {
		detectHandle(evt);

		if(selectedHandle==-1 && !selectedShape.isActiveShape(selectedShape.x1,selectedShape.y1,selectedShape.x2,selectedShape.y2,mouseLocX,mouseLocY)) {
			if(selectedShape.getType()!="ruler") {
				selectedShape.measure(selectedShape);
			}
			selectedShape.active = false;
			selectedShape = null;
			drawAllShapes();
		}
	} else {
		detectSelectedShape();
	}
	
	evt.stopPropagation();
	evt.preventDefault();
	evt.target.style.cursor = 'default';
}

function mousemoveHandler(evt) {
	var x = evt.pageX-drawCanvas.offsetLeft;
	var y = evt.pageY-drawCanvas.offsetTop;
	var imgX = parseInt((x-state.translationX)/state.scale);
	var imgY = parseInt((y-state.translationY)/state.scale);
	jQuery('#huDisplayPanel').html("X :"+imgX+" Y :"+imgY+" HU :"+getPixelValAt(imgX,imgY));
	if(state.drag) {
		drawAllShapes();
		
		if(selectedShape==null ) { // New Shape
			switch(tool) { 
				case 'ruler':
					var mouseLocX1 = evt.pageX-drawCanvas.offsetLeft;
					var mouseLocY1 = evt.pageY-drawCanvas.offsetTop;
					ruler.drawRuler(context,mouseLocX,mouseLocY,mouseLocX1,mouseLocY1);
					break;
				case 'rectangle':
					var mouseLocX1 = (evt.pageX - drawCanvas.offsetLeft)-mouseLocX;
					var mouseLocY1 = (evt.pageY - drawCanvas.offsetTop)-mouseLocY;
				
					rect.drawRect(context,mouseLocX,mouseLocY,mouseLocX1,mouseLocY1);
					break;
				case 'oval':
					var mouseLocX1 = (evt.pageX - drawCanvas.offsetLeft)-mouseLocX;
					var mouseLocY1 = (evt.pageY - drawCanvas.offsetTop)-mouseLocY;
				
					oval.drawOval(context,mouseLocX,mouseLocY,mouseLocX1,mouseLocY1);
					break;
			}
		} else if(selectedHandle==-1) { // Move a shape			
			selectedShape.moveShape(selectedShape,(evt.pageX- drawCanvas.offsetLeft),(evt.pageY - drawCanvas.offsetTop));			
			if(selectedShape.getType()!="ruler") {
				selectedShape.mean = selectedShape.stdDev = "";
			}
		} else { // Resizing a shape
			selectedShape.resizeShape(selectedShape,(evt.pageX- drawCanvas.offsetLeft),(evt.pageY - drawCanvas.offsetTop),selectedHandle);
			if(selectedShape.getType()!="ruler") {
				selectedShape.mean = selectedShape.stdDev = "";
			}
		}
	} else if(selectedShape!=null) {
			detectHandle(evt);
	}
}

function mouseupHandler(evt) {
	if(selectedShape==null) { // New Shape
		switch(tool) {
			case 'ruler':
				ruler.createNewLine(mouseLocX, mouseLocY, (evt.pageX-drawCanvas.offsetLeft), (evt.pageY-drawCanvas.offsetTop));
				break;
			case 'rectangle':
				rect.createNewRect(mouseLocX, mouseLocY, (evt.pageX-drawCanvas.offsetLeft)-mouseLocX, (evt.pageY-drawCanvas.offsetTop)-mouseLocY);
				break;
			case 'oval':
				oval.createNewOval(mouseLocX, mouseLocY, (evt.pageX-drawCanvas.offsetLeft)-mouseLocX, (evt.pageY-drawCanvas.offsetTop)-mouseLocY);
				break;
		}
		drawAllShapes();
	}
	state.drag = false;					
}

function drawAllShapes() {
	if(context!=null) {
		context.clearRect(0,0,drawCanvas.width,drawCanvas.height);
		ruler.drawData(context);
		rect.drawData(context);
		oval.drawData(context);
	}
}

function setShape(shape) {
	tool = shape;
	jQuery('.selectedshape',window.parent.document).removeClass('selectedshape');
}	

function detectSelectedShape() {
	if((selectedShape = ruler.getActiveLine(mouseLocX,mouseLocY))!=null ||(selectedShape = rect.getActiveRect(mouseLocX,mouseLocY))!=null || (selectedShape = oval.getActiveOval(mouseLocX,mouseLocY))!=null) {
		selectedShape.active = true;
		drawAllShapes();
		return;
	}
}

function detectHandle(e) {
	selectedHandle = selectedShape.detectHandle(selectedShape,e.pageX-drawCanvas.offsetLeft,e.pageY-drawCanvas.offsetTop,e.target);	
}

function clearMeasurements() {
	curr_Img = -1;
	ruler = rect = oval = null;	
	if(context!=null) {
		context.clearRect(0,0,drawCanvas.width,drawCanvas.height);
	}
}

function deleteSelectedMeasurement() {
	if(selectedShape!=null) {
		selectedShape.removeShape(selectedShape);
		selectedShape = null;
		drawAllShapes();
	}
}