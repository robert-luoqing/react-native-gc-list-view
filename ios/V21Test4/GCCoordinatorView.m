//
//  GCCoordinatorView.m
//  V21Test4
//
//  Created by livstar on 2019/12/3.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "GCCoordinatorView.h"
#import <React/RCTScrollContentView.h>
#import <React/RCTScrollView.h>
#import "GCNotifyView.h"
#import "GCNotifyVisiableModel.h"

@implementation GCCoordinatorView  {
  
}

@synthesize forceIndex = _forceIndex;

-(NSInteger)forceIndex
{
  return _forceIndex;
}

-(void)setForceIndex:(NSInteger) index
{
  if (index != _forceIndex) {
    _forceIndex = index;
    if(self.rctScrollView!=nil) {
      [self handleScroll: self.rctScrollView.scrollView isForce:TRUE];
    }
  }
}

@synthesize itemLayouts = _itemLayouts;

-(NSArray<NSNumber *>*)itemLayouts
{
  return _itemLayouts;
}

-(void)setItemLayouts:(NSArray<NSNumber *>*) itemLayouts
{
  if (itemLayouts != _itemLayouts) {
    _itemLayouts = itemLayouts;
    if(self.rctScrollView!=nil) {
      [self handleScroll: self.rctScrollView.scrollView isForce:TRUE];
    }
  }
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
  [self initRelevantView];
}

- (void)layoutSubviews {
  [super layoutSubviews];
  [self initRelevantView];
}

- (void) initRelevantView {
  if(_rctScrollView == nil) {
    _rctScrollView = [self findRCTScrollView: self];
    if(_rctScrollView!=nil) {
      if(_rctScrollView.gcDelegate == nil) {
        _rctScrollView.gcDelegate = self;
        [self handleScroll: self.rctScrollView.scrollView isForce:FALSE];
      }
    }
  } else {
    [self handleScroll: self.rctScrollView.scrollView isForce:FALSE];
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


-(void) notifyToShowIndex: (NSMutableArray*) showObjs isForce:(BOOL) isForce {
  NSMutableArray* matchedNotifyViews = [self getMatchedNotifyViews];
  if([showObjs count]>0 && [matchedNotifyViews count]>0) {
    NSInteger notifyCount = [matchedNotifyViews count];
    NSMutableDictionary* assignedViews = [[NSMutableDictionary alloc] init];
    
    
    for(NSInteger notifyIndex=0;notifyIndex<notifyCount;notifyIndex++) {
      GCNotifyView*notifyObj = [matchedNotifyViews objectAtIndex:notifyIndex];
      if(notifyObj.index>=0) {
        [assignedViews setValue:notifyObj forKey:[NSString stringWithFormat:@"%ld", (long)notifyObj.index]];
      }
    }
    
    NSMutableArray* unhandledShowObjs = [[NSMutableArray alloc] init];
    for(NSInteger showIndex = 0;showIndex< [showObjs count];showIndex++) {
      GCNotifyVisiableModel* showObj =[showObjs objectAtIndex:showIndex];
      NSString *key = [NSString stringWithFormat:@"%ld", (long)showObj.index];
      GCNotifyView *notifyObj = [assignedViews objectForKey:key];
      if(notifyObj!=nil) {
        [notifyObj notifyRebind:showObj isForce:isForce];
        [matchedNotifyViews removeObject:notifyObj];
      } else {
        [unhandledShowObjs addObject:showObj];
      }
    }
    
    for(NSInteger i=0;i<[unhandledShowObjs count];i++) {
      GCNotifyVisiableModel* showObj =[unhandledShowObjs objectAtIndex:i];
      if([matchedNotifyViews count]>0) {
        GCNotifyView *notifyObj = [matchedNotifyViews objectAtIndex:0];
        [notifyObj notifyRebind:showObj isForce:isForce];
        [matchedNotifyViews removeObject:notifyObj];
      }
    }
  }
  
  for(NSInteger index = 0; index < [matchedNotifyViews count]; index++) {
    GCNotifyView* notifyView = matchedNotifyViews[index];
    [notifyView emptyBind];
  }
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
  [self handleScroll: scrollView isForce:FALSE];
}

-(void) handleScroll:(UIScrollView *)scrollView isForce:(BOOL) isForce {
  CGFloat minY=scrollView.contentOffset.y-50;
  if(minY<0) { minY = 0; }
  CGFloat maxY = minY + scrollView.frame.size.height+100;
  
  Boolean isPassed = false;
  NSMutableArray* showObjs = [[NSMutableArray alloc] initWithCapacity:100];
  NSUInteger itemCount = 0;
  if(self.itemLayouts != nil) {
    itemCount = [self.itemLayouts count];
  }
  
  for(NSInteger i= 0;i<itemCount;i++) {
    CGFloat startY = 0;
    CGFloat endY = [self.itemLayouts[i] doubleValue];
    if(i!=0) {
      startY = [self.itemLayouts[i-1] doubleValue];
    }
    
    // It mean the element fall in visual area
    if((startY<=minY && endY>=minY)
       ||(startY>=minY && endY<=maxY)
       ||(startY<=maxY && endY>=maxY)
       ) {
      
      GCNotifyVisiableModel* notifyVisiableModel = [[GCNotifyVisiableModel alloc] init];
      notifyVisiableModel.index = i;
      notifyVisiableModel.startY = startY;
      notifyVisiableModel.endY = endY;
      // to show the item
      [showObjs addObject: notifyVisiableModel];
      isPassed = true;
    } else {
      if(isPassed) {
        break;
      }
    }
  }
  
  [self notifyToShowIndex: showObjs isForce: isForce];
}

- (void)dealloc {
}

- (NSMutableArray*) getMatchedNotifyViews {
  NSMutableArray *matchedViews = [[NSMutableArray alloc] initWithCapacity:100];
  
  if(self.notifyViews!=nil) {
    NSArray* allObjs = [self.notifyViews allObjects];
    for (NSInteger i=[allObjs count]; i>0; i--) {
      GCNotifyView* notifyView =(GCNotifyView*) [allObjs objectAtIndex:i-1];
      [matchedViews addObject:notifyView];
    }
  }
  
  return matchedViews;
}

- (void)registerView: (GCNotifyView*) notifyView {
  if(self.notifyViews==nil) {
    // self.notifyViews = [[NSMutableArray alloc] initWithCapacity:100];
    self.notifyViews = [NSPointerArray weakObjectsPointerArray];
  }
  [self.notifyViews addPointer:(__bridge void*)(notifyView)];
  //[self.notifyViews addObject:notifyView];
}
@end
