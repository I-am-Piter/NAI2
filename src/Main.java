import javax.naming.Name;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    //W tej chwili skuteczność algorytmu jest bardzo wysoka na podanych danych, wynika to z tego,
    //że tablica testów która dochodzi do metody finalAnswer jest posortowana w kolejności niemalejącej po odległości.
    static ArrayList<Test> tests = new ArrayList<>();
    static int columnCount = 0;
    static boolean firstObject = true;
    public static void main(String[] args) {
        File inputFile = new File("mpp2/iris_training.txt");
        try {
            Scanner input = new Scanner(inputFile);
            while(input.hasNextLine()) {
                String entry = input.nextLine();
                try {
                    tests.add(createTest(entry));
                } catch (WrongFormattedDataError e) {
                    throw new RuntimeException(e);
                }
            }
            for(Test t:tests){
                System.out.println(t);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Scanner uzytkownikIn = new Scanner(System.in);

        boolean readTestFile = true;

        //trenowanie perceptronu

        Perceptron perceptron = new Perceptron(columnCount);

        boolean stillTrain = true;
        Test tmpTest = null;
        int iterNum = 0;
        double[] dataTab = new double[columnCount];
        boolean isSetosa = false;

        while(stillTrain){
            stillTrain = false;
            iterNum++;
            for (int i = 0; i < tests.size(); i++) {
                tmpTest = tests.get(i);
                for (int j = 0; j < dataTab.length; j++) {
                    dataTab[j] = tmpTest.getMeasurementAt(j);
                }
                isSetosa = perceptron.classify(dataTab);
                if(isSetosa && (!tmpTest.getConclusion().equals("Iris-setosa"))){
                    perceptron.adjust(dataTab,-1,0.5);
                    stillTrain = true;
                } else if (!isSetosa && (tmpTest.getConclusion().equals("Iris-setosa"))) {
                    perceptron.adjust(dataTab,1,0.5);
                    stillTrain = true;
                }
            }
        }



        System.out.println("chcesz sprawdzić dane z pliku, czy wprowadzić własne? \n Własne dane = 0, Czytanie z pliku = inna liczba");
        readTestFile = (uzytkownikIn.nextInt() != 0);
        boolean stillask = true;
        if(!readTestFile){
            while(stillask) {
                System.out.println("podaj teraz " + columnCount + " wartości, zgodnie z tym jak system był trenowany, oddziel je enterem (0,0 ; nie 0.0)");
                double[] tmpData = new double[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    tmpData[i] = uzytkownikIn.nextDouble();
                }

                Test testUzytkownika = new Test(tmpData, "testUzytkownika");
                System.out.println((perceptron.classify(testUzytkownika)) ? "to setosa" : "to nie setosa");
                System.out.println("chcesz wprowadzić kolejny przypadek, czy zakończyć działanie programu? (aby zakończyć wpisz 0, aby kontynuować wpisz inną liczbę)");

                stillask = (uzytkownikIn.nextInt() != 0);
            }
        }else{
            int all = 0;
            int good = 0;

            inputFile = new File("mpp2/iris_test.txt");
            String rawData;

            try {
                Scanner input = new Scanner(inputFile);
                while(input.hasNextLine()){
                    rawData = input.nextLine();
                    all++;
                    tmpTest = createTest(rawData);
                    isSetosa = perceptron.classify(tmpTest);
                    if((isSetosa && tmpTest.getConclusion().equals("Iris-setosa"))||(!isSetosa && !tmpTest.getConclusion().equals("Iris-setosa"))) {
                        good++;
                    }
                }

                double effectivness = ((double)good/all)*100;
                System.out.println("prawidłowo przydzielonych zostało "+good+" pozycji, tworzy to skuteczność "+effectivness+"%.");

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (WrongFormattedDataError e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static Test createTest(String entry) throws WrongFormattedDataError {
        entry = entry.replace(" ","");
        entry = entry.replace(',','.');
        String[] data = entry.split("\t");
        String name = data[data.length-1];
        double[] values = new double[data.length-1];
        if(firstObject){
            columnCount = values.length;
            firstObject = false;
        }else{
            if(columnCount != values.length){
                throw new WrongFormattedDataError("Dane są źle sformatowane, popraw je!");
            }
        }

        for (int i = 0; i < values.length; i++) {
            values[i] = Double.parseDouble(data[i]);
        }

        Test test = new Test(values,name);
        return test;
    }

}
