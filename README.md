# cordova-plugin-mobsmsSDK
just a mob.com sms sdk for cordova

example

var mob = mobsms.init({
        MobConfig: {
              APPKEY: "1416328677438",
              APPSECRET: "363cfcee4b1403aa04522a5a6998c82e"
        }});
		
   mob.RequestVerifyCode(
                                function(){
                                    alert("request ok");
                                },
                                function(code){
                                    alert("fail" + code);
                                },
                                "13900000000");