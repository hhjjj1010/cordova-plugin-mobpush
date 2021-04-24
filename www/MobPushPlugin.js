var exec = require('cordova/exec');

exports.initPush = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'initPush');
};

exports.getRegistrationId = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'getRegistrationId');
};

exports.openNotificationSettings = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'openNotificationSettings');
};

exports.addLocalNotification = function (arg0, success, error) {
    exec(success, error, 'MobPushPlugin', 'addLocalNotification', [arg0]);
};

exports.addTags = function (arg0, success, error) {
    exec(success, error, 'MobPushPlugin', 'addTags', [arg0]);
};
exports.getTags = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'getTags');
};
exports.deleteTags = function (arg0,success, error) {
    exec(success, error, 'MobPushPlugin', 'deleteTags', [arg0]);
};
exports.cleanAllTags = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'cleanAllTags');
};

exports.setAlias = function (arg0, success, error) {
    exec(success, error, 'MobPushPlugin', 'setAlias', [arg0]);
};

exports.getAlias = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'getAlias');
};

exports.deleteAlias = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'deleteAlias');
};

exports.setBadge = function (arg0, success, error) {
    exec(success, error, 'MobPushPlugin', 'setBadge', [arg0]);
};

exports.clearBadge = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'clearBadge');
};

exports.setApplicationIconBadgeNumber = function (arg0, success, error) {
    exec(success, error, 'MobPushPlugin', 'setApplicationIconBadgeNumber', [arg0]);
};

exports.getApplicationIconBadgeNumber = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'getApplicationIconBadgeNumber');
};

exports.isPushStopped = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'isPushStopped');
};

exports.stopPush = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'stopPush');
};

exports.restartPush = function (success, error) {
    exec(success, error, 'MobPushPlugin', 'restartPush');
};

exports.bindPhoneNumber = function (arg0, success, error) {
    exec(success, error, 'MobPushPlugin', 'bindPhoneNumber', [arg0]);
};

exports.openedNotificationMessageInAndroidCallback = function (data) {
    data = JSON.stringify(data);
    cordova.fireDocumentEvent("mobpush.openedNotificationMessage", JSON.parse(data));
};

exports.receivedNotificationMessageInAndroidCallback = function (data) {
    data = JSON.stringify(data);
    cordova.fireDocumentEvent("mobpush.receivedNotificationMessage", JSON.parse(data));
};

exports.receivedLocalMessageInAndroidCallback = function (data) {
    data = JSON.stringify(data);
    cordova.fireDocumentEvent("mobpush.receivedLocalMessage", JSON.parse(data));
};
