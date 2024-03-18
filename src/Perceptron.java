public class Perceptron {
    double trigger = 0.0;
    double[] weights;

    public Perceptron(int numOfInputs) {
        weights = new double[numOfInputs];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1;
        }
    }

    public void adjust(double[] przyklad,int delta,double alfa){
        double factor = delta * alfa;
        for (int i = 0; i < przyklad.length; i++) {
            przyklad[i] = przyklad[i] * factor;
        }
        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] + przyklad[i];
        }
        trigger = trigger + ((delta*(-1))*alfa);
        for (double x:weights) {
            System.out.print(x+" ");
        }
        System.out.println(trigger);
    }

    public boolean classify(double[] item){
        double sum = 0;
        for (int i = 0; i < item.length; i++) {
            sum += (item[i] * weights[i]);
        }
        if(sum >= trigger){
            return true;
        }else{
            return false;
        }
    }

    public boolean classify(Test test){
        double[] tab = new double[Main.columnCount];

        for (int j = 0; j < tab.length; j++) {
            tab[j] = test.getMeasurementAt(j);
        }

        return classify(tab);
    }
}
