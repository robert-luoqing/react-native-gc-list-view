package com.GC;

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

    private int index;
    private int startY;
    private int endY;

    public GCNotifyVisiableModel(int index, int startY, int endY) {
        this.index = index;
        this.startY = startY;
        this.endY = endY;
    }
}
