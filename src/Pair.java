public class Pair {
    private Double distance;
    private Test test;

    public Pair(Double distance, Test test) {
        this.distance = distance;
        this.test = test;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "distance=" + distance +
                ", test=" + test.toString() +
                '}';
    }
}
