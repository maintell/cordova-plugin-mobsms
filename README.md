# cordova-plugin-mobsmsSDK
just a mob.com sms sdk for cordova

example

var mob = mobsms.init({
        MobConfig: {
              APPKEY: "1416328677438",
              APPSECRET: "363cfcee4b1403aa04522a5a6998c82e"
        }});
		
mob.RequestVerifyCode(
                function(info) {
                    if (info.status == 0) {
                        alert("0000000request ok" + info.message);
                    } else {
                        alert("1111111request failed" + info.message);
                    }
                },
                function(code) {
                    alert("22222222fail" + code);
                },
                "17752863457");
				
				
  mob.SubmitVerifyCode(
                function(info) {
                    if (info.status == 0) {
                        alert("0000000request ok" + info.message);
                    } else {
                        alert("3333333request faled" + info.message);
                    }
                },
                function(code) {
                    alert("4444444fail" + code);
                },
                vcode);				