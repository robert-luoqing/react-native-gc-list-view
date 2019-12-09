//
//  GCCoordinatorManager.m
//  V21Test4
//
//  Created by livstar on 2019/12/3.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "GCCoordinatorManager.h"
#import "GCCoordinatorView.h"

@implementation GCCoordinatorManager
RCT_EXPORT_MODULE(GCCoordinatorView)
RCT_EXPORT_VIEW_PROPERTY(forceIndex, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(pixelRatio, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(itemLayouts, NSArray<NSNumber *>)
RCT_EXPORT_VIEW_PROPERTY(categories, NSArray<NSNumber *>)

- (UIView *)view
{
  return [[GCCoordinatorView alloc] init];
}
@end
