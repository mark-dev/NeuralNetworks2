package ru.study.neuralnetworks.entity;

import Jama.Matrix;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/22/13
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class HopfieldImage {
    private Matrix img;
    private String description;

    public HopfieldImage(Matrix img, String description) {
        this.img = img;
        this.description = description;

    }

    public Matrix getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public void setImg(Matrix img) {
        this.img = img;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
