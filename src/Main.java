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
        File inputFile = new File("mpp1/iris_training.txt");
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

        System.out.println("system gotowy, proszę podać K - liczbę sąsiadów");
        Scanner uzytkownikIn = new Scanner(System.in);
        int k = uzytkownikIn.nextInt();

        boolean readTestFile = true;

        System.out.println("chcesz sprawdzić dane z pliku, czy wprowadzić własne? \n Własne dane = 0, Czytanie z pliku = inna liczba");
        readTestFile = (uzytkownikIn.nextInt() != 0);
        if(!readTestFile){
            System.out.println("podaj teraz "+ columnCount +" wartości, zgodnie z tym jak system był trenowany, oddziel je enterem (0.0 ; nie 0,0)");
            double[] tmpData = new double[columnCount];
            for (int i = 0; i < columnCount; i++) {
                 tmpData[i] = uzytkownikIn.nextDouble();
            }

            Test testUzytkownika = new Test(tmpData,"testUzytkownika");

            System.out.println(finalAnswer(getNClosestTo(testUzytkownika,k)));
        }else{
            int all = 0;
            int good = 0;

            inputFile = new File("mpp1/iris_test.txt");
            String rawData;
            Test tmpTest;

            try {
                Scanner input = new Scanner(inputFile);
                while(input.hasNextLine()){
                    rawData = input.nextLine();
                    all++;
                    tmpTest = createTest(rawData);
                    String finalName =finalAnswer(getNClosestTo(tmpTest,k));
                    if(finalName.equals(tmpTest.getConclusion())){
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

    private static String finalAnswer(Test[] testy){
        ArrayList<String> tmplist = new ArrayList<>();
        for (int i = 0; i < testy.length; i++) {
            if(!tmplist.contains(testy[i].getConclusion())){
                tmplist.add(testy[i].getConclusion());
            }
        }

        int[] counts = new int[tmplist.size()];

        String tmp;
        int count;

        for (int i = 0; i < tmplist.size(); i++) {
            count = 0;
            tmp = tmplist.get(i);
            for (int j = 0; j < testy.length; j++) {
                if(tmp.equals(testy[j].getConclusion())){
                    count++;
                }
            }
            counts[i] = count;
        }

        int highestCountIndex = 0;
        int highestCount = Integer.MIN_VALUE;
        for (int i = 0; i < counts.length; i++) {
            if(counts[i]>highestCount){
                highestCount = counts[i];
                highestCountIndex = i;
            }
        }

        for (int i = 0; i < tmplist.size(); i++) {
            System.out.println(tmplist.get(i) + "'s count: " + counts[i]);
        }


        return tmplist.get(highestCountIndex);
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

    private static Test[] getNClosestTo(Test test,int n){
        Pair[] pary = new Pair[tests.size()];
        Test tmpTest;
        for (int i = 0; i < pary.length; i++) {
            tmpTest = tests.get(i);
            pary[i] = new Pair(tmpTest.distanceFrom(test),tmpTest);
        }

        Arrays.sort(pary, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return (o1.getDistance()<o2.getDistance()?-1:(o1.getDistance()==o2.getDistance()?0:1));
            }
        });

        Test[] nClosest = new Test[n];

        for (int i = 0; i < nClosest.length; i++) {
            nClosest[i] = pary[i].getTest();
        }

        return nClosest;
    }
}
