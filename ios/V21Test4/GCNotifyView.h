//
//  GCNotifyView.h
//  V21Test4
//
//  Created by livstar on 2019/12/3.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>
#import <React/RCTScrollView.h>
#import "GCNotifyVisiableModel.h"
#import "GCViewManager.h"

NS_ASSUME_NONNULL_BEGIN

@interface GCNotifyView : UIView
@property (nonatomic, copy) RCTBubblingEventBlock onIndexChange;
@property (nonatomic) CGFloat y;
@property (nonatomic) NSInteger index;
// Priority to use same category element
@property (nonatomic) NSInteger lastCategory;
@property (nonatomic, weak) RCTScrollView*  rctScrollView;
@property (nonatomic, weak) UIView* parentView;
- (void)notifyRebind:(GCNotifyVisiableModel*) model isForce: (BOOL) isForce;
-(void)emptyBind;
@end

NS_ASSUME_NONNULL_END
