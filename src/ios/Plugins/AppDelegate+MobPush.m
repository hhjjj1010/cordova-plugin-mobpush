

#import "AppDelegate+MobPush.h"
#import <MobPush/MobPush.h>
#import <MOBFoundation/MobSDK.h>
#import <objc/runtime.h>
#import "MobPushPlugin.h"
#import <UserNotifications/UserNotifications.h>

@implementation AppDelegate (MobPush)

+ (void)load {
    Method origin1;
    Method swizzle1;
    origin1  = class_getInstanceMethod([self class],@selector(init));
    swizzle1 = class_getInstanceMethod([self class], @selector(init_plus));
    method_exchangeImplementations(origin1, swizzle1);
}

- (instancetype)init_plus{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidLaunch:) name:UIApplicationDidFinishLaunchingNotification object:nil];
    return [self init_plus];
}

- (void)applicationDidLaunch:(NSNotification *)notification{
    // 设置推送环境
#ifdef DEBUG
    [MobPush setAPNsForProduction:NO];
#else
    [MobPush setAPNsForProduction:YES];
#endif

    //MobPush推送设置（获得角标、声音、弹框提醒权限）
    MPushNotificationConfiguration *configuration = [[MPushNotificationConfiguration alloc] init];
    configuration.types = MPushAuthorizationOptionsBadge | MPushAuthorizationOptionsSound | MPushAuthorizationOptionsAlert;
    [MobPush setupNotification:configuration];

    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didReceiveMessage:) name:MobPushDidReceiveMessageNotification object:nil];
}

// 收到通知回调
- (void)didReceiveMessage:(NSNotification *)notification
{
    MPushMessage *message = notification.object;

    NSError *error;
    NSData *data = [NSJSONSerialization dataWithJSONObject:message.msgInfo options:0 error:&error];
    NSString *jsonStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];

    switch (message.messageType)
    {
        case MPushMessageTypeCustom: {
            // 自定义消息回调
            NSLog(@"收到自定义消息");
            [MobPushPlugin fireDocumentEvent:@"receivedCustomMessage" jsString:jsonStr];
        }
            break;
        case MPushMessageTypeAPNs: {
            // APNs回调
            NSLog(@"收到APNs消息");
            [MobPushPlugin fireDocumentEvent:@"receivedNotificationMessage" jsString:jsonStr];
        }
            break;
        case MPushMessageTypeLocal: {
            // 本地通知回调
            NSLog(@"收到本地通知");
            [MobPushPlugin fireDocumentEvent:@"receivedLocalMessage" jsString:jsonStr];
        }
            break;
        case MPushMessageTypeClicked: {
            // 点击通知回调
            [MobPushPlugin fireDocumentEvent:@"openedNotificationMessage" jsString:jsonStr];
        }
        default:
            break;
    }
}

/******************************************************
// model转化为字典
- (NSDictionary *)dictFromObject:(NSObject *)object {
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    unsigned int count;
    objc_property_t *propertyList = class_copyPropertyList([object class], &count);

    for (int i = 0; i < count; i++) {
        objc_property_t property = propertyList[i];
        const char *cName = property_getName(property);
        NSString *name = [NSString stringWithUTF8String:cName];
        NSObject *value = [object valueForKey:name];//valueForKey返回的数字和字符串都是对象

        if ([value isKindOfClass:[NSString class]] || [value isKindOfClass:[NSNumber class]]) {
            //string , bool, int ,NSinteger
            [dict setObject:value forKey:name];

        } else if ([value isKindOfClass:[NSArray class]] || [value isKindOfClass:[NSDictionary class]]) {
            //字典或字典
            [dict setObject:[self arrayOrDictWithObject:(NSArray*)value] forKey:name];

        } else if (value == nil) {
            //null
            //[dic setObject:[NSNull null] forKey:name];//这行可以注释掉?????
        } else {
            //model
            [dict setObject:[self dictFromObject:value] forKey:name];
        }
    }

    return [dict copy];
}

// 将可能存在model数组转化为普通数组
- (id)arrayOrDictWithObject:(id)origin {
    if ([origin isKindOfClass:[NSArray class]]) {
        //数组
        NSMutableArray *array = [NSMutableArray array];
        for (NSObject *object in origin) {
            if ([object isKindOfClass:[NSString class]] || [object isKindOfClass:[NSNumber class]]) {
                //string , bool, int ,NSInteger
                [array addObject:object];

            } else if ([object isKindOfClass:[NSArray class]] || [object isKindOfClass:[NSDictionary class]]) {
                //数组或字典
                [array addObject:[self arrayOrDictWithObject:(NSArray *)object]];
            } else {
                //model
                [array addObject:[self dictFromObject:object]];
            }
        }

        return [array copy];

    } else if ([origin isKindOfClass:[NSDictionary class]]) {
        //字典
        NSDictionary *originDict = (NSDictionary *)origin;
        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
        for (NSString *key in originDict.allKeys) {
            id object = [originDict objectForKey:key];

            if ([object isKindOfClass:[NSString class]] || [object isKindOfClass:[NSNumber class]]) {
                //string , bool, int ,NSInteger
                [dict setObject:object forKey:key];

            } else if ([object isKindOfClass:[NSArray class]] || [object isKindOfClass:[NSDictionary class]]) {
                //数组或字典
                [dict setObject:[self arrayOrDictWithObject:object] forKey:key];

            } else {
                //model
                [dict setObject:[self dictFromObject:object] forKey:key];
            }
        }

        return [dict copy];
    }

    return [NSNull null];
}
 ************************************************** */


- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    NSString *token = [[[[deviceToken description] stringByReplacingOccurrencesOfString:@"<"withString:@""]
                        stringByReplacingOccurrencesOfString:@">" withString:@""]
                       stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSLog(@"devicetoken：%@", token);
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end
