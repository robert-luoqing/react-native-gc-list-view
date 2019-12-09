package com.gc.listview;

public class GCNotifyVisiableModel {
  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
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

  private int index;
  private int startY;
  private int endY;
  private String category;

  public GCNotifyVisiableModel(int index, int startY, int endY, String category) {
    this.index = index;
    this.startY = startY;
    this.endY = endY;
    this.category = category;
  }
}
