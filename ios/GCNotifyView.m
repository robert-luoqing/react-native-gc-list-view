//
//  GCNotifyView.m
//  V21Test4
//
//  Created by livstar on 2019/12/3.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "GCNotifyView.h"
#import "GCCoordinatorView.h"
#import "GCScrollItemView.h"

@implementation GCNotifyView {
  NSTimeInterval lastBind;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (void)didMoveToSuperview {
  [super didMoveToSuperview];
  [self initAndRegistSelf];
}

- (void)layoutSubviews {
  [super layoutSubviews];
  [self initAndRegistSelf];
}

- (void) initAndRegistSelf {
  if(_parentView == nil) {
    _parentView = [self findGCScrollItemView: self];
  }
  if(self.rctScrollView == nil) {
    self.rctScrollView = [self findRCTScrollView: self];
    if(self.rctScrollView!=nil) {
      GCCoordinatorView* coordinatorView = [self findGCCoordinatorView: self];
      [coordinatorView registerView:self];
      [coordinatorView scrollViewDidScroll:self.rctScrollView.scrollView];
    }
  } else {
    GCCoordinatorView* coordinatorView = [self findGCCoordinatorView: self];
    [coordinatorView scrollViewDidScroll:self.rctScrollView.scrollView];
  }
}

-(GCScrollItemView*) findGCScrollItemView: (UIView *) item {
    UIView * ptem = item.superview;
    if(ptem != nil) {
        if([ptem class] == [GCScrollItemView class]) {
          return (GCScrollItemView*)ptem;
        } else {
          return [self findGCScrollItemView: ptem];
        }
    } else {
      return nil;
    }
}

-(RCTScrollView*) findRCTScrollView: (UIView *) item {
    UIView * ptem = item.superview;
    if(ptem != nil) {
        if([ptem isKindOfClass:[RCTScrollView class]]) {
          return (RCTScrollView*)ptem;
        } else {
          return [self findRCTScrollView: ptem];
        }
    } else {
      return nil;
    }
}

-(GCCoordinatorView*) findGCCoordinatorView: (UIView *) item {
    UIView * ptem = item.superview;
    if(ptem != nil) {
        if([ptem isKindOfClass:[GCCoordinatorView class]]) {
          return (GCCoordinatorView*)ptem;
        } else {
          return [self findGCCoordinatorView: ptem];
        }
    } else {
      return nil;
    }
}

- (void)notifyRebind:(GCNotifyVisiableModel*) model isForce: (BOOL) isForce {
  NSInteger oldIndex = self.index;
  NSTimeInterval now = [[NSDate date] timeIntervalSince1970];
  CGRect rect = _parentView.frame;
  
  self.index = model.index;
  if(_parentView!=nil) {
    _parentView.frame = CGRectMake(rect.origin.x, model.startY, self.rctScrollView.frame.size.width, model.endY-model.startY);
  }
  
  if(isForce) {
    [GCViewManager notifyIndexChanged:self index:model.index startY:model.startY endY:model.endY];
  } else {
    if(oldIndex!=self.index
       || rect.origin.y != model.startY
       || rect.size.height != (model.endY-model.startY)
       ) {
      [GCViewManager notifyIndexChanged:self index:model.index startY:model.startY endY:model.endY];
    } else {
      if((now-lastBind)>200) {
        [GCViewManager notifyIndexChanged:self index:model.index startY:model.startY endY:model.endY];
      }
    }
  }
  
  lastBind = now;
}

-(void)emptyBind {
//  self.index = -1;
  if(_parentView!=nil) {
    CGRect rect = _parentView.frame;
    _parentView.frame = CGRectMake(rect.origin.x, -100000, rect.size.width, rect.size.height);
  }
}

@end
