/**
 *  DicomParser.js
 *  Version 0.5
 *  Author: BabuHussain<babuhussain.a@raster.in>
 */
function DicomParser(inputBuffer,reader)
{
    this.inputBuffer=inputBuffer;
    this.reader=reader;
    this.dicomElement = null;    
    this.parseAll=parseAll;
    this.index = 0;
    this.pixelBuffer;
    this.bitsStored;
    this.pixelRepresentation;
    this.minPix;
    this.maxPix;
}

var elementIndex=0;

function getPixelBuffer()
{
    return this.pixelBuffer;
}

function parseAll()
{
	//read photometric interpretation value
    this.readTag(40,0,4,0,"photometricInterpretation");
	//read bits allocated value
    this.bitsStored = this.readTagAsNumber(40,0,1,1,"BitsStored");
    //read pixel representation    
    this.pixelRepresentation = this.readTagAsNumber(40,0,3,1,"PxelRepresentation");
    //read wc value    
    this.readTag(40,0,80,16,"windowCenter");
    //read ww value
    this.readTag(40,0,81,16,"windowWidth");
    //read rescale slope value
    this.readTag(40,0,82,16,"rescaleIntercept");
    //read rescale intercept value
    this.readTag(40,0,83,16,"rescaleSlope");
    //move to pixel data
    this.moveToPixelDataTag();
    //read pixel data
    this.readImage();    
}

DicomParser.prototype.setDicomElement=function(name,vr,vl,group,element,value,offset)
{
    if(this.dicomElement==null) {
        this.dicomElement=new Array();
        elementIndex = 0;
    }
	
    this.dicomElement[elementIndex++]=new DicomElement(name,vr,vl,group,element,value,offset);

}

DicomParser.prototype.readTag=function(firstContent,secondContent,thirdContent,fourthContent,tagName)
{
    var i=this.index;

    for(; i<this.inputBuffer.length; i++)
    {
        if(this.reader.readNumber(1,i)==firstContent && this.reader.readNumber(1,i+1)==secondContent&&this.reader.readNumber(1,i+2)==thirdContent&&this.reader.readNumber(1,i+3)==fourthContent)
        {
            i=i+4;
            var vr= this.reader.readString(2,i);
            var vl=this.reader.readNumber(2,i+2);
            var val=this.reader.readString(vl,i+4);
            var tagValue=val.split("\\");
            this.setDicomElement(tagName,vr,vl,firstContent+secondContent,thirdContent+fourthContent,tagValue,i-4);
            i=i+4+vl;
			this.index = i;
            break;
        }    
    }  
}

DicomParser.prototype.readTagAsNumber=function(firstContent,secondContent,thirdContent,fourthContent,tagName)
{
    var i=this.index;
    for(; i<this.inputBuffer.length; i++)
    {
        if(this.reader.readNumber(1,i)==firstContent && this.reader.readNumber(1,i+1)==secondContent&&this.reader.readNumber(1,i+2)==thirdContent&&this.reader.readNumber(1,i+3)==fourthContent)
        {
            i=i+4;
            var vr= this.reader.readString(2,i);
            var vl=this.reader.readNumber(2,i+2);
            var val=this.reader.readNumber(vl,i+4);
            this.setDicomElement(tagName,vr,vl,firstContent+secondContent,thirdContent+fourthContent,val,i-4);
            i=i+4+vl;
            this.index = i;
            return val;
        }    
    }
}

DicomParser.prototype.moveToPixelDataTag=function()
{
    var i=this.index;    
    for(; i<this.inputBuffer.length; i++)
    {
        if(this.reader.readNumber(1,i)==224 &&this.reader.readNumber(1,i+1)==127&&this.reader.readNumber(1,i+2)==16&&this.reader.readNumber(1,i+3)==0)
	{			
            i=i+4;
            this.index = i;
	}    
    }
}

DicomParser.prototype.readImage=function()
{
	 this.minPix = 65535;
     this.maxPix = -32768;   

	if(this.pixelRepresentation === 0 && this.bitsStored ===8) {
		this.pixelBuffer = new Uint8Array(this.reader.getBytes(this.index).buffer);
	} else if(this.pixelRepresentation === 0 && this.bitsStored>8) {
		this.pixelBuffer = new Uint16Array(this.reader.getBytes(this.index).buffer);
	} else if(this.pixelRepresentation === 1 && this.bitsStored>8) {
		this.pixelBuffer = new Int16Array(this.reader.getBytes(this.index).buffer);    
	} else {
		this.pixelBuffer = new Array();
	}	
	
    for(var j=0;j<this.pixelBuffer.length;j++) {
    	var pix = this.pixelBuffer[j];
		this.minPix = Math.min(this.minPix,pix);
		this.maxPix = Math.max(this.maxPix,pix);
	}  
}