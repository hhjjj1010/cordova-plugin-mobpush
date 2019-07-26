/********* MobPush.m Cordova Plugin Implementation *******/

#import "MobPushPlugin.h"
#import <MobPush/MobPush.h>
#import <MOBFoundation/MobSDK.h>

@interface MobPushPlugin ()

@end

@implementation MobPushPlugin

- (void)pluginInitialize {
    [super pluginInitialize];
    if (!SharedMobPushPlugin) {
        SharedMobPushPlugin = self;
    }
}

- (void)initPush:(CDVInvokedUrlCommand*)command
{
    // do nothing!!!
    NSLog(@"+---- MobPush initPush ----+");
}

#pragma mark - tags
- (void)addTags:(CDVInvokedUrlCommand *)command {
    NSArray* tags = [command.arguments objectAtIndex:0];

    [MobPush addTags:tags result:^(NSError *error) {
        if (error) {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR] callbackId:command.callbackId];
        } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
        }
    }];
}

- (void)getTags:(CDVInvokedUrlCommand *)command {
    [MobPush getTagsWithResult:^(NSArray *tags, NSError *error) {
        if (error) {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR] callbackId:command.callbackId];
        } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:tags] callbackId:command.callbackId];
        }
    }];
}

- (void)deleteTags:(CDVInvokedUrlCommand *)command {
    NSArray* tags = [command.arguments objectAtIndex:0];
    [MobPush deleteTags:tags result:^(NSError *error) {
        if (error) {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR] callbackId:command.callbackId];
        } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
        }
    }];
}

- (void)cleanAllTags:(CDVInvokedUrlCommand *)command {
    [MobPush cleanAllTags:^(NSError *error) {
        if (error) {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR] callbackId:command.callbackId];
        } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
        }
    }];
}

#pragma mark - alias
- (void)setAlias:(CDVInvokedUrlCommand *)command {
    NSString *alias = [command.arguments objectAtIndex:0];
    [MobPush setAlias:alias result:^(NSError *error) {
        if (error) {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR] callbackId:command.callbackId];
        } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
        }
    }];
}

- (void)getAlias:(CDVInvokedUrlCommand *)command {
    [MobPush getAliasWithResult:^(NSString *alias, NSError *error) {
        if (error) {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR] callbackId:command.callbackId];
        } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:alias] callbackId:command.callbackId];
        }
    }];
}

- (void)deleteAlias:(CDVInvokedUrlCommand *)command {
    [MobPush deleteAlias:^(NSError *error) {
        if (error) {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR] callbackId:command.callbackId];
        } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
        }
    }];
}

#pragma mark - registration id
- (void)getRegistrationID:(CDVInvokedUrlCommand *)command {
    [MobPush getRegistrationID:^(NSString *registrationID, NSError *error) {
        if (error) {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR] callbackId:command.callbackId];
        } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:registrationID] callbackId:command.callbackId];
        }
    }];
}

#pragma mark - status
- (void)isPushStopped:(CDVInvokedUrlCommand *)command {
    BOOL stop = [MobPush isPushStopped];
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:stop] callbackId:command.callbackId];
}

- (void)stopPush:(CDVInvokedUrlCommand *)command {
    [MobPush stopPush];
}

- (void)restartPush:(CDVInvokedUrlCommand *)command {
    [MobPush restartPush];
}

#pragma mark - local notification
- (void)addLocalNotification:(CDVInvokedUrlCommand *)command {
    NSDictionary *dict = [command.arguments objectAtIndex:0];

    MPushMessage *message = [[MPushMessage alloc] init];
    message.messageType = MPushMessageTypeLocal;

    MPushNotification *mnoti = [[MPushNotification alloc] init];

    mnoti.body = dict[@"body"];
    mnoti.title = dict[@"title"];
    mnoti.subTitle = dict[@"subTitle"];
    mnoti.sound = dict[@"sound"];
    mnoti.badge = ([UIApplication sharedApplication].applicationIconBadgeNumber < 0 ? 0 : [UIApplication sharedApplication].applicationIconBadgeNumber) + 1;
    message.notification = mnoti;

    NSInteger timeValue = (NSInteger)dict[@"timeValue"];

    if (timeValue) {
        // 设置几分钟后发起本地推送
        NSDate *currentDate = [NSDate dateWithTimeIntervalSinceNow:0];
        NSTimeInterval nowtime = [currentDate timeIntervalSince1970] * 1000;
        NSTimeInterval taskDate = nowtime + timeValue*60*1000;
        message.taskDate = taskDate;
    } else {
        message.isInstantMessage = YES;
    }

    [MobPush addLocalNotification: message];
}

#pragma mark - bind phone number
- (void)bindPhoneNumber:(CDVInvokedUrlCommand *)command {
    NSString *phoneNumber = [command.arguments objectAtIndex:0];
    [MobPush bindPhoneNum:phoneNumber result:^(NSError *error) {
        if (error) {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR] callbackId:command.callbackId];
        } else {
            [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK] callbackId:command.callbackId];
        }
    }];
}

#pragma mark - badge
- (void)setApplicationIconBadgeNumber:(CDVInvokedUrlCommand*)command {
    NSNumber *badge = [command argumentAtIndex:0];
    [UIApplication sharedApplication].applicationIconBadgeNumber = badge.intValue;
}

- (void)getApplicationIconBadgeNumber:(CDVInvokedUrlCommand*)command {
    NSInteger num = [UIApplication sharedApplication].applicationIconBadgeNumber;
    NSNumber *number = [NSNumber numberWithInteger:num];
    [self.commandDelegate sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsNSInteger:number.integerValue] callbackId:command.callbackId];
}

- (void)setBadge:(CDVInvokedUrlCommand *)command {
    NSNumber *badge = [command argumentAtIndex:0];
    [MobPush setBadge:badge.integerValue];
}

- (void)clearBadge:(CDVInvokedUrlCommand *)command {
    [MobPush clearBadge];
}

#pragma mark - private method
+ (void)fireDocumentEvent:(NSString *)eventName jsString:(NSString *)jsString {
    if (SharedMobPushPlugin) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [SharedMobPushPlugin.commandDelegate evalJs:[NSString stringWithFormat:@"cordova.fireDocumentEvent('mobpush.%@',%@)", eventName, jsString]];
        });
        return;
    }
}

@end
