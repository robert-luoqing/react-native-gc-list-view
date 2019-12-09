package com.gc.listview;

public class GCJSPassModel {
  private int offset;
  private String category;
  private String key;

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public GCJSPassModel(int offset, String key, String category) {
    this.offset = offset;
    this.key = key;
    this.category = category;
  }
}
