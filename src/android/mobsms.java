package com.jlg.mobsms;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;

import cn.smssdk.EventHandler;
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
        if (action.equals("DoVerify")) {
            this.DoVerify(args.getJSONObject(0).toString());
        }       
        else if (action.equals("GetVerifyCode")) {
            JSONObject r = new JSONObject();
            r.put("Phone", mobsms.PhoneNumber);
            r.put("VerifyCode", mobsms.VerifyCode);           
            callbackContext.success(r);        }
        else {
            return false;
        }
        return true;
    }

    
    public void DoVerify(String Phone) {
       
    }

	
	private void initSDK() {
		SMSSDK.initSDK(this,APPKEY,APPSECRET);
        EventHandler eh=new EventHandler(){ 
            @Override
            public void afterEvent(int event, int result, Object data) {
 
               if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                //提交验证码成功
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                //获取验证码成功
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
    
   
}
