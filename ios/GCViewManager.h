//
//  GCViewManager.h
//  V21Test4
//
//  Created by livstar on 2019/12/3.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>

NS_ASSUME_NONNULL_BEGIN

@interface GCViewManager : RCTViewManager
+ (void) notifyIndexChanged:(id) notifyView index: (NSInteger) index startY:(CGFloat)startY endY: (CGFloat)endY;
@end

NS_ASSUME_NONNULL_END
