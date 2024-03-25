import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class IOtesting {

    public static int countCategory(String filename) {

        int count = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line;
            while ((line = reader.readLine()) != null) {
                count++;
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        return count;
    }

    public static void getCategory(String[] category, int[] quantity, int[] weight, String filename) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\s+");
                category[count] = parts[0].trim();
                quantity[count] = Integer.parseInt(parts[1].trim());
                weight[count] = Integer.parseInt(parts[2].trim());
                count++;
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public static void getScores(String[] names, double[] scores, String filename) {

        try (Scanner scanner = new Scanner(new File(filename))) {
            int count = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\s+");
                names[count] = parts[0];
                scores[count] = Double.parseDouble(parts[1]);
                count++;
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public static void writeGrades(String[] student, double[] grade, String studentGrades, String errorLog) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(studentGrades));
            BufferedWriter errorwriter = new BufferedWriter(new FileWriter(errorLog));
            for (int i = 0; i < grade.length; i++) {
                if (grade[i] != 0) {
                    double a = grade[i];
                    writer.write(
                            student[i] + " " + a + " " + gradeLetter(a) + " " + gpaPoints(a) + " " + status(a) + "\n");
                } else {
                    errorwriter.write("ERROR: Student " + student[i] +
                            " - cannot calculate due to invalid grade entered\n");
                }
            }
            writer.close();
            errorwriter.close();
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public static boolean quantitycheck(int[] quantities) {

        boolean a = true;
        for (int i = 0; i < quantities.length; i++) {
            if (quantities[i] <= 0)
                a = false;
        }
        return a;
    }

    public static boolean weightCheck(int[] weights) {

        int sum = 0;
        for (int weight : weights) {
            if (weight <= 0 || weight > 100)
                return false;
            sum += weight;
        }
        return sum == 100;
    }

    public static boolean weightsumcheck(int[] weights) {

        boolean a = true;
        int b = 0;
        for (int i = 0; i < weights.length; i++) {
            b += weights[i];
        }
        if (b != 100) {
            a = false;
        }
        return a;
    }

    public static void studentnotecheck(boolean[] array, double[][] notes) {

        for (int i = 0; i < notes.length; i++) {
            boolean a = true;
            for (int j = 0; j < notes[i].length; j++) {
                if (notes[i][j] > 100 || notes[i][j] < 0) {
                    a = false;
                }
            }
            array[i] = a;
        }
    }

    public static double[] gradecalculator(double[][] scores, int[] quantities, int[] weights) {

        double[] grades = new double[scores.length];

        for (int i = 0; i < grades.length; i++) {
            Double[] sums = new Double[quantities.length];
            int count = 0;
            int count2 = 0;
            while (count < quantities.length) {
                double sum = 0;
                for (int j = 0; j < quantities[count]; j++) {
                    sum += scores[i][count2++];
                }
                sums[count] = sum / quantities[count];
                count++;
            }
            double grade = 0;
            for (int j = 0; j < sums.length; j++) {
                grade += sums[j] * weights[j] / 100;
            }
            grades[i] = grade;
        }
        return grades;
    }

    public static void main(String[] args) {

        String file1, file2, file3, file4;

        if (args.length == 1) {

            file1 = args[0] + "_CourseDetails.txt";
            file2 = args[0] + "_StudentScores.txt";
            file3 = args[0] + "_StudentGrades.txt";
            file4 = args[0] + "_Errors.log";

            int catcount = countCategory(file1);

            String[] cats = new String[catcount];
            int[] quantities = new int[catcount];
            int[] weights = new int[catcount];

            getCategory(cats, quantities, weights, file1);

            boolean quanch = quantitycheck(quantities);
            boolean weightsumcheck = weightsumcheck(weights);
            boolean weightnegcheck = weightCheck(weights);

            if (quanch && weightnegcheck && weightsumcheck) {
                int sumquan = 0;
                for (int i = 0; i < quantities.length; i++) {
                    sumquan += quantities[i];
                }

                int studentcount = countCategory(file2);

                String[] Students = new String[studentcount];
                double[][] Studentscores = new double[studentcount][sumquan];
                double[] Studentsco = new double[sumquan * studentcount];

                getScores(Students, Studentsco, file2);

                int a = 0;
                for (int i = 0; i < Studentscores.length; i++) {
                    for (int j = 0; j < Studentscores[i].length; j++) {
                        Studentscores[i][j] = Studentsco[a++];
                    }
                }

                boolean[] notesvalid = new boolean[studentcount];
                studentnotecheck(notesvalid, Studentscores);
                double[] grades = new double[studentcount];

                grades = gradecalculator(Studentscores, quantities, weights);

                for (int i = 0; i < grades.length; i++) {
                    if (notesvalid[i] == false)
                        grades[i] = 0;
                }
                writeGrades(Students, grades, file3, file4);
            } else {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file4));
                    if (quanch == false) {
                        writer.write("ERROR: Course details - invalid quantity" +
                                " - negative value\n");
                    }
                    if (weightnegcheck == false) {
                        writer.write("ERROR: Course details - invalid weight" +
                                " - negative value\n");
                    }
                    if (weightsumcheck == false) {
                        writer.write("ERROR: Course details - invalid weight" +
                                " - does not sum to 100\n");
                    }
                    writer.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (args.length == 4) {

            file1 = args[0] + ".txt";
            file2 = args[1] + ".txt";
            file3 = args[2] + ".txt";
            file4 = args[3] + ".log";

            int catcount = countCategory(file1);
            String[] cats = new String[catcount];
            int[] quantities = new int[catcount];
            int[] weights = new int[catcount];

            getCategory(cats, quantities, weights, file1);

            boolean quanch = quantitycheck(quantities);
            boolean weightsumcheck = weightsumcheck(weights);
            boolean weightnegcheck = weightCheck(weights);

            if (quanch && weightnegcheck && weightsumcheck) {
                int sumquan = 0;
                for (int i = 0; i < quantities.length; i++) {
                    sumquan += quantities[i];
                }

                int studentcount = countCategory(file2);

                String[] Students = new String[studentcount];
                double[][] Studentscores = new double[studentcount][sumquan];
                double[] Studentsco = new double[sumquan * studentcount];

                getScores(Students, Studentsco, file2);
                int a = 0;
                for (int i = 0; i < Studentscores.length; i++) {
                    for (int j = 0; j < Studentscores[i].length; j++) {
                        Studentscores[i][j] = Studentsco[a++];
                    }
                }

                boolean[] notesvalid = new boolean[studentcount];
                studentnotecheck(notesvalid, Studentscores);
                double[] grades = new double[studentcount];

                grades = gradecalculator(Studentscores, quantities, weights);
                for (int i = 0; i < grades.length; i++) {
                    if (notesvalid[i] == false)
                        grades[i] = 0;
                }
                writeGrades(Students, grades, file3, file4);
            } else {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file4));
                    if (quanch == false) {
                        writer.write("ERROR: Course details - invalid quantity" +
                                " - negative value\n");
                    }
                    if (weightnegcheck == false) {
                        writer.write("ERROR: Course details - invalid weight" +
                                " - negative value\n");
                    }
                    if (weightsumcheck == false) {
                        writer.write("ERROR: Course details - invalid weight" +
                                " - does not sum to 100\n");
                    }
                    writer.close();
                } catch (IOException e) {
                }
            }
        } else {
            System.out.println("ERROR: Invalid arguments for file names.");
        }
    }

    public static boolean isQuantityValid(int quantity) {

        if (quantity > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isWeightValid(int weight, int totalWeight) {

        if (weight < 0 || totalWeight > 100) {
            return false;
        } else {
            return true;
        }
    }

    public static String gradeLetter(double grade) {

        if (grade >= 88)
            return "AA";
        else if (grade >= 81)
            return "BA";
        else if (grade >= 74)
            return "BB";
        else if (grade >= 67)
            return "CB";
        else if (grade >= 60)
            return "CC";
        else if (grade >= 53)
            return "DC";
        else if (grade >= 46)
            return "DD";
        else if (grade >= 35)
            return "FD";
        else
            return "FF";
    }

    public static double gpaPoints(double gpa) {

        if (gpa >= 88)
            return 4.0;
        else if (gpa >= 81)
            return 3.5;
        else if (gpa >= 74)
            return 3.0;
        else if (gpa >= 67)
            return 2.5;
        else if (gpa >= 60)
            return 2.0;
        else if (gpa >= 53)
            return 1.5;
        else if (gpa >= 46)
            return 1.0;
        else if (gpa >= 35)
            return 0.5;
        else
            return 0.0;
    }

    public static String status(double grade) {

        if (grade >= 60)
            return "passed";
        if (grade >= 46)
            return "conditionally passed";
        else
            return "failed";
    }
}