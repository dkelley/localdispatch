window.stack = {};

stack.getCachedUrl = function(url){
	if (stack.cachedUrls[url])
		return stack.cachedUrls[url];
	return url;
}

stack.debuggingEnabled = true;
stack.characterEncoding = "UTF-8";

stack.log = function() {
    if(!window.console || !window.console.log || !window.console.log.apply)
        return;

    console.log.apply(console, arguments);
};

stack.toJson = function(object, indentation) {
    return JSON.stringify(object, null, indentation);
};

stack.toJavascript = function(json) {
    return JSON.parse(json);
};

stack.urlEncode = function(value) {
    return encodeURIComponent(value).replace("'", "%27");
};

stack.stringEndsWith = function(string, endsWith) {
    return new RegExp(stack.escapeRegex(endsWith) + "$").test(string);
};

stack.escapeHtml = function(string) {
    var element = $("<div>");
    element.text(string);
    return element.html();
};

// Escapes all special regex characters in string and returns the result.
// Useful for using a user-entered string as a regex.
// Thanks to Theodor Zoulias        
stack.escapeRegex = function(string) {
    return string.replace(/([.*+?^${}()|[\]\/\\])/g, "\\$1");
};

// Takes a base URL without a parameter string and a dictionary of parameters (optional).
stack.constructUrl = function(urlBase, parameters) {
    var url = urlBase;
    parameters = parameters || {};

    if (stack.countKeysInObject(parameters) == 0)
        return url;

    url += "?";

    $.each(parameters, function(key, value) {
        url += (stack.stringEndsWith(url, "?") ? "" : "&") + stack.urlEncode(key) + "=" + stack.urlEncode(value);
    });

    return url;
};

stack.countKeysInObject = function (object) {
    var count = 0;
    for (var key in object)
        // if (object.hasOwnProperty(key)) // doesn't work in IE!
        ++count;

    return count;
};

stack.scrollTo = function(scrollTop, duration) {
    scrollTop = scrollTop || 0;

    if (duration === undefined)
        duration = "slow";

    $("html:not(:animated), body:not(:animated)").animate({scrollTop: scrollTop}, duration);
};

stack.getUrlParameter = function(name, url) {
    return stack.getUrlParameters(url)[name];
};

stack.getUrlParameters = function(url) {
    url = url || window.location.href;
    var map = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m, key, value) {
        map[key] = value;
    });

    return map;
};

stack.focus = function(selector) {
    if (!selector.focus)
        return;

    setTimeout(function() {
        try {
        	selector.focus();
        } catch(e) {}
    }, 10);
};

stack.isBlank = function(string) {
	return string === undefined || $.trim(string).length === 0;
};

stack.isObject = function(unknown) {
	return Object.prototype.toString.call(unknown) === "[object Object]"; 
};

stack.isArray = function(unknown) {
	return Object.prototype.toString.call(unknown) === "[object Array]"; 
};

stack.isFunction = function(unknown) {
	return Object.prototype.toString.call(unknown) === "[object Function]"; 
};

// FILE UPLOAD SUPPORT

// Global upload identifier (so multiple file uploads on the same page don't step on each other)
stack.uploadId = 0;

// Force IE to fire the change event on file elements.
// For some reason it will not do this unless the file element is blurred.
if (navigator.userAgent.match(/msie/i)) {
	setInterval(function() {
		$("input[type=file]").blur();
	}, 500);
}

// Turns on automatic "ajax" file-upload support.
//
// params: a map
//	--> url: File upload API endpoint URL
//	--> fileInputSelector: jQuery selector string which picks out file inputs
//	--> additionalParams: Map of string keys -> values which are added as hidden form fields during the upload.
//	--> onStart: function which takes the newly-cloned file input
//	--> onSuccess: function which takes the server response (e.g. the ID of the uploaded file) and the newly-cloned file input
//	--> onFailure: function which takes an error string and the newly-cloned file input
stack.enableAutomaticFileUploads = function(params) {
	params = params || {};
	params.additionalParams = params.additionalParams || {};
	params.onStart = params.onStart || function() {};
	params.onSuccess = params.onSuccess || function() {};
	params.onFailure = params.onFailure || function() {};
	
    if(!params.url)
    	throw "You must supply a file upload API URL.";    
    
    if(!params.fileInputSelector)
    	throw "You must supply a file input jQuery selector.";        
	        
    // TODO: fix
    // params.additionalParams["X-API-Stateful"] = "true";
    params.url += "?X-API-Stateful=true";
    
	$(params.fileInputSelector).change(function() {
		var fileInput = $(this);

		if(fileInput.val() == "" || fileInput.data("busy"))
			return;
			
		fileInput.data("busy", true);             
  
		var currentUploadId = ++stack.uploadId;

		// Because IE cannot clone file input elements correctly,
		// we move the original fileInput into the hidden iframe
		// and put a clone back in the original fileInput location...
		// this is our only option.
		var clonedFileInput = fileInput.clone(true);          
		clonedFileInput.removeData("busy").val("").insertAfter(fileInput);                          
		    
		$("body").append(
			'<div id="uploader' + currentUploadId + '" style="display: none;">' +
				'<form action="' + params.url + '" method="POST" ' +
					'enctype="multipart/form-data" target="upload-result-iframe' + currentUploadId + '">' +               
				'</form>' +
				'<iframe name="upload-result-iframe' + currentUploadId + '" src="javascript://"></iframe>' +
			'</div>'
		);  

		var uploadContainer = $("#uploader" + currentUploadId);
  
		for(paramName in params.additionalParams)
			uploadContainer.find("form").append("<input type='hidden' name='" + paramName + "' value='" + 
				params.additionalParams[paramName] + "'/>");
  
		$("iframe", uploadContainer).on("load", function() {
			var frame = window.frames["upload-result-iframe" + currentUploadId];	
			
			var responseJson = {};
			
			try {
				// The browser tacks on bizarre stuff to the response body when accessed this way, e.g. a response of "{}" might look like this:
				// <pre style="word-wrap: break-word; white-space: pre-wrap;">{}</pre>
				// So we need to unbox it.						
				var responseBody = frame.document.body.innerHTML;			
				var responseJsonString = responseBody.substring(responseBody.indexOf("{"), responseBody.lastIndexOf("}") + 1);					
				responseJson = stack.toJavascript(responseJsonString);
			} catch(e) {
				stack.log("Warning: unable to parse JSON from file upload response body. Reason: " + e.message + "\nResponse Body:\n" + responseBody);
				
				// Synthesize a fake error object in this case
				responseJson.errorCode = 1000;
				responseJson.description = "Sorry, we were unable to process your file.";
			}
			
			// Unfortunately we can't get at HTTP status codes via iframe.
			// But we can examine the JSON response and see if it has an "errorCode" field.
			// If it does, then we know something went wrong.			
			if(stack.isObject(responseJson) && responseJson.errorCode)
				params.onFailure(responseJson, clonedFileInput);
			else
				params.onSuccess(responseJson, clonedFileInput);

			// Clean up the temporary form and iframe -
			// do this in a setTimeout so the browser doesn't
			// get confused and think it's still processing
			// (we don't want to have the wait cursor displayed forever)    
			setTimeout(function() {
				uploadContainer.remove();
			}, 30);                                         
		});

		fileInput.appendTo($("form", uploadContainer));                                       
		$("form", uploadContainer)[0].submit();   
    
		params.onStart(clonedFileInput);
	});		 
};

// Normalize websocket
if (!window.WebSocket && window.MozWebSocket)
  window.WebSocket = window.MozWebSocket;