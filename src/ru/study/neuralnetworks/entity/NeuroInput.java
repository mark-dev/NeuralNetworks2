package ru.study.neuralnetworks.entity;

import ru.study.neuralnetworks.neurons.Neuron;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class NeuroInput {
    private Double value;
    private Double weight;

    public NeuroInput(Neuron from, NeuronEdge edge) {
        this.value = from.getOut();
        this.weight = edge.getWeight();
    }
    public NeuroInput(double value, double weight){
        this.value = value;
        this.weight = weight;
    }



    public Double getValue() {
        return value;
    }

    public Double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "NeuroInput{" +
                "value=" + value +
                ", weight=" + weight +
                '}';
    }
}
