package ru.study.neuralnetworks.neurons;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/22/13
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class HopfieldNeuron extends Neuron {
    private double state = 1;

    public HopfieldNeuron(int requiredInputs) {
        super(NEURON_TYPE_FINAL, requiredInputs);
    }

    public HopfieldNeuron(int requiredInputs, String label) {
        super(NEURON_TYPE_FINAL, requiredInputs, label);
    }

    @Override
    protected double processInputs(double sum) {
        return state * sum;
    }

    public void setState(double val) {
        state = val;
    }

    public void invertState() {
        state = state * -1;
    }

    public double getState() {
        return state;
    }
}
