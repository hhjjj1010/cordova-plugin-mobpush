# MobPush cordova / cordova-plugin-mobpush
- [插件源码地址](https://github.com/hhjjj1010/cordova-plugin-mobpush.git)
- [Mob 官方文档](http://wiki.mob.com)
- 支持5大Android厂商通道（小米、华为、魅族、OPPO、VIVO）
- 支持FCM（此插件暂未配置FCM，若需要请自行参照Mob官方文档修改gradle）

Android推送，优先走厂商通道，若不满足走厂商通道的则走MobPush的长连接。
走厂商通道，在app被杀死的情况下也能收到推送；走MobPush的长连接，则只有app未被杀死的情况下才能收到推送。
***

# 安装

## 在线安装
``` shell
cordova plugin add https://github.com/hhjjj1010/cordova-plugin-mobpush.git --variable APP_KEY=your mobpush appkey --variable APP_SECRET=your mobpush appSecret
```

## 本地安装
下载插件到本地
``` shell
cordova plugin add /your/local/path --variable APP_KEY=your mobpush appkey --variable APP_SECRET=your mobpush appSecret
```

***

# Android 使用说明（必须）
#### 执行 cordova plugin add 命令添加插件之后，还有两个步骤是必须要做的。

第一步，修改此插件目录下的src/android/mobpush.gradle文件，配置相关appkey之类的信息。厂商通道的配置为选配，若不需要就无需配置。

``` gradle
apply plugin: 'com.mob.sdk'

// 在MobSDK的扩展中注册MobPush的相关信息
MobSDK {
appKey "替换为mob官方申请的appkey"
appSecret "替换为mob官方申请的appkey对应的appSecret"

MobPush {

    //设置角标开关（不需要可不设置）
    badge true
    //集成其他推送通道（可选）
    devInfo {
        //华为推送配置信息
        HUAWEI {
            appId "华为的appid"
        }

        //魅族推送配置信息
        MEIZU {
            appId "魅族的appid"
            appKey "魅族的appkey"
        }

        //小米推送配置信息
        XIAOMI {
            appId "小米的appid"
            appKey "小米的appkey"
        }
        //FCM推送通道配置
        FCM {
            //设置默认推送通知显示图标
            iconRes "@mipmap/ic_launcher"
        }

       //OPPO推送配置信息
        OPPO {
            appKey "OPPO的appKey"
            appSecret "OPPO的appSecret"
        }

        //VIVO推送配置信息
        VIVO {
           appId "应用对应的vivo appID"
           appKey "应用对应的vivo appKey"
        }
    }
}
```

第二步，执行cordova build android 命令之后，使用android studio打开platforms/android文件夹，在 __build.gradle(Moudle:app)__ 文件的 dependencies 添加 classpath 'com.mob.sdk:MobSDK。
``` gradle
apply plugin: 'com.android.application'

buildscript {
    repositories {
        mavenCentral()
        maven {
            url "https://maven.google.com"
        }
        jcenter()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.1'
        classpath 'com.mob.sdk:MobSDK:+' // MobSDK
    }
}
```

***
# 使用

## API
- [初始化、停止与恢复推送服务](#初始化、停止与恢复推送服务)
  - [initPush](#initPush)
  - [getRegistrationId](#getRegistrationId)
  - [isPushStopped](#isPushStopped)
  - [stopPush](#stopPush)
  - [restartPush](#restartPush)
- [设置别名与标签](#设置别名与标签)
  - [setAlias](#setAlias)
  - [getAlias](#getAlias)
  - [deleteAlias](#deleteAlias)
  - [addTags](#addTags)
  - [getTags](#getTags)
  - [deleteTags](#deleteTags)
  - [cleanAllTags](#cleanAllTags)
- [获取点击通知内容](#获取点击通知内容)
  - [event-mobpush.openedNotificationMessage](#event-mobpush.openedNotificationMessage)
- [获取通知内容](#获取通知内容)
  - [event-mobpush.receivedNotificationMessage](#event-mobpush.receivedNotificationMessage)
- [获取自定义消息内容](#获取自定义消息内容)
   - [event-mobpush.receivedLocalMessage](#event-mobpush.receivedLocalMessage)
- [绑定手机号码](#绑定手机号码)
  - [bindPhoneNumber](#bindPhoneNumber)
- [添加本地通知](#添加本地通知)
  - [addLocalNotification](#addLocalNotification)
- [Badge](#Badge)
  - [setBadge](#setBadge)
  - [clearBadge](#clearBadge)
  - [setApplicationIconBadgeNumber](#setApplicationIconBadgeNumber)
  - [getApplicationIconBadgeNumber](#getApplicationIconBadgeNumber)

### 初始化、停止与恢复推送服务
#### initPush
~~初始化推送服务~~。*此方法无任何实质作用，只会在控制台打印输出+--- MobPush initPush ----+。*

代码示例：
``` js
window.MobPush.initPush();
```
#### getRegistrationId
代码示例：
``` js
window.MobPush.getRegistrationId(function success(id) {
  console.log(id);
}, function error(err) {
  console.log(err);
});
```
#### isPushStopped
检查推送服务是否停止。

代码示例：
``` js
window.MobPush.isPushStopped(function(bool) {

});
```
#### stopPush
停止推送服务。

代码示例：
``` js
window.MobPush.stopPush();
```
#### restartPush
重启推送服务。

代码示例：
``` js
window.MobPush. restartPush();
```
### 设置别名与标签
#### setAlias
设置别名，覆盖操作。

代码示例：
``` js
window.MobPush.setAlias(function() {
  // 设置别名成功
}, function() {
  // 设置别名失败
});
```

#### getAlias
获取别名。

代码示例：
```js
window.MobPush.getAlias(function(alias) {
  // 获取别名成功
}, function() {
  // 获取别名失败
});
```

#### deleteAlias
删除别名。

代码示例：
```js
window.MobPush.deleteAlias(function() {
  // 删除别名成功
}, function() {
  // 删除别名失败
});
```

#### addTags
添加标签，增量操作。

代码示例：
```js
let tags = ['tag1', 'tag2'];
window.MobPush.addTags(tags, function() {
  // 添加标签成功
}, function() {
  // 添加标签失败
});
```

 #### getTags
获取标签。

代码示例：
```js
window.MobPush.getTags(function(tags) {
  // 获取标签成功，返回标签数组
}, function() {
  // 删除别名失败
});
```

#### deleteTags
删除标签。

代码示例：
```js
let tags = ['tag1', 'tag2']
window.MobPush.deleteTags(tags, function() {
  // 删除标签成功
}, function() {
  // 删除标签失败
});
```

#### cleanAllTags
清除所有的标签。

代码示例：
```js
window.MobPush. cleanAllTags(function() {
  // 清除所有的标签成功
}, function() {
  // 清除所有的标签失败
});
```

### 获取点击通知内容
#### event-mobpush.openedNotificationMessage
点击通知进入应用程序时触发。

代码示例：
在你需要接收通知点击事件的 js 文件中加入一下代码（Ps：ionic1通常是加在app.js的run方法里面）。
``` js
/** 点击了推送消息 */
document.addEventListener("mobpush.openedNotificationMessage", function (event) {
  // alert(angular.toJson(event));

  let url, messageId; // 此处的 url 和 messageId 为附加字段示例

  if (ionic.Platform.isIOS()) {
    url = event.url;
    messageId = event.messageId;
  } else if (ionic.Platform.isAndroid()) {
    url = event.extras.url;
    messageId = event.extras.messageId;
  }
});
```

### 获取通知内容
#### event-mobpush.receivedNotificationMessage
收到通知时触发。

代码示例：
在你需要接收通知事件的 js 文件中加入一下代码（Ps：ionic1通常是加在app.js的run方法里面）。
``` js
 /** 收到推送消息 */
document.addEventListener("mobpush.receivedNotificationMessage", function (event) {

});
```

### 获取自定义消息内容
#### event-mobpush.receivedLocalMessage
收到本地推送时触发。

代码示例：
在你需要接收自定义消息事件的 js 文件中加入一下代码（Ps：ionic1通常是加在app.js的run方法里面）。
``` js
/** 收到自定义消息 */
document.addEventListener("mobpush.receivedLocalMessage", function (event) {

});
```

### 绑定手机号码
#### bindPhoneNumber
绑定手机号码后，可以在推送无法到达的时候，发送手机短信通知用户。

代码示例：
``` js
let mobile = "13000000000";
window.MobPush. bindPhoneNumber(mobile, function() {
  // 绑定手机号码成功
}, function() {
  // 绑定手机号码失败
});
```

### 添加本地通知
#### addLocalNotification
添加本地通知。

代码示例：
``` js
let noti = {
  title: "noti title",
  body: "noti content",
  sound: true
};

window.MobPush. addLocalNotification(noti, function() {
  // 添加本地通知成功
}, function() {
  // 添加本地通知失败
});
```

### Badge (iOS Only)
#### setBadge
设置角标数字。

代码示例：
``` js
window.MobPush.setBadge(1);
```

#### clearBadge
清除角标数字。

代码示例：
``` js
window.MobPush. clearBadge();
```

#### setApplicationIconBadgeNumber
设置角标数字。

代码示例：
``` js
window.MobPush. setApplicationIconBadgeNumber(1);
```

#### getApplicationIconBadgeNumber
获取角标数字。

代码示例：
``` js
window.MobPush.getApplicationIconBadgeNumber(function(num) {

});
```

***
# Tips
1. MobPush后台配置iOS时一定要配置推送证书。
2. OPPO开发者账号个人账号目前不能上架APP。
3. VIVO开发者账号目前不支持个人账号。
4. OPPO和VIVO的厂商通道目前是有限制的，具体请查看Mob官方文档或各平台文档。
5. 华为厂商通道需将app打包为release才能测试，且EMUI 5以上的手机才支持厂商通道。
