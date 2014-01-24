window.stack = {};

stack.getCachedUrl = function(url){
	if (stack.cachedUrls[url])
		return stack.cachedUrls[url];
	return url;
}

stack.debuggingEnabled = true;
stack.characterEncoding = "UTF-8";

/**
 * Global collection of API calls that are currently executing -
 * used by stack.api(params) to prevent double-click issues.
 */
stack.apiCallsInProgress = {};

stack.api = function(params) {
    params = params || {};
    
    if(!params.url)
    	throw "You must supply an API URL.";    
    
    if(!params.method)
    	throw "You must supply a method, e.g. POST.";

    var url = params.url;
    var jsonIndentation = stack.debuggingEnabled ? 2 : 0;    
    var method = $.trim(params.method).toUpperCase();

    var headers = {
    		// Lets the server know what page we're on
    		"X-Page-Url": location.href,
    		// By default, API requests are stateful (use the session) unless we explicitly override
    		"X-API-Stateful": params.stateful !== undefined ? params.stateful : "true"
    		};
        
    if (params.apiToken !== undefined)
        headers["X-API-Token"] = params.apiToken; 

    var data;
    
    // If we're already processing a call to this endpoint, don't process another one until the current call ends.
    if (stack.apiCallsInProgress[url] === true) {
    	stack.log("Ignoring duplicate request - we're still busy processing an existing call to " + url);
        return;
    }

    stack.apiCallsInProgress[url] = true;

    // We handle GET requests differently from non-GETs (e.g. POSTs).
    //
    // Since GET requests cannot include data in the request body, we pass everything over as
    // URL parameters and add an extra "json" parameter that is a JSON object containing all
    // the parameters and their values.
    //
    // For non-GETs, we take the parameters and turn them into a JSON object and pass that along
    // in the request body.
    if (params.data === undefined) {
        stack.log("Performing API " + method + " for " + url + " with no parameters.");
        data = method == "GET" ? {} : "{}";
    } else {
        if (method == "GET") {
            data = {};

            // Copy out all provided data into an object that we'll send to the server
            $.each(params.data, function(key, value) {
                data[key] = value;
            });

            // Turn all params into a big JSON string parameter named "json"
            data.json = stack.toJson(data, jsonIndentation);

            stack.log("Performing API GET for " + url + " with parameters: " + data.json);
        } else {
            data = stack.toJson(params.data, jsonIndentation);
            stack.log("Performing API POST for " + url + " with parameters: " + data);
        }
    }

    return $.ajax({
        type: method,
        url: url,
        data: data,
        headers: headers,
        contentType: (method == "GET" ? "application/x-www-form-urlencoded" : "application/json") + "; charset=" + stack.characterEncoding,
        dataType: "json",
        success: function(response, textStatus, jqXHR) {
            if (stack.debuggingEnabled)
                stack.log("API " + method + " for " + url + " succeeded (status " + jqXHR.status + "). Server said:", stack.toJson(response, jsonIndentation));

            try {
                if (params.onSuccess !== undefined)
                    params.onSuccess(response, jqXHR.status);
            } finally {
                delete stack.apiCallsInProgress[url];
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            stack.log("API " + method + " for " + url + " failed. Server status code was " + jqXHR.status + " (" + (stack.isBlank(errorThrown) ? "no response from server" : errorThrown) + ").");

            var aborted = textStatus === "abort";

            try {
                if (jqXHR.status === 0) {
                	// Wrap this in a setTimeout to handle the case where an API call is cancelled by the user navigating away.
                	// We don't have any way of differentiating between that and a call that failed because the server is down,
                	// so the hack is to wait long enough that the page unloads before displaying the error.
                	// Without the setTimeout, if you leave a page while an API call is in progress, you'll see this error in an alert box.
          	        setTimeout(function() {          	        	
                        if (!aborted) {                        	
                            handleErrorResponse({
                                description: "Oops! The internet connection seems to be down at the moment or the server is unavailable. Please try again in a few seconds."
                            }, jqXHR.status);
                        }          	        	
          	        }, 5000);
                } else {
                    var response = {};

                    try {
                        response = stack.toJavascript(jqXHR.responseText);

                        // Redundant "is dev" check here to avoid overhead of JSON marshaling in non-dev environments
                        if (stack.debuggingEnabled) {
                            var responseWithElidedStackTrace = {};
                            $.each(response, function(key, value) {
                                responseWithElidedStackTrace[key] = key === "stackTrace" ? "(elided)" : value;
                            });

                            stack.log("Response body is", stack.toJson(responseWithElidedStackTrace, jsonIndentation));
                        }

                        // Special behavior...if we get a 401 or 403 from an API call, redirect to wherever the API response tells us to (e.g. the sign-in page).
                        // TODO: revisit this
                        if (response.destination && (jqXHR.status === 401 || jqXHR.status === 403)) {
                            location.href = response.destination;
                            return;
                        }
                    } catch(e) {}

                    handleErrorResponse(response, jqXHR.status);
                }
            } finally {
                delete stack.apiCallsInProgress[url];
            }
        }
    });

    function handleErrorResponse(response, status) {
        var defaultErrorMessage = "Sorry, an unexpected error has occurred. The technical team has been notified of the problem.";        
        var description = (response === undefined || stack.isBlank(response.description)) ? defaultErrorMessage : response.description;
        var errorCode = (response === undefined || response.errorCode === undefined) ? 1000 /* TODO: plug in the real default */ : response.errorCode;
        status = status || 0;
        
        // If there's no response provided, make a fake one so client code doesn't choke.
        if (response === undefined) {
            if (params.onFailure === undefined) {
                alert(description);
            } else {
                params.onFailure({
                    description: description,
                    errorCode: errorCode 
                }, status);
            }
        } else {
        	// There is a response - pass it along to the client.
            if (stack.debuggingEnabled && response !== undefined && response.stackTrace !== undefined)
                stack.log("Stack trace:\n", response.stackTrace);            
            
            if (params.onFailure === undefined) {
                alert(description);
            } else {
            	// Make sure it has correct defaults in case the server didn't return one of these fields
            	response.description = description;
            	response.errorCode = errorCode;
                params.onFailure(response, status);
            }
        }
    }
};

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