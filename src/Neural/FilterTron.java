package Neural;

import Neural.autodiff.Graph;
import Neural.matrix.Matrix;
import Neural.model.SigmoidUnit;

import java.util.Random;

/**
 * Created by Federica on 21/05/2018.
 */
public class FilterTron {

    public Matrix filter;
    public final float LEARNING_RATE = 1.0f;

    public FilterTron() {
        filter = Matrix.rand(1,6,1,new Random());
        Graph graph = new Graph();
        try {
            filter = graph.nonlin(new SigmoidUnit(), filter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int nonRandomGuess(double[] inputs) {
        double sum = 0;
        for (int i = 0; i < 6; i++) {
            sum += inputs[i] * filter.getW(0, i);
        }

        int output = activationFunction(sum);
        return output;
    }

    private int activationFunction(double sum) {
        if (sum >= 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public void train(double[] inputs, int target) {
        int guess = nonRandomGuess(inputs);
        int error = target - guess;

        for (int i = 0; i < 6; i++) {
            filter.setW(0, i, filter.getW(0 , i) + (error * inputs[i] * LEARNING_RATE));
        }
    }
}
