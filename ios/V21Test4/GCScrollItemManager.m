//
//  GCScrollItemManager.m
//  V21Test4
//
//  Created by livstar on 2019/12/4.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "GCScrollItemManager.h"
#import "GCScrollItemView.h"


@implementation GCScrollItemManager
RCT_EXPORT_MODULE(GCScrollItemContainerView)
- (UIView *)view
{
  return [[GCScrollItemView alloc] init];
}
@end
