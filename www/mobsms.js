var cordova = require('cordova'),
    exec = require('cordova/exec');

var STATUS_CRITICAL = 5;
var STATUS_LOW = 20;

var Mobsms = function() {
    this._level = null;
    this._isPlugged = null;
    // Create new event handlers on the window (returns a channel instance)
    this.channels = {
      mobsms_requeststatus:cordova.addWindowEventHandler("mobsms_requeststatus"),
      mobsms_codestatus:cordova.addWindowEventHandler("mobsms_codestatus")
    };
    for (var key in this.channels) {
        this.channels[key].onHasSubscribersChange = Mobsms.onHasSubscribersChange;
    }
};

function handlers() {
    return mobsms.channels.mobsms_requeststatus.numHandlers +
        mobsms.channels.mobsms_codestatus.numHandlers ;
}

/**
 * Event handlers for when callbacks get registered for the Mobsms.
 * Keep track of how many handlers we have so we can start and stop the native Mobsms listener
 * appropriately (and hopefully save on Mobsms life!).
 */
//Mobsms.onHasSubscribersChange = function() {
//  // If we just registered the first handler, make sure native listener is started.
//  if (this.numHandlers === 1 && handlers() === 1) {
//      exec(mobsms._status, mobsms._error, "Mobsms", "start", []);
//  } else if (handlers() === 0) {
//      exec(null, null, "Mobsms", "stop", []);
//  }
//};

Mobsms.RequestVerifyCode = function(phone)
  {
       exec(mobsms._requestcodeCallback, mobsms._error, "mobsms", "RequestVerifyCode", [phone]);
  }

  Mobsms.prototype._requestcodeCallback = function (info) {

      if (info) {
          cordova.fireWindowEvent("mobsms_requeststatus", info);
      }
  };

Mobsms.SubmitVerifyCode = function(code)
{
     exec(mobsms._submitcodeCallback, mobsms._error, "mobsms", "SubmitVerifyCode", [code]);
}

Mobsms.prototype._submitcodeCallback = function (info) {
    if (info) {
        cordova.fireWindowEvent("mobsms_codestatus", info);
    }
};


/**
 * Error callback for Mobsms start
 */
Mobsms.prototype._error = function(e) {
    console.log("Error initializing Mobsms: " + e);
};

var mobsms = new Mobsms(); // jshint ignore:line

module.exports = mobsms;
