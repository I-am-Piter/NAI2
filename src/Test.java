import java.util.ArrayList;

public class Test {
    private ArrayList<Double> measurementsList = new ArrayList<>();
    private String conclusion;

    public Test(double[] pomiary, String conclusion){
        this.conclusion = conclusion;
        for (int i = 0; i < pomiary.length; i++) {
            measurementsList.add(pomiary[i]);
        }
    }

    public double distanceFrom(Test otherOne){
        double distance = 0;
        for (int i = 0; i < measurementsList.size(); i++) {
            distance += Math.pow((this.measurementsList.get(i)-otherOne.getMeasurementAt(i)),2);
        }
        distance = Math.sqrt(distance);
        return distance;
    }

    public double getMeasurementAt(int i){
        return measurementsList.get(i);
    }

    public String getConclusion() {
        return conclusion;
    }

    @Override
    public String toString() {
        String toReturn = "";
        for (int i = 0; i < measurementsList.size(); i++) {
            toReturn+=(measurementsList.get(i)+" ");
        }
        toReturn += conclusion;
        return toReturn;
    }
}
