/**
 * @namespace shape
 */
ovm.shape = ovm.shape || {};

/**
 * shapes.js
 * Definition of shapes
*/


ovm.shape.ruler = function(xPixelSpacing,yPixelSpacing) {		
	var handle = 3;	
	var lines = [];
	
	var xPxlSpcing = xPixelSpacing;
	var yPxlSpcing = yPixelSpacing;
	
	this.createNewLine = function(x1,y1,x2,y2) {	
		var len = (this.calculateLength(((x2-state.translationX)/state.scale) - ((x1-state.translationX)/state.scale), ((y2-state.translationY)/state.scale) - ((y1-state.translationY)/state.scale))/10).toFixed(3);

		if(parseInt(len)>0) {
			len+= " cm";
			lines.push({x1:x1,
						y1:y1,
						x2:x2,
						y2:y2,
						len:len,
						active:false,
						getType:this.getType,						
						isActiveShape:this.isActiveShape,
						calculateLength:this.calculateLength,
						getFloatShift:this.getFloatShift,						
						moveShape:this.moveShape,						
						resizeShape:this.resizeShape,
						detectHandle:this.getActiveHandle,
						removeShape:this.removeShape
					});
		}
	};
	
	this.drawData = function(ctx) {		
		ctx.save();		
		ctx.lineWidth='2';
		
		for(var i=0;i<lines.length;i++) {
			var line = lines[i];			
			ctx.strokeStyle=ctx.fillStyle='orange';
			ctx.beginPath();
			ctx.moveTo(line.x1,line.y1);
			ctx.lineTo(line.x2,line.y2);
			ctx.stroke();
			ctx.closePath();
			// Handles		
			ctx.strokeStyle = ctx.fillStyle = line.active? 'red' : 'white';
			ctx.strokeRect(line.x1-handle,line.y1-handle,handle*2,handle*2);
			ctx.strokeRect(line.x2-handle,line.y2-handle,handle*2,handle*2);			
			
			// Text
			ctx.fillStyle = "maroon";
			ctx.globalAlpha = 0.7;
			ctx.fillRect(line.x1,line.y1-14,60,14);
			ctx.globalAlpha = 0.9;
			ctx.fillStyle = "white";
			ctx.fillText(line.len,line.x1+2,line.y1-3);					
		}
		ctx.restore();
	};
	
	this.drawRuler = function(canvasCtx,x1,y1,x2,y2) {
		canvasCtx.save();	
		canvasCtx.strokeStyle=canvasCtx.fillStyle='orange';
		canvasCtx.lineWidth='2';
		
		canvasCtx.beginPath();
		canvasCtx.moveTo(x1,y1);
		canvasCtx.lineTo(x2,y2);
		canvasCtx.stroke();
		canvasCtx.closePath();
		// Handles
		canvasCtx.strokeStyle=canvasCtx.fillStyle='red';
		canvasCtx.strokeRect(x1-handle,y1-handle,handle*2,handle*2);
		canvasCtx.strokeRect(x2-handle,y2-handle,handle*2,handle*2);
		
		//Text
		canvasCtx.fillStyle = "maroon";
		canvasCtx.globalAlpha = 0.7;
		canvasCtx.fillRect(x1,y1-14,60,14);
		canvasCtx.globalAlpha = 0.9;
		canvasCtx.fillStyle = "white";
		canvasCtx.fillText((this.calculateLength(((x2-state.translationX)/state.scale) - ((x1-state.translationX)/state.scale), ((y2-state.translationY)/state.scale) - ((y1-state.translationY)/state.scale))/10).toFixed(3) + " cm",x1+2,y1-3);
		
		canvasCtx.restore();
	};
	
	this.getActiveLine = function(x,y) {
		for(var i=0;i<lines.length;i++) {
			var line = lines[i];
			if(this.isActiveShape(line.x1,line.y1,line.x2,line.y2,x,y)) {
				return line;
			}
		}
		return null;
	};
	
	this.getType = function() {
		return "ruler";
	};
	
	this.isActiveShape = function(x1,y1,x2,y2,x,y) {
		var a = Math.round(Math.sqrt(Math.pow((x2+handle)-(x1+handle),2) + Math.pow((y2+handle)-(y1+handle),2)));
		var b = Math.round(Math.sqrt(Math.pow(x-(x1+handle),2) + Math.pow(y-(y1+handle),2)));
		var c = Math.round(Math.sqrt(Math.pow((x2+handle)-x,2) + Math.pow((y2+handle)-y,2)));
		return (a==b+c);
	}
	
	this.getActiveHandle = function(shape,x,y) {
		if(x>=shape.x1-handle && x<=shape.x1+handle && y>=shape.y1-handle && y<=shape.y1+handle) {
			return 0;
		} else if(x>=shape.x2-handle && x<=shape.x2+handle && y>=shape.y2-handle && y<=shape.y2+handle) {
			return 1;
		} else {
			return -1;
		}
	};
	
	this.moveShape = function(shape,x,y) {
		var dist1 = shape.x2-shape.x1;
		var dist2 = shape.y2-shape.y1;
		shape.x1 = x-(dist1/2);
		shape.y1 = y-(dist2/2);
		shape.x2 = selectedShape.x1+dist1;
		shape.y2 = selectedShape.y1+dist2;
	};
	
	this.resizeShape = function(shape,x,y,direction) {
		switch(direction) {
			case 0:
				shape.x1 = x;
				shape.y1 = y;				
				shape.len = (this.calculateLength(((shape.x2-state.translationX)/state.scale) - ((shape.x1-state.translationX)/state.scale), ((shape.y2-state.translationY)/state.scale) - ((shape.y1-state.translationY)/state.scale))/10).toFixed(3) + " cm";
				break;
			case 1:
				shape.x2 = x;
				shape.y2 = y;
				shape.len = (this.calculateLength(((shape.x2-state.translationX)/state.scale) - ((shape.x1-state.translationX)/state.scale), ((shape.y2-state.translationY)/state.scale) - ((shape.y1-state.translationY)/state.scale))/10).toFixed(3) + " cm";
				break;
		}	
	};
	
	this.calculateLength = function(xDiff,yDiff) {
		var mult = Math.max(this.getFloatShift(xPxlSpcing),this.getFloatShift(yPxlSpcing));
		var xDist = mult * xPxlSpcing * xDiff;
		var yDist = mult * yPxlSpcing * yDiff;
		
		return (Math.sqrt((Math.pow(xDist,2)+Math.pow(yDist,2))/Math.pow(mult,2)));
	};
	
	this.getFloatShift = function(floatNum) {
		var decimalLen = 0;
		var floatElements = floatNum.toString().split('\.');
		if(floatElements.length==2) {
			decimalLen = floatElements[1].length;
		}
		mult = Math.pow(10,decimalLen);
		return mult;
	};
	
	this.removeShape = function(shape) {
		lines.splice(lines.indexOf(shape),1);
	};
};

