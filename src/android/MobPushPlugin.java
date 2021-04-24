package cn.hhjjj.mobpush;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushLocalNotification;
import com.mob.pushsdk.MobPushNotifyMessage;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * This class echoes a string called from JavaScript.
 */
public class MobPushPlugin extends CordovaPlugin {

    static Map<String, ArrayList<CallbackContext>> tagsEventCallbackMap = new HashMap<>();
    static Map<String, ArrayList<CallbackContext>> aliasEventCallbackMap = new HashMap<>();

    static MobPushNotifyMessage openMessage;

    private static MobPushPlugin instance;
    
    private static final String TAG = "MobPushLogger";
    private static String msg;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        
        Bundle extras = cordova.getActivity().getIntent().getExtras();
        
        if (extras != null) {
            for (String key: extras.keySet()) {
               msg += key + "=" + extras.get(key).toString();
            }
        }
        
        if (instance == null) {
            instance = this;
        }

        if (openMessage != null) {
            handleOnNotificationMessageOpened(openMessage);
        }

    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        if (openMessage != null) {
            handleOnNotificationMessageOpened(openMessage);
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("initPush")) {

            this.initPush(callbackContext);
            return true;
        }

        if (action.equals("addTags")) {
            addTags(args, callbackContext);
            return true;
        }

        if (action.equals("getTags")) {
            getTags(callbackContext);
            return true;
        }

        if (action.equals("deleteTags")) {
            deleteTags(args, callbackContext);
            return true;
        }

        if (action.equals("cleanAllTags")) {
            cleanTags(callbackContext);
            return true;
        }


        if (action.equals("getRegistrationId")) {
            getRegistrationId(callbackContext);
            return true;
        }

        if (action.equals("isPushStopped")) {
            isPushStopped(callbackContext);
            return true;
        }

        if (action.equals("stopPush")) {
            stopPush(callbackContext);
            return true;
        }

        if (action.equals("restartPush")) {
            restartPush(callbackContext);
            return true;
        }

        if (action.equals("setAlias")) {
            setAlias(args, callbackContext);
            return true;
        }

        if (action.equals("getAlias")) {
            getAlias(callbackContext);
            return true;
        }

        if (action.equals("deleteAlias")) {
            deleteAlias(callbackContext);
            return true;
        }

        if (action.equals("addLocalNotification")) {
            addLocalNotification(args, callbackContext);
            return true;
        }

        if (action.equals("bindPhoneNumber")) {
            bindPhoneNumber(args, callbackContext);
            return true;
        }
        
