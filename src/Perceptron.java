public class Perceptron {
    double trigger = 0.0;
    double[] weights;

    public Perceptron(int numOfInputs) {
        weights = new double[numOfInputs];
    }
}