ovm.shape.rect = function(xPixelSpacing,yPixelSpacing) {
	var handle = 3;	
	var rects = [];
	
	var xPxlSpcing = xPixelSpacing;
	var yPxlSpcing = yPixelSpacing;	
	
	this.createNewRect = function(x1,y1,x2,y2) {
		var area = ((this.calculateLength(((x2-state.translationX)/state.scale),0))/10 * (this.calculateLength(0,((y2-state.translationY)/state.scale)))/10).toFixed(3);
		var mean = this.meanOfRect(x1,y1,x2,y2);
		
		if(area>0) {
			area+= " cm" + String.fromCharCode(178);
			rects.push({x1:x1,
						y1:y1,
						x2:x2,
						y2:y2,
						area:area,
						mean:mean,
						stdDev:this.stdDevOfRect(mean,x1,y1,x2,y2),
						active:false,
						getType:this.getType,
						isActiveShape:this.isActiveShape,
						calculateLength:this.calculateLength,
						getFloatShift:this.getFloatShift,
						meanOfRect:this.meanOfRect,
						stdDevOfRect:this.stdDevOfRect,
						measure:this.measure,
						moveShape:this.moveShape,
						resizeShape:this.resizeShape,
						detectHandle:this.getActiveHandle,
						removeShape:this.removeShape
					});
				}
	};
	
	this.drawData = function(ctx) {		
		ctx.save();		
		
		for(var i=0;i<rects.length;i++) {
			var rect = rects[i];			
			ctx.lineWidth='2';
			ctx.strokeStyle=ctx.fillStyle='orange';
			ctx.strokeRect(rect.x1,rect.y1,rect.x2,rect.y2);
			
			if(rect.active) { // Draw Handles
				ctx.strokeStyle=ctx.fillStyle='red';
				ctx.strokeRect(rect.x1-handle,rect.y1-handle,handle*2,handle*2);
				ctx.strokeRect(rect.x1+(rect.x2/2)-handle,rect.y1-handle,handle*2,handle*2);
				ctx.strokeRect((rect.x1+rect.x2)-handle,rect.y1-handle,handle*2,handle*2);		
				ctx.strokeRect(rect.x1-handle,rect.y1+(rect.y2/2)-handle,handle*2,handle*2);
				ctx.strokeRect((rect.x1+rect.x2)-handle,rect.y1+(rect.y2/2)-handle,handle*2,handle*2);
				ctx.strokeRect(rect.x1-handle,(rect.y1+rect.y2)-handle,handle*2,handle*2);
				ctx.strokeRect(rect.x1+(rect.x2/2)-handle,(rect.y1+rect.y2)-handle,handle*2,handle*2);
				ctx.strokeRect((rect.x1+rect.x2)-handle,(rect.y1+rect.y2)-handle,handle*2,handle*2);

			}
			
			// Draw Text
			ctx.globalAlpha = 0.5;
			ctx.fillStyle = 'maroon';
			ctx.fillRect(rect.x1,rect.y1-50,120,40);
			ctx.globalAlpha = 0.9;
			ctx.fillStyle = 'white';
			ctx.fillText("Area : " + rect.area,rect.x1+2,rect.y1-38);
			ctx.fillText("Mean : " + rect.mean,rect.x1+2,rect.y1-26);
			ctx.fillText("StdDev : " + rect.stdDev,rect.x1+2,rect.y1-14);

			ctx.closePath();			
		}
		ctx.restore();
	};
	
	this.drawRect = function(canvasCtx,x1,y1,x2,y2) {
		canvasCtx.save();	
		canvasCtx.lineWidth='2';
		canvasCtx.strokeStyle=canvasCtx.fillStyle='orange';
		canvasCtx.strokeRect(x1,y1,x2,y2);
		// Handles
		canvasCtx.strokeStyle=canvasCtx.fillStyle='red';
		canvasCtx.strokeRect(x1-handle,y1-handle,handle*2,handle*2);
		canvasCtx.strokeRect(x1+(x2/2)-handle,y1-handle,handle*2,handle*2);
		canvasCtx.strokeRect((x1+x2)-handle,y1-handle,handle*2,handle*2);		
		canvasCtx.strokeRect(x1-handle,y1+(y2/2)-handle,handle*2,handle*2);
		canvasCtx.strokeRect((x1+x2)-handle,y1+(y2/2)-handle,handle*2,handle*2);
		canvasCtx.strokeRect(x1-handle,(y1+y2)-handle,handle*2,handle*2);
		canvasCtx.strokeRect(x1+(x2/2)-handle,(y1+y2)-handle,handle*2,handle*2);
		canvasCtx.strokeRect((x1+x2)-handle,(y1+y2)-handle,handle*2,handle*2);
		
		// Text
		var area = ((this.calculateLength(((x2-state.translationX)/state.scale),0))/10 * (this.calculateLength(0,((y2-state.translationY)/state.scale)))/10).toFixed(3) + " cm" + String.fromCharCode(178);
		canvasCtx.globalAlpha = 0.5;
		canvasCtx.fillStyle = 'maroon';
		canvasCtx.fillRect(x1,y1-50,120,40);
		canvasCtx.globalAlpha = 0.9;
		canvasCtx.fillStyle = 'white';
		canvasCtx.fillText("Area : " + area,x1+2,y1-38);
		canvasCtx.fillText("Mean : " + "",x1+2,y1-26);
		canvasCtx.fillText("StdDev : " + "",x1+2,y1-14);
		
		canvasCtx.restore();
	};
	
	this.getType = function() {
		return "rect";
	};
	
	this.getActiveRect = function(x,y) {
		for(var i=0;i<rects.length;i++) {
			var rect = rects[i];
			if(this.isLineIntersect(rect.x1,rect.y1,rect.x1+rect.x2,rect.y1,x,y) || this.isLineIntersect(rect.x1,rect.y1+rect.y2,rect.x1+rect.x2,rect.y1+rect.y2,x,y) || this.isLineIntersect(rect.x1,rect.y1,rect.x1,rect.y1+rect.y2,x,y) || this.isLineIntersect(rect.x1+rect.x2,rect.y2,rect.x1+rect.x2,rect.y1+rect.y2,x,y)) {
				return rect;
			}
		}
		return null;
	};
	
	this.isLineIntersect = function(x1,y1,x2,y2,x,y) {
		var a = Math.round(Math.sqrt(Math.pow((x2+handle)-(x1+handle),2) + Math.pow((y2+handle)-(y1+handle),2)));
		var b = Math.round(Math.sqrt(Math.pow(x-(x1+handle),2) + Math.pow(y-(y1+handle),2)));
		var c = Math.round(Math.sqrt(Math.pow((x2+handle)-x,2) + Math.pow((y2+handle)-y,2)));
		return (a==b+c);
	};
	
	this.isActiveShape = function(x1,y1,x2,y2,x,y) {
		return (x1<=x && x1+x2>=x && y1<=y && y1+y2>=y) ? true : false;
	};
	
	this.getActiveHandle = function(shape,x,y,target) {
		if(x>=shape.x1-handle && x<=shape.x1+handle && y>=shape.y1-handle && y<=shape.y1+handle) {
			target.style.cursor = 'nw-resize';
			return 0;			
		} else if(x>=(shape.x1+(shape.x2/2)-handle) && x<=(shape.x1+(shape.x2/2)+handle) && y>=shape.y1-handle && y<=shape.y1+handle) {
			target.style.cursor = 'n-resize';
			return 1;			
		} else if(x>=(shape.x1+shape.x2-handle) && x<=(shape.x1+shape.x2+handle) && y>=shape.y1-handle && y<=shape.y1+handle) {
			target.style.cursor = 'ne-resize';
			return 2;			
		} else if(x>=shape.x1-handle && x<=shape.x1+handle && y>=(shape.y1+(shape.y2/2)-handle) && y<=(shape.y1+(shape.y2/2)+handle)) {
			target.style.cursor = 'w-resize';
			return 3;			
		} else if(x>=shape.x1+shape.x2-handle && x<=shape.x1+shape.x2+handle && y>=(shape.y1+(shape.y2/2)-handle) && y<=(shape.y1+(shape.y2/2)+handle)) {
			target.style.cursor = 'e-resize';
			return 4;			
		} else if(x>=shape.x1-handle && x<=shape.x1+handle && y>=(shape.y1+shape.y2-handle) && y<=(shape.y1+shape.y2+handle)) {
			target.style.cursor = 'sw-resize';
			return 5;			
		} else if(x>=(shape.x1+(shape.x2/2)-handle) && x<=(shape.x1+(shape.x2/2)+handle) && y>=shape.y1+shape.y2-handle && y<=shape.y1+shape.y2+handle) {
			target.style.cursor = 's-resize';
			return 6;			
		} else if(x>=shape.x1+shape.x2-handle && x>=shape.x1+shape.x2+handle && y>=shape.y1+shape.y2-handle && y<=shape.y1+shape.y2+handle) {			
			target.style.cursor = 'se-resize';
			return 7;			
		} else {
			target.style.cursor = 'default';
			return -1;
		}
	};
	
	this.moveShape = function(shape,x,y) {
		shape.x1 = x;
		shape.y1 = y;
	};
	
	this.resizeShape = function(shape,x,y,direction) {
		var oldX1 = shape.x1, oldY1 = shape.y1;				
		
		switch(direction) {
			case 0:
				shape.x1 = x;
				shape.y1 = y;
				shape.x2+=oldX1-x;
				shape.y2+=oldY1-y;
				break;
			case 1:
				shape.y1 = y;
				shape.y2+=oldY1-y;
				break;
			case 2:
				shape.y1 = y;
				shape.x2=x-oldX1;
				shape.y2+=oldY1-y;
				break;
			case 3:
				shape.x1 = x;
				shape.x2+=oldX1-x;
				break;
			case 4:
				shape.x2 = x-oldX1;				
				break;
			case 5:
				shape.x1 = x;
				shape.x2+=oldX1-x;
				shape.y2=y-oldY1;
				break;
			case 6:
				shape.y2=y-oldY1;
				break;				
			case 7:
				shape.x2 = x-oldX1;
				shape.y2 = y-oldY1;
				break;
		}
		shape.area = ((this.calculateLength(((shape.x2-state.translationX)/state.scale),0))/10 * (this.calculateLength(0,((shape.y2-state.translationY)/state.scale)))/10).toFixed(3) + " cm" + String.fromCharCode(178);	
	};
	
	this.calculateLength = function(xDiff,yDiff) {
		var mult = Math.max(this.getFloatShift(xPxlSpcing),this.getFloatShift(yPxlSpcing));
		var xDist = mult * xPxlSpcing * xDiff;
		var yDist = mult * yPxlSpcing * yDiff;
		
		return (Math.sqrt((Math.pow(xDist,2)+Math.pow(yDist,2))/Math.pow(mult,2)));
	};
	
	this.getFloatShift = function(floatNum) {
		var decimalLen = 0;
		var floatElements = floatNum.toString().split('\.');
		if(floatElements.length==2) {
			decimalLen = floatElements[1].length;
		}
		mult = Math.pow(10,decimalLen);
		return mult;
	};
	
	this.meanOfRect = function(x,y,width,height) {
		var sum = 0, pixelCount = 0;
		for(var i = x;i<x+width;i++) {
			for(var j = y;j<y+height;j++) {
				++pixelCount;
				var pixel = getPixelValAt(i,j);
				if(pixel!=undefined) {
					sum+=pixel;
				}
			}
		}
		if(pixelCount==0) {
			return 0;
		}
		return (sum/pixelCount).toFixed(3);
	};
	
	this.stdDevOfRect = function(mean,x,y,width,height) {
		var sum = 0,pixelCount = 0;
		for(var i=x;i<x+width;i++) {
			for(var j=y;j<y+height;j++) {
				var value = getPixelValAt(i,j);
				if(typeof value!="undefined") {
					var deviation = value - mean;
					sum+=deviation * deviation;
				}
				pixelCount++;
			}
		}
		if(pixelCount==0) {
			return 0;
		}
		return Math.sqrt(sum/pixelCount).toFixed(3);
	};
	
	this.measure = function(shape) {
		shape.mean = this.meanOfRect(shape.x1,shape.y1,shape.x2,shape.y2);
		shape.stdDev = this.stdDevOfRect(shape.mean,shape.x1,shape.y1,shape.x2,shape.y2);
	};
	
	this.removeShape = function(shape) {
		rects.splice(rects.indexOf(shape),1);
	};
};

