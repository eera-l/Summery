package Neural;

import Neural.autodiff.Graph;
import Neural.matrix.Matrix;
import Neural.model.SigmoidUnit;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * Created by Federica on 21/05/2018.
 */
public class FilterTron {

    public Matrix filter;
    public final float LEARNING_RATE = 0.001f;

    public FilterTron() {
        loadFilter();
        if (filter==null){
            filter = Matrix.rand(1,6,1,new Random());
        }
        System.out.println(filter.toString());
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

    public void setFilter(Matrix filter){
        if (filter!=null) {
            this.filter = filter;
        }
    }

    private void loadFilter() {
        if (!Files.exists(Paths.get("Filter.mx"))){
            System.out.println("Filter file does not exist.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Filter.mx"))) {
            setFilter((Matrix) ois.readObject());
            System.out.println("Filter loaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void normalize(){
        Graph.normalize(filter);
    }

}
