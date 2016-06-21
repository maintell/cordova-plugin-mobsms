package com.jlg.mobsms;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * This class provides access to mobsms on the device.
 */
public class mobsms extends CordovaPlugin {

// 填写从短信SDK应用后台注册得到的APPKEY
	//此APPKEY仅供测试使用，且不定期失效，请到mob.com后台申请正式APPKEY
	private static String APPKEY = "f3fc6baa9ac4";

	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "7f3dedcb36d92deebcb373af921d635a";
	
	private static String PhoneNumber = "";
	private static String VerifyCode = "";


    private CallbackContext requestcodeCallbackContext = null;
    private CallbackContext submitcodeCallbackContext = null;

    private OnSendMessageHandler osmHandler = new OnSendMessageHandler() {
        @Override
        public boolean onSendMessage(String s, String s1) {
            Log.d("OnSendMessageHandler",s+"|"+s1);
            return true;
        }
    };


    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        SMSSDK.initSDK(cordova.getActivity().getApplicationContext(), APPKEY , APPSECRET);
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        PluginResult submitcodeResult = new PluginResult(PluginResult.Status.OK, data.toString());
                        submitcodeResult.setKeepCallback(true);
                        submitcodeCallbackContext.sendPluginResult(submitcodeResult);
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        PluginResult requestcodeResult = new PluginResult(PluginResult.Status.OK, data.toString());
                        requestcodeResult.setKeepCallback(true);
                        requestcodeCallbackContext.sendPluginResult(requestcodeResult);

                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();

                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }
    /**
     * Constructor.
     */
    public mobsms() {
		
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArray of arguments for the plugin.
     * @param callbackContext   The callback context used when calling back into JavaScript.
     * @return                  True when the action was valid, false otherwise.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("RequestVerifyCode")) {
            requestcodeCallbackContext = callbackContext;
            this.RequestVerifyCode(args.getJSONObject(0).toString());
        }
        else if(action.equals("SubmitVerifyCode"))
        {
            submitcodeCallbackContext = callbackContext;
            this.SubmitVerifyCode(args.getJSONObject(0).toString());
        }
        else if (action.equals("GetVerifyCode")) {
            JSONObject r = new JSONObject();
            r.put("Phone", mobsms.PhoneNumber);
            r.put("VerifyCode", mobsms.VerifyCode);           
            callbackContext.success(r);
        }
        else {
            return false;
        }
        return true;
    }

    public void RequestVerifyCode(String Phone) {
        SMSSDK.getVerificationCode("86", Phone.trim(), osmHandler);
    }

    public void SubmitVerifyCode(String code) {
        SMSSDK.submitVerificationCode("86",mobsms.PhoneNumber, code);
    }




//    private String[] getCurrentCountry() {
//        String mcc = getMCC();
//        String[] country = null;
//        if (!TextUtils.isEmpty(mcc)) {
//            country = SMSSDK.getCountryByMCC(mcc);
//        }
//
//        if (country == null) {
//            Log.w("SMSSDK", "no country found by MCC: " + mcc);
//            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
//        }
//        return country;
//    }
//
//
//    private String getMCC() {
//        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
//        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
//        String networkOperator = tm.getNetworkOperator();
//        if (!TextUtils.isEmpty(networkOperator)) {
//            return networkOperator;
//        }
//
//        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
//        return tm.getSimOperator();
//    }
    
   
}