ovm.shape.oval = function(xPixelSpacing,yPixelSpacing) {
	var handle = 3;
	var ovals = [];	
	var xPxlSpcing = xPixelSpacing;
	var yPxlSpcing = yPixelSpacing;	
	
	this.createNewOval = function(centerX,centerY,radiusX,radiusY) {
		var area = (Math.PI * (this.calculateLength(((radiusX-state.translationX)/state.scale),0))/10 * (this.calculateLength(0,((radiusY-state.translationY)/state.scale)))/10).toFixed(3);
		var mean = this.meanOfOval(centerX,centerY,radiusX,radiusY);
		
		if(area>0) {
			area+=" cm"+String.fromCharCode(178);
			ovals.push({x1:centerX,
				y1:centerY,
				x2:radiusX,
				y2:radiusY,
				area:area,
				mean:mean,
				stdDev:this.stdDevOfOval(mean,centerX,centerY,radiusX,radiusY),
				active:false,
				getType:this.getType,
				isActiveShape:this.isActiveShape,
				calculateLength:this.calculateLength,
				getFloatShift:this.getFloatShift,
				pointInsideOval:this.pointInsideOval,
				meanOfOval:this.meanOfOval,
				stdDevOfOval:this.stdDevOfOval,
				measure:this.measure,
				moveShape:this.moveShape,
				resizeShape:this.resizeShape,
				detectHandle:this.getActiveHandle,
				removeShape:this.removeShape
			});
		}
	};
	
	this.drawData = function(ctx) {			
		
		for(var i=0;i<ovals.length;i++) {			
			var oval = ovals[i];		
			ctx.strokeStyle=ctx.fillStyle='orange';						
			ctx.lineWidth='2';	
			ctx.save();
			ctx.beginPath();
			ctx.translate(oval.x1-oval.x2,oval.y1-oval.y2);
			ctx.scale(oval.x2,oval.y2);
			ctx.arc(1,1,1,0,2*Math.PI,false);
			ctx.restore();
			ctx.stroke();						
			
			// Handles
			if(oval.active) {
				ctx.save();
				ctx.strokeStyle=ctx.fillStyle='red';
				ctx.strokeRect(oval.x1-oval.x2,oval.y1-oval.y2-handle,handle*2, handle*2);
				ctx.strokeRect(oval.x1,oval.y1-oval.y2-handle,handle*2, handle*2);
				ctx.strokeRect(oval.x1+oval.x2,oval.y1-oval.y2-handle,handle*2,handle*2);
				ctx.strokeRect(oval.x1-oval.x2-handle,oval.y1,handle*2,handle*2);
				ctx.strokeRect(oval.x1+oval.x2-handle,oval.y1,handle*2,handle*2);
				ctx.strokeRect(oval.x1-oval.x2-handle,oval.y1+oval.y2,handle*2,handle*2);
				ctx.strokeRect(oval.x1,oval.y1+oval.y2-handle,handle*2,handle*2);
				ctx.strokeRect(oval.x1+oval.x2-handle,oval.y1+oval.y2,handle*2,handle*2);
				ctx.restore();	
			}
			
			// Text
			ctx.globalAlpha = 0.7;
			ctx.fillStyle = 'maroon';
			ctx.fillRect(oval.x1,oval.y1-oval.y2-50,120,40);
			ctx.globalAlpha = 0.9;
			ctx.fillStyle = 'white';
			ctx.fillText("Area : "+oval.area,oval.x1+2,oval.y1-oval.y2-38);
			ctx.fillText("Mean : "+oval.mean,oval.x1+2,oval.y1-oval.y2-26);
			ctx.fillText("StdDev : "+oval.stdDev,oval.x1+2,oval.y1-oval.y2-14);
			
			ctx.closePath();			
		}			
	};
	
	this.drawOval = function(canvasCtx,centerX,centerY,radiusX,radiusY) {
		
		canvasCtx.lineWidth='2';
		canvasCtx.strokeStyle=canvasCtx.fillStyle='orange';		
		canvasCtx.save();	
		canvasCtx.beginPath();
		canvasCtx.translate(centerX-radiusX,centerY-radiusY);
		canvasCtx.scale(radiusX,radiusY);
		canvasCtx.arc(1,1,1,0,2*Math.PI,false);		
		canvasCtx.restore();
		canvasCtx.stroke();
		canvasCtx.closePath();
		
		//Handles
		canvasCtx.save();
		canvasCtx.strokeStyle=canvasCtx.fillStyle='red';
		canvasCtx.strokeRect(centerX-radiusX,centerY-radiusY-handle,handle*2, handle*2);
		canvasCtx.strokeRect(centerX,centerY-radiusY-handle,handle*2, handle*2);
		canvasCtx.strokeRect(centerX+radiusX,centerY-radiusY-handle,handle*2,handle*2);
		canvasCtx.strokeRect(centerX-radiusX-handle,centerY,handle*2,handle*2);
		canvasCtx.strokeRect(centerX+radiusX-handle,centerY,handle*2,handle*2);
		canvasCtx.strokeRect(centerX-radiusX-handle,centerY+radiusY,handle*2,handle*2);
		canvasCtx.strokeRect(centerX,centerY+radiusY-handle,handle*2,handle*2);
		canvasCtx.strokeRect(centerX+radiusX-handle,centerY+radiusY,handle*2,handle*2);
		
		// Text
		var area = (Math.PI * (this.calculateLength(((radiusX-state.translationX)/state.scale),0))/10 * (this.calculateLength(0,((radiusY-state.translationY)/state.scale)))/10).toFixed(3) + " cm"+String.fromCharCode(178);
		canvasCtx.globalAlpha = 0.7;
		canvasCtx.fillStyle = 'maroon';
		canvasCtx.fillRect(centerX,centerY-radiusY-50,120,40);
		canvasCtx.globalAlpha = 0.9;
		canvasCtx.fillStyle = 'white';
		canvasCtx.fillText("Area : "+area,centerX+2,centerY-radiusY-38);
		canvasCtx.fillText("Mean : "+"",centerX+2,centerY-radiusY-26);
		canvasCtx.fillText("StdDev : "+"",centerX+2,centerY-radiusY-14);
	
		canvasCtx.restore();		
	};
	
	this.getType = function() {
		return "oval";
	};
	
	this.getActiveOval = function(x,y) {
		for(var i=0;i<ovals.length;i++) {
			var oval = ovals[i];
			
			if(this.isOvalIntersect(oval.x1,oval.y1,oval.x2,oval.y2,x,y)) {
				return oval;
			}
		}
		return null;
	};
	
	this.isOvalIntersect = function(centerX,centerY,radiusX,radiusY,x,y) {
		return (((Math.pow(x-centerX,2)/Math.pow(radiusX,2))+(Math.pow(y-centerY,2)/Math.pow(radiusY,2))).toFixed(0)==1) ? true : false;
	};
	
	this.isActiveShape = function(centerX,centerY,radiusX,radiusY,x,y) {
		return ((Math.pow(x-centerX,2)/Math.pow(radiusX,2))+(Math.pow(y-centerY,2)/Math.pow(radiusY,2))).toFixed(0)<=1 ? true : false;
	};
	
	this.getActiveHandle = function(shape,x,y,target) {
		if(x>=shape.x1-shape.x2-handle && x<=shape.x1-shape.x2+handle && y>=shape.y1-shape.y2-handle && y<=shape.y1-shape.y2+handle) {
			target.style.cursor = 'nw-resize';
			return 0;
		} else if(x>=shape.x1-handle && x<=shape.x1+handle && y>=shape.y1-shape.y2-handle && y<=shape.y1-shape.y2+handle) {
			target.style.cursor = 'n-resize';
			return 1;
		} else if(x>=shape.x1+shape.x2-handle && x<=shape.x1+shape.x2+handle && y>=shape.y1-shape.y2-handle && y<=shape.y1-shape.y2+handle) {
			target.style.cursor = 'ne-resize';
			return 2;
		} else if(x>=shape.x1-shape.x2-handle && x<=shape.x1-shape.x2+handle && y>=shape.y1-handle && y<=shape.y1+handle) {
			target.style.cursor = 'w-resize';
			return 3;
		} else if(x>=shape.x1+shape.x2-handle && x<=shape.x1+shape.x2+handle && y>=shape.y1-handle && y<=shape.y1+handle) {
			target.style.cursor = 'e-resize';
			return 4;
		} else if(x>=shape.x1-shape.x2-handle && x<=shape.x1-shape.x2+handle && y>=shape.y1+shape.y2-handle && y<=shape.y1+shape.y2+handle) {
			target.style.cursor = 'sw-resize';
			return 5;
		} else if(x>=shape.x1-handle && x<=shape.x1+handle && y>=shape.y1+shape.y2-handle && y<=shape.y1+shape.y2+handle) {
			target.style.cursor = 's-resize';
			return 6;
		} else if(x>=shape.x1+shape.x2-handle && x<=shape.x1+shape.x2+handle && y>=shape.y1+shape.y2-handle && y<=shape.y1+shape.y2+handle) {
			target.style.cursor = 'se-resize';
			return 7;
		} else {
			target.style.cursor = 'default';
			return -1;
		}
	};
	
	this.moveShape = function(shape,x,y) {
		shape.x1 = x;
		shape.y1 = y;
	};
	
	this.resizeShape = function(shape,x,y,direction) {
		switch(direction) {
			case 0:			
				shape.x2 = (shape.x2+shape.x1-x)/2;
				shape.y2 = (shape.y2+shape.y1-y)/2
				break;
			case 1:
				shape.y2 = (shape.y2+shape.y1-y)/2;
				break;
			case 2:
				shape.x2 = (shape.x2+x-shape.x1)/2;
				shape.y2 = (shape.y2+shape.y1-y)/2;
				break;
			case 3:
				shape.x2 = (shape.x2+shape.x1-x)/2;
				break;
			case 4:
				shape.x2 = (shape.x2+x-shape.x1)/2;
				break;
			case 5:
				shape.x2 = (shape.x2+shape.x1-x)/2;
				shape.y2 = (shape.y2+y-shape.y1)/2;
				break;
			case 6:
				shape.y2 = (shape.y2+y-shape.y1)/2;
				break;
			case 7:
				shape.x2 = (shape.x2+x-shape.x1)/2;
				shape.y2 = (shape.y2+y-shape.y1)/2;
				break;
		}
		shape.area = (Math.PI * (this.calculateLength(((shape.x2-state.translationX)/state.scale),0))/10 * (this.calculateLength(0,((shape.y2-state.translationY)/state.scale)))/10).toFixed(3) + " cm"+String.fromCharCode(178);
	};
	
	this.calculateLength = function(xDiff,yDiff) {
		var mult = Math.max(this.getFloatShift(xPxlSpcing),this.getFloatShift(yPxlSpcing));
		var xDist = mult * xPxlSpcing * xDiff;
		var yDist = mult * yPxlSpcing * yDiff;
		
		return (Math.sqrt((Math.pow(xDist,2)+Math.pow(yDist,2))/Math.pow(mult,2)));
	};
	
	this.getFloatShift = function(floatNum) {
		var decimalLen = 0;
		var floatElements = floatNum.toString().split('\.');
		if(floatElements.length==2) {
			decimalLen = floatElements[1].length;
		}
		mult = Math.pow(10,decimalLen);
		return mult;
	};
	
	this.meanOfOval = function(x,y,width,height) {
		var sum = 0, pixelCount = 0;
		for(var i = x;i<x+width;i++) {
			for(var j = y;j<y+height;j++) {
				if(this.pointInsideOval(x,y,width,height,i,j)) {
					++pixelCount;
					var pixel = getPixelValAt(i,j);
					if(typeof pixel != "undefined") {
						sum+=pixel;
					}
				}
			}
		}
		if(pixelCount==0) {
			return 0;
		}
		return (sum/pixelCount).toFixed(3);
	};
	
	this.stdDevOfOval = function(mean,x,y,width,height) {
		var sum = 0,pixelCount = 0;
		for(var i=x;i<x+width;i++) {
			for(var j=y;j<y+height;j++) {
				if(this.pointInsideOval(x,y,width,height,i,j)) {
					var value = getPixelValAt(i,j);
					if(typeof value != "undefined") {
						var deviation = value - mean;
						sum+=deviation * deviation;
					}
					pixelCount++;
				}
			}
		}
		if(pixelCount==0) {
			return 0;
		}
		return Math.sqrt(sum/pixelCount).toFixed(3);
	};
	
	this.pointInsideOval = function(x,y,r1,r2,pointX,pointY) {
		return ((Math.pow(pointX-x,2)/Math.pow(r1,2))+(Math.pow(pointY-y,2)/Math.pow(r2,2))).toFixed(0)<=1;
	};
	
	this.measure = function(shape) {
		shape.mean = this.meanOfOval(shape.x1,shape.y1,shape.x2,shape.y2);
		shape.stdDev = this.stdDevOfOval(shape.mean,shape.x1,shape.y1,shape.x2,shape.y2);
	};
	
	this.removeShape = function(shape) {
		ovals.splice(ovals.indexOf(shape),1);
	};
};