//
//  GCViewManager.m
//  V21Test4
//
//  Created by livstar on 2019/12/3.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "GCViewManager.h"
#import "GCNotifyView.h"

@implementation GCViewManager {
  
}

RCT_EXPORT_MODULE(GCNotifyView)
RCT_EXPORT_VIEW_PROPERTY(onIndexChange, RCTBubblingEventBlock)

- (UIView *)view
{
 GCNotifyView* notifyView = [[GCNotifyView alloc] init];
  notifyView.index =-1;
  return notifyView;
}

+ (void) notifyIndexChanged:(id) notifyView index: (NSInteger) index startY:(CGFloat)startY endY: (CGFloat)endY {
  GCNotifyView* nv = (GCNotifyView*)notifyView;
  if (!nv.onIndexChange) {
    return;
  }
  
  nv.onIndexChange(@{
      @"index": @(index),
      @"startY": @(startY),
      @"endY": @(endY),
  });
}
@end
