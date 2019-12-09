//
//  GCCoordinatorView.h
//  V21Test4
//
//  Created by livstar on 2019/12/3.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GCNotifyView.h"
#import <React/RCTView.h>

NS_ASSUME_NONNULL_BEGIN

@interface GCCoordinatorView : RCTView<UIScrollViewDelegate>
- (void)registerView: (GCNotifyView*) notifyView;
@property (nonatomic, weak) RCTScrollView*  rctScrollView;
@property (nonatomic) NSInteger forceIndex;
@property (nonatomic, strong) NSPointerArray *notifyViews;
// The array will save the end of position of element
@property (nonatomic, strong) NSArray<NSNumber *> *itemLayouts;
@property (nonatomic, strong) NSArray<NSString *> *categories;
@property (nonatomic) CGFloat pixelRatio;
@end

NS_ASSUME_NONNULL_END