        if (action.equals("openNotificationSettings")) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run () {
                    Context context = cordova.getActivity().getApplicationContext();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra("android.provider.extra.APP_PACKAGE", context.getApplicationContext().getPackageName());
                    cordova.getActivity().startActivity(intent);
                }
            });

            return true;
        }


        return false;
    }

    private void initPush(CallbackContext callbackContext) {
        if (instance == null) {
            instance = this;
        }
        
        if (callbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, msg);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
        }

        System.out.println("+---- initPush ----+");
    }

    private void addTags(JSONArray args, CallbackContext callbackContext) {

        try {
            JSONArray tagsArr = args.getJSONArray(0);
            String[] tags = new String[tagsArr.length()];

            for (int i = 0; i < tagsArr.length(); i++) {
                String tag = tagsArr.getString(i);
                tags[i] = tag;
            }

            MobPush.addTags(tags);

            handleTagsEventCallback("add", callbackContext);

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
        }

    }

    private void getTags(CallbackContext callbackContext) {
        MobPush.getTags();
        handleTagsEventCallback("get", callbackContext);
    }

    private void deleteTags(JSONArray args, CallbackContext callbackContext) {
        try {

            JSONArray tagsArr = args.getJSONArray(0);
            String[] tags = new String[tagsArr.length()];
            for (int i = 0; i < tagsArr.length(); i++) {
                String tag = tagsArr.getString(i);
                tags[i] = tag;
            }

            MobPush.deleteTags(tags);
            handleTagsEventCallback("del", callbackContext);

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
        }
    }

    private void cleanTags(CallbackContext callbackContext) {
        MobPush.cleanTags();
        handleTagsEventCallback("del", callbackContext);
    }

    private void getRegistrationId(CallbackContext callbackContext) {
        MobPush.getRegistrationId(new MobPushCallback<String>() {
            @Override
            public void onCallback(String rid) {
                callbackContext.success(rid);
            }
        });
    }

    private void isPushStopped(CallbackContext callbackContext) {
        boolean stop = MobPush.isPushStopped();
        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, stop));
    }

    private void stopPush(CallbackContext callbackContext) {
        MobPush.stopPush();
        callbackContext.success();
    }

    private void restartPush(CallbackContext callbackContext) {
        MobPush.restartPush();
        callbackContext.success();
    }

    private void getAlias(CallbackContext callbackContext) {
        MobPush.getAlias();
        handleAliasEventCallback("get", callbackContext);

    }

    private void setAlias(JSONArray args, CallbackContext callbackContext) {
        try {
            String alias = args.getString(0);
            MobPush.setAlias(alias);

            handleAliasEventCallback("edit", callbackContext);

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
        }
    }

    private void deleteAlias(CallbackContext callbackContext) {
        MobPush.deleteAlias();
        handleAliasEventCallback("del", callbackContext);
    }

    private void addLocalNotification(JSONArray args, CallbackContext callbackContext) {


        try {
            JSONObject object = args.getJSONObject(0);

            MobPushLocalNotification notification = new MobPushLocalNotification();


            if (!object.has("title")) {
                callbackContext.error("notification title cannot be null!");
                return;
            }

            if (!object.has("body")) {
                callbackContext.error("notification content cannot be null!");
                return;
            }

            notification.setTitle(object.getString("title"));


            notification.setContent(object.getString("body"));


            boolean voice = false;

            if (object.has("sound")) {
                voice = object.getBoolean("sound");
            }

            notification.setVoice(voice);//可设置不进行声音提醒，默认声音、振动、指示灯
            notification.setNotificationId(new Random().nextInt());

            int delay = 0;
            if (object.has("delay")) {
                delay = object.getInt("delay");
            }
            notification.setTimestamp(delay + System.currentTimeMillis());


            boolean succeed = MobPush.addLocalNotification(notification);
            if (succeed) {
                callbackContext.success();
            } else {
                callbackContext.error("add local notification error.");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
        }


    }

    private void bindPhoneNumber(JSONArray args, CallbackContext callbackContext) {
        try {
            String phoneNumber = args.getString(0);
            MobPush.bindPhoneNum(phoneNumber, new MobPushCallback<Boolean>() {
                @Override
                public void onCallback(Boolean aBoolean) {
                    if (aBoolean) {
                        callbackContext.success();
                    } else {
                        callbackContext.error("Bind phone number failed.");
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Parameters error.");
        }
    }

    private void handleTagsEventCallback(String event, CallbackContext callbackContext) {
        if (tagsEventCallbackMap.containsKey(event)) {
            tagsEventCallbackMap.get(event).add(callbackContext);
        } else {
            ArrayList<CallbackContext> list = new ArrayList<>();
            list.add(callbackContext);
            tagsEventCallbackMap.put(event, list);
        }
    }

    private void handleAliasEventCallback(String event, CallbackContext callbackContext) {
        if (aliasEventCallbackMap.containsKey(event)) {
            aliasEventCallbackMap.get(event).add(callbackContext);
        } else {
            ArrayList<CallbackContext> list = new ArrayList<>();
            list.add(callbackContext);
            aliasEventCallbackMap.put(event, list);
        }
    }

    static void handleOnNotificationMessageReceived(MobPushNotifyMessage message) {

        Map<String, Object> map = new HashMap<>();

        map.put("title", message.getTitle());
        map.put("content", message.getContent());
        map.put("extras", message.getExtrasMap());
        map.put("messageId", message.getMessageId());
        map.put("timestamp", message.getTimestamp());

        JSONObject jsonObject = new JSONObject(map);

        String format = "window.MobPush.receivedNotificationMessageInAndroidCallback(%s)";
        String jsString = String.format(format, jsonObject.toString());

        instance.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + jsString);
            }
        });
    }

    static void handleOnNotificationMessageOpened(MobPushNotifyMessage message) {

        Map<String, Object> map = new HashMap<>();

        map.put("title", message.getTitle());
        map.put("content", message.getContent());
        map.put("extras", message.getExtrasMap());
        map.put("messageId", message.getMessageId());
        map.put("timestamp", message.getTimestamp());

        JSONObject jsonObject = new JSONObject(map);

        String format = "window.MobPush.openedNotificationMessageInAndroidCallback(%s)";
        String jsString = String.format(format, jsonObject.toString());

        instance.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + jsString);
            }
        });

        openMessage = null;
    }

    static void handleOnLocalMessageReceived(MobPushCustomMessage message) {

        Map<String, Object> map = new HashMap<>();

        map.put("content", message.getContent());
        map.put("extras", message.getExtrasMap());
        map.put("messageId", message.getMessageId());
        map.put("timestamp", message.getTimestamp());

        JSONObject jsonObject = new JSONObject(map);

        String format = "window.MobPush.receivedLocalMessageInAndroidCallback(%s)";
        String jsString = String.format(format, jsonObject.toString());

        instance.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + jsString);
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (instance != null) {
            instance = null;
        }
    }
}
