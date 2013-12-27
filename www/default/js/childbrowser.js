
/* JavaScript content from js/childbrowser.js in folder android */
/*
 * cordova is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2005-2010, Nitobi Software Inc.
 * Copyright (c) 2011, IBM Corporation
 */

/**
 * Constructor
 */
function ChildBrowser() {
};

ChildBrowser.CLOSE_EVENT = 0;
ChildBrowser.LOCATION_CHANGED_EVENT = 1;
ChildBrowser.ORDERLIST_EVENT = 2;
ChildBrowser.ORDERCOMPLETE_EVENT = 3;
ChildBrowser.DIALOGOK_EVENT = 4;
/**
 * Display a new browser with the specified URL.
 * This method loads up a new web view in a dialog.
 *
 * @param url           The url to load
 * @param options       An object that specifies additional options
 */
ChildBrowser.prototype.showWebPage = function(url, options) {
    if (options === null || options === "undefined") {
        var options = new Object();
        options.showLocationBar = true;
        options.locationBarAlign = "top";
    }
    cordova.exec(this._onEvent, this._onError, "ChildBrowser", "showWebPage", [url, options]);
};

ChildBrowser.prototype.showAdsView = function(options) {
    cordova.exec(this._onEvent, this._onError, "ChildBrowser", "showAdsView", ["", options]);
};

ChildBrowser.prototype.openDialog = function(options) {
    cordova.exec(this._onEvent, this._onError, "ChildBrowser", "openDialog", ["", options]);
};

ChildBrowser.prototype.updateDialog = function(timeStr) {
    cordova.exec(this._onEvent, this._onError, "ChildBrowser", "updateDialog", [timeStr, null]);
};

ChildBrowser.prototype.closeDialog = function() {
    cordova.exec(this._onEvent, this._onError, "ChildBrowser", "closeDialog", ["", null]);
};
/**
 * Close the browser opened by showWebPage.
 */
ChildBrowser.prototype.close = function() {
    cordova.exec(null, null, "ChildBrowser", "close", []);
};

/**
 * Display a new browser with the specified URL.
 * This method starts a new web browser activity.
 *
 * @param url           The url to load
 * @param usecordova   Load url in cordova webview [optional]
 */
ChildBrowser.prototype.openExternal = function(url, usecordova) {
    if (usecordova === true) {
        navigator.app.loadUrl(url);
    }
    else {
        cordova.exec(null, null, "ChildBrowser", "openExternal", [url, usecordova]);
    }
};

/**
 * Method called when the child browser has an event.
 */
ChildBrowser.prototype._onEvent = function(data) {
	
    if (data.type == ChildBrowser.ORDERLIST_EVENT && typeof window.plugins.childBrowser.onOrderList === "function") {
        window.plugins.childBrowser.onOrderList();
    }
    if (data.type == ChildBrowser.ORDERCOMPLETE_EVENT && typeof window.plugins.childBrowser.onOrderComplete === "function") {
        window.plugins.childBrowser.onOrderComplete();
    }
    if (data.type == ChildBrowser.DIALOGOK_EVENT && typeof window.plugins.childBrowser.onCounterDialogOk === "function") {
        window.plugins.childBrowser.onCounterDialogOk();
    }
};

/**
 * Method called when the child browser has an error.
 */
ChildBrowser.prototype._onError = function(data) {
    if (typeof window.plugins.childBrowser.onError === "function") {
        window.plugins.childBrowser.onError(data);
    }
};

/**
 * Maintain API consistency with iOS
 */
ChildBrowser.prototype.install = function(){
};

/**
 * Load ChildBrowser
 */

if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.childBrowser) {
    window.plugins.childBrowser = new ChildBrowser();
}
