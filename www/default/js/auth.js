
/* JavaScript content from js/auth.js in folder common */
/*
 *  Licensed Materials - Property of IBM
 *  5725-G92 (C) Copyright IBM Corp. 2011, 2012. All Rights Reserved.
 *  US Government Users Restricted Rights - Use, duplication or
 *  disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/*
 *	This piece of code was added as a part of upgrading your application 
 *	to a new authentication API introduced in Worklight 5.0.0.3
 *		
 *	Authenticator object that was used previously is deprecated.
 *	New challenge handler APIs are serving as a wrapper for original Authenticator object.
 *	To learn more about challenge handler APIs please refer to Worklight documentation.
 *
 */

// ----------------- Challenge handler start -----------------
var challengeHandler1 = WL.Client.createChallengeHandler("MorAuthRealm");

challengeHandler1.isInitialized = false;
challengeHandler1.isCustomResponse = function(response) {
    if (typeof Authenticator == "undefined" && 
   	     response.responseJSON.realm!=challengeHandler1.realm ) {
        return false;
    }    
    
    if (!this.isInitialized) {
        this.isInitialized = true;
        Authenticator.init();
    }

    var isLoginFormResponse = Authenticator.isLoginFormResponse(response);
    if (isLoginFormResponse) {
        Authenticator.onBeforeLogin(response, null, challengeHandler1.onSubmitButtonClicked);
        Authenticator.onShowLogin();
    } else {
        Authenticator.onHideLogin();
    }
    return isLoginFormResponse;
};

challengeHandler1.onSubmitButtonClicked = function(reqURL, authParams) {
    var options = {
        headers : {},
        parameters : (authParams && authParams.parameters) ? authParams.parameters : {}
    };

    challengeHandler1.submitLoginForm(reqURL, options, challengeHandler1.submitLoginFormCallback);
};

challengeHandler1.submitLoginFormCallback = function(response) {
    var isLoginFormResponse = challengeHandler1.isCustomResponse(response);
    if (!isLoginFormResponse) {
        challengeHandler1.submitSuccess();
    }
};
// ----------------- Challenge handler end -----------------
var Authenticator = function() {

	return {
		
		init : function() {
			
		},

		isLoginFormResponse : function(response) {
		},

		onBeforeLogin : function(response, username, onSubmit, onCancel) {
		},

		onShowLogin : function() {
		},

		onHideLogin : function() {
		}
	};
}();





var dummyRealmChallengeHandler = WL.Client.createChallengeHandler("DummyAuthRealm");

dummyRealmChallengeHandler.isCustomResponse = function(response) {
if (!response || !response.responseJSON	|| response.responseText === null) {
	return false;
}
if ( response.responseJSON.authRequired && 
     response.responseJSON.realm==dummyRealmChallengeHandler.realm){
	return true;
} else {
	return false;
}
	
};

dummyRealmChallengeHandler.handleChallengecallback= function(response){

	dummyRealmChallengeHandler.submitSuccess();
};

dummyRealmChallengeHandler.handleChallenge = function(response){
	

	var invocationData = {
			adapter : "AuthenticationAdapter",
			procedure : "submitDummyAuthentication",
			parameters : [ "worklight", "worklight" ]
		};

	dummyRealmChallengeHandler.submitAdapterAuthentication(invocationData,  {
		onSuccess: dummyRealmChallengeHandler.handleChallengecallback, 
		onFailure: dummyRealmChallengeHandler.handleChallengecallback
	});
};