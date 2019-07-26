/********* MobPush.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface MobPushPlugin : CDVPlugin {
  // Member variables go here.
}

- (void)initPush:(CDVInvokedUrlCommand *)command;

- (void)addLocalNotification:(CDVInvokedUrlCommand *)command;

- (void)getRegistrationID:(CDVInvokedUrlCommand *)command;
- (void)addTags:(CDVInvokedUrlCommand *)command;
- (void)getTags:(CDVInvokedUrlCommand *)command;
- (void)deleteTags:(CDVInvokedUrlCommand *)command;
- (void)cleanAllTags:(CDVInvokedUrlCommand *)command;

- (void)setAlias:(CDVInvokedUrlCommand *)command;
- (void)getAlias:(CDVInvokedUrlCommand *)command;
- (void)deleteAlias:(CDVInvokedUrlCommand *)command;

- (void)setBadge:(CDVInvokedUrlCommand *)command;
- (void)clearBadge:(CDVInvokedUrlCommand *)command;

- (void)setApplicationIconBadgeNumber:(CDVInvokedUrlCommand*)command;
- (void)getApplicationIconBadgeNumber:(CDVInvokedUrlCommand*)command;

- (void)bindPhoneNumber:(CDVInvokedUrlCommand *)command;

- (void)isPushStopped:(CDVInvokedUrlCommand *)command;
- (void)stopPush:(CDVInvokedUrlCommand *)command;
- (void)restartPush:(CDVInvokedUrlCommand *)command;

+ (void)fireDocumentEvent:(NSString*)eventName jsString:(NSString*)jsString;

@end

static MobPushPlugin *SharedMobPushPlugin;