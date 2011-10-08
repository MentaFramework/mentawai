/**
 * Version 1.1
 * select input
 * 
 * @author Robert Willian
 * 
 * @param {String} id of span element that will be monitored
 * @param {String} name of param that will be sent to request
 * @param {String} url that will be requested
 * @param {Object} options with params
 * 
 * EX: implace("idSpan", "parName", "AajaxTeste.jsp") will be sent by ajax this:
 * AajaxTeste.jsp?id=idSpan0&name=parName&value=inputedString
 */
function implace(id, name, url, options){
	var TYPES = {
		DATE: "date",
		MONEY: "money",
		CUSTOM: "custom"
	}
	
	
	var input;
	
	var settings = {
		eventDefault:	"click",
		type:			"text",
		placeHolder:	"",
		callback:		null,
		cssclass:		null,
		style:			null,
		size:			null,
		maxlength:		null,
		indicator:		null,
		width:			"auto",
		typeMask:		null,
		maskerOptions:	null,
		selectOptions:	null,
		multiple:		null,
		onValueClean:	null
	}
	
	if(options != undefined) {
		for(var op in options){
			eval("settings." + op + " = " + "options." + op);
		}	
	}
	
	
	var editing = false;
	var usingPlaceHolder = false;
	
	var span = document.getElementById(id);
	if(span.innerHTML == "") {
		span.innerHTML = settings.placeHolder;
		usingPlaceHolder = true;
	}
		
	var value = span.innerHTML;
	
	addEvent(settings.eventDefault , span, handler );
	
	
	function handler(evt) {
		
		if(window.event) {		// IE
			if(!editing) {
				if(settings.type == "text") {
					createInput();	
				} else {
					createSelect();
				}
				editing = true;	
			}
			
		} else {				// FF
			if(!editing) {
				if(settings.type == "text") {
					createInput();	
				} else {
					createSelect();
				}
				editing = true;
			}
		}
	}
	
	
	function handleKeyDown(evt){
		
		if(window.event) {		//	IE
			
			if(window.event.type == "click") {
				reset();
			}	
				
			if(window.event.keyCode == 27) {		// 27 = ESC
				window.event.cancelBubble = true;
				reset();
			}
			
			if(window.event.keyCode == 13) {		// 13 = ENTER
				if(settings.callback != null) {
					var re = settings.callback( {"name": name, "value": input.value} );
					if(re != undefined) {
						value = re;
						reset();
					}	
				} else {
					
					if(settings.indicator != null)
						span.innerHTML = settings.indicator; 
					
					var r = new mtw.request();
					r.setUrl(url);
					r.addParameter("name", input.name);
					
					if(settings.type == "select") {
						
						for(var i=0; i<select.length; i++) {
							if(select.options[i].selected){
								r.addParameter("value", select.options[i].value);
							}
						}
						
					} else {
						
						r.addParameter("value", input.value);
						
					}

					r.onSuccess(function(t){
							value = new mtw.response(t).getString();
							reset();
						}
					);
					r.send();
				}
			}
		
		} else {				//	FF
			
			if(evt.keyCode == 27) {					// 27 = ESC
				evt.preventDefault();
				reset();
			}	
			
			if(evt.keyCode == 13) {					// 13 = ENTER
			
				if(settings.callback != null) {
					var re = settings.callback( {"name": name, "value": input.value} );
					if(re != undefined) {
						value = re;
						reset();
					}	
				} else {
					
					if(settings.indicator != null)
						span.innerHTML = settings.indicator; 
					
					var r = new mtw.request();
					r.setUrl(url);
					r.addParameter("name", input.name);
					
					if(settings.type == "select") {
						
						for(var i=0; i<select.length; i++) {
							if(select.options[i].selected){
								r.addParameter("value", select.options[i].value);
							}
						}
						
					} else {
						
						r.addParameter("value", input.value);
						
					}
					
					r.onSuccess(function(t){
							value = new mtw.response(t).getString();
							reset();
						}
					);
					r.send();
				}
			}
		}
		
	}
	
	function createInput(){
		input = document.createElement('input');
		input.id = id + "INPUT";
		input.value = usingPlaceHolder ? "" : value == settings.onValueClean ? "" : value;
		input.name = name;
		
		if(settings.cssclass != null) input.setAttribute("class", settings.cssclass);
		if(settings.style != null) input.setAttribute("style", settings.style);
		if(settings.size != null) input.setAttribute("size", settings.size);
		if(settings.maxlength != null) input.setAttribute("maxlength", settings.maxlength);
		if(settings.width != null) input.setAttribute("width", settings.width);
		
		while(span.firstChild)
			span.removeChild(span.firstChild);
			
		span.appendChild(input);
		addEvent('keydown', input, handleKeyDown);
		setTimeout( function(){input.focus();} , 10);
		
		addEvent('blur', input, function() { setTimeout( reset, 500); });
		
		hasMask(input);
		
	}
	
	function createSelect(){
		select = document.createElement('select');
		select.id = id + "SELECT";
		select.name = name;
		
		if(settings.cssclass != null) select.setAttribute("class", settings.cssclass);
		if(settings.style != null) select.setAttribute("style", settings.style);
		if(settings.size != null) select.setAttribute("size", settings.size);
		if(settings.multiple != null) select.setAttribute("multiple", settings.multiple);
		
		
		for(var i=0; i<settings.selectOptions.obj.length; i++) {
			select.options[ select.length ] = new Option(settings.selectOptions.obj[i].value,  settings.selectOptions.obj[i].key );
		}
		
		for(var i=0; i<select.length; i++) {
			if(select.options[i].text == value)
				select.options[i].selected = true;
		}
		
		while(span.firstChild)
			span.removeChild(span.firstChild);
		
		span.appendChild(select);
		addEvent('keydown', select, handleKeyDown);
		setTimeout( function(){select.focus();} , 10);
		
		addEvent('blur', select, function() { setTimeout( reset, 500); });	
		
		input = select;
	}
	
	function hasMask(input){
		
		if(input != null && input.value != "") {
			
			if(settings.typeMask != null){
			
				//********	DATE
				if(settings.typeMask == TYPES.DATE){
	
					if(settings.maskerOptions != null) {
						var opt = settings.maskerOptions;
						
						inputDateMask(input, opt.par1);
						
						addEvent('focus', input, function() {
							// return showCalendar(input.id,'dd/mm/y');
							return showCalendar(input.id, opt.par2);
						});
						
					} else {
						// Default
						inputDateMask(input,"99/99/9999");
					}
							
				}
				//*********
				
				//*********		MONEY
				if(settings.typeMask == TYPES.MONEY){
					input.setAttribute("dir", "rtl");
					
					if(settings.maskerOptions != null) {
						var opt = settings.maskerOptions;
						
						inputMoneyMask(input, opt.par1, opt.par2, opt.par3); 
						
					} else {
						// Default
						inputMoneyMask(input,2,".",",");	
					}
						
				}
				//********
				
				//********	MASKERCUSTOM
				if(settings.typeMask == TYPES.CUSTOM){
					var opt = settings.maskerOptions;
					inputMask(input, opt.par1);										
				}
				//*********			
			}	
		}
			
	}
	

	function addEvent(event, field, fn) {
		if(field.addEventListener) {
			field.addEventListener(event , fn, false);	
		} else {
			field.attachEvent("on" + event, fn);
		}	
	}
	
    function reset() {
    	span.innerHTML = value;
    	editing = false;
    }
	
}