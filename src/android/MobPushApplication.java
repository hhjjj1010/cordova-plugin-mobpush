package cn.hhjjj.mobpush;

import android.content.Context;
import android.content.Intent;


import com.mob.MobApplication;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;


public class MobPushApplication extends MobApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        MobPush.addPushReceiver(new MobPushReceiver() {
            @Override
            public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
                //接收自定义消息(透传)
                System.out.println("onCustomMessageReceive:" + message.toString());
                MobPushPlugin.handleOnLocalMessageReceived(message);
            }

            @Override
            public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
                // 接收通知消息
                System.out.println("MobPush onNotifyMessageReceive:" + message.toString());
                MobPushPlugin.handleOnNotificationMessageReceived(message);
            }

            @Override
            public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
                // 接收通知消息被点击事件
                System.out.println("MobPush onNotifyMessageOpenedReceive:" + message.toString());

                MobPushPlugin.openMessage = message;
                MobPushPlugin.handleOnNotificationMessageOpened(message);

                Intent launch = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                if (launch != null) {
                    launch.addCategory(Intent.CATEGORY_LAUNCHER);
                    launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(launch);
                }
            }

            @Override
            public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
                // 接收tags的增改删查操作
                System.out.println("onTagsCallback:" + operation + "  " + errorCode);
                String event = "";

                switch (operation) {
                    case 0: {
                        event = "get";
                        break;
                    }
                    case 1: {
                        event = "add";
                        break;
                    }
                    case 2: {
                        event = "del";
                        break;
                    }

                    default:
                        break;
                }

                CallbackContext callbackContext = MobPushPlugin.tagsEventCallbackMap.get(event).get(0);

                if (callbackContext == null) {
                    return;
                }


                if (errorCode == 0) {

                    if (tags == null) {
                        callbackContext.success();
                    } else {
                        try {
                            JSONArray jsonArray = new JSONArray(tags);
                            callbackContext.success(jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callbackContext.error("tags " + event + "result error");
                        }

                    }
                } else {
                    callbackContext.error("tags " + event + " operation error");
                }


                MobPushPlugin.tagsEventCallbackMap.get(event).remove(0);
            }

            @Override
            public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
                // 接收alias的增改删查操作
                System.out.println("onAliasCallback:" + alias + "  " + operation + "  " + errorCode);
                String event = "";

                switch (operation) {
                    case 0: {
                        event = "get";
                        break;
                    }
                    case 1: {
                        event = "edit";
                        break;
                    }
                    case 2: {
                        event = "del";
                        break;
                    }

                    default:
                        break;
                }


                CallbackContext callbackContext = MobPushPlugin.aliasEventCallbackMap.get(event).get(0);

                if (callbackContext == null) {
                    return;
                }

                if (errorCode == 0) {
                    callbackContext.success(alias);

                } else {
                    callbackContext.error("tags " + event + " operation error");
                }

                MobPushPlugin.aliasEventCallbackMap.get(event).remove(0);
            }
        });

    }
}