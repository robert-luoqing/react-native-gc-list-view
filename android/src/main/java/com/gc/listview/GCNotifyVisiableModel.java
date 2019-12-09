package com.gc.listview;

public class GCNotifyVisiableModel {
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public int getStartY() {
    return startY;
  }

  public void setStartY(int startY) {
    this.startY = startY;
  }

  public int getEndY() {
    return endY;
  }

  public void setEndY(int endY) {
    this.endY = endY;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  private int index;
  private String key;
  private int startY;
  private int endY;
  private String category;

  public GCNotifyVisiableModel(int index, String key, int startY, int endY, String category) {
    this.index = index;
    this.key = key;
    this.startY = startY;
    this.endY = endY;
    this.category = category;
  }
}
