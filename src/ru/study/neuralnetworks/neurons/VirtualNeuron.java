package ru.study.neuralnetworks.neurons;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class VirtualNeuron extends Neuron {
    private double outValue;

    public VirtualNeuron(double outValue) {
        super(Neuron.NEURON_TYPE_VIRTUAL, 0);
        this.outValue = outValue;
    }

    public VirtualNeuron() {
        super(NEURON_TYPE_FINAL, 0);
    }


    public VirtualNeuron(double outValue, String label) {
        super(Neuron.NEURON_TYPE_VIRTUAL, 0, label);
        this.outValue = outValue;
    }

    @Override
    protected double processInputs(double sum) {
        if (sum < 0.5)
            return 0;
        else
            return 1;
    }

    @Override
    public boolean hasOut() {
        return true;
    }

    @Override
    public Double getOut() {
        return outValue;
    }

    public void setOutValue(double outValue) {
        this.outValue = outValue;
    }
}
