package com.hlab.emotionanalysisapi.models;

public class FaceRectangle {

    private int left;
    private int top;
    private int width;
    private int height;

    /**
     * No args constructor for use in serialization
     *
     */
    public FaceRectangle() {
    }

    /**
     *
     * @param height
     * @param width
     * @param left
     * @param top
     */
    public FaceRectangle(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    /**
     *
     * @return
     *     The left
     */
    public int getLeft() {
        return left;
    }

    /**
     *
     * @param left
     *     The left
     */
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     *
     * @return
     *     The top
     */
    public int getTop() {
        return top;
    }

    /**
     *
     * @param top
     *     The top
     */
    public void setTop(int top) {
        this.top = top;
    }

    /**
     *
     * @return
     *     The width
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @param width
     *     The width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return
     *     The height
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @param height
     *     The height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
