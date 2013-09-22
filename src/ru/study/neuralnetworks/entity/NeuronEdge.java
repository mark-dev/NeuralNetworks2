package ru.study.neuralnetworks.entity;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class NeuronEdge {
    private double weight;

    public NeuronEdge(double weight) {

        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
